package com.raithumitra.ui.equipment;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.raithumitra.R;
import com.raithumitra.data.local.entity.Equipment;
import com.raithumitra.viewmodel.EquipmentViewModel;
import java.util.ArrayList;

public class EquipmentFragment extends Fragment {

    private EquipmentViewModel viewModel;
    private EquipmentAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_equipment, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_equipment); // Ensure XML has this ID
        // Grid Layout with 2 columns
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        adapter = new EquipmentAdapter();
        recyclerView.setAdapter(adapter);

        // Setup ViewModel
        viewModel = new ViewModelProvider(this).get(EquipmentViewModel.class);
        viewModel.getAllEquipment().observe(getViewLifecycleOwner(), equipmentList -> {
            adapter.setEquipmentList(equipmentList);
        });

        viewModel.getNetworkError().observe(getViewLifecycleOwner(), error -> {
            if (error != null) {
                Toast.makeText(getContext(), error, Toast.LENGTH_LONG).show();
            }
        });

        // Check User Role using SessionManager
        com.raithumitra.data.local.SessionManager sessionManager = new com.raithumitra.data.local.SessionManager(
                requireContext());
        String userRole = sessionManager.getUserRole();
        String userPhone = sessionManager.getUserPhone();

        adapter.setUserContext(userRole, userPhone);

        com.google.android.material.floatingactionbutton.FloatingActionButton fab = view
                .findViewById(R.id.fabAddEquipment);
        if ("OWNER".equals(userRole) || "EQUIPMENT_OWNER".equals(userRole)) {
            fab.setVisibility(View.VISIBLE);
            fab.setOnClickListener(v -> {
                startActivity(new android.content.Intent(getContext(), AddEquipmentActivity.class));
            });

        }

        // Handle Booking/Delete Interaction
        adapter.setOnEquipmentClickListener(new EquipmentAdapter.OnEquipmentClickListener() {
            @Override
            public void onBookClick(Equipment equipment) {
                showBookingDialog(equipment);
            }

            @Override
            public void onDeleteClick(Equipment equipment) {
                new android.app.AlertDialog.Builder(getContext())
                        .setTitle("పరికరం తొలగించండి (Delete Equipment)")
                        .setMessage("మీరు " + equipment.equipmentName
                                + " ని తొలగించాలనుకుంటున్నారా? (Are you sure you want to delete "
                                + equipment.equipmentName + "?)")
                        .setPositiveButton("అవును (Yes)", (dialog, which) -> {
                            viewModel.deleteEquipment(equipment.equipmentId,
                                    new com.raithumitra.data.repository.EquipmentRepository.RepositoryCallback() {
                                        @Override
                                        public void onSuccess() {
                                            if (getActivity() != null) {
                                                getActivity().runOnUiThread(() -> Toast.makeText(getContext(),
                                                        "విజయవంతంగా తొలగించబడింది (Deleted Successfully)",
                                                        Toast.LENGTH_SHORT).show());
                                            }
                                        }

                                        @Override
                                        public void onError(String message) {
                                            if (getActivity() != null) {
                                                getActivity().runOnUiThread(() -> Toast
                                                        .makeText(getContext(), "లోపం (Error): " + message,
                                                                Toast.LENGTH_SHORT)
                                                        .show());
                                            }
                                        }
                                    });
                        })
                        .setNegativeButton("వద్దు (No)", null)
                        .show();
            }
        });

        return view;
    }

    private void showBookingDialog(Equipment equipment) {
        Dialog dialog = new Dialog(requireContext());
        dialog.setContentView(R.layout.dialog_booking);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        CalendarView calendarView = dialog.findViewById(R.id.calendarView);
        Button btnConfirm = dialog.findViewById(R.id.btn_confirm_booking);

        btnConfirm.setOnClickListener(v -> {
            // Format Date to YYYY-MM-DD for Backend LocalDate
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.US);
            String dateStr = sdf.format(new java.util.Date(calendarView.getDate()));

            com.raithumitra.data.local.entity.EquipmentBooking booking = new com.raithumitra.data.local.entity.EquipmentBooking(
                    equipment.equipmentId,
                    dateStr);

            com.raithumitra.data.repository.EquipmentRepository repo = new com.raithumitra.data.repository.EquipmentRepository(
                    requireActivity().getApplication());
            repo.bookEquipment(booking, new com.raithumitra.data.repository.EquipmentRepository.RepositoryCallback() {
                @Override
                public void onSuccess() {
                    if (getActivity() != null) {
                        getActivity().runOnUiThread(() -> {
                            Toast.makeText(getContext(),
                                    equipment.equipmentName + " కి బుకింగ్ నిర్ధారణ (Booking Confirmed for: "
                                            + equipment.equipmentName + ")",
                                    Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        });
                    }
                }

                @Override
                public void onError(String message) {
                    if (getActivity() != null) {
                        getActivity().runOnUiThread(
                                () -> Toast.makeText(getContext(), "లోపం (Error): " + message, Toast.LENGTH_SHORT)
                                        .show());
                    }
                }
            });
        });

        dialog.show();
    }
}
