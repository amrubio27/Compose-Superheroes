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
    override suspend fun getSuperHeroes(forceRefresh: Boolean): Result<List<SuperHero>> {
        val superHeroesFromLocal = if (forceRefresh) {
            Result.failure(Exception("Force refresh"))
        } else {
            local.getAll()
        }

        return if (superHeroesFromLocal.isFailure) {
            remote.getSuperHeroes().apply {
                onSuccess { heroes ->
                    if (forceRefresh) {
                        local.clearAll()
                    }
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

    override suspend fun deleteHeroById(id: Int): Result<Unit> {
        // Por ahora solo borramos en local
        return local.deleteHeroById(id)
    }
}
