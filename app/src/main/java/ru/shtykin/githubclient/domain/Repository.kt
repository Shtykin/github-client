package ru.shtykin.githubclient.domain

import ru.shtykin.githubclient.domain.entity.UserRepositoryModel

interface Repository {
    suspend fun getUserRepositories(user: String): List<UserRepositoryModel>
}