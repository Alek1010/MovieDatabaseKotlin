package com.example.w1965221_moviedatabase_coursework
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.w1965221_moviedatabase_coursework.data.Movie
import com.example.w1965221_moviedatabase_coursework.data.MovieDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

// ViewModel to manage UI-related data and handle database/network operations
class MainViewModel(private val database: MovieDatabase) : ViewModel() {

    // Retrofit service to make network calls to OMDb API
    private val apiService = RetrofitInstance.api

    // DAO object to interact with the Movie database
    private val movieDao = database.MovieDao()

    // Initialize block - clears the database when ViewModel is created
    init {
        clearDatabase()
    }

    // Function to clear all movie data from the local database (used for testing/fresh start)
    private fun clearDatabase() {
        viewModelScope.launch(Dispatchers.IO) {
            movieDao.clearAll()
        }
    }

    // Adds a hardcoded list of movies to the database
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
                plot = "Two imprisoned men bond over a number of years...",
                posterUrl = ""
            ),
            Movie(
                title = "Batman: The Dark Knight Returns, Part 1",
                year = "2012",
                rated = "PG-13",
                released = "25 Sep 2012",
                runtime = "76 min",
                genre = "Animation, Action, Crime, Drama, Thriller",
                director = "Jay Oliva",
                writer = "Bob Kane (character created by: Batman), Frank Miller (comic book), Klaus Janson (comic book), Bob Goodman",
                actors = "Peter Weller, Ariel Winter, David Selby, Wade Williams",
                plot = "Batman has not been seen for ten years. A new breed.",
                posterUrl = ""
            )
        )

        // Insert the hardcoded movies into the database asynchronously
        viewModelScope.launch(Dispatchers.IO) {
            movieDao.insertAll(movies)
        }
    }

    // Searches for a movie by its title using the OMDb API
    fun searchMovieByTitle(title: String, onResult: (Movie?) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = apiService.getMovieByTitle(title)
                if (response.response == "True") {
                    // If a movie is found, map the API response to a Movie object
                    val movie = Movie(
                        title = response.title,
                        year = response.year,
                        genre = response.genre,
                        plot = response.plot,
                        posterUrl = response.posterUrl,
                        actors = response.actors,
                        rated = response.rated,
                        released = response.released,
                        runtime = response.runtime,
                        director = response.director,
                        writer = response.writer
                    )
                    // Return the result on the main thread
                    withContext(Dispatchers.Main) { onResult(movie) }
                } else {
                    // If movie not found, return null
                    withContext(Dispatchers.Main) { onResult(null) }
                }
            } catch (e: Exception) {
                // Handle network or parsing errors
                e.printStackTrace()
                withContext(Dispatchers.Main) { onResult(null) }
            }
        }
    }

    // Retrieves all movies stored in the local database
    fun getAllMovies(onResult: (List<Movie>) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val movies = movieDao.getAllMovies()
            withContext(Dispatchers.Main) {
                onResult(movies)
            }
        }
    }

    // Searches for movies where the actor's name contains the input string
    fun searchMoviesByActor(actor: String, onResult: (List<Movie>) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = movieDao.findMoviesByActor(actor)
                withContext(Dispatchers.Main) {
                    onResult(result)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    onResult(emptyList()) // Return empty list in case of error
                }
            }
        }
    }

    // Inserts a single movie into the local database
    fun addMovieToDb(movie: Movie) {
        viewModelScope.launch(Dispatchers.IO) {
            movieDao.insert(movie)
        }
    }
}
