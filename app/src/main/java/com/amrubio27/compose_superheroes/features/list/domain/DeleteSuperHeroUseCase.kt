package com.amrubio27.compose_superheroes.features.list.domain

import org.koin.core.annotation.Single

/**
 * Use case for deleting a superhero by ID.
 *
 * @param repository The repository to handle superhero data operations
 */
@Single
class DeleteSuperHeroUseCase(
    private val repository: SuperHeroesRepository
) {
    /**
     * Deletes a superhero by its ID.
     *
     * @param id The ID of the superhero to delete
     * @return Result<Unit> indicating success or failure of the operation
     */
    suspend operator fun invoke(id: Int): Result<Unit> {
        return repository.deleteHeroById(id)
    }
}