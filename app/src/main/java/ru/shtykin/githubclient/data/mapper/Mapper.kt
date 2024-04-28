package ru.shtykin.githubclient.data.mapper

import ru.shtykin.githubclient.data.db.models.ArchiveInfoDbModel
import ru.shtykin.githubclient.data.network.models.UserRepositoryDto
import ru.shtykin.githubclient.domain.entity.ArchiveInfo
import ru.shtykin.githubclient.domain.entity.UserRepositoryModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class Mapper {
    fun mapArchiveInfoDbModelToArchiveInfo(archiveInfoDbModel: ArchiveInfoDbModel) =
        ArchiveInfo(
            user = archiveInfoDbModel.user,
            name = archiveInfoDbModel.name,
            downloadDate = timeToTimeFormatted(archiveInfoDbModel.downloadDate)
        )

    private fun timeToTimeFormatted(time: String): String {
        return try {
            val fdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
            val date = fdf.parse(time)
            val sdf = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
            sdf.format(date)
        } catch (e: Exception) {
            ""
        }
    }

    fun dateToString(date: Date): String {
        return try {
            val fdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
            return fdf.format(date)
        } catch (e: Exception) {
            ""
        }
    }

    fun mapUserRepositoryDtoToUserRepositoryModel(userRepositoryDto: UserRepositoryDto) = UserRepositoryModel(
        user = userRepositoryDto.owner.login,
        userAvatarUrl = userRepositoryDto.owner.avatarUrl,
        name = userRepositoryDto.name,
        lastUpdate = timeToTimeFormatted(userRepositoryDto.updatedAt),
        htmlUrl = userRepositoryDto.htmlUrl,
        archiveUrl = "https://api.github.com/repos/${userRepositoryDto.owner.login}/${userRepositoryDto.name}/zipball/"
    )

}