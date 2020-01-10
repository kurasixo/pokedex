package com.keparisss.pokedex.login

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.util.Patterns

import com.keparisss.pokedex.data.AuthRepository
import com.keparisss.pokedex.models.Result

import com.keparisss.pokedex.models.*

import com.keparisss.pokedex.R
import javax.inject.Inject

class LoginViewModel @Inject constructor(private val authRepository: AuthRepository): ViewModel() {
    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult

    fun login(username: String, password: String, preferences: SharedPreferences) {
        val result = authRepository.login(username, password, preferences)

        if (result is Result.Success) {
            _loginResult.postValue(LoginResult(success = LoggedInUserView(displayName = result.data.displayName)))
        } else {
            _loginResult.postValue(LoginResult(error = R.string.login_failed))
        }
    }

    fun loginDataChanged(username: String, password: String) {
        if (!isUserNameValid(username)) {
            _loginForm.postValue(LoginFormState(usernameError = R.string.invalid_username))
        } else if (!isPasswordValid(password)) {
            _loginForm.postValue(LoginFormState(passwordError = R.string.invalid_password))
        } else {
            _loginForm.postValue(LoginFormState(isDataValid = true))
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
