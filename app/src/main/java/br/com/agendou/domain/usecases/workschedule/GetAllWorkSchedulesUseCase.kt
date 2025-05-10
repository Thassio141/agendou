package br.com.agendou.domain.usecases.workschedule

import br.com.agendou.data.repository.WorkScheduleRepository
import br.com.agendou.domain.models.WorkSchedule
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllWorkSchedulesUseCase @Inject constructor(
    private val workScheduleRepository: WorkScheduleRepository
) {
    operator fun invoke(): Flow<List<WorkSchedule>> {
        return workScheduleRepository.getAllWorkSchedules()
    }
} 