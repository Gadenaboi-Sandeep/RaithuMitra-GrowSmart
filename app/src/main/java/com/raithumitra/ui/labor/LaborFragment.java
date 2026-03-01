package com.raithumitra.ui.labor;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.raithumitra.R;
import com.raithumitra.data.local.entity.Laborer;
import com.raithumitra.viewmodel.LaborViewModel;

public class LaborFragment extends Fragment {

    private LaborViewModel viewModel;
    private LaborAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_labor, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_labor);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new LaborAdapter();
        recyclerView.setAdapter(adapter);

        // Setup ViewModel
        viewModel = new ViewModelProvider(this).get(LaborViewModel.class);
        viewModel.getAllLaborers().observe(getViewLifecycleOwner(), laborers -> {
            adapter.setLaborList(laborers);
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

        // Setup FAB for Worker Role
        com.google.android.material.floatingactionbutton.FloatingActionButton fab = view
                .findViewById(R.id.fabCreateProfile);

        if ("WORKER".equals(userRole)) {
            fab.setVisibility(View.VISIBLE);
            fab.setOnClickListener(v -> {
                startActivity(new Intent(getContext(), CreateLaborProfileActivity.class));
            });
        }

        adapter.setOnLaborClickListener(new LaborAdapter.OnLaborClickListener() {
            @Override
            public void onCallClick(Laborer laborer) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                if (laborer.user != null) {
                    intent.setData(Uri.parse("tel:" + laborer.user.phoneNumber));
                    startActivity(intent);
                } else {
                    Toast.makeText(getContext(), "నంబర్ అందుబాటులో లేదు (Number not available)", Toast.LENGTH_SHORT)
                            .show();
                }
            }

            @Override
            public void onHireClick(Laborer laborer) {
                showHiringDialog(laborer);
            }

            @Override
            public void onEditClick(Laborer laborer) {
                startActivity(new Intent(getContext(), CreateLaborProfileActivity.class));
            }

            @Override
            public void onDeleteClick(Laborer laborer) {
                Toast.makeText(getContext(), "తొలగించు ఫీచర్ త్వరలో వస్తుంది (Delete Feature Coming Soon)",
                        Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private void showHiringDialog(Laborer laborer) {
        android.app.Dialog dialog = new android.app.Dialog(requireContext());
        dialog.setContentView(R.layout.dialog_booking);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        android.widget.TextView tvTitle = dialog.findViewById(R.id.tv_booking_title);
        tvTitle.setText("నియమించు (Hire) " + (laborer.user != null ? laborer.user.fullName : "కూలీ (Worker)"));

        android.widget.CalendarView calendarView = dialog.findViewById(R.id.calendarView);
        android.widget.Button btnConfirm = dialog.findViewById(R.id.btn_confirm_booking);
        btnConfirm.setText("నియామకం నిర్ధారించండి (Confirm Hiring)");

        btnConfirm.setOnClickListener(v -> {
            // Format Date to YYYY-MM-DD
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.US);
            String dateStr = sdf.format(new java.util.Date(calendarView.getDate()));

            com.raithumitra.data.repository.LaborRepository repo = new com.raithumitra.data.repository.LaborRepository(
                    requireActivity().getApplication());

            com.raithumitra.data.local.entity.LaborHiring hiring = new com.raithumitra.data.local.entity.LaborHiring(
                    laborer.laborId, dateStr);

            repo.hireLaborer(hiring, new com.raithumitra.data.repository.LaborRepository.RepositoryCallback() {
                @Override
                public void onSuccess() {
                    if (getActivity() != null) {
                        String name = laborer.user != null ? laborer.user.fullName : "కూలీ (Worker)";
                        getActivity().runOnUiThread(() -> {
                            Toast.makeText(getContext(),
                                    "నియామక అభ్యర్థన పంపబడింది (Hiring Request Sent for) " + dateStr,
                                    Toast.LENGTH_SHORT)
                                    .show();
                            dialog.dismiss();
                        });
                    }
                }

                @Override
                public void onError(String message) {
                    if (getActivity() != null) {
                        getActivity().runOnUiThread(() -> Toast
                                .makeText(getContext(), "లోపం (Error): " + message, Toast.LENGTH_SHORT).show());
                    }
                }
            });
        });

        dialog.show();
    }
}
