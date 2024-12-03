package org.clevernameinc.itnfinal.db;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

///
/// @brief
///     Loads a .properties file containing the user/pass of the service account
///
/// @todo: Should this be moved to the Credential Manager?
/// 
public class ConnectionPropertyLoader {
    private final String mUsername;
    private final String mPassword;

    public ConnectionPropertyLoader(String filename) throws IOException, FileNotFoundException {
        Properties dbcfg = new Properties();
        dbcfg.load(new FileInputStream(filename));

        mUsername = dbcfg.getProperty("username");
        mPassword = dbcfg.getProperty("password");
    }

    public String getUsername() { return mUsername; }
    public String getPassword() { return mPassword; }

}
