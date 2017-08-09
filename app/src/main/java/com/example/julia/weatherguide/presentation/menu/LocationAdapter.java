package com.example.julia.weatherguide.presentation.menu;

import android.support.annotation.ColorRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static android.support.v7.widget.RecyclerView.ViewHolder;

import com.example.julia.weatherguide.R;
import com.example.julia.weatherguide.presentation.menu.LocationAdapter.LocationViewHolder;
import com.example.julia.weatherguide.data.entities.presentation.location.LocationWithTemperature;


public class LocationAdapter extends RecyclerView.Adapter<LocationViewHolder> implements LocationModel {

    private final int CURRENT_LOCATION_COLOR;
    private final int TRANSPARENT_COLOR;
    private static final int MAIN_VIEW_TYPE = 0;

    private List<LocationWithTemperature> locations;
    private Callbacks callbacks;
    private boolean isDeletionMode;


    public LocationAdapter(int currentLocationColor, int transparentColor) {
        CURRENT_LOCATION_COLOR = currentLocationColor;
        TRANSPARENT_COLOR = transparentColor;
        locations = new ArrayList<>();
        setHasStableIds(true);
    }

    // ---------------------------------------- LocationModel -----------------------------------------

    @Override
    public void attachCallbacks(Callbacks callbacks) {
        this.callbacks = callbacks;
    }

    @Override
    public void detachCallbacks() {
        this.callbacks = null;
    }

    @Override
    public void setupLocations(List<LocationWithTemperature> locations) {
        // clear locations
        this.locations.clear();

        // set locations
        if (locations != null) this.locations.addAll(locations);

        // notify changes
        isDeletionMode = false;
        notifyDataSetChanged();
        if (callbacks != null) {
            callbacks.onDeletionModeChanged(false);
            if (this.locations.size() <= 1) {
                callbacks.onLocationsCanBeDeleted();
            } else {
                callbacks.onLocationsCannotBeDeleted();
            }
        }
    }

    @Override
    public void setDeletionMode(boolean newValue) {
        if (isDeletionMode != newValue) {
            isDeletionMode = newValue;
            if (callbacks != null) callbacks.onDeletionModeChanged(newValue);
            notifyDataSetChanged();
        }
    }

    @Override
    public boolean isInDeletionMode() {
        return isDeletionMode;
    }

    // ----------------------------------- RecyclerView.Adapter -----------------------------------

    @Override
    public int getItemCount() {
        return locations.size();
    }

    @Override
    public LocationViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
        final View view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_location, parent, false);

        final LocationViewHolder locationViewHolder = new LocationViewHolder(view);
        locationViewHolder.setOnClickListener(v -> {
            if (callbacks != null) {
                if (!locationViewHolder.getLocation().location.isCurrent) {
                    callbacks.onLocationClicked(locationViewHolder.getLocation());
                }
            }
        });
        locationViewHolder.setOnDeleteClickListener(v -> {
            if (callbacks != null) {
                callbacks.onLocationRemoveClicked(locationViewHolder.getLocation());
            }
        });

        return locationViewHolder;
    }


    @Override
    public void onBindViewHolder(LocationViewHolder holder, int position) {
        holder.bind(locations.get(position), isDeletionMode);
    }

    @Override
    public int getItemViewType(int position) {
        return MAIN_VIEW_TYPE;
    }

    // -------------------------------------- inner types -----------------------------------------


    class LocationViewHolder extends ViewHolder {

        private LocationWithTemperature location;
        private final View rootView;
        private final TextView textLocationName;
        private final TextView textTemperature;
        private final ImageView imageDelete;

        public LocationViewHolder(View view) {
            super(view);
            rootView = view;
            textLocationName = (TextView) view.findViewById(R.id.text_location_name);
            textTemperature = (TextView) view.findViewById(R.id.text_temperature);
            imageDelete = (ImageView) view.findViewById(R.id.image_delete);
            imageDelete.setVisibility(View.GONE);
            textTemperature.setVisibility(View.VISIBLE);
        }

        public void bind(LocationWithTemperature location, boolean isDeletionMode) {
            this.location = location;
            textLocationName.setText(location.location.name);

            if (location.location.isCurrent) {
                rootView.setBackgroundColor(CURRENT_LOCATION_COLOR);
            } else {
                rootView.setBackgroundColor(TRANSPARENT_COLOR);
            }

            if (isDeletionMode) {
                if (!location.location.isCurrent) {
                    imageDelete.setVisibility(View.VISIBLE);
                    textTemperature.setVisibility(View.GONE);
                } else {
                    imageDelete.setVisibility(View.GONE);
                    textTemperature.setVisibility(View.INVISIBLE);
                }
            } else {
                imageDelete.setVisibility(View.GONE);
                textTemperature.setVisibility(View.VISIBLE);
            }
        }

        public LocationWithTemperature getLocation() {
            return location;
        }

        public void setOnClickListener(View.OnClickListener listener) {
            rootView.setOnClickListener(listener);
        }

        private void setOnDeleteClickListener(View.OnClickListener listener) {
            imageDelete.setOnClickListener(listener);
        }
    }

}