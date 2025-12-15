package com.example.devmart.data.remote

import retrofit2.http.GET
import retrofit2.http.Path

// 상품 DTO
data class ItemDto(
    val itemId: Long,
    val itemName: String,
    val price: Long,
    val imagePath: String?,
    val brand: String,
    val categories: List<String>,
    val productDetailImgUrl: String?,
    val productDetailContent: String?,
    val new: Boolean
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
