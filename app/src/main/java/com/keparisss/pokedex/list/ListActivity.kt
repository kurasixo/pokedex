package com.keparisss.pokedex.list

import android.content.Context

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders

import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager
import com.keparisss.pokedex.models.ListPokemonViewModel

import com.keparisss.pokedex.R
import kotlinx.android.synthetic.main.list_activity.*
import kotlinx.coroutines.GlobalScope
//import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class ListActivity : AppCompatActivity() {
    private var pokemonViewModel: ListPokemonViewModel? = null
    private val pokemonApiUrl: String = "https://pokeapi.co/api/v2/pokemon"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.list_activity)

        pokemonViewModel = ViewModelProviders.of(this)[ListPokemonViewModel::class.java]
        pokemonList.layoutManager = LinearLayoutManager(this)
        val pokemons = pokemonViewModel!!.getAllPokemons()

        GlobalScope.launch { pokemonViewModel!!.fetchPokemons(pokemonApiUrl) }
        pokemonList.adapter = ListActivityAdapter(pokemons.value!!, this)

        pokemons.observe(this, Observer {
            pokemonList.adapter?.notifyDataSetChanged()
        })

        pokemonList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if (!recyclerView.canScrollVertically(1)) {
                    GlobalScope.launch {
                        pokemonViewModel!!.onOverScroll()
                    }
                }
            }
        })
    }

//    override fun onPause() {
//        super.onPause()
//
//        val editor = getPreferences(Context.MODE_PRIVATE).edit()
//
//        editor.putString("next", next)
//        editor.putString("current", current)
//        editor.putString("pokemons", gson.toJson(pokemonViewModel!!.getAllPokemons().value))
//        editor.putString("pokemonsSize", pokemonViewModel!!.getAllPokemons().value!!.size.toString())
//
//        editor.apply()
//    }
}
