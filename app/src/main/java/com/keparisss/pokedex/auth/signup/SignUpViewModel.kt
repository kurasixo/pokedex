package com.keparisss.pokedex.auth.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

import android.util.Patterns
import android.content.SharedPreferences

import com.keparisss.pokedex.data.AuthRepository

import com.keparisss.pokedex.R
import com.keparisss.pokedex.models.*

import javax.inject.Inject

class SignUpViewModel @Inject constructor(private val authRepository: AuthRepository): ViewModel() {
    private val _signupForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _signupForm

    private val _signupResult = MutableLiveData<LoginResult>()
    val signupResult: LiveData<LoginResult> = _signupResult

    fun signup(username: String, password: String, preferences: SharedPreferences) {
        val result = authRepository.signup(username, password, preferences)

        if (result is Result.Success) {
            _signupResult.value =
                LoginResult(success = LoggedInUserView(displayName = result.data.displayName))
        } else {
            _signupResult.value = LoginResult(error = R.string.login_failed)
        }
    }

    fun loginDataChanged(username: String, password: String) {
        if (!isUserNameValid(username)) {
            _signupForm.value = LoginFormState(usernameError = R.string.invalid_username)
        } else if (!isPasswordValid(password)) {
            _signupForm.value = LoginFormState(passwordError = R.string.invalid_password)
        } else {
            _signupForm.value = LoginFormState(isDataValid = true)
        }
    }

    private fun isUserNameValid(username: String): Boolean {
        return if (username.contains('@')) {
            Patterns.EMAIL_ADDRESS.matcher(username).matches()
        } else {
            username.isNotBlank()
        }
    }

    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }
}
