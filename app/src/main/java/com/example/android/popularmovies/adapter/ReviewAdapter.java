package com.example.android.popularmovies.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.model.Review;
import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {

    public static class ViewHolder extends RecyclerView.ViewHolder {

        protected TextView txtAuthor;
        protected TextView txtContent;

        public ViewHolder(View itemView) {
            super(itemView);
            txtAuthor = itemView.findViewById(R.id.tv_review_author);
            txtContent = itemView.findViewById(R.id.tv_review_content);
        }
    }

    private List<Review> reviews;
    private Context mContext;

    public ReviewAdapter(Context c, List<Review> reviews) {
        this.reviews = reviews;
        this.mContext = c;
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_cardview, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.txtAuthor.setText(reviews.get(position).getAuthor());
        holder.txtContent.setText(reviews.get(position).getContent());
    }
}
