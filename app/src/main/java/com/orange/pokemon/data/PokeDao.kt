package com.orange.pokemon.data

import androidx.room.*

@Dao
interface PokeDao {

    @Insert
    suspend fun insertOne(entity: PokeEntity)

    @Insert
    suspend fun insertAll(entities: List<PokeEntity>)

    @Delete
    suspend fun deleteOne(entity: PokeEntity)

    @Query("SELECT * FROM Pokemon_Tab")
    suspend fun getAll(): List<PokeEntity>

    @Query("SELECT * FROM Pokemon_Tab WHERE id = :id")
    suspend fun getOneByID(id: Int): PokeEntity

    @Update
    suspend fun updateOne(transaction: PokeEntity)


}