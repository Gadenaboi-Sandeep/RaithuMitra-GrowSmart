package com.raithumitra.ui.labor;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.raithumitra.R;
import com.raithumitra.data.local.SessionManager;
import com.raithumitra.data.local.entity.Laborer;
import com.raithumitra.data.repository.LaborRepository;

public class CreateLaborProfileActivity extends AppCompatActivity {

    private TextInputEditText etSkill, etWage, etContact, etMemberCount;
    private MaterialButton btnSave;
    private LaborRepository repository;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_labor_profile);

        repository = new LaborRepository(getApplication());
        sessionManager = new SessionManager(this);

        etSkill = findViewById(R.id.etSkillType);
        etWage = findViewById(R.id.etDailyWage);
        etContact = findViewById(R.id.etContactNumber);
        etMemberCount = findViewById(R.id.etMemberCount);
        btnSave = findViewById(R.id.btnSaveProfile);

        // Pre-fill contact if available from session? Currently not stored.
        // But we can set Name from session.

        btnSave.setOnClickListener(v -> saveProfile());
    }

    private void saveProfile() {
        String skill = etSkill.getText().toString().trim();
        String wage = etWage.getText().toString().trim();
        String contact = etContact.getText().toString().trim();
        String countStr = etMemberCount.getText().toString().trim();

        if (skill.isEmpty() || wage.isEmpty() || contact.isEmpty()) {
            Toast.makeText(this, "దయచేసి అన్ని ఫీల్డ్‌లు నింపండి (Please fill all fields)", Toast.LENGTH_SHORT).show();
            return;
        }

        int memberCount = 1;
        try {
            memberCount = Integer.parseInt(countStr);
            if (memberCount < 1)
                memberCount = 1;
        } catch (NumberFormatException e) {
            memberCount = 1;
        }

        // Validate and Format
        // Backend expects Laborer object.
        // User is set in Controller based on Token, but we need it for local entity if
        // we saved locally (we don't here).
        // We create a transient User object for the Laborer entity constructor.
        com.raithumitra.data.local.entity.User user = new com.raithumitra.data.local.entity.User(
                0, // ID unknown
                sessionManager.getUserName(),
                sessionManager.getUserRole(),
                contact,
                "Local" // Address
        );

        Laborer laborer = new Laborer(
                user,
                skill,
                "₹" + wage + "/రోజు (/day)",
                4 // Default Rating (int)
        );
        laborer.memberCount = memberCount;

        repository.createLaborerProfile(laborer, new LaborRepository.RepositoryCallback() {
            @Override
            public void onSuccess() {
                runOnUiThread(() -> {
                    Toast.makeText(CreateLaborProfileActivity.this, "ప్రొఫైల్ ప్రచురించబడింది! (Profile Published!)",
                            Toast.LENGTH_SHORT).show();
                    finish();
                });
            }

            @Override
            public void onError(String message) {
                runOnUiThread(() -> Toast
                        .makeText(CreateLaborProfileActivity.this, "లోపం (Error): " + message, Toast.LENGTH_SHORT)
                        .show());
            }
        });
    }
}
