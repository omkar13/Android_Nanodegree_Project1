package com.mycompany.omkar.popularmoviesapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by omkar on 3/4/16.
 */
public class FavouriteMoviesDBHelper extends SQLiteOpenHelper{

    public static final String LOG_TAG = FavouriteMoviesDBHelper.class.getSimpleName();

    //name and version
    private static final String DATABASE_NAME = "favMovies.db";

    //why 12???????????????????/
    private static final int DATABASE_VERSION = 12;

    public FavouriteMoviesDBHelper (Context context){
        super(context , DATABASE_NAME , null , DATABASE_VERSION);
    }

    //create the database
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase){
        final String SQL_CREATE_FAV_MOVIE_TABLE = "CREATE TABLE " +
                                FavouriteMoviesContract.FavMoviesEntry.TABLE_FAV_MOVIES + "(" +
                                FavouriteMoviesContract.FavMoviesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                                FavouriteMoviesContract.FavMoviesEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                                FavouriteMoviesContract.FavMoviesEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL, " +
                                FavouriteMoviesContract.FavMoviesEntry.COLUMN_URL + " TEXT NOT NULL, " +
                                FavouriteMoviesContract.FavMoviesEntry.COLUMN_VOTE_AVERAGE + " REAL NOT NULL, " +
                                FavouriteMoviesContract.FavMoviesEntry.COLUMN_OVERVIEW + " TEXT NOT NULL, " +
                                FavouriteMoviesContract.FavMoviesEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL UNIQUE); " ;
                               // FavouriteMoviesContract.FavMoviesEntry.COLUMN_MOVIE_ICON + " INTEGER NOT NULL);";

        sqLiteDatabase.execSQL(SQL_CREATE_FAV_MOVIE_TABLE);

    }

    //upgrade database when version is changed
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase ,int oldVersion , int newVersion){

        // warning
        Log.w(LOG_TAG, "Upgrading database from version " + oldVersion + " to " +
                newVersion + ". OLD DATA WILL BE DESTROYED");

        //Drop the table
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + FavouriteMoviesContract.FavMoviesEntry.TABLE_FAV_MOVIES);

        sqLiteDatabase.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" +
                FavouriteMoviesContract.FavMoviesEntry.TABLE_FAV_MOVIES + "'");

        //re-create database

        onCreate(sqLiteDatabase);

    }





}
