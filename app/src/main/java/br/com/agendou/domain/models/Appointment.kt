package br.com.agendou.domain.models

import br.com.agendou.domain.enums.AppointmentStatus
import java.sql.Timestamp

data class Appointment (
    val id: String,
    val clientId: String,
    val professionalId: String,
    val serviceId: String,
    val startAt: Timestamp,
    val appointmentStatus: AppointmentStatus = AppointmentStatus.SCHEDULED,
    val notes: String? = null,
    val ratingGiven : Boolean? = null,
    val createdAt : Timestamp,
    val updatedAt : Timestamp
)