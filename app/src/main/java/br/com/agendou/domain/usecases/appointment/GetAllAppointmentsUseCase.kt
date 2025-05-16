package br.com.agendou.domain.usecases.appointment

import br.com.agendou.data.repository.AppointmentRepository
import br.com.agendou.domain.models.Appointment
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllAppointmentsUseCase @Inject constructor(
    private val appointmentRepository: AppointmentRepository
) {
    operator fun invoke(): Flow<List<Appointment>> {
        return appointmentRepository.getAllAppointments()
    }
} 