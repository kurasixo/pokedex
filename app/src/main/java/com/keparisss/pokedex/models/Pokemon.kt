package com.keparisss.pokedex.models

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

