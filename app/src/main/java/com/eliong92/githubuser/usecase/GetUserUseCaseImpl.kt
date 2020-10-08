package com.eliong92.githubuser.usecase

import com.eliong92.githubuser.repository.UserRepository

class GetUserUseCaseImpl(
    private val userRepository: UserRepository
) : GetUserUseCase {
    override suspend fun execute(
        query: String,
        page: Int
    ): List<UserViewObject> {
        return userRepository.getUsers(
            query = query,
            perPage = 10,
            page = page
        ).items.map {
            UserViewObject(
                name = it.login,
                avatar = it.avatar
            )
        }
    }
}