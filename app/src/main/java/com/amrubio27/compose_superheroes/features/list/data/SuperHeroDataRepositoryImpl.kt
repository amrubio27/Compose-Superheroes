package com.amrubio27.compose_superheroes.features.list.data

import com.amrubio27.compose_superheroes.features.list.data.local.room.SuperHeroLocalRoomDataSourceImpl
import com.amrubio27.compose_superheroes.features.list.data.remote.SuperHeroRemoteDataSource
import com.amrubio27.compose_superheroes.features.list.domain.SuperHero
import com.amrubio27.compose_superheroes.features.list.domain.SuperHeroesRepository
import org.koin.core.annotation.Single

@Single
class SuperHeroDataRepositoryImpl(
    private val remote: SuperHeroRemoteDataSource,
    private val local: SuperHeroLocalRoomDataSourceImpl
) : SuperHeroesRepository {
    override suspend fun getSuperHeroes(): Result<List<SuperHero>> {
        val superHeroesFromLocal = local.getAll()

        return if (superHeroesFromLocal.isFailure) {
            remote.getSuperHeroes().apply {
                onSuccess { heroes ->
                    local.saveAll(heroes)
                }
            }
        } else {
            superHeroesFromLocal
        }
    }

    override suspend fun getHeroById(id: Int): Result<SuperHero> {
        val localHero = local.getHeroById(id)

        return if (localHero.isFailure) {
            remote.getHeroById(id).apply {
                onSuccess { hero ->
                    local.saveByHero(hero)
                }
            }
        } else {
            localHero
        }
    }
}