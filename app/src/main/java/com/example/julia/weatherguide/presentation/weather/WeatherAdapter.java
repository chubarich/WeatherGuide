package com.example.julia.weatherguide.presentation.weather;

import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.julia.weatherguide.R;
import com.example.julia.weatherguide.data.entities.presentation.weather.CurrentWeather;
import com.example.julia.weatherguide.data.entities.presentation.weather.Weather;
import com.example.julia.weatherguide.data.entities.presentation.weather.WeatherPrediction;

import static com.example.julia.weatherguide.presentation.weather.WeatherAdapter.HolderType.CURRENT;
import static com.example.julia.weatherguide.presentation.weather.WeatherAdapter.HolderType.PREDICTION;

public class WeatherAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
    implements WeatherModel {

    private Weather weather;
    private Resources resources;

    public WeatherAdapter() {
    }

    // ------------------------------------- WeatherModel -----------------------------------------

    @Override
    public void setWeather(Weather weather) {
        this.weather = weather;
        notifyDataSetChanged();
    }

    @Override
    public void bindResources(Resources resources) {
        this.resources = resources;
    }

    @Override
    public void unbindResources() {
        resources = null;
    }

    // ---------------------------------- RecyclerView.Adapter ------------------------------------

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        if (viewType == CURRENT.getLayoutRes()) {
            return new CurrentWeatherViewHolder(view);
        } else if (viewType == PREDICTION.getLayoutRes()) {
            return new PredictionWeatherViewHolder(view);
        } else {
            throw new IllegalStateException("Unknown view type");
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (resources != null) {
            long positionViewType = getItemViewType(position);
            if (positionViewType == CURRENT.getLayoutRes() && holder instanceof CurrentWeatherViewHolder) {
                ((CurrentWeatherViewHolder)holder).bind(weather.getCurrent(), resources);
            } else if (positionViewType == PREDICTION.getLayoutRes() && holder instanceof PredictionWeatherViewHolder) {
                ((PredictionWeatherViewHolder)holder).bind(weather.getPredictionAt(position - 1), resources);
            }
        }
    }

    @Override
    public int getItemCount() {
        int size = weather == null ? 0 : (weather.getPredictionsSize() + 1);
        return size;
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0
            ? CURRENT.getLayoutRes()
            : PREDICTION.getLayoutRes();
    }

    // -------------------------------------- inner types -----------------------------------------

    private static class CurrentWeatherViewHolder extends RecyclerView.ViewHolder {

        private TextView textDate;
        private TextView textTemperature;
        private TextView textConditionDescription;
        private ImageView imageConditionIcon;

        CurrentWeatherViewHolder(View view) {
            super(view);
            textDate = (TextView) view.findViewById(R.id.text_date);
            textTemperature = (TextView) view.findViewById(R.id.text_temperature);
            textConditionDescription = (TextView) view.findViewById(R.id.text_condition_description);
            imageConditionIcon = (ImageView) view.findViewById(R.id.image_condition);
        }

        void bind(CurrentWeather weather, Resources resources) {
            textDate.setText(weather.getDatetimeOfUpdate());

            int temperature = weather.getMainTemperature();
            textTemperature.setText(getTemperatureDescription(temperature, resources));

            textConditionDescription.setText(weather.getConditionDescription());

            imageConditionIcon.setBackgroundResource(weather.getConditionIconResource());
        }

        private static String getTemperatureDescription(int temperature, Resources resources) {
            return (temperature > 0 ? "+" : "") + temperature + resources.getString(R.string.degree);
        }
    }

    private static class PredictionWeatherViewHolder extends RecyclerView.ViewHolder {

        private TextView textMinTemperature;
        private TextView textMaxTemperature;
        private TextView textDate;
        private ImageView imageCondition;

        PredictionWeatherViewHolder(View view) {
            super(view);
            textMinTemperature = (TextView) view.findViewById(R.id.text_min_temperature);
            textMaxTemperature = (TextView) view.findViewById(R.id.text_max_temperature);
            textDate = (TextView) view.findViewById(R.id.text_date);
            imageCondition = (ImageView) view.findViewById(R.id.image_condition);
        }

        void bind(WeatherPrediction prediction, Resources resources) {
            textMinTemperature.setText(getTemperatureDescription((int)prediction.getMinTemperature(), resources));
            textMaxTemperature.setText(getTemperatureDescription((int)prediction.getMaxTemperature(), resources));
            textDate.setText(prediction.getDate());
            imageCondition.setBackgroundResource(prediction.getConditionIconId());
        }

        private static String getTemperatureDescription(int temperature, Resources resources) {
            return (temperature > 0 ? "+" : "") + temperature + resources.getString(R.string.degree);
        }
    }

    enum HolderType {

        CURRENT(R.layout.item_current_weather),
        PREDICTION(R.layout.item_prediction_weather);

        private final int layoutRes;

        HolderType(int layoutRes) {
            this.layoutRes = layoutRes;
        }

        public int getLayoutRes() {
            return layoutRes;
        }
    }

}
