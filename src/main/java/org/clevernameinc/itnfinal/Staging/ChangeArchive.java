package org.clevernameinc.itnfinal.Staging;

import org.clevernameinc.itnfinal.user.CredentialManager;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.*;
import java.io.*;
import java.util.ArrayList;

///
/// @brief
///     Reads/Writes serialized Change Lists
///     This is primarily for emergency data storage if
///     the database's connection is lost during editing
///
///     This *should* be de/encrypted with the user's password hash
/// @references
///     https://stackoverflow.com/questions/10303767/encrypt-and-decrypt-in-java#34098587
///
public class ChangeArchive {

    private static final String sChangeFileName = "change.db";

    public void write(ArrayList<Change> changeList) {


        try {
            ByteArrayOutputStream bOut = new ByteArrayOutputStream();

            ObjectOutputStream out = new ObjectOutputStream(bOut);

            out.writeObject(changeList);
            Cipher ci = Cipher.getInstance("AES");
            SecretKeySpec secret = CredentialManager.getInstance().getPassKey();

            ci.init(Cipher.ENCRYPT_MODE, secret);

            byte[] encryptedArchive = ci.doFinal(bOut.toByteArray());

            FileOutputStream fout = new FileOutputStream(sChangeFileName);
            fout.write(encryptedArchive);

        } catch(Exception e) {
            failedToReadWriteArchive(e);
        }


    }


    public ArrayList<Change> read() throws Exception {

            /// There are sooo many exceptions. Wrap them up, we handle them the same way anyway

            FileInputStream fin = new FileInputStream(sChangeFileName);

            byte[] encryptedArchive = fin.readAllBytes();

            Cipher ci = Cipher.getInstance("AES");
            SecretKeySpec secret = CredentialManager.getInstance().getPassKey();

            ci.init(Cipher.DECRYPT_MODE, secret);

            byte[] decryptedArchive = ci.doFinal(encryptedArchive);
            ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(decryptedArchive));

        return (ArrayList<Change>)in.readObject();


    }

    private void failedToReadWriteArchive(Exception e) {
        JFrame frame = new JFrame();
        frame.setAlwaysOnTop(true);
        JOptionPane.showMessageDialog(
                frame,
                "Could not read/write changes to archive: " + e.getMessage(),
                "Archive Error",
                JOptionPane.ERROR_MESSAGE
        );
    }


}
