package scheduler

import java.time.LocalTime

enum class BusinessHours(val time: LocalTime) {
    Opening(LocalTime.of(8,0)),
    Closing(LocalTime.of(18,0))
}