package seblakratupos.view.component;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;

public class ToggleCustom extends JToggleButton {

    private Color onColor = new Color(255, 111, 0);
    private Color offColor = new Color(90, 90, 90);
    private Color thumbColor = Color.WHITE;

    private Icon onIcon;
    private Icon offIcon;

    private int borderRadius = 30;
    private int padding = 4;

    public ToggleCustom() {
        setPreferredSize(new Dimension(55, 30));
        setMinimumSize(new Dimension(45, 25));
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setOpaque(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
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

        int thumbSize = height - (padding * 2);
        int thumbX;

        if (isSelected()) {
            g2.setColor(onColor);
            thumbX = width - thumbSize - padding;
        } else {
            g2.setColor(offColor);
            thumbX = padding;
        }

        // background toggle
        g2.fillRoundRect(0, 0, width, height, borderRadius, borderRadius);

        // lingkaran tombol
        g2.setColor(thumbColor);
        g2.fillOval(thumbX, padding, thumbSize, thumbSize);

        // gambar icon sesuai kondisi ON / OFF
        Icon currentIcon = isSelected() ? onIcon : offIcon;

        if (currentIcon != null) {
            int iconX = thumbX + (thumbSize - currentIcon.getIconWidth()) / 2;
            int iconY = padding + (thumbSize - currentIcon.getIconHeight()) / 2;
            currentIcon.paintIcon(this, g2, iconX, iconY);
        }

        g2.dispose();
    }

    // =========================
    // GETTER SETTER WARNA
    // =========================

    public Color getOnColor() {
        return onColor;
    }

    public void setOnColor(Color onColor) {
        this.onColor = onColor;
        repaint();
    }

    public Color getOffColor() {
        return offColor;
    }

    public void setOffColor(Color offColor) {
        this.offColor = offColor;
        repaint();
    }

    public Color getThumbColor() {
        return thumbColor;
    }

    public void setThumbColor(Color thumbColor) {
        this.thumbColor = thumbColor;
        repaint();
    }

    public int getBorderRadius() {
        return borderRadius;
    }

    public void setBorderRadius(int borderRadius) {
        this.borderRadius = borderRadius;
        repaint();
    }

    public int getPadding() {
        return padding;
    }

    public void setPadding(int padding) {
        this.padding = padding;
        repaint();
    }

    // =========================
    // GETTER SETTER ICON
    // =========================

    public Icon getOnIcon() {
        return onIcon;
    }

    public void setOnIcon(Icon onIcon) {
        this.onIcon = onIcon;
        repaint();
    }

    public Icon getOffIcon() {
        return offIcon;
    }

    public void setOffIcon(Icon offIcon) {
        this.offIcon = offIcon;
        repaint();
    }

    // =========================
    // BIAR VALUE BOOLEAN BISA DIBACA
    // =========================

    public boolean isOn() {
        return isSelected();
    }

    public void setOn(boolean on) {
        setSelected(on);
        repaint();
    }
}