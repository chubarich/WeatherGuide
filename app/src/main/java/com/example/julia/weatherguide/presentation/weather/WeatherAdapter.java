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

import net.cachapa.expandablelayout.ExpandableLayout;

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
        holder.setIsRecyclable(false);
        if (resources != null) {
            long positionViewType = getItemViewType(position);
            if (positionViewType == CURRENT.getLayoutRes() && holder instanceof CurrentWeatherViewHolder) {
                ((CurrentWeatherViewHolder) holder).bind(weather.getCurrent(), resources);
            } else if (positionViewType == PREDICTION.getLayoutRes() && holder instanceof PredictionWeatherViewHolder) {
                ((PredictionWeatherViewHolder) holder).bind(weather.getPredictionAt(position - 1), resources);
            }
        }
    }

    @Override
    public int getItemCount() {
        return weather == null ? 0 : (weather.getPredictionsSize() + 1);
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
        private TextView textWind;
        private TextView textHumidity;
        private TextView textPressure;
        private ImageView imageConditionIcon;


        CurrentWeatherViewHolder(View view) {
            super(view);
            textDate = (TextView) view.findViewById(R.id.text_date);
            textTemperature = (TextView) view.findViewById(R.id.text_temperature);
            textConditionDescription = (TextView) view.findViewById(R.id.text_condition_description);
            imageConditionIcon = (ImageView) view.findViewById(R.id.image_condition);
            textWind = (TextView) view.findViewById(R.id.text_wind);
            textHumidity = (TextView) view.findViewById(R.id.text_humidity);
            textPressure = (TextView) view.findViewById(R.id.text_pressure);
        }

        void bind(CurrentWeather weather, Resources resources) {
            textDate.setText(resources.getString(R.string.updated) + " " + weather.getDatetimeOfUpdate());

            int temperature = weather.getMainTemperature();
            textTemperature.setText(getTemperatureDescription(temperature, resources));
            textConditionDescription.setText(weather.getConditionDescription());
            imageConditionIcon.setBackgroundResource(weather.getConditionIconResource());

            textWind.setText(weather.getWindSummary());
            textHumidity.setText(weather.getHumidity() + "%");
            textPressure.setText(weather.getPressureSummary());
        }

        private static String getTemperatureDescription(int temperature, Resources resources) {
            return (temperature > 0 ? "+" : "") + temperature + resources.getString(R.string.degree);
        }
    }

    private static class PredictionWeatherViewHolder extends RecyclerView.ViewHolder
        implements View.OnClickListener {

        private TextView textMinTemperature;
        private TextView textMaxTemperature;
        private TextView textDayTemperature;
        private TextView textMorningTemperature;
        private TextView textEveningTemperature;
        private TextView textNightTemperature;
        private TextView textDate;
        private ImageView imageCondition;
        private ExpandableLayout expandableView;
        private TextView textWind;
        private TextView textHumidity;
        private TextView textPressure;

        PredictionWeatherViewHolder(View view) {
            super(view);
            textMinTemperature = (TextView) view.findViewById(R.id.text_min_temperature);
            textMaxTemperature = (TextView) view.findViewById(R.id.text_max_temperature);
            textDate = (TextView) view.findViewById(R.id.text_date);
            imageCondition = (ImageView) view.findViewById(R.id.image_condition);
            textWind = (TextView) view.findViewById(R.id.text_wind);
            textHumidity = (TextView) view.findViewById(R.id.text_humidity);
            textPressure = (TextView) view.findViewById(R.id.text_pressure);
            textDayTemperature = (TextView) view.findViewById(R.id.text_day_temperature);
            textMorningTemperature = (TextView) view.findViewById(R.id.text_morning_temperature);
            textEveningTemperature = (TextView) view.findViewById(R.id.text_evening_temperature);
            textNightTemperature = (TextView) view.findViewById(R.id.text_night_temperature);

            expandableView = (ExpandableLayout) view.findViewById(R.id.expandable_view);

            view.setOnClickListener(this);
        }

        void bind(WeatherPrediction prediction, Resources resources) {
            textMinTemperature.setText(getTemperatureDescription((int) prediction.getMinTemperature(), resources));
            textMaxTemperature.setText(getTemperatureDescription((int) prediction.getMaxTemperature(), resources));
            textDate.setText(prediction.getDate());
            imageCondition.setBackgroundResource(prediction.getConditionIconId());

            textWind.setText(prediction.getWindSummary());
            textHumidity.setText(prediction.getHumidity() + "%");
            textPressure.setText(prediction.getPressureSummary());

            textDayTemperature.setText(getTemperatureDescription((int)prediction.getDayTemperature(), resources));
            textMorningTemperature.setText(getTemperatureDescription((int)prediction.getMorningTemperature(), resources));
            textEveningTemperature.setText(getTemperatureDescription((int)prediction.getEveningTemperature(), resources));
            textNightTemperature.setText(getTemperatureDescription((int)prediction.getNightTemperature(), resources));
        }

        private static String getTemperatureDescription(int temperature, Resources resources) {
            return (temperature > 0 ? "+" : "") + temperature + resources.getString(R.string.degree);
        }

        @Override
        public void onClick(View view) {
            expandableView.toggle();
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
