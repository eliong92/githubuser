package com.eliong92.githubuser.repository

import com.eliong92.githubuser.model.UserResponse
import com.eliong92.githubuser.network.ApiService

class UserRepositoryImpl(
    private val apiService: ApiService
) : UserRepository {
    override suspend fun getUsers(
        query: String,
        perPage: Int,
        page: Int
    ): UserResponse {
        return apiService.getUsers(
            query = query,
            perPage = perPage,
            page = page
        )
    }
}