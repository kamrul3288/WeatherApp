package kamrulhasan3288.weatherapp.module;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dagger.Module;
import dagger.Provides;
import kamrulhasan3288.weatherapp.interfaces.WeatherApi;
import kamrulhasan3288.weatherapp.scope.WeatherApplicationScope;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module(includes = OkkHttpModule.class)
public class WeatherApiModule {

    @Provides
    public WeatherApi randomUsersApi(Retrofit retrofit){
        return retrofit.create(WeatherApi.class);
    }


    @WeatherApplicationScope
    @Provides
    public Retrofit retrofit(OkHttpClient okHttpClient, GsonConverterFactory gsonConverterFactory){
        return new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl("http://api.openweathermap.org/")
                .addConverterFactory(gsonConverterFactory)
                .build();
    }

    @Provides
    public GsonConverterFactory gsonConverterFactory(Gson gson){
        return GsonConverterFactory.create(gson);
    }

    @Provides
    public Gson gson(){
        GsonBuilder gsonBuilder = new GsonBuilder();
        return gsonBuilder.create();
    }


}
