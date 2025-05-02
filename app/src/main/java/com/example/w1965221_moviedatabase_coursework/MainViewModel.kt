package com.example.w1965221_moviedatabase_coursework
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.w1965221_moviedatabase_coursework.data.Movie
import com.example.w1965221_moviedatabase_coursework.data.MovieDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(private val database: MovieDatabase) : ViewModel() {

    fun addMoviesToDb() {
        val movies = listOf(
            Movie(
                title = "The Shawshank Redemption",
                year = "1994",
                rated = "R",
                released = "14 Oct 1994",
                runtime = "142 min",
                genre = "Drama",
                director = "Frank Darabont",
                writer = "Stephen King, Frank Darabont",
                actors = "Tim Robbins, Morgan Freeman, Bob Gunton",
                plot = "Two imprisoned men bond over a number of years..."
            )
            // Add more movies as needed
        )

        viewModelScope.launch(Dispatchers.IO) {
            database.MovieDao().insertAll(movies)
        }
    }
}