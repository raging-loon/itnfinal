package org.clevernameinc.itnfinal.forms;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.clevernameinc.itnfinal.db.DatabaseManager;
import org.clevernameinc.itnfinal.user.User;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;

///
/// @brief
///     Login Form
///     This will attempt to log in based on the ITN261.users table
///     Once logged in, you can construct a User object
///
public class LoginForm {

    private static final int MAX_PASSWORD_TRIES = 3;

    @FXML
    private TextField mUsernameField;

    @FXML
    private PasswordField mPasswordField;

    @FXML
    private Label mLoginFailLabel;

    private int mNumFails = 0;

    private boolean mLoginSuccessfull = false;

    @FXML
    public void onLoginPressed() {
        // @todo: hacky
        Stage mStage = (Stage) mUsernameField.getScene().getWindow();

        if(mUsernameField.getText().isEmpty()){
            mUsernameField.requestFocus();
            return;
        }

        if(mPasswordField.getText().isEmpty()){
            mPasswordField.requestFocus();
            return;
        }

        if(!doLogin()) {
            mNumFails++;
            mLoginFailLabel.setVisible(true);
            if(mNumFails >= MAX_PASSWORD_TRIES) {
                mStage.close();
            }
        } else {
            mLoginSuccessfull = true;
            mStage.close();
        }
    }

    public boolean wasLoginSuccessful() {
        return mLoginSuccessfull;
    }

    public User constructUser() {
        if(!mLoginSuccessfull)
            return null;
        return new User(mUsernameField.getText(), getSHA256Hash());
    }



    private boolean doLogin() {
        try {
            String stmt = "SELECT u_passwd FROM users WHERE users.u_name = \"" + mUsernameField.getText() + "\"";
            ResultSet res = DatabaseManager.getInstance().createStatement().executeQuery(stmt);

            if(!res.next() || res.wasNull())
                return false;
            return res.getString(1).equals(getSHA256Hash());

        } catch(SQLException e) {
            return false;
        }
    }

    private String getSHA256Hash() {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(mPasswordField.getText().getBytes(StandardCharsets.UTF_8));
            return String.format("%064x", new BigInteger(1, hash));
        } catch(NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
