package com.example.movieslibrary.activities

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.movieslibrary.R
import com.example.movieslibrary.exceptions.InvalidDataException
import com.example.movieslibrary.models.Movie

@RequiresApi(Build.VERSION_CODES.O)
class AddMovieActivity : BaseActivity() {
    private var addButton: Button? = null
    private var titleEditText: EditText? = null
    private var typeEditText: EditText? = null
    private var genreEditText: EditText? = null
    private var dateEditText: EditText? = null
    private var directorEditText: EditText? = null
    private var countryEditText: EditText? = null
    private var ratingEditText: EditText? = null
    private var plotEditText: EditText? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_movie)
        addButton = findViewById(R.id.addMovie)
        titleEditText = findViewById(R.id.movieTitle)
        typeEditText = findViewById(R.id.movieType)
        genreEditText = findViewById(R.id.movieGenre)
        dateEditText = findViewById(R.id.movieDate)
        directorEditText = findViewById(R.id.movieDirector)
        countryEditText = findViewById(R.id.movieCountry)
        ratingEditText = findViewById(R.id.movieRating)
        plotEditText = findViewById(R.id.moviePlot)
        addButton?.setOnClickListener(View.OnClickListener {
            val title = titleEditText?.getText().toString()
            val type = typeEditText?.getText().toString()
            val genre = genreEditText?.getText().toString()
            val date = dateEditText?.getText().toString()
            val director = directorEditText?.getText().toString()
            val country = countryEditText?.getText().toString()
            val rating = ratingEditText?.getText().toString()
            val plot = plotEditText?.getText().toString()
            val thread = Thread(Runnable {
                try {
                    val movie = Movie(title, type, genre, date, director, country, rating, plot)
                    saveMovie(movie)
                    runOnUiThread {
                        titleEditText?.setText("")
                        typeEditText?.setText("")
                        genreEditText?.setText("")
                        dateEditText?.setText("")
                        directorEditText?.setText("")
                        countryEditText?.setText("")
                        ratingEditText?.setText("")
                        plotEditText?.setText("")
                        Toast.makeText(applicationContext, "Movie added", Toast.LENGTH_LONG).show()
                    }
                } catch (e: InvalidDataException) {
                    runOnUiThread { Toast.makeText(applicationContext, e.message, Toast.LENGTH_LONG).show() }
                    e.printStackTrace()
                } catch (e: Exception) {
                    runOnUiThread { Toast.makeText(applicationContext, "Error adding a movie", Toast.LENGTH_LONG).show() }
                    e.printStackTrace()
                }
            })
            thread.start()
        })
    }
}