package com.amrubio27.compose_superheroes.features.list.domain

import org.junit.Test
import org.junit.Assert.*
import kotlinx.coroutines.runBlocking
import org.mockito.Mockito.*
import org.mockito.kotlin.mock

/**
 * Unit test for GetSuperHeroByIdUseCase
 */
class GetSuperHeroByIdUseCaseTest {

    private val mockRepository: SuperHeroesRepository = mock()
    private val useCase = GetSuperHeroByIdUseCase(mockRepository)

    @Test
    fun `invoke should return superhero when repository returns success`() = runBlocking {
        // Given
        val heroId = 1
        val expectedHero = createMockSuperHero(heroId)
        `when`(mockRepository.getHeroById(heroId)).thenReturn(Result.success(expectedHero))

        // When
        val result = useCase.invoke(heroId)

        // Then
        assertTrue(result.isSuccess)
        assertEquals(expectedHero, result.getOrNull())
        verify(mockRepository).getHeroById(heroId)
    }

    @Test
    fun `invoke should return failure when repository returns failure`() = runBlocking {
        // Given
        val heroId = 1
        val exception = RuntimeException("Network error")
        `when`(mockRepository.getHeroById(heroId)).thenReturn(Result.failure(exception))

        // When
        val result = useCase.invoke(heroId)

        // Then
        assertTrue(result.isFailure)
        assertEquals(exception, result.exceptionOrNull())
        verify(mockRepository).getHeroById(heroId)
    }

    private fun createMockSuperHero(id: Int): SuperHero {
        return SuperHero(
            id = id,
            name = "Test Hero",
            slug = "test-hero",
            powerstats = PowerStats(80, 90, 85, 88, 95, 92),
            appearance = Appearance(
                gender = "Male",
                race = "Human",
                height = listOf("6'0", "183 cm"),
                weight = listOf("180 lb", "82 kg"),
                eyeColor = "Blue",
                hairColor = "Black"
            ),
            biography = Biography(
                fullName = "Test Hero Full Name",
                alterEgos = "No alter egos found.",
                aliases = listOf("The Test", "Hero Test"),
                placeOfBirth = "Test City",
                firstAppearance = "Test Comics #1",
                publisher = "Test Publisher",
                alignment = "good"
            ),
            work = Work(
                occupation = "Superhero",
                base = "Test Base"
            ),
            connections = Connections(
                groupAffiliation = "Test Team",
                relatives = "None"
            ),
            images = Images(
                xs = "https://test.com/xs.jpg",
                sm = "https://test.com/sm.jpg",
                md = "https://test.com/md.jpg",
                lg = "https://test.com/lg.jpg"
            )
        )
    }
}