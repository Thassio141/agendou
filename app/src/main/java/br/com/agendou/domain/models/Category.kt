package br.com.agendou.domain.models

import com.google.firebase.Timestamp
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
data class Category(
    @get:Exclude
    var id: String = "",
    var name: String = "",
    var createdAt: Timestamp? = null
)