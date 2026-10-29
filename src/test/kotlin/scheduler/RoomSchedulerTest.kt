package scheduler

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.time.LocalTime

class RoomSchedulerTest {
    var rooms = getRooms()
    val b: Building = Building(rooms)

    @Test
    fun `bookingIsAvailable() correctly returns true`() {
        val booking1 = Booking(Rooms.Oak, LocalTime.of(9,0), LocalTime.of(10, 0), 3)
        b.scheduler.bookRoom(rooms[Rooms.Oak.ordinal], booking1)

        println(LocalTime.of(9,0) < LocalTime.of(9, 0))
        println(LocalTime.of(9,0) > LocalTime.of(9,0))

        val booking2 = Booking(Rooms.Oak, LocalTime.of(10,0), LocalTime.of(11, 0), 3)

        assertEquals(true, b.scheduler.bookingIsAvailable(rooms[Rooms.Oak.ordinal], booking2))
    }

    @Test
    fun `bookingIsAvailable() returns false on overlap front of existing booking`() {
        val booking1 = Booking(Rooms.Oak, LocalTime.of(10,30), LocalTime.of(11, 30), 3)
        b.scheduler.bookRoom(rooms[Rooms.Oak.ordinal], booking1)

        val booking2 = Booking(Rooms.Oak, LocalTime.of(10,0), LocalTime.of(11, 0), 3)
        assertEquals(b.scheduler.bookingIsAvailable(rooms[Rooms.Oak.ordinal], booking2), false)
    }

    @Test
    fun `bookingIsAvailable() returns false on overlap rear of existing booking`() {
        val booking1 = Booking(Rooms.Oak, LocalTime.of(9,0), LocalTime.of(10, 0), 3)
        b.scheduler.bookRoom(rooms[Rooms.Oak.ordinal], booking1)

        val booking2 = Booking(Rooms.Oak, LocalTime.of(9,30), LocalTime.of(11, 0), 3)
        assertEquals(b.scheduler.bookingIsAvailable(rooms[Rooms.Oak.ordinal], booking2), false)
    }

    @Test
    fun `bookingIsAvailable() returns false on fully nested in existing booking`() {
        val booking1 = Booking(Rooms.Oak, LocalTime.of(9,0), LocalTime.of(10, 0), 3)
        b.scheduler.bookRoom(rooms[Rooms.Oak.ordinal], booking1)

        val booking2 = Booking(Rooms.Oak, LocalTime.of(9,30), LocalTime.of(9, 45), 3)
        assertEquals(b.scheduler.bookingIsAvailable(rooms[Rooms.Oak.ordinal], booking2), false)
    }

    @Test
    fun `bookingIsAvailable() returns false on fully encompassing existing booking`() {
        val booking1 = Booking(Rooms.Oak, LocalTime.of(9,0), LocalTime.of(10, 0), 3)
        b.scheduler.bookRoom(rooms[Rooms.Oak.ordinal], booking1)

        val booking2 = Booking(Rooms.Oak, LocalTime.of(8,30), LocalTime.of(10, 45), 3)
        assertEquals(b.scheduler.bookingIsAvailable(rooms[Rooms.Oak.ordinal], booking2), false)
    }

    @Test
    fun `bookingIsAvailable() returns false on equal to existing booking`() {
        val booking1 = Booking(Rooms.Oak, LocalTime.of(9,0), LocalTime.of(10, 0), 3)
        b.scheduler.bookRoom(rooms[Rooms.Oak.ordinal], booking1)

        val booking2 = Booking(Rooms.Oak, LocalTime.of(9,0), LocalTime.of(10, 0), 3)
        assertEquals(b.scheduler.bookingIsAvailable(rooms[Rooms.Oak.ordinal], booking2), false)
    }

    @Test
    fun `bookingIsAvailable() returns false on partial overlap rear booking`() {
        val booking1 = Booking(Rooms.Oak, LocalTime.of(9,0), LocalTime.of(10, 0), 3)
        b.scheduler.bookRoom(rooms[Rooms.Oak.ordinal], booking1)

        val booking2 = Booking(Rooms.Oak, LocalTime.of(9,0), LocalTime.of(10, 1), 3)
        assertEquals(b.scheduler.bookingIsAvailable(rooms[Rooms.Oak.ordinal], booking2), false)
    }

    @Test
    fun `bookingIsAvailable() returns false on partial nesting rear booking`() {
        val booking1 = Booking(Rooms.Oak, LocalTime.of(9,0), LocalTime.of(10, 0), 3)
        b.scheduler.bookRoom(rooms[Rooms.Oak.ordinal], booking1)

        val booking2 = Booking(Rooms.Oak, LocalTime.of(9,0), LocalTime.of(9, 59), 3)
        assertEquals(b.scheduler.bookingIsAvailable(rooms[Rooms.Oak.ordinal], booking2), false)
    }

    @Test
    fun `bookingIsAvailable() returns false on partial overlap front booking`() {
        val booking1 = Booking(Rooms.Oak, LocalTime.of(9,0), LocalTime.of(10, 0), 3)
        b.scheduler.bookRoom(rooms[Rooms.Oak.ordinal], booking1)

        val booking2 = Booking(Rooms.Oak, LocalTime.of(8,59), LocalTime.of(10, 0), 3)
        assertEquals(b.scheduler.bookingIsAvailable(rooms[Rooms.Oak.ordinal], booking2), false)
    }

    @Test
    fun `bookingIsAvailable() returns false on partial nesting front booking`() {
        val booking1 = Booking(Rooms.Oak, LocalTime.of(9,0), LocalTime.of(10, 0), 3)
        b.scheduler.bookRoom(rooms[Rooms.Oak.ordinal], booking1)

        val booking2 = Booking(Rooms.Oak, LocalTime.of(9,1), LocalTime.of(10, 0), 3)
        assertEquals(b.scheduler.bookingIsAvailable(rooms[Rooms.Oak.ordinal], booking2), false)
    }
    @Test
    fun `bookingIsAvailable() returns false on fully equal booking`() {
        val booking1 = Booking(Rooms.Oak, LocalTime.of(9,0), LocalTime.of(10, 0), 3)
        b.scheduler.bookRoom(rooms[Rooms.Oak.ordinal], booking1)

        val booking2 = Booking(Rooms.Oak, LocalTime.of(9,0), LocalTime.of(10, 0), 3)
        assertEquals(b.scheduler.bookingIsAvailable(rooms[Rooms.Oak.ordinal], booking2), false)
    }

    @Test
    fun `isWithinBusinessHours() returns true on 8_00 to 12_00`() {
        val booking1 = Booking(Rooms.Oak, LocalTime.of(8,0), LocalTime.of(10, 0), 3)

        assertEquals(b.scheduler.isWithinBusinessHours(booking1), true)
    }
}