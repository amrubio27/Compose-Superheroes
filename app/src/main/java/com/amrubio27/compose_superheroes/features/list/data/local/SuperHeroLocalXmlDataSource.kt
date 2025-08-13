package com.amrubio27.compose_superheroes.features.list.data.local

import com.amrubio27.compose_superheroes.features.list.domain.SuperHero
import org.koin.core.annotation.Single

interface SuperHeroLocalDataSource {
    fun getAll(): List<SuperHero>
    fun saveAll(superHeroes: List<SuperHero>)
}

@Single
class SuperHeroLocalXmlDataSource : SuperHeroLocalDataSource {
    private val cachedSuperHeroes = mutableListOf<SuperHero>()

    override fun getAll(): List<SuperHero> {
        return cachedSuperHeroes.toList()
    }

    override fun saveAll(superHeroes: List<SuperHero>) {
        cachedSuperHeroes.clear()
        cachedSuperHeroes.addAll(superHeroes)
    }
}
