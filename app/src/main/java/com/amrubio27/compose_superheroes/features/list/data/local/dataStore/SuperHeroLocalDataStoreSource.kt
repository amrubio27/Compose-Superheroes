package com.amrubio27.compose_superheroes.features.list.data.local.dataStore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.amrubio27.compose_superheroes.features.list.data.local.datastore.SuperHeroLocalDataSourceSuspend
import com.amrubio27.compose_superheroes.features.list.domain.SuperHero
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import org.koin.core.annotation.Single

@Single
class SuperHeroLocalDataStoreSource(
    private val dataStore: DataStore<Preferences>, private val json: Json
) : SuperHeroLocalDataSourceSuspend {

    // Decodifica un String a SuperHero de forma segura.
    private fun String.toHeroOrNull(): SuperHero? =
        runCatching { json.decodeFromString<SuperHero>(this) }.getOrNull()

    // Extrae todos los héroes de Preferences.
    private fun Preferences.extractHeroes(): List<SuperHero> =
        asMap().values.asSequence().mapNotNull { (it as? String)?.toHeroOrNull() }.toList()

    override fun getAllFlow(): Flow<List<SuperHero>> = dataStore.data.map { it.extractHeroes() }

    override suspend fun getAll(): List<SuperHero> = getAllFlow().first()

    override fun getHeroByIdFlow(id: Int): Flow<SuperHero?> {
        val key = stringPreferencesKey(id.toString())
        return dataStore.data.map { prefs ->
            prefs[key]?.toHeroOrNull()
        }
    }

    override suspend fun getHeroById(id: Int): SuperHero? = getHeroByIdFlow(id).first()

    override suspend fun saveAll(superHeroes: List<SuperHero>) {
        dataStore.edit { preferences ->
            preferences.clear()
            superHeroes.forEach { hero ->
                val key = stringPreferencesKey(hero.id.toString())
                preferences[key] = json.encodeToString(hero)
            }
        }
    }

    override suspend fun saveByHero(superHero: SuperHero) {
        dataStore.edit { preferences ->
            val key = stringPreferencesKey(superHero.id.toString())
            preferences[key] = json.encodeToString(superHero)
        }
    }

    override suspend fun delete() {
        dataStore.edit { it.clear() }
    }
}
