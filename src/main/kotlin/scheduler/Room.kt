package scheduler

import java.time.LocalTime

class Room(val roomType: Rooms) {
    val name: String = roomType.name
    val capacity: Int = roomType.capacity
    var bookings: MutableList<Booking> = mutableListOf<Booking>()

    fun addBooking(booking: Booking) {
        bookings.add(booking)
    }

    fun removeBooking(booking: Booking) {
        bookings.remove(booking)
    }

    fun bookingExists(booking: Booking) : Boolean {
        return bookings.contains(booking)
    }

    fun getVacancies(): List<Vacancy> {
        var vacancies = mutableListOf<Vacancy>()
        var vacancyStartTime: LocalTime = BusinessHours.Opening.time
        var vacancyEndTime: LocalTime

        for (booking in bookings) {
            if (booking.startTime > vacancyStartTime) {
                vacancyEndTime = booking.startTime
                addVacancy(vacancies, vacancyStartTime, vacancyEndTime)
            }
            vacancyStartTime = booking.endTime
        }
        if (vacancyStartTime < BusinessHours.Closing.time) {
            addVacancy(vacancies, vacancyStartTime, BusinessHours.Closing.time)
        }
        return vacancies
    }

    fun addVacancy(vacancies: MutableList<Vacancy>, startTime: LocalTime, endTime: LocalTime) {
        vacancies.add(Vacancy(startTime, endTime))
        println("Vacancy Found: $startTime to $endTime")
    }

    override fun toString() : String {
        return(name)
    }
}