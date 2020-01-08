package com.keparisss.pokedex.list


import android.content.Context
import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.ImageView
import android.widget.RatingBar
import com.bumptech.glide.Glide
import com.keparisss.pokedex.details.DetailsActivity
import com.keparisss.pokedex.models.PokemonModel
import com.keparisss.pokedex.R
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.list_activity_item.view.*


class ListActivityAdapter(private var items: ArrayList<PokemonModel>, private val context: Context):
    RecyclerView.Adapter<ListActivityAdapter.ViewHolder>() {

    private val gson = GsonBuilder().create()

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val pokemonName: TextView = view.pokemonName
        val pokemonRatingBar: RatingBar = view.pokemonRatingBar
        val pokemonSprite: ImageView = view.pokemonSpritePreview
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater
                .from(context)
                .inflate(R.layout.list_activity_item, parent, false)
        )

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.pokemonName.text = items[position].name
        holder.pokemonRatingBar.rating = items[position].rating

        Glide
            .with(context)
            .load(items[position].sprites.front_default)
            .error(R.drawable.ic_broken_image)
            .fallback(R.drawable.ic_broken_image)
            .into(holder.pokemonSprite)

        holder.pokemonName.setOnClickListener {
            val intent = Intent(context, DetailsActivity::class.java)
            intent.putExtra("pokemon", gson.toJson(items[position]))

            context.startActivity(intent)
        }
    }
}

