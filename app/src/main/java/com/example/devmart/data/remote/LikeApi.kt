package com.example.devmart.data.remote

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.POST

data class LikeRequest(
    val itemId: Long
)

data class LikeResponse(
    val success: Boolean,
    val code: String,
    val message: String,
    val result: Any?
)

// 즐겨찾기 상품 정보 (백엔드 ItemResponseDto와 동일)
data class LikeItemDetailDto(
    val itemId: Long?,
    val itemName: String,
    val price: Int,
    val imagePath: String?,
    val brand: String,
    val isNew: Boolean,
    val categories: List<String>,
    val productDetailImgUrl: String? = null,
    val productDetailContent: String? = null
)

// 즐겨찾기 아이템 DTO (백엔드 LikeBookmarkResponse와 동일)
data class LikeItemDto(
    val bookmarkId: Long?,
    val item: LikeItemDetailDto
)

// 즐겨찾기 목록 응답
data class LikeListResponse(
    val success: Boolean,
    val code: String,
    val message: String,
    val result: List<LikeItemDto>
)

interface LikeApi {
    @GET("likes")
    suspend fun getLikes(): LikeListResponse

    @POST("likes")
    suspend fun addLike(@Body request: LikeRequest): LikeResponse

    @HTTP(method = "DELETE", path = "likes", hasBody = true)
    suspend fun removeLike(@Body request: LikeRequest): LikeResponse
}
