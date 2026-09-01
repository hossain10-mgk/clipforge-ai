package com.clipforge.ai.data.local.dao

import androidx.room.*
import com.clipforge.ai.data.local.entity.ExportEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ExportDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExport(export: ExportEntity)

    @Update
    suspend fun updateExport(export: ExportEntity)

    @Delete
    suspend fun deleteExport(export: ExportEntity)

    @Query("SELECT * FROM exports WHERE id = :id")
    suspend fun getExportById(id: String): ExportEntity?

    @Query("SELECT * FROM exports WHERE clipId = :clipId ORDER BY createdAt DESC LIMIT 1")
    suspend fun getLatestExportForClip(clipId: String): ExportEntity?

    @Query("SELECT * FROM exports WHERE status = 'processing' OR status = 'pending'")
    fun getActiveExports(): Flow<List<ExportEntity>>

    @Query("SELECT * FROM exports WHERE status = 'completed' ORDER BY completedAt DESC")
    fun getCompletedExports(): Flow<List<ExportEntity>>

    @Query("UPDATE exports SET progress = :progress, status = :status WHERE id = :id")
    suspend fun updateExportProgress(id: String, progress: Int, status: String)

    @Query("DELETE FROM exports WHERE clipId = :clipId")
    suspend fun deleteExportsByClipId(clipId: String)
}
