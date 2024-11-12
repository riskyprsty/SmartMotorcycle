package com.tri.smartmotorcycle;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import me.ibrahimsn.lib.OnItemSelectedListener;
import me.ibrahimsn.lib.SmoothBottomBar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        SmoothBottomBar bottomBar = findViewById(R.id.bottomBar);
        loadFragment(new HomeFragment()); // Load the default fragment

        bottomBar.setOnItemSelectedListener((OnItemSelectedListener) i -> {
            Fragment selectedFragment;
            switch (i) {
                case 0:
                    selectedFragment = new HomeFragment();
                    break;
                case 1:
                    selectedFragment = new SearchFragment();
                    break;
                case 2:
                    selectedFragment = new ProfileFragment();
                    break;
                default:
                    return false;
            }
            loadFragment(selectedFragment);
            return true;
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });



    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_layout, fragment)
                .commit();
    }
}