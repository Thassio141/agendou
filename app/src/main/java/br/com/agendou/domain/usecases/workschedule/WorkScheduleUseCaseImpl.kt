package br.com.agendou.domain.usecases.workschedule

import br.com.agendou.common.ErrorMessages
import br.com.agendou.data.repository.WorkScheduleRepository
import br.com.agendou.domain.models.WorkSchedule
import br.com.agendou.domain.usecases.auth.AuthUseCase
import br.com.agendou.domain.usecases.user.UserUseCase
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WorkScheduleUseCaseImpl @Inject constructor(
    private val workScheduleRepository: WorkScheduleRepository,
    private val authUseCase: AuthUseCase,
    private val userUseCase: UserUseCase,
    private val firestore: FirebaseFirestore
) : WorkScheduleUseCase {

    override suspend fun createWorkSchedule(workSchedule: WorkSchedule): Result<WorkSchedule> {
        return try {
            val user = userUseCase.getCurrentUser().getOrNull()
                ?: return Result.failure(Exception("Perfil de usuário não encontrado"))
            
            val professionalRef = firestore.collection("users").document(user.id)
            val workScheduleWithProfessional = workSchedule.copy(
                professionalRef = professionalRef
            )
            workScheduleRepository.createWorkSchedule(workScheduleWithProfessional)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getWorkScheduleById(id: String): Result<WorkSchedule> {
        return workScheduleRepository.getWorkScheduleById(id)
    }

    override suspend fun updateWorkSchedule(workSchedule: WorkSchedule): Result<WorkSchedule> {
        return try {
            val currentUser = authUseCase.currentUser
                ?: return Result.failure(Exception(ErrorMessages.USER_NOT_AUTHENTICATED))
            
            val existingWorkSchedule = workScheduleRepository.getWorkScheduleById(workSchedule.id).getOrNull()
                ?: return Result.failure(Exception("Horário de trabalho não encontrado"))
            
            if (existingWorkSchedule.professionalRef?.id != currentUser.uid) {
                return Result.failure(Exception("Não autorizado a atualizar este horário de trabalho"))
            }
            
            workScheduleRepository.updateWorkSchedule(workSchedule)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteWorkSchedule(id: String): Result<Unit> {
        return try {
            val currentUser = authUseCase.currentUser
                ?: return Result.failure(Exception(ErrorMessages.USER_NOT_AUTHENTICATED))
            
            val workSchedule = workScheduleRepository.getWorkScheduleById(id).getOrNull()
                ?: return Result.failure(Exception("Horário de trabalho não encontrado"))
            
            if (workSchedule.professionalRef?.id != currentUser.uid) {
                return Result.failure(Exception("Não autorizado a deletar este horário de trabalho"))
            }
            
            workScheduleRepository.deleteWorkSchedule(id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getAllWorkSchedules(): Flow<List<WorkSchedule>> {
        return workScheduleRepository.getAllWorkSchedules()
    }

    override fun getWorkSchedulesByProfessional(professionalRef: DocumentReference): Flow<List<WorkSchedule>> {
        return workScheduleRepository.getWorkSchedulesByProfessional(professionalRef)
    }

    override suspend fun getProfessionalWorkSchedules(): Result<List<WorkSchedule>> {
        return try {
            val currentUser = authUseCase.currentUser
                ?: return Result.failure(Exception(ErrorMessages.USER_NOT_AUTHENTICATED))
            
            val professionalRef = firestore.collection("users").document(currentUser.uid)
            val workSchedules = workScheduleRepository.getWorkSchedulesByProfessional(professionalRef).first()
            Result.success(workSchedules)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
} 