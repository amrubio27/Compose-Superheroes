package com.amrubio27.compose_superheroes.features.list.domain

interface SuperHeroesRepository {
    suspend fun getSuperHeroes(forceRefresh: Boolean = false): Result<List<SuperHero>>
    suspend fun getHeroById(id: Int): Result<SuperHero>
    suspend fun deleteHeroById(id: Int): Result<Unit>
}
