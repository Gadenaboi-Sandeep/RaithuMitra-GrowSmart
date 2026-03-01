package com.raithumitra.ui.contract;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.raithumitra.R;
import com.raithumitra.data.local.SessionManager;
import com.raithumitra.data.local.entity.Contract;
import com.raithumitra.data.repository.ContractRepository;

public class CreateContractActivity extends AppCompatActivity {

    private TextInputEditText etCrop, etPrice, etQuantity, etLocation, etTerms;
    private MaterialButton btnCreate;
    private ContractRepository repository;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_contract);

        repository = new ContractRepository(getApplication());
        sessionManager = new SessionManager(this);

        etCrop = findViewById(R.id.etCropName);
        etPrice = findViewById(R.id.etPrice);
        etQuantity = findViewById(R.id.etQuantity);
        etLocation = findViewById(R.id.etLocation);
        etTerms = findViewById(R.id.etTerms);
        btnCreate = findViewById(R.id.btnCreateContract);

        btnCreate.setOnClickListener(v -> createContract());
    }

    private void createContract() {
        String crop = etCrop.getText().toString().trim();
        String price = etPrice.getText().toString().trim();
        String quantityStr = etQuantity.getText().toString().trim();
        String location = etLocation.getText().toString().trim();
        String terms = etTerms.getText().toString().trim();

        if (crop.isEmpty() || price.isEmpty() || quantityStr.isEmpty()) {
            Toast.makeText(this, "దయచేసి అన్ని అవసరమైన ఫీల్డ్‌లు నింపండి (Please fill all required fields)",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        Contract contract = new Contract(
                crop,
                price.replaceAll("[^\\d.]", ""), // Sanitize Number
                Integer.parseInt(quantityStr),
                terms.isEmpty() ? "సాధారణ నిబంధనలు (Standard Terms)" : terms,
                location);

        contract.companyName = sessionManager.getUserName();

        repository.createContract(contract, new ContractRepository.RepositoryCallback() {
            @Override
            public void onSuccess() {
                runOnUiThread(() -> {
                    Toast.makeText(CreateContractActivity.this, "ఒప్పందం ప్రచురించబడింది! (Contract Published!)",
                            Toast.LENGTH_SHORT).show();
                    finish();
                });
            }

            @Override
            public void onError(String message) {
                runOnUiThread(() -> Toast
                        .makeText(CreateContractActivity.this, "లోపం (Error): " + message, Toast.LENGTH_SHORT)
                        .show());
            }
        });
    }
}
