package com.example.devmart.data.repository

import com.example.devmart.data.remote.AddressApi
import com.example.devmart.data.remote.dto.AddressResponse
import com.example.devmart.data.remote.dto.DefaultResponse
import com.example.devmart.data.remote.dto.UpdateAddressRequest
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Named

class AddressRepository @Inject constructor(
    @Named("backend") retrofit: Retrofit
) {
    private val api = retrofit.create(AddressApi::class.java)

    suspend fun getMyAddress(): AddressResponse {
        return api.getMyAddress()
    }

    suspend fun updateAddress(req: UpdateAddressRequest): DefaultResponse {
        return api.updateAddress(req)
    }
}
