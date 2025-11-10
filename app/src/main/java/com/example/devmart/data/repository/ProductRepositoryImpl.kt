package com.example.devmart.data.repository

import com.example.devmart.data.mapper.toDomain
import com.example.devmart.data.remote.ProductApi
import com.example.devmart.domain.model.Product
import com.example.devmart.domain.repo.ProductRepository
import com.example.devmart.domain.repo.Result
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductRepositoryImpl @Inject constructor(
    retrofit: Retrofit
) : ProductRepository {
    private val api = retrofit.create(ProductApi::class.java)

    override suspend fun list(): Result<List<Product>> =
        try { Result.Ok(api.list().map { it.toDomain() }) }
        catch (t: Throwable) { Result.Err(t.message ?: "list failed", t) }

    override suspend fun detail(id: String): Result<Product> =
        try { Result.Ok(api.detail(id).toDomain()) }
        catch (t: Throwable) { Result.Err(t.message ?: "detail failed", t) }
}
