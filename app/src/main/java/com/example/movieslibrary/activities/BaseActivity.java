package com.example.movieslibrary.activities;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.movieslibrary.exceptions.InvalidDataException;
import com.example.movieslibrary.models.Movie;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.O)
public abstract class BaseActivity extends AppCompatActivity {
    private static final int CURRENT_YEAR = Year.now().getValue();
    private static final String OMDB_API_BASE_URL = "https://www.omdbapi.com";
    private static final String RECENT_MOVIE_URL = OMDB_API_BASE_URL + "/?y=" + CURRENT_YEAR + "&t=movie&apikey=7b890814";
    private static final String SEARCH_MOVIE_ID = OMDB_API_BASE_URL + "?apikey=7b890814&s=";
    private static final String SEARCH_MOVIE_BY_ID = OMDB_API_BASE_URL + "?apikey=7b890814&i=";

    protected void initDatabase() {
        SQLiteDatabase database = SQLiteDatabase.openOrCreateDatabase(getFilesDir().getPath() + "/MoviesLibrary.db", null);

        String directorsTableQuery = "CREATE TABLE IF NOT EXISTS directors(id integer primary key autoincrement, name text not null);";
        String typesTableQuery = "CREATE TABLE IF NOT EXISTS types(id integer primary key autoincrement, name text not null unique);";
        String genresTableQuery = "CREATE TABLE IF NOT EXISTS genres(id integer primary key autoincrement,name text not null unique);";
        String countriesTableQuery = "CREATE TABLE IF NOT EXISTS countries(id integer primary key autoincrement,name text not null unique);";
        String moviesTableQuery = "CREATE TABLE IF NOT EXISTS movies(id integer primary key autoincrement," +
                "title text not null unique,type_id integer not null,genre_id integer not null,released_date text not null,director_id integer not null,country_id integer not null," +
                "imdb_rating integer,plot text, FOREIGN KEY(type_id) REFERENCES types(id), FOREIGN KEY(genre_id) REFERENCES genres(id), FOREIGN KEY(director_id) " +
                "REFERENCES directors(id), FOREIGN KEY(country_id) REFERENCES countries(id));";

        database.execSQL(directorsTableQuery);
        database.execSQL(typesTableQuery);
        database.execSQL(genresTableQuery);
        database.execSQL(countriesTableQuery);
        database.execSQL(moviesTableQuery);

        database.close();
    }

    protected void saveMovie(Movie movie) {
        String title = movie.getTitle();
        String type = movie.getType().getName();
        String genre = movie.getGenre().getName();
        String releasedDate = movie.getReleasedDate();
        String director = movie.getDirector().getName();
        String country = movie.getCountry().getName();
        String imdbRating = movie.getImdbRating();
        String plot = movie.getPlot();

        SQLiteDatabase database = SQLiteDatabase.openOrCreateDatabase(getFilesDir().getPath() + "/MoviesLibrary.db", null);
        insertOrIgnore(database, "INSERT OR IGNORE INTO types(name) VALUES(?)", new String[]{type});
        insertOrIgnore(database, "INSERT OR IGNORE INTO genres(name) VALUES(?)", new String[]{genre});
        insertOrIgnore(database, "INSERT OR IGNORE INTO directors(name) VALUES(?)", new String[]{director});
        insertOrIgnore(database, "INSERT OR IGNORE INTO countries(name) VALUES(?)", new String[]{country});

        int typeId = findId(database, "SELECT id FROM types WHERE name = ?", new String[]{type});
        int genreId = findId(database, "SELECT id FROM genres WHERE name = ?", new String[]{genre});
        int directorId = findId(database, "SELECT id FROM directors WHERE name = ?", new String[]{director});
        int countryId = findId(database, "SELECT id FROM countries WHERE name = ?", new String[]{country});

        database.execSQL("INSERT INTO movies(title, type_id, genre_id, released_date, director_id, country_id, imdb_rating, plot) VALUES(?, ?, ?, ?, ?, ?, ?, ?)",
                new String[]{title, String.valueOf(typeId), String.valueOf(genreId), releasedDate, String.valueOf(directorId),
                        String.valueOf(countryId), imdbRating, plot});
        database.close();
    }

    protected List<Movie> getAllMovies() {
        SQLiteDatabase database = SQLiteDatabase.openOrCreateDatabase(getFilesDir().getPath() + "/MoviesLibrary.db", null);
        String moviesQuery = "SELECT m.id as id, m.title as title, t.name as type, g.name as genre, m.released_date as released_date, " +
                "d.name as director, " +
                "c.name as country," +
                "m.imdb_rating as rating, m.plot as plot " +
                "FROM movies m " +
                "INNER JOIN types t ON m.type_id = t.id " +
                "INNER JOIN genres g ON m.genre_id = g.id " +
                "INNER JOIN directors d ON m.director_id = d.id " +
                "INNER JOIN countries c ON m.country_id = c.id";

        Cursor cursor = database.rawQuery(moviesQuery, new String[0]);
        List<Movie> movieList = new ArrayList<>();
        while (cursor.moveToNext()) {
            String title = cursor.getString(cursor.getColumnIndex("title"));
            String type = cursor.getString(cursor.getColumnIndex("type"));
            String genre = cursor.getString(cursor.getColumnIndex("genre"));
            String releasedDate = cursor.getString(cursor.getColumnIndex("released_date"));
            String director = cursor.getString(cursor.getColumnIndex("director"));
            String country = cursor.getString(cursor.getColumnIndex("country"));
            String imdbRating = cursor.getString(cursor.getColumnIndex("rating"));
            String plot = cursor.getString(cursor.getColumnIndex("plot"));

            Movie movie = new Movie(title, type, genre, releasedDate, director, country, imdbRating, plot);
            movieList.add(movie);
        }

        database.close();
        return movieList;
    }

    protected Movie getMovie(String title) {
        SQLiteDatabase database = SQLiteDatabase.openOrCreateDatabase(getFilesDir().getPath() + "/MoviesLibrary.db", null);
        String movieQuery = "SELECT t.name as type, g.name as genre, m.released_date as released_date, d.name as director, c.name as country," +
                "m.imdb_rating as rating, m.plot as plot " +
                "FROM movies m " +
                "INNER JOIN types t ON m.type_id = t.id " +
                "INNER JOIN genres g ON m.genre_id = g.id " +
                "INNER JOIN directors d ON m.director_id = d.id " +
                "INNER JOIN countries c ON m.country_id = c.id " +
                "WHERE m.title = ?";

        Cursor movieCursor = database.rawQuery(movieQuery, new String[] { title });
        if (!movieCursor.moveToNext()) {
            throw new InvalidDataException("Movie doesn't exist");
        } else {
            String type = movieCursor.getString(movieCursor.getColumnIndex("type"));
            String genre = movieCursor.getString(movieCursor.getColumnIndex("genre"));
            String releasedDate = movieCursor.getString(movieCursor.getColumnIndex("released_date"));
            String director = movieCursor.getString(movieCursor.getColumnIndex("director"));
            String country = movieCursor.getString(movieCursor.getColumnIndex("country"));
            String imdbRating = movieCursor.getString(movieCursor.getColumnIndex("rating"));
            String plot = movieCursor.getString(movieCursor.getColumnIndex("plot"));

            Movie movie = new Movie(title, type, genre, releasedDate, director, country, imdbRating, plot);
            return movie;
        }
    }

    protected void updateMovie(Movie movie, String title) {
        SQLiteDatabase database = SQLiteDatabase.openOrCreateDatabase(getFilesDir().getPath() + "/MoviesLibrary.db", null);
        insertOrIgnore(database, "INSERT OR IGNORE INTO types(name) VALUES(?)", new String[]{movie.getType().getName()});
        insertOrIgnore(database, "INSERT OR IGNORE INTO genres(name) VALUES(?)", new String[]{movie.getGenre().getName()});
        insertOrIgnore(database, "INSERT OR IGNORE INTO directors(name) VALUES(?)", new String[]{movie.getDirector().getName()});
        insertOrIgnore(database, "INSERT OR IGNORE INTO countries(name) VALUES(?)", new String[]{movie.getCountry().getName()});

        int typeId = findId(database, "SELECT id FROM types WHERE name = ?", new String[]{movie.getType().getName()});
        int genreId = findId(database, "SELECT id FROM genres WHERE name = ?", new String[]{movie.getGenre().getName()});
        int directorId = findId(database, "SELECT id FROM directors WHERE name = ?", new String[]{movie.getDirector().getName()});
        int countryId = findId(database, "SELECT id FROM countries WHERE name = ?", new String[]{movie.getCountry().getName()});

        database.execSQL("UPDATE movies SET title = ?, type_id = ?, genre_id = ?, released_date = ?, director_id = ?, country_id = ?, imdb_rating = ?, plot = ? " +
                "WHERE title = ?", new String[]{movie.getTitle(), String.valueOf(typeId), String.valueOf(genreId), movie.getReleasedDate(),
                String.valueOf(directorId), String.valueOf(countryId), movie.getImdbRating(), movie.getPlot(), title});
        database.close();
    }

    protected void deleteMovie(String title) {
        SQLiteDatabase database = SQLiteDatabase.openOrCreateDatabase(getFilesDir().getPath() + "/MoviesLibrary.db", null);
        database.execSQL("DELETE FROM movies WHERE title = ?", new String[]{title});
        database.close();
    }

    protected Movie getRecentMovie() throws Exception {
        String data = getDataFromOMDB(RECENT_MOVIE_URL);
        JSONObject recentMovieObject = new JSONObject(data);
        String title = recentMovieObject.getString("Title");
        String type = recentMovieObject.getString("Type");
        String genre = recentMovieObject.getString("Genre");
        String releasedDate = recentMovieObject.getString("Released");
        String director = recentMovieObject.getString("Director");
        String country = recentMovieObject.getString("Country");
        String rating = recentMovieObject.getString("imdbRating");
        String plot = recentMovieObject.getString("Plot");

        Movie movie = new Movie(title, type, genre, releasedDate, director, country, rating, plot);
        return movie;
    }

    protected Movie searchMovie(String title) throws Exception {
        String movieId = findMovieIMDBId(title);
        Movie movie = findMovieDataFromIMDB(movieId);
        return movie;
    }

    private String findMovieIMDBId(String title) throws Exception {
        String data = getDataFromOMDB(SEARCH_MOVIE_ID + title);
        JSONObject foundResult = new JSONObject(data);
        JSONObject foundMovie = foundResult.getJSONArray("Search").getJSONObject(0);
        return foundMovie.getString("imdbID");
    }

    private Movie findMovieDataFromIMDB(String id) throws Exception {
        String result = getDataFromOMDB(SEARCH_MOVIE_BY_ID + id);
        JSONObject foundMovie = new JSONObject(result);

        String title = foundMovie.getString("Title");
        String type = foundMovie.getString("Type");
        String genre = foundMovie.getString("Genre");
        String date = foundMovie.getString("Released");
        String director = foundMovie.getString("Director");
        String country = foundMovie.getString("Country");
        String rating = foundMovie.getString("imdbRating");
        String plot = foundMovie.getString("Plot");

        Movie movie = new Movie(title, type, genre, date, director, country, rating, plot);
        return movie;
    }

    private String getDataFromOMDB(String apiUrl) throws Exception {
        URL url = new URL(apiUrl);
        HttpURLConnection client = (HttpURLConnection) url.openConnection();
        client.setDoInput(true);
        client.setDoOutput(true);
        client.setRequestMethod("GET");
        client.setRequestProperty("Content-Type", "application/json");

        String result = "";
        int ResponseCode = client.getResponseCode();
        if (ResponseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream()));
            String line = "";
            while ((line = br.readLine()) != null) {
                result += line + "\n";
            }
            br.close();
        } else {
            throw new Exception("HTTP ERROR Response code: " + ResponseCode);
        }

        return result;
    }

    private void insertOrIgnore(SQLiteDatabase database, String query, String[] params) {
        database.execSQL(query, params);
    }

    private int findId(SQLiteDatabase database, String query, String[] params) {
        Cursor cursor = database.rawQuery(query, params);
        cursor.moveToNext();
        return cursor.getInt(0);
    }
}
