package com.example.devmart.data.remote

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

// 리뷰 상품 정보 (ItemResponseDto와 동일)
data class ReviewItemDetailDto(
    val itemId: Long?,
    val itemName: String,
    val price: Int,
    val imagePath: String?,
    val brand: String,
    val new: Boolean,  // 백엔드가 "new" 사용
    val categories: List<String>,
    val productDetailImgUrl: String? = null,
    val productDetailContent: String? = null
)

// 리뷰 DTO (백엔드 ReviewResponse와 동일)
data class ReviewDto(
    val reviewId: Long?,
    val nickname: String? = null,
    val rating: Int?,
    val content: String?,
    val imgUrl: String?,
    val imgKey: String?,
    val item: ReviewItemDetailDto
)

// 리뷰 목록 응답
data class ReviewListResponse(
    val success: Boolean,
    val code: String,
    val message: String,
    val result: List<ReviewDto>
)

// 리뷰 등록 요청
data class CreateReviewRequest(
    val itemId: Long,
    val rating: Int,
    val content: String,
    val imgUrl: String = "",
    val imgKey: String = ""
)

// 리뷰 등록 응답
data class CreateReviewResponse(
    val success: Boolean,
    val code: String,
    val message: String,
    val result: ReviewDto?
)

interface ReviewApi {
    @GET("reviews/items/{itemId}")
    suspend fun getReviewsByItemId(@Path("itemId") itemId: Long): ReviewListResponse
    
    @POST("reviews")
    suspend fun createReview(@Body request: CreateReviewRequest): CreateReviewResponse
}

