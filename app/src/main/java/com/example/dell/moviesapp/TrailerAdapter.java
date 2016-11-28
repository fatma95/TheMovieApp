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

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.ViewHolder> {

    private List<Trailer> trailers;
    private LayoutInflater layoutInflater;
    private Context context;

    public TrailerAdapter(Context context){

        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.trailers = new ArrayList<>();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView textView;

        public ViewHolder(View view) {
            super(view);
            this.imageView = (ImageView) view.findViewById(R.id.trailerView);
            this.textView = (TextView) view.findViewById(R.id.trailerName);

        }

    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.trailer_view, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Trailer trailer =trailers.get(position);
        final String key = trailer.getKey();
        final String name = trailer.getName();
        holder.textView.setText(name);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + key));
                context.startActivity(intent);

            }
        });


    }

    @Override
    public int getItemCount() {
        return trailers.size();
    }

    public void setTrailerList(List<Trailer> list)
    {

        this.trailers.addAll(list);
        notifyDataSetChanged();
    }


}
