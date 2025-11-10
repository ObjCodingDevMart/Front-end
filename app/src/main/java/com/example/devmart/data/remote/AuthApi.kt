package com.example.devmart.data.remote

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

data class LoginRequest(val email: String, val password: String)
data class LoginResponse(val accessToken: String)
data class UserDto(val id: String, val email: String, val name: String?)

interface AuthApi {
    @POST("v1/auth/login") suspend fun login(@Body req: LoginRequest): LoginResponse
    @GET("v1/auth/me") suspend fun me(): UserDto
}
