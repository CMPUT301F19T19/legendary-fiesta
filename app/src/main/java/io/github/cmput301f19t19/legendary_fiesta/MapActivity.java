package io.github.cmput301f19t19.legendary_fiesta;

/*
 * MapActivity sets up a map display that has markers of the user's MoodEvents (if any) or the user's
 * following's MoodEvents (if any) on the map.
 */

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 177;
    private static final int DEFAULT_ZOOM = 15;
    private static final LatLng DEFAULT_LOCATION = new LatLng(53.523, -113.527); // near UofA
    // Marker values
    private static final int MARKER_HEIGHT = 150;
    private static final int MARKER_WIDTH = 150;
    private static final int MARKER_MODE = 72;
    private static final int MARKER_FONT = 30;
    // Date Format
    DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.CANADA);
    DateFormat timeFormat = new SimpleDateFormat("hh:mm aa", Locale.CANADA);
    // Map defaults
    private boolean mLocationPermissionGranted = false;
    // Geolocation
    private GoogleMap googleMap = null;
    private Location mLastKnownLocation = null;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    // MoodEvents
    private ArrayList<MoodEvent> dataList;
    private ArrayList<String> nameList;

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
        dataList = new ArrayList<>();
        // Get MoodEvents from fragment
        dataList = getIntent().getParcelableArrayListExtra("EVENTS");

        if (getIntent().getIntExtra("FRIEND_MODE", 0) == MARKER_MODE) {
            nameList = new ArrayList<>();
            nameList = getIntent().getStringArrayListExtra("FRIENDS");
        }

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
                        } else {
                            Log.e("FeelsLog", "Exception: %s", task.getException());
                        }
                    }
                });
            }
        } catch (SecurityException e) {
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
        // If request is cancelled, the result arrays are empty.
        if (requestCode == PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {
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

        // Default camera position
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(DEFAULT_LOCATION, DEFAULT_ZOOM));

        // Set camera and markers
        if (dataList != null && dataList.size() != 0) {
            // Set Markers
            boolean marked = false;
            int index = 0;
            for (MoodEvent moodEvent : dataList) {
                // Skip MoodEvents that has no location
                if (moodEvent.getLocation() != null) {
                    LatLng location = new LatLng(moodEvent.getLocation().latitude,
                            moodEvent.getLocation().longitude);
                    int resource = getEmotionRadioId(moodEvent.getMoodType());

                    // Move camera to the first MoodEvent in the list
                    if (!marked) {
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                new LatLng(moodEvent.getLocation().latitude,
                                        moodEvent.getLocation().longitude), DEFAULT_ZOOM));
                        marked = true;
                    }

                    String name = "";
                    if (nameList != null) {
                        name = nameList.get(index);
                    }

                    // Add markers
                    googleMap.addMarker(new MarkerOptions().position(location)
                            .title("Date: " + dateFormat.format(moodEvent.getDate())
                                    + "\nTime: " + timeFormat.format(moodEvent.getDate()))
                            .snippet("Description: " + moodEvent.getDescription()
                                    + "\nSocial Condition: "
                                    + getSelectedSocialCondition(moodEvent.getCondition()))
                            .icon(bitmapDescriptor(getApplicationContext(), resource,
                                    getEmotionColor(moodEvent.getMoodType()), name)));
                }
                index++;
            }
        }
        getLocationPermission();
    }

    /**
     * Get the Resource ID (Image) for the respective mood types
     *
     * @param moodId Mood type of the Mood Event
     * @return Returns the Resource ID of the respective mood type
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
                return R.drawable.icon_surprised;
        }
        return R.drawable.icon_neutral;
    }

    /**
     * Get the Color ID for the respective mood types
     *
     * @param moodId Mood type of the MoodEvent
     * @return Returns the Color ID for that mood type.
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
        return R.color.color_neutral;
    }

    /**
     * Returns the selected social condition
     *
     * @param socialCondition Selected social condition from the dropdown (spinner)
     * @return Returns an integer that corresponds to the selected social condition
     */
    // TODO: TEST
    private String getSelectedSocialCondition(@Nullable Integer socialCondition) {
        if (socialCondition == null) {
            return "None";
        }
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
                return "None";
        }
    }

    /**
     * @param context Context of the application
     * @param resID   Resource ID (referring to Image)
     * @param colorID Color ID
     * @return Returns the definition of a bitmap image (map marker)
     */
    private BitmapDescriptor bitmapDescriptor(Context context, int resID, int colorID, String username) {
        // Mood Icon
        Drawable drawable = ContextCompat.getDrawable(context, resID);
        drawable.setBounds(0, 0, MARKER_WIDTH, MARKER_HEIGHT);

        // Circle shape with colors according to mood type
        ShapeDrawable circle = new ShapeDrawable(new OvalShape());
        circle.setIntrinsicHeight(MARKER_HEIGHT);
        circle.setIntrinsicWidth(MARKER_WIDTH);
        circle.getPaint().setColor(ContextCompat.getColor(context, colorID));
        circle.setBounds(new Rect(0, 0, MARKER_WIDTH, MARKER_HEIGHT));

        // Username
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.BLACK);
        paint.setAntiAlias(true);
        paint.setTypeface(Typeface.DEFAULT_BOLD);
        paint.setTextSize(MARKER_FONT);
        paint.setShadowLayer(10, 0, 0, Color.LTGRAY);


        // Layered image (circle, mood icon)
        LayerDrawable finalDrawable = new LayerDrawable(new Drawable[]{circle, drawable});
        Bitmap bitmap = Bitmap.createBitmap(MARKER_WIDTH, MARKER_HEIGHT + 50, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        finalDrawable.draw(canvas);

        if (nameList != null && nameList.size() == dataList.size()) {
            canvas.drawText(username, 0, MARKER_HEIGHT + 20, paint);
        }

        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }
}