package kamrulhasan3288.weatherapp.module;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import java.io.File;


import dagger.Module;
import dagger.Provides;
import kamrulhasan3288.weatherapp.interfaces.ApplicationContext;
import kamrulhasan3288.weatherapp.scope.WeatherApplicationScope;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

@Module(includes = ContextModule.class)
public class OkkHttpModule {

    @Provides
    public OkHttpClient okHttpClient(Cache cache, HttpLoggingInterceptor httpLoggingInterceptor){
        return new OkHttpClient()
                .newBuilder()
                .cache(cache)
                .addInterceptor(httpLoggingInterceptor)
                .build();
    }

    @Provides
    public Cache cache(File cacheFile){
        return new Cache(cacheFile, 10 * 1000 * 1000); //10 MB
    }

    @Provides
    @WeatherApplicationScope
    public File file(@ApplicationContext Context context){
        return new File(context.getCacheDir(), "HttpCache");
    }

    @Provides
    public HttpLoggingInterceptor httpLoggingInterceptor(){
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(@NonNull String message) {
                Log.d("NETWORK",message);
            }
        });
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return httpLoggingInterceptor;
    }

}
