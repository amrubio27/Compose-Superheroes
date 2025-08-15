package com.amrubio27.compose_superheroes.features.list.data.remote

interface SuperHeroService {
    suspend fun getSuperHeroes(): Result<List<SuperHeroApiModel>>
    suspend fun getHeroById(id: Int): Result<SuperHeroApiModel>
}