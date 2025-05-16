package br.com.agendou.data.repository

import br.com.agendou.domain.models.Service
import com.google.firebase.firestore.DocumentReference
import kotlinx.coroutines.flow.Flow

interface ServiceRepository {
    suspend fun createService(service: Service): Result<Service>
    suspend fun getServiceById(id: String): Result<Service>
    suspend fun updateService(service: Service): Result<Service>
    suspend fun deleteService(id: String): Result<Unit>
    fun getAllServices(): Flow<List<Service>>
    fun getServicesByProfessional(professionalRef: DocumentReference): Flow<List<Service>>
    fun getServicesByCategory(categoryRef: DocumentReference): Flow<List<Service>>
} 