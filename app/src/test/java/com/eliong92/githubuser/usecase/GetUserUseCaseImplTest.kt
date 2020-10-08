package com.eliong92.githubuser.usecase

import com.eliong92.githubuser.CoroutineTestRule
import com.eliong92.githubuser.model.UserResponse
import com.eliong92.githubuser.repository.UserRepository
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
class GetUserUseCaseImplTest {

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    @Mock
    lateinit var userRepo: UserRepository

    private lateinit var useCase: GetUserUseCaseImpl

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        useCase = GetUserUseCaseImpl(userRepo)
    }

    @Test
    fun execute_shouldMapUserResponseToUserViewObject() = coroutineTestRule.testDispatcher.runBlockingTest {
        val query = "query"
        val page = 1
        val expectedResult = listOf(
            UserViewObject(
                name = "name1",
                avatar = "avatar1"
            ),
            UserViewObject(
                name = "name2",
                avatar = "avatar2"
            )
        )

        whenever(userRepo.getUsers(
            query = query,
            perPage = 10,
            page = page
        )).thenReturn(
            UserResponse(
                items = listOf(
                    UserResponse.User(
                        login = "name1",
                        avatar = "avatar1"
                    ),
                    UserResponse.User(
                        login = "name2",
                        avatar = "avatar2"
                    )
                )
            )
        )

        val result = useCase.execute(
            query = query,
            page = page
        )

        assertEquals(expectedResult, result)
    }
}