package com.example.julia.weatherguide.presentation.choose_location;


import android.support.v7.widget.RecyclerView;

import static android.support.v7.widget.RecyclerView.ViewHolder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.julia.weatherguide.R;
import com.example.julia.weatherguide.data.entities.presentation.location.LocationPrediction;
import com.example.julia.weatherguide.presentation.choose_location.LocationPredictionAdapter.LocationPredictionViewHolder;

import java.util.ArrayList;
import java.util.List;


public class LocationPredictionAdapter extends RecyclerView.Adapter<LocationPredictionViewHolder>
    implements LocationPredictionModel {

    private List<LocationPrediction> locationPredictions;
    private Callbacks callbacks;

    public LocationPredictionAdapter() {
        locationPredictions = new ArrayList<>();
    }

    // -------------------------------- LocationPredictionModel ---------------------------------------

    @Override
    public void setPredictions(List<LocationPrediction> locationPredictions) {
        int currentSize = this.locationPredictions.size();
        if (currentSize != 0) {
            this.locationPredictions.clear();
            notifyItemRangeRemoved(0, currentSize);
        }

        if (locationPredictions != null) {
            for (LocationPrediction locationPrediction : locationPredictions) {
                this.locationPredictions.add(locationPrediction);
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public void attachCallbacks(Callbacks callbacks) {
        this.callbacks = callbacks;
    }

    @Override
    public void detachCallbacks() {
        this.callbacks = null;
    }

    // -------------------------------- RecyclerView.Adapter --------------------------------------

    @Override
    public LocationPredictionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LocationPredictionViewHolder viewHolder = new LocationPredictionViewHolder(
            LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_location_prediction, parent, false)
        );

        viewHolder.setOnClickListener(v -> {
                if (callbacks != null) {
                    callbacks.onPredictionClickListener(viewHolder.getLocationPrediction());
                }
            }
        );

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(LocationPredictionViewHolder holder, int position) {
        holder.bind(locationPredictions.get(position));
    }

    @Override
    public int getItemCount() {
        return locationPredictions.size();
    }

    public static class LocationPredictionViewHolder extends ViewHolder {

        private LocationPrediction locationPrediction;
        private final View rootView;
        private final TextView textMain;
        private final TextView textSecondary;

        public LocationPredictionViewHolder(View view) {
            super(view);
            rootView = view;
            textMain = (TextView) view.findViewById(R.id.text_main);
            textSecondary = (TextView) view.findViewById(R.id.text_secondary);
        }

        public void bind(LocationPrediction locationPrediction) {
            this.locationPrediction = locationPrediction;
            textMain.setText(locationPrediction.mainText);
            textSecondary.setText(locationPrediction.secondaryText);
        }

        public LocationPrediction getLocationPrediction() {
            return locationPrediction;
        }

        public void setOnClickListener(View.OnClickListener listener) {
            rootView.setOnClickListener(listener);
        }

    }

}
