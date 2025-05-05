package com.example.w1965221_moviedatabase_coursework

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

import androidx.room.Room
import com.example.w1965221_moviedatabase_coursework.data.Movie
import com.example.w1965221_moviedatabase_coursework.data.MovieDatabase



// MainActivity sets up the database and ViewModel, and loads the Composable UI
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Create Room database instance
        val db = Room.databaseBuilder(
            applicationContext,
            MovieDatabase::class.java,
            "movie_db"
        ).fallbackToDestructiveMigration().build()

        // Create instance of MainViewModel and pass database
        val viewModel = MainViewModel(db)

        enableEdgeToEdge() // Optional edge-to-edge layout
        setContent {
            MovieApp(viewModel) // Load the main UI Composable
        }
    }
}

// Enum to track which screen the user is currently on
enum class Screen {
    MAIN, ADD_MOVIES, SEARCH_MOVIES, SEARCH_ACTORS, SHOW_MOVIES
}

// Main Composable that handles screen navigation
@Composable
fun MovieApp(viewModel: MainViewModel) {
    var currentScreen by rememberSaveable { mutableStateOf(Screen.MAIN) }

    // Switch between screens based on state
    when (currentScreen) {
        Screen.MAIN -> MainScreen(onNavigate = { currentScreen = it })
        Screen.ADD_MOVIES -> AddMoviesScreen(onBack = { currentScreen = Screen.MAIN }, viewModel)
        Screen.SEARCH_MOVIES -> SearchMovieScreen(onBack = { currentScreen = Screen.MAIN }, viewModel)
        Screen.SEARCH_ACTORS -> SearchActorsScreen(onBack = { currentScreen = Screen.MAIN }, viewModel)
        Screen.SHOW_MOVIES -> SHOW_MOVIES(onBack = { currentScreen = Screen.MAIN }, viewModel)
    }
}

// Main menu with navigation buttons to other features
@Composable
fun MainScreen(onNavigate: (Screen) -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = { onNavigate(Screen.ADD_MOVIES) }, modifier = Modifier.fillMaxWidth()) {
            Text("Add Movies to DB")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { onNavigate(Screen.SEARCH_MOVIES) }, modifier = Modifier.fillMaxWidth()) {
            Text("Search for Movies")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { onNavigate(Screen.SEARCH_ACTORS) }, modifier = Modifier.fillMaxWidth()) {
            Text("Search for Actors")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { onNavigate(Screen.SHOW_MOVIES) }, modifier = Modifier.fillMaxWidth()) {
            Text("show movies")
        }
    }
}

// Screen to insert predefined movies into the local Room database
@Composable
fun AddMoviesScreen(onBack: () -> Unit, viewModel: MainViewModel) {
    var message by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Add Hardcoded Movies")
        Spacer(Modifier.height(16.dp))

        Button(onClick = {
            viewModel.addMoviesToDb() // Insert hardcoded movies
            message = "Movies added to local DB!"
        }) {
            Text("Add Movies to DB")
        }

        Spacer(Modifier.height(8.dp))
        Text(message)

        Spacer(Modifier.height(16.dp))
        Button(onClick = onBack) {
            Text("Back")
        }
    }
}

// Screen to search for a movie via API and optionally save to DB
@Composable
fun SearchMovieScreen(onBack: () -> Unit, viewModel: MainViewModel) {
    var title by remember { mutableStateOf("") }
    var result by remember { mutableStateOf<Movie?>(null) }
    var message by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text("Search for Movie by Title")

        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Enter Movie Title") }
        )

        Spacer(Modifier.height(8.dp))

        Row {
            Button(onClick = {
                // Call API to retrieve movie info by title
                viewModel.searchMovieByTitle(title) { movie ->
                    result = movie
                    message = if (movie == null) "Movie not found!" else ""
                }
            }) {
                Text("Retrieve Movie")
            }

            Spacer(Modifier.width(8.dp))

            // Save result to DB if available
            Button(
                onClick = {
                    result?.let {
                        viewModel.addMovieToDb(it)
                        message = "Movie saved to DB!"
                    }
                },
                enabled = result != null
            ) {
                Text("Save Movie to DB")
            }
        }

        Spacer(Modifier.height(16.dp))

        // Show movie details if a movie was found
        result?.let {
            Text("Title: ${it.title}")
            Text("Year: ${it.year}")
            Text("Rated: ${it.rated}")
            Text("Released: ${it.released}")
            Text("Runtime: ${it.runtime}")
            Text("Genre: ${it.genre}")
            Text("Director: ${it.director}")
            Text("Writer: ${it.writer}")
            Text("Actors: ${it.actors}")
            Text("Plot: ${it.plot}")
        }

        Spacer(Modifier.height(8.dp))
        if (message.isNotBlank()) {
            Text(message, color = MaterialTheme.colorScheme.primary)
        }

        Spacer(Modifier.height(16.dp))
        Button(onClick = onBack) {
            Text("Back")
        }
    }
}

// Screen to search database for movies by actor name
@Composable
fun SearchActorsScreen(onBack: () -> Unit, viewModel: MainViewModel) {
    var actorName by remember { mutableStateOf("") }
    var searchResults by remember { mutableStateOf<List<Movie>>(emptyList()) }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        Text("Search for Movies by Actor Name")

        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = actorName,
            onValueChange = { actorName = it },
            label = { Text("Enter part of actor's name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(8.dp))

        Button(
            onClick = {
                // Query Room DB for movies with matching actor
                viewModel.searchMoviesByActor(actorName) {
                    searchResults = it
                }
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Search")
        }

        Spacer(Modifier.height(16.dp))

        // Display results in a scrolling list
        LazyColumn {
            items(searchResults) { movie ->
                Column(modifier = Modifier.padding(8.dp)) {
                    Text("Title: ${movie.title}")
                    Text("Actors: ${movie.actors}")
                    Divider(modifier = Modifier.padding(vertical = 8.dp))
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onBack, modifier = Modifier.align(Alignment.CenterHorizontally)) {
            Text("Back")
        }
    }
}

// Screen to show all movies stored in local database
@Composable
fun SHOW_MOVIES(
    onBack: () -> Unit,
    viewModel: MainViewModel
) {
    var movieList by remember { mutableStateOf<List<Movie>>(emptyList()) }

    // Load all movies when the screen is first displayed
    LaunchedEffect(Unit) {
        viewModel.getAllMovies {
            movieList = it
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("All Movies", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(modifier = Modifier.weight(1f)) {
            items(movieList) { movie ->
                Text(text = movie.title ?: "No Title")
                Divider()
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onBack) {
            Text("Back")
        }
    }
}
