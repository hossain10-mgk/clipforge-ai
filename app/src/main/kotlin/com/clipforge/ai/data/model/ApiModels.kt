package com.clipforge.ai.data.model

data class ClipRequest(
    val videoUrl: String? = null,
    val videoPath: String? = null,
    val title: String? = null,
    val description: String? = null,
    val language: String = "en",
    val aspectRatio: String = "9:16",
    val minClipDuration: Int = 15,
    val maxClipDuration: Int = 60
)

data class ClipResponse(
    val id: String,
    val projectId: String,
    val videoUrl: String,
    val title: String,
    val description: String,
    val hashtags: List<String>,
    val duration: Int,
    val aspectRatio: String,
    val thumbnailUrl: String,
    val startTime: Int,
    val endTime: Int,
    val engagementScore: Float,
    val captionsUrl: String? = null,
    val status: String,
    val createdAt: Long,
    val updatedAt: Long
)

data class VideoAnalysisResponse(
    val videoId: String,
    val duration: Int,
    val scenes: List<Scene>,
    val transcript: List<TranscriptSegment>,
    val keyMoments: List<KeyMoment>,
    val suggestedClips: List<SuggestedClip>,
    val analysis: VideoAnalysis
)

data class Scene(
    val startTime: Int,
    val endTime: Int,
    val description: String,
    val confidence: Float
)

data class TranscriptSegment(
    val startTime: Int,
    val endTime: Int,
    val text: String,
    val speaker: String? = null
)

data class KeyMoment(
    val startTime: Int,
    val endTime: Int,
    val type: String, // "laugh", "applause", "silence", "emotional", etc.
    val score: Float
)

data class SuggestedClip(
    val startTime: Int,
    val endTime: Int,
    val reason: String,
    val engagementScore: Float,
    val suggestedTitle: String,
    val suggestedHashtags: List<String>
)

data class VideoAnalysis(
    val sentiment: String,
    val topics: List<String>,
    val difficulty: String,
    val recommendedFormats: List<String>
)

data class ExportRequest(
    val clipId: String,
    val aspectRatio: String = "9:16",
    val resolution: String = "1080p",
    val fps: Int = 30,
    val includeWatermark: Boolean = false,
    val captionStyle: String = "default"
)

data class ExportResponse(
    val exportId: String,
    val clipId: String,
    val status: String, // "pending", "processing", "completed", "failed"
    val progress: Int,
    val downloadUrl: String? = null,
    val fileSize: Long? = null,
    val completedAt: Long? = null,
    val error: String? = null
)

data class ProjectResponse(
    val id: String,
    val name: String,
    val sourceUrl: String? = null,
    val sourceLocalPath: String? = null,
    val clipCount: Int,
    val totalDuration: Int,
    val createdAt: Long,
    val updatedAt: Long,
    val clips: List<ClipResponse>? = null
)
