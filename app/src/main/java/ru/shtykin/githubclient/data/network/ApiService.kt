package ru.shtykin.githubclient.data.network

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import ru.shtykin.githubclient.data.network.models.UserRepositoryDto


interface ApiService {
    @GET("/users/{name}/repos")
    suspend fun getUserRepositories(@Path("name") user: String): Response<List<UserRepositoryDto>>

}