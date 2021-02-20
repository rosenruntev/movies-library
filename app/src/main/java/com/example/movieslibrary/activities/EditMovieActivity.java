package com.example.movieslibrary.activities;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.example.movieslibrary.R;
import com.example.movieslibrary.exceptions.InvalidDataException;
import com.example.movieslibrary.models.Movie;

import java.util.List;
import java.util.stream.Collectors;

public class EditMovieActivity extends BaseActivity {
    private Spinner spinner;
    private EditText editTitle;
    private EditText editType;
    private EditText editGenre;
    private EditText editDate;
    private EditText editDirector;
    private EditText editCountry;
    private EditText editRating;
    private EditText editPlot;
    private Button updateButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_movie);

        spinner = findViewById(R.id.editMoviesSpinner);
        editTitle = findViewById(R.id.editTitle);
        editType = findViewById(R.id.editType);
        editGenre = findViewById(R.id.editGenre);
        editDate = findViewById(R.id.editDate);
        editDirector = findViewById(R.id.editDirector);
        editCountry = findViewById(R.id.editCountry);
        editRating = findViewById(R.id.editRating);
        editPlot = findViewById(R.id.editPlot);
        updateButton = findViewById(R.id.editMovieButton);
        getMovies();

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (spinner.getSelectedItem() == null) {
                    runOnUiThread(() -> {
                        Toast.makeText(getApplicationContext(), "Please select a movie", Toast.LENGTH_SHORT).show();
                    });
                } else {
                    String selectedMovieTitle = spinner.getSelectedItem().toString();
                    String title = editTitle.getText().toString();
                    String type = editType.getText().toString();
                    String genre = editGenre.getText().toString();
                    String date = editDate.getText().toString();
                    String director = editDirector.getText().toString();
                    String country = editCountry.getText().toString();
                    String rating = editRating.getText().toString();
                    String plot = editPlot.getText().toString();

                    Thread thread = new Thread(new Runnable() {
                        @RequiresApi(api = Build.VERSION_CODES.O)
                        @Override
                        public void run() {
                            try {
                                Movie movie = new Movie(title, type, genre, date, director, country, rating, plot);
                                updateMovie(movie, selectedMovieTitle);

                                runOnUiThread(() -> {
                                    getMovies();
                                    editTitle.setText("");
                                    editType.setText("");
                                    editGenre.setText("");
                                    editDate.setText("");
                                    editDirector.setText("");
                                    editCountry.setText("");
                                    editRating.setText("");
                                    editPlot.setText("");
                                    Toast.makeText(getApplicationContext(), "Movie updated", Toast.LENGTH_LONG).show();
                                });
                            } catch (InvalidDataException e) {
                                runOnUiThread(() -> {
                                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                                });
                                e.printStackTrace();
                            }  catch (Exception e) {
                                runOnUiThread(() -> {
                                    Toast.makeText(getApplicationContext(), "Error updating the movie", Toast.LENGTH_LONG).show();
                                });
                                e.printStackTrace();
                            }
                        }
                    });

                    thread.start();
                }
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String title = parent.getSelectedItem().toString();
                Movie movie = getMovie(title);
                runOnUiThread(() -> {
                    editTitle.setText(movie.getTitle());
                    editType.setText(movie.getType().getName());
                    editGenre.setText(movie.getGenre().getName());
                    editDate.setText(movie.getReleasedDate());
                    editDirector.setText(movie.getDirector().getName());
                    editCountry.setText(movie.getCountry().getName());
                    editRating.setText(movie.getImdbRating());
                    editPlot.setText(movie.getPlot());
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                runOnUiThread(() -> {
                    editTitle.setText("");
                    editType.setText("");
                    editGenre.setText("");
                    editDate.setText("");
                    editDirector.setText("");
                    editCountry.setText("");
                    editRating.setText("");
                    editPlot.setText("");
                });
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
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(EditMovieActivity.this, android.R.layout.simple_list_item_1, android.R.id.text1, moviesTitle);
                        spinner.setAdapter(adapter);
                    });
                } catch (Exception e) {
                    runOnUiThread(() -> {
                        Toast.makeText(getApplicationContext(), "Error getting the movies", Toast.LENGTH_LONG).show();
                    });
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }
}
