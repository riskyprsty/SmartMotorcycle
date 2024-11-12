package com.tri.smartmotorcycle;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class HomeFragment extends Fragment {
    private TextView textField;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("neo_6m");

        textField = view.findViewById(R.id.test);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Double lat = dataSnapshot.child("lat").getValue(Double.class);
                Double lng = dataSnapshot.child("lng").getValue(Double.class);
                String res = "Lat: " + String.valueOf(lat) + "\nLong: " + String.valueOf(lng);
                textField.setText(res);
                Log.d("firebase", res);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                System.err.println("Failed to read value: " + error.toException());
            }
        });

        return view;
    }
}

