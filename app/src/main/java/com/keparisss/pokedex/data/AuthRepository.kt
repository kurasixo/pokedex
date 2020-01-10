package com.keparisss.pokedex.data

import android.content.SharedPreferences

import com.keparisss.pokedex.models.*

import javax.inject.Inject

class AuthRepository @Inject constructor(private val authDataSource: AuthDataSource) {
    private var user: LoggedInUser? = null

    init {
        user = null
    }

    fun login(username: String, password: String, preferences: SharedPreferences): Result<LoggedInUser> {
        val result = authDataSource.login(username, password, preferences)

        if (result is Result.Success) {
            setLoggedInUser(result.data)
        }

        return result
    }

    fun signup(username: String, password: String, preferences: SharedPreferences): Result<LoggedInUser> {
        val result = authDataSource.signup(username, password, preferences)

        if (result is Result.Success) {
            setLoggedInUser(result.data)
        }

        return result
    }

    private fun setLoggedInUser(loggedInUser: LoggedInUser) {
        user = loggedInUser
    }
}
