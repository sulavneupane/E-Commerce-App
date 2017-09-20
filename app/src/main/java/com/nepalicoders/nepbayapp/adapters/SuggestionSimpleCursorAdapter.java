package com.nepalicoders.nepbayapp.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;

import com.nepalicoders.nepbayapp.SQLite_database.DatabaseHelper;
import android.util.Log;

/**
 * Created by kAy on 12/31/2015.
 */
public class SuggestionSimpleCursorAdapter
        extends SimpleCursorAdapter {

    public SuggestionSimpleCursorAdapter(Context context, int layout, Cursor c,
                                         String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);

    }

    @Override
    public CharSequence convertToString(Cursor cursor) {

        int indexColumnSuggestion = cursor.getColumnIndex(DatabaseHelper.FIELD_SUGGESTION);

        return cursor.getString(indexColumnSuggestion);
        //return "Hello";
    }


}