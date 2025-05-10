package br.com.agendou.domain.usecases.service

import br.com.agendou.data.repository.ServiceRepository
import br.com.agendou.domain.models.Service
import javax.inject.Inject

class GetServiceByIdUseCase @Inject constructor(
    private val serviceRepository: ServiceRepository
) {
    suspend operator fun invoke(id: String): Result<Service> {
        return serviceRepository.getServiceById(id)
    }
} 