package com.keparisss.pokedex.di

import com.keparisss.pokedex.data.AuthRepository
import com.keparisss.pokedex.data.AuthDataSource
import com.keparisss.pokedex.auth.common.AuthViewModelFactory

import dagger.Module
import dagger.Provides

@Module
class AuthModule {
    // Add gson and okhttp

    @Provides
    fun providesAuthViewModelFactory(): AuthViewModelFactory = AuthViewModelFactory()
}

@Module
class AuthViewModelFactoryModule {
    @Provides
    fun providesAuthRepository(): AuthRepository = AuthRepository(AuthDataSource())
}
