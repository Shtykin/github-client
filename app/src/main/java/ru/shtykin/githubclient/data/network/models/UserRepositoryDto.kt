package ru.shtykin.githubclient.data.network.models

import com.google.gson.annotations.SerializedName

data class UserRepositoryDto(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("owner") val owner: OwnerDto,
    @SerializedName("html_url") val htmlUrl: String,
    @SerializedName("updated_at") val updatedAt: String,
)
