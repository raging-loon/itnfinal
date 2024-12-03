package org.clevernameinc.itnfinal.forms;

import javafx.scene.control.Alert;

import javax.swing.*;

public class MessageBox {

    private MessageBox () {}

    public static void Error(String message) {
        new Alert(Alert.AlertType.ERROR, message).showAndWait();
    }

    public static void Warning(String message) {
        new Alert(Alert.AlertType.WARNING, message).showAndWait();
    }

    public static void Info(String message) {
        new Alert(Alert.AlertType.INFORMATION, message).showAndWait();
    }

    public static void HeadlessError(String message, String title) {
        JFrame frame = new JFrame();
        frame.setAlwaysOnTop(true);
        JOptionPane.showMessageDialog(frame, message, title, JOptionPane.ERROR_MESSAGE);
    }

    public static void HeadlessInfo(String message, String title) {
        JFrame frame = new JFrame();
        frame.setAlwaysOnTop(true);
        JOptionPane.showMessageDialog(frame, message, title, JOptionPane.INFORMATION_MESSAGE);
    }
}
