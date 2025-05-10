package br.com.agendou.domain.usecases.appointment

import br.com.agendou.data.repository.AppointmentRepository
import javax.inject.Inject

class DeleteAppointmentUseCase @Inject constructor(
    private val appointmentRepository: AppointmentRepository
) {
    suspend operator fun invoke(id: String): Result<Unit> {
        return appointmentRepository.deleteAppointment(id)
    }
} 