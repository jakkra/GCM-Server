package se.jakobkrantz.Server.database;/*
 * Created by krantz on 14-12-09.
 */

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Database {
    private final String dbPapth = "jdbc:sqlite:" + System.getProperty("user.home") + "/TransitSQLiteDB.db";
    private static Database instance;
    private static String TABLE_NAME = "REGISTRATED_USERS";
    private static String COLUMN_ID = "id";
    private static String COLUMN_REG_ID = "regid";

    private static String COLUMN_TIME_REGISTRATED = "timeRegistrated";

    private Database() throws ClassNotFoundException {
        System.out.println(dbPapth);
        Connection connection = null;
        Class.forName("org.sqlite.JDBC");
        try {
            // create a database connection
            connection = DriverManager.getConnection(dbPapth);
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(20);  // set timeout to 30 sec.

            String createTable = "create table " + TABLE_NAME +
                    " (" + COLUMN_ID + " INTEGER PRIMARY KEY not NULL ," +
                    " " + COLUMN_REG_ID + " TEXT unique," +
                    " " + COLUMN_TIME_REGISTRATED + " DATE DEFAULT (datetime('now','localtime')))";


            statement.executeUpdate(createTable);

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                System.err.println(e);
            }
        }
    }

    public static Database getInstance() {
        if (instance == null) {
            throw new IllegalStateException("You have to prepare the Database first");
        }
        return instance;
    }

    public static Database prepareDatabase() throws ClassNotFoundException {
        synchronized (Database.class) {
            if (instance == null) {
                instance = new Database();
            }
        }
        return instance;
    }

    public void register(String from) {
        executeStatement("insert into " + TABLE_NAME + " values (null," + "\'" + from + "\'," + "datetime('now','localtime'))");

    }

    //TODO Check
    public List<String> getAllRegistrated() {
        Connection connection = null;
        List<String> allRegIds = new ArrayList<String>();
        try {
            connection = DriverManager.getConnection(dbPapth);

            Statement statement = connection.createStatement();
            statement.setQueryTimeout(20);  // set timeout to 20 sec.

            ResultSet rs = statement.executeQuery("select * from " + TABLE_NAME);

            while (rs.next()) {

                if (rs.getString(COLUMN_REG_ID) != null) {
                    allRegIds.add(rs.getString(COLUMN_REG_ID));
                }
            }
            rs.close();
        } catch (Exception e) {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println(e.getMessage());
        } finally {
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e);
            }
        }
        return allRegIds;
    }

    private void executeStatement(String command) {

        System.out.println("Executing command: " + command);
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(dbPapth);

            Statement statement = connection.createStatement();
            statement.setQueryTimeout(20);  // set timeout to 30 sec.

            statement.executeUpdate(command);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                System.err.println(e);
            }
        }
    }
}

