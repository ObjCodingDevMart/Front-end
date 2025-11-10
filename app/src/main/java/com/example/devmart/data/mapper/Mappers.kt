package com.example.devmart.data.mapper

import com.example.devmart.data.remote.ProductDto
import com.example.devmart.data.remote.UserDto
import com.example.devmart.domain.model.Product
import com.example.devmart.domain.model.User

fun UserDto.toDomain() = User(id, email, name)
fun ProductDto.toDomain() = Product(id, title, price, imageUrl)
