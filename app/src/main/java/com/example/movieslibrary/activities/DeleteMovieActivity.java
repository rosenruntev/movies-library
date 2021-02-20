package com.example.movieslibrary.activities;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.example.movieslibrary.R;
import com.example.movieslibrary.models.Movie;

import java.util.List;
import java.util.stream.Collectors;

public class DeleteMovieActivity extends BaseActivity {
    private Spinner moviesSpinner;
    private Button deleteMovieButton;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_movie);

        moviesSpinner = findViewById(R.id.moviesSpinner);
        deleteMovieButton = findViewById(R.id.deleteMovieButton);
        getMovies();

        deleteMovieButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    deleteMovie(moviesSpinner.getSelectedItem().toString());
                    getMovies();
                    runOnUiThread(() -> {
                        Toast.makeText(getApplicationContext(), "Movie deleted", Toast.LENGTH_LONG).show();
                    });
                } catch (Exception e) {
                    runOnUiThread(() -> {
                        Toast.makeText(getApplicationContext(), "Error deleting a movie", Toast.LENGTH_LONG).show();
                    });
                    e.printStackTrace();
                }
            }
        });
    }

    protected void getMovies() {
        Thread thread = new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                try {
                    List<Movie> movies = getAllMovies();
                    List<String> moviesTitle = movies.stream().map(movie -> movie.getTitle()).collect(Collectors.toList());

                    runOnUiThread(() -> {
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(DeleteMovieActivity.this, android.R.layout.simple_list_item_1, android.R.id.text1, moviesTitle);
                        moviesSpinner.setAdapter(adapter);
                    });
                } catch (Exception e) {
                    runOnUiThread(() -> {
                        Toast.makeText(getApplicationContext(), "Error deleting a movie", Toast.LENGTH_LONG).show();
                    });
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }
}