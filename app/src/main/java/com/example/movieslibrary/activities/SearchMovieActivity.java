package com.example.movieslibrary.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.movieslibrary.R;
import com.example.movieslibrary.models.Movie;

import org.json.JSONException;

public class SearchMovieActivity extends BaseActivity {
    private EditText titleEditText;
    private Button searchMovieButton;
    private TextView titleTextView;
    private TextView typeTextView;
    private TextView genreTextView;
    private TextView dateTextView;
    private TextView directorTextView;
    private TextView countryTextView;
    private TextView ratingTextView;
    private TextView plotTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_movie);

        titleEditText = findViewById(R.id.searchTitle);
        searchMovieButton = findViewById(R.id.searchMovieButton);
        titleTextView = findViewById(R.id.foundMovieTitle);
        typeTextView = findViewById(R.id.foundMovieType);
        genreTextView = findViewById(R.id.foundMovieGenre);
        dateTextView = findViewById(R.id.foundMovieReleased);
        directorTextView = findViewById(R.id.foundMovieDirector);
        countryTextView = findViewById(R.id.foundMovieCountry);
        ratingTextView = findViewById(R.id.foundMovieRating);
        plotTextView = findViewById(R.id.foundMoviePlot);
        setFoundMovieViewInvisible();

        searchMovieButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String title = titleEditText.getText().toString().replace(" ", "%20");
                            Movie movie = searchMovie(title);
                            runOnUiThread(() -> {
                                setFoundMovieViewVisible();
                                titleTextView.setText("Title: " + movie.getTitle());
                                typeTextView.setText("Type: " + movie.getType().getName());
                                genreTextView.setText("Genre: " + movie.getGenre().getName());
                                dateTextView.setText("Released Date: " + movie.getReleasedDate());
                                directorTextView.setText("Director: " + movie.getDirector().getName());
                                countryTextView.setText("Country: " + movie.getCountry().getName());
                                ratingTextView.setText("IMDB Rating: " + movie.getImdbRating());
                                plotTextView.setText("Plot: " + movie.getPlot());
                            });
                        } catch (JSONException e) {
                          runOnUiThread(() -> {
                              setFoundMovieViewInvisible();
                              Toast.makeText(getApplicationContext(), "No movie found with this title", Toast.LENGTH_SHORT).show();
                          });
                          e.printStackTrace();
                        } catch (Exception e) {
                            runOnUiThread(() -> {
                                setFoundMovieViewInvisible();
                                Toast.makeText(getApplicationContext(), "Error getting the movie data", Toast.LENGTH_SHORT).show();
                            });
                            e.printStackTrace();
                        }
                    }
                });
                thread.start();
            }
        });
    }

    private void setFoundMovieViewInvisible() {
        runOnUiThread(() -> {
            titleTextView.setVisibility(View.INVISIBLE);
            typeTextView.setVisibility(View.INVISIBLE);
            genreTextView.setVisibility(View.INVISIBLE);
            dateTextView.setVisibility(View.INVISIBLE);
            directorTextView.setVisibility(View.INVISIBLE);
            countryTextView.setVisibility(View.INVISIBLE);
            ratingTextView.setVisibility(View.INVISIBLE);
            plotTextView.setVisibility(View.INVISIBLE);
        });
    }

    private void setFoundMovieViewVisible() {
        runOnUiThread(() -> {
            titleTextView.setVisibility(View.VISIBLE);
            typeTextView.setVisibility(View.VISIBLE);
            genreTextView.setVisibility(View.VISIBLE);
            dateTextView.setVisibility(View.VISIBLE);
            directorTextView.setVisibility(View.VISIBLE);
            countryTextView.setVisibility(View.VISIBLE);
            ratingTextView.setVisibility(View.VISIBLE);
            plotTextView.setVisibility(View.VISIBLE);
        });
    }
}