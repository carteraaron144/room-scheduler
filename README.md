# Assignment 3: Meeting Room Scheduler

## Scenario
An office has three meeting rooms. Write a Kotlin program that books rooms, cancels bookings, finds a free room for a meeting, and lists a room's free time. All bookings are on a single day.

This is a classic interview problem. The core of it is one question: **when do two time ranges overlap?** Get that right and most of the rest falls into place.

## Rooms
Hard-code these in your program:

| Room | Capacity |
|---|---|
| Oak | 4 |
| Maple | 8 |
| Cedar | 12 |

Business hours are **08:00 to 18:00**.

## Requirements
Callers identify a room by its **name, as a string** (for example `"Oak"`). Names are case-sensitive. How you store rooms internally is up to you.

1. **Book a room** for a start time, end time, and number of attendees. A booking is **rejected**, with a reason, if:
   - the room doesn't exist
   - the end time isn't after the start time
   - any part of it is outside business hours
   - there are more attendees than the room's capacity
   - it overlaps an existing booking in the same room

   Bookings that only touch are **not** overlapping: 10:00–11:00 and 11:00–12:00 can both be booked in the same room.
2. **Cancel a booking.** After that, its time is free again. Cancelling a booking that doesn't exist should be reported, not crash.
3. **Find a room** for a time range and number of attendees. Return the **smallest** room that fits everyone and is free for the whole range, or report that none is available.
4. **List free slots** for a room: every gap between 08:00 and 18:00 that has no booking, in time order. Back-to-back bookings leave no gap between them.
5. **Tests are required.** At minimum, cover: overlap vs. touching, each rejection reason, cancel-then-rebook, find-room choosing the smallest fit, and free slots with back-to-back bookings.
6. `main` runs the scenario below and prints what happened at each step.

## Scenario to run
Run these in order:

| # | Request |
|---|---|
| 1 | Book Oak, 09:00–10:00, 3 people |
| 2 | Book Oak, 09:30–10:30, 2 people |
| 3 | Book Oak, 10:00–11:00, 4 people |
| 4 | Book Maple, 13:00–12:00, 5 people |
| 5 | Book Oak, 14:00–15:00, 6 people |
| 6 | Book Cedar, 17:30–18:30, 10 people |
| 7 | Book Birch, 11:00–12:00, 2 people |
| 8 | Book Maple, 08:00–12:00, 8 people |
| 9 | Cancel the booking from request 1 |
| 10 | Book Oak, 09:30–10:00, 2 people |
| 11 | Find a room for 09:00–10:00, 5 people |
| 12 | List free slots for Oak |

## Constraints
- Plain Kotlin and JUnit only. No files to read, no database, no frameworks.
- Use `java.time.LocalTime` for times. You don't need dates.
- Keep it small: a few classes at most. If you're designing layers or interfaces, stop and ask whether the problem needs them.

## Before you code
1. Work out by hand what happens at each of the 12 requests, including the exact answers to 11 and 12. That's your test oracle.
2. Write down, in one line, the condition for "booking A overlaps booking B." Check it against touching bookings and against one booking fully inside another.
3. Describe your design: how you represent a booking, how a booking is identified so it can be cancelled, and how `book` tells its caller it succeeded or which rule it broke.

## Concepts this exercises
- Interval logic and boundary conditions
- Sorting and searching collections
- Choosing how to report success vs. failure
- Testing edge cases on purpose

## Stretch goals (optional)
- Book a meeting across several rooms' free time: "find the earliest 1-hour slot in any room that fits 6 people."
- Support more than one day.
