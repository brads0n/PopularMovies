package com.example.android.popularmovies.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.model.Trailer;

import java.net.URL;
import java.util.List;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.ViewHolder> {

    public static class ViewHolder extends RecyclerView.ViewHolder {

        protected TextView txtTitle;
        protected URL url;
        public ViewHolder(View itemView) {
            super(itemView);
            txtTitle = (TextView) itemView.findViewById(R.id.tv_trailer_name);
        }
    }

    private List<Trailer> trailers;
    private Context mContext;

    public TrailerAdapter(Context c, List<Trailer> trailers) {
        this.trailers = trailers;
        this.mContext = c;
    }

    @Override
    public int getItemCount() {
        return trailers.size();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.trailer_cardview, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.txtTitle.setText(trailers.get(position).getTitle());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "Gonna open Youtube for " + trailers.get(position).getUrl(), Toast.LENGTH_SHORT).show();
                Log.e("Bradson", "Gonna open Youtube");

                Intent intent = null;
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse(trailers.get(position).getUrl().toString()));
                    mContext.startActivity(intent);
            }
        });

    }
}
