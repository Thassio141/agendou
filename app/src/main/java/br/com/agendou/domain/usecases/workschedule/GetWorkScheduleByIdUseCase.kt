package br.com.agendou.domain.usecases.workschedule

import br.com.agendou.data.repository.WorkScheduleRepository
import br.com.agendou.domain.models.WorkSchedule
import javax.inject.Inject

class GetWorkScheduleByIdUseCase @Inject constructor(
    private val workScheduleRepository: WorkScheduleRepository
) {
    suspend operator fun invoke(id: String): Result<WorkSchedule> {
        return workScheduleRepository.getWorkScheduleById(id)
    }
} 