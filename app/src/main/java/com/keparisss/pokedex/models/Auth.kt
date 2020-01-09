package com.keparisss.pokedex.models

data class LoginResult(
    val success: LoggedInUserView? = null,
    val error: Int? = null
)

data class LoginFormState(
    val usernameError: Int? = null,
    val passwordError: Int? = null,
    val isDataValid: Boolean = false
)

data class LoggedInUserView(
    val displayName: String
)

data class LoggedInUser(
    val userId: String,
    val displayName: String,
    val lastLoggedIn: Long
)

sealed class Result<out T : Any> {

    data class Success<out T : Any>(val data: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[exception=$exception]"
        }
    }
}


