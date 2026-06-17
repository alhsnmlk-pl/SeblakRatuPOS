/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package seblakratupos.view.component;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.awt.geom.Ellipse2D;

/**
 *
 * @author Acer
 */
public class ToggleCustomD extends JToggleButton {

    private Color colorOn = new Color(234, 89, 11);
    private Color colorOff = new Color(115, 115, 115);
    private Color thumbColor = Color.WHITE;

    public ToggleCustomD() {
        setOpaque(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setPreferredSize(new Dimension(53, 32));
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                setSelected(!isSelected());
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();
        int arc = height;
        g2.setColor(isSelected() ? colorOn : colorOff);
        g2.fill(new RoundRectangle2D.Double(0, 0, width - 1, height - 1, arc, arc));
        int thumbDiameter = height - 6;
        int thumbY = 3;
        int thumbX = isSelected() ? (width - thumbDiameter - 4) : 4;
        g2.setColor(new Color(0, 0, 0, 30));
        g2.fill(new Ellipse2D.Double(thumbX, thumbY + 1, thumbDiameter, thumbDiameter));
        g2.setColor(thumbColor);
        g2.fill(new Ellipse2D.Double(thumbX, thumbY, thumbDiameter, thumbDiameter));
        g2.dispose();
    }
}
