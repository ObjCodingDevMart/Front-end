package com.example.devmart.data.remote

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

data class KakaoLoginRequest(val kakaoAccessToken: String)
data class LoginResponse(val accessToken: String)
data class UserDto(val id: String, val email: String, val name: String?)

interface AuthApi {
    @POST("token/login") suspend fun loginWithKakao(@Body req: KakaoLoginRequest): LoginResponse
    @GET("v1/auth/me") suspend fun me(): UserDto
}
