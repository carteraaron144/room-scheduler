package scheduler

import java.awt.print.Book
import java.time.LocalTime
import java.util.Optional

class RoomScheduler(var rooms: List<Room>) {

    fun bookRoom(room: Room, booking: Booking) : BookingStatus {
        if (!isWithinBusinessHours(booking)) {
            return BookingStatus.OutOfBusinessHours
        }
        if (!bookingIsAvailable(room, booking)) {
            return BookingStatus.OverlappingTime
        }
        // is booking within a v

        room.addBooking(booking)
        return BookingStatus.Success
    }

    fun cancelBooking(room: Room, booking: Booking) {
        if (room.bookingExists(booking)) {
            room.removeBooking(booking)
        }
    }

    fun findSmallestRoom(startTime: LocalTime, endTime: LocalTime, attendees: Int) : Room? {
        for (room in rooms) {
            if (room.capacity > attendees) {
                if (bookingIsAvailable(room, Booking(room.roomType, startTime, endTime, attendees))){
                    return room
                }
            }
        }
        return null
    }

    fun listVacancies(room: Room) : List<Vacancy> {
        return room.getVacancies()
    }

    fun isWithinBusinessHours(booking: Booking): Boolean {
        return booking.startTime >= BusinessHours.Opening.time && booking.endTime <= BusinessHours.Closing.time
    }

    fun bookingIsAvailable(room: Room, booking: Booking) : Boolean {
        for (roomBooking in room.bookings) {
            if (booking.startTime < roomBooking.endTime && roomBooking.startTime < booking.endTime) {
                return false
            }
        }
        return true
    }
}