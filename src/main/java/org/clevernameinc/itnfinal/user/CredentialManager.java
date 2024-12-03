package org.clevernameinc.itnfinal.user;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

public class CredentialManager {


    private String mUsername;
    private SecretKeySpec mPasskey;
    /// Singleton Boilerplate

    private final static CredentialManager sInstance = new CredentialManager();

    private CredentialManager() {
        mPasskey = null;
        mUsername = null;
    }

    public static CredentialManager getInstance() { return sInstance; }


    public void setUser(String username) {
        if(mUsername == null)
            mUsername = username;
    }


    public void setPasskey(String hash) throws NoSuchAlgorithmException, InvalidKeySpecException {
        if(mPasskey == null)
            mPasskey = generatePassKey(hash);
    }

    public String getUsername() {
        return mUsername;
    }

    public SecretKeySpec getPassKey() {
        return mPasskey;
    }

    private SecretKeySpec generatePassKey(String hash) throws NoSuchAlgorithmException, InvalidKeySpecException {
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");

        KeySpec spec = new PBEKeySpec(
                hash.toCharArray(),
                "This is a salt please replace me".getBytes(StandardCharsets.UTF_8),
                65536,
                256
        );

        byte[] rawKey = factory.generateSecret(spec).getEncoded();
        return new SecretKeySpec(rawKey, "AES");
    }

}
