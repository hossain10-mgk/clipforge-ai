package com.clipforge.ai.data.network

import com.clipforge.ai.data.model.ClipRequest
import com.clipforge.ai.data.model.ClipResponse
import com.clipforge.ai.data.model.VideoAnalysisResponse
import com.clipforge.ai.data.model.ExportRequest
import com.clipforge.ai.data.model.ExportResponse
import com.clipforge.ai.data.model.ProjectResponse
import retrofit2.http.*

interface ClipForgeApiService {

    // Video Analysis & Clip Generation
    @POST("api/v1/videos/analyze")
    suspend fun analyzeVideo(
        @Body request: ClipRequest
    ): VideoAnalysisResponse

    @GET("api/v1/videos/{videoId}/clips")
    suspend fun getGeneratedClips(
        @Path("videoId") videoId: String,
        @Header("Authorization") token: String
    ): List<ClipResponse>

    @POST("api/v1/clips/generate")
    suspend fun generateClips(
        @Body request: ClipRequest,
        @Header("Authorization") token: String
    ): ClipResponse

    // Captions & Transcription
    @POST("api/v1/videos/{videoId}/transcribe")
    suspend fun transcribeVideo(
        @Path("videoId") videoId: String,
        @Query("language") language: String = "en",
        @Header("Authorization") token: String
    ): VideoAnalysisResponse

    // Clip Editing & Export
    @POST("api/v1/clips/{clipId}/export")
    suspend fun exportClip(
        @Path("clipId") clipId: String,
        @Body request: ExportRequest,
        @Header("Authorization") token: String
    ): ExportResponse

    @GET("api/v1/exports/{exportId}/status")
    suspend fun checkExportStatus(
        @Path("exportId") exportId: String,
        @Header("Authorization") token: String
    ): ExportResponse

    // Projects
    @GET("api/v1/projects")
    suspend fun getProjects(
        @Header("Authorization") token: String
    ): List<ProjectResponse>

    @POST("api/v1/projects")
    suspend fun createProject(
        @Body name: Map<String, String>,
        @Header("Authorization") token: String
    ): ProjectResponse

    @DELETE("api/v1/projects/{projectId}")
    suspend fun deleteProject(
        @Path("projectId") projectId: String,
        @Header("Authorization") token: String
    ): Unit

    // Authentication
    @POST("api/v1/auth/register")
    suspend fun register(
        @Body credentials: AuthRequest
    ): AuthResponse

    @POST("api/v1/auth/login")
    suspend fun login(
        @Body credentials: AuthRequest
    ): AuthResponse

    @POST("api/v1/auth/refresh")
    suspend fun refreshToken(
        @Body request: RefreshTokenRequest
    ): AuthResponse

    // Health Check
    @GET("api/v1/health")
    suspend fun healthCheck(): HealthCheckResponse
}

data class AuthRequest(
    val email: String,
    val password: String
)

data class AuthResponse(
    val accessToken: String,
    val refreshToken: String,
    val expiresIn: Long,
    val user: UserData
)

data class UserData(
    val id: String,
    val email: String,
    val name: String
)

data class RefreshTokenRequest(
    val refreshToken: String
)

data class HealthCheckResponse(
    val status: String,
    val timestamp: Long
)
