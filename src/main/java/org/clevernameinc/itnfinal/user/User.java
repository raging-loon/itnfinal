package org.clevernameinc.itnfinal.user;

public class User {
    private final String mUsername;
    private final String mPasswordHash;

    public User(String user, String pass) {
        mUsername = user;
        mPasswordHash = pass;
    }

    public String getUsername() {
        return mUsername;
    }

    public String getPasswordHash() {
        return mPasswordHash;
    }
}
