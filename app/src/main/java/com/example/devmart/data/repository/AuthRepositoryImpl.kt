package com.example.devmart.data.repository

import com.example.devmart.data.local.TokenStore
import com.example.devmart.data.remote.AuthApi
import com.example.devmart.domain.repo.AuthRepository
import com.example.devmart.domain.repo.Result
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    @Named("backend") retrofit: Retrofit,
    private val tokenStore: TokenStore
) : AuthRepository {
    private val api = retrofit.create(AuthApi::class.java)

    override suspend fun me(): Result<com.example.devmart.domain.model.User> =
        try {
            val response = api.me()
            if (response.success && response.result != null) {
                val result = response.result
                // UserResponse를 User 도메인 모델로 변환
                // 주의: User 도메인 모델은 id, email, name만 가지고 있음
                Result.Ok(
                    com.example.devmart.domain.model.User(
                        id = "", // API 응답에 id가 없음
                        email = "", // API 응답에 email이 없음
                        name = result.usernickname
                    )
                )
            } else {
                Result.Err(response.message ?: "사용자 정보 조회 실패")
            }
        } catch (t: Throwable) {
            Result.Err(t.message ?: "me failed", t)
        }
}
