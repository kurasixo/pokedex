package com.keparisss.pokedex.details

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView

import com.bumptech.glide.Glide
import com.keparisss.pokedex.models.PokemonModel
import com.keparisss.pokedex.R
import com.google.gson.GsonBuilder


class DetailsActivity : AppCompatActivity() {
    private val gson = GsonBuilder().create()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.details_activity)

        getIncomingIntent()
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
        val descriptionTextView = findViewById<TextView>(R.id.pokemonName)
        descriptionTextView.text = name
    }

    private fun setDescription(description: String?) {
        val descriptionTextView = findViewById<TextView>(R.id.pokemonDescription)
        descriptionTextView.text = description
    }

    private fun setImage(spriteUrl: String?) {
        val imageComponent = findViewById<ImageView>(R.id.pokemonSprite)

        Glide
            .with(this)
            .load(spriteUrl)
            .into(imageComponent)
    }
}
