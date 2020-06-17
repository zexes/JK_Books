package com.zikozee.books;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

class BooksAdapter extends RecyclerView.Adapter<BooksAdapter.BookViewHolder>{
    List<Book> books;
    public BooksAdapter(List<Book> books){
        this.books = books;
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.book_list_item, parent, false);
        return new BookViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        Book book = books.get(position);
        holder.bind(book);
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    public class BookViewHolder extends RecyclerView.ViewHolder{

        TextView tvTitle;
        TextView tvAuthors;
        TextView tvDate;
        TextView tvPublisher;

        public BookViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvAuthors = itemView.findViewById(R.id.tvAuthors);
            tvDate = itemView.findViewById(R.id.tvPublishedDate);
            tvPublisher = itemView.findViewById(R.id.tvPublisher);
        }

        public void bind (Book book){
            tvTitle.setText(book.title);
            StringBuilder authorBuilder = new StringBuilder();
            int i = 0;
            for(String author: book.authors){
                i++;
                authorBuilder.append(author)
                        .append(i < book.authors.length ? "," : "");

            }
            tvAuthors.setText(authorBuilder.toString());
            tvDate.setText(book.publishedDate);
            tvPublisher.setText(book.publisher);
        }
    }
}