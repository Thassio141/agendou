package br.com.agendou.domain.models

import br.com.agendou.domain.enums.DayOfWeek
import java.sql.Timestamp

data class WorkSchedule(
    val id: String,
    val idProfessional : String,
    val dayOfWeek : DayOfWeek,
    val startAt : Int,
    val endAt : Int,
    val isFreeDay : Boolean,
    val exceptions : List<Timestamp>?= null,
    val createdAt : Timestamp,
    val updatedAt : Timestamp
)