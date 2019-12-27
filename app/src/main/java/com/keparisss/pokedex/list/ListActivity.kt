package com.keparisss.pokedex.list

import android.content.Context
import okhttp3.*
import com.google.gson.GsonBuilder

import kotlinx.coroutines.*

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.keparisss.pokedex.models.PokeAPIResponse
import com.keparisss.pokedex.models.PokemonModel
import com.keparisss.pokedex.R
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.list_activity.*
import java.io.IOException

class ListActivity : AppCompatActivity() {
    private var isLoading: Boolean = false
    private val pokemonApiUrl: String = "https://pokeapi.co/api/v2/pokemon"

    private val client = OkHttpClient()
    private val gson = GsonBuilder().create()

    private var next: String? = null
    private var current: String? = null
    private var pokemons: ArrayList<PokemonModel> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.list_activity)

        restoreData()

        pokemonList.layoutManager = LinearLayoutManager(this)
        pokemonList.adapter = ListActivityAdapter(pokemons, this)

        hideLoader()

        if (pokemons.size == 0) {
            GlobalScope.launch { fetchPokemons(pokemonApiUrl) }
        }

        pokemonList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if (isLoading) {
                    return
                }

                if (
                    next != null &&
                    next != current &&
                    !recyclerView.canScrollVertically(1)
                ) {
                    GlobalScope.launch { fetchPokemons(next) }
                }
            }
        })
    }

    override fun onPause() {
        super.onPause()

        val editor = getPreferences(Context.MODE_PRIVATE).edit()

        editor.putString("next", next)
        editor.putString("current", current)
        editor.putString("pokemons", gson.toJson(pokemons))

        editor.apply()
    }

    private fun restoreData() {
        val preferences = getPreferences(Context.MODE_PRIVATE)

        val preloadedNext = preferences.getString("next", null)
        val preloadedCurrent = preferences.getString("current", null)
        val preloadedPokemons = preferences.getString("pokemons", null)

        if (preloadedNext != null) {
            next = preloadedNext
        }

        if (preloadedCurrent != null) {
            current = preloadedCurrent
        }

        if (preloadedPokemons != null) {
            pokemons = gson.fromJson(preloadedPokemons, object : TypeToken<ArrayList<PokemonModel>>() {}.type)
        }
    }

    private fun fetchPokemons(url: String?) {
        val request = Request.Builder()
            .url(url!!)
            .build()

        showLoader()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                hideLoader()
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

                    hideLoader()
                    current = next
                    next = resp.next!!

                    resp.results.forEach { fetchPokemon(it.url) }
                }
            }
        })
    }

    private fun fetchPokemon(url: String) {
        showLoader()
        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                hideLoader()
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

                    hideLoader()
                    pokemons.add(resp)

                    pokemonList.post {
                        pokemonList.adapter!!.notifyDataSetChanged()
                    }
                }
            }
        })
    }

    private fun showLoader() {
        isLoading = true
        pokemonListProgressBar.post {
            pokemonListProgressBar.visibility = View.VISIBLE
        }
    }

    private fun hideLoader() {
        isLoading = false
        pokemonListProgressBar.post {
            pokemonListProgressBar.visibility = View.GONE
        }
    }
}
