package com.keparisss.pokedex.list

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

import com.bumptech.glide.Glide

import com.keparisss.pokedex.R
import com.keparisss.pokedex.models.PokemonModel
import com.keparisss.pokedex.models.ListPokemonViewModel

import kotlinx.android.synthetic.main.details_activity.*
import kotlinx.android.synthetic.main.details_activity.pokemonName

import javax.inject.Inject
import com.keparisss.pokedex.di.ListModule
import com.keparisss.pokedex.di.DaggerListComponent

class DetailsActivity: AppCompatActivity() {
    private var position: Int? = null
    private lateinit var pokemon: PokemonModel

    @Inject
    lateinit var pokemonViewModel: ListPokemonViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        DaggerListComponent
            .builder()
            .listModule(ListModule(this))
            .build()
            .inject(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.details_activity)

        getIncomingIntent()

        detailsPokemonRatingBar.setOnRatingBarChangeListener { _, rating, fromUser ->
            if (fromUser) {
                pokemon.rating = rating

                val newPokemons = pokemonViewModel.getAllPokemons().value!!
                newPokemons[position!!] = pokemon

                pokemonViewModel.pokemonModelLiveData.mutableLiveData.postValue(newPokemons)
            }
        }
    }

    private fun getIncomingIntent() {
        if (intent.hasExtra("position")) {
            position = intent.getIntExtra("position", 0)
            pokemon = pokemonViewModel.getAllPokemons().value!![position!!]

            setName(pokemon.name)
            setRating(pokemon.rating)
            setDescription(pokemon.getAbilities())
            setImage(pokemon.sprites.front_default)
        }
    }

    private fun setRating(rating: Float?) {
        detailsPokemonRatingBar.rating = rating!!
    }

    private fun setName(name: String?) {
        pokemonName.text = name
    }

    private fun setDescription(description: String?) {
        pokemonDescription.text = description
    }

    private fun setImage(spriteUrl: String?) {
        Glide
            .with(this)
            .load(spriteUrl)
            .into(pokemonSprite)
    }
}
