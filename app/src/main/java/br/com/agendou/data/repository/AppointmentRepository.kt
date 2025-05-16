package br.com.agendou.data.repository

import br.com.agendou.domain.models.Appointment
import com.google.firebase.firestore.DocumentReference
import kotlinx.coroutines.flow.Flow

interface AppointmentRepository {
    suspend fun createAppointment(appointment: Appointment): Result<Appointment>
    suspend fun getAppointmentById(id: String): Result<Appointment>
    suspend fun updateAppointment(appointment: Appointment): Result<Appointment>
    suspend fun deleteAppointment(id: String): Result<Unit>
    fun getAllAppointments(): Flow<List<Appointment>>
    fun getAppointmentsByClient(clientRef: DocumentReference): Flow<List<Appointment>>
    fun getAppointmentsByProfessional(professionalRef: DocumentReference): Flow<List<Appointment>>
    fun getAppointmentsByService(serviceRef: DocumentReference): Flow<List<Appointment>>
} 