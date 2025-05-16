package br.com.agendou.domain.models

import br.com.agendou.domain.enums.UserType
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
data class User(
    @get:Exclude
    var id: String = "",
    var name: String = "",
    var email: String = "",
    var type: UserType = UserType.CLIENT,
    var phone: String? = null,
    var imageUrl: String? = null,
    var rating: Double = 0.0,
    var categoryRef: DocumentReference? = null,
    var createdAt: Timestamp? = null,
    var updatedAt: Timestamp? = null
)