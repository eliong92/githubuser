package com.eliong92.githubuser.viewmodel

import com.eliong92.githubuser.usecase.GetUserUseCase
import dagger.Module
import dagger.Provides

@Module
class MainViewModelModule {

    @Provides
    fun provideMainViewModelProvider(useCase: GetUserUseCase): MainViewModelProvider {
        return MainViewModelProvider(useCase)
    }
}