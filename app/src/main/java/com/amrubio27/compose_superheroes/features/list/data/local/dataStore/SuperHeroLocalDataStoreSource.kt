package com.amrubio27.compose_superheroes.features.list.data.local.dataStore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
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

    private companion object {
        val SUPERHEROES_LIST_KEY = stringPreferencesKey("superheroes_list")
    }

    // Decodifica un String a SuperHero de forma segura.
    private fun String.toHeroOrNull(): SuperHero? =
        runCatching { json.decodeFromString<SuperHero>(this) }.getOrNull()

    // Extrae todos los héroes de Preferences.
    private fun Preferences.extractHeroes(): List<SuperHero> =
        asMap().values.asSequence().mapNotNull { (it as? String)?.toHeroOrNull() }.toList()

    override fun getAllFlow(): Flow<List<SuperHero>> {
        return dataStore.data.map { preferences ->
            json.decodeFromString<List<SuperHero>>(preferences[SUPERHEROES_LIST_KEY] ?: "[]")
        }
    }

    override suspend fun getAll(): List<SuperHero> = getAllFlow().first()

    override fun getHeroByIdFlow(id: Int): Flow<SuperHero?> {
        return getAllFlow().map { heroes ->
            heroes.find { it.id == id }
        }
    }

    override suspend fun getHeroById(id: Int): SuperHero? = getHeroByIdFlow(id).first()

    override suspend fun saveAll(superHeroes: List<SuperHero>) {
        dataStore.edit { preferences ->
            preferences[SUPERHEROES_LIST_KEY] = json.encodeToString(superHeroes)
        }
    }

    override suspend fun saveByHero(superHero: SuperHero) {
        dataStore.edit { preferences ->
            // 1. Leemos la lista actual que está en el DataStore.
            val currentJson = preferences[SUPERHEROES_LIST_KEY]
            val heroes = if (currentJson != null) {
                json.decodeFromString<MutableList<SuperHero>>(currentJson)
            } else {
                mutableListOf()
            }

            // 2. Buscamos si el héroe ya existe para reemplazarlo o añadirlo.
            val existingIndex = heroes.indexOfFirst { it.id == superHero.id }
            if (existingIndex != -1) {
                heroes[existingIndex] = superHero // Reemplaza
            } else {
                heroes.add(superHero) // Añade
            }

            // 3. Guardamos la lista actualizada de vuelta en el DataStore.
            preferences[SUPERHEROES_LIST_KEY] = json.encodeToString(heroes)
        }
    }

    override suspend fun delete() {
        dataStore.edit { it.clear() }
    }
}
