package br.com.agendou.domain.usecases.appointment

import br.com.agendou.data.repository.AppointmentRepository
import br.com.agendou.domain.models.Appointment
import com.google.firebase.firestore.DocumentReference
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAppointmentsByServiceUseCase @Inject constructor(
    private val appointmentRepository: AppointmentRepository
) {
    operator fun invoke(serviceRef: DocumentReference): Flow<List<Appointment>> {
        return appointmentRepository.getAppointmentsByService(serviceRef)
    }
} 