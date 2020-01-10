package com.keparisss.pokedex.di

import androidx.lifecycle.ViewModelProviders
import androidx.appcompat.app.AppCompatActivity

import com.keparisss.pokedex.models.ListPokemonViewModel
import com.keparisss.pokedex.list.PokemonViewModelFactory

import dagger.Module
import dagger.Provides

@Module
class ListModule(private val activity: AppCompatActivity) {
    // TODO: deal with livedata between activities
    private val factory = PokemonViewModelFactory(activity.application)

    @Provides
    fun providesPokemonListViewModel(): ListPokemonViewModel {
        return ViewModelProviders.of(activity, factory)[ListPokemonViewModel::class.java]
    }
}
