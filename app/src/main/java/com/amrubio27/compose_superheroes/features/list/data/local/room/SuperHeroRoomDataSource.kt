package com.amrubio27.compose_superheroes.features.list.data.local.room

import com.amrubio27.compose_superheroes.app.domain.ErrorApp
import com.amrubio27.compose_superheroes.features.list.data.local.room.dao.SuperHeroDao
import com.amrubio27.compose_superheroes.features.list.data.local.room.entity.SuperHeroEntity
import com.amrubio27.compose_superheroes.features.list.domain.SuperHero
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.annotation.Single

interface SuperHeroLocalRoomSource {
    suspend fun getAll(ttlMillis: Long = DEFAULT_TTL_MILLIS): Result<List<SuperHero>>
    suspend fun getHeroById(id: Int): Result<SuperHero>
    suspend fun saveAll(heroes: List<SuperHero>)
    suspend fun saveByHero(hero: SuperHero)
    suspend fun clearAll()
    suspend fun refreshSuperHeroes(heroes: List<SuperHero>)

    suspend fun deleteHeroById(id: Int): Result<Unit>

    companion object {
        // TTL de 1 hora por defecto
        const val DEFAULT_TTL_MILLIS = 60 * 60 * 1000L
    }
}

@Single
class SuperHeroLocalRoomDataSourceImpl(
    private val superHeroDao: SuperHeroDao
) : SuperHeroLocalRoomSource {

    override suspend fun getAll(ttlMillis: Long): Result<List<SuperHero>> =
        withContext(Dispatchers.IO) {
            val heroes = superHeroDao.getAllSuperHeroes()

            return@withContext if (heroes.isEmpty() || System.currentTimeMillis() - heroes.first().timeStamp > ttlMillis) {
                Result.failure(ErrorApp.DataExpiredError)
            } else {
                Result.success(heroes.map { it.toDomain() })
            }
        }

    override suspend fun getHeroById(id: Int): Result<SuperHero> = withContext(Dispatchers.IO) {
        val hero = superHeroDao.getSuperHeroById(id)
        return@withContext if (hero != null && System.currentTimeMillis() - hero.timeStamp < SuperHeroLocalRoomSource.DEFAULT_TTL_MILLIS) {
            Result.success(hero.toDomain())
        } else {
            Result.failure(ErrorApp.DataExpiredError)
        }
    }

    override suspend fun saveAll(heroes: List<SuperHero>) = withContext(Dispatchers.IO) {
        val currentTime = System.currentTimeMillis()
        val entities = heroes.map { SuperHeroEntity.toEntity(it, currentTime) }
        superHeroDao.insertAll(entities)
    }

    override suspend fun saveByHero(hero: SuperHero) = withContext(Dispatchers.IO) {
        val entity = SuperHeroEntity.toEntity(hero, System.currentTimeMillis())
        superHeroDao.insert(entity)
    }

    override suspend fun clearAll() = withContext(Dispatchers.IO) {
        superHeroDao.deleteAllSuperHeroes()
    }

    override suspend fun refreshSuperHeroes(heroes: List<SuperHero>) = withContext(Dispatchers.IO) {
        val currentTime = System.currentTimeMillis()
        val entities = heroes.map { SuperHeroEntity.toEntity(it, currentTime) }
        superHeroDao.replaceAll(entities)
    }

    override suspend fun deleteHeroById(id: Int): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            superHeroDao.deleteSuperHeroById(id)
        } catch (e: Exception) {
            //The error handling discards the original exception information by always returning ErrorApp.UnknownError. Consider preserving the original exception message or type to aid debugging: Result.failure(ErrorApp.UnknownError.withCause(e)) or passing the exception message to provide more context about what went wrong during deletion.
            return@withContext Result.failure(ErrorApp.UnknownError)
        }
        return@withContext Result.success(Unit)
    }
}
