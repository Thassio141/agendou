package br.com.agendou.domain.usecases.service

import br.com.agendou.data.repository.ServiceRepository
import br.com.agendou.domain.models.Service
import com.google.firebase.firestore.DocumentReference
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetServicesByCategoryUseCase @Inject constructor(
    private val serviceRepository: ServiceRepository
) {
    operator fun invoke(categoryRef: DocumentReference): Flow<List<Service>> {
        return serviceRepository.getServicesByCategory(categoryRef)
    }
} 