package com.miked;

import java.sql.*;
import java.util.Scanner;

public class Cubes {

    //Table and column names
    final static String CUBE_TABLE_NAME = "Cubes";
    final static String CUBE_SOLVER_COL = "Cube_Solver";
    final static String TIME_ELAPSED_COL = "Time_Elapsed";
    //Scanners
    static Scanner stringScanner = new Scanner(System.in);
    static Scanner numberScanner = new Scanner(System.in);

    public static void main(String[] args) {

        DBUtils.registerDriver();
        //first create the table if it doesnt already exist
        createTable();
        //display the table for the user to see if they want to add or update any records
        displayTable();
        //if the user would like to enter a new record give them the option to do so or not
        System.out.println("Do you want to enter a new record? (y or n)");
        String answer = stringScanner.nextLine();
        if (answer.equalsIgnoreCase("y")){
            //if a new entry is required, call insertNewData method
            insertNewData();
        }
        //if the user would like to update a current record give them the option to do so or not
        System.out.println("Do you want to update a current record? (y or n)");
        String answer2 = stringScanner.nextLine();
        if (answer2.equalsIgnoreCase("y")){
            //if an update is required, call updateEntry
            updateEntry();
        }
        //close scanners after all options are exhausted
        stringScanner.close();
        numberScanner.close();
    }
    //this will create the table if it doesnt already exist
    private static void createTable() {
        //
        try (Connection connection = DBUtils.getConnection();
             Statement createTableStatement = connection.createStatement()) {
            //Both record holder and time will be held as a string
            String createTableSQLtemplate = "CREATE TABLE %s (%s VARCHAR(100), %s VARCHAR(100))";
            String createTableSQL = String.format(createTableSQLtemplate, CUBE_TABLE_NAME, CUBE_SOLVER_COL, TIME_ELAPSED_COL);
            System.out.println("The SQL to be executed is: " + createTableSQL);
            //Show SQL statement to be executed for verification and testing
            createTableStatement.execute(createTableSQL);
            //I moved this method in to createTable because it wont keep
            //inserting test data if the table already exists
            insertTestData();
            //verification that table was created
            System.out.println("Created Cubes Table");

            connection.close();
            createTableStatement.close();

        } catch (SQLException sqle) {
            sqle.toString();
        }
    }
    //simple display of data in the table for user to see for spelling
    //or to decide if they want to add or update entries
    private static void displayTable(){

        try (Connection connection = DBUtils.getConnection()){

            Statement statement = connection.createStatement();
            //select all from the cube table
            ResultSet rs = statement.executeQuery("SELECT * FROM "+ CUBE_TABLE_NAME );
            //print each entry
            while(rs.next()){
                System.out.println("Cube Solver: "+ rs.getString(1));
                System.out.println("Time Elapsed: "+ rs.getString(2));
                System.out.println("------------------------");
            }
            //close stuff
            rs.close();
            statement.close();
            connection.close();

        }catch (SQLException sqle){
            System.out.println(sqle.toString());
        }

    }

    private static void insertTestData() {
        //insert test data if the table doesnt exist (only once)
        try (Connection connection = DBUtils.getConnection()) {

            String insertSQL = String.format("INSERT INTO %s(%s, %s) VALUES ( ? , ? ) ", CUBE_TABLE_NAME, CUBE_SOLVER_COL, TIME_ELAPSED_COL);
            System.out.println("Insert data - the statement to execute is  " + insertSQL);
            PreparedStatement insertTestDataStatement = connection.prepareStatement(insertSQL);

            insertTestDataStatement.setString(1, "CubeStormer II Robot");
            insertTestDataStatement.setString(2, "5.270");
            insertTestDataStatement.execute();

            insertTestDataStatement.setString(1, "Fakhri Raihaan (using his feet)");
            insertTestDataStatement.setString(2, "27.93");
            insertTestDataStatement.execute();

            insertTestDataStatement.setString(1, "Ruxin Liu (age 3)");
            insertTestDataStatement.setString(2, "99.33");
            insertTestDataStatement.execute();

            insertTestDataStatement.setString(1, "Mats Valk (human record holder)");
            insertTestDataStatement.setString(2, "6.27");
            insertTestDataStatement.execute();

            insertTestDataStatement.close();
            connection.close();
            //confirmation test data was inserted in to the table
            System.out.println("Added four rows of test data");


        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }

    private static void insertNewData() {
        //variable that will determine if additional entries need to be made
        //after the first
        String answer;
        do {//do-while loop for repeat entries
            //get input from user for adding new records
            System.out.println("Enter name of Cube_Solver");
            String solver = stringScanner.nextLine();
            System.out.println("Enter Time_Elapsed to solve");
            String timeElapsed = stringScanner.nextLine();

            try (Connection connection = DBUtils.getConnection()) {

                String insertSQL = String.format("INSERT INTO %s (%s, %s) VALUES ( ? , ? ) ", CUBE_TABLE_NAME, CUBE_SOLVER_COL, TIME_ELAPSED_COL);
                PreparedStatement insertTestDataStatement = connection.prepareStatement(insertSQL);
                //use user input and add it to table
                insertTestDataStatement.setString(1, solver);
                insertTestDataStatement.setString(2, timeElapsed);
                insertTestDataStatement.execute();
                //confirmation that record was added
                System.out.println("Added new solve record.");

                insertTestDataStatement.close();
                connection.close();

            } catch (SQLException sqle) {
                sqle.printStackTrace();
            }
            //after new record is entered, display the table so user can see it
            displayTable();
            //ask if another entry is necessary
            System.out.println("Would you like to add another new entry?");
            answer = stringScanner.nextLine();
        }while(answer.equalsIgnoreCase("y"));

    }

    private static void updateEntry() {
        //display table for updates so spelling is right or at least close
        displayTable();
        String answer;
        do{//similar to newEntry method
            //User input for updates
              System.out.println("Enter name of Cube_Solver to update");
              String solver = stringScanner.nextLine();
              System.out.println("Enter updated Time_Elapsed to solve");
              String timeElapsed = stringScanner.nextLine();

            try (Connection connection = DBUtils.getConnection()) {
                //statement for updating the table only where the name is similar or correct
                String insertSQL = String.format("UPDATE %s SET %s=? WHERE %s like ?  ", CUBE_TABLE_NAME, TIME_ELAPSED_COL, CUBE_SOLVER_COL);
                PreparedStatement insertTestDataStatement = connection.prepareStatement(insertSQL);

                insertTestDataStatement.setString(1, timeElapsed);
                //the % symbols allow user to type a portion of the name instead of
                //the complete correct spelling
                insertTestDataStatement.setString(2, "%" + solver + "%");
                insertTestDataStatement.execute();

            } catch (SQLException sqle) {
                sqle.printStackTrace();
            }
            System.out.println("Another?");
            answer = stringScanner.nextLine();
        }while(answer.equalsIgnoreCase("y"));
    }
}
//the only additions I think that could be added would be to choose from a list
//of current entries when updating but I dont mind the way this works, and if
//the user tries to update with a name that doesnt match, nothing is added to the
//table.  I used both the primary key example and the car database example as
//references for this project.