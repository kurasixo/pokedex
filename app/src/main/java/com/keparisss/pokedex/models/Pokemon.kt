package com.keparisss.pokedex.models

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.LiveData
import com.google.gson.GsonBuilder

import kotlinx.coroutines.*

import okhttp3.*
import java.io.IOException
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import com.google.gson.reflect.TypeToken

// Utilities
interface ResourceSummary {
    val id: Int
    val category: String
}

data class NamedApiResource(
    val name: String,
    override val category: String,
    override val id: Int
) : ResourceSummary

// Pokemon Model
data class PokemonModel(
    val id: Int,
    val name: String,
    val baseExperience: Int,
    val height: Int,
    val weight: Int,
    val species: NamedApiResource,
    val abilities: List<PokemonAbility>,
    val forms: List<NamedApiResource>,
    val heldItems: List<PokemonHeldItem>,
    val moves: List<PokemonMove>,
    val stats: List<PokemonStat>,
    val types: List<PokemonType>,
    val sprites: PokemonSprites,
    var rating: Float
) {
    fun getAbilities(): String {
        return abilities.joinToString(", ", "abilities: ") {
            it.ability.name
        }
    }
}

data class PokemonViewModel(
    val name: String,
    val url: String
)

data class PokemonSprites(
    val front_default: String?
)

data class PokemonAbility(
    val isHidden: Boolean,
    val slot: Int,
    val ability: NamedApiResource
)

data class PokemonHeldItem(
    val item: NamedApiResource,
    val versionDetails: List<PokemonHeldItemVersion>
)

data class PokemonHeldItemVersion(
    val version: NamedApiResource,
    val rarity: Int
)

data class PokemonMove(
    val move: NamedApiResource,
    val versionGroupDetails: List<PokemonMoveVersion>
)

data class PokemonMoveVersion(
    val moveLearnMethod: NamedApiResource,
    val versionGroup: NamedApiResource,
    val levelLearnedAt: Int
)

data class PokemonStat(
    val stat: NamedApiResource,
    val effort: Int,
    val baseStat: Int
)

data class PokemonType(
    val slot: Int,
    val type: NamedApiResource
)

data class PokeAPIResponse(
    val next: String?,
    val previous: String?,
    val count: Int,
    val results: ArrayList<PokemonViewModel>
)

class PokemonModelLiveData {
    val mutableLiveData: MutableLiveData<ArrayList<PokemonModel>> =
        MutableLiveData(ArrayList())
}

class ListPokemonViewModel: ViewModel() {

    val pokemonModelLiveData: PokemonModelLiveData = PokemonModelLiveData()

    private val client = OkHttpClient()
    private val gson = GsonBuilder().create()

    private var pokemonsSize = 0

    private var isLoading: Boolean = false
    private var next: String? = null
    private var current: String? = null

//    fun restoreData() {
//        val preferences = getPreferences(Context.MODE_PRIVATE)
//
//        val preloadedNext = preferences.getString("next", null)
//        val preloadedCurrent = preferences.getString("current", null)
//        val preloadedPokemons = preferences.getString("pokemons", null)
//
//        next = preloadedNext
//        current = preloadedCurrent
//
//        if (preloadedPokemons != null) {
//            val pokemons = gson.fromJson<ArrayList<PokemonModel>>(
//                preloadedPokemons,
//                object : TypeToken<ArrayList<PokemonModel>>() {}.type)
//
//            pokemonModelLiveData.mutableLiveData.value = pokemons
//        }
//    }

    fun fetchPokemons(url: String?) {
        isLoading = true
        val request = Request.Builder()
            .url(url!!)
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

                    current = next
                    next = resp.next!!

                    resp.results.forEachIndexed {pos, it ->
                        fetchPokemon(it.url)

                        if (pos == resp.results.size - 1) {
                            isLoading = false
                        }
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

                    val prevPokemons = getAllPokemons().value!!
                    prevPokemons.add(resp)

                    pokemonModelLiveData.mutableLiveData.postValue(prevPokemons)
                }
            }
        })
    }

    fun onOverScroll() {
        if (isLoading) {
            return
        }

        if (next != null && next != current) {
            fetchPokemons(next)
        }
    }

    fun getAllPokemons(): LiveData<ArrayList<PokemonModel>> {
        return pokemonModelLiveData.mutableLiveData
    }
}
