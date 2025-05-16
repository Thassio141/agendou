package br.com.agendou.domain.models

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
data class Review(
    @get:Exclude
    var id: String = "",
    var appointmentRef: DocumentReference? = null,
    var professionalRef: DocumentReference? = null,
    var clientRef: DocumentReference? = null,
    var rating: Int = 0,
    var comment: String? = null,
    var createdAt: Timestamp? = null
)