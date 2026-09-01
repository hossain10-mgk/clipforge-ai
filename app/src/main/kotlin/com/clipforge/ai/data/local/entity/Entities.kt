package com.clipforge.ai.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey

@Entity(tableName = "projects")
data class ProjectEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val sourceUrl: String? = null,
    val sourceLocalPath: String? = null,
    val clipCount: Int = 0,
    val totalDuration: Int = 0,
    val synced: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

@Entity(
    tableName = "clips",
    foreignKeys = [
        ForeignKey(
            entity = ProjectEntity::class,
            parentColumns = ["id"],
            childColumns = ["projectId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ClipEntity(
    @PrimaryKey
    val id: String,
    val projectId: String,
    val videoUrl: String? = null,
    val videoLocalPath: String? = null,
    val title: String,
    val description: String? = null,
    val hashtags: List<String> = emptyList(),
    val duration: Int,
    val aspectRatio: String = "9:16",
    val thumbnailPath: String? = null,
    val startTime: Int,
    val endTime: Int,
    val engagementScore: Float = 0f,
    val captionsPath: String? = null,
    val status: String = "draft", // draft, processing, ready, exported, failed
    val syncedWithServer: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

@Entity(
    tableName = "exports",
    foreignKeys = [
        ForeignKey(
            entity = ClipEntity::class,
            parentColumns = ["id"],
            childColumns = ["clipId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ExportEntity(
    @PrimaryKey
    val id: String,
    val clipId: String,
    val resolution: String = "1080p",
    val fps: Int = 30,
    val outputPath: String? = null,
    val fileSize: Long? = null,
    val status: String = "pending", // pending, processing, completed, failed
    val progress: Int = 0,
    val error: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val completedAt: Long? = null
)
