package kamrulhasan3288.weatherapp.module;


import dagger.Module;
import dagger.Provides;
import kamrulhasan3288.weatherapp.activities.MainActivity;
import kamrulhasan3288.weatherapp.adepter.WeatherResultListAdepter;
import kamrulhasan3288.weatherapp.scope.MainActivityScope;

@Module
public class MainActivityModule {

    private final MainActivity activity;
    public MainActivityModule(MainActivity activity) {
        this.activity = activity;
    }


    @MainActivityScope
    @Provides
    public WeatherResultListAdepter weatherResultListAdepter(){
        return new WeatherResultListAdepter(activity);
    }
}
