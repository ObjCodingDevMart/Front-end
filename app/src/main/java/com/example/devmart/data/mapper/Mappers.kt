package com.example.devmart.data.mapper

import com.example.devmart.data.remote.ItemDto
import com.example.devmart.domain.model.Product

fun ItemDto.toDomain() = Product(
    id = itemId.toString(),
    brand = brand,
    title = itemName,
    price = price,
    imageUrl = imagePath,
    categories = categories,
    detailImageUrl = productDetailImgUrl,
    detailContent = productDetailContent,
    isNew = new
)
