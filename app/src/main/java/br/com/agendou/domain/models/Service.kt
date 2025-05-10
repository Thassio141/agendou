package br.com.agendou.domain.models

import java.sql.Timestamp

data class Service (
    val id: String,
    val idProfessional : String,
    val duration: Int,
    val name: String,
    val description: String?= null,
    val price: Double,
    val active : Boolean = true,
    val createdAt : Timestamp,
    val updatedAt : Timestamp
)