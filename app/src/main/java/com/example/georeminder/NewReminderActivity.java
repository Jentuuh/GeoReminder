package com.example.georeminder;

import androidx.fragment.app.FragmentActivity;

import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;

/**
 * Activity Class that handles the creation of new reminders
 */
public class NewReminderActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private TextView instructionTitle;
    private TextView instructionSubtitle;
    private SeekBar radiusBar;
    private TextView radiusDescription;
    private EditText message;
    private ImageView marker;
    private Button next;

    private FusedLocationProviderClient fusedLocationClient;
    private ReminderEntity reminder;

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
        next = (Button) findViewById(R.id.next);

        reminder = new ReminderEntity();
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        instructionTitle.setVisibility(View.GONE);
        instructionSubtitle.setVisibility(View.GONE);
        radiusBar.setVisibility(View.GONE);
        radiusDescription.setVisibility(View.GONE);
        message.setVisibility(View.GONE);
        marker.setVisibility(View.GONE);
        next.setVisibility(View.GONE);

        RelativeLayout parentLayout = (RelativeLayout) marker.getParent();
        parentLayout.bringChildToFront(marker);

        radiusBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                updateRadiusWithProgress(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
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


        AppDatabase.getReminderDatabase(getApplicationContext());

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            LatLng lastLocation = new LatLng(location.getLatitude(), location.getLongitude());
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(lastLocation));
                            mMap.animateCamera(CameraUpdateFactory.zoomTo(getZoomLevel(2000f)));
                        }
                    }
                });
        showLocationSetup();
    }

    /**
     * Calculates the zoomLevel according to the given radius
     * @param radius : radius of geofence reminder
     */
    public int getZoomLevel(float radius) {
        double scale = radius / 500;
        int zoomLevel =(int) (16 - Math.log(scale) / Math.log(2));
        return zoomLevel - 1;
    }

    /**
     * Updates all the objects dependent on the radius according to the given progress (SeekBar)
     * @param progress : progress of a SeekBar
     */
    private void updateRadiusWithProgress(int progress) {
        int radius = Math.round(getRadius(progress));
        reminder.setRadius((float)radius);
        radiusDescription.setText(getString(R.string.radius_description, Integer.toString(radius)));
        LatLng reminderLocation = new LatLng(reminder.getLatitude(), reminder.getLongitude());
        mMap.clear();
        mMap.addCircle(new CircleOptions()
                .center(reminderLocation)
                .radius(radius)
                .strokeColor(Color.RED)
                .fillColor(0x2500ff00));
    }

    /**
     * Calculates the radius according to the given progress
     * @param progress : progress of a SeekBar
     */
    private float getRadius(int progress) {
        return 100 + (2 * progress + 1) * 100;
    }

    /**
     * Sets up the GUI to choose the location
     */
    public void showLocationSetup() {
        instructionTitle.setVisibility(View.VISIBLE);
        instructionSubtitle.setVisibility(View.VISIBLE);
        radiusBar.setVisibility(View.GONE);
        radiusDescription.setVisibility(View.GONE);
        message.setVisibility(View.GONE);
        marker.setVisibility(View.VISIBLE);
        next.setVisibility(View.VISIBLE);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reminder.setLatitude(mMap.getCameraPosition().target.latitude);
                reminder.setLongitude(mMap.getCameraPosition().target.longitude);
                showRadiusSetup();
            }
        });
    }

    /**
     * Sets up the GUI to choose the radius
     */
    public void showRadiusSetup() {
        instructionTitle.setVisibility(View.VISIBLE);
        instructionSubtitle.setVisibility(View.GONE);
        radiusBar.setVisibility(View.VISIBLE);
        radiusDescription.setVisibility(View.VISIBLE);
        message.setVisibility(View.GONE);
        marker.setVisibility(View.GONE);
        next.setVisibility(View.VISIBLE);
        instructionTitle.setText(getString(R.string.instruction_radius_description));

        updateRadiusWithProgress(2);
        mMap.animateCamera(CameraUpdateFactory.zoomTo(getZoomLevel(1000f)));

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMessageSetup();
            }
        });
    }

    /**
     * Sets up the GUI to choose the message
     */
    public void showMessageSetup() {
        instructionTitle.setVisibility(View.VISIBLE);
        instructionSubtitle.setVisibility(View.GONE);
        radiusBar.setVisibility(View.GONE);
        radiusDescription.setVisibility(View.GONE);
        message.setVisibility(View.VISIBLE);
        marker.setVisibility(View.GONE);
        next.setVisibility(View.VISIBLE);
        instructionTitle.setText(getString(R.string.instruction_message_description));

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reminder.setMessage(message.getText().toString());

                if (reminder.getMessage().isEmpty() || reminder.getMessage() == null) {
                    message.setError(getString(R.string.error_required));
                } else {
                    addReminder(reminder);
                }
                finish();
            }
        });
    }

    /**
     * Adds the given reminder to the database
     * @param reminder : reminder to add
     */
    public void addReminder(ReminderEntity reminder) {
        AppDatabase db = AppDatabase.getReminderDatabase(getApplicationContext());

        // Get new ID
        ReminderEntity lastReminder = db.reminderDAO().getLast();
        String lastID = lastReminder.getID();
        String newID = String.valueOf(Integer.parseInt(lastID) + 1);
        reminder.setID(newID);

        db.reminderDAO().insertReminder(reminder);
        setResult(RESULT_OK, getIntent());
    }

}
