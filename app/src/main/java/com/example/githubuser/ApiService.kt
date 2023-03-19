package com.example.githubuser

import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("search/users")
    @Headers("Authorization: token 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")
    fun getGithub(
        @Query("q") username: String
    ): Call<GithubResponse>
    @Headers("Authorization: token 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")
    @GET("users/{username}")
    fun getDetailUser(@Path("username") username: String): Call<DetailUserResponse>
    @Headers("Authorization: token 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")
    @GET("users/{username}/followers")
    fun getFollowers(@Path("username") username: String): Call<List<ItemsItem>>
    @Headers("Authorization: token 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")
    @GET("users/{username}/following")
    fun getFollowing(@Path("username") username: String): Call<List<ItemsItem>>
}