package com.amrubio27.compose_superheroes.features.list.data

import com.amrubio27.compose_superheroes.features.list.data.local.dataStore.SuperHeroLocalDataStoreSource
import com.amrubio27.compose_superheroes.features.list.data.remote.SuperHeroRemoteDataSource
import com.amrubio27.compose_superheroes.features.list.domain.SuperHero
import com.amrubio27.compose_superheroes.features.list.domain.SuperHeroesRepository
import org.koin.core.annotation.Single

@Single
class SuperHeroDataRepositoryImpl(
    private val remoteDataSource: SuperHeroRemoteDataSource,
    private val localDataSource: SuperHeroLocalDataStoreSource
) : SuperHeroesRepository {
    override suspend fun getSuperHeroes(): Result<List<SuperHero>> {
        val superHeroesFromLocal = localDataSource.getAll()
        return if (superHeroesFromLocal.isEmpty()) {
            remoteDataSource.getSuperHeroes().onSuccess { heroes ->
                localDataSource.saveAll(heroes)
            }
        } else {
            Result.success(superHeroesFromLocal)
        }
    }

    override suspend fun getHeroById(id: Int): Result<SuperHero> {
        val localHero = localDataSource.getHeroById(id)

        return if (localHero == null) {
            remoteDataSource.getHeroById(id).onSuccess { hero ->
                localDataSource.saveByHero(hero)
            }
        } else {
            Result.success(localHero)
        }
    }
}