package com.example.devmart.domain.model
data class User(val id: String, val email: String, val name: String?)
data class Product(val id: String, val title: String, val price: Long, val imageUrl: String?)
