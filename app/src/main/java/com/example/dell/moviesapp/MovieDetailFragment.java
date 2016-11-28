package com.example.dell.moviesapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieDetailFragment extends Fragment {
    ArrayList<Trailer> list = new ArrayList<>();
    ArrayList<Review> reviewList = new ArrayList<>();
    private RecyclerView trailerRecycler, reviewRecycler;
    private TrailerAdapter trailerAdapter;
    private ReviewAdapter reviewAdapter;
    Button favourite,show;

    String url;
    String url2;

    DatabaseHelper myDB;
    String id;
    String description;
    String poster;
    String backDrop ;
    boolean added = false;
    Movie movieDetails ;
    public MovieDetailFragment() {

    }

public void setFragmentValues(Movie myMovie){
    movieDetails=myMovie;
}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        myDB = new DatabaseHelper(getActivity());
        View rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);
        Intent intent = getActivity().getIntent();
        if(movieDetails==null)
        {

            movieDetails = (Movie) intent.getSerializableExtra("movie");
        }


        id= movieDetails.getId();
        poster = movieDetails.getImage().replace("http://image.tmdb.org/t/p/w185//", "");
        int index = 0;
        poster= poster.substring(index, poster.length());
        description = movieDetails.getDescription();
        backDrop = movieDetails.getBackdrop().replace("http://image.tmdb.org/t/p/w780//", "");
        backDrop=backDrop.substring(index,backDrop.length());


        TextView textView = (TextView) rootView.findViewById(R.id.description_textView);
        ImageView imageView = (ImageView) rootView.findViewById(R.id.imageView2);
        ImageView imageView1 = (ImageView) rootView.findViewById(R.id.backdrop);
        TextView textView2 = (TextView) rootView.findViewById(R.id.textView);
        textView.setText(movieDetails.getDescription());
        textView2.setText(movieDetails.getReleaseData());
        Picasso.with(getActivity()).load(movieDetails.getBackdrop()).into(imageView1);
        Picasso.with(getActivity()).load(movieDetails.getImage()).into(imageView);
        favourite = (Button)rootView.findViewById(R.id.favourite);
        favourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddFavourite();
            }
        });

        show = (Button)rootView.findViewById(R.id.showbtnn);
        list = new ArrayList<Trailer>();
        url = "https://api.themoviedb.org/3/movie/" + id + "/videos?";
        url2 = "https://api.themoviedb.org/3/movie/" + id + "/reviews?";
        trailerRecycler = (RecyclerView) rootView.findViewById(R.id.trailerRecyclerView);
        trailerRecycler.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        trailerAdapter = new TrailerAdapter(getActivity());
        trailerRecycler.setAdapter(trailerAdapter);
        trailerAdapter.setTrailerList(list);

        reviewRecycler = (RecyclerView)rootView.findViewById(R.id.reviewRecyclerView);
        reviewRecycler.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        reviewAdapter = new ReviewAdapter(getActivity());
        reviewRecycler.setAdapter(reviewAdapter);
        reviewAdapter.setReviewList(reviewList);

        if(movieDetails.getFavourite() == true)
        {
            favourite.setText("MARK AS \n UNFAVOURITE");
        }
        else

            favourite.setText("MARK AS \n FAVOURITE");

        new GetTrailer().execute(url);
        new GetReview().execute(url2);
        return rootView;



    }

    public void AddFavourite()
    {
        if (movieDetails.getFavourite() == false) {
            boolean isInserted = myDB.insertData(id, poster, description, backDrop,1);
            if (isInserted == true) {
                favourite.setText("MARK AS \n UNFAVOURITE");
                movieDetails.setFavourite(true);

            } else
                Toast.makeText(getActivity(), "ERROR", Toast.LENGTH_SHORT).show();

        }

        else if(movieDetails.getFavourite() ==true)
        {

                    Integer deleteRows = myDB.deleteData(id);
                    if(deleteRows > 0)
                    {
                        favourite.setText("MARK AS \n FAVOURITE");
                        movieDetails.setFavourite(false);
                    }
        }


    }

    public class GetTrailer extends AsyncTask<String, Void, ArrayList<Trailer>> {

        private ArrayList<Trailer> getTrailer(String JsonStr) throws JSONException {

            JSONObject jsonObject = new JSONObject(JsonStr);
            JSONArray trailerArray = jsonObject.getJSONArray("results");
            for (int i = 0; i < trailerArray.length(); i++) {
                JSONObject object = trailerArray.getJSONObject(i);
                Trailer trailer = new Trailer();
                trailer.setName(object.getString("name"));
                trailer.setKey(object.getString("key"));
                list.add(trailer);
            }

            return list;
        }

        @Override
        protected ArrayList<Trailer> doInBackground(String... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String JsonStr = null;

            try {
                final String BASE_URL = url;
                String key = "10047e858191611b2a68ca2e2573a55d";
                final String APPID_PARAM = "api_key";
                Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                        .appendQueryParameter(APPID_PARAM, key)
                        .build();
                URL url = new URL(builtUri.toString());

                urlConnection = (HttpURLConnection) url.openConnection(); //set a connection
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {

                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                JsonStr = buffer.toString();


            } catch (IOException e) {
                e.printStackTrace();
            }


            try {

                return getTrailer(JsonStr);
            } catch (JSONException e) {

            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<Trailer> list) {
            trailerAdapter.setTrailerList(list);
            trailerRecycler.setAdapter(trailerAdapter);

        }
    }


    public class GetReview extends AsyncTask<String, Void, ArrayList<Review>> {

        private ArrayList<Review> getReview(String JsonStr) throws JSONException {

            JSONObject jsonObject = new JSONObject(JsonStr);
            JSONArray reviewArray = jsonObject.getJSONArray("results");
            for (int i = 0; i < reviewArray.length(); i++) {

                JSONObject object = reviewArray.getJSONObject(i);
                Review review = new Review();
                review.setContent(object.getString("content"));
                review.setId(object.getString("id"));
                reviewList.add(review);
            }
            return reviewList;
        }

        @Override
        protected ArrayList<Review> doInBackground(String... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String JsonStr = null;

            try {
                final String BASE_URL = url2;
                String key = "10047e858191611b2a68ca2e2573a55d";
                final String APPID_PARAM = "api_key";
                Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                        .appendQueryParameter(APPID_PARAM, key)
                        .build();
                URL url = new URL(builtUri.toString());

                urlConnection = (HttpURLConnection) url.openConnection(); //set a connection
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {

                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                JsonStr = buffer.toString();


            } catch (IOException e) {
                e.printStackTrace();
            }


            try {

                return getReview(JsonStr);
            } catch (JSONException e) {

            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<Review> list) {
            reviewAdapter.setReviewList(list);
            reviewRecycler.setAdapter(reviewAdapter);

        }
    }




}


