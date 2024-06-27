## Introduction:
Badminton Court Booking Platform is a platform designed to streamline the management and booking of sports facilities specific to badminton. The system serves the following parties: guests, customers, court administrators, court staff, and system administrators, providing a seamless and efficient experience for all users. This project aims to improve the accessibility, convenience and efficiency of scheduling, thereby improving user satisfaction and operational management.

##Major Features:
1. User Account Management
2. Venue Search and Information
3. Booking
4. Payment
5. Check-in
6. Venue Management
7. Booking Management
8. Reporting and Analystic

##Technology Stack::
1. Backend: Spring Boot, MySQL, Spring Security
2. Frontend: React.js
3. Payment Integration: MoMo or VNPay

##Link RO_Mapping: https://docs.google.com/spreadsheets/d/1qQMCpwugaEjk_rlcOgAMKcqxJcEkXljYg6GCDU6MwSg/edit?usp=sharing

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
| UC-14           | Rate the Venue                                             |
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
| UC-28           | Staff find a court to check-in for customers               |
| UC-31           | Manager add court information                              |
| UC-31           | Manager generate reports                                   |
| UC-32           | Manager delete court                                       |
| UC-33           | Manager register booking type information                  |
| UC-34           | Manager handles walk-in booking                            |
| UC-35           | Reset Password                                             |

# Sprint Planning Table
## Sprint 1 (Set up)
| Use Case ID | Use Case Description                      |
|-------------|-------------------------------------------|
| UC-01       | Register for an Account                   |
| UC-05       | Login                                     |
| UC-06       | Logout                                    |
| UC-15       | Customer Forgot Password                  |
| UC-25.a     | Create account for staff and manager      |
| UC-25.b     | Update account information                |
| UC-25.c     | Delete account                            |
| UC-25.d     | Lock/Unlock account                       |
| UC-19       | Manager registers venue information       |
| UC-23.a     | Add payment information to the venue      |
| UC-20       | Manager updates venue information         |
| UC-26       | System admin registers new venue          |
| UC-3        | Manager deletes court                     |
| UC-21.a     | Create a time slot and set the price      |
| UC-33       | Manager register booking type information |
| UC-35       | Reset Password                            |


## Sprint 2 (Business Flow)

| Use Case ID | Use Case Description                            |
|-------------|-------------------------------------------------|
| UC-02       | Search Venues                                   |
| UC-03       | View Venue Details                              |
| UC-04       | View All Venues                                 |
| UC-07       | View Reviews and Ratings                        |
| UC-08.a     | Book Fixed Schedule                             |
| UC-08.b     | Book Single Day                                 |
| UC-08.c     | Book Flexible Schedule                          |
| UC-09       | Customer Makes a Payment                        |
| UC-10       | View Booking History                            |
| UC-11       | Modify Booking                                  |
| UC-12       | Cancel Booking                                  |
| UC-13       | Customer Check-in at Venue                      |
| UC-16       | Staff Check-in for Customer                     |
| UC-27       | Court Staff Check Court Schedule                |
| UC-28       | Staff Finds a Court for Check-in for Customers  |
| UC-17       | Staff Updates Court Status                      |
| UC-21b      | Update schedule state                           |
| UC-22       | Manager/Staff Can View the Booking List         |
| UC-23       | Manager Confirms Booking                        |
| UC-24       | Manager Handles Booking Cancellation            |
| UC-30       | Manager Handles Walk-in Booking                 |


## Sprint 3 (Report)

## Sprint 4 (Dashboard)

For Sprint 1:
## Back-End (BE)

### Backend Developers

| STT | BE Developer   | Use Cases                                     |
|-----|----------------|-----------------------------------------------|
| 1   | Thịnh | UC-05, UC-19, UC-20, UC-23.a |
| 2   | Ngân | UC-01, UC-15, UC-25.a, UC-25.b, UC-35                     |
| 3   | Trung | UC-21.a, UC-25.c, UC-25.d, UC-33                     |

### Frontend Developers

| STT | FE Developer   | Use Cases                                     |
|-----|----------------|-----------------------------------------------|
| 1   | Nam  | UC-01, UC-05, UC-06, UC-15                           |
| 2   | Son | UC-33, UC-19, UC-20, UC-2 |


For Sprint 2


### Backend Developers

| STT | BE Developer   | Use Cases                                     |
|-----|----------------|-----------------------------------------------|
| 1   | Thịnh | UC-08.a, UC-08.b, UC-08.c, UC-09, UC-10, UC-11, UC-12 |
| 2   | Ngân | UC-13, UC-16, UC-27, UC-28                     |
| 3   | Trung | UC-22, UC-23, UC-24, UC-34                     |

### Frontend Developers

| STT | FE Developer   | Use Cases                                     |
|-----|----------------|-----------------------------------------------|
| 1   |  | UC-02, UC-03, UC-04                           |
| 2   |  | UC-07, UC-08.a, UC-08.b, UC-08.c, UC-09, UC-10, UC-11, UC-12 |

### Notes:
- BE: Backend Developers
- FE: Frontend Developers
- STT: Số thứ tự


For Sprint 3
## BE
## FE

For Sprint 4
## BE
## FE

