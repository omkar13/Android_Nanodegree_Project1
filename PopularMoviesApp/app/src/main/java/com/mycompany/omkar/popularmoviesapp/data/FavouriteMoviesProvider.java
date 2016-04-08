package com.mycompany.omkar.popularmoviesapp.data;


import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

/**
 * Created by omkar on 3/4/16.
 */
public class FavouriteMoviesProvider extends ContentProvider{


    private static final String LOG_TAG = FavouriteMoviesProvider.class.getSimpleName();
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private FavouriteMoviesDBHelper moviesDBHelper;


    private static final int FAV_MOVIES = 100;
    private static final int FAV_MOVIES_WITH_ID = 200;


    @Override
    public boolean onCreate(){
        moviesDBHelper = new FavouriteMoviesDBHelper(getContext());
        return true ;

    }

    private static UriMatcher buildUriMatcher(){
        // Build a UriMatcher by adding a specific code to return based on a match
        // It's common to use NO_MATCH as the code for this case.
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = FavouriteMoviesContract.CONTENT_AUTHORITY;

        // add a code for each type of URI you want
        matcher.addURI(authority, FavouriteMoviesContract.FavMoviesEntry.TABLE_FAV_MOVIES, FAV_MOVIES);
        matcher.addURI(authority, FavouriteMoviesContract.FavMoviesEntry.TABLE_FAV_MOVIES + "/#", FAV_MOVIES_WITH_ID);

        return matcher;
    }


    //use case ???

    @Override
    public String getType(Uri uri){

        final int match = sUriMatcher.match(uri);

        switch (match){

            case FAV_MOVIES: {
                return FavouriteMoviesContract.FavMoviesEntry.CONTENT_DIR_TYPE;
            }

            case FAV_MOVIES_WITH_ID: {
                return FavouriteMoviesContract.FavMoviesEntry.CONTENT_ITEM_TYPE;
            }

            default:{
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }

        }


    }

    public Cursor query(Uri uri , String[] projection , String selection ,String[] selectionArgs, String sortOrder) {

        Cursor retCursor;

        switch (sUriMatcher.match(uri)) {

            //All Fav movies selected

            case FAV_MOVIES: {
                retCursor = moviesDBHelper.getReadableDatabase().query(FavouriteMoviesContract.FavMoviesEntry.TABLE_FAV_MOVIES, projection, selection, selectionArgs, null, null, sortOrder);
                return retCursor;

            }

            // individual movie based on id selected

            case FAV_MOVIES_WITH_ID: {

                retCursor = moviesDBHelper.getReadableDatabase().query(FavouriteMoviesContract.FavMoviesEntry.TABLE_FAV_MOVIES, projection, FavouriteMoviesContract.FavMoviesEntry._ID + " = ?", new String[]{String.valueOf(ContentUris.parseId(uri))}, null, null, sortOrder);
                return retCursor;

            }

            default: {
                // By default, we assume a bad URI
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }

        }

    }

    @Override
    public Uri insert(Uri uri , ContentValues contentValues){

        final SQLiteDatabase db  = moviesDBHelper.getWritableDatabase();

        Uri returnUri;
        long _id = -1;

        switch (sUriMatcher.match(uri)){

            case FAV_MOVIES: {

                //id of newly inserted row

                try {
                    _id = db.insertOrThrow(FavouriteMoviesContract.FavMoviesEntry.TABLE_FAV_MOVIES, null, contentValues);
                }


                catch (SQLiteConstraintException e){

                    //to handle unique constraints if any
                    Log.w(LOG_TAG, "Attempting to insert " +
                             " but value is already in database.");
                }

                if (_id > 0) {

                    returnUri = FavouriteMoviesContract.FavMoviesEntry.buildFavMoviesUri(_id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into: " + uri);
                }

                break;

            }

            default:{
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }



        }

        getContext().getContentResolver().notifyChange(uri , null);
        return  returnUri;
    }

    @Override
    public int delete(Uri uri , String selection , String[] selectionArgs){

        final SQLiteDatabase db = moviesDBHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int numDeleted;

        switch (match){

            case FAV_MOVIES: {

                numDeleted = db.delete(FavouriteMoviesContract.FavMoviesEntry.TABLE_FAV_MOVIES, selection , selectionArgs);


                //the following statement is used to reset the auto increment id of our table.
                //so that the next insert happens at the end only !
                //SQLITE_SEQUENCE is a table which keeps track of auto increments of user made tables.

                db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" + FavouriteMoviesContract.FavMoviesEntry.TABLE_FAV_MOVIES + "'");
                break;
            }

            case FAV_MOVIES_WITH_ID : {

                numDeleted = db.delete(FavouriteMoviesContract.FavMoviesEntry.TABLE_FAV_MOVIES , FavouriteMoviesContract.FavMoviesEntry._ID + " = ?" , new String[]{String.valueOf(ContentUris.parseId(uri))});

                db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" + FavouriteMoviesContract.FavMoviesEntry.TABLE_FAV_MOVIES + "'");

                break;

            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        return numDeleted;
    }



    @Override
    public int bulkInsert(Uri uri , ContentValues[] contentValues){

        final SQLiteDatabase db = moviesDBHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);

        switch (match){

            case FAV_MOVIES: {

                //we make many changes and then finally commit them all together. this is possible with transactions.

                db.beginTransaction();

                int numInserted = 0;

                try{

                    for(ContentValues value : contentValues){

                        if(value == null){
                            throw new IllegalArgumentException("Cannot have null content values");
                        }

                        long _id = -1;

                        try{
                            _id = db.insertOrThrow(FavouriteMoviesContract.FavMoviesEntry.TABLE_FAV_MOVIES, null, value);

                        }

                        catch (SQLiteConstraintException e){

                            //to handle unique constraints if any
                            Log.w(LOG_TAG, "Attempting to insert " +
                                    value.getAsString(
                                            FavouriteMoviesContract.FavMoviesEntry.COLUMN_MOVIE_ID)
                                    + " but value is already in database.");
                        }

                        if (_id != -1){
                            numInserted++;
                        }



                    }

                    if(numInserted > 0){
                        // If no errors, declare a successful transaction.
                        // database will not populate if this is not called
                        db.setTransactionSuccessful();
                    }




                }

                finally {
                    //all transactions occur at once
                    db.endTransaction();
                }

                if (numInserted > 0){
                    // if there was successful insertion, notify the content resolver that there
                    // was a change
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return numInserted;

            }

            default:{
                return super.bulkInsert(uri, contentValues);
            }

        }



    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs){

        final SQLiteDatabase db =  moviesDBHelper.getWritableDatabase();
        int numUpdated = 0;

        if (contentValues == null){
            throw new IllegalArgumentException("Cannot have null content values");
        }

        switch (sUriMatcher.match(uri)){

            case FAV_MOVIES:{
                numUpdated = db.update(FavouriteMoviesContract.FavMoviesEntry.TABLE_FAV_MOVIES, contentValues , selection , selectionArgs);
                break;
            }

            case FAV_MOVIES_WITH_ID: {

                numUpdated = db.update(FavouriteMoviesContract.FavMoviesEntry.TABLE_FAV_MOVIES , contentValues , FavouriteMoviesContract.FavMoviesEntry._ID + " = ?" , new String[] {String.valueOf(ContentUris.parseId(uri))});
                break;
            }

            default:{
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }



        }

        if (numUpdated > 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return numUpdated;

    }







}
