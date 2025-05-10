package br.com.agendou.domain.usecases.workschedule

import br.com.agendou.data.repository.WorkScheduleRepository
import javax.inject.Inject

class DeleteWorkScheduleUseCase @Inject constructor(
    private val workScheduleRepository: WorkScheduleRepository
) {
    suspend operator fun invoke(id: String): Result<Unit> {
        return workScheduleRepository.deleteWorkSchedule(id)
    }
} 