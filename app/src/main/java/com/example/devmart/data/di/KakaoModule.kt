package com.example.devmart.data.di

import com.example.devmart.data.remote.KakaoApi
import com.example.devmart.data.repository.KakaoAddressRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object KakaoModule {

    @Provides
    @Singleton
    @Named("kakao")
    fun provideKakaoRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://dapi.kakao.com")
            .addConverterFactory(GsonConverterFactory.create())
            .client(OkHttpClient.Builder().build())
            .build()
    }

    @Provides
    @Singleton
    fun provideKakaoApi(@Named("kakao") retrofit: Retrofit): KakaoApi {
        return retrofit.create(KakaoApi::class.java)
    }

    @Provides
    @Singleton
    fun provideKakaoAddressRepository(api: KakaoApi): KakaoAddressRepository {
        return KakaoAddressRepository(api)
    }
}
