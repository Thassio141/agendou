package br.com.agendou.domain.usecases.appointment

import br.com.agendou.data.repository.AppointmentRepository
import br.com.agendou.domain.models.Appointment
import javax.inject.Inject

class CreateAppointmentUseCase @Inject constructor(
    private val appointmentRepository: AppointmentRepository
) {
    suspend operator fun invoke(appointment: Appointment): Result<Appointment> {
        return appointmentRepository.createAppointment(appointment)
    }
} 