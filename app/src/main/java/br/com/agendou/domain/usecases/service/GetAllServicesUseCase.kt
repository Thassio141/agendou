package br.com.agendou.domain.usecases.service

import br.com.agendou.data.repository.ServiceRepository
import br.com.agendou.domain.models.Service
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllServicesUseCase @Inject constructor(
    private val serviceRepository: ServiceRepository
) {
    operator fun invoke(): Flow<List<Service>> {
        return serviceRepository.getAllServices()
    }
} 