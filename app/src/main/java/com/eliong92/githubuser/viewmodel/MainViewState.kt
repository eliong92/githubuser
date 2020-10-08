package com.eliong92.githubuser.viewmodel

import com.eliong92.githubuser.usecase.UserViewObject

sealed class MainViewState {
    object OnError: MainViewState()
    object OnUserNotFound: MainViewState()
    data class OnSuccess(val users: List<UserViewObject>): MainViewState()
}