package com.amrubio27.compose_superheroes.features.list.domain

interface SuperHeroesRepository {
    fun getSuperHeroes(): Result<List<SuperHero>>
}