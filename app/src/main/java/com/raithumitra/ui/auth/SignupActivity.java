package com.raithumitra.ui.auth;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import com.raithumitra.data.repository.AuthRepository;
import com.raithumitra.databinding.ActivitySignupBinding;

public class SignupActivity extends AppCompatActivity {

    private ActivitySignupBinding binding;
    private AuthRepository authRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        authRepository = new AuthRepository(this);

        // Setup Role Dropdown
        String[] roleLabels = new String[] { "రైతు (Farmer)", "కార్పొరేట్ (Corporate)", "కూలీ (Worker)",
                "యజమాని (Owner)" };
        String[] roleValues = new String[] { "FARMER", "CORPORATE", "WORKER", "OWNER" };
        android.widget.ArrayAdapter<String> adapter = new android.widget.ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, roleLabels);
        ((android.widget.AutoCompleteTextView) binding.etRole).setAdapter(adapter);

        binding.btnSignup.setOnClickListener(v -> {
            String name = binding.etFullName.getText().toString();
            String phone = binding.etSignupPhone.getText().toString();
            String roleLabel = binding.etRole.getText().toString();
            // Map display label back to backend value
            String role = "";
            for (int i = 0; i < roleLabels.length; i++) {
                if (roleLabels[i].equals(roleLabel)) {
                    role = roleValues[i];
                    break;
                }
            }
            String pass = binding.etSignupPassword.getText().toString();
            String confirmPass = binding.etConfirmPassword.getText().toString();
            String addr = binding.etAddress.getText().toString();

            if (role.isEmpty()) {
                com.raithumitra.utils.DialogUtils.showErrorDialog(this,
                        "దయచేసి పాత్ర ఎంచుకోండి! (Please select a Role!)");
                return;
            }

            if (!pass.equals(confirmPass)) {
                com.raithumitra.utils.DialogUtils.showErrorDialog(this,
                        "పాస్‌వర్డ్‌లు సరిపోలడం లేదు! (Passwords do not match!)");
                return;
            }

            authRepository.signup(name, phone, pass, role, addr).observe(this, result -> {
                if ("SUCCESS".equals(result)) {
                    com.raithumitra.utils.DialogHelper.showSuccessDialog(this,
                            "రిజిస్ట్రేషన్ విజయవంతం!",
                            "కొనసాగించడానికి లాగిన్ చేయండి.",
                            () -> {
                                finish();
                            });
                } else {
                    // Show raw error for debugging
                    com.raithumitra.utils.DialogHelper.showErrorDialog(this, "రిజిస్ట్రేషన్ విఫలం (Signup Failed)",
                            result != null ? result : "Unknown Error");
                }
            });
        });
    }
}
