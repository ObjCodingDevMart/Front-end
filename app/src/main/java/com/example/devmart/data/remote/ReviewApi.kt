package com.example.devmart.data.remote

import retrofit2.http.GET
import retrofit2.http.Path

// 리뷰 DTO
data class ReviewDto(
    val reviewId: Long,
    val userId: Long,
    val userName: String,
    val rating: Int,
    val content: String,
    val createdAt: String,
    val images: List<String>?
)

// 리뷰 목록 응답
data class ReviewListResponse(
    val success: Boolean,
    val code: String,
    val message: String,
    val result: List<ReviewDto>
)

interface ReviewApi {
    @GET("reviews/items/{itemId}")
    suspend fun getReviewsByItemId(@Path("itemId") itemId: Long): ReviewListResponse
}

