package br.com.agendou.data.repository

import br.com.agendou.domain.models.WorkSchedule
import com.google.firebase.firestore.DocumentReference
import kotlinx.coroutines.flow.Flow

interface WorkScheduleRepository {
    suspend fun createWorkSchedule(workSchedule: WorkSchedule): Result<WorkSchedule>
    suspend fun getWorkScheduleById(id: String): Result<WorkSchedule>
    suspend fun updateWorkSchedule(workSchedule: WorkSchedule): Result<WorkSchedule>
    suspend fun deleteWorkSchedule(id: String): Result<Unit>
    fun getAllWorkSchedules(): Flow<List<WorkSchedule>>
    fun getWorkSchedulesByProfessional(professionalRef: DocumentReference): Flow<List<WorkSchedule>>
} 