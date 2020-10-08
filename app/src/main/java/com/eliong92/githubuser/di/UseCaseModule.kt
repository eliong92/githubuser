package com.eliong92.githubuser.di

import com.eliong92.githubuser.repository.UserRepository
import com.eliong92.githubuser.usecase.GetUserUseCase
import com.eliong92.githubuser.usecase.GetUserUseCaseImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class UseCaseModule {

    @Singleton
    @Provides
    fun provideGetUserUseCase(repo: UserRepository): GetUserUseCase {
        return GetUserUseCaseImpl(repo)
    }
}