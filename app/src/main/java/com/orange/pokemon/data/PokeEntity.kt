package com.orange.pokemon.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Pokemon_Tab")

data class PokeEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val attack:Int,
    val name:String,
    val image:String,
)
