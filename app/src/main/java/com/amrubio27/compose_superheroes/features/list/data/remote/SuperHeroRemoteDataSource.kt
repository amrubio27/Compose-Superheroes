package com.amrubio27.compose_superheroes.features.list.data.remote

import com.amrubio27.compose_superheroes.features.list.domain.SuperHero

interface SuperHeroRemoteDataSource {
    suspend fun getSuperHeroes(): Result<List<SuperHero>>
    //suspend fun getHeroById(id: Int): Result<SuperHero>
}