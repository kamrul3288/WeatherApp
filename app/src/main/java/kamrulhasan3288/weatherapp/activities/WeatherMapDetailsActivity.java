package kamrulhasan3288.weatherapp.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import butterknife.BindView;
import butterknife.ButterKnife;
import kamrulhasan3288.weatherapp.R;
import kamrulhasan3288.weatherapp.infrastructure.Util;

public class WeatherMapDetailsActivity extends AppCompatActivity implements OnMapReadyCallback{

    //------------xml instance------------------
    @BindView(R.id.weather_map_details_activity_locationName)
    TextView locationNameET;

    @BindView(R.id.weather_map_details_activity_weatherDescription)
    TextView weatherDescriptionET;

    @BindView(R.id.weather_map_details_activity_humidity)
    TextView humidityET;

    @BindView(R.id.weather_map_details_activity_windSpeed)
    TextView windSpeedET;

    @BindView(R.id.weather_map_details_activity_maxTemp)
    TextView maxTempET;

    @BindView(R.id.weather_map_details_activity_minTemp)
    TextView minTempET;

    @BindView(R.id.weather_map_details_activity_currentTemp)
    TextView currentTempET;

    //--------------class instance----------------------
    private GoogleMap mGoogleMap;
    private String locationName;
    private double latitude,longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_map_details);
        ButterKnife.bind(this);

        //---------action bar---------------------
        setUpSupportActionBar();

        //--------------set up google map-------------------
        initializeGoogleMap();


    }


    private void setUpSupportActionBar() {
        Toolbar toolbar = findViewById(R.id.weather_map_details_activity_toolBar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void initializeGoogleMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.weather_map_details_activity_google_map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        getExtraDataPassFromMainActivity();
    }

    private void getExtraDataPassFromMainActivity() {
        Bundle extras = getIntent().getExtras();
        if (extras != null){
            String weatherDescription = extras.getString(Util.WEATHER_DESCRIPTION);
            String maxTemp = extras.getString(Util.MAX_TEMP);
            String minTemp = extras.getString(Util.MIN_TEMP);
            String humidity = extras.getString(Util.HUMIDITY);
            String windSpeed = extras.getString(Util.WIND_SPEED);
            String currentTemp = extras.getString(Util.CURRENT_TEMP);
            locationName = extras.getString(Util.LOCATION_NAME);
            latitude = extras.getDouble(Util.LATITUDE);
            longitude = extras.getDouble(Util.LONGITUDE);

            locationNameET.setText(locationName);
            weatherDescriptionET.setText(weatherDescription);
            currentTempET.setText(currentTemp);
            maxTempET.setText(maxTemp);
            minTempET.setText(minTemp);
            humidityET.setText(humidity);
            windSpeedET.setText(windSpeed);


            setMarkerOnGoogleMapPosition();

        }
    }


    private void setMarkerOnGoogleMapPosition() {
        LatLng locationLatLng = new LatLng(latitude, longitude);
        mGoogleMap.addMarker(new MarkerOptions().position(locationLatLng).title(locationName));
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(locationLatLng,10));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            super.onBackPressed();
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
