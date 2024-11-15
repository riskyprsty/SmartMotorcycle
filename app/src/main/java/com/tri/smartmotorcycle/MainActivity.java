package com.tri.smartmotorcycle;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import me.ibrahimsn.lib.OnItemSelectedListener;
import me.ibrahimsn.lib.SmoothBottomBar;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private FloatingActionButton floatingHamburger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        floatingHamburger = findViewById(R.id.floating_hamburger);

        // Setup Floating Hamburger Button
        findViewById(R.id.floating_hamburger).setOnClickListener(v -> {
            drawerLayout.openDrawer(GravityCompat.START);
        });

        // Setup Bottom Navigation
        SmoothBottomBar bottomBar = findViewById(R.id.bottomBar);
        loadFragment(new HomeFragment()); // Load fragment default

        // Bottom Navigation Handler
        bottomBar.setOnItemSelectedListener((OnItemSelectedListener) i -> {
            Fragment selectedFragment;
            switch (i) {
                case 0:
                    selectedFragment = new HomeFragment();
                    setHamburgerVisibility(true);
                    break;
                case 1:
                    selectedFragment = new MapsFragment();
                    setHamburgerVisibility(false);
                    break;
                case 2:
                    selectedFragment = new ProfileFragment();
                    setHamburgerVisibility(false);
                    break;
                default:
                    return false;
            }
            loadFragment(selectedFragment);
            return true;
        });

        // Setup Circular Button Click
        findViewById(R.id.circular_button).setOnClickListener(v -> {
            // Handle circular button click
            Toast.makeText(this, "Circular Button Clicked", Toast.LENGTH_SHORT).show();
        });

        // Setup Power Button Click
        findViewById(R.id.power_button).setOnClickListener(v -> {
            // Handle power button click
            Toast.makeText(this, "Power Button Clicked", Toast.LENGTH_SHORT).show();
        });

        // Window Insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
//            switch (item.getItemId()) {
//                case R.id.nav_fi:
//                    Toast.makeText(this, "Logout Clicked", Toast.LENGTH_SHORT).show();
//                    // Implement logout functionality
//                    break;
//            }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_layout, fragment)
                .commit();
    }

    private void setHamburgerVisibility(boolean isVisible) {
        if (floatingHamburger != null) {
            floatingHamburger.setVisibility(isVisible ? View.VISIBLE : View.GONE);
        }
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}