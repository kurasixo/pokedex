package com.keparisss.pokedex.di

import com.keparisss.pokedex.login.LoginActivity
import com.keparisss.pokedex.signup.SignUpActivity
import com.keparisss.pokedex.login.AuthViewModelFactory

import dagger.Component

@Component(modules = [AuthModule::class])
interface AuthComponent {
    fun inject(target: LoginActivity)
    fun inject(target: SignUpActivity)
}

@Component(modules = [AuthViewModelFactoryModule::class])
interface AuthViewModelComponent {
    fun inject(target: AuthViewModelFactory)
}
