package com.zikozee.books;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class BookListActivity extends AppCompatActivity {
    private ProgressBar mLoadingProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list);
        mLoadingProgress = findViewById(R.id.pb_loading);
        try {
            URL bookUrl = ApiUtil.buildUrl("cooking");
            new BookQueryTask().execute(bookUrl);

        } catch (Exception e) {
            Log.d("error", e.getMessage());
        }
    }

    public class BookQueryTask extends AsyncTask<URL, Void, String>{

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
            TextView tvResult = findViewById(R.id.tvResponse);
            TextView tvError = findViewById(R.id.tv_error);
            mLoadingProgress.setVisibility(View.INVISIBLE);
            if(result == null){
                tvResult.setVisibility(View.INVISIBLE);
                tvError.setVisibility(View.VISIBLE);
            }else{
                tvResult.setVisibility(View.VISIBLE);
                tvError.setVisibility(View.INVISIBLE);
            }
            ArrayList<Book> books = ApiUtil.getBooksFromJson(result);
            StringBuilder resultString = new StringBuilder();
            for(Book book: books){
                resultString.append(resultString)
                        .append(book.title)
                        .append("\n")
                        .append(book.publishedDate)
                        .append("\n\n");
            }
            tvResult.setText(resultString);
        }

    }
}