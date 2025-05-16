package br.com.agendou.domain.usecases.workschedule

import br.com.agendou.data.repository.WorkScheduleRepository
import br.com.agendou.domain.models.WorkSchedule
import com.google.firebase.firestore.DocumentReference
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetWorkSchedulesByProfessionalUseCase @Inject constructor(
    private val workScheduleRepository: WorkScheduleRepository
) {
    operator fun invoke(professionalRef: DocumentReference): Flow<List<WorkSchedule>> {
        return workScheduleRepository.getWorkSchedulesByProfessional(professionalRef)
    }
} 