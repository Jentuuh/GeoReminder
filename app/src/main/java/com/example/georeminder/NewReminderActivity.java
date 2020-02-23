package com.example.georeminder;

import androidx.core.view.ViewCompat;
import androidx.fragment.app.FragmentActivity;

import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;


public class NewReminderActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private TextView instructionTitle;
    private TextView instructionSubtitle;
    private SeekBar radiusBar;
    private TextView radiusDescription;
    private EditText message;
    private ImageView marker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_reminder);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        instructionTitle = (TextView) findViewById(R.id.instructionTitle);
        instructionSubtitle = (TextView) findViewById(R.id.instructionSubtitle);
        radiusBar = (SeekBar) findViewById(R.id.radiusBar);
        radiusDescription = (TextView) findViewById(R.id.radiusDescription);
        message = (EditText) findViewById(R.id.message);
        marker = (ImageView) findViewById(R.id.marker);

        instructionTitle.setVisibility(View.GONE);
        instructionSubtitle.setVisibility(View.GONE);
        radiusBar.setVisibility(View.GONE);
        radiusDescription.setVisibility(View.GONE);
        message.setVisibility(View.GONE);

        ViewCompat.setTranslationZ(marker, 2);
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

        showLocationSetup();
    }

    public void showLocationSetup() {
        instructionTitle.setVisibility(View.VISIBLE);
        instructionSubtitle.setVisibility(View.VISIBLE);
        radiusBar.setVisibility(View.GONE);
        radiusDescription.setVisibility(View.GONE);
        message.setVisibility(View.GONE);
    }
}
