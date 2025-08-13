package com.amrubio27.compose_superheroes.features.list.data.remote

import com.amrubio27.compose_superheroes.features.list.domain.SuperHero
import org.koin.core.annotation.Single

@Single
class SuperHeroRemoteDataSourceImpl(
    private val superHeroService: SuperHeroService
) : SuperHeroRemoteDataSource {

    override suspend fun getSuperHeroes(): Result<List<SuperHero>> {
        return superHeroService.getSuperHeroes().map { apiModels ->
            apiModels.map { it.toModel() }
        }
    }

    /*override suspend fun getHeroById(id: Int): Result<SuperHero> {
        return superHeroService.getHeroById(id).map { it.toModel() }
    }*/
}
