package com.example.devmart.data.remote

import com.example.devmart.data.remote.dto.AddressResponse
import com.example.devmart.data.remote.dto.UpdateAddressRequest
import com.example.devmart.data.remote.dto.DefaultResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT

interface AddressApi {

    @GET("/users/me/address")
    suspend fun getMyAddress(): AddressResponse

    @PUT("/users/me/address")
    suspend fun updateAddress(
        @Body request: UpdateAddressRequest
    ): DefaultResponse
}
