Introduction:This project aims to build a convenient and effective online badminton court booking platform for both players and court managers. This platform will help players easily find a course that suits their needs, book a playing schedule, pay online and track booking history. At the same time, the platform also helps yard managers manage yard information, schedules, employees, and revenue and expenditure effectively.

Link RO_Mapping: https://docs.google.com/spreadsheets/d/1qQMCpwugaEjk_rlcOgAMKcqxJcEkXljYg6GCDU6MwSg/edit?usp=sharing

# Use Case List
## Use Cases

| Use Case        | Description                                                |
|-----------------|------------------------------------------------------------|
| UC-01           | Register for an Account                                    |
| UC-02           | Search Venues                                              |
| UC-03           | View venue details                                         |
| UC-04           | View all venues                                            |
| UC-05           | Login                                                      |
| UC-06           | Logout                                                     |
| UC-07           | View reviews and ratings                                   |
| UC-08           | Book a venue                                               |
| UC-08.a         | Book fixed schedule                                        |
| UC-08.b         | Book daily schedule                                        |
| UC-08.c         | Book flexible schedule                                     |
| UC-09           | Customer make a payment                                    |
| UC-10           | View Booking History                                       |
| UC-11           | Modify Booking                                             |
| UC-12           | Cancel Booking                                             |
| UC-13           | Customer Check-in at Venue                                 |
| UC-15           | Customer Forgot Password                                   |
| UC-16           | Staff Check-in for Customer                                |
| UC-17           | Staff updates court status                                 |
| UC-19           | Manager register venue information                         |
| UC-20           | Manager update venue information                           |
| UC-21           | Manager manages schedule                                   |
| UC-21.a         | Create time slot and set the price                         |
| UC-21.b         | Update schedule state                                      |
| UC-22           | Manager/Staff can view booking list                        |
| UC-23           | Manager confirm booking                                    |
| UC-23.a         | Add payment information to the venue                       |
| UC-23.b         | Track transaction                                          |
| UC-24           | Manager handle booking cancellation                        |
| UC-25           | System Admin can manage user accounts                      |
| UC-25.a         | Create account for staff and manager                       |
| UC-25.b         | Update account information                                 |
| UC-25.c         | Delete account                                             |
| UC-25.d         | Lock/Unlock account                                        |
| UC-26           | System admin register new venue (dashboard integration)    |
| UC-27           | System Admin Monitor System Activity                       |
| UC-27           | Court Staff check court schedule                           |
| UC-28           | Staff find a court to check-in for customers                |
| UC-31           | Manager add court information                              |
| UC-31           | Manager generate reports                                   |
| UC-32           | Manager delete court                                       |


# Sprint Planning Table
## Sprint 1 (Set up)

| Sprint  | Use Case ID     | Use Case Description             |
|---------|-----------------|----------------------------------|
| Sprint 1 (Set Up) | UC-01          | Register for an Account          |
|         | UC-05          | Login                            |
|         | UC-06          | Log out                          |
|         | UC-15          | Forgot password                  |
|         | UC-19          | Register Venue Information       |
|         | UC-20          | Update Venue Information         |
|         | UC-21          | Manage Schedule                  |
|         | UC-21.a        | Create TimeSlot                  |                      |
|         | UC-21.b        | Update Schedule state            |
|         | UC-23.a        | Add Payment Information          |
|         | UC-23.b        | Track Transaction                |
|         | UC-25.a        | Create Account                   |
|         | UC-25.b        | Update Account                   |
|         | UC-25.c        | Delete Account                   |
|         | UC-25.d        | Lock/Unlock account              |
|         | UC-26          | Register new Venue               |
|         | UC-27          | Update System Settings           |

## Sprint 2 (Business Flow)

| Sprint  | Use Case ID | Use Case Description                                                                                         |
|---------|-------------|--------------------------------------------------------------------------------------------------------------|
| Sprint 2| UC-08       | Book a venue (Customer: book a venue)                                                                         |
|         | UC-08.a     | Book Fixed Schedule (Customer: book a fixed schedule)                                                         |
|         | UC-08.b     | Book Daily Schedule (Customer: book a daily schedule)                                                         |
|         | UC-08.c     | Book Flexible Schedule (Customer: book a flexible schedule)                                                   |
|         | UC-09       | Make a Payment (Customer: make an online payment when booking)                                                |
|         | UC-13       | Check-in at Venue (Customer: check-in at the venue)                                                           |
|         | UC-16       | Check-in for Customers (Court Staff: check-in customers at the venue)                                         |
|         | UC-22.a     | View Booking List (Court Manager: view the list of bookings)                                                  |
|         | UC-22.b     | Confirm Booking (Court Manager: confirm bookings)                                                             |
|         | UC-22.c     | Handle Booking Cancellation (Court Manager: handle cancellations of bookings)                                 |

## Sprint 3 (Report)

| Sprint  | Use Case ID | Use Case Description                                                                                         |
|---------|-------------|--------------------------------------------------------------------------------------------------------------|
| Sprint 3| UC-21       | Manage Schedule (Court Manager: manage schedule of the venue)                                                |
|         | UC-21.a     | Create TimeSlot (Court Manager: create time slots for booking)                                               |
|         | UC-21.b     | Set Price (Court Manager: set prices for different time slots)                                               |
|         | UC-24       | Check-Court Status (Court Staff: check the status of the courts)                                             |
|         | UC-23.a     | Add Payment Information (Court Manager: add payment information for the venue)                               |
|         | UC-23.b     | Track Transaction (Court Manager: track payment transactions)                                                |
|         | UC-28       | Monitor System Activity (System Admin: monitor activity across the system)                                   |

## Sprint 4 (Dashboard)

| Sprint  | Use Case ID | Use Case Description                                                                                         |
|---------|-------------|--------------------------------------------------------------------------------------------------------------|
| Sprint 4| UC-12       | View the calendar with available time slots (Customer: view available time slots on a calendar)               |
|         | UC-14       | Rate court (Customer: submit a rating for a court)                                                           |
|         | UC-28       | Monitor System Activity (System Admin: monitor activity across the system)                                   |

For Sprint 1:
## Back-End (BE)

| Member | Task |
|------------|----------|
| Ngân       | UC-15, UC-1|
| Thịnh      | UC-5, UC-19|
| Trung      |  UC-21.a|

## Front-End (FE)

| Member | Task |
|------------|----------|
| Sơn        | UC-1, UC-5, UC15|
| Nam        | UC-19, UC21.c, UC-26|

