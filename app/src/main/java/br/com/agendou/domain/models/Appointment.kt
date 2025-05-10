package br.com.agendou.domain.models

import br.com.agendou.domain.enums.AppointmentStatus
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference

data class Appointment (
    val id: String,
    val clientRef: DocumentReference,
    val professionalRef: DocumentReference,
    val serviceRef: DocumentReference,
    val startAt: Timestamp,
    val appointmentStatus: AppointmentStatus = AppointmentStatus.SCHEDULED,
    val notes: String? = null,
    val ratingGiven : Boolean = false,
    val createdAt : Timestamp,
    val updatedAt : Timestamp
)