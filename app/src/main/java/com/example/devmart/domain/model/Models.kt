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
    val userId: String,
    val userName: String,
    val rating: Int, // 1-5
    val content: String,
    val date: String,
    val images: List<String>? = null
)
