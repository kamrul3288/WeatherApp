package kamrulhasan3288.weatherapp.component;


import dagger.Component;
import kamrulhasan3288.weatherapp.interfaces.WeatherApi;
import kamrulhasan3288.weatherapp.module.WeatherApiModule;
import kamrulhasan3288.weatherapp.scope.WeatherApplicationScope;


@Component(modules = WeatherApiModule.class)
@WeatherApplicationScope
public interface WeatherApiComponent {
    WeatherApi getWeatherApi();
}
