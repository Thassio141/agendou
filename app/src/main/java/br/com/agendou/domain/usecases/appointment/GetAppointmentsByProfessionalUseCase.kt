package br.com.agendou.domain.usecases.appointment

import br.com.agendou.data.repository.AppointmentRepository
import br.com.agendou.domain.models.Appointment
import com.google.firebase.firestore.DocumentReference
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAppointmentsByProfessionalUseCase @Inject constructor(
    private val appointmentRepository: AppointmentRepository
) {
    operator fun invoke(professionalRef: DocumentReference): Flow<List<Appointment>> {
        return appointmentRepository.getAppointmentsByProfessional(professionalRef)
    }
} 