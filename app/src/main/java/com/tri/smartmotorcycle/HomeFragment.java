package com.tri.smartmotorcycle;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import java.util.Locale;


public class HomeFragment extends Fragment {
    private TextView locationText;
    private ImageView hexagon;
    private MotorViewModel motorViewModel;
    private TextView textField2;

    Animation Rotate_Animation, Bottom_Animation, Left_Animation;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        locationText = view.findViewById(R.id.test);
        textField2 = view.findViewById(R.id.test2);
        hexagon = view.findViewById(R.id.hexagon);

        motorViewModel = new ViewModelProvider(requireActivity()).get(MotorViewModel.class);

        motorViewModel.getTemperatureData().observe(getViewLifecycleOwner(), temperature -> {
            if (temperature != null) {
                textField2.setText(String.format(Locale.getDefault(), "%.2f °C", temperature));
            } else {
                textField2.setText("No Data");
            }
        });

        motorViewModel.getLatitude().observe(getViewLifecycleOwner(), latitude -> updateLocationText());
        motorViewModel.getLongitude().observe(getViewLifecycleOwner(), longitude -> updateLocationText());
        motorViewModel.getTimestamp().observe(getViewLifecycleOwner(), timestamp -> updateLocationText());

        Rotate_Animation = AnimationUtils.loadAnimation(getContext(), R.anim.fadeanim);
        locationText.setAnimation(Rotate_Animation);
        hexagon.setAnimation(Rotate_Animation);

        return view;
    }

    private void updateLocationText() {
        Double latitude = motorViewModel.getLatitude().getValue();
        Double longitude = motorViewModel.getLongitude().getValue();
        Long timestamp = motorViewModel.getTimestamp().getValue();

        if (latitude != null && longitude != null && timestamp != null) {
            String locationInfo = String.format(Locale.getDefault(),
                    "Lat: %.6f\nLng: %.6f\nTimestamp: %d", latitude, longitude, timestamp);
            locationText.setText(locationInfo);
        } else {
            locationText.setText("Location: N/A");
        }
    }
}

