# Assignment 3 Write-up: Meeting Room Scheduler

## Status
Finished on 2026-10-29 by choice. The core logic works and the 12 overlap and business-hours tests pass. Several scenario steps and rejection rules were never wired up (see "Missing requirements").

**Working:**
- Overlap check, reduced to one line: `a.start < b.end && b.start < a.end`. Touching bookings are allowed.
- Business-hours check (08:00–18:00, inclusive at both ends).
- Booking and cancelling a booking.
- Finding the smallest free room (with the capacity bug below).
- Listing a room's free slots (with the sorting bug below).

## What I learned
- **Negate the opposite case.** When there are many ways for something to be true and only a few for it to be false, write the false case and negate it. Overlap went from three `||` cases to one line: two bookings don't overlap only when one ends before the other starts.
- **Unit tests help refine logic.** Writing a test for each overlap shape (front, back, nested, encompassing, identical, touching) made it safe to simplify the condition. The tests confirmed the shorter version right away.
- **Pass plain data around.** Not everything needs to be wrapped in an object first. Extra objects can get in the way.
- **A map would have worked better than a list for rooms.** Looking rooms up by key beats indexing a list with `.ordinal`.
- **Write top-down.** Starting with the broad functions, with placeholder functions for the details, made the program easier to break down and plan.
- **Kotlin specifics:** `for (x in list)`, not `:`. A constructor parameter needs `val` to become a property. `associateWith { value }` vs. `associate { key to value }`. `&&`/`||` short-circuit and `and`/`or` don't, and `and`/`or` bind tighter than comparisons. In `assertEquals`, the expected value comes first. Kotlin's `assert()` isn't JUnit's.

## Bugs (wrong results, not just missing features)
1. **`findSmallestRoom` uses `room.capacity > attendees`.** A 4-person meeting can't get Oak, which holds 4. It should be `>=`.
2. **`getVacancies` assumes bookings are sorted by start time.** Book Oak 14:00–15:00 and then 09:00–10:00, and it reports 08:00–14:00 as free. Sort the bookings by `startTime` before walking them.
3. **`findSmallestRoom` relies on `rooms` already being sorted by capacity**, because `getRooms()` sorts them. That's a hidden dependency. Choose the smallest room inside the function instead, for example `filter { ... }.minByOrNull { it.capacity }`.

## Structure feedback
In order of how much each would help:

1. **One way to name a room.** Right now a room is identified four ways: the `Rooms` enum, `Room` (which copies the enum's name and capacity), `rooms[Rooms.X.ordinal]`, and `Booking.room`, which also gets passed separately to `bookRoom`. The ordinal indexing only works because the enum is declared smallest-first and `getRooms()` sorts by capacity. Adding a room in a different spot would quietly break it.
   *Change:* `RoomScheduler` owns a `Map<String, Room>` keyed by name. Public methods take the room name and plain values, for example `bookRoom(roomName, start, end, attendees): BookingStatus`, and build the `Booking` themselves. `rooms[roomName]` returning `null` also covers the "room doesn't exist" rule.
2. **Remove `Building`.** It only wraps `RoomScheduler`. Use the scheduler directly. (`public` is the default in Kotlin and can be dropped.)
3. **Hide internals.** `Room.bookings` is a public `var MutableList`, so any code can add to it and skip the rules. Make it `private val` and expose a read-only `List` if needed. `bookingIsAvailable` and `isWithinBusinessHours` are public only so tests can call them. It's better to test through `bookRoom` and its `BookingStatus`, and make the helpers `private`.
4. **Put each piece of logic with its data.** Overlap belongs on `Booking`: `fun overlaps(other: Booking) = startTime < other.endTime && other.startTime < endTime`. Then availability becomes `room.bookings.none { it.overlaps(booking) }`. `RoomScheduler.listVacancies` only forwards to `Room.getVacancies`, so one of them should go.
5. **Logic shouldn't print.** `addVacancy` prints "Vacancy Found." Return data and let `main` print. Without the print, the helper is just `vacancies.add(...)`.

**Smaller items:**
- `BusinessHours` is an enum, but opening and closing time aren't alternatives. Two constants would be simpler.
- Enum names are usually singular: `RoomName` or `RoomType` instead of `Rooms`.
- `getRooms()` can be one line: `Rooms.entries.map { Room(it) }.sortedBy { it.capacity }`.
- Cleanup: unused imports (`java.awt.print.Book`, `Optional`), debug `println`s in the first test, `SetupCheckTest.kt`, and `assertEquals` arguments in the wrong order in most tests.

**Done well:** `bookRoom` returns a `BookingStatus` instead of reporting the same failure in two places, which is the habit from Assignment 2. Using the `Booking` itself (data-class equality) as the cancel key is safe here, because the overlap rule prevents two identical bookings in one room.

## Missing requirements
- Scenario requests 2–8 are created in `main` but never booked, and `main` doesn't print each step's result.
- Rejection reasons not implemented: unknown room (`NonexistentRoom`), end not after start (`InvalidStartEndTime`), over capacity (`OverCapacity`). The enum values exist but nothing returns them.
- Rooms are requested by enum, not by name string, so "Birch" can't be requested.
- Cancelling a booking that doesn't exist isn't reported.
- Tests don't yet cover each rejection reason, cancel-then-rebook, find-room, or free slots.

## Expected scenario results (for reference)
| # | Result |
|---|---|
| 1 | Booked |
| 2 | Rejected: overlaps #1 |
| 3 | Booked (touches #1) |
| 4 | Rejected: end not after start |
| 5 | Rejected: over capacity |
| 6 | Rejected: outside business hours |
| 7 | Rejected: unknown room |
| 8 | Booked |
| 9 | Cancelled |
| 10 | Booked (touches #3) |
| 11 | Cedar (Oak too small, Maple busy 08:00–12:00) |
| 12 | Oak free: 08:00–09:30, 11:00–18:00 |
