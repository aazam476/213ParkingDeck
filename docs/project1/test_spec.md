# Test Specification

- **Course:** CS 213 Fall 2026, Project #1 (Parking Management System)
- **Prepared by:** Ali Azam
- **Classes tested:** Date, Timestamp

This document lists the unit test cases for the isValid() method of the Date class and the compareTo() method of the Timestamp class. Each test case is implemented in the testbed main() of its class with the same number and input. When run, the testbed prints the expected and actual result of every case.

Dates are written in the yyyy-MM-dd form that the E and X commands use, and times in 24-hour HH:mm form.

## 1. Date class

**Java class name:** Date  **Method signature:** public boolean isValid()

**Requirement being tested:** the E and X commands must reject a date that is not a valid calendar date. A valid month is from 1 to 12. April, June, September, and November have 30 days. February has 28 days, or 29 in a leap year. All other months have 31 days. A year is a leap year if it is evenly divisible by 4, unless it is evenly divisible by 100 but not by 400.

| Test Case # | Functional Requirement, or Test Objective | Test description and Input Data | Expected result/output |
|:---:|---|---|:---:|
| 1 | The valid range for the month shall be >= 1 and <= 12. The method shall return false for a month value outside the valid range. | Create an instance of Date with a valid year and day but with a month greater than 12.<br>Test input: "2026-13-01" | false |
| 2 | The day of the month shall be at least 1. The method shall return false for a day less than 1. | Create an instance of Date with a valid year and month but with day 0.<br>Test input: "2026-04-00" | false |
| 3 | The number of days in February for a non-leap year shall be 28. The method shall return false if the date given has 29 days for a non-leap year. | Create an instance of Date with month 2 and day 29 in a year that is not evenly divisible by 4, so it is not a leap year.<br>Test input: "2019-02-29" | false |
| 4 | April, June, September, and November shall have 30 days. The method shall return false for day 31 in any of these months. | Create an instance of Date with month 4 (April) and day 31.<br>Test input: "2026-04-31" | false |
| 5 | The method shall return true for a date whose month is from 1 to 12 and whose day is within the number of days in that month. | Create an instance of Date with month 10 (October, 31 days) and day 9.<br>Test input: "2026-10-09" | true |
| 6 | A year evenly divisible by 400 shall be a leap year even though it is also divisible by 100, so February shall have 29 days. The method shall return true for February 29 in such a year. | Create an instance of Date with month 2 and day 29 in the year 2000, which is evenly divisible by 4, 100, and 400.<br>Test input: "2000-02-29" | true |

Implemented as Tests 1 to 6 in the testbed main() of the Date class.

## 2. Timestamp class

**Java class name:** Timestamp  **Method signature:** public int compareTo(Timestamp other)

**Requirement being tested:** the X command must reject an exit time that is before the recorded entry time, and parking history is displayed in descending order of entry timestamp. Both depend on comparing two timestamps by date first, then by hour, then by minute.

In every case, compareTo() is called on the timestamp in the test input, and the reference timestamp 2026-03-09 10:48 is passed as the other timestamp. An expected result of -1, 1, or 0 means the test input is earlier than, later than, or the same as the reference.

| Test Case # | Functional Requirement, or Test Objective | Test description and Input Data | Expected result/output |
|:---:|---|---|:---:|
| 1 | Timestamps shall be compared by date first. The method shall return -1 when this timestamp's date is earlier than the other's, even if its hour and minute are later. | Create a Timestamp on an earlier date, in the previous year, with a later hour and minute than the reference.<br>Test input: "2025-12-31 23:59" compared to "2026-03-09 10:48" | -1 |
| 2 | When the dates are the same, timestamps shall be compared by hour. The method shall return -1 when this timestamp's hour is earlier, even if its minute is later. | Create a Timestamp on the same date as the reference with an earlier hour but a later minute.<br>Test input: "2026-03-09 09:59" compared to "2026-03-09 10:48" | -1 |
| 3 | When the date and hour are the same, timestamps shall be compared by minute. The method shall return -1 when this timestamp's minute is earlier. | Create a Timestamp with the same date and hour as the reference and an earlier minute.<br>Test input: "2026-03-09 10:30" compared to "2026-03-09 10:48" | -1 |
| 4 | Timestamps shall be compared by date first. The method shall return 1 when this timestamp's date is later than the other's, even if its hour and minute are earlier. | Create a Timestamp on the next day at midnight, which is earlier in the day than the reference time.<br>Test input: "2026-03-10 00:00" compared to "2026-03-09 10:48" | 1 |
| 5 | When the dates are the same, timestamps shall be compared by hour. The method shall return 1 when this timestamp's hour is later, even if its minute is earlier. | Create a Timestamp on the same date as the reference with a later hour but an earlier minute.<br>Test input: "2026-03-09 11:00" compared to "2026-03-09 10:48" | 1 |
| 6 | When the date and hour are the same, timestamps shall be compared by minute. The method shall return 1 when this timestamp's minute is later. | Create a Timestamp with the same date and hour as the reference and a later minute.<br>Test input: "2026-03-09 10:50" compared to "2026-03-09 10:48" | 1 |
| 7 | The method shall return 0 when both timestamps have the same date, hour, and minute. | Create a second Timestamp with exactly the same date and time as the reference.<br>Test input: "2026-03-09 10:48" compared to "2026-03-09 10:48" | 0 |

Implemented as Tests 1 to 7 in the testbed main() of the Timestamp class.