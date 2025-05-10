package br.com.agendou.domain.enums

enum class DayOfWeek {
    MONDAY(1),
    TUESDAY(2),
    WEDNESDAY(3),
    THURSDAY(4),
    FRIDAY(5),
    SATURDAY(6),
    SUNDAY(7);

    companion object {
        fun fromInt(value: Int): DayOfWeek? {
            return DayOfWeek.entries.find { it.value == value }
        }
    }

    val value: Int

    constructor(value: Int) {
        this.value = value
    }
}