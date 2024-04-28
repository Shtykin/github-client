package ru.shtykin.githubclient.domain

import kotlinx.coroutines.flow.Flow
import ru.shtykin.githubclient.domain.entity.ArchiveInfo
import ru.shtykin.githubclient.domain.entity.UserRepositoryModel
import java.util.Date

interface Repository {
    suspend fun getUserRepositories(user: String): List<UserRepositoryModel>
    suspend fun getFlowInfoAllArchive(): Flow<List<ArchiveInfo>>
    suspend fun insertInfoData(user: String, name: String, date: Date)
}