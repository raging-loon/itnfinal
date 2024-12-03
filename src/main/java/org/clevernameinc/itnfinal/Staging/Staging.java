package org.clevernameinc.itnfinal.Staging;

import org.clevernameinc.itnfinal.db.DatabaseManager;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

///
/// @brief
///     Keeps track of changes and allows you to push them to the
///     remote server via UPDATE statements
///
///     This will also start a thread ensuring database connectivity
///     so that in the case where it is broken, a @see ChangeArchive
///     can be made
///
public class Staging {

    private final ArrayList<Change> mChangeList;

    public Staging() {
        mChangeList = new ArrayList<>();

        Thread conval = new Thread(new ConnectionValidator(mChangeList));
        conval.start();
    }

    public void addChange(Change c) {
        mChangeList.add(c);
    }


    ///
    /// @brief
    ///     Push the changes to the server
    ///
    /// @throws SQLException if anything goes wrong
    /// Note, if the SQLException occurs WHILE updating, this function will
    /// catch that exception, rollback all changes, and re throw it for the caller to handle
    ///
    public void pushChanges() throws SQLException{
        DatabaseManager inst = DatabaseManager.getInstance();

        inst.enableAutoCommit(false);

        for(Change c : mChangeList) {

            String stmt = String.format("UPDATE products SET %s = %s WHERE p_id = %d\n",
                c.getFieldName(), c.getNewValue(), c.getPrimaryKey()
            );

            try {
                Statement s = inst.createStatement();
                s.executeUpdate(stmt);
                s.close();
            } catch (SQLException e) {
                inst.rollback();
                /// propagate error upwards
                throw e;
            }

        }

        inst.enableAutoCommit(true);

        mChangeList.clear();
    }

    ///
    /// @brief
    ///     get a const-ref to the change list
    ///
    public final ArrayList<Change> getChangeList() {
        return mChangeList;
    }


    public void clearChangeTable() {
        mChangeList.clear();
    }

    public void removeChange(Change c) {
        mChangeList.remove(c);
    }
}
