package com.example.devmart.data.remote

import retrofit2.http.GET
import retrofit2.http.Path

data class ProductDto(val id: String, val title: String, val price: Long, val imageUrl: String?)

interface ProductApi {
    @GET("v1/products") suspend fun list(): List<ProductDto>
    @GET("v1/products/{id}") suspend fun detail(@Path("id") id: String): ProductDto
}
