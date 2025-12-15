package com.example.devmart.data.di

import com.example.devmart.BuildConfig
import com.example.devmart.data.local.TokenStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module @InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides @Singleton
    fun authInterceptor(tokenStore: TokenStore): Interceptor = Interceptor { chain ->
        val token = runBlocking { tokenStore.snapshot() }
        val req = if (!token.isNullOrBlank())
            chain.request().newBuilder().addHeader("Authorization", "Bearer $token").build()
        else chain.request()
        
        val response = chain.proceed(req)
        
        // 401 Unauthorized 응답 처리 (토큰 만료 등)
        if (response.code == 401||response.code==0||response.code==403) {
            // 토큰 삭제
            runBlocking { tokenStore.clear() }
        }
        
        response
    }

    @Provides @Singleton
    fun okHttp(authInterceptor: Interceptor): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY })
            .build()

    @Provides @Singleton
    @Named("backend")
    fun retrofit(client: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
}
