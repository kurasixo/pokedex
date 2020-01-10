package com.keparisss.pokedex.list

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.keparisss.pokedex.models.ListPokemonViewModel
import javax.inject.Singleton

@Singleton
class PokemonViewModelFactory(val app: Application): ViewModelProvider.AndroidViewModelFactory(app) {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ListPokemonViewModel::class.java)) {
            val key = "ListPokemonViewModel"
            val listPokemonViewModel = ListPokemonViewModel(app)

            if (hashMapViewModel.containsKey(key)) {
                return getViewModel(key) as T
            }

            addViewModel(key, listPokemonViewModel)
            return listPokemonViewModel as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }

    companion object {
        val hashMapViewModel = HashMap<String, AndroidViewModel>()

        fun addViewModel(key: String, viewModel: AndroidViewModel){
            hashMapViewModel.put(key, viewModel)
        }

        fun getViewModel(key: String): AndroidViewModel? {
            return hashMapViewModel[key]
        }
    }
}