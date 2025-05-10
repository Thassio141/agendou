package br.com.agendou.domain.usecases.appointment

import br.com.agendou.domain.models.Appointment
import kotlinx.coroutines.flow.Flow

interface AppointmentUseCase {
    suspend fun createAppointment(appointment: Appointment): Result<Appointment>
    suspend fun getAppointmentById(id: String): Result<Appointment>
    suspend fun updateAppointment(appointment: Appointment): Result<Appointment>
    suspend fun deleteAppointment(id: String): Result<Unit>
    fun getAllAppointments(): Flow<List<Appointment>>
    fun getAppointmentsByClient(clientId: String): Flow<List<Appointment>>
    fun getAppointmentsByProfessional(proId: String): Flow<List<Appointment>>
    fun getAppointmentsByService(serviceId: String): Flow<List<Appointment>>
    suspend fun getClientAppointments(): Result<List<Appointment>>
    suspend fun getProfessionalAppointments(): Result<List<Appointment>>
} 