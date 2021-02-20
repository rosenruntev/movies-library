package com.example.movieslibrary.models

import com.example.movieslibrary.exceptions.InvalidDataException

class Genre {
    var id = 0
        set(id) {
            if (id < 0) {
                throw InvalidDataException("Id cannot be a negative number")
            }
            field = id
        }
    var name: String? = null
        set(name) {
            if (name == null || name.isEmpty()) {
                throw InvalidDataException("Genre cannot be null or empty")
            }
            field = name
        }

    constructor(name: String?) {
        this.name = name
    }

    constructor(id: Int, name: String?) {
        this.id = id
        this.name = name
    }

}