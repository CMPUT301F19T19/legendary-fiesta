package io.github.cmput301f19t19.legendary_fiesta;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import io.github.cmput301f19t19.legendary_fiesta.ui.ProxyLatLng;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {
    private boolean mLocationPermissionGranted = false;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 177;
    private static final int DEFAULT_ZOOM = 15;
    private static final LatLng DEFAULT_LOCATION = new LatLng(53.523,-113.527); // near UofA
    private GoogleMap googleMap = null;
    private Location mLastKnownLocation = null;
    private FusedLocationProviderClient mFusedLocationProviderClient;

    // MoodEvents
    private ArrayList<MoodEvent> dataList;

    // Date Format
    DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
    DateFormat timeFormat = new SimpleDateFormat("hh:mm aa");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Retrieve the content view that renders the map.
        setContentView(R.layout.activity_map);
        // Get the SupportMapFragment and request notification when the map is ready to be used.

        // Hide action bar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.hide();

        // Get list of MoodEvents from OwnMoods or FriendsMoods Fragment
        dataList  = new ArrayList<>();
        // Dummy Data
        @Mood.MoodType Integer moodType = Mood.NEUTRAL;
        String user = "TestUser";
        Date date = new Date();
        String description = "TestDescription";
        Integer condition = MoodEvent.SocialCondition.SINGLE;
        String photoURL = "https://example.com/photo.jpg";
        ProxyLatLng location = new ProxyLatLng(53.521231, -113.524631);

        dataList.add(new MoodEvent(moodType, user, description, date,
                condition, photoURL, location));
        dataList.add(new MoodEvent(Mood.ANGRY, "USERB", "Test user B", new Date(), MoodEvent.SocialCondition.CROWD, photoURL, new ProxyLatLng(53.510317, -113.514320)));

        // Intent intent = getIntent();
        // dataList = intent.getParcelableArrayListExtra("EVENTS");
        // MoodEvent[] moodEventArray = (MoodEvent[]) intent.getParcelableArrayExtra("EVENTARY");

        // Get the map fragment
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
    }

    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (mLocationPermissionGranted) {
                Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            mLastKnownLocation = task.getResult();
                            assert mLastKnownLocation != null;
                            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                    new LatLng(mLastKnownLocation.getLatitude(),
                                            mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                        } else {
                            Log.e("FeelsLog", "Exception: %s", task.getException());
                            googleMap.moveCamera(CameraUpdateFactory
                                    .newLatLngZoom(DEFAULT_LOCATION, DEFAULT_ZOOM));
                        }
                    }
                });
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    /**
     * Handles the result of the request for location permissions.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        if (requestCode == PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) { // If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionGranted = true;
                getDeviceLocation();
            }
        }
    }

    /**
     * Request location permission, so that we can get the location of the
     * device. The result of the permission request is handled by a callback,
     * onRequestPermissionsResult.
     */
    private void getLocationPermission() {
        mLocationPermissionGranted = false;
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
            getDeviceLocation();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        googleMap.getUiSettings().setZoomControlsEnabled(true);

        // Marker Custom Info Window
        googleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                Context mContext = getApplicationContext();

                LinearLayout info = new LinearLayout(mContext);
                info.setOrientation(LinearLayout.VERTICAL);

                TextView title = new TextView(mContext);
                title.setTextColor(Color.BLACK);
                title.setGravity(Gravity.CENTER);
                title.setTypeface(null, Typeface.BOLD);
                title.setText(marker.getTitle());

                TextView snippet = new TextView(mContext);
                snippet.setTextColor(Color.BLACK);
                snippet.setText(marker.getSnippet());
                snippet.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);

                info.addView(title);
                info.addView(snippet);

                return info;
            }
        });

        CameraPosition cameraPosition;

        // University of Alberta
        LatLng defaultPosition = new LatLng(53.523047, -113.526329);
        // Set camera position to University of Alberta
        cameraPosition = new CameraPosition.Builder()
                .target(defaultPosition)
                .zoom(12)
                .bearing(0)
                .build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        // Set camera and markers
        if (dataList.size() != 0) {
            // Set default camera to position of first MoodEvent
            MoodEvent firstEvent = dataList.get(0);
            cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(firstEvent.getLocation().latitude,
                            firstEvent.getLocation().longitude))
                    .zoom(12)
                    .bearing(0)
                    .build();
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

            // Set Markers
            for (MoodEvent moodEvent : dataList) {
                // Skip MoodEvents that has no location
                if (moodEvent.getLocation() != null) {
                    LatLng location = new LatLng(moodEvent.getLocation().latitude,
                            moodEvent.getLocation().longitude);
                    int resource = getEmotionRadioId(moodEvent.getMoodType());

                    googleMap.addMarker(new MarkerOptions().position(location)
                            .title("Date: " + dateFormat.format(moodEvent.getDate())
                                    + "\nTime: " + timeFormat.format(moodEvent.getDate()))
                            .snippet("Description: "  + moodEvent.getDescription()
                                    + "\n Social Condition: "
                                    + getSelectedSocialCondition(moodEvent.getCondition()))
                            .icon(bitmapDescriptor(getApplicationContext(), resource,
                                    getEmotionColor(moodEvent.getMoodType()))));
                }
            }
        }
        getLocationPermission();
    }

    /**
     * Get the Resource ID (Image) for the respective mood types
     * @param moodId
     *  Mood type of the Mood Event
     * @return
     *  Returns the Resource ID of the respective mood type
     */
    private int getEmotionRadioId(@Mood.MoodType int moodId) {
        switch (moodId) {
            case Mood.NEUTRAL:
                return R.drawable.icon_neutral;
            case Mood.HAPPY:
                return R.drawable.icon_happy;
            case Mood.ANGRY:
                return R.drawable.icon_angry;
            case Mood.DISGUSTED:
                return R.drawable.icon_disgusted;
            case Mood.SAD:
                return R.drawable.icon_sad;
            case Mood.SCARED:
                return R.drawable.icon_scared;
            case Mood.SURPRISED:
                return R.id.icon_surprised;
        }
        return R.id.icon_neutral;
    }

    /**
     * Get the Color ID for the respective mood types
     * @param moodId
     * Mood type of the MoodEvent
     * @return
     * Returns the Color ID for that mood type.
     */
    private int getEmotionColor(@Mood.MoodType int moodId) {
        switch (moodId) {
            case Mood.NEUTRAL:
                return R.color.color_neutral;
            case Mood.HAPPY:
                return R.color.color_happy;
            case Mood.ANGRY:
                return R.color.color_angry;
            case Mood.DISGUSTED:
                return R.color.color_disgusted;
            case Mood.SAD:
                return R.color.color_sad;
            case Mood.SCARED:
                return R.color.color_scared;
            case Mood.SURPRISED:
                return R.color.color_surprised;
        }
        return R.id.icon_neutral;
    }

    /**
     * Returns the selected social condition
     * @param socialCondition
     *  Selected social condition from the dropdown (spinner)
     * @return
     *  Returns an integer that corresponds to the selected social condition
     */
    // TODO: TEST
    private String getSelectedSocialCondition(int socialCondition) {
        switch (socialCondition) {
            case MoodEvent.SocialCondition.SINGLE:
                return "Single";
            case MoodEvent.SocialCondition.PAIR:
                return "Pair";
            case MoodEvent.SocialCondition.SMALL_GROUP:
                return "Small Group";
            case MoodEvent.SocialCondition.CROWD:
                return "Crowd";
            default:
                return "NONE";
        }
    }

    /**
     *
     * @param context
     *  Context of the application
     * @param resID
     *  Resource ID (referring to Image)
     * @param colorID
     *  Color ID
     * @return
     * Returns the definition of a bitmap image (map marker)
     */
    private BitmapDescriptor bitmapDescriptor (Context context, int resID, int colorID) {
        Drawable drawable = ContextCompat.getDrawable(context, resID);
        drawable.setBounds(0, 0, 150, 150);
        drawable.setTint(ContextCompat.getColor(context, colorID));
        Bitmap bitmap = Bitmap.createBitmap(150, 150, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }
}