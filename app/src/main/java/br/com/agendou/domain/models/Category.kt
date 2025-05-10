package br.com.agendou.domain.models

import java.sql.Timestamp

data class Category(
    val id: String,
    val name: String,
    val createdAt: Timestamp
)