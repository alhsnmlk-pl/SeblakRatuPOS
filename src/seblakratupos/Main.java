/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package seblakratupos;

import javax.swing.*;
import com.formdev.flatlaf.FlatLightLaf;
import seblakratupos.view.LoginFrame;

/**
 *
 * @author Al
 */
public class Main {

    public static void main(String[] args) throws UnsupportedLookAndFeelException {

        UIManager.setLookAndFeel(new FlatLightLaf());

        SwingUtilities.invokeLater(() -> {
            new LoginFrame().setVisible(true);
        });
    }
}
