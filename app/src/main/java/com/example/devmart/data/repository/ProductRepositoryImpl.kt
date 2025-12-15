package com.example.devmart.data.repository

import com.example.devmart.data.mapper.toDomain
import com.example.devmart.data.remote.ProductApi
import com.example.devmart.domain.model.Product
import com.example.devmart.domain.repo.ProductRepository
import com.example.devmart.domain.repo.Result
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class ProductRepositoryImpl @Inject constructor(
    @Named("backend") retrofit: Retrofit
) : ProductRepository {
    private val api = retrofit.create(ProductApi::class.java)

    override suspend fun list(): Result<List<Product>> =
        try {
            val response = api.list()
            if (response.success) {
                Result.Ok(response.result.map { it.toDomain() })
            } else {
                Result.Err(response.message)
            }
        } catch (t: Throwable) {
            Result.Err(t.message ?: "상품 목록 조회 실패", t)
        }

    override suspend fun detail(id: String): Result<Product> =
        try {
            val response = api.detail(id.toLong())
            if (response.success) {
                Result.Ok(response.result.toDomain())
            } else {
                Result.Err(response.message)
            }
        } catch (t: Throwable) {
            Result.Err(t.message ?: "상품 상세 조회 실패", t)
        }
}
