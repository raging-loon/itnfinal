package org.clevernameinc.itnfinal.db;


import java.io.IOException;
import java.sql.*;

///
/// @brief
///     Manages the connection to the database and provides an interface
///     through which statements can be constructed
///
///     This class also allows you to create/rollback transactions via
///     enableAutoCommit and rollback
///
public class DatabaseManager {

    /// Singleton boilerplate
    private static final DatabaseManager sInstance = new DatabaseManager();

    public static DatabaseManager getInstance() { return sInstance; }

    private DatabaseManager() {}

    private Connection mConnection;

    ///
    /// @brief
    ///     Creates a connection
    ///
    public void initialize() throws SQLTimeoutException, SQLException, IOException {
        ConnectionPropertyLoader mCpl = new ConnectionPropertyLoader("itndb.properties");

        mConnection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/ITN261", mCpl.getUsername(), mCpl.getPassword());


    }

    public Statement createStatement() throws SQLException{
        return mConnection.createStatement();
    }

    ///
    /// @brief
    ///     Turn On/Off transactions
    ///
    /// @todo: This is NOT thread-safe
    ///
    public void enableAutoCommit(boolean e) throws SQLException {
        mConnection.setAutoCommit(e);
    }

    public void rollback() throws SQLException {
        mConnection.rollback();
    }

    public boolean isConnectionValid() {
        try {
            return mConnection.isValid(1);
        } catch(Exception e) {
            // The exception is if the timeout is less than zero
            // We will ignore that because it's hardcoded
            return false;
        }
    }

}
