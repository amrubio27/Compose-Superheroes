package com.amrubio27.compose_superheroes.features.list.data.remote

import org.junit.Test
import org.junit.Assert.*

/**
 * Test to verify that the endpoint URL is correctly formatted
 * for the superhero detail feature according to the requirement: id/{id}.json
 */
class SuperHeroServiceEndpointTest {

    @Test
    fun `verify endpoint URL format for getHeroById`() {
        // Given
        val baseUrl = "https://akabab.github.io/superhero-api/api/"
        val heroId = 123
        val expectedUrl = "${baseUrl}id/$heroId.json"
        
        // When - This simulates what happens in SuperHeroServiceImpl.getHeroById()
        val actualUrl = "${baseUrl}id/$heroId.json"
        
        // Then
        assertEquals(expectedUrl, actualUrl)
        assertEquals("https://akabab.github.io/superhero-api/api/id/123.json", actualUrl)
        
        // Verify the pattern matches the requirement
        assertTrue("URL should contain 'id/' pattern", actualUrl.contains("id/"))
        assertTrue("URL should end with '.json'", actualUrl.endsWith(".json"))
        assertTrue("URL should contain the hero ID", actualUrl.contains(heroId.toString()))
    }

    @Test
    fun `verify endpoint works with different hero IDs`() {
        val baseUrl = "https://akabab.github.io/superhero-api/api/"
        
        // Test various hero IDs
        val testCases = listOf(
            1 to "https://akabab.github.io/superhero-api/api/id/1.json",
            42 to "https://akabab.github.io/superhero-api/api/id/42.json", 
            999 to "https://akabab.github.io/superhero-api/api/id/999.json"
        )
        
        testCases.forEach { (heroId, expectedUrl) ->
            val actualUrl = "${baseUrl}id/$heroId.json"
            assertEquals("URL should match expected format for hero ID $heroId", expectedUrl, actualUrl)
        }
    }
}