package kamrulhasan3288.weatherapp.interfaces;


import kamrulhasan3288.weatherapp.model.CurrentWeather;
import kamrulhasan3288.weatherapp.model.WeatherResultList;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherApi {

    @GET("/data/2.5/find?")
    Call<WeatherResultList> getWeatherData(@Query("lat") double lat, @Query("lon") double lon, @Query("cnt") int cnt, @Query("appid") String appid,@Query("units") String units);

    @GET("data/2.5/weather?")
    Call<CurrentWeather> getCurrentWeatherData(@Query("lat") double lat, @Query("lon") double lon, @Query("cnt") int cnt, @Query("appid") String appid, @Query("units") String units);


}
