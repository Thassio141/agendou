package br.com.agendou.domain.usecases.workschedule

import br.com.agendou.domain.models.WorkSchedule
import com.google.firebase.firestore.DocumentReference
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface WorkScheduleUseCase {
    suspend fun createWorkSchedule(workSchedule: WorkSchedule): Result<WorkSchedule>
    suspend fun getWorkScheduleById(id: String): Result<WorkSchedule>
    suspend fun updateWorkSchedule(workSchedule: WorkSchedule): Result<WorkSchedule>
    suspend fun deleteWorkSchedule(id: String): Result<Unit>
    fun getAllWorkSchedules(): Flow<List<WorkSchedule>>
    fun getWorkSchedulesByProfessional(professionalRef: DocumentReference): Flow<List<WorkSchedule>>
    suspend fun getProfessionalWorkSchedules(): Result<List<WorkSchedule>>
} 