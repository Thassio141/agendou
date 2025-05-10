package br.com.agendou.domain.usecases.service

import br.com.agendou.common.ErrorMessages
import br.com.agendou.data.repository.ServiceRepository
import br.com.agendou.domain.models.Service
import br.com.agendou.domain.usecases.auth.AuthUseCase
import br.com.agendou.domain.usecases.user.UserUseCase
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ServiceUseCaseImpl @Inject constructor(
    private val serviceRepository: ServiceRepository,
    private val authUseCase: AuthUseCase,
    private val userUC: UserUseCase,
    private val firestore: FirebaseFirestore
) : ServiceUseCase {


    private fun currentUserOrThrow() =
        authUseCase.currentUser ?: throw IllegalStateException(ErrorMessages.USER_NOT_AUTHENTICATED)

    private fun userRef(uid: String) =
        firestore.collection("users").document(uid)

    override suspend fun createService(service: Service): Result<Service> =
        runCatching {
            currentUserOrThrow()
            val profile = userUC.getCurrentUser().getOrThrow()

            val toSave = service.copy(
                professionalRef = userRef(profile.id)
            )
            serviceRepository.createService(toSave).getOrThrow()
        }

    override suspend fun updateService(service: Service): Result<Service> =
        runCatching {
            val me = currentUserOrThrow()
            val existing = serviceRepository.getServiceById(service.id).getOrThrow()

            if (existing.professionalRef?.id != me.uid)
                throw SecurityException("Não autorizado")

            serviceRepository.updateService(service).getOrThrow()
        }

    override suspend fun getServiceById(id: String): Result<Service> = 
        serviceRepository.getServiceById(id)

    override suspend fun deleteService(id: String): Result<Unit> {
        return try {
            val currentUser = authUseCase.currentUser
                ?: return Result.failure(Exception(ErrorMessages.USER_NOT_AUTHENTICATED))

            val service = serviceRepository.getServiceById(id).getOrNull()
                ?: return Result.failure(Exception("Serviço não encontrado"))

            if (service.professionalRef?.id != currentUser.uid) {
                return Result.failure(Exception("Não autorizado a deletar este serviço"))
            }

            serviceRepository.deleteService(id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getAllServices(): Flow<List<Service>> = 
        serviceRepository.getAllServices()

    override fun getServicesByProfessional(professionalRef: DocumentReference): Flow<List<Service>> = 
        serviceRepository.getServicesByProfessional(professionalRef)

    override fun getServicesByCategory(categoryRef: DocumentReference): Flow<List<Service>> = 
        serviceRepository.getServicesByCategory(categoryRef)

    override suspend fun getProfessionalServices(): Result<List<Service>> = try {
        val currentUser = authUseCase.currentUser
            ?: return Result.failure(Exception(ErrorMessages.USER_NOT_AUTHENTICATED))

        val professionalRef = userRef(currentUser.uid)
        val services = serviceRepository.getServicesByProfessional(professionalRef).first()
        Result.success(services)
    } catch (e: Exception) {
        Result.failure(e)
    }
} 