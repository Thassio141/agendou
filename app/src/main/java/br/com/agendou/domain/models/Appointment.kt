package br.com.agendou.domain.models

import br.com.agendou.domain.enums.AppointmentStatus
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.Exclude

data class Appointment (
    @get:Exclude
    var id: String = "",
    var clientRef: DocumentReference? = null,
    var professionalRef: DocumentReference? = null,
    var serviceRef: DocumentReference? = null,
    var startAt: Timestamp? = null,
    var appointmentStatus: AppointmentStatus = AppointmentStatus.SCHEDULED,
    var notes: String? = null,
    var ratingGiven: Boolean = false,
    var createdAt: Timestamp? = null,
    var updatedAt: Timestamp? = null
)