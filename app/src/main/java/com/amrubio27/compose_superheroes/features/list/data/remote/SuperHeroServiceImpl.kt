package com.amrubio27.compose_superheroes.features.list.data.remote

import com.amrubio27.compose_superheroes.app.data.remote.safeApiCall
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import org.koin.core.annotation.Single


@Single
class SuperHeroServiceImpl(
    private val client: HttpClient,
    private val baseUrl: String
) : SuperHeroService {

    override suspend fun getSuperHeroes(): Result<List<SuperHeroApiModel>> {
        return safeApiCall {
            client.get("${baseUrl}all.json")
        }
    }

    /*override suspend fun getHeroById(id: Int): Result<SuperHeroApiModel> {
        return safeApiCall {
            client.get("$baseUrl/id/$id.json")
        }
    }*/
}
