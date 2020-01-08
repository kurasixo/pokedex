package com.keparisss.pokedex.data

import android.content.SharedPreferences
import com.keparisss.pokedex.models.*

class LoginRepository(val dataSource: LoginDataSource) {
    var user: LoggedInUser? = null
        private set

    val isLoggedIn: Boolean
        get() = user != null

    init {
        user = null
    }

    fun login(username: String, password: String, preferences: SharedPreferences): Result<LoggedInUser> {
        val result = dataSource.login(username, password, preferences)

        if (result is Result.Success) {
            setLoggedInUser(result.data)
        }

        return result
    }

    fun signup(username: String, password: String, preferences: SharedPreferences): Result<LoggedInUser> {
        val result = dataSource.signup(username, password, preferences)

        if (result is Result.Success) {
            setLoggedInUser(result.data)
        }

        return result
    }

    private fun setLoggedInUser(loggedInUser: LoggedInUser) {
        this.user = loggedInUser
    }
}
