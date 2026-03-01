package com.raithumitra.ui.main;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.raithumitra.R;
import com.raithumitra.ui.contract.ContractFragment;
import com.raithumitra.ui.equipment.EquipmentFragment;
import com.raithumitra.ui.labor.LaborFragment;
import com.raithumitra.data.local.SessionManager;

public class MainActivity extends AppCompatActivity {

    private androidx.drawerlayout.widget.DrawerLayout drawerLayout;
    private com.google.android.material.navigation.NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SessionManager sessionManager = new SessionManager(this);
        String role = sessionManager.getUserRole();

        // Setup Drawer
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        android.view.View headerView = navigationView.getHeaderView(0);

        android.widget.TextView navName = headerView.findViewById(R.id.tv_nav_name);
        android.widget.TextView navPhone = headerView.findViewById(R.id.tv_nav_cnt);
        navName.setText(sessionManager.getUserName());
        navPhone.setText(sessionManager.getMobileNumber());

        android.widget.ImageView btnMenu = findViewById(R.id.btn_menu);
        btnMenu.setOnClickListener(v -> drawerLayout.open());

        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_profile) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new com.raithumitra.ui.profile.ProfileFragment()).commit();
                drawerLayout.close();
            } else if (id == R.id.nav_logout) {
                sessionManager.clearSession();
                startActivity(new android.content.Intent(this, com.raithumitra.ui.auth.LoginActivity.class));
                finish();
            }
            return true;
        });

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnItemSelectedListener(navListener);

        // Add menu items programmatically based on Role
        bottomNav.getMenu().add(0, 0, 0, "హోమ్").setIcon(R.drawable.ic_home); // All roles see Home

        if ("FARMER".equals(role)) {
            bottomNav.getMenu().add(0, 1, 0, "ఒప్పందాలు").setIcon(R.drawable.ic_handshake);
            bottomNav.getMenu().add(0, 2, 0, "అద్దె").setIcon(R.drawable.ic_equipment);
            bottomNav.getMenu().add(0, 3, 0, "కూలీలు").setIcon(R.drawable.ic_labor);
        } else if ("CORPORATE".equals(role)) {
            bottomNav.getMenu().add(0, 1, 0, "ఒప్పందాలు").setIcon(R.drawable.ic_handshake);
        } else if ("WORKER".equals(role)) {
            bottomNav.getMenu().add(0, 3, 0, "పనులు").setIcon(R.drawable.ic_labor);
        } else if ("OWNER".equals(role)) {
            bottomNav.getMenu().add(0, 2, 0, "ఇన్వెంటరీ").setIcon(R.drawable.ic_equipment);
        }

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new LandingPageFragment())
                .commit();
    }

    private final com.google.android.material.navigation.NavigationBarView.OnItemSelectedListener navListener = item -> {
        Fragment selectedFragment = null;
        String title = item.getTitle().toString();

        if (title.contains("Home") || title.contains("హోమ్"))
            selectedFragment = new LandingPageFragment();
        else if (title.contains("ఒప్పందాలు"))
            selectedFragment = new com.raithumitra.ui.contract.ContractFragment();
        else if (title.contains("అద్దె") || title.contains("ఇన్వెంటరీ"))
            selectedFragment = new com.raithumitra.ui.equipment.EquipmentFragment();
        else if (title.contains("కూలీలు") || title.contains("పనులు"))
            selectedFragment = new com.raithumitra.ui.labor.LaborFragment();

        if (selectedFragment != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
        }
        return true;
    };
}
