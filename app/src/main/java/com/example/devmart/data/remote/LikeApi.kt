package com.example.devmart.data.remote

import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.POST
import retrofit2.http.Path

data class LikeRequest(
    val itemId: Long
)

data class LikeResponse(
    val success: Boolean,
    val code: String,
    val message: String,
    val result: Any?
)

interface LikeApi {
    @POST("likes")
    suspend fun addLike(@Body request: LikeRequest): LikeResponse

    @DELETE("likes/{itemId}")
    suspend fun removeLike(@Path("itemId") itemId: Long): LikeResponse
}

