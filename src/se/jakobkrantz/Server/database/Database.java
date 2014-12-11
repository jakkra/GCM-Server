package se.jakobkrantz.Server.database;/*
 * Created by krantz on 14-12-09.
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {
    private final String dbPapth = "jdbc:sqlite:" + System.getProperty("user.home") + "/TransitSQLiteDB.db";
    private static Database instance;

    private Database() throws ClassNotFoundException {
        System.out.println(dbPapth);
        Connection connection = null;
        Class.forName("org.sqlite.JDBC");
        try {
            // create a database connection
            connection = DriverManager.getConnection(dbPapth);
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(20);  // set timeout to 30 sec.

            String createTable = "create table REGISTRATED_USERS " +
                    " (id INTEGER PRIMARY KEY not NULL ," +
                    " regId TEXT unique," +
                    " timeRegistrated DATE DEFAULT (datetime('now','localtime')))";


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

    }
}
