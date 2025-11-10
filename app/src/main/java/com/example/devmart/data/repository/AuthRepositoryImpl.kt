package com.example.devmart.data.repository

import com.example.devmart.data.local.TokenStore
import com.example.devmart.data.mapper.toDomain
import com.example.devmart.data.remote.AuthApi
import com.example.devmart.domain.repo.AuthRepository
import com.example.devmart.domain.repo.Result
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    retrofit: Retrofit,
    private val tokenStore: TokenStore
) : AuthRepository {
    private val api = retrofit.create(AuthApi::class.java)

    override suspend fun login(email: String, password: String): Result<Unit> =
        try {
            val token = api.login(com.example.devmart.data.remote.LoginRequest(email, password)).accessToken
            tokenStore.save(token)
            Result.Ok(Unit)
        } catch (t: Throwable) { Result.Err(t.message ?: "login failed", t) }

    override suspend fun me(): Result<com.example.devmart.domain.model.User> =
        try { Result.Ok(api.me().toDomain()) }
        catch (t: Throwable) { Result.Err(t.message ?: "me failed", t) }
}
