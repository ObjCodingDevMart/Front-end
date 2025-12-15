package com.example.devmart.domain.model
data class User(val id: String, val email: String, val name: String?)
data class Product(
    val id: String,
    val brand: String,
    val title: String,
    val price: Long,
    val imageUrl: String?,
    val categories: List<String> = emptyList(),
    val detailImageUrl: String? = null,
    val detailContent: String? = null,
    val isNew: Boolean = false
)
data class Review(
    val id: String,
    val rating: Int, // 1-5
    val content: String,
    val imgUrl: String? = null,  // 리뷰 이미지 URL
    val userName: String? = null  // 작성자 이름 (마스킹용)
)
