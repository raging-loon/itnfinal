///
/// @author Conner Macolley
/// @class ITN264
/// @class-section 201
/// @date 11/24/2024
/// @assignment Final
/// @references
///
///     https://stackoverflow.com/questions/1318347/how-to-use-java-property-files
///     https://docs.oracle.com/javase/8/javafx/api/javafx/scene/control/TableColumn.html
///     https://stackoverflow.com/questions/29090583/javafx-tableview-how-to-get-cells-data
///
package org.clevernameinc.itnfinal;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.clevernameinc.itnfinal.db.DatabaseManager;
import org.clevernameinc.itnfinal.db.ProductLoader;
import org.clevernameinc.itnfinal.forms.LoginForm;
import org.clevernameinc.itnfinal.user.CredentialManager;
import org.clevernameinc.itnfinal.user.User;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.SQLException;
import java.util.ArrayList;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws IOException {


        ArrayList<Product> productList = null;

        try {
            DatabaseManager.getInstance().initialize();
            productList = ProductLoader.loadProducts(DatabaseManager.getInstance().createStatement());
        } catch(IOException e) { // thrown by DatabaseManager c'tor
            System.out.println("Failed to open config file! "+ e.getMessage());
            System.exit(1);
        } catch(SQLException e) { // thrown by DatabaseManager or ProductLoader
            System.out.println("Failed to connect to database! " + e.getMessage());
            System.exit(1);
        }

        User user;
        if((user = doLogin()) == null)
            System.exit(1);

        try {
            updateCredManager(user);
            /// this syntax             vvv      was shown to me by Intellij
        } catch(NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource("main-view.fxml"));
        Scene scene = new Scene(loader.load());

        Controller controller = loader.getController();

        controller.loadProductList(productList);

        stage.setScene(scene);
        stage.show();

        /// finalize after the user can see the UI
        controller.finalizeController();

        stage.setOnCloseRequest(e -> {System.exit(0);});
    }

    public static void main(String[] args) {
        launch();
    }

    private static User doLogin() throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("forms/login-form.fxml"));
        Scene scene = new Scene(loader.load());
        stage.setScene(scene);
        stage.showAndWait();

        LoginForm lf = loader.getController();

        if(!lf.wasLoginSuccessful())
            return null;

        return lf.constructUser();
    }

    private static void updateCredManager(User u) throws NoSuchAlgorithmException, InvalidKeySpecException {
        CredentialManager cm = CredentialManager.getInstance();
        cm.setUser(u.getUsername());
        cm.setPasskey(u.getPasswordHash());
    }
}