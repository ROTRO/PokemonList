package com.orange.pokemon.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.orange.pokemon.databinding.PokeitemBinding
import com.orange.pokemon.model.Pokemon

class PokeAdapter:ListAdapter<Pokemon, PokeAdapter.PokemonViewholder>(PokemonDiffUtils()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonViewholder {
        val binding = PokeitemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )


        return PokemonViewholder(binding)
    }

    override fun onBindViewHolder(holder: PokemonViewholder, position: Int) {
        val item = getItem(position)
        holder.bind(item)

    }


    inner class PokemonViewholder(val binding: PokeitemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(pokemon: Pokemon) {

            val ctx = itemView.context
            with(binding) {
                name.text = pokemon.name
                attack.text = pokemon.attack.toString()

                Glide.with(ctx)
                    .load(pokemon.imageurl)
                    .centerCrop()
                    .into(binding.image)
            }
        }
    }
}
class PokemonDiffUtils : DiffUtil.ItemCallback<Pokemon>() {

    override fun areItemsTheSame(
        oldItem: Pokemon,
        newItem: Pokemon
    ) = oldItem.id == newItem.id

    override fun areContentsTheSame(
        oldItem: Pokemon,
        newItem: Pokemon
    ) = oldItem == newItem
}