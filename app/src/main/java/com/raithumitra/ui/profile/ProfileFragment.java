package com.raithumitra.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.raithumitra.R;
import com.raithumitra.data.local.SessionManager;

public class ProfileFragment extends Fragment {

    private TextInputEditText etName, etPhone, etAddress;
    private MaterialButton btnSave;
    private SessionManager sessionManager;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        sessionManager = new SessionManager(requireContext());

        etName = view.findViewById(R.id.et_profile_name);
        etPhone = view.findViewById(R.id.et_profile_phone);
        etAddress = view.findViewById(R.id.et_profile_address);
        btnSave = view.findViewById(R.id.btn_save_profile);

        // Load Data
        etName.setText(sessionManager.getUserName());
        etPhone.setText(sessionManager.getMobileNumber()); // Assuming added to SessionManager
        // etAddress.setText(sessionManager.getAddress()); // Need to add to
        // SessionManager

        btnSave.setOnClickListener(v -> {
            String newName = etName.getText().toString();
            String newAddress = etAddress.getText().toString();

            // Save to Session (Mock implementation for now, should call API)
            // sessionManager.saveProfile(newName, newAddress);
            Toast.makeText(getContext(), "ప్రొఫైల్ అప్‌డేట్ అయింది (లోకల్) (Profile Updated (Local))",
                    Toast.LENGTH_SHORT).show();
            // In real app, call Repository -> API
        });

        return view;
    }
}
