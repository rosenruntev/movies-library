package com.example.movieslibrary.models;

import com.example.movieslibrary.exceptions.InvalidDataException;

public class MovieType {
    private int id;
    private String name;

    public MovieType(String name) {
        setName(name);
    }

    public MovieType(int id, String name) {
        setId(id);
        setName(name);
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.isEmpty()) {
            throw new InvalidDataException("Movie type cannot be null or empty");
        }

        this.name = name;
    }
}
