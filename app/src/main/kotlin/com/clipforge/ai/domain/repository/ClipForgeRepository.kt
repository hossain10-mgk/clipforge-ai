package com.clipforge.ai.domain.repository

import com.clipforge.ai.data.model.ClipRequest
import com.clipforge.ai.data.model.ClipResponse
import com.clipforge.ai.data.model.VideoAnalysisResponse
import com.clipforge.ai.data.model.ExportRequest
import com.clipforge.ai.data.model.ExportResponse
import com.clipforge.ai.data.model.ProjectResponse
import com.clipforge.ai.data.local.entity.ProjectEntity
import com.clipforge.ai.data.local.entity.ClipEntity
import kotlinx.coroutines.flow.Flow

interface ClipForgeRepository {
    
    // Authentication
    suspend fun login(email: String, password: String): Result<String>
    suspend fun register(email: String, password: String, name: String): Result<String>
    suspend fun logout(): Result<Unit>
    
    // Video Analysis & Clip Generation
    suspend fun analyzeVideo(request: ClipRequest): Result<VideoAnalysisResponse>
    suspend fun generateClips(request: ClipRequest): Result<List<ClipResponse>>
    
    // Transcription
    suspend fun transcribeVideo(videoId: String, language: String = "en"): Result<VideoAnalysisResponse>
    
    // Clip Management
    suspend fun getClip(clipId: String): Result<ClipEntity?>
    fun getClipsByProject(projectId: String): Flow<List<ClipEntity>>
    suspend fun updateClip(clip: ClipEntity): Result<Unit>
    suspend fun deleteClip(clipId: String): Result<Unit>
    
    // Export
    suspend fun exportClip(clipId: String, request: ExportRequest): Result<ExportResponse>
    suspend fun checkExportStatus(exportId: String): Result<ExportResponse>
    fun getExportProgress(exportId: String): Flow<Int>
    
    // Projects
    fun getProjects(): Flow<List<ProjectEntity>>
    suspend fun createProject(name: String, sourceUrl: String? = null): Result<ProjectResponse>
    suspend fun deleteProject(projectId: String): Result<Unit>
    
    // Local Cache
    suspend fun saveClip(clip: ClipEntity): Result<Unit>
    suspend fun saveProject(project: ProjectEntity): Result<Unit>
    fun getAllProjects(): Flow<List<ProjectEntity>>
    fun getAllClips(): Flow<List<ClipEntity>>
}
