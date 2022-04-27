# Deliverable 4
## By: Kyle Kryza, Emily Halva, Matt Cummings, Josue Hernandez, Kaylor Davis

### Screencast

[Deliverable 4 Video](https://youtu.be/yWjvIIy_3lk)

### How do run

Our project runs using a build environment called Gradle. Call 'gradle run' (like in video) to run the software.

### Two SQL select queries

#### [1] Find the amount of items which have a minimum bid above 15, grouped by their respective lot number.

SELECT COUNT(Minimum_Bid), Lot_Number
FROM item
WHERE Minimum_Bid > 15
GROUP BY Lot_Number
HAVING COUNT(Minimum_Bid) > 0;

#### [2] Find the first and last name of a bidder on an invoice along with the amount of their payment

SELECT Payment, Customer_First_Name, Customer_Last_Name
FROM INVOICE;

### Update Query

#### [1] Update facility name based on id (example)

UPDATE FACILITY
SET Company_Name = "MKEKJ Co."
WHERE Facility_ID = 0; 