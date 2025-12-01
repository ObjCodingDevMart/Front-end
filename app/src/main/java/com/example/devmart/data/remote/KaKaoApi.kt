package com.example.devmart.data.remote

import com.example.devmart.data.remote.dto.KakaoAddressResponse
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface KakaoApi {

    @Headers("Authorization: KakaoAK ${"YOUR_API_KEY"}")
    @GET("/v2/local/search/address.json")
    suspend fun searchAddress(
        @Query("query") keyword: String
    ): KakaoAddressResponse
}
