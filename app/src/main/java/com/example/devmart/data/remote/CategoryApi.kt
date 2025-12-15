package com.example.devmart.data.remote

import retrofit2.http.GET

// 카테고리 DTO
data class CategoryDto(
    val categoryId: Long,
    val categoryName: String
)

// 카테고리 목록 응답
data class CategoryListResponse(
    val success: Boolean,
    val code: String,
    val message: String,
    val result: List<CategoryDto>
)

interface CategoryApi {
    @GET("categories")
    suspend fun getCategories(): CategoryListResponse
}

