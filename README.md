# Project
IMPORTANT TO NOTE. SERVER TAB AND APPLICATION TAB MUST BE OPEN CONCURRENTLY TO HAVE THE PROGRAM WORK 
Disclaimer: application requires a MySQL local instance connection and Java 8 or above set up. 

Steps for someone to install our application:
IMPORTANT TO NOTE: SERVER WINDOW AND CLIENT APPLICATION WINDOW MUST BE OPEN CONCURRENTLY TO HAVE THE PROGRAM WORK. MAKE SURE MYSQL IS OPEN WITH A CONNECTION RUNNING.
1. Unzip Project-main.zip and open project in IntelliJ (or have Server.jar and Client.jar available)
2. Server Setup
    2.1. Open Server.jar file
    Alternate Method (skip to 2.4 if using 2.1): 
    2.2. Open “java/csi2136/project/Server.java” in IntelliJ
    2.3. Run static main method
    2.4. Setting up server settings
        2.4.1. Replace “root” with database username
        2.4.2. Replace “db password…” with password associated with database username
        2.4.3. Replace “8888” with the port of the server. Note: The server port should not be the same port as the database. As this will cause network problems and stop the client from connecting to the server.
        2.4.4. Click on connect
        2.4.5. Replace “db name” with the name of database
        2.4.6. Click on “Use >” if database is already set up or “Create >” to set up database from scratch
        2.4.7. Leave server application open to access database, close only when finished working with client application.
3. Client Startup
    3.1. Open Client.jar file
    Alternate Method (skip to 3.3 if using 3.1): 
    3.2. Open “java/csi2136/project/Client.java” in IntelliJ
    3.3 Connect to server
        3.3.1. Replace “8888” with port settings in 2.4.3
4. Register in user
    4.1. After Client start up click on “register”
    4.2. Replace “username…” with name of unique username
    4.3. Replace “password…” with password. Note: The passwords are hashed and therefore can’t be retrieved from the database. Therefore the password should be something easy to remember or written down somewhere.
    4.4. Replace “insurance” with any value
    4.5. Click on basic and select user type
5. Logining
    5.1. Replace “username…” with the username of an account. Note: Since the database prefills the tables with users and appointments. Therefore, use “ShaunFrancis”,   “johndoe” and “thomasjones”  to login as a patient, dentist and manager respectively.  
    5.2. Replace “password…” with the password of the account. Note: The passwords “patient” and “employee” belong to the predefined users that are either a patient or employee respectively. So to access other features, i.e. manage appointments, etc, login to the premade accounts of the staff a client books with.
6. Creating an appointment
    6.1. Create dependent/self
        6.1.1. Click on the plus on the top left
        6.1.2. Replace “First Name…” with first name. Note: Field cannot be blank
        6.1.3. Replace “Middle Name…” with middle name. Note: Field cannot be blank
        6.1.4. Replace “Last Name…” with last name. Note: Field cannot be blank
        6.1.5. Replace “SSN…” with SSN name. Note: Field cannot be blank
        6.1.6. Replace “Date of Birth…” with date of birth in the format “yyyy-dd-mm”. Note: Field cannot be blank
        6.1.7. Click on the drop down below “Date of Birth…” text box and select gender. Note: Field cannot be blank
        6.1.8. Replace “0” with street number. Note: Field cannot be blank
        6.1.9. Replace “Street…” with street name. Note: Field cannot be blank
        6.1.10. Replace “City…” with city name. Note: Field cannot be blank
        6.1.11. Click on the drop down below the “City…” text field and select province. Note: Field cannot be blank
        6.1.12. Replace “Email…” with user email. Note: Field cannot be blank
        6.1.13. Replace “Phone Number…” with phone number. Note: Field cannot be blank
        6.1.14. Click on Save button
    6.2. Create Appointment
        6.2.1. Select User by clicking on tab of client window
        6.2.2. Click “New…”
        6.2.3. Click on first dropdown and select branch for 
        6.2.4. Click on second dropdown and select dentist
        6.2.5. Replace “Date…” with date in the format YYYY-DD-MM 
        6.2.6. Replace “Start Time…” with start time in the format HH:MM:SS
        6.2.7. Replace “End Time…” with start time in the format HH:MM:SS
        6.2.8. Click on third drop down a select treatment
        6.2.9. Click on Book to book an appointment
7. Adding Treatments and Invoice
    7.1. Login as Dentist who has been selected in an appointment
    7.2. Click on the Appointments tab. Note: Clicking on the tab updates the page and is essential inorder to access all visible appointments
    7.3. Scroll to appointment that needs to be edited
    7.4. Change the text fields on the left to update treatment, affected teeth, medication and appointment comments
    7.5. Change the number fields on the left to update insurance, patient, discount and penalty charges
    7.6. Click on the drop down on the right to select the status of the appointment
    7.7. Click on “Save” to update appointment

Built-in Logins:
To log into built in employees (whose employee ID is non-null), enter username from user table in MySQL, and “employee” as password.
-Example Manager:
	Username: thomasjones
	Password: employee
-Example Dentist:
	Username: johndoe
	Password: employee
To log into built in patients (whose employee ID is null), enter username from user table in MySQL, and “patient” as password.
-Example Patient:
	Username: ShaunFrancis
	Password: patient
