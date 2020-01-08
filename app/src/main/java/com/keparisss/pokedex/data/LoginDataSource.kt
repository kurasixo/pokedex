package com.keparisss.pokedex.data

import android.content.SharedPreferences
import com.keparisss.pokedex.data.model.LoggedInUser
import java.io.IOException

class LoginDataSource {
    fun login(username: String, password: String, preferences: SharedPreferences): Result<LoggedInUser> {
        try {
            val passwordFromPreferences = preferences.getString("password", null)
            val usernameFromPreferences = preferences.getString("username", null)

            if (passwordFromPreferences != null && usernameFromPreferences != null) {
                if (passwordFromPreferences == password && usernameFromPreferences == username) {
                    val user = LoggedInUser(
                        java.util.UUID.randomUUID().toString(),
                        username
                    )

                    return Result.Success(user)
                }
            }
            return Result.Error(IOException("Invalid credentials"))
        } catch (e: Throwable) {
            return Result.Error(IOException("Error logging in", e))
        }
    }

    fun signup(username: String, password: String, preferences: SharedPreferences): Result<LoggedInUser> {
        try {
            val editor = preferences.edit()

            editor.putString("username", username)
            editor.putString("password", password)

            editor.apply()

            val user = LoggedInUser(
                java.util.UUID.randomUUID().toString(),
                username
            )

            return Result.Success(user)
        } catch (e: Throwable) {
            return Result.Error(IOException("Error signing up"))
        }
    }

    fun logout() {
        // TODO: revoke authentication
    }
}

