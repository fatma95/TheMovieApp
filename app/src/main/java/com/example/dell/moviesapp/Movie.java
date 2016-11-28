package com.example.dell.moviesapp;

import java.io.Serializable;

/**
 * Created by Dell on 10/20/2016.
 */

// the movie class represents the movies , as a movie is an object with name , desc , image
public class Movie implements Serializable {
    private String name;
    private String description ;
    private String image;
    private String  id;
    private String  backdrop;
    private String releaseData;
    private boolean favourite;
    


    public void setFavourite(boolean favourite)
    {
        this.favourite = favourite;

    }
    public boolean getFavourite(){

        return favourite;


    }

    public void setReleaseData(String releaseData)
    {
        this.releaseData = releaseData;

    }

    public String getReleaseData()
    {

        return releaseData;
    }


    public void setBackDrop(String backDrop)
    {
       this.backdrop = backDrop;

    }

    public String getBackdrop()
    {

        return "http://image.tmdb.org/t/p/w780//"+backdrop;
    }

    public String getId(){
        return id;
    }

    public void setId(String id){

        this.id = id;
    }





    public void setName(String name)
    {

        this.name = name;
    }

    public String getName()

    {
        return name;
    }




    public void setDescription(String description)
    {

        this.description = description;
    }

    public String getDescription()


    {
        return description;
    }




    public void setImage(String image)
    {
        this.image = image;

    }
    public String getImage()
    {

        return "http://image.tmdb.org/t/p/w185//"+image;
    }
}