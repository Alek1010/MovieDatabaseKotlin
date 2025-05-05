package com.example.w1965221_moviedatabase_coursework

import retrofit2.http.GET
import retrofit2.http.Query

interface OmdbApiService {
    @GET("/")
    suspend fun getMovieByTitle(
        @Query("t") title: String,
        @Query("apikey") apiKey: String = "5c12c11c"
    ): OmdbMovieResponse

}
