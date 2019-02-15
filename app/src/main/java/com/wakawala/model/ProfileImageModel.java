package com.wakawala.model;

import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.wakawala.R;

import androidx.databinding.BindingAdapter;

public class ProfileImageModel {
    private String image;

    public ProfileImageModel(String image) {
        this.image = image;
    }

    public String getImageUrl() {
        // The URL will usually come from a model (i.e Profile)
        return image;
    }

    @BindingAdapter({"bind:imageUrl"})
    public static void loadImage(ImageView view, String imageUrl) {

        try {
            if (!imageUrl.isEmpty())
                Picasso.with(view.getContext())
                        .load(imageUrl)
                        .into(view);
        }catch (Exception e){

        }

    }
}
