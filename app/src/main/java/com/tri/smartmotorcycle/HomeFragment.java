package com.tri.smartmotorcycle;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class HomeFragment extends Fragment {
    private TextView textField;
    private ImageView hexagon;

    Animation Rotate_Animation, Bottom_Animation, Left_Animation;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference neo6m = database.getReference("neo_6m");

        textField = view.findViewById(R.id.test);
        hexagon = view.findViewById(R.id.hexagon);

        Rotate_Animation = AnimationUtils.loadAnimation(getContext(), R.anim.rotateanim);
        textField.setAnimation(Rotate_Animation);
        hexagon.setAnimation(Rotate_Animation);
//        Left_Animation = AnimationUtils.loadAnimation(this, R.anim.leftanim);
//        Bottom_Animation = AnimationUtils.loadAnimation(this, R.anim.bottomanim);


        neo6m.addValueEventListener(new ValueEventListener() {
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

