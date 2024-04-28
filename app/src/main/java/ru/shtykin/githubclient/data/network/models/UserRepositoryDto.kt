package ru.shtykin.githubclient.data.network.models

import com.google.gson.annotations.SerializedName
import ru.shtykin.githubclient.domain.entity.UserRepositoryModel
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

data class UserRepositoryDto(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("owner") val owner: OwnerDto,
    @SerializedName("html_url") val htmlUrl: String,
    @SerializedName("updated_at") val updatedAt: String,
) {
    fun toUserRepositoryModel() = UserRepositoryModel(
        user = this.owner.login,
        userAvatarUrl = this.owner.avatarUrl,
        name = this.name,
        lastUpdate = timeToTimeFormatted(this.updatedAt),
        htmlUrl = this.htmlUrl,
        archiveUrl = "https://api.github.com/repos/${this.owner.login}/${this.name}/zipball/"
    )

    private fun timeToTimeFormatted(time: String): String {
        return try {
            val fdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
            val date = fdf.parse(time)
            val sdf = SimpleDateFormat("yyyy.MM.dd HH:mm", Locale.getDefault())
            sdf.format(date)
        } catch (e: Exception) {
            ""
        }
    }
}
