package com.eliong92.githubuser.viewmodel

import android.view.View
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eliong92.githubuser.usecase.GetUserUseCase
import com.eliong92.githubuser.usecase.UserViewObject
import kotlinx.coroutines.launch
import retrofit2.HttpException

class MainViewModel(
    private val useCase: GetUserUseCase
) : ViewModel() {
    val state = MutableLiveData<MainViewState>()

    val loadingVisibility = MutableLiveData(View.GONE)

    @VisibleForTesting var currentPage = 1
    @VisibleForTesting var currentQuery = ""
    @VisibleForTesting val userList = mutableListOf<UserViewObject>()

    fun onSearch(query: String) {
        currentPage = 1
        currentQuery = query

        viewModelScope.launch {
            loadingVisibility.value = View.VISIBLE

            try {
                val users = useCase.execute(
                    query = currentQuery,
                    page = currentPage
                )

                userList.clear()
                userList.addAll(users)

                state.value = if(users.isEmpty()) {
                    MainViewState.OnUserNotFound
                } else MainViewState.OnSuccess(userList)

                loadingVisibility.value = View.GONE
            } catch (e: HttpException) {
                showErrorMessage()
            }
        }
    }

    fun onLoadMore() {
        viewModelScope.launch {
            loadingVisibility.value = View.VISIBLE
            try {
                val users = useCase.execute(
                    query = currentQuery,
                    page = currentPage.inc()
                )
                userList.addAll(users)
                state.value = MainViewState.OnSuccess(userList)
                loadingVisibility.value = View.GONE
                currentPage = currentPage.inc()
            } catch (e: HttpException) {
                showErrorMessage()
            }
        }
    }

    private fun showErrorMessage() {
        state.value = MainViewState.OnError
        loadingVisibility.value = View.GONE
    }
}