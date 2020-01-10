package com.keparisss.pokedex.di

import androidx.lifecycle.ViewModelProviders
import com.keparisss.pokedex.list.ListActivity
import com.keparisss.pokedex.models.ListPokemonViewModel

import dagger.Module
import dagger.Provides

@Module
class ListModule(private val listActivity: ListActivity) {
    @Provides
    fun providesPokemonListViewModel(): ListPokemonViewModel {
        return ViewModelProviders.of(listActivity)[ListPokemonViewModel::class.java]
    }
}
