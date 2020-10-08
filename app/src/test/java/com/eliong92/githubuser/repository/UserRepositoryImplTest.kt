package com.eliong92.githubuser.repository

import com.eliong92.githubuser.CoroutineTestRule
import com.eliong92.githubuser.model.UserResponse
import com.eliong92.githubuser.network.ApiService
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
class UserRepositoryImplTest {

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    @Mock
    lateinit var apiService: ApiService

    private lateinit var repository: UserRepositoryImpl

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        repository = UserRepositoryImpl(apiService)
    }

    @Test
    fun getUsers_shouldReturnUserResponse() = coroutineTestRule.testDispatcher.runBlockingTest {
        val query = "query"
        val perPage = 10
        val page = 1
        val expectedResult = UserResponse(
            totalCount = 1,
            items = listOf(
                UserResponse.User(
                    login = "Edbert",
                    avatar = "avatar_url"
                )
            )
        )

        whenever(apiService.getUsers(
            query = query,
            perPage = perPage,
            page = page
        )).thenReturn(expectedResult)

        val result = repository.getUsers(
            query = query,
            perPage = perPage,
            page = page
        )

        assertEquals(expectedResult, result)
    }
}