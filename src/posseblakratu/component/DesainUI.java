/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package posseblakratu.component;

import com.formdev.flatlaf.ui.FlatLineBorder;
import java.awt.Color;
import java.awt.Insets;
import javax.swing.JComponent;

/**
 *
 * @author Al
 */


//membuat class panel melengkung agar bisa di panggil dari manapun panel ataupun frame

public class DesainUI {

    private static final String globalColor = "#E7BDBB";
    private static final float globalThickness = 1f;
    private static final int globalArc = 10;
    private static final Insets globalInsets = new Insets(0, 0, 0, 0);

    public static void borderLengkung(JComponent c) {
        c.setBorder(new FlatLineBorder(
                globalInsets,
                Color.decode(globalColor),
                globalThickness,
                globalArc
        ));
    }

    public static void borderLengkung(JComponent c,
                                     Insets insets,
                                     String hex,
                                     float thickness,
                                     int arc) {

        c.setBorder(new FlatLineBorder(
                insets,
                Color.decode(hex),
                thickness,
                arc
        ));
    }
}