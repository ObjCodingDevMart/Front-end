package com.example.devmart.data.repository

import com.example.devmart.data.remote.CreateOrderRequest
import com.example.devmart.data.remote.OrderApi
import com.example.devmart.data.remote.OrderResponse
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class OrderRepository @Inject constructor(
    @Named("backend") retrofit: Retrofit
) {
    private val api = retrofit.create(OrderApi::class.java)

    suspend fun createOrder(
        itemId: Long,
        quantity: Int,
        mileageToUse: Int = 0
    ): Result<OrderResponse> {
        return try {
            val response = api.createOrder(
                CreateOrderRequest(
                    itemId = itemId,
                    quantity = quantity,
                    mileageToUse = mileageToUse
                )
            )
            if (response.success) {
                Result.success(response)
            } else {
                Result.failure(Exception(response.message))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

