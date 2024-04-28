package ru.shtykin.githubclient.data.db.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "archive_info")
data class ArchiveInfoDbModel(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo
    val user: String = "",

    @ColumnInfo
    val name: String = "",

    @ColumnInfo
    val downloadDate: String = "",
)