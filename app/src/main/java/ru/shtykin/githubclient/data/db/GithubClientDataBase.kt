package ru.shtykin.githubclient.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.shtykin.githubclient.data.db.dao.ArchiveInfoDao
import ru.shtykin.githubclient.data.db.models.ArchiveInfoDbModel

@Database(entities = [ArchiveInfoDbModel::class], version = 1)
abstract class GithubClientDataBase: RoomDatabase() {
    abstract fun getArchiveInfoDao(): ArchiveInfoDao
}