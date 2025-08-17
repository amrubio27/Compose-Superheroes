package com.amrubio27.compose_superheroes.features.list.data.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.amrubio27.compose_superheroes.features.list.data.local.room.entity.SuperHeroEntity

@Dao
interface SuperHeroDao {
    @Query("SELECT * FROM superheroes")
    suspend fun getAllSuperHeroes(): List<SuperHeroEntity>

    @Query("SELECT * FROM superheroes WHERE id = :id")
    suspend fun getSuperHeroById(id: Int): SuperHeroEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(superheroes: List<SuperHeroEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(superhero: SuperHeroEntity)

    @Query("DELETE FROM superheroes WHERE id = :id")
    suspend fun deleteSuperHeroById(id: Int)

    @Query("DELETE FROM superheroes")
    suspend fun deleteAllSuperHeroes()

}
