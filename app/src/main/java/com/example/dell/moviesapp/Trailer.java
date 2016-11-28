package com.example.dell.moviesapp;

import java.io.Serializable;

public class Trailer implements Serializable {
    String key ;
    String name;
    String id;

    public void setKey(String key)
    {
        this.key = key ;
    }

    public String getKey()
    {
        return key;

    }

    public void setName(String name)
    {
        this.name = name ;
    }

    public String getName()
    {
        return name;

    }

    public void setId(String id)
    {
        this.id = id ;
    }

    public String getId()
    {
        return id;

    }



}
