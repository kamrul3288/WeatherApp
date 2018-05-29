package kamrulhasan3288.weatherapp.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import javax.inject.Inject;
import butterknife.BindView;
import butterknife.ButterKnife;
import kamrulhasan3288.weatherapp.R;
import kamrulhasan3288.weatherapp.adepter.WeatherResultListAdepter;
import kamrulhasan3288.weatherapp.application.WeatherApplication;
import kamrulhasan3288.weatherapp.component.DaggerMainActivityComponent;
import kamrulhasan3288.weatherapp.component.MainActivityComponent;
import kamrulhasan3288.weatherapp.infrastructure.Util;
import kamrulhasan3288.weatherapp.interfaces.WeatherApi;
import kamrulhasan3288.weatherapp.model.CurrentWeather;
import kamrulhasan3288.weatherapp.model.WeatherResultList;
import kamrulhasan3288.weatherapp.module.MainActivityModule;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener{

    //-------------xml instance-------------------
    @BindView(R.id.main_activity_recyclerView)
    RecyclerView weatherResultListRecyclerView;

    //--------------class instance---------------
    private GoogleApiClient googleApiClient;
    PendingResult<LocationSettingsResult> result;
    private LocationRequest mLocationRequest;

    @Inject
    WeatherApi weatherApi;

    @Inject
    WeatherResultListAdepter adepter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);



        //---------check runtime permission------------------
        checkRuntimePermission();

        MainActivityComponent mainActivityComponent = DaggerMainActivityComponent.builder()
                .mainActivityModule(new MainActivityModule(this))
                .weatherApiComponent(WeatherApplication.get(this).getWeatherApiComponentApplication())
                .build();
        mainActivityComponent.injectMainActivity(this);


        //---------action bar---------------------
        setUpSupportActionBar();
        configureRecyclerVIew();
        //--------------fire up weather data--------------
        populateWeathers();

    }

    private void setUpSupportActionBar() {
        Toolbar toolbar = findViewById(R.id.main_activity_toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
    }


    private void populateWeathers() {
        Call<WeatherResultList> call = weatherApi.getWeatherData(23.68, 90.35, 50, "e384f9ac095b2109c751d95296f8ea76", "metric");
        call.enqueue(new Callback<WeatherResultList>() {
            @Override
            public void onResponse(@NonNull Call<WeatherResultList> call, @NonNull Response<WeatherResultList> response) {
                if (response.isSuccessful()) {
                    adepter.setItems(response.body().getList());
                    weatherResultListRecyclerView.setAdapter(adepter);
                    adepter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(@NonNull Call<WeatherResultList> call, @NonNull Throwable t) {

            }
        });
    }

    private void configureRecyclerVIew() {
        weatherResultListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void checkRuntimePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, Util.LOCATION_REQUEST_CODE);
        } else {
            buildGoogleApiClient();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case Util.LOCATION_REQUEST_CODE: {
                //----------phone state permission result---------------
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    buildGoogleApiClient();
                } else {
                    checkRuntimePermission();
                }
            }
        }

    }

    private void buildGoogleApiClient() {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
        googleApiClient.connect();
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(mLocationRequest);
        builder.setAlwaysShow(true);
        checkLocationSetting(builder);

        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient,mLocationRequest,this);
    }



    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            populateCurrentWeatherData(location.getLatitude(),location.getLongitude());
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
            googleApiClient.disconnect();

        }

    }


    public void checkLocationSetting(LocationSettingsRequest.Builder builder) {
        result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(@NonNull LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        try {
                            status.startResolutionForResult(MainActivity.this, Util.LOCATION_SETTING_REQUEST_CODE);
                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        break;
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Util.LOCATION_SETTING_REQUEST_CODE:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        break;
                    case Activity.RESULT_CANCELED:
                        Toast.makeText(getApplicationContext(),"Please enable permission for getting notification",Toast.LENGTH_SHORT).show();
                        break;
                }
                break;
            default:
                break;
        }
    }



    private void populateCurrentWeatherData(double lat, double lng) {
        Call<CurrentWeather> call = weatherApi.getCurrentWeatherData(lat, lng, 50, "e384f9ac095b2109c751d95296f8ea76", "metric");
        call.enqueue(new Callback<CurrentWeather>() {
            @Override
            public void onResponse(@NonNull Call<CurrentWeather> call, @NonNull Response<CurrentWeather> response) {
                if (response.isSuccessful()) {
                    String temp = "Current Temperature: "+ response.body().getMain().getTemp() + getResources().getString(R.string.celsius);
                    sendCustomNotifications(temp);
                }
            }

            @Override
            public void onFailure(@NonNull Call<CurrentWeather> call, @NonNull Throwable t) {

            }
        });
    }

    private void sendCustomNotifications(String temp) {
        RemoteViews remoteViews = new RemoteViews(getPackageName(),R.layout.custom_notification);

        remoteViews.setImageViewResource(R.id.left_image,R.mipmap.ic_launcher_round);
        remoteViews.setTextViewText(R.id.title,getResources().getString(R.string.app_name));
        remoteViews.setTextViewText(R.id.temperature,temp);

        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(MainActivity.this,"tempNotification");
        builder.setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true)
                .setCustomContentView(remoteViews)
                .setSound(soundUri)
                .setChannelId("tempNotification");

        assert notificationManager != null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel("tempNotification", "Weather App", importance);
            mChannel.setDescription("notification");
            mChannel.enableLights(true);
            mChannel.setLightColor(ContextCompat.getColor(getApplicationContext(),R.color
                    .colorPrimary));

            notificationManager.createNotificationChannel(mChannel);

        }
        builder.getNotification().flags |= Notification.FLAG_AUTO_CANCEL;
        notificationManager.notify(0,builder.build());

    }


}




