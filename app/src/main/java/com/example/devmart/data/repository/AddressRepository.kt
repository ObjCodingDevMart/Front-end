package com.example.devmart.data.repository

import com.example.devmart.data.remote.AddressApi
import com.example.devmart.data.remote.dto.UpdateAddressRequest
import javax.inject.Inject

class AddressRepository @Inject constructor(
    private val api: AddressApi
) {

    suspend fun getMyAddress() = api.getMyAddress()

    suspend fun updateAddress(req: UpdateAddressRequest) = api.updateAddress(req)
}