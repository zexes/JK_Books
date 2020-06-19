package com.zikozee.books;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BookListActivity extends AppCompatActivity implements SearchView.OnQueryTextListener{
    private ProgressBar mLoadingProgress;
    private RecyclerView rvBooks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list);
        rvBooks = findViewById(R.id.rv_books);
        mLoadingProgress = findViewById(R.id.pb_loading);
        Intent intent = getIntent();
        String query = intent.getStringExtra("Query");
        URL bookUrl;
        try {
            if(query == null || query.isEmpty()){

             bookUrl = ApiUtil.buildUrl("cooking");
            }else{
                bookUrl = new URL(query);
            }
            new BooksQueryTask().execute(bookUrl);

        } catch (Exception e) {
            Log.d("error", e.getMessage());
        }
        LinearLayoutManager booksLayoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false);
        rvBooks.setLayoutManager(booksLayoutManager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.book_list_menu, menu);
        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(this);

        List<String> recentList = SpUtil.getQueryList(getApplicationContext());
        int itemNum = recentList.size();
        for (int i = 0; i < itemNum; i++) {
            menu.add(Menu.NONE, i, Menu.NONE, recentList.get(i));
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_advanced_search:
                Intent intent = new Intent(this, SearchActivity.class);
                startActivity(intent);
                return true;
            default:
                int position = item.getItemId() + 1;
                String preferenceName = SpUtil.QUERY + position;
                String query = SpUtil.getPreferenceString(getApplicationContext(), preferenceName);
                String[] prefParams = query.split("\\,");
                Log.d("here", "onOptionsItemSelected: " + Arrays.toString(prefParams));
                String[] queryParams = new String[4];
                System.arraycopy(prefParams, 0, queryParams, 0, prefParams.length);

                URL bookURL = ApiUtil.buildUrl(
                        (queryParams[0]== null) ? "" : queryParams[0].trim(),
                        (queryParams[1]== null) ? "" : queryParams[1].trim(),
                        (queryParams[2]== null) ? "" : queryParams[2].trim(),
                        (queryParams[3]== null) ? "" : queryParams[3].trim()
                );

                Log.d("URL", "onOptionsItemSelected: " + bookURL.toString());

                new BooksQueryTask().execute(bookURL);

                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        try{
            URL bookUrl = ApiUtil.buildUrl(query);
            new BooksQueryTask().execute(bookUrl);
        }catch (Exception e){
            Log.d("error", e.getMessage());
        }
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    public class BooksQueryTask extends AsyncTask<URL, Void, String>{

        @Override
        protected void onPreExecute() {//at initiation
            super.onPreExecute();
            mLoadingProgress.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(URL... urls) {// runs independent of main Thread
            URL searchUrl = urls[0];
            String result = null;
            try {
                result = ApiUtil.getJSON(searchUrl);
            }catch (IOException e){
                Log.d("Error", e.getMessage());
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {// called when doInBackground completes
            TextView tvError = findViewById(R.id.tv_error);
            mLoadingProgress.setVisibility(View.INVISIBLE);
            if(result == null){
                rvBooks.setVisibility(View.INVISIBLE);
                tvError.setVisibility(View.VISIBLE);
            }else{
                rvBooks.setVisibility(View.VISIBLE);
                tvError.setVisibility(View.INVISIBLE);
            }
            ArrayList<Book> books = ApiUtil.getBooksFromJson(result);

            BooksAdapter adapter = new BooksAdapter(books);
            rvBooks.setAdapter(adapter);
        }

    }
}