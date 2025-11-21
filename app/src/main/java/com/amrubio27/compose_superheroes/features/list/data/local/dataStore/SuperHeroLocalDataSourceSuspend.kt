package com.amrubio27.compose_superheroes.features.list.data.local.dataStore

import com.amrubio27.compose_superheroes.features.list.domain.SuperHero
import kotlinx.coroutines.flow.Flow

interface SuperHeroLocalDataSourceSuspend {
    suspend fun saveAll(superHeroes: List<SuperHero>)
    suspend fun saveByHero(superHero: SuperHero)
    suspend fun getAll(): List<SuperHero>
    suspend fun getHeroById(id: Int): SuperHero?
    suspend fun delete()

    // Métodos reactivos que utilizan Flow
    fun getAllFlow(): Flow<List<SuperHero>>
    fun getHeroByIdFlow(id: Int): Flow<SuperHero?>
}
