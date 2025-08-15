package com.amrubio27.compose_superheroes.features.list.data

import com.amrubio27.compose_superheroes.features.list.data.local.SuperHeroLocalXmlDataSource
import com.amrubio27.compose_superheroes.features.list.data.remote.SuperHeroRemoteDataSource
import com.amrubio27.compose_superheroes.features.list.domain.SuperHero
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.Assert.*
import org.mockito.kotlin.mock
import org.mockito.kotlin.when
import org.mockito.kotlin.verify

/**
 * Integration test for SuperHeroDataRepositoryImpl
 * Tests the getHeroById functionality
 */
class SuperHeroDataRepositoryImplTest {

    private val mockRemoteDataSource: SuperHeroRemoteDataSource = mock()
    private val mockLocalDataSource: SuperHeroLocalXmlDataSource = mock()
    
    private val repository = SuperHeroDataRepositoryImpl(
        remoteDataSource = mockRemoteDataSource,
        localDataSource = mockLocalDataSource
    )

    @Test
    fun `getHeroById should return hero from local cache when available`() = runBlocking {
        // Given
        val heroId = 1
        val localHeroes = listOf(createMockSuperHero(heroId))
        when(mockLocalDataSource.getAll()).thenReturn(localHeroes)

        // When
        val result = repository.getHeroById(heroId)

        // Then
        assertTrue(result.isSuccess)
        assertEquals(heroId, result.getOrNull()?.id)
        verify(mockLocalDataSource).getAll()
        // Should not call remote when found locally
        verify(mockRemoteDataSource, org.mockito.kotlin.never()).getHeroById(heroId)
    }

    @Test
    fun `getHeroById should fetch from remote when not in local cache`() = runBlocking {
        // Given
        val heroId = 1
        val remoteHero = createMockSuperHero(heroId)
        when(mockLocalDataSource.getAll()).thenReturn(emptyList())
        when(mockRemoteDataSource.getHeroById(heroId)).thenReturn(Result.success(remoteHero))

        // When
        val result = repository.getHeroById(heroId)

        // Then
        assertTrue(result.isSuccess)
        assertEquals(heroId, result.getOrNull()?.id)
        verify(mockLocalDataSource).getAll()
        verify(mockRemoteDataSource).getHeroById(heroId)
    }

    @Test
    fun `getHeroById should return failure when hero not found locally and remote fails`() = runBlocking {
        // Given
        val heroId = 999
        val exception = RuntimeException("Network error")
        when(mockLocalDataSource.getAll()).thenReturn(emptyList())
        when(mockRemoteDataSource.getHeroById(heroId)).thenReturn(Result.failure(exception))

        // When
        val result = repository.getHeroById(heroId)

        // Then
        assertTrue(result.isFailure)
        assertEquals(exception, result.exceptionOrNull())
        verify(mockLocalDataSource).getAll()
        verify(mockRemoteDataSource).getHeroById(heroId)
    }

    private fun createMockSuperHero(id: Int): SuperHero {
        return SuperHero(
            id = id,
            name = "Test Hero $id",
            slug = "test-hero-$id",
            powerstats = com.amrubio27.compose_superheroes.features.list.domain.PowerStats(80, 90, 85, 88, 95, 92),
            appearance = com.amrubio27.compose_superheroes.features.list.domain.Appearance(
                gender = "Male",
                race = "Human",
                height = listOf("6'0", "183 cm"),
                weight = listOf("180 lb", "82 kg"),
                eyeColor = "Blue",
                hairColor = "Black"
            ),
            biography = com.amrubio27.compose_superheroes.features.list.domain.Biography(
                fullName = "Test Hero $id Full Name",
                alterEgos = "No alter egos found.",
                aliases = listOf("The Test $id"),
                placeOfBirth = "Test City",
                firstAppearance = "Test Comics #1",
                publisher = "Test Publisher",
                alignment = "good"
            ),
            work = com.amrubio27.compose_superheroes.features.list.domain.Work(
                occupation = "Superhero",
                base = "Test Base"
            ),
            connections = com.amrubio27.compose_superheroes.features.list.domain.Connections(
                groupAffiliation = "Test Team",
                relatives = "None"
            ),
            images = com.amrubio27.compose_superheroes.features.list.domain.Images(
                xs = "https://test.com/xs.jpg",
                sm = "https://test.com/sm.jpg",
                md = "https://test.com/md.jpg",
                lg = "https://test.com/lg.jpg"
            )
        )
    }
}