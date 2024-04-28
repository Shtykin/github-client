package ru.shtykin.githubclient.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.shtykin.githubclient.data.db.models.ArchiveInfoDbModel

@Dao
interface ArchiveInfoDao {
    @Query("SELECT * from archive_info")
    fun getInfoAllArchive(): Flow<List<ArchiveInfoDbModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(archiveInfo: ArchiveInfoDbModel)
}