package br.com.agendou.domain.models

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
data class Service(
    @get:Exclude
    var id: String = "",
    var professionalRef: DocumentReference? = null,
    var duration: Int = 0,
    var name: String = "",
    var description: String? = null,
    var price: Double = 0.0,
    var active: Boolean = true,
    var createdAt: Timestamp? = null,
    var updatedAt: Timestamp? = null
)