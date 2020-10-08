package com.eliong92.githubuser.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.eliong92.githubuser.usecase.GetUserUseCase
import javax.inject.Inject

class MainViewModelProvider @Inject constructor(
    private val useCase: GetUserUseCase
): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MainViewModel(useCase) as T
    }
}