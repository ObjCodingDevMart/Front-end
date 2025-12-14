package com.example.devmart.data.mapper

import com.example.devmart.data.remote.ProductDto
import com.example.devmart.domain.model.Product

fun ProductDto.toDomain() = Product(id, brand , title, price, imageUrl)
