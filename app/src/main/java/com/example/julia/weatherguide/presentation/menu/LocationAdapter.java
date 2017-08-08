package com.example.julia.weatherguide.presentation.menu;


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

    private List<LocationWithTemperature> locations;
    private Callbacks callbacks;
    private boolean isDeletionMode;

    public LocationAdapter() {
        this.locations = new ArrayList<>();
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
        isDeletionMode = false;

        int currentSize = this.locations.size();
        if (currentSize != 0) {
            this.locations.clear();
            notifyItemRangeRemoved(0, currentSize);
        }

        if (locations != null) {
            for (LocationWithTemperature location : locations) {
                this.locations.add(location);
            }
        }
        notifyDataSetChanged();

        if (callbacks != null) {
            if (this.locations.size() == 0) {
                callbacks.onLocationsEmpty();
            } else {
                callbacks.onLocationsNotEmpty();
            }
        }
    }

    @Override
    public void removeLocation(LocationWithTemperature location) {
        int index = -1;
        for (int i = 0; i < locations.size(); ++i) {
            if (locations.get(i).location.coordinatesMatches(location.location)) {
                locations.remove(i);
                notifyItemRemoved(i);
                setDeletionMode(false);
                index = i;
                break;
            }
        }

        if (index != -1 && callbacks != null && this.locations.size() == 0) {
            callbacks.onLocationsEmpty();
        }
    }

    @Override
    public void addLocation(LocationWithTemperature location) {
        int index = -1;
        for (int i = 0; i < locations.size(); ++i) {
            if (locations.get(i).location.coordinatesMatches(location.location)) {
                index = i;
                break;
            }
        }

        if (index == -1) {
            locations.add(location);
            notifyItemInserted(locations.size());
            if (callbacks != null) {
                callbacks.onLocationsNotEmpty();
            }
            setDeletionMode(false);
        }
    }

    @Override
    public void setDeletionMode(boolean newValue) {
        if (isDeletionMode != newValue) {
            isDeletionMode = newValue;
            if (callbacks != null) {
                callbacks.onDeletionModeChanged(newValue);
            }
            notifyDataSetChanged();
        }
    }

    @Override
    public boolean isDeletionMode() {
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
                callbacks.onLocationClicked(locationViewHolder.getLocation());
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
            if (isDeletionMode) {
                imageDelete.setVisibility(View.VISIBLE);
                textTemperature.setVisibility(View.GONE);
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