package com.tri.smartmotorcycle;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MotorViewModel extends ViewModel {
    private final MutableLiveData<String> selectedVehicleId = new MutableLiveData<>();
    private final MutableLiveData<Double> temperatureData = new MutableLiveData<>();
    private final MutableLiveData<Double> latitude = new MutableLiveData<>();
    private final MutableLiveData<Double> longitude = new MutableLiveData<>();
    private final MutableLiveData<Long> timestamp = new MutableLiveData<>();
    private DatabaseReference firebaseRef;

    public MotorViewModel() {
        firebaseRef = FirebaseDatabase.getInstance().getReference("vehicle");
    }

    public LiveData<String> getSelectedVehicleId() {
        return selectedVehicleId;
    }

    public void setSelectedVehicleId(String vehicleId) {
        if (!vehicleId.equals(selectedVehicleId.getValue())) {
            selectedVehicleId.setValue(vehicleId);
            loadTemperatureData(vehicleId);
            loadLocationData(vehicleId);
        }
    }

    public LiveData<Double> getTemperatureData() {
        return temperatureData;
    }

    public LiveData<Double> getLatitude() {
        return latitude;
    }

    public LiveData<Double> getLongitude() {
        return longitude;
    }

    public LiveData<Long> getTimestamp() {
        return timestamp;
    }

    // ambil data temperaturenya rek
    private void loadTemperatureData(String vehicleId) {
        if (firebaseRef != null) {
            firebaseRef.child(vehicleId).child("monitoring").child("temperature")
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Double temperature = dataSnapshot.getValue(Double.class);
                            if (temperature != null) {
                                Log.d("MotorViewModel", "Temperature data retrieved: " + temperature);
                            } else {
                                Log.d("MotorViewModel", "Temperature data is null");
                            }
                            temperatureData.setValue(temperature);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.e("MotorViewModel", "Failed to load temperature data.", databaseError.toException());
                        }
                    });
        }
    }

    // buat ambil lokasi e dari firebase
    private void loadLocationData(String vehicleId) {
        if (firebaseRef != null) {
            firebaseRef.child(vehicleId).child("location")
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Double lat = dataSnapshot.child("lat").getValue(Double.class);
                            Double lng = dataSnapshot.child("lng").getValue(Double.class);
                            Long time = dataSnapshot.child("timestamp").getValue(Long.class);

                            if (lat != null && lng != null && time != null) {
                                Log.d("MotorViewModel", "Location data retrieved: Lat=" + lat + ", Lng=" + lng + ", Timestamp=" + time);
                                latitude.setValue(lat);
                                longitude.setValue(lng);
                                timestamp.setValue(time);
                            } else {
                                Log.d("MotorViewModel", "One or more location values are null");
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.e("MotorViewModel", "Failed to load location data.", databaseError.toException());
                        }
                    });
        }
    }
}
