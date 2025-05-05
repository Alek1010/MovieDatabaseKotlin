package com.example.w1965221_moviedatabase_coursework

object RetrofitInstance {
    val api: OmdbApiService by lazy {
        retrofit2.Retrofit.Builder()
            .baseUrl("https://www.omdbapi.com")
            .addConverterFactory(retrofit2.converter.gson.GsonConverterFactory.create())
            .build()
            .create(OmdbApiService::class.java)
    }
}