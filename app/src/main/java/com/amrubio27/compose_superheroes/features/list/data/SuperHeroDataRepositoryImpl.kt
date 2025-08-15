package com.amrubio27.compose_superheroes.features.list.data

import com.amrubio27.compose_superheroes.features.list.data.local.SuperHeroLocalXmlDataSource
import com.amrubio27.compose_superheroes.features.list.data.remote.SuperHeroRemoteDataSource
import com.amrubio27.compose_superheroes.features.list.domain.SuperHero
import com.amrubio27.compose_superheroes.features.list.domain.SuperHeroesRepository
import org.koin.core.annotation.Single

@Single
class SuperHeroDataRepositoryImpl(
    private val remoteDataSource: SuperHeroRemoteDataSource,
    private val localDataSource: SuperHeroLocalXmlDataSource
) : SuperHeroesRepository {
    override suspend fun getSuperHeroes(): Result<List<SuperHero>> {
        val superHeroesFromLocal = localDataSource.getAll()
        return if (superHeroesFromLocal.isEmpty()) {
            remoteDataSource.getSuperHeroes()
                .onSuccess { heroes ->
                    localDataSource.saveAll(heroes)
                }
        } else {
            Result.success(superHeroesFromLocal)
        }
    }

    override suspend fun getHeroById(id: Int): Result<SuperHero> {
        // First try to get from local cache
        val localHeroes = localDataSource.getAll()
        val localHero = localHeroes.find { it.id == id }
        
        return if (localHero != null) {
            Result.success(localHero)
        } else {
            // If not found locally, fetch from remote
            remoteDataSource.getHeroById(id)
        }
    }
}