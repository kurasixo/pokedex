package com.keparisss.pokedex.di

import com.keparisss.pokedex.list.ListActivity
import com.keparisss.pokedex.list.DetailsActivity

import dagger.Component

@Component(modules = [ListModule::class])
interface ListComponent {
    fun inject(target: ListActivity)
    fun inject(target: DetailsActivity)
}
