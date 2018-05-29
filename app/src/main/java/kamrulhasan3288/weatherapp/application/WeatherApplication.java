package kamrulhasan3288.weatherapp.application;

import android.app.Activity;
import android.app.Application;

import kamrulhasan3288.weatherapp.component.DaggerWeatherApiComponent;
import kamrulhasan3288.weatherapp.component.WeatherApiComponent;
import kamrulhasan3288.weatherapp.module.ContextModule;

public class WeatherApplication extends Application {
    private WeatherApiComponent weatherApiComponentApplication;

    public static WeatherApplication get(Activity activity){
        return (WeatherApplication) activity.getApplication();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        weatherApiComponentApplication = DaggerWeatherApiComponent.builder()
                .contextModule(new ContextModule(this))
                .build();
    }

    public WeatherApiComponent getWeatherApiComponentApplication() {
        return weatherApiComponentApplication;
    }
}
