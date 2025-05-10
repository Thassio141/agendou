package br.com.agendou.domain.models

import br.com.agendou.domain.enums.DayOfWeek
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
data class WorkSchedule(
    var id: String = "",
    var professionalRef: DocumentReference? = null,
    var dayOfWeek: DayOfWeek = DayOfWeek.MONDAY,
    var startAt: Int = 0,
    var endAt: Int = 0,
    var isFreeDay: Boolean = false,
    var exceptions: List<Timestamp> = emptyList(),
    var createdAt: Timestamp? = null,
    var updatedAt: Timestamp? = null
)