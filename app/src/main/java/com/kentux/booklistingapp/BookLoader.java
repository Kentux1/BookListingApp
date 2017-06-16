package com.kentux.booklistingapp;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

public class BookLoader extends AsyncTaskLoader<List<Book>> {
    private String mQuery;

    public BookLoader(Context context, String query) {
        super(context);
        mQuery = query;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Book> loadInBackground() {

        //If there is no query yet, return early
        if (mQuery == null) {
            return null;
        }

        //If there is a query, fetch the data and create a list of books from it
        List<Book> books = QueryUtils.fetchBookData(mQuery);

        return books;
    }

}
