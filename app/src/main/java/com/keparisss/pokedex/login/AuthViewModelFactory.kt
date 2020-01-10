package com.keparisss.pokedex.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.keparisss.pokedex.data.AuthRepository
import com.keparisss.pokedex.signup.SignUpViewModel

import javax.inject.Inject
import com.keparisss.pokedex.di.DaggerAuthViewModelComponent

class AuthViewModelFactory @Inject constructor(): ViewModelProvider.Factory {
    @Inject
    lateinit var authRepository: AuthRepository

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        DaggerAuthViewModelComponent.create().inject(this)

        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(authRepository) as T
        }

        if (modelClass.isAssignableFrom(SignUpViewModel::class.java)) {
            return SignUpViewModel(authRepository) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}