package ru.shtykin.githubclient.domain.entity

data class UserRepositoryModel(
    val user: String,
    val userAvatarUrl: String,
    val name: String,
    val lastUpdate: String,
    val htmlUrl: String,
    val archiveUrl: String,
)
