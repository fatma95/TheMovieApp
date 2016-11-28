package com.example.dell.moviesapp;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {
    private List<Movie> list;
    private LayoutInflater layoutInflater;
    private Context context;

    public MovieAdapter(Context context){

        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.list = new ArrayList<>();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView imageView;
        public ViewHolder(View view){
            super(view);
            this.imageView = (ImageView) view.findViewById(R.id.imageView);

        }

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = layoutInflater.inflate(R.layout.movie_image_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    public void onBindViewHolder(ViewHolder holder, int position) {
        final Movie movie =list.get(position);
       holder.imageView.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               ScreenUtils screenUtils = new ScreenUtils();
               if(screenUtils.isTablet(context)==true)
               {
                   MovieDetailFragment movieDetailFragment = new MovieDetailFragment();
                   movieDetailFragment.setFragmentValues(movie);
                   ((MainActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.fragmentDetail,movieDetailFragment).commit();
               }
               else {
                   Intent intent = new Intent(context, MovieDetail.class);
                   intent.putExtra("movie", movie);
                   context.startActivity(intent);
               }
           }
       });

        Picasso.with(context)
                .load(movie.getImage())
                .into(holder.imageView);
    }

    @Override
    public int getItemCount()
    {
        return list.size();
    }

    public void setMovieList(List<Movie> list)
    { //this method adds the movies to a list
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }



}
