package com.clipforge.ai.data.network

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.clipforge.ai.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiClient {

    private lateinit var httpClient: OkHttpClient
    private lateinit var retrofit: Retrofit
    private lateinit var securePreferences: EncryptedSharedPreferences

    fun initialize(context: Context) {
        // Initialize secure storage
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        securePreferences = EncryptedSharedPreferences.create(
            context,
            "clipforge_secure_prefs",
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )

        // Build OkHttp client with interceptors
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.BASIC
            }
        }

        val authInterceptor = Interceptor { chain ->
            val originalRequest = chain.request()
            val token = getAccessToken()

            val requestBuilder = originalRequest.newBuilder()
            if (token != null) {
                requestBuilder.header("Authorization", "Bearer $token")
            }
            requestBuilder.header("Content-Type", "application/json")
            requestBuilder.header("User-Agent", "ClipForgeAI/1.0")

            val request = requestBuilder.build()
            try {
                chain.proceed(request)
            } catch (e: Exception) {
                // Handle token expiration and refresh
                if (e.message?.contains("401") == true) {
                    refreshAccessToken()
                    val newToken = getAccessToken()
                    val retryRequest = originalRequest.newBuilder()
                        .header("Authorization", "Bearer $newToken")
                        .build()
                    chain.proceed(retryRequest)
                } else {
                    throw e
                }
            }
        }

        httpClient = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(authInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .build()

        // Build Retrofit instance
        retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.API_BASE_URL)
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun getApiService(): ClipForgeApiService {
        return retrofit.create(ClipForgeApiService::class.java)
    }

    fun saveAccessToken(token: String) {
        securePreferences.edit().putString("access_token", token).apply()
    }

    fun saveRefreshToken(token: String) {
        securePreferences.edit().putString("refresh_token", token).apply()
    }

    fun getAccessToken(): String? {
        return securePreferences.getString("access_token", null)
    }

    fun getRefreshToken(): String? {
        return securePreferences.getString("refresh_token", null)
    }

    fun clearTokens() {
        securePreferences.edit()
            .remove("access_token")
            .remove("refresh_token")
            .apply()
    }

    private fun refreshAccessToken() {
        val refreshToken = getRefreshToken() ?: return
        try {
            val response = retrofit.create(ClipForgeApiService::class.java)
                .refreshToken(RefreshTokenRequest(refreshToken))
            saveAccessToken(response.accessToken)
            saveRefreshToken(response.refreshToken)
        } catch (e: Exception) {
            clearTokens()
        }
    }
}
