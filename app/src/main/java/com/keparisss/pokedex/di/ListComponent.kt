package com.keparisss.pokedex.di

import com.keparisss.pokedex.list.ListActivity

import dagger.Component

@Component(modules = [ListModule::class])
interface ListComponent {
    fun inject(target: ListActivity)
}
