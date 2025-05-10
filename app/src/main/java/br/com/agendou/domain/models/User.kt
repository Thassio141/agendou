package br.com.agendou.domain.models

import br.com.agendou.domain.enums.UserType
import java.sql.Timestamp

data class User(
    val id: String,
    val name: String,
    val email: String,
    val password: String,
    val type : UserType = UserType.CLIENT,
    val phone: String? = null,
    val imageUrl: String?= null,
    val rating : Double = 0.0,
    val category : Category?= null,
    val createdAt : Timestamp,
    val updatedAt : Timestamp
)