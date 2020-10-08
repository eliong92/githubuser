package com.eliong92.githubuser.viewmodel

import android.view.View
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.eliong92.githubuser.CoroutineTestRule
import com.eliong92.githubuser.usecase.GetUserUseCase
import com.eliong92.githubuser.usecase.UserViewObject
import com.nhaarman.mockitokotlin2.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import retrofit2.HttpException
import retrofit2.Response

@ExperimentalCoroutinesApi
class MainViewModelTest {

    @get:Rule
    val rule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    private lateinit var viewModel: MainViewModel

    @Mock
    lateinit var viewStateObserver: Observer<MainViewState>

    @Mock
    lateinit var loadingVisibilityObserver: Observer<Int>

    @Mock
    lateinit var useCase: GetUserUseCase

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        viewModel = MainViewModel(useCase)
        viewModel.state.observeForever(viewStateObserver)
        viewModel.loadingVisibility.observeForever(loadingVisibilityObserver)
    }

    @Test
    fun onSearch_whenSuccess_shouldShowUsers() = coroutineTestRule.testDispatcher.runBlockingTest {
        val expectedQuery = "query"
        val users = listOf(
            UserViewObject(
                name = "edbert",
                avatar = "avatar"
            )
        )

        val expectedQueryPage = 1
        whenever(useCase.execute(
            query = expectedQuery,
            page = expectedQueryPage
        )).thenReturn(users)

        val initialPage = 10
        val initialCurrentQuery = "abc"
        val initialUser = UserViewObject("a", "b")
        viewModel.currentPage = initialPage
        viewModel.currentQuery = initialCurrentQuery
        viewModel.userList.add(initialUser)
        viewModel.onSearch(expectedQuery)

        val inOrder = inOrder(viewStateObserver, loadingVisibilityObserver)
        inOrder.verify(loadingVisibilityObserver).onChanged(View.VISIBLE)
        inOrder.verify(viewStateObserver).onChanged(MainViewState.OnSuccess(users))
        inOrder.verify(loadingVisibilityObserver).onChanged(View.GONE)

        assertEquals(expectedQueryPage, viewModel.currentPage)
        assertEquals(expectedQuery, viewModel.currentQuery)
        assertEquals(users, viewModel.userList)
    }

    @Test
    fun onSearch_whenError_ShouldShowErrorMessage() = coroutineTestRule.testDispatcher.runBlockingTest {
        val query = "query"

        whenever(useCase.execute(
            query = query,
            page = 1
        )).thenThrow(HttpException(Response.error<List<UserViewObject>>(500, "Internal Error".toResponseBody())))

        viewModel.onSearch(query)

        val inOrder = inOrder(viewStateObserver, loadingVisibilityObserver)
        inOrder.verify(loadingVisibilityObserver).onChanged(View.VISIBLE)
        inOrder.verify(viewStateObserver).onChanged(MainViewState.OnError)
        inOrder.verify(loadingVisibilityObserver).onChanged(View.GONE)
    }

    @Test
    fun onSearch_whenResultEmpty_shouldShowEmptyMessage() = coroutineTestRule.testDispatcher.runBlockingTest {
        whenever(useCase.execute(
            query = any(),
            page = any()
        )).thenReturn(emptyList())

        viewModel.onSearch("query")

        verify(viewStateObserver).onChanged(MainViewState.OnUserNotFound)
    }

    @Test
    fun onLoadMore_whenSuccess_shouldAppendUserList() = coroutineTestRule.testDispatcher.runBlockingTest {
        val initialUser = UserViewObject("a", "b")
        val initialQuery = "ggwp"
        val initialPage = 1
        val newUser = UserViewObject("newUser", "newAvatar")
        val expectedCurrentPage = 2
        val expectedUserList = listOf(
            initialUser, newUser
        )
        viewModel.userList.add(initialUser)
        viewModel.currentQuery = initialQuery
        viewModel.currentPage = initialPage

        whenever(useCase.execute(
            query = initialQuery,
            page = expectedCurrentPage
        )).thenReturn(listOf(newUser))

        viewModel.onLoadMore()

        val inOrder = inOrder(viewStateObserver, loadingVisibilityObserver)
        inOrder.verify(loadingVisibilityObserver).onChanged(View.VISIBLE)
        inOrder.verify(viewStateObserver).onChanged(MainViewState.OnSuccess(expectedUserList))
        inOrder.verify(loadingVisibilityObserver).onChanged(View.GONE)

        assertEquals(expectedCurrentPage, viewModel.currentPage)
        assertEquals(expectedUserList, viewModel.userList)
    }

    @Test
    fun onLoadMore_whenError_shouldShowErrorMessage() = coroutineTestRule.testDispatcher.runBlockingTest {
        val initialUser = UserViewObject("a", "b")
        val currentPage = 2
        viewModel.userList.add(initialUser)
        viewModel.currentQuery = "query"
        viewModel.currentPage = currentPage

        whenever(useCase.execute(
            query = any(),
            page = eq(3)
        )).thenThrow(HttpException(Response.error<List<UserViewObject>>(500, "Internal Error".toResponseBody())))

        viewModel.onLoadMore()

        val inOrder = inOrder(viewStateObserver, loadingVisibilityObserver)
        inOrder.verify(loadingVisibilityObserver).onChanged(View.VISIBLE)
        inOrder.verify(viewStateObserver).onChanged(MainViewState.OnError)
        inOrder.verify(loadingVisibilityObserver).onChanged(View.GONE)

        assertEquals(currentPage, viewModel.currentPage)
    }
}