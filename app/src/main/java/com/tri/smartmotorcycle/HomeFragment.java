package com.tri.smartmotorcycle;

import android.os.Bundle;
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

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;

import java.util.Locale;


public class HomeFragment extends Fragment {
    private TextView locationText;
    private ImageView hexagon;
    private VehicleViewModel vehicleViewModel;
    private TextView vehicleName;

    Animation Rotate_Animation, Bottom_Animation, Left_Animation;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        locationText = view.findViewById(R.id.test);
        vehicleName = view.findViewById(R.id.vehicle_name);
        hexagon = view.findViewById(R.id.power_circle);
        final LottieAnimationView lottiePowerButton = view.findViewById(R.id.powerButton);

        vehicleViewModel = new ViewModelProvider(requireActivity()).get(VehicleViewModel.class);

        vehicleViewModel.getSelectedVehicleId().observe(getViewLifecycleOwner(), id -> {
            if (id != null && !id.isEmpty()) {
                vehicleName.setText(id);
            } else {
                vehicleName.setText("Scan QR untuk menambahkan Vehicle");
            }
        });
//        vehicleViewModel.getTemperatureData().observe(getViewLifecycleOwner(), temperature -> {
//            if (temperature != null) {
//                textField2.setText(String.format(Locale.getDefault(), "%.2f °C", temperature));
//            } else {
//                textField2.setText("No Data");
//            }
//        });

        vehicleViewModel.getLatitude().observe(getViewLifecycleOwner(), latitude -> updateLocationText());
        vehicleViewModel.getLongitude().observe(getViewLifecycleOwner(), longitude -> updateLocationText());
        vehicleViewModel.getTimestamp().observe(getViewLifecycleOwner(), timestamp -> updateLocationText());

        Rotate_Animation = AnimationUtils.loadAnimation(getContext(), R.anim.fadeanim);
        locationText.setAnimation(Rotate_Animation);
        hexagon.setAnimation(Rotate_Animation);

        vehicleViewModel.getPowerStatus().observe(getViewLifecycleOwner(), isPowerOn -> {
            if (isPowerOn) {
                lottiePowerButton.setAnimation(R.raw.power_on);
                lottiePowerButton.setRepeatCount(LottieDrawable.INFINITE);
            } else {
                lottiePowerButton.setAnimation(R.raw.power_off);
                lottiePowerButton.setRepeatCount(LottieDrawable.INFINITE);
            }
            lottiePowerButton.playAnimation();
        });

        lottiePowerButton.setOnClickListener(v -> {
            boolean statusPower = vehicleViewModel.getPowerStatus().getValue() != null && vehicleViewModel.getPowerStatus().getValue();
            vehicleViewModel.setPowerStatus(!statusPower);
        });

        return view;
    }

    private void updateLocationText() {
        Double latitude = vehicleViewModel.getLatitude().getValue();
        Double longitude = vehicleViewModel.getLongitude().getValue();
        Long timestamp = vehicleViewModel.getTimestamp().getValue();

        if (latitude != null && longitude != null && timestamp != null) {
            String locationInfo = String.format(Locale.getDefault(),
                    "Lat: %.6f\nLng: %.6f\nTimestamp: %d", latitude, longitude, timestamp);
            locationText.setText(locationInfo);
        } else {
            locationText.setText("Location: N/A");
        }
    }
}

