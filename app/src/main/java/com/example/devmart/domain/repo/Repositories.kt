package com.example.devmart.domain.repo

import com.example.devmart.domain.model.Product
import com.example.devmart.domain.model.User

sealed class Result<out T> {
    data class Ok<T>(val value: T): Result<T>()
    data class Err(val message: String, val cause: Throwable? = null): Result<Nothing>()
}

interface AuthRepository {
    suspend fun me(): Result<User>
}
interface ProductRepository {
    suspend fun list(): Result<List<Product>>
    suspend fun detail(id: String): Result<Product>
}
