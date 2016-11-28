package com.example.dell.moviesapp;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

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

public class MainActivityFragment extends Fragment {
    ArrayList<Movie> list ;
    private RecyclerView recyclerView;
    private MovieAdapter movieAdapter;
    Button favourite;
    String url ="https://api.themoviedb.org/3/movie/popular?";
    DatabaseHelper myDB;
    public MainActivityFragment()
    {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView ;
        rootView = inflater.inflate(R.layout.fragment_main, container, false);

        if(!isConnected())
        {
            Toast.makeText(getActivity(),"No internet",Toast.LENGTH_SHORT).show();
        }



        else {
            list = new ArrayList<Movie>();
            favourite = (Button) rootView.findViewById(R.id.favourite);
            recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
            recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
            movieAdapter = new MovieAdapter(getActivity());
            recyclerView.setAdapter(movieAdapter); // here the adapter is bound to the recyclerview
            movieAdapter.setMovieList(list); // here we supported the adapter with the data (images)
            myDB = new DatabaseHelper(getActivity());


        }
        return rootView;

    }

    public boolean isConnected(){

        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if(networkInfo !=null && networkInfo.isConnected())
        {
            return true;

        }
        else
            return false;


    }

    @Override
    public void onPause() {
        super.onPause();

        if (!isConnected()) {
            Toast.makeText(getActivity(), "No internet", Toast.LENGTH_SHORT).show();
        } else {
            list.clear();
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
        }
    }

    public ArrayList<Movie> getFavourite(){
        ArrayList<Movie> favourites = new ArrayList<>();
        Cursor res = myDB.getAllData();
        while (res.moveToNext()) {
            Movie movie = new Movie();
            for (int i = 0; i < res.getCount(); i++) {
                movie.setId(res.getString(1));
                movie.setImage(res.getString(2));
                movie.setDescription(res.getString(3));
                movie.setBackDrop(res.getString(4));
                movie.setFavourite(true);
            }
            favourites.add(movie);
        }
        return favourites;
    }

    @Override
    public void onResume() {
        super.onResume();
         SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
            String movieType = sharedPreferences.getString(getString(R.string.pref_movie_key), getString(R.string.pref_movie_label_mostPop));
            if (movieType.equals(getString(R.string.pref_movie_mostPop))) {
                url = "https://api.themoviedb.org/3/movie/popular?";
                new GetMovieData().execute(url);
                list.clear();

            } else if (movieType.equals(getString(R.string.pref_movie_topRated))) {
                url = "https://api.themoviedb.org/3/movie/top_rated?";
                new GetMovieData().execute(url);
                list.clear();


            } else if (movieType.equals(getString(R.string.pref_movie_favourite))) {
                list.addAll(getFavourite());
                movieAdapter.setMovieList(list);
                recyclerView.setAdapter(movieAdapter);
                list.clear();


            }
        }

    @Override
    public void onStop()
    {
        super.onStop();
        if(!isConnected())
        {

        }


        else
        {
        list.clear();
        movieAdapter.setMovieList(list);
        recyclerView.setAdapter(movieAdapter);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();

    }
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState)
    {
        super.onViewStateRestored(savedInstanceState);
    }


    @Override
    public void onDestroy()
    {

        super.onDestroy();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
    }

    public class GetMovieData extends AsyncTask<String, Void, ArrayList<Movie>> {

        private ArrayList<Movie> getMovieData(String JsonStr) throws JSONException {

            JSONObject jsonObject = new JSONObject(JsonStr);
            JSONArray movieArray = jsonObject.getJSONArray("results");
            for (int i = 0; i < movieArray.length(); i++) {
                JSONObject object = movieArray.getJSONObject(i);
                Movie movie = new Movie();
                movie.setName(object.getString("title"));
                movie.setDescription(object.getString("overview"));
                movie.setImage(object.getString("poster_path"));
                movie.setId(object.getString("id"));
                movie.setBackDrop(object.getString("backdrop_path"));
                movie.setReleaseData(object.getString("release_date"));

                for(int j= 0; j < getFavourite().size();j++)
                {
                    if(movie.getId().equals(getFavourite().get(j).getId()))
                        movie.setFavourite(true);
                    else
                        movie.setFavourite(false);
                }

                list.add(movie);
            }

            return list;
        }

        @Override
        protected ArrayList<Movie> doInBackground(String... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String JsonStr = null;

            try {
                final String BASE_URL = url;
                String key = "";
                final String APPID_PARAM = "api_key";
                Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                        .appendQueryParameter(APPID_PARAM, key)
                        .build();
                URL url = new URL(builtUri.toString());
                urlConnection = (HttpURLConnection) url.openConnection();
                //set a connection
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
                    return null;
                }
                JsonStr = buffer.toString();


            } catch (IOException e) {
                e.printStackTrace();
            }


            try {

                return getMovieData(JsonStr);
            } catch (JSONException e) {

            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<Movie> list)
        {
            movieAdapter.setMovieList(list);
            recyclerView.setAdapter(movieAdapter);
        }
    }
}