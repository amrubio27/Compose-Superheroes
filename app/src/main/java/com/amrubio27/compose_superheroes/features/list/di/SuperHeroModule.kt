package com.amrubio27.compose_superheroes.features.list.di

import com.amrubio27.compose_superheroes.features.list.data.remote.SuperHeroService
import com.amrubio27.compose_superheroes.features.list.data.remote.SuperHeroServiceImpl
import io.ktor.client.HttpClient
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
@ComponentScan("com.amrubio27.compose_superheroes.features.list")
class SuperHeroModule {
    @Single
    fun provideSuperHeroService(client: HttpClient, baseUrl: String): SuperHeroService =
        SuperHeroServiceImpl(client, baseUrl)
}