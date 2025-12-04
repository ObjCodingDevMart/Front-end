package com.example.devmart.data.remote.dto

data class KakaoAddressResponse(
    val documents: List<KakaoAddressDocument>
)

data class KakaoAddressDocument(
    val address_name: String,
    val road_address: KakaoRoadAddress?,
    val address: KakaoJibunAddress?
)

data class KakaoRoadAddress(
    val address_name: String,
    val building_name: String?,
    val zone_no: String
)

data class KakaoJibunAddress(
    val address_name: String
)
