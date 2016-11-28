package com.example.dell.moviesapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dell on 11/18/2016.
 */
public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {

    private List<Review> reviews;
    private LayoutInflater layoutInflater;
    private Context context;

    public ReviewAdapter(Context context){

        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.reviews = new ArrayList<>();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;

        public ViewHolder(View view) {
            super(view);
            this.textView = (TextView) view.findViewById(R.id.content);

        }

    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.review_view, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Review review =reviews.get(position);
        final String content = review.getContent();
        holder.textView.setText(content);
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    public void setReviewList(List<Review> list)
    {

        this.reviews.addAll(list);
        notifyDataSetChanged();
    }


}
