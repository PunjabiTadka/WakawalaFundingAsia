package com.wakawala.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wakawala.ApplicationLoader;
import com.wakawala.R;
import com.wakawala.model.PlaceModel;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SelectPlaceAdapter extends RecyclerView.Adapter<SelectPlaceAdapter.SelectPlaceViewHolder> {

    List<PlaceModel> data;
    boolean isCountry;

    public SelectPlaceAdapter(List<PlaceModel> data, boolean isCountry) {
        this.data = data;
        this.isCountry = isCountry;
    }

    @NonNull
    @Override
    public SelectPlaceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SelectPlaceViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.place_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SelectPlaceViewHolder holder, int position) {
        try {
            PlaceModel current = data.get(position);
            if (current != null) {
                holder.name.setText(current.getName());
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (isCountry) {
                            ApplicationLoader.getInstance().setCampaignCountry(current.getName());
                            ApplicationLoader.getInstance().setCampaignCountryId(current.getId());
                        } else {
                            ApplicationLoader.getInstance().setCampaignCity(current.getName());
                            ApplicationLoader.getInstance().setCampaignCityId(current.getId());
                        }
                        ((Activity) v.getContext()).finish();
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return data != null ? data.size() : 0;
    }

    class SelectPlaceViewHolder extends RecyclerView.ViewHolder {

        TextView name;

        public SelectPlaceViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
        }
    }
}
