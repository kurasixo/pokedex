package com.keparisss.pokedex.data

import android.content.SharedPreferences
import com.keparisss.pokedex.data.model.LoggedInUser

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */

class LoginRepository(val dataSource: LoginDataSource) {

    // in-memory cache of the loggedInUser object
    var user: LoggedInUser? = null
        private set

    val isLoggedIn: Boolean
        get() = user != null

    init {
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
        user = null
    }

    fun logout() {
        user = null
        dataSource.logout()
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
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
    }
}
