package seblakratupos.view.component;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.Area;
import java.awt.geom.Path2D;
import javax.swing.JPanel;

public class PanelCustomV2 extends JPanel {

    private int roundTopLeft = 0;
    private int roundTopRight = 0;
    private int roundBottomLeft = 0;
    private int roundBottomRight = 0;

    private Color borderColor = Color.BLACK;

    private int borderThickness = 0;

    private int borderTopThickness = 0;
    private int borderRightThickness = 0;
    private int borderBottomThickness = 0;
    private int borderLeftThickness = 0;

    public PanelCustomV2() {
        setOpaque(false);
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

    public Color getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(Color borderColor) {
        this.borderColor = borderColor == null ? Color.BLACK : borderColor;
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

    @Override
    protected void paintComponent(Graphics graphic) {
        super.paintComponent(graphic);

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

            g2.setColor(borderColor);
            g2.fill(borderArea);

            g2.setColor(getBackground());
            g2.fill(innerShape);
        } else {
            g2.setColor(getBackground());
            g2.fill(outerShape);
        }

        g2.dispose();
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
}