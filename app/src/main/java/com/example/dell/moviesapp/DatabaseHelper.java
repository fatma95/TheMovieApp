package com.example.dell.moviesapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "favourite.db";
    public static final String TABLE_NAME = "favourite_table";
    public static final String ID = "ID";
    public static final String MOVIE_ID = "movie_id";
    public static final String Movie_Poster = "movie_poster";
    public static final String Description = "movie_description";
    public static final String BackDrop = "movie_backDrop";
    public static final String  Fav_State = "favourite_state";




    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);


    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT,movie_id TEXT,movie_poster TEXT,movie_description TEXT,movie_backDrop TEXT,favourite_state INTEGER ,unique(movie_id))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(String movie_id,String movie_poster,String movie_description,String movie_backDrop,int fav_state) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(MOVIE_ID, movie_id);
        contentValues.put(Movie_Poster,movie_poster);
        contentValues.put(Description,movie_description);
        contentValues.put(BackDrop,movie_backDrop);
        contentValues.put(Fav_State,fav_state);

        long result = db.insert(TABLE_NAME, null, contentValues);
        if (result == -1)
            return false;

        else
            return true;
    }

    public Integer deleteData(String id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME , "MOVIE_ID = ?",new String [] {id});
    }


    public Cursor getAllData()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from " + TABLE_NAME, null);
        return cursor;
    }

}
