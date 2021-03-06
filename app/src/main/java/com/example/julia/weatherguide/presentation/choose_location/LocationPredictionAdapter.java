package com.example.julia.weatherguide.presentation.choose_location;


import android.support.v7.widget.RecyclerView;

import static android.support.v7.widget.RecyclerView.ViewHolder;

import android.text.Html;
import android.text.Spannable;
import android.text.style.ForegroundColorSpan;
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

    private final int firstLetterColor;

    public LocationPredictionAdapter(int firstLetterColor) {
        locationPredictions = new ArrayList<>();
        this.firstLetterColor = firstLetterColor;
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
        holder.bind(locationPredictions.get(position), firstLetterColor);
    }

    @Override
    public int getItemCount() {
        return locationPredictions.size();
    }

    static class LocationPredictionViewHolder extends ViewHolder {

        private LocationPrediction locationPrediction;
        private final View rootView;
        private final TextView textMain;
        private final TextView textSecondary;

        LocationPredictionViewHolder(View view) {
            super(view);
            rootView = view;
            textMain = (TextView) view.findViewById(R.id.text_main);
            textSecondary = (TextView) view.findViewById(R.id.text_secondary);
        }

        void bind(LocationPrediction locationPrediction, int firstLetterColor) {
            this.locationPrediction = locationPrediction;

            String mainText = locationPrediction.mainText;
            textMain.setText(mainText, TextView.BufferType.SPANNABLE);
            if (!mainText.isEmpty()) {
                Spannable spannable = (Spannable)textMain.getText();
                spannable.setSpan(new ForegroundColorSpan(firstLetterColor), 0, 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            }

            textSecondary.setText(locationPrediction.secondaryText);
        }

        LocationPrediction getLocationPrediction() {
            return locationPrediction;
        }

        void setOnClickListener(View.OnClickListener listener) {
            rootView.setOnClickListener(listener);
        }
    }
}
