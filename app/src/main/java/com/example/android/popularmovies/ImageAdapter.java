package com.example.android.popularmovies;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.android.popularmovies.beans.MyMovie;
import com.squareup.picasso.Picasso;

import java.util.List;

import info.movito.themoviedbapi.model.core.MovieResultsPage;

class ImageAdapter extends BaseAdapter {
    private List<String> covers;
    private Context mContext;

    public ImageAdapter(Context c, List<String> covers) {
        this.covers = covers;
        this.mContext = c;
    }

    @Override
    public int getCount() {
        return sampleImages.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            imageView = new ImageView(mContext);
            imageView.setAdjustViewBounds(true);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        } else {
            imageView = (ImageView) convertView;
        }
        imageView.setImageResource(sampleImages[position]);
        Picasso.get().load(
                "https://image.tmdb.org/t/p/w300"
                        + covers.get(position)
        ).into(imageView);
        return imageView;
    }

    private Integer[] sampleImages = {
            R.drawable.sample_0, R.drawable.sample_1,
            R.drawable.sample_2, R.drawable.sample_3,
            R.drawable.sample_4, R.drawable.sample_5,
            R.drawable.sample_6, R.drawable.sample_7,
            R.drawable.sample_0, R.drawable.sample_1
    };
}
