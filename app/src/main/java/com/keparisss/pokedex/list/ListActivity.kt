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
import kotlinx.coroutines.*

class ListActivity : AppCompatActivity() {
    private var pokemonViewModel: ListPokemonViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.list_activity)

        pokemonViewModel = ViewModelProviders.of(this)[ListPokemonViewModel::class.java]
        pokemonList.layoutManager = LinearLayoutManager(this)
        val pokemons = pokemonViewModel!!.getAllPokemons()

        CoroutineScope(Dispatchers.IO).launch { pokemonViewModel!!.initPokemons() }
        pokemonList.adapter = ListActivityAdapter(pokemons.value!!, this)

        pokemons.observe(this, Observer {
            pokemonList.adapter?.notifyDataSetChanged()
            pokemonCount.text = resources.getQuantityString(R.plurals.pokemons, it.size, it.size)
        })

        pokemonList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if (!recyclerView.canScrollVertically(1)) {
                    CoroutineScope(Dispatchers.IO).launch {
                        pokemonViewModel!!.onOverScroll()
                    }
                }
            }
        })
    }

    override fun onPause() {
        super.onPause()

        pokemonViewModel!!.saveToPreferences()

        val preferences = getSharedPreferences("sensitiveData", Context.MODE_PRIVATE)
        val editor = preferences.edit()

        editor.putLong("lastAppOpened", System.currentTimeMillis())

        editor.apply()
    }
}
