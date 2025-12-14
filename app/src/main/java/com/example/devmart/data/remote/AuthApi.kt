package com.example.devmart.data.remote

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

data class KakaoLoginRequest(val kakaoAccessToken: String)

// 백엔드 로그인 응답 구조
data class LoginResult(
    val accessToken: String,
    val refreshToken: String,
    val accessTokenExpiresIn: Long,
    val refreshTokenExpiresIn: Long
)

data class LoginResponse(
    val success: Boolean,
    val code: String,
    val message: String,
    val result: LoginResult
)

data class UserDto(val id: String, val email: String, val name: String?)

interface AuthApi {
    @POST("token/login") suspend fun loginWithKakao(@Body req: KakaoLoginRequest): LoginResponse
    @GET("v1/auth/me") suspend fun me(): UserDto
}
