package com.raithumitra.ui.main;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.google.android.material.chip.Chip;
import com.raithumitra.R;
import com.raithumitra.data.local.SessionManager;

public class LandingPageFragment extends Fragment {

    private SessionManager sessionManager;
    private SwipeRefreshLayout swipeRefreshLayout;

    // Repositories
    private com.raithumitra.data.repository.ContractRepository contractRepo;
    private com.raithumitra.data.repository.EquipmentRepository equipmentRepo;
    private com.raithumitra.data.repository.LaborRepository laborRepo;
    private com.raithumitra.data.repository.BookingRepository bookingRepo;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_landing_page, container, false);

        sessionManager = new SessionManager(requireContext());

        // Initialize Repositories
        contractRepo = new com.raithumitra.data.repository.ContractRepository(requireActivity().getApplication());
        equipmentRepo = new com.raithumitra.data.repository.EquipmentRepository(requireActivity().getApplication());
        laborRepo = new com.raithumitra.data.repository.LaborRepository(requireActivity().getApplication());
        bookingRepo = new com.raithumitra.data.repository.BookingRepository(requireContext());

        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setColorSchemeResources(R.color.primary_green);

        TextView tvName = view.findViewById(R.id.tv_welcome_name);
        Chip chipRole = view.findViewById(R.id.chip_role_badge);

        String name = sessionManager.getUserName();
        String role = sessionManager.getUserRole();

        tvName.setText("స్వాగతం, " + name + "!");
        chipRole.setText(role);

        // Setup Observers
        setupObservers(view, role);

        // Refresh Listener
        swipeRefreshLayout.setOnRefreshListener(() -> {
            refreshData(role);
        });

        return view;
    }

    private void setupObservers(View view, String role) {
        contractRepo.getAllContracts().observe(getViewLifecycleOwner(), contracts -> {
            updateDashboard(view, role, contracts == null ? 0 : contracts.size(), -1, -1);
        });

        equipmentRepo.getAllEquipment().observe(getViewLifecycleOwner(), equipment -> {
            updateDashboard(view, role, -1, equipment == null ? 0 : equipment.size(), -1);
        });

        laborRepo.getAllLaborers().observe(getViewLifecycleOwner(), laborers -> {
            updateDashboard(view, role, -1, -1, laborers == null ? 0 : laborers.size());
        });

        // Equipment Owner Dashboard
        if ("OWNER".equals(role) || "EQUIPMENT_OWNER".equals(role)) {
            View cardDashboard = view.findViewById(R.id.cardDashboard);
            cardDashboard.setVisibility(View.VISIBLE);

            android.widget.TextView tvRequestCount = view.findViewById(R.id.tvRequestCount);
            android.widget.TextView tvOngoingCount = view.findViewById(R.id.tvOngoingCount);

            bookingRepo.getReceivedBookings().observe(getViewLifecycleOwner(), bookings -> {
                if (bookings != null) {
                    long pendingCount = bookings.stream().filter(b -> "PENDING".equalsIgnoreCase(b.getStatus()))
                            .count();
                    long ongoingCount = bookings.stream().filter(b -> "CONFIRMED".equalsIgnoreCase(b.getStatus()))
                            .count();
                    tvRequestCount.setText(pendingCount + " పెండింగ్ అభ్యర్థనలు");
                    tvOngoingCount.setText(ongoingCount + " కొనసాగుతున్న బుకింగ్‌లు");
                }
            });

            cardDashboard.setOnClickListener(v -> {
                com.raithumitra.ui.equipment.RequestsBottomSheet bottomSheet = new com.raithumitra.ui.equipment.RequestsBottomSheet();
                bottomSheet.show(getParentFragmentManager(), "RequestsBottomSheet");
            });
        }

        // Labor Worker Dashboard
        if ("WORKER".equals(role)) {
            View cardDashboard = view.findViewById(R.id.cardDashboard);
            cardDashboard.setVisibility(View.VISIBLE);

            android.widget.TextView tvDashboardTitle = view.findViewById(R.id.tvDashboardTitle);
            tvDashboardTitle.setText("కూలీ అభ్యర్థనలు");
            ((android.widget.ImageView) view.findViewById(R.id.iconTractor)).setImageResource(R.drawable.ic_labor);

            android.widget.TextView tvRequestCount = view.findViewById(R.id.tvRequestCount);
            android.widget.TextView tvOngoingCount = view.findViewById(R.id.tvOngoingCount);

            laborRepo.getReceivedLaborRequests().observe(getViewLifecycleOwner(), requests -> {
                if (requests != null) {
                    long pendingCount = requests.stream().filter(r -> "REQUESTED".equalsIgnoreCase(r.getStatus()))
                            .count();
                    long ongoingCount = requests.stream().filter(r -> "ACCEPTED".equalsIgnoreCase(r.getStatus()))
                            .count();
                    tvRequestCount.setText(pendingCount + " పెండింగ్ అభ్యర్థనలు");
                    tvOngoingCount.setText(ongoingCount + " కొనసాగుతున్న పనులు");
                }
            });

            cardDashboard.setOnClickListener(v -> {
                com.raithumitra.ui.labor.LaborRequestsBottomSheet bottomSheet = new com.raithumitra.ui.labor.LaborRequestsBottomSheet();
                bottomSheet.show(getParentFragmentManager(), "LaborRequestsBottomSheet");
            });
        }
    }

    private void refreshData(String role) {
        contractRepo.refreshContracts();
        equipmentRepo.refreshEquipment();
        laborRepo.refreshLaborers();

        if ("OWNER".equals(role) || "EQUIPMENT_OWNER".equals(role)) {
            bookingRepo.refreshReceivedBookings();
        }
        if ("WORKER".equals(role)) {
            laborRepo.refreshReceivedLaborRequests();
        }

        // Stop refresh animation after 1.5 seconds
        new Handler().postDelayed(() -> {
            if (swipeRefreshLayout != null) {
                swipeRefreshLayout.setRefreshing(false);
            }
        }, 1500);
    }

    private int lastContractCount = 0;
    private int lastEquipCount = 0;
    private int lastLaborCount = 0;

    private void updateDashboard(View view, String role, int contracts, int equipment, int laborers) {
        if (contracts != -1)
            lastContractCount = contracts;
        if (equipment != -1)
            lastEquipCount = equipment;
        if (laborers != -1)
            lastLaborCount = laborers;

        com.google.android.material.card.MaterialCardView cardContracts = view.findViewById(R.id.card_contracts);
        com.google.android.material.card.MaterialCardView cardEquipment = view.findViewById(R.id.card_equipment);
        com.google.android.material.card.MaterialCardView cardLabor = view.findViewById(R.id.card_labor);

        ((TextView) view.findViewById(R.id.tv_contract_count)).setText(String.valueOf(lastContractCount));
        ((TextView) view.findViewById(R.id.tv_equip_count)).setText(String.valueOf(lastEquipCount));
        ((TextView) view.findViewById(R.id.tv_labor_count)).setText(String.valueOf(lastLaborCount));

        // Click Listeners -> Navigate to respective tabs
        cardContracts.setOnClickListener(v -> navigateToTab(1));
        cardEquipment.setOnClickListener(v -> navigateToTab(2));
        cardLabor.setOnClickListener(v -> navigateToTab(3));

        // Visibility Logic
        if ("FARMER".equals(role)) {
            cardContracts.setVisibility(View.VISIBLE);
            cardEquipment.setVisibility(View.VISIBLE);
            cardLabor.setVisibility(View.VISIBLE);
        } else if ("CORPORATE".equals(role)) {
            cardContracts.setVisibility(View.VISIBLE);
            cardEquipment.setVisibility(View.GONE);
            cardLabor.setVisibility(View.GONE);
        } else if ("WORKER".equals(role)) {
            cardContracts.setVisibility(View.GONE);
            cardEquipment.setVisibility(View.GONE);
            cardLabor.setVisibility(View.VISIBLE);
            ((TextView) view.findViewById(R.id.tv_labor_title)).setText("అందుబాటులో పనులు (Available Jobs)");
        } else if ("OWNER".equals(role)) {
            cardContracts.setVisibility(View.GONE);
            cardEquipment.setVisibility(View.VISIBLE);
            cardLabor.setVisibility(View.GONE);
        }
    }

    private void navigateToTab(int index) {
        if (getActivity() instanceof MainActivity) {
            com.google.android.material.bottomnavigation.BottomNavigationView bottomNav = getActivity()
                    .findViewById(R.id.bottom_navigation);
            if (bottomNav != null) {
                android.view.Menu menu = bottomNav.getMenu();
                for (int i = 0; i < menu.size(); i++) {
                    android.view.MenuItem item = menu.getItem(i);
                    String title = item.getTitle().toString();
                    if ((index == 1 && title.contains("Contract")) ||
                            (index == 2 && (title.contains("Rental") || title.contains("Equipment"))) ||
                            (index == 3 && (title.contains("Worker") || title.contains("Jobs")))) {
                        item.setChecked(true);
                        bottomNav.setSelectedItemId(item.getItemId());
                        return;
                    }
                }
            }
        }
    }
}
