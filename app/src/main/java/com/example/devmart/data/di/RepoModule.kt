package com.example.devmart.data.di

import com.example.devmart.data.repository.AuthRepositoryImpl
import com.example.devmart.data.repository.ProductRepositoryImpl
import com.example.devmart.domain.repo.AuthRepository
import com.example.devmart.domain.repo.ProductRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module @InstallIn(SingletonComponent::class)
abstract class RepoModule {
    @Binds abstract fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository
    @Binds abstract fun bindProductRepository(impl: ProductRepositoryImpl): ProductRepository
}
