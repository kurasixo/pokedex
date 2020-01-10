package com.keparisss.pokedex.data

import android.content.SharedPreferences
import com.keparisss.pokedex.models.*
import javax.inject.Inject

class AuthRepository @Inject constructor(private val dataSource: AuthDataSource) {
    var user: LoggedInUser? = null
        private set

    init {
        user = null
    }

    fun login(username: String, password: String, preferences: SharedPreferences): Result<LoggedInUser> {
        val result = dataSource.login(username, password, preferences)

        if (result is Result.Success) {
            setLoggedInUser(result.data, preferences)
        }

        return result
    }

    fun signup(username: String, password: String, preferences: SharedPreferences): Result<LoggedInUser> {
        val result = dataSource.signup(username, password, preferences)

        if (result is Result.Success) {
            setLoggedInUser(result.data, preferences)
        }

        return result
    }

    private fun setLoggedInUser(loggedInUser: LoggedInUser, preferences: SharedPreferences) {
        this.user = loggedInUser
    }
}
