package com.app.cookwise

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private val retrofit = Retrofit.Builder()
    .baseUrl("https://www.themealdb.com/api/json/v1/1/")
    .addConverterFactory(GsonConverterFactory.create())
    .build()

val retrofitService: ApiService = retrofit.create(ApiService::class.java)

interface ApiService {
    @GET("random.php")
    suspend fun getRecipe(): RecipeResponse

    @GET("search.php")
    suspend fun searchRecipe(@Query("s") query: String): RecipeResponse
}