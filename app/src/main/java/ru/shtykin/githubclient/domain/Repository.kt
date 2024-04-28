package ru.shtykin.githubclient.domain

import kotlinx.coroutines.flow.Flow
import ru.shtykin.githubclient.domain.entity.ArchiveInfo
import ru.shtykin.githubclient.domain.entity.UserRepositoryModel
import java.util.Date

interface Repository {
    fun getUserRepositories(): List<UserRepositoryModel>
    suspend fun getUserRepositoriesFromDb(user: String): List<UserRepositoryModel>
    suspend fun getFlowInfoAllArchive(): Flow<List<ArchiveInfo>>
    suspend fun insertInfoData(user: String, name: String, date: Date)
    fun clearAllData()
}