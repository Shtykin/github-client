package ru.shtykin.githubclient.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.shtykin.githubclient.data.db.GithubClientDataBase
import ru.shtykin.githubclient.data.db.models.ArchiveInfoDbModel
import ru.shtykin.githubclient.data.mapper.Mapper
import ru.shtykin.githubclient.data.network.ApiService
import ru.shtykin.githubclient.domain.Repository
import ru.shtykin.githubclient.domain.entity.ArchiveInfo
import ru.shtykin.githubclient.domain.entity.UserRepositoryModel
import java.util.Date

class RepositoryImpl(
    private val apiService: ApiService,
    private val db: GithubClientDataBase,
    private val mapper: Mapper
) : Repository {

    private var userRepositories: List<UserRepositoryModel> = emptyList()

    override fun getUserRepositories(): List<UserRepositoryModel> =
        userRepositories

    override suspend fun getUserRepositoriesFromDb(user: String): List<UserRepositoryModel> {
        val response = apiService.getUserRepositories(user)
        userRepositories = response
            .body()
            ?.map {mapper.mapUserRepositoryDtoToUserRepositoryModel(it)}
            ?: emptyList()
        return userRepositories
    }

    override suspend fun getFlowInfoAllArchive(): Flow<List<ArchiveInfo>> =
        db.getArchiveInfoDao().getInfoAllArchive().map { list ->
            list.map {mapper.mapArchiveInfoDbModelToArchiveInfo(it)}
        }

    override suspend fun insertInfoData(
        user: String,
        name: String,
        date: Date
    ) {
        db.getArchiveInfoDao().insert(ArchiveInfoDbModel(
            user = user,
            name = name,
            downloadDate = mapper.dateToString(date)
        ))
    }

    override fun clearAllData() {
        db.getArchiveInfoDao().deleteAllInfo()
    }
}