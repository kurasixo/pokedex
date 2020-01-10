package com.keparisss.pokedex.list

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.keparisss.pokedex.models.ListPokemonViewModel
import javax.inject.Singleton

@Singleton
class PokemonViewModelFactory(val app: Application): ViewModelProvider.AndroidViewModelFactory(app) {
    private val listPokemonViewModel: ListPokemonViewModel = ListPokemonViewModel(app)

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ListPokemonViewModel::class.java)) {
            return listPokemonViewModel as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}