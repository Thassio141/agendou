package br.com.agendou.domain.usecases.service

import br.com.agendou.data.repository.ServiceRepository
import javax.inject.Inject

class DeleteServiceUseCase @Inject constructor(
    private val serviceRepository: ServiceRepository
) {
    suspend operator fun invoke(id: String): Result<Unit> {
        return serviceRepository.deleteService(id)
    }
} 