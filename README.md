# Project
IMPORTANT TO NOTE. SERVER TAB AND APPLICATION TAB MUST BE OPEN CONCURRENTLY TO HAVE THE PROGRAM WORK 


DEFAULT USERS

For localhost database set up please follow these instructions:

Step 1: Launch MySQL Step 3: Return to MySQL, in the upper navigator bars select Database > Connect To Database

Step 2: After connecting to your local database create a new scheme with whatever name you wish.

Step 3: After creating a new scheme at the bottom of the schemas column open the Administration tab

Step 4: Launch the DBConnectionData.java file in intelij or equivalent

Step 5: In the DBConnectionData.java file insert the data as instructed.

    Step 5.1: host = "localhost"
    
    Step 5.2: Click on the Server Status option in the MYSql Admin tab (navigation to admin can be found in Step 3) and set 
    port in DBConnectionData.java = {port from the Server Status MySQL workbench menu}
    
    Step 5.3: Set name = {schema name you made in step 2}

    Step 5.4: Set userName = root

    Step 5.5: Set password = whatever password you set when creating your MySQL Database

Step 6: Navigate to the DBHandler.java class and run ONLY the main method, this will set up the database in your MySQL
work bench and you will be ready to begin using the program.



