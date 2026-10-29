package scheduler

import java.time.LocalTime

class Building(rooms: List<Room>) {
    public val scheduler = RoomScheduler(rooms)
}