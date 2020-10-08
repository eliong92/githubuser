package com.eliong92.githubuser.usecase

interface GetUserUseCase {
    suspend fun execute(
        query: String,
        page: Int
    ): List<UserViewObject>
}