package com.tri.smartmotorcycle;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Date;

public class MapsFragment extends Fragment implements OnMapReadyCallback {
    private GoogleMap gMap;
    private MotorViewModel motorViewModel;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MapsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MapsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MapsFragment newInstance(String param1, String param2) {
        MapsFragment fragment = new MapsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_maps, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        motorViewModel = new ViewModelProvider(requireActivity()).get(MotorViewModel.class);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.gMap = googleMap;

        motorViewModel = new ViewModelProvider(requireActivity()).get(MotorViewModel.class);

        motorViewModel.getLatitude().observe(getViewLifecycleOwner(), latitude -> updateMapMarker());
        motorViewModel.getLongitude().observe(getViewLifecycleOwner(), longitude -> updateMapMarker());
        motorViewModel.getTimestamp().observe(getViewLifecycleOwner(), timestamp -> updateMapMarker());
    }

    private void updateMapMarker() {
        if (gMap == null) return;

        Double latitude = motorViewModel.getLatitude().getValue();
        Double longitude = motorViewModel.getLongitude().getValue();
        Long timestamp = motorViewModel.getTimestamp().getValue();

        if (latitude != null && longitude != null && timestamp != null) {
            gMap.clear();

            Date date = new Date(timestamp);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm:ss");
            String terakirUpdate = simpleDateFormat.format(date);

            LatLng location = new LatLng(latitude, longitude);
            gMap.addMarker(new MarkerOptions()
                    .position(location)
                    .title("Updated " + terakirUpdate));

            gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15));
        }
    }
}
