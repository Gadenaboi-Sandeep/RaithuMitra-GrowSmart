package com.raithumitra.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.raithumitra.data.repository.AuthRepository;
import com.raithumitra.databinding.ActivityLoginBinding;
import com.raithumitra.ui.main.MainActivity;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private AuthRepository authRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        authRepository = new AuthRepository(this);

        binding.btnLogin.setOnClickListener(v -> {
            String phone = binding.etPhoneNumber.getText().toString();
            String pass = binding.etPassword.getText().toString();

            authRepository.login(phone, pass).observe(this, result -> {
                if ("SUCCESS".equals(result)) {
                    startActivity(new Intent(this, MainActivity.class));
                    finish();
                } else if (result.contains("User not found")) {
                    new android.app.AlertDialog.Builder(this)
                            .setTitle("లోపం (Error)")
                            .setMessage(
                                    "వినియోగదారు నమోదు కాలేదు. దయచేసి రిజిస్టర్ చేయండి. (User not registered. Please Sign up.)")
                            .setPositiveButton("రిజిస్టర్ (Sign Up)", (dialog, which) -> {
                                startActivity(new Intent(this, SignupActivity.class));
                            })
                            .setNegativeButton("మూసివేయండి (Close)", (dialog, which) -> dialog.dismiss())
                            .show();
                } else if (result.contains("Incorrect password")) {
                    android.view.animation.Animation shake = android.view.animation.AnimationUtils.loadAnimation(this,
                            com.raithumitra.R.anim.shake);
                    binding.etPassword.startAnimation(shake);
                    com.raithumitra.utils.DialogUtils.showErrorDialog(this, "తప్పు పాస్‌వర్డ్ (Incorrect Password)");
                } else {
                    com.raithumitra.utils.DialogUtils.showErrorDialog(this, result);
                }
            });
        });

        binding.btnGoToSignup.setOnClickListener(v -> {
            startActivity(new Intent(this, SignupActivity.class));
        });
    }
}
