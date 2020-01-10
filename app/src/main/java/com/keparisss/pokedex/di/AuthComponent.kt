package com.keparisss.pokedex.di

import com.keparisss.pokedex.auth.login.LoginActivity
import com.keparisss.pokedex.auth.signup.SignUpActivity
import com.keparisss.pokedex.auth.common.AuthViewModelFactory

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
