package com.keparisss.pokedex.data

import java.io.IOException

import android.content.SharedPreferences

import com.keparisss.pokedex.models.*

class AuthDataSource {
    fun login(
        username: String,
        password: String,
        preferences: SharedPreferences
    ): Result<LoggedInUser> {
        try {
            val passwordFromPreferences = preferences
                .getString("password", null)

            val usernameFromPreferences = preferences
                .getString("username", null)

            if (
                passwordFromPreferences != null &&
                usernameFromPreferences != null
            ) {
                if (
                    passwordFromPreferences == password &&
                    usernameFromPreferences == username
                ) {
                    val user = LoggedInUser(
                        java.util.UUID.randomUUID().toString(),
                        username,
                        System.currentTimeMillis()
                    )

                    val editor = preferences.edit()
                    editor.putLong("lastLoggedIn", user.lastLoggedIn)
                    editor.apply()

                    return Result.Success(user)
                }
            }

            return Result.Error(IOException("Invalid credentials"))
        } catch (e: Throwable) {
            return Result.Error(IOException("Error logging in", e))
        }
    }

    fun signup(
        username: String,
        password: String,
        preferences: SharedPreferences
    ): Result<LoggedInUser> {
        try {
            val editor = preferences.edit()

            editor.putString("username", username)
            editor.putString("password", password)

            editor.apply()

            return login(username, password, preferences)
        } catch (e: Throwable) {
            return Result.Error(IOException("Error signing up"))
        }
    }
}

