package scheduler

enum class BookingStatus {
    Success,
    OverlappingTime,
    NonexistentRoom,
    InvalidStartEndTime,
    OverCapacity,
    OutOfBusinessHours
}