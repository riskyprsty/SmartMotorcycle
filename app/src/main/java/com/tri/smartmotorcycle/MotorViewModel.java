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
    private DatabaseReference firebaseRef;

    public MotorViewModel() {
        // Initialize Firebase reference
        firebaseRef = FirebaseDatabase.getInstance().getReference("vehicle");
    }

    public LiveData<String> getSelectedVehicleId() {
        return selectedVehicleId;
    }

    public void setSelectedVehicleId(String vehicleId) {
        if (!vehicleId.equals(selectedVehicleId.getValue())) {
            selectedVehicleId.setValue(vehicleId);
            loadTemperatureData(vehicleId);
        }
    }

    public LiveData<Double> getTemperatureData() {
        return temperatureData;
    }

    // Function to load temperature data for the selected vehicle
    private void loadTemperatureData(String vehicleId) {
        if (firebaseRef != null) {
            firebaseRef.child(vehicleId).child("monitoring").child("temperature")
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Double temperature = dataSnapshot.getValue(Double.class);
                            if (temperature != null) {
                                Log.d("MotorViewModel", "Temperature data  retrieved: " + temperature);
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
}
