package com.conamobile.walpapers.networking

import com.conamobile.walpapers.models.RandomAllModels.RandomAllModels
import com.conamobile.walpapers.models.SearchModels.SearchHome
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface HomeService {

    @GET("photos/random?count=30")
    fun getAllRandomPhotos(@Query("count") count: Int): Call<ArrayList<RandomAllModels>>

    @GET("search/photos?page=1&per_page=1&query=")
    fun searchPhotos(
        @Query("page") page: Int,
        @Query("per_page") per_page: Int,
        @Query("query") search: String
    ): Call<SearchHome>


//    @GET("users/{username}")
//    fun getUser(@Path("username") username: String): Call<User>
//
//    @GET("photos/{id}")
//    fun getImagesCategories(@Path("id") id: String): Call<Result>
//
//    @GET("collections/")
//    fun getCollections(): Call<List<CollectionsModelItem>>
}