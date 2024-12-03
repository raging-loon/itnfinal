package org.clevernameinc.itnfinal.Staging;

import org.clevernameinc.itnfinal.db.DatabaseManager;
import org.clevernameinc.itnfinal.forms.MessageBox;

import java.util.ArrayList;

public class ConnectionValidator implements Runnable {

    private final ArrayList<Change> changeList;

    public ConnectionValidator(ArrayList<Change> c) {
        changeList = c;
    }

    public void run() {
        while(true) {
            try {
                Thread.sleep(1000);
            } catch(IllegalArgumentException e) {
                // do nothing
            } catch(InterruptedException e) {
                // do nothing... still
            }

            if(!DatabaseManager.getInstance().isConnectionValid()) {


                /// We cannot rely on JavaFX in a multithreaded environment
                MessageBox.HeadlessError("Database Connection has been lost!", "Database error");

                if(changeList.isEmpty())
                    return;
                MessageBox.HeadlessInfo("Your changes will be written to an encrypted archive for you to correct when you can reconnect to the server", "Database error");

                ChangeArchive ca = new ChangeArchive();
                ca.write(changeList);
                return;
            }

        }
    }
}
