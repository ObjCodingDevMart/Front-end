package com.example.devmart.data.remote

import retrofit2.http.GET
import retrofit2.http.Path

// 상품 DTO
// 상품 DTO (백엔드 ItemResponseDto와 동일)
data class ItemDto(
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

// 상품 목록 응답
data class ItemListResponse(
    val success: Boolean,
    val code: String,
    val message: String,
    val result: List<ItemDto>
)

// 상품 상세 응답
data class ItemDetailResponse(
    val success: Boolean,
    val code: String,
    val message: String,
    val result: ItemDto
)

interface ProductApi {
    @GET("items")
    suspend fun list(): ItemListResponse

    @GET("items/{itemId}")
    suspend fun detail(@Path("itemId") itemId: Long): ItemDetailResponse
}
