package br.com.agendou.domain.usecases.service

import br.com.agendou.data.repository.ServiceRepository
import br.com.agendou.domain.models.Service
import javax.inject.Inject

class UpdateServiceUseCase @Inject constructor(
    private val serviceRepository: ServiceRepository
) {
    suspend operator fun invoke(service: Service): Result<Service> {
        return serviceRepository.updateService(service)
    }
} 