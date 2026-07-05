/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package posseblakratu;

import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import posseblakratu.view.FrameLogin;

/**
 *
 * @author Al
 */
public class POSSeblakRatu {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        try {
            UIManager.put("ComboBox.selectionBackground", new java.awt.Color(252, 232, 230)); //memgatur warna selection backround di combo box
            UIManager.put("ComboBox.selectionForeground", new java.awt.Color(0, 0, 0)); //mengatur warna teks / foreground di selection combo box
            UIManager.put("Button.arc", 10); //membaut button melengkung
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (UnsupportedLookAndFeelException unsupportedLookAndFeelException) {
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new FrameLogin().setVisible(true));
    }
    
}
