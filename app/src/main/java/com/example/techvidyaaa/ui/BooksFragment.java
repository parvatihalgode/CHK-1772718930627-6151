package com.example.techvidyaaa.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.techvidyaaa.R;
import com.example.techvidyaaa.databinding.FragmentBooksBinding;
import com.google.android.material.chip.Chip;

import java.util.ArrayList;
import java.util.List;

public class BooksFragment extends Fragment {

    private FragmentBooksBinding binding;
    private List<Book> bookList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentBooksBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bookList = new ArrayList<>();
        generateBookContent();

        binding.rvBooks.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvBooks.setAdapter(new BookAdapter(bookList));
    }

    private void generateBookContent() {
        // Technical field books with CDN-hosted cover images
        bookList.add(new Book("The C Programming Language", "Brian W. Kernighan & Dennis M. Ritchie", "Technical - C", "https://m.media-amazon.com/images/I/41as+S3vFpL.jpg"));
        bookList.add(new Book("Introduction to Algorithms", "Thomas H. Cormen", "Technical - Algorithms", "https://m.media-amazon.com/images/I/41v3oAtkVWL.jpg"));
        bookList.add(new Book("Clean Code", "Robert C. Martin", "Technical - Software Eng", "https://m.media-amazon.com/images/I/41xShlnTZTL.jpg"));
        bookList.add(new Book("Python Crash Course", "Eric Matthes", "Technical - Python", "https://m.media-amazon.com/images/I/51mUnmZpZdL.jpg"));
        bookList.add(new Book("Effective Java", "Joshua Bloch", "Technical - Java", "https://m.media-amazon.com/images/I/41pY6Bj96mL.jpg"));
        bookList.add(new Book("Database System Concepts", "Abraham Silberschatz", "Technical - DBMS", "https://m.media-amazon.com/images/I/41-vAt6XQXL.jpg"));
    }

    public static class Book {
        public String title, author, category, imageUrl;
        public Book(String t, String a, String c, String i) { 
            this.title = t; this.author = a; this.category = c; this.imageUrl = i;
        }
    }

    private class BookAdapter extends RecyclerView.Adapter<BookAdapter.ViewHolder> {
        private final List<Book> books;
        public BookAdapter(List<Book> books) { this.books = books; }
        
        @NonNull @Override public ViewHolder onCreateViewHolder(@NonNull ViewGroup p, int v) {
            View view = LayoutInflater.from(p.getContext()).inflate(R.layout.item_book, p, false);
            return new ViewHolder(view);
        }
        
        @Override public void onBindViewHolder(@NonNull ViewHolder h, int p) {
            Book b = books.get(p);
            h.tvTitle.setText(b.title);
            h.tvAuthor.setText(b.author);
            h.chipCategory.setText(b.category);
            
            Glide.with(h.ivCover.getContext())
                    .load(b.imageUrl)
                    .placeholder(R.drawable.ic_books)
                    .error(R.drawable.ic_practice)
                    .into(h.ivCover);
        }
        
        @Override public int getItemCount() { return books.size(); }
        
        class ViewHolder extends RecyclerView.ViewHolder {
            ImageView ivCover;
            TextView tvTitle, tvAuthor;
            Chip chipCategory;
            public ViewHolder(View v) { 
                super(v);
                ivCover = v.findViewById(R.id.ivBookCover);
                tvTitle = v.findViewById(R.id.tvBookTitle);
                tvAuthor = v.findViewById(R.id.tvBookAuthor);
                chipCategory = v.findViewById(R.id.chipCategory);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
