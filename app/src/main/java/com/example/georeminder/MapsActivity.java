package com.example.georeminder;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        Intent intent = getIntent();
        String id = intent.getStringExtra("ID");
        double longtitude = intent.getDoubleExtra("LONG", 0);
        double latitude = intent.getDoubleExtra("LAT", 0);
        float radius = intent.getFloatExtra("RADIUS", 0);

        // Show geofence from reminder and move/zoom the camera to it
        LatLng reminderPlace = new LatLng(latitude, longtitude);
        mMap.addMarker(new MarkerOptions().position(reminderPlace).title(id));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(reminderPlace));
        mMap.addCircle(new CircleOptions()
                        .center(reminderPlace)
                        .radius(radius)
                        .strokeColor(Color.RED)
                        .fillColor(0x2500ff00));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(reminderPlace,
                getZoomLevel(radius)));
    }

    public int getZoomLevel(float radius) {
        double scale = radius / 500;
        int zoomLevel =(int) (16 - Math.log(scale) / Math.log(2));
        return zoomLevel - 1;
    }
}
