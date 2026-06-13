package seblakratupos.view.component;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.Area;
import java.awt.geom.Path2D;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.border.EmptyBorder;

public class ButtonCustomV2 extends JButton {

    private int roundTopLeft = 15;
    private int roundTopRight = 15;
    private int roundBottomLeft = 15;
    private int roundBottomRight = 15;

    private int borderThickness = 0;
    private int borderTopThickness = 0;
    private int borderRightThickness = 0;
    private int borderBottomThickness = 0;
    private int borderLeftThickness = 0;

    private boolean active = false;
    private boolean activeOnClick = false;

    private Color inactiveBackground = Color.WHITE;
    private Color inactiveForeground = new Color(150, 150, 150);
    private Color inactiveBorderColor = Color.WHITE;

    private Color activeBackground = new Color(255, 243, 232);
    private Color activeForeground = new Color(230, 120, 30);
    private Color activeBorderColor = new Color(230, 120, 30);

    private Color hoverBackground = new Color(250, 250, 250);
    private Color hoverForeground = new Color(230, 120, 30);
    private Color hoverBorderColor = Color.WHITE;

    private Color activeHoverBackground = new Color(255, 238, 220);
    private Color activeHoverForeground = new Color(230, 120, 30);
    private Color activeHoverBorderColor = new Color(230, 120, 30);

    private Color pressedBackground = new Color(255, 230, 205);
    private Color pressedForeground = new Color(230, 120, 30);
    private Color pressedBorderColor = new Color(230, 120, 30);

    private Color disabledBackground = new Color(230, 230, 230);
    private Color disabledForeground = new Color(160, 160, 160);
    private Color disabledBorderColor = new Color(210, 210, 210);

    private int paddingTop = 5;
    private int paddingLeft = 10;
    private int paddingBottom = 5;
    private int paddingRight = 10;

    private int iconWidth = 0;
    private int iconHeight = 0;

    private Icon inactiveIcon;
    private Icon activeIcon;
    private Icon hoverIcon;
    private Icon activeHoverIcon;
    private Icon pressedIcon;
    private Icon disabledIcon;

    private boolean handCursor = true;

    public ButtonCustomV2() {
        setOpaque(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setRolloverEnabled(true);

        super.setBackground(inactiveBackground);
        super.setForeground(inactiveForeground);

        updatePadding();
        updateCursor();
        refreshVisualState();

        getModel().addChangeListener(e -> {
            refreshVisualState();
            repaint();
        });

        addActionListener(e -> {
            if (activeOnClick) {
                setActive(true);
            }
        });
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
        refreshVisualState();
        repaint();
    }

    public boolean isActiveOnClick() {
        return activeOnClick;
    }

    public void setActiveOnClick(boolean activeOnClick) {
        this.activeOnClick = activeOnClick;
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

        this.borderTopThickness = this.borderThickness;
        this.borderRightThickness = this.borderThickness;
        this.borderBottomThickness = this.borderThickness;
        this.borderLeftThickness = this.borderThickness;

        repaint();
    }

    public int getBorderTopThickness() {
        return borderTopThickness;
    }

    public void setBorderTopThickness(int borderTopThickness) {
        this.borderTopThickness = Math.max(0, borderTopThickness);
        repaint();
    }

    public int getBorderRightThickness() {
        return borderRightThickness;
    }

    public void setBorderRightThickness(int borderRightThickness) {
        this.borderRightThickness = Math.max(0, borderRightThickness);
        repaint();
    }

    public int getBorderBottomThickness() {
        return borderBottomThickness;
    }

    public void setBorderBottomThickness(int borderBottomThickness) {
        this.borderBottomThickness = Math.max(0, borderBottomThickness);
        repaint();
    }

    public int getBorderLeftThickness() {
        return borderLeftThickness;
    }

    public void setBorderLeftThickness(int borderLeftThickness) {
        this.borderLeftThickness = Math.max(0, borderLeftThickness);
        repaint();
    }

    public Color getInactiveBackground() {
        return inactiveBackground;
    }

    public void setInactiveBackground(Color inactiveBackground) {
        this.inactiveBackground = inactiveBackground == null ? Color.WHITE : inactiveBackground;
        refreshVisualState();
        repaint();
    }

    public Color getInactiveForeground() {
        return inactiveForeground;
    }

    public void setInactiveForeground(Color inactiveForeground) {
        this.inactiveForeground = inactiveForeground == null ? Color.GRAY : inactiveForeground;
        refreshVisualState();
        repaint();
    }

    public Color getInactiveBorderColor() {
        return inactiveBorderColor;
    }

    public void setInactiveBorderColor(Color inactiveBorderColor) {
        this.inactiveBorderColor = inactiveBorderColor == null ? Color.WHITE : inactiveBorderColor;
        repaint();
    }

    public Color getActiveBackground() {
        return activeBackground;
    }

    public void setActiveBackground(Color activeBackground) {
        this.activeBackground = activeBackground == null ? inactiveBackground : activeBackground;
        refreshVisualState();
        repaint();
    }

    public Color getActiveForeground() {
        return activeForeground;
    }

    public void setActiveForeground(Color activeForeground) {
        this.activeForeground = activeForeground == null ? inactiveForeground : activeForeground;
        refreshVisualState();
        repaint();
    }

    public Color getActiveBorderColor() {
        return activeBorderColor;
    }

    public void setActiveBorderColor(Color activeBorderColor) {
        this.activeBorderColor = activeBorderColor == null ? inactiveBorderColor : activeBorderColor;
        repaint();
    }

    public Color getHoverBackground() {
        return hoverBackground;
    }

    public void setHoverBackground(Color hoverBackground) {
        this.hoverBackground = hoverBackground == null ? inactiveBackground : hoverBackground;
        refreshVisualState();
        repaint();
    }

    public Color getHoverForeground() {
        return hoverForeground;
    }

    public void setHoverForeground(Color hoverForeground) {
        this.hoverForeground = hoverForeground == null ? inactiveForeground : hoverForeground;
        refreshVisualState();
        repaint();
    }

    public Color getHoverBorderColor() {
        return hoverBorderColor;
    }

    public void setHoverBorderColor(Color hoverBorderColor) {
        this.hoverBorderColor = hoverBorderColor == null ? inactiveBorderColor : hoverBorderColor;
        repaint();
    }

    public Color getActiveHoverBackground() {
        return activeHoverBackground;
    }

    public void setActiveHoverBackground(Color activeHoverBackground) {
        this.activeHoverBackground = activeHoverBackground == null ? activeBackground : activeHoverBackground;
        refreshVisualState();
        repaint();
    }

    public Color getActiveHoverForeground() {
        return activeHoverForeground;
    }

    public void setActiveHoverForeground(Color activeHoverForeground) {
        this.activeHoverForeground = activeHoverForeground == null ? activeForeground : activeHoverForeground;
        refreshVisualState();
        repaint();
    }

    public Color getActiveHoverBorderColor() {
        return activeHoverBorderColor;
    }

    public void setActiveHoverBorderColor(Color activeHoverBorderColor) {
        this.activeHoverBorderColor = activeHoverBorderColor == null ? activeBorderColor : activeHoverBorderColor;
        repaint();
    }

    public Color getPressedBackground() {
        return pressedBackground;
    }

    public void setPressedBackground(Color pressedBackground) {
        this.pressedBackground = pressedBackground == null ? activeBackground : pressedBackground;
        refreshVisualState();
        repaint();
    }

    public Color getPressedForeground() {
        return pressedForeground;
    }

    public void setPressedForeground(Color pressedForeground) {
        this.pressedForeground = pressedForeground == null ? activeForeground : pressedForeground;
        refreshVisualState();
        repaint();
    }

    public Color getPressedBorderColor() {
        return pressedBorderColor;
    }

    public void setPressedBorderColor(Color pressedBorderColor) {
        this.pressedBorderColor = pressedBorderColor == null ? activeBorderColor : pressedBorderColor;
        repaint();
    }

    public Color getDisabledBackground() {
        return disabledBackground;
    }

    public void setDisabledBackground(Color disabledBackground) {
        this.disabledBackground = disabledBackground == null ? Color.LIGHT_GRAY : disabledBackground;
        refreshVisualState();
        repaint();
    }

    public Color getDisabledForeground() {
        return disabledForeground;
    }

    public void setDisabledForeground(Color disabledForeground) {
        this.disabledForeground = disabledForeground == null ? Color.GRAY : disabledForeground;
        refreshVisualState();
        repaint();
    }

    public Color getDisabledBorderColor() {
        return disabledBorderColor;
    }

    public void setDisabledBorderColor(Color disabledBorderColor) {
        this.disabledBorderColor = disabledBorderColor == null ? Color.LIGHT_GRAY : disabledBorderColor;
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
        refreshVisualState();
        revalidate();
        repaint();
    }

    public int getIconHeight() {
        return iconHeight;
    }

    public void setIconHeight(int iconHeight) {
        this.iconHeight = Math.max(0, iconHeight);
        refreshVisualState();
        revalidate();
        repaint();
    }

    public Icon getInactiveIcon() {
        return inactiveIcon;
    }

    public void setInactiveIcon(Icon inactiveIcon) {
        this.inactiveIcon = inactiveIcon;
        refreshVisualState();
        repaint();
    }

    public Icon getActiveIcon() {
        return activeIcon;
    }

    public void setActiveIcon(Icon activeIcon) {
        this.activeIcon = activeIcon;
        refreshVisualState();
        repaint();
    }

    public Icon getHoverIcon() {
        return hoverIcon;
    }

    public void setHoverIcon(Icon hoverIcon) {
        this.hoverIcon = hoverIcon;
        refreshVisualState();
        repaint();
    }

    public Icon getActiveHoverIcon() {
        return activeHoverIcon;
    }

    public void setActiveHoverIcon(Icon activeHoverIcon) {
        this.activeHoverIcon = activeHoverIcon;
        refreshVisualState();
        repaint();
    }

    public Icon getPressedIcon() {
        return pressedIcon;
    }

    public void setPressedIcon(Icon pressedIcon) {
        this.pressedIcon = pressedIcon;
        refreshVisualState();
        repaint();
    }

    public Icon getDisabledIconCustom() {
        return disabledIcon;
    }

    public void setDisabledIconCustom(Icon disabledIcon) {
        this.disabledIcon = disabledIcon;
        refreshVisualState();
        repaint();
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
        this.inactiveIcon = icon;
        refreshVisualState();
        repaint();
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

        if (width <= 0 || height <= 0) {
            g2.dispose();
            return;
        }

        int top = Math.min(borderTopThickness, height / 2);
        int right = Math.min(borderRightThickness, width / 2);
        int bottom = Math.min(borderBottomThickness, height / 2);
        int left = Math.min(borderLeftThickness, width / 2);

        Shape outerShape = createCustomShape(
                0,
                0,
                width,
                height,
                roundTopLeft,
                roundTopRight,
                roundBottomLeft,
                roundBottomRight
        );

        int innerX = left;
        int innerY = top;
        int innerWidth = Math.max(0, width - left - right);
        int innerHeight = Math.max(0, height - top - bottom);

        int innerRoundTopLeft = Math.max(0, roundTopLeft - Math.max(left, top));
        int innerRoundTopRight = Math.max(0, roundTopRight - Math.max(right, top));
        int innerRoundBottomLeft = Math.max(0, roundBottomLeft - Math.max(left, bottom));
        int innerRoundBottomRight = Math.max(0, roundBottomRight - Math.max(right, bottom));

        Shape innerShape = createCustomShape(
                innerX,
                innerY,
                innerWidth,
                innerHeight,
                innerRoundTopLeft,
                innerRoundTopRight,
                innerRoundBottomLeft,
                innerRoundBottomRight
        );

        if (top > 0 || right > 0 || bottom > 0 || left > 0) {
            Area borderArea = new Area(outerShape);
            borderArea.subtract(new Area(innerShape));

            g2.setColor(getCurrentBorderColor());
            g2.fill(borderArea);

            g2.setColor(getCurrentBackground());
            g2.fill(innerShape);
        } else {
            g2.setColor(getCurrentBackground());
            g2.fill(outerShape);
        }

        g2.dispose();

        super.paintComponent(graphic);
    }

    private void refreshVisualState() {
        super.setForeground(getCurrentForeground());
        super.setIcon(resizeIcon(getCurrentIcon()));
    }

    private Color getCurrentBackground() {
        if (!isEnabled()) {
            return disabledBackground;
        }

        if (getModel().isPressed()) {
            return pressedBackground;
        }

        if (active && getModel().isRollover()) {
            return activeHoverBackground;
        }

        if (active) {
            return activeBackground;
        }

        if (getModel().isRollover()) {
            return hoverBackground;
        }

        return inactiveBackground;
    }

    private Color getCurrentForeground() {
        if (!isEnabled()) {
            return disabledForeground;
        }

        if (getModel().isPressed()) {
            return pressedForeground;
        }

        if (active && getModel().isRollover()) {
            return activeHoverForeground;
        }

        if (active) {
            return activeForeground;
        }

        if (getModel().isRollover()) {
            return hoverForeground;
        }

        return inactiveForeground;
    }

    private Color getCurrentBorderColor() {
        if (!isEnabled()) {
            return disabledBorderColor;
        }

        if (getModel().isPressed()) {
            return pressedBorderColor;
        }

        if (active && getModel().isRollover()) {
            return activeHoverBorderColor;
        }

        if (active) {
            return activeBorderColor;
        }

        if (getModel().isRollover()) {
            return hoverBorderColor;
        }

        return inactiveBorderColor;
    }

    private Icon getCurrentIcon() {
        if (!isEnabled()) {
            if (disabledIcon != null) {
                return disabledIcon;
            }
            return inactiveIcon;
        }

        if (getModel().isPressed()) {
            if (pressedIcon != null) {
                return pressedIcon;
            }

            if (active && activeIcon != null) {
                return activeIcon;
            }

            return inactiveIcon;
        }

        if (active && getModel().isRollover()) {
            if (activeHoverIcon != null) {
                return activeHoverIcon;
            }

            if (activeIcon != null) {
                return activeIcon;
            }

            return inactiveIcon;
        }

        if (active) {
            if (activeIcon != null) {
                return activeIcon;
            }

            return inactiveIcon;
        }

        if (getModel().isRollover()) {
            if (hoverIcon != null) {
                return hoverIcon;
            }

            return inactiveIcon;
        }

        return inactiveIcon;
    }

    private Icon resizeIcon(Icon icon) {
        if (icon == null) {
            return null;
        }

        if (iconWidth > 0 && iconHeight > 0 && icon instanceof ImageIcon) {
            Image image = ((ImageIcon) icon).getImage();

            Image scaledImage = image.getScaledInstance(
                    iconWidth,
                    iconHeight,
                    Image.SCALE_SMOOTH
            );

            return new ImageIcon(scaledImage);
        }

        return icon;
    }

    private Shape createCustomShape(
            double x,
            double y,
            double width,
            double height,
            int topLeft,
            int topRight,
            int bottomLeft,
            int bottomRight
    ) {
        double tl = Math.min(topLeft, Math.min(width, height));
        double tr = Math.min(topRight, Math.min(width, height));
        double bl = Math.min(bottomLeft, Math.min(width, height));
        double br = Math.min(bottomRight, Math.min(width, height));

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

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        refreshVisualState();
        repaint();
    }

    @Override
    public boolean contains(int x, int y) {
        Shape shape = createCustomShape(
                0,
                0,
                getWidth(),
                getHeight(),
                roundTopLeft,
                roundTopRight,
                roundBottomLeft,
                roundBottomRight
        );

        return shape.contains(x, y);
    }

    @Override
    public Component.BaselineResizeBehavior getBaselineResizeBehavior() {
        return super.getBaselineResizeBehavior();
    }
}