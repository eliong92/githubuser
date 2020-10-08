package com.eliong92.githubuser.model

import com.google.gson.annotations.SerializedName

data class UserResponse(
    @SerializedName("total_count")
    val totalCount: Int = 0,
    @SerializedName("items")
    val items: List<User> = emptyList()
) {
    data class User(
        @SerializedName("login")
        val login: String = "",
        @SerializedName("avatar_url")
        val avatar: String = ""
    )
}