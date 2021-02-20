package com.example.movieslibrary.models;

import com.example.movieslibrary.exceptions.InvalidDataException;

public class Movie {
    private int id;
    private String title;
    private MovieType type;
    private Genre genre;
    private String releasedDate;
    private Director director;
    private Country country;
    private String imdbRating;
    private String plot;

    public Movie(String title, String type, String genre, String releasedDate, String director, String country, String imdbRating, String plot) {
        this(0, title, new MovieType(type), new Genre(genre), releasedDate, new Director(director), new Country(country), imdbRating, plot);
    }

    public Movie(int id, String title, MovieType type, Genre genre, String releasedDate, Director director, Country country, String imdbRating, String plot) {
        setId(id);
        setTitle(title);
        setType(type);
        setGenre(genre);
        setReleasedDate(releasedDate);
        setDirector(director);
        setCountry(country);
        setImdbRating(imdbRating);
        setPlot(plot);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        if (id < 0) {
            throw new InvalidDataException("Id cannot be a negative number");
        }

        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        if (title == null || title.isEmpty()) {
            throw new InvalidDataException("Title cannot be null or empty");
        }

        this.title = title;
    }

    public MovieType getType() {
        return type;
    }

    public void setType(MovieType type) {
        if (type == null) {
            throw new InvalidDataException("Movie type cannot be null");
        }

        this.type = type;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        if (genre == null) {
            throw new InvalidDataException("Genre cannot be null");
        }

        this.genre = genre;
    }

    public String getReleasedDate() {
        return releasedDate;
    }

    public void setReleasedDate(String releasedDate) {
        if (releasedDate == null || releasedDate.isEmpty()) {
            throw new InvalidDataException("Movie released date cannot be null or empty");
        }

        this.releasedDate = releasedDate;
    }

    public Director getDirector() {
        return director;
    }

    public void setDirector(Director director) {
        if (director == null) {
            throw new InvalidDataException("Director cannot be null");
        }

        this.director = director;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        if (country == null) {
            throw new InvalidDataException("Country cannot be null");
        }

        this.country = country;
    }

    public String getImdbRating() {
        return imdbRating;
    }

    public void setImdbRating(String imdbRating) {
        this.imdbRating = imdbRating;
    }

    public String getPlot() {
        return plot;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }

    @Override
    public String toString() {
        return String.format("Title: %s\nType: %s\nGenre: %s\nReleased Date: %s\nDirector: %s\nCountry: %s\nIMDB Rating: %s\nPlot: %s\n",
                title, type.getName(), genre.getName(), releasedDate, director.getName(), country.getName(), imdbRating, plot);
    }
}
