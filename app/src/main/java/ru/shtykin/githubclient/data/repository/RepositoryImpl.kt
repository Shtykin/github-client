package ru.shtykin.githubclient.data.repository

import android.util.Log
import ru.shtykin.githubclient.data.network.ApiService
import ru.shtykin.githubclient.domain.Repository
import ru.shtykin.githubclient.domain.entity.UserRepositoryModel

class RepositoryImpl(
    private val apiService: ApiService
) : Repository {

    override suspend fun getUserRepositories(user: String): List<UserRepositoryModel> {
        val response = apiService.getUserRepositories(user)
        return response.body()?.map { it.toUserRepositoryModel() } ?: emptyList()
    }
}