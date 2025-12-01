package com.example.devmart.data.repository

import com.example.devmart.data.remote.KakaoApi
import com.example.devmart.ui.payment.Address
import javax.inject.Inject

class KakaoAddressRepository @Inject constructor(
    private val api: KakaoApi
) {

    suspend fun search(keyword: String): List<Address> {
        val res = api.searchAddress(keyword)

        return res.documents.mapNotNull { doc ->
            val road = doc.road_address
            val jibun = doc.address

            if (road == null && jibun == null) return@mapNotNull null

            Address(
                roadAddress = road?.address_name ?: "",
                jibunAddress = jibun?.address_name ?: "",
                postalCode = road?.zone_no ?: "",
                detail = ""
            )
        }
    }
}
