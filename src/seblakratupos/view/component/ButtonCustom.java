package seblakratupos.view.component;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.Path2D;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.border.EmptyBorder;

public class ButtonCustom extends JButton {

    private int roundTopLeft = 15;
    private int roundTopRight = 15;
    private int roundBottomLeft = 15;
    private int roundBottomRight = 15;

    private int borderThickness = 0;

    private Color borderColor = new Color(180, 180, 180);
    private Color hoverBorderColor = new Color(160, 160, 160);
    private Color pressedBorderColor = new Color(130, 130, 130);
    private Color disabledBorderColor = new Color(200, 200, 200);

    private Color hoverBackground = new Color(230, 230, 230);
    private Color pressedBackground = new Color(210, 210, 210);
    private Color disabledBackground = new Color(220, 220, 220);

    private Color hoverForeground = null;
    private Color pressedForeground = null;
    private Color disabledForeground = new Color(150, 150, 150);

    private int paddingTop = 5;
    private int paddingLeft = 10;
    private int paddingBottom = 5;
    private int paddingRight = 10;

    private int iconWidth = 0;
    private int iconHeight = 0;
    private Icon originalIcon;

    private boolean handCursor = true;

    public ButtonCustom() {
        setOpaque(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setRolloverEnabled(true);

        setBackground(new Color(255, 255, 255));
        setForeground(Color.BLACK);

        updatePadding();
        updateCursor();
    }

    public int getRoundTopLeft() {
        return roundTopLeft;
    }

    public void setRoundTopLeft(int roundTopLeft) {
        this.roundTopLeft = Math.max(0, roundTopLeft);
        repaint();
    }

    public int getRoundTopRight() {
        return roundTopRight;
    }

    public void setRoundTopRight(int roundTopRight) {
        this.roundTopRight = Math.max(0, roundTopRight);
        repaint();
    }

    public int getRoundBottomLeft() {
        return roundBottomLeft;
    }

    public void setRoundBottomLeft(int roundBottomLeft) {
        this.roundBottomLeft = Math.max(0, roundBottomLeft);
        repaint();
    }

    public int getRoundBottomRight() {
        return roundBottomRight;
    }

    public void setRoundBottomRight(int roundBottomRight) {
        this.roundBottomRight = Math.max(0, roundBottomRight);
        repaint();
    }

    public int getBorderThickness() {
        return borderThickness;
    }

    public void setBorderThickness(int borderThickness) {
        this.borderThickness = Math.max(0, borderThickness);
        repaint();
    }

    public Color getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(Color borderColor) {
        this.borderColor = borderColor == null ? Color.BLACK : borderColor;
        repaint();
    }

    public Color getHoverBorderColor() {
        return hoverBorderColor;
    }

    public void setHoverBorderColor(Color hoverBorderColor) {
        this.hoverBorderColor = hoverBorderColor == null ? borderColor : hoverBorderColor;
        repaint();
    }

    public Color getPressedBorderColor() {
        return pressedBorderColor;
    }

    public void setPressedBorderColor(Color pressedBorderColor) {
        this.pressedBorderColor = pressedBorderColor == null ? borderColor : pressedBorderColor;
        repaint();
    }

    public Color getDisabledBorderColor() {
        return disabledBorderColor;
    }

    public void setDisabledBorderColor(Color disabledBorderColor) {
        this.disabledBorderColor = disabledBorderColor == null ? borderColor : disabledBorderColor;
        repaint();
    }

    public Color getHoverBackground() {
        return hoverBackground;
    }

    public void setHoverBackground(Color hoverBackground) {
        this.hoverBackground = hoverBackground == null ? getBackground() : hoverBackground;
        repaint();
    }

    public Color getPressedBackground() {
        return pressedBackground;
    }

    public void setPressedBackground(Color pressedBackground) {
        this.pressedBackground = pressedBackground == null ? getBackground() : pressedBackground;
        repaint();
    }

    public Color getDisabledBackground() {
        return disabledBackground;
    }

    public void setDisabledBackground(Color disabledBackground) {
        this.disabledBackground = disabledBackground == null ? getBackground() : disabledBackground;
        repaint();
    }

    public Color getHoverForeground() {
        return hoverForeground;
    }

    public void setHoverForeground(Color hoverForeground) {
        this.hoverForeground = hoverForeground;
        repaint();
    }

    public Color getPressedForeground() {
        return pressedForeground;
    }

    public void setPressedForeground(Color pressedForeground) {
        this.pressedForeground = pressedForeground;
        repaint();
    }

    public Color getDisabledForeground() {
        return disabledForeground;
    }

    public void setDisabledForeground(Color disabledForeground) {
        this.disabledForeground = disabledForeground == null ? Color.GRAY : disabledForeground;
        repaint();
    }

    public int getPaddingTop() {
        return paddingTop;
    }

    public void setPaddingTop(int paddingTop) {
        this.paddingTop = Math.max(0, paddingTop);
        updatePadding();
    }

    public int getPaddingLeft() {
        return paddingLeft;
    }

    public void setPaddingLeft(int paddingLeft) {
        this.paddingLeft = Math.max(0, paddingLeft);
        updatePadding();
    }

    public int getPaddingBottom() {
        return paddingBottom;
    }

    public void setPaddingBottom(int paddingBottom) {
        this.paddingBottom = Math.max(0, paddingBottom);
        updatePadding();
    }

    public int getPaddingRight() {
        return paddingRight;
    }

    public void setPaddingRight(int paddingRight) {
        this.paddingRight = Math.max(0, paddingRight);
        updatePadding();
    }

    public int getIconWidth() {
        return iconWidth;
    }

    public void setIconWidth(int iconWidth) {
        this.iconWidth = Math.max(0, iconWidth);
        updateIconSize();
    }

    public int getIconHeight() {
        return iconHeight;
    }

    public void setIconHeight(int iconHeight) {
        this.iconHeight = Math.max(0, iconHeight);
        updateIconSize();
    }

    public boolean isHandCursor() {
        return handCursor;
    }

    public void setHandCursor(boolean handCursor) {
        this.handCursor = handCursor;
        updateCursor();
    }

    @Override
    public void setIcon(Icon icon) {
        this.originalIcon = icon;
        updateIconSize();
    }

    @Override
    protected void paintComponent(Graphics graphic) {
        Graphics2D g2 = (Graphics2D) graphic.create();

        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );

        int width = getWidth();
        int height = getHeight();

        int thickness = Math.min(borderThickness, Math.min(width, height));
        double inset = thickness / 2.0;

        Shape shape = createCustomShape(
                inset,
                inset,
                width - thickness,
                height - thickness
        );

        g2.setColor(getCurrentBackground());
        g2.fill(shape);

        if (borderThickness > 0) {
            g2.setColor(getCurrentBorderColor());
            g2.setStroke(new BasicStroke(
                    borderThickness,
                    BasicStroke.CAP_ROUND,
                    BasicStroke.JOIN_ROUND
            ));
            g2.draw(shape);
        }

        g2.dispose();

        Color oldForeground = getForeground();
        Color currentForeground = getCurrentForeground();

        if (currentForeground != null) {
            super.setForeground(currentForeground);
        }

        super.paintComponent(graphic);

        super.setForeground(oldForeground);
    }

    private Color getCurrentBackground() {
        if (!isEnabled()) {
            return disabledBackground;
        }

        if (getModel().isPressed()) {
            return pressedBackground;
        }

        if (getModel().isRollover()) {
            return hoverBackground;
        }

        return getBackground();
    }

    private Color getCurrentBorderColor() {
        if (!isEnabled()) {
            return disabledBorderColor;
        }

        if (getModel().isPressed()) {
            return pressedBorderColor;
        }

        if (getModel().isRollover()) {
            return hoverBorderColor;
        }

        return borderColor;
    }

    private Color getCurrentForeground() {
        if (!isEnabled()) {
            return disabledForeground;
        }

        if (getModel().isPressed() && pressedForeground != null) {
            return pressedForeground;
        }

        if (getModel().isRollover() && hoverForeground != null) {
            return hoverForeground;
        }

        return getForeground();
    }

    private Shape createCustomShape(double x, double y, double width, double height) {
        double tl = Math.min(roundTopLeft, Math.min(width, height));
        double tr = Math.min(roundTopRight, Math.min(width, height));
        double bl = Math.min(roundBottomLeft, Math.min(width, height));
        double br = Math.min(roundBottomRight, Math.min(width, height));

        Path2D path = new Path2D.Double();

        path.moveTo(x + tl, y);

        path.lineTo(x + width - tr, y);
        if (tr > 0) {
            path.quadTo(x + width, y, x + width, y + tr);
        } else {
            path.lineTo(x + width, y);
        }

        path.lineTo(x + width, y + height - br);
        if (br > 0) {
            path.quadTo(x + width, y + height, x + width - br, y + height);
        } else {
            path.lineTo(x + width, y + height);
        }

        path.lineTo(x + bl, y + height);
        if (bl > 0) {
            path.quadTo(x, y + height, x, y + height - bl);
        } else {
            path.lineTo(x, y + height);
        }

        path.lineTo(x, y + tl);
        if (tl > 0) {
            path.quadTo(x, y, x + tl, y);
        } else {
            path.lineTo(x, y);
        }

        path.closePath();

        return path;
    }

    private void updatePadding() {
        setBorder(new EmptyBorder(
                paddingTop,
                paddingLeft,
                paddingBottom,
                paddingRight
        ));
        revalidate();
        repaint();
    }

    private void updateCursor() {
        if (handCursor) {
            setCursor(new Cursor(Cursor.HAND_CURSOR));
        } else {
            setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }
    }

    private void updateIconSize() {
        if (originalIcon == null) {
            super.setIcon(null);
            return;
        }

        if (iconWidth > 0 && iconHeight > 0 && originalIcon instanceof ImageIcon) {
            Image image = ((ImageIcon) originalIcon).getImage();

            Image scaledImage = image.getScaledInstance(
                    iconWidth,
                    iconHeight,
                    Image.SCALE_SMOOTH
            );

            super.setIcon(new ImageIcon(scaledImage));
        } else {
            super.setIcon(originalIcon);
        }

        revalidate();
        repaint();
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        repaint();
    }

    @Override
    public void setAlignmentX(float alignmentX) {
        super.setAlignmentX(alignmentX);
    }

    @Override
    public void setAlignmentY(float alignmentY) {
        super.setAlignmentY(alignmentY);
    }

    @Override
    public boolean contains(int x, int y) {
        Shape shape = createCustomShape(
                0,
                0,
                getWidth(),
                getHeight()
        );

        return shape.contains(x, y);
    }

    @Override
    public Component.BaselineResizeBehavior getBaselineResizeBehavior() {
        return super.getBaselineResizeBehavior();
    }
}