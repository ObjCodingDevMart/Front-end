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

// 백엔드 유저 정보 응답 구조
data class OrderStatusCounts(
    val PROCESSING: Int,
    val COMPLETE: Int,
    val CANCEL: Int
)

data class UserResult(
    val usernickname: String,
    val recentTotal: Int,
    val maxMilege: Int,
    val userLikeCnt: Int,
    val orderStatusCounts: OrderStatusCounts
)

data class UserResponse(
    val success: Boolean,
    val code: String,
    val message: String,
    val result: UserResult
)

interface AuthApi {
    @POST("token/login") suspend fun loginWithKakao(@Body req: KakaoLoginRequest): LoginResponse
    @GET("user/me") suspend fun me(): UserResponse
}
