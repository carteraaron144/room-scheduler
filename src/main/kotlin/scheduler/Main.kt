package scheduler

import java.time.LocalTime

fun main() {
    //val rooms: Map<Rooms, Room> = Rooms.entries.associateWith { it -> Room(it.name, it.capacity) }
    val rooms: List<Room> = getRooms()

    val b: Building = Building(rooms)

    //1	Book Oak, 09:00–10:00, 3 people
    val booking1 = Booking(Rooms.Oak, LocalTime.of(9,0), LocalTime.of(10, 0), 3)
    b.scheduler.bookRoom(rooms[Rooms.Oak.ordinal], booking1)

    //2	Book Oak, 09:30–10:30, 2 people
    val booking2 = Booking(Rooms.Oak, LocalTime.of(9,30), LocalTime.of(10, 30), 2)


    //3	Book Oak, 10:00–11:00, 4 people
    val booking3 = Booking(Rooms.Oak, LocalTime.of(10,0), LocalTime.of(11, 0), 4)


    //4	Book Maple, 13:00–12:00, 5 people
    val booking4 = Booking(Rooms.Maple, LocalTime.of(13,0), LocalTime.of(12, 0), 5)


    //5	Book Oak, 14:00–15:00, 6 people
    val booking5 = Booking(Rooms.Oak, LocalTime.of(14,0), LocalTime.of(15, 0), 6)

    //6	Book Cedar, 17:30–18:30, 10 people
    val booking6 = Booking(Rooms.Cedar, LocalTime.of(17,30), LocalTime.of(18, 30), 10)

    //7	Book Birch, 11:00–12:00, 2 people
    //val booking7 = Booking(Rooms.Birch, LocalTime.of(11,0), LocalTime.of(12, 0), 2)

    //8	Book Maple, 08:00–12:00, 8 people
    val booking8 = Booking(Rooms.Maple, LocalTime.of(8,0), LocalTime.of(12, 0), 8)

    //9	Cancel the booking from request 1
    b.scheduler.cancelBooking(rooms[Rooms.Oak.ordinal], booking1)

    //10	Book Oak, 09:30–10:00, 2 people
    val booking10 = Booking(Rooms.Oak, LocalTime.of(9,30), LocalTime.of(10,0), 2)
    b.scheduler.bookRoom(rooms[Rooms.Oak.ordinal], booking10)

    //11	Find a room for 09:00–10:00, 5 people
    println(b.scheduler.findSmallestRoom(LocalTime.of(9,0), LocalTime.of(10,0), 5))

    //12	List free slots for Oak
    val oak = b.scheduler.listVacancies(rooms[Rooms.Oak.ordinal])
    println("Oak vacancies:");
    printList(oak)

}

fun printList(list: List<Vacancy>) {
    for (v in list) {
        println("Vacancies: ${v.startTime} - ${v.endTime}")
    }
}

fun getRooms() : List<Room> {
    var rooms: MutableList<Room> = mutableListOf()
    for (rm in Rooms.entries) rooms.add(Room(rm))
    return rooms.sortedBy {it.capacity}
}