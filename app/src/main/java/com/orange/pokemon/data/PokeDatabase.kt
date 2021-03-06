package com.orange.pokemon.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(
    entities = [PokeEntity::class],
    version = 1
)
abstract class PokeDatabase: RoomDatabase() {
    abstract fun getPokemonDao(): PokeDao

    companion object {

        private var instance: PokeDatabase? = null

        fun getInstance(context: Context): PokeDatabase {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context,
                    PokeDatabase::class.java,
                    "Pokemon_DB"
                ).build()
            }
            return instance!!
        }
    }
}