package com.example.w1965221_moviedatabase_coursework

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.example.w1965221_moviedatabase_coursework.data.Movie
import com.example.w1965221_moviedatabase_coursework.data.MovieDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = Room.databaseBuilder(
            applicationContext,
            MovieDatabase::class.java,
            "movie_db"
        ).build()

        val viewModel = MainViewModel(db)

        enableEdgeToEdge()
        setContent {
            MovieApp(viewModel)
        }
    }
}



enum class Screen {
    MAIN, ADD_MOVIES, SEARCH_MOVIES, SEARCH_ACTORS, SEARCH_TITLES
}

@Composable
fun MovieApp(viewModel: MainViewModel) {
    var currentScreen by remember { mutableStateOf(Screen.MAIN) }

    when (currentScreen) {
        Screen.MAIN -> MainScreen(onNavigate = { currentScreen = it })
        Screen.ADD_MOVIES -> AddMoviesScreen(onBack = { currentScreen = Screen.MAIN })
        Screen.SEARCH_MOVIES -> SearchMovieScreen(onBack = { currentScreen = Screen.MAIN })
        Screen.SEARCH_ACTORS -> SearchActorsScreen(onBack = { currentScreen = Screen.MAIN })
        Screen.SEARCH_TITLES -> SearchTitlesScreen(onBack = { currentScreen = Screen.MAIN })
    }
}

@Composable
fun MainScreen(onNavigate: (Screen) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
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
        Button(onClick = { onNavigate(Screen.SEARCH_TITLES) }, modifier = Modifier.fillMaxWidth()) {
            Text("Search Titles Online")
        }
    }
}

@Composable
fun AddMoviesScreen(onBack: () -> Unit) {
    Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Add Movies Screen")
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onBack) {
            Text("Back")
        }
    }
}

@Composable
fun SearchMovieScreen(onBack: () -> Unit) {
    Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Search Movie Screen")
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onBack) {
            Text("Back")
        }
    }
}

@Composable
fun SearchActorsScreen(onBack: () -> Unit) {
    Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Search Actors Screen")
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onBack) {
            Text("Back")
        }
    }
}

@Composable
fun SearchTitlesScreen(onBack: () -> Unit) {
    Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Search Titles Screen")
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onBack) {
            Text("Back")
        }
    }
}



