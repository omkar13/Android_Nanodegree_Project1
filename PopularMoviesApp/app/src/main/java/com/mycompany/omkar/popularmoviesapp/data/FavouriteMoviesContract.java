package com.mycompany.omkar.popularmoviesapp.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by omkar on 3/4/16.
 */

public class FavouriteMoviesContract {

    public static final String CONTENT_AUTHORITY = "com.mycompany.omkar.popularmoviesapp.app";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    /*inner class that defines the table contents of the fav movies table*/

    public static final class FavMoviesEntry implements BaseColumns{

        //table name
        public static final String TABLE_FAV_MOVIES =   "favMovies";

        //columns
        public static final String _ID = "_id";

        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_RELEASE_DATE = "release_date";
        public static final String COLUMN_URL = "url";
        public static final String COLUMN_VOTE_AVERAGE = "vote_average";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_MOVIE_ID = "movie_id";
       // public static final String COLUMN_MOVIE_ICON = "icon";

        //public static final String COLUMN_REVIEW_LIST
        //public static final String COLUMN_TRAILER_LIST


        //create content uri
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(TABLE_FAV_MOVIES).build();

        //create cursor of base type directory for multiple entries
        public static final String CONTENT_DIR_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + TABLE_FAV_MOVIES;

        //create cursor of base type directory for single entries
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + TABLE_FAV_MOVIES;


        //for building uris on insertion

        public static Uri buildFavMoviesUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI , id);
        }

    }


}
