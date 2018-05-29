package kamrulhasan3288.weatherapp.adepter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import kamrulhasan3288.weatherapp.R;
import kamrulhasan3288.weatherapp.activities.WeatherMapDetailsActivity;
import kamrulhasan3288.weatherapp.infrastructure.Util;

public class WeatherResultListAdepter extends RecyclerView.Adapter<WeatherResultListAdepter.WeatherResultListViewHolder>{

    private List<kamrulhasan3288.weatherapp.model.List> weatherResultLists;
    private Activity activity;
    private Typeface typeface;

    public WeatherResultListAdepter(Activity activity) {
        this.activity = activity;
        typeface = Typeface.createFromAsset(activity.getAssets(), "fonts/roboto_regular.ttf");
    }

    @NonNull
    @Override
    public WeatherResultListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_weather_result_list,parent,false);
        return new WeatherResultListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherResultListViewHolder holder, int position) {
        kamrulhasan3288.weatherapp.model.List list = weatherResultLists.get(position);

        holder.placeNameText.setTypeface(typeface);
        holder.weatherStatusText.setTypeface(typeface);
        holder.weatherTemperatureText.setTypeface(typeface);

        final String placeName = list.getName();
        final String weatherDescription = list.getWeather().get(0).getDescription();
        final String currentTemperature = String.valueOf(list.getMain().getTemp()) + activity.getResources().getString(R.string.celsius);
        final String maxTemp = "Max Temp: "+String.valueOf(list.getMain().getTempMax()) + activity.getResources().getString(R.string.celsius);
        final String minTemp = "Min Temp: "+String.valueOf(list.getMain().getTempMin()) + activity.getResources().getString(R.string.celsius);
        final String windSpeed = "Wind Speed: "+String.valueOf(list.getWind().getSpeed());
        final String humidity = "Humidity: "+String.valueOf(list.getMain().getHumidity());
        final double lat = list.getCoord().getLat();
        final double lon = list.getCoord().getLon();


        holder.placeNameText.setText(placeName);
        holder.weatherStatusText.setText(weatherDescription);
        holder.weatherTemperatureText.setText(currentTemperature);

        holder.singleRow.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            activity.startActivity(new Intent(activity, WeatherMapDetailsActivity.class)
                    .putExtra(Util.LOCATION_NAME,placeName)
                    .putExtra(Util.WEATHER_DESCRIPTION,weatherDescription)
                    .putExtra(Util.CURRENT_TEMP,currentTemperature)
                    .putExtra(Util.MAX_TEMP,maxTemp)
                    .putExtra(Util.MIN_TEMP,minTemp)
                    .putExtra(Util.WIND_SPEED,windSpeed)
                    .putExtra(Util.HUMIDITY,humidity)
                    .putExtra(Util.LATITUDE,lat)
                    .putExtra(Util.LONGITUDE,lon)
            );
        }
    });
}

    @Override
    public int getItemCount() {
        return weatherResultLists.size();
    }

    public void setItems(List<kamrulhasan3288.weatherapp.model.List> results) {
        weatherResultLists = results;
    }

    class WeatherResultListViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.list_weather_result_list_placeName)
        TextView placeNameText;

        @BindView(R.id.list_weather_result_list_weather_status)
        TextView weatherStatusText;

        @BindView(R.id.list_weather_result_list_weather_temperature)
        TextView weatherTemperatureText;

        View singleRow;

        WeatherResultListViewHolder(View itemView) {
            super(itemView);
            singleRow = itemView;
            ButterKnife.bind(this,itemView);
        }
    }
}
