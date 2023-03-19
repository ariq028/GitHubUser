package com.example.githubuser

import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("search/users")
    @Headers("Authorization: token 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")
    fun getGithub(
        @Query("q") username: String
    ): Call<GithubResponse>
    @GET("users/{username}")
    fun getDetailUser(@Path("username") username: String): Call<DetailUserResponse>
    @GET("users/{username}/followers")
    fun getFollowers(@Path("username") username: String): Call<List<ItemsItem>>
    @GET("users/{username}/following")
    fun getFollowing(@Path("username") username: String): Call<List<ItemsItem>>
}