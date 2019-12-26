package com.example.room


import android.content.Context
import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.list_activity_item.view.*


class PokemonAdapter(private val items: ArrayList<PokemonModel>, private val context: Context):
    RecyclerView.Adapter<PokemonAdapter.ViewHolder>() {

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val tvAnimal: TextView = view.tv_animal
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater
                .from(context)
                .inflate(R.layout.list_activity_item, parent, false))

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvAnimal.text = items[position].name
        holder.tvAnimal.setOnClickListener{
            val intent = Intent(context, DetailsActivity::class.java)
            intent.putExtra("description", items[position].name)
            context.startActivity(intent)
        }
    }
}

