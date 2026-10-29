package scheduler

import java.time.LocalTime

data class Booking(val room: Rooms, val startTime: LocalTime, val endTime: LocalTime, val attendees: Int)