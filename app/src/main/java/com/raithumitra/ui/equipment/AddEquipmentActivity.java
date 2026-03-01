package com.raithumitra.ui.equipment;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.raithumitra.R;
import com.raithumitra.data.local.SessionManager;
import com.raithumitra.data.local.entity.Equipment;
import com.raithumitra.data.repository.EquipmentRepository;

public class AddEquipmentActivity extends AppCompatActivity {

    private android.widget.ImageView ivImage;
    private android.net.Uri selectedImageUri;
    private boolean isImageVerified = false;

    private TextInputEditText etName, etType, etRate, etLocation;
    private MaterialButton btnAdd;
    private EquipmentRepository repository;
    private SessionManager sessionManager;

    private final androidx.activity.result.ActivityResultLauncher<String> pickImageLauncher = registerForActivityResult(
            new androidx.activity.result.contract.ActivityResultContracts.GetContent(),
            uri -> {
                if (uri != null) {
                    selectedImageUri = uri;
                    ivImage.setVisibility(android.view.View.VISIBLE);
                    ivImage.setImageURI(uri);
                    verifyImage(uri);
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_equipment);

        repository = new EquipmentRepository(getApplication());
        sessionManager = new SessionManager(this);

        etName = findViewById(R.id.etEqName);
        etType = findViewById(R.id.etEqType);
        etRate = findViewById(R.id.etEqRate);
        etLocation = findViewById(R.id.etEqLocation);
        btnAdd = findViewById(R.id.btnAddEquipment);
        ivImage = findViewById(R.id.ivEquipmentImage);

        MaterialButton btnSelectImage = findViewById(R.id.btnSelectImage);
        btnSelectImage.setOnClickListener(v -> pickImageLauncher.launch("image/*"));

        btnAdd.setOnClickListener(v -> addEquipment());
    }

    private void verifyImage(android.net.Uri uri) {
        String type = etType.getText().toString().trim();
        if (type.isEmpty()) {
            Toast.makeText(this, "దయచేసి పరికరం రకం నమోదు చేయండి (Please enter Equipment Type first e.g., Tractor)",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        Toast.makeText(this, "AI తో చిత్రం ధృవీకరిస్తున్నాం... (Verifying Image with AI...)", Toast.LENGTH_SHORT)
                .show();
        com.raithumitra.utils.ImageVerifier.verifyImage(this, uri, type,
                new com.raithumitra.utils.ImageVerifier.VerificationCallback() {
                    @Override
                    public void onSuccess() {
                        isImageVerified = true;
                        Toast.makeText(AddEquipmentActivity.this, "చిత్రం ధృవీకరించబడింది (Image Verified): " + type,
                                Toast.LENGTH_SHORT)
                                .show();
                    }

                    @Override
                    public void onFailure(String reason) {
                        isImageVerified = false;
                        Toast.makeText(AddEquipmentActivity.this, "లోపం (Error): " + reason, Toast.LENGTH_LONG).show();
                        ivImage.setImageURI(null); // Clear invalid image
                        ivImage.setVisibility(android.view.View.GONE);
                        selectedImageUri = null;
                    }
                });
    }

    private void addEquipment() {
        String name = etName.getText().toString().trim();
        String type = etType.getText().toString().trim();
        String rate = etRate.getText().toString().trim();
        String location = etLocation.getText().toString().trim();

        if (name.isEmpty() || type.isEmpty() || rate.isEmpty()) {
            Toast.makeText(this, "దయచేసి అన్ని అవసరమైన ఫీల్డ్‌లు నింపండి (Please fill all required fields)",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedImageUri == null || !isImageVerified) {
            Toast.makeText(this, "దయచేసి ధృవీకరించిన చిత్రం ఎంచుకోండి (Please select a valid verified image)",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        uploadImageAndSave(name, type, rate, location);
    }

    private void uploadImageAndSave(String name, String type, String rate, String location) {
        Toast.makeText(this, "చిత్రం అప్‌లోడ్ అవుతోంది... (Uploading Image...)", Toast.LENGTH_SHORT).show();

        try {
            java.io.InputStream inputStream = getContentResolver().openInputStream(selectedImageUri);
            java.io.ByteArrayOutputStream byteBuffer = new java.io.ByteArrayOutputStream();
            int bufferSize = 1024;
            byte[] buffer = new byte[bufferSize];

            int len = 0;
            while ((len = inputStream.read(buffer)) != -1) {
                byteBuffer.write(buffer, 0, len);
            }
            byte[] bytes = byteBuffer.toByteArray();

            okhttp3.RequestBody requestFile = okhttp3.RequestBody.create(okhttp3.MediaType.parse("image/*"), bytes);
            okhttp3.MultipartBody.Part body = okhttp3.MultipartBody.Part.createFormData("file", "image.jpg",
                    requestFile);

            repository.uploadImage(body, new EquipmentRepository.ImageUploadCallback() {
                @Override
                public void onSuccess(String imageUrl) {
                    saveEquipment(name, type, rate, location, imageUrl);
                }

                @Override
                public void onError(String message) {
                    Toast.makeText(AddEquipmentActivity.this, "అప్‌లోడ్ విఫలం (Upload Failed): " + message,
                            Toast.LENGTH_SHORT).show();
                }
            });

        } catch (Exception e) {
            Toast.makeText(this, "ఫైల్ లోపం (File Error): " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void saveEquipment(String name, String type, String rate, String location, String imageUrl) {
        Equipment equipment = new Equipment(
                name,
                type.toUpperCase(),
                rate.replaceAll("[^\\d.]", ""),
                sessionManager.getUserName(),
                location,
                sessionManager.getUserPhone());
        equipment.imageUrl = imageUrl;

        repository.addEquipment(equipment, new EquipmentRepository.RepositoryCallback() {
            @Override
            public void onSuccess() {
                runOnUiThread(() -> {
                    Toast.makeText(AddEquipmentActivity.this, "పరికరం జాబితాలో చేర్చబడింది! (Equipment Listed!)",
                            Toast.LENGTH_SHORT).show();
                    finish();
                });
            }

            @Override
            public void onError(String message) {
                runOnUiThread(
                        () -> Toast.makeText(AddEquipmentActivity.this, "లోపం (Error): " + message, Toast.LENGTH_SHORT)
                                .show());
            }
        });
    }
}
