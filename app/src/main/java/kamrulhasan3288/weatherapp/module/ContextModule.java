package kamrulhasan3288.weatherapp.module;

import android.content.Context;
import dagger.Module;
import dagger.Provides;
import kamrulhasan3288.weatherapp.interfaces.ApplicationContext;
import kamrulhasan3288.weatherapp.scope.WeatherApplicationScope;

@Module
public class ContextModule {
    private Context context;

    public ContextModule(Context context) {
        this.context = context;
    }

    @ApplicationContext
    @WeatherApplicationScope
    @Provides
    public Context context(){
        return context.getApplicationContext();
    }
}
