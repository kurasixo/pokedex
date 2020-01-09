package com.keparisss.pokedex.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.keparisss.pokedex.data.AuthDataSource
import com.keparisss.pokedex.data.AuthRepository
import com.keparisss.pokedex.signup.SignUpViewModel


class AuthViewModelFactory : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(
                authRepository = AuthRepository(
                    dataSource = AuthDataSource()
                )
            ) as T
        }

        if (modelClass.isAssignableFrom(SignUpViewModel::class.java)) {
            return SignUpViewModel(
                authRepository = AuthRepository(
                    dataSource = AuthDataSource()
                )
            ) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}