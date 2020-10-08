package com.eliong92.githubuser.di

import com.eliong92.githubuser.network.ApiService
import com.eliong92.githubuser.repository.UserRepository
import com.eliong92.githubuser.repository.UserRepositoryImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RepositoryModule {

    @Singleton
    @Provides
    fun provideUserRepository(apiService: ApiService): UserRepository {
        return UserRepositoryImpl(apiService)
    }
}