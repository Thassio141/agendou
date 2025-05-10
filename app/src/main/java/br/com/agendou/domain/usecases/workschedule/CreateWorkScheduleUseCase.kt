package br.com.agendou.domain.usecases.workschedule

import br.com.agendou.data.repository.WorkScheduleRepository
import br.com.agendou.domain.models.WorkSchedule
import javax.inject.Inject

class CreateWorkScheduleUseCase @Inject constructor(
    private val workScheduleRepository: WorkScheduleRepository
) {
    suspend operator fun invoke(workSchedule: WorkSchedule): Result<WorkSchedule> {
        return workScheduleRepository.createWorkSchedule(workSchedule)
    }
} 