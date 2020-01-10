package com.keparisss.pokedex.list

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import javax.inject.Inject

import com.bumptech.glide.Glide
import com.keparisss.pokedex.models.PokemonModel
import com.keparisss.pokedex.R
import com.keparisss.pokedex.di.DaggerListComponent
import com.keparisss.pokedex.di.ListModule
import com.keparisss.pokedex.models.ListPokemonViewModel
import kotlinx.android.synthetic.main.details_activity.*


class DetailsActivity: AppCompatActivity() {
    lateinit var pokemon: PokemonModel

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

        ratingBar.setOnRatingBarChangeListener { _, rating, fromUser ->
            if (fromUser) {
                pokemon.rating = rating
            }
        }
    }

    private fun getIncomingIntent() {
        if (intent.hasExtra("position")) {
            val position = intent.getIntExtra("position", 0)
            pokemon = pokemonViewModel.getAllPokemons().value!![position]

            setName(pokemon.name)
            setDescription(pokemon.getAbilities())
            setImage(pokemon.sprites.front_default)
        }
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
