package com.kentux.booklistingapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Book>> {
    private SearchView searchView;
    private String mQuery;
    private TextView mEmptyStateTextView;
    private BookAdapter mAdapter;

    @Override
    public Loader<List<Book>> onCreateLoader(int id, Bundle args) {
        mQuery = searchView.getQuery().toString().replace(" ", "%20");

        BookLoader bookLoader = new BookLoader(this, mQuery);
        return bookLoader;
    }

    @Override
    public void onLoadFinished(Loader<List<Book>> loader, List<Book> books) {
        mAdapter.clear();

        if(books !=null && !books.isEmpty()) {
            mAdapter.addAll(books);
        }
        mEmptyStateTextView.setText("No book found with that given query.");
    }

    @Override
    public void onLoaderReset(Loader<List<Book>> loader) {
        mAdapter.clear();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final List<Book> books = new ArrayList<>();
        mAdapter = new BookAdapter(this, books);

        ListView bookListView = (ListView) findViewById(R.id.list);
        bookListView.setAdapter(mAdapter);
        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        bookListView.setEmptyView(mEmptyStateTextView);
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();

        final boolean isConnected = networkInfo != null && networkInfo.isConnectedOrConnecting();

        searchView = (SearchView) findViewById(R.id.search_view);
        searchView.setSubmitButtonEnabled(true);

        if(isConnected) {
            getLoaderManager().initLoader(1, null, this);
        } else {
            mEmptyStateTextView.setText(R.string.no_internet);
        }

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(isConnected) {
                    getLoaderManager().restartLoader(1, null, MainActivity.this);
                    return true;
                } else {
                    mEmptyStateTextView.setText(R.string.no_internet);
                    return false;
                }
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        bookListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Uri bookUrl = Uri.parse(books.get(position).getBookUrl());

                Intent webIntent = new Intent(Intent.ACTION_VIEW, bookUrl);
                if (webIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(webIntent);
                }
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //Get the query given by the user
        mQuery = searchView.getQuery().toString();
        outState.putString("query", mQuery);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        mQuery = savedInstanceState.getString("query");
        //Initialize the Loader (execute the search)
        super.onRestoreInstanceState(savedInstanceState);
    }
}
