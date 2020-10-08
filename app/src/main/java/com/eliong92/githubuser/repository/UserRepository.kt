package com.eliong92.githubuser.repository

import com.eliong92.githubuser.model.UserResponse

interface UserRepository {
    suspend fun getUsers(
        query: String,
        perPage: Int,
        page: Int,
    ): UserResponse
}