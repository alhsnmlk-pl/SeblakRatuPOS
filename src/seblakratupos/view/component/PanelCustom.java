package seblakratupos.view.component;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.Path2D;
import javax.swing.JPanel;

public class PanelCustom extends JPanel {

    private int roundTopLeft = 0;
    private int roundTopRight = 0;
    private int roundBottomLeft = 0;
    private int roundBottomRight = 0;

    private Color borderColor = Color.BLACK;
    private int borderThickness = 0;

    public PanelCustom() {
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
        this.borderColor = borderColor;
        repaint();
    }

    public int getBorderThickness() {
        return borderThickness;
    }

    public void setBorderThickness(int borderThickness) {
        this.borderThickness = Math.max(0, borderThickness);
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

        int thickness = borderThickness;
        int inset = thickness / 2;

        Shape shape = createCustomShape(
                inset,
                inset,
                width - thickness,
                height - thickness
        );

        g2.setColor(getBackground());
        g2.fill(shape);

        if (borderThickness > 0) {
            g2.setColor(borderColor);
            g2.setStroke(new BasicStroke(borderThickness));
            g2.draw(shape);
        }

        g2.dispose();

        super.paintComponent(graphic);
    }

    private Shape createCustomShape(int x, int y, int width, int height) {
        int tl = Math.min(roundTopLeft, Math.min(width, height));
        int tr = Math.min(roundTopRight, Math.min(width, height));
        int bl = Math.min(roundBottomLeft, Math.min(width, height));
        int br = Math.min(roundBottomRight, Math.min(width, height));

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