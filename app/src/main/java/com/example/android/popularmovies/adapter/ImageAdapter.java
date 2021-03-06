package com.example.android.popularmovies.adapter;

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

import com.squareup.picasso.Picasso;

import java.util.List;

import info.movito.themoviedbapi.model.core.MovieResultsPage;

public class ImageAdapter extends BaseAdapter {
    private List<String> covers;
    private Context mContext;

    public ImageAdapter(Context c, List<String> covers) {
        this.covers = covers;
        this.mContext = c;
    }

    @Override
    public int getCount() {
        return covers.size();
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
//        imageView.setImageResource(sampleImages[position]);
        Picasso.get().load(
                "https://image.tmdb.org/t/p/w300"
                        + covers.get(position)
        ).into(imageView);
        return imageView;
    }
}
