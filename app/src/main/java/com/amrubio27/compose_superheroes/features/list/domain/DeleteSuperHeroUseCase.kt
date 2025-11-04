package com.amrubio27.compose_superheroes.features.list.domain

import org.koin.core.annotation.Single

/**
 * Use case para borrar un superhéroe.
 * Encapsula la lógica de negocio del borrado siguiendo el principio de Single Responsibility.
 */
@Single
class DeleteSuperHeroUseCase(
    private val repository: SuperHeroesRepository
) {
    /**
     * Borra un superhéroe por su ID.
     *
     * @param id ID del superhéroe a borrar
     * @return Result<Unit> Success si se borró correctamente, Failure en caso de error
     */
    suspend operator fun invoke(id: Int): Result<Unit> {
        return repository.deleteHeroById(id)
    }
}
