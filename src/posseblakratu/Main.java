/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package posseblakratu;

import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import posseblakratu.view.FrameLogin;
import posseblakratu.view.FrameMain;

/**
 *
 * @author Al
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        try {
            UIManager.put("ComboBox.selectionBackground", new java.awt.Color(252, 232, 230)); //mengatur warna selection background di combo box
            UIManager.put("ComboBox.selectionForeground", new java.awt.Color(0, 0, 0)); //mengatur warna teks / foreground di selection combo box
            UIManager.put("Button.arc", 10); //membuat button melengkung
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (UnsupportedLookAndFeelException unsupportedLookAndFeelException) {
        }

        java.awt.EventQueue.invokeLater(() -> {

            //cek sesi tersimpan sebelum menampilkan apapun
            if (FrameLogin.muatSesi()) {
                //jika sesi valid, langsung buka FrameMain tanpa menampilkan form login
                new FrameMain().setVisible(true);
            } else {
                //jika tidak ada sesi, tampilkan form login seperti biasa
                new FrameLogin().setVisible(true);
            }
        });
    }

}
