package com.keparisss.pokedex.details

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import com.bumptech.glide.Glide
import com.keparisss.pokedex.models.PokemonModel
import com.keparisss.pokedex.R
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.details_activity.*


class DetailsActivity : AppCompatActivity() {
    private val gson = GsonBuilder().create()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.details_activity)

        getIncomingIntent()

        ratingBar.setOnRatingBarChangeListener { _, rating, fromUser ->
            if (fromUser) {
//                items[position].rating = rating
//                notifyItemChanged(position)
            }
        }
    }

    private fun getIncomingIntent() {
        if (intent.hasExtra("pokemon")) {
            val deserializedPokemon = intent.getStringExtra("pokemon")
            val pokemon = gson.fromJson(deserializedPokemon, PokemonModel::class.java)

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
