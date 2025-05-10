package br.com.agendou.domain.models

import java.sql.Timestamp

data class Reviews (
    val id: String,
    val idProfessional : String,
    val idClient : String,
    val rating: Int,
    val comment: String?= null,
    val createdAt : Timestamp
)
