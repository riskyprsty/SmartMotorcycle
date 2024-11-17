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
    private TextView vehicleName, operatorName, signalStrength, imeiText, imsiText, ipAddress, voltageText, temperatureText, humidityText;

    Animation Rotate_Animation, Bottom_Animation, Left_Animation;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

//        locationText = view.findViewById(R.id.test);
        vehicleName = view.findViewById(R.id.vehicle_name);
        operatorName = view.findViewById(R.id.operator_name);
        signalStrength = view.findViewById(R.id.strength);
        imeiText = view.findViewById(R.id.imei);
        imsiText = view.findViewById(R.id.imsi);
        ipAddress = view.findViewById(R.id.ip_address);
        voltageText = view.findViewById(R.id.voltage_text);
        temperatureText = view.findViewById(R.id.temp_value);
        humidityText = view.findViewById(R.id.humidity_value);
        hexagon = view.findViewById(R.id.power_circle);

        final LottieAnimationView lottiePowerButton = view.findViewById(R.id.powerButton);
        final LottieAnimationView lottieSignalWidget = view.findViewById(R.id.signal_animation);
        final LottieAnimationView lottieBatteryWidget = view.findViewById(R.id.battery_animation);
        final LottieAnimationView lottieTemperatureWidget = view.findViewById(R.id.temperature_animation);

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

//        vehicleViewModel.getLatitude().observe(getViewLifecycleOwner(), latitude -> updateLocationText());
//        vehicleViewModel.getLongitude().observe(getViewLifecycleOwner(), longitude -> updateLocationText());
//        vehicleViewModel.getTimestamp().observe(getViewLifecycleOwner(), timestamp -> updateLocationText());

        vehicleViewModel.getModemOperator().observe(getViewLifecycleOwner(), operator -> updateModemText());
        vehicleViewModel.getModemImei().observe(getViewLifecycleOwner(), imei -> updateModemText());
        vehicleViewModel.getModemImsi().observe(getViewLifecycleOwner(), imsi -> updateModemText());
        vehicleViewModel.getModemIpAddress().observe(getViewLifecycleOwner(), ip_address -> updateModemText());
        vehicleViewModel.getModemSignalStrength().observe(getViewLifecycleOwner(), signal_strength -> {
            updateModemText();

            if (signal_strength != null) {
                if (signal_strength < 10) {
                    lottieSignalWidget.setMinAndMaxProgress(0.0f, 0.1f);
                } else if (signal_strength < 20) {
                    lottieSignalWidget.setMinAndMaxProgress(0.1f, 0.2f);
                } else if (signal_strength < 30) {
                    lottieSignalWidget.setMinAndMaxProgress(0.2f, 0.3f);
                } else if (signal_strength < 40) {
                    lottieSignalWidget.setMinAndMaxProgress(0.3f, 0.4f);
                } else if (signal_strength > 50) {
                    lottieSignalWidget.setMinAndMaxProgress(0.4f, 0.5f);
                }
                lottieSignalWidget.playAnimation();
            }
        });
        vehicleViewModel.getVoltage().observe(getViewLifecycleOwner(), voltage -> {
            updateElectricityText();

            if (voltage != null) {
                if (voltage < 5) {
                    lottieBatteryWidget.setMinAndMaxProgress(0.0f, 0.1f);
                } else if (voltage < 10) {
                    lottieBatteryWidget.setMinAndMaxProgress(0.2f, 0.3f);
                } else {
                    lottieBatteryWidget.setMinAndMaxProgress(0.3f, 0.6f);
                }
            }
            lottieBatteryWidget.playAnimation();
        });

        vehicleViewModel.getTemperatureData().observe(getViewLifecycleOwner(), temperature -> {
            updateTemperatureText();
            lottieTemperatureWidget.setMinAndMaxProgress(0.0f, 0.5f);
            lottieTemperatureWidget.playAnimation();
        });

        vehicleViewModel.getHumidityData().observe(getViewLifecycleOwner(), humidity -> updateTemperatureText());

        Rotate_Animation = AnimationUtils.loadAnimation(getContext(), R.anim.fadeanim);
//        locationText.setAnimation(Rotate_Animation);
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

    private void updateModemText() {
        String operator = vehicleViewModel.getModemOperator().getValue();
        String imei = vehicleViewModel.getModemImei().getValue();
        String imsi = vehicleViewModel.getModemImsi().getValue();
        String ip_address = vehicleViewModel.getModemIpAddress().getValue();
        Integer signal_strength = vehicleViewModel.getModemSignalStrength().getValue();
        String signal = String.format(Locale.getDefault(), "Signal Strength: %d dBm", signal_strength);

        if (operator != null && imei != null && imsi != null && ip_address != null && signal_strength != null) {
            operatorName.setText(operator);
            signalStrength.setText(signal);
            imeiText.setText("IMEI: " + imei);
            imsiText.setText("IMSI: " + imsi);
            ipAddress.setText(ip_address);

        } else {
            operatorName.setText("N/A");
            signalStrength.setText("Signal Strength: " + 0 + "dBm");
            imeiText.setText("IMEI: " + "N/A");
            imsiText.setText("IMSI: " + "N/A");
            ipAddress.setText("0.0.0.0");
        }

    }

    private void updateElectricityText() {
        Double voltage = vehicleViewModel.getVoltage().getValue();

        if (voltage != null) {
            String voltageInfo = String.format(Locale.getDefault(),
                    "%.2f V", voltage);
            voltageText.setText(voltageInfo);
        } else {
            voltageText.setText("N/A V");
        }
    }

    private void updateTemperatureText() {
        Double temperature = vehicleViewModel.getTemperatureData().getValue();
        Double humidity = vehicleViewModel.getHumidityData().getValue();

        if (temperature != null && humidity != null) {
            String temperatureInfo = String.format(Locale.getDefault(),
                    "%.2f° Celcius", temperature);

            String humidityInfo = String.format(Locale.getDefault(), "%.2f%% Humidity", humidity);
            temperatureText.setText(temperatureInfo);
            humidityText.setText(humidityInfo);
        } else {
            humidityText.setText("N/A %");
            temperatureText.setText("N/A° Celcius");
        }
    }

//    private void updateLocationText() {
//        Double latitude = vehicleViewModel.getLatitude().getValue();
//        Double longitude = vehicleViewModel.getLongitude().getValue();
//        Long timestamp = vehicleViewModel.getTimestamp().getValue();
//
//        if (latitude != null && longitude != null && timestamp != null) {
//            String locationInfo = String.format(Locale.getDefault(),
//                    "Lat: %.6f\nLng: %.6f\nTimestamp: %d", latitude, longitude, timestamp);
//            locationText.setText(locationInfo);
//        } else {
//            locationText.setText("Location: N/A");
//        }
//    }
}

