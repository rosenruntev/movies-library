package com.example.movieslibrary.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.movieslibrary.R;
import com.example.movieslibrary.models.Movie;

import java.io.File;

@RequiresApi(api = Build.VERSION_CODES.O)
public class MainActivity extends BaseActivity {
    private Button myMoviesButton;
    private Button addMovieButton;
    private Button editMovieButton;
    private Button deleteMovieButton;
    private Button searchMovieButton;
    private TextView recentMovieHeaderView;
    private TextView recentMovieTitleView;
    private TextView recentMovieTypeView;
    private TextView recentMovieGenreView;
    private TextView recentMovieReleasedView;
    private TextView recentMovieDirectorView;
    private TextView recentMovieCountryView;
    private TextView recentMovieRatingView;
    private TextView recentMoviePlotView;

    private Movie recentMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SQLiteDatabase.deleteDatabase(new File(getFilesDir().getPath() + "/MoviesLibrary.db"));
        initThreadPolicy();
        initDatabase();
        initViewFields();
        showRecentMovie();
    }

    private void initThreadPolicy() {
        StrictMode.ThreadPolicy tp = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(tp);

        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.INTERNET}, 200);
        }
    }

    private void initViewFields() {
        myMoviesButton = findViewById(R.id.myMovies);
        addMovieButton = findViewById(R.id.addMovie);
        editMovieButton = findViewById(R.id.editMovie);
        deleteMovieButton = findViewById(R.id.deleteMovie);
        searchMovieButton = findViewById(R.id.searchMovie);
        recentMovieHeaderView = findViewById(R.id.recentMovie);
        recentMovieTitleView = findViewById(R.id.recentMovieTitle);
        recentMovieTypeView = findViewById(R.id.recentMovieType);
        recentMovieGenreView = findViewById(R.id.recentMovieGenre);
        recentMovieReleasedView = findViewById(R.id.recentMovieReleased);
        recentMovieDirectorView = findViewById(R.id.recentMovieDirector);
        recentMovieCountryView = findViewById(R.id.recentMovieCountry);
        recentMovieRatingView = findViewById(R.id.recentMovieRating);
        recentMoviePlotView = findViewById(R.id.recentMoviePlot);

        myMoviesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MyMoviesListActivity.class));
            }
        });

        addMovieButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AddMovieActivity.class));
            }
        });

        editMovieButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, EditMovieActivity.class));
            }
        });

        deleteMovieButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, DeleteMovieActivity.class));
            }
        });

        searchMovieButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SearchMovieActivity.class));
            }
        });
    }

    protected void showRecentMovie() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    recentMovie = getRecentMovie();

                    runOnUiThread(() -> {
                            recentMovieTitleView.setText("Title: " + recentMovie.getTitle());
                            recentMovieTypeView.setText("Type: " + recentMovie.getType().getName());
                            recentMovieGenreView.setText("Genre: " + recentMovie.getGenre().getName());
                            recentMovieReleasedView.setText("Release Date: " + recentMovie.getReleasedDate());
                            recentMovieDirectorView.setText("Director: " + recentMovie.getDirector().getName());
                            recentMovieCountryView.setText("Country: " + recentMovie.getCountry().getName());
                            recentMovieRatingView.setText("IMDB Rating: " + recentMovie.getImdbRating());
                            recentMoviePlotView.setText("Plot: " + recentMovie.getPlot());
                    });
                } catch (Exception e) {
                    runOnUiThread(() -> {
                        Toast.makeText(getApplicationContext(), "Error getting recent movie", Toast.LENGTH_LONG).show();
                        setRecentMovieViewInvisible();
                    });
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    private void setRecentMovieViewInvisible() {
        recentMovieHeaderView.setVisibility(View.INVISIBLE);
        recentMovieTitleView.setVisibility(View.INVISIBLE);
        recentMovieReleasedView.setVisibility(View.INVISIBLE);
        recentMovieTypeView.setVisibility(View.INVISIBLE);
        recentMovieGenreView.setVisibility(View.INVISIBLE);
        recentMovieDirectorView.setVisibility(View.INVISIBLE);
        recentMovieCountryView.setVisibility(View.INVISIBLE);
        recentMovieRatingView.setVisibility(View.INVISIBLE);
        recentMoviePlotView.setVisibility(View.INVISIBLE);
    }
}
