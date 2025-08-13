package com.amrubio27.compose_superheroes.features.list.data

import com.amrubio27.compose_superheroes.features.list.data.local.SuperHeroLocalXmlDataSource
import com.amrubio27.compose_superheroes.features.list.data.remote.mock.SuperHeroRemoteDataSourceMock
import com.amrubio27.compose_superheroes.features.list.domain.SuperHero
import com.amrubio27.compose_superheroes.features.list.domain.SuperHeroesRepository
import org.koin.core.annotation.Single

@Single
class SuperHeroDataRepositoryImpl(
    private val remoteDataSource: SuperHeroRemoteDataSourceMock,
    private val localDataSource: SuperHeroLocalXmlDataSource
) : SuperHeroesRepository {
    override fun getSuperHeroes(): Result<List<SuperHero>> {
        val superHeroesFromLocal = localDataSource.getAll()
        return if (superHeroesFromLocal.isEmpty()) {
            val superHeroesFromRemote = remoteDataSource.getSuperHeroes()
            superHeroesFromRemote.apply {
                onSuccess { heroes ->
                    localDataSource.saveAll(heroes)
                }
            }
        } else {
            Result.success(superHeroesFromLocal)
        }
    }
}