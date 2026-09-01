package com.clipforge.ai.data.local.dao

import androidx.room.*
import com.clipforge.ai.data.local.entity.ClipEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ClipDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertClip(clip: ClipEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertClips(clips: List<ClipEntity>)

    @Update
    suspend fun updateClip(clip: ClipEntity)

    @Delete
    suspend fun deleteClip(clip: ClipEntity)

    @Query("SELECT * FROM clips WHERE id = :id")
    suspend fun getClipById(id: String): ClipEntity?

    @Query("SELECT * FROM clips WHERE projectId = :projectId ORDER BY createdAt DESC")
    fun getClipsByProjectId(projectId: String): Flow<List<ClipEntity>>

    @Query("SELECT * FROM clips WHERE status = 'ready' OR status = 'exported'")
    fun getExportedClips(): Flow<List<ClipEntity>>

    @Query("SELECT * FROM clips WHERE syncedWithServer = 0")
    suspend fun getUnsyncedClips(): List<ClipEntity>

    @Query("UPDATE clips SET status = :status WHERE id = :id")
    suspend fun updateClipStatus(id: String, status: String)

    @Query("DELETE FROM clips WHERE projectId = :projectId")
    suspend fun deleteClipsByProjectId(projectId: String)
}
