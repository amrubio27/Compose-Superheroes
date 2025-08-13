package com.amrubio27.compose_superheroes.features.list.data

import com.amrubio27.compose_superheroes.features.list.data.remote.mock.SuperHeroRemoteDataSourceMock
import com.amrubio27.compose_superheroes.features.list.domain.SuperHero
import com.amrubio27.compose_superheroes.features.list.domain.SuperHeroesRepository
import org.koin.core.annotation.Single

@Single
class SuperHeroDataRepositoryImpl(
    private val remoteDataSource: SuperHeroRemoteDataSourceMock
) : SuperHeroesRepository {
    override fun getSuperHeroes(): Result<List<SuperHero>> {
        return remoteDataSource.getSuperHeroes()
    }
}