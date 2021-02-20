package com.example.movieslibrary.activities

import android.os.Build
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.movieslibrary.R

@RequiresApi(api = Build.VERSION_CODES.O)
class MyMoviesListActivity : BaseActivity() {
    private var myMoviesListView: ListView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_movies_list)
        myMoviesListView = findViewById(R.id.myMoviesList)
        val thread = Thread(Runnable {
            try {
                val movies = allMovies
                runOnUiThread {
                    val adapter = ArrayAdapter(this@MyMoviesListActivity, android.R.layout.simple_list_item_1, android.R.id.text1, movies)
                    myMoviesListView?.setAdapter(adapter)
                }
            } catch (e: Exception) {
                runOnUiThread { Toast.makeText(applicationContext, "Error getting the movies", Toast.LENGTH_LONG).show() }
                e.printStackTrace()
            }
        })
        thread.start()
    }
}