package kamrulhasan3288.weatherapp.component;


import dagger.Component;
import kamrulhasan3288.weatherapp.activities.MainActivity;
import kamrulhasan3288.weatherapp.module.MainActivityModule;
import kamrulhasan3288.weatherapp.scope.MainActivityScope;


@Component(modules = MainActivityModule.class, dependencies = WeatherApiComponent.class)
@MainActivityScope
public interface MainActivityComponent {
    void injectMainActivity(MainActivity activity);
}
