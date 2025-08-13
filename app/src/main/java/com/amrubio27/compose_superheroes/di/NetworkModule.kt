package com.amrubio27.compose_superheroes.di

import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
@ComponentScan("com.amrubio27.compose_superheroes")
class NetworkModule {

    @Single
    fun provideBaseUrl() = "https://akabab.github.io/superhero-api/api/"

    @Single
    fun provideKtorClient(json: Json): HttpClient {
        return HttpClient(OkHttp) {
            install(ContentNegotiation) {
                json(json)
            }
        }
    }
}
