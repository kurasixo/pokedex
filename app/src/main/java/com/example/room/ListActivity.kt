package com.example.room

import okhttp3.*
import com.google.gson.GsonBuilder

import kotlinx.coroutines.*

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.GridLayoutManager
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.list_activity.*
import java.io.IOException

class ListActivity : AppCompatActivity() {
    private val POKEMON_API_URL: String = "https://pokeapi.co/api/v2/pokemon"
    private var pokemons: ArrayList<PokemonModel> = ArrayList()

    private val gson = GsonBuilder().create()
    private val client = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.list_activity)

        rv_pokemon_list.layoutManager = LinearLayoutManager(this)
        rv_pokemon_list.layoutManager = GridLayoutManager(this, 1)

        GlobalScope.launch {
            getPokemons()
        }

        rv_pokemon_list.adapter = PokemonAdapter(pokemons, this)
    }

    override fun onDestroy() {
        super.onDestroy()

        val preferences = getPreferences(Context.MODE_PRIVATE)
        val editor = preferences.edit()

        editor.putString("pokemons", gson.toJson(pokemons))
        editor.apply()
    }

    private fun fetchPokemons() {
        val request = Request.Builder()
            .url(POKEMON_API_URL)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) {
                        throw IOException("Unexpected code $response")
                    }

                    val resp = gson.fromJson(
                        response.body!!.charStream(),
                        PokeAPIResponse::class.java)

                    resp.results.forEach {
                        fetchPokemon(it.url)
                    }
                }
            }
        })
    }

    private fun fetchPokemon(url: String) {
        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) {
                        throw IOException("Unexpected code $response")
                    }

                    val resp = gson.fromJson(
                        response.body!!.charStream(),
                        PokemonModel::class.java)

                    pokemons.add(resp)
                }
            }
        })
    }

    private fun getPokemons() {
        val preferences = getPreferences(Context.MODE_PRIVATE)
        val preloadedPokemons = preferences.getString("pokemons", null)

        if (preloadedPokemons != null) {
            pokemons = gson.fromJson(preloadedPokemons, object : TypeToken<ArrayList<PokemonModel>>() {}.type)
        } else {
            fetchPokemons()
        }
    }
}
