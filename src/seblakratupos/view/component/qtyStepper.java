/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package seblakratupos.view.component;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.RoundRectangle2D;
import java.io.Serializable;
import javax.swing.JPanel;
import javax.swing.event.EventListenerList;

/**
 *
 * @author Al
 */



public class qtyStepper extends JPanel implements Serializable {

    private int count = 0;
    private int minCount = 0;
    private int maxCount = 99;
    private int step = 1;

    private int radius = 8;
    private int borderSize = 1;
    private int buttonWidth = 26;

    private Color fillColor = Color.WHITE;
    private Color borderColor = new Color(235, 210, 207);
    private Color hoverColor = new Color(250, 240, 238);

    private Color minusColor = new Color(180, 80, 70);
    private Color plusColor = new Color(255, 98, 45);
    private Color countColor = new Color(35, 35, 35);

    private int hoverZone = 0;

    private final EventListenerList listenerList = new EventListenerList();

    public qtyStepper() {
        setOpaque(false);
        setPreferredSize(new Dimension(87, 29));
        setMinimumSize(new Dimension(55, 22));
        setFont(new Font("Segoe UI", Font.BOLD, 13));
        setCursor(new Cursor(Cursor.HAND_CURSOR));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() != MouseEvent.BUTTON1) {
                    return;
                }

                int zone = getZone(e.getX());

                if (zone == -1) {
                    minus();
                } else if (zone == 1) {
                    plus();
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                hoverZone = 0;
                repaint();
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                int zone = getZone(e.getX());

                if (hoverZone != zone) {
                    hoverZone = zone;
                    repaint();
                }
            }
        });
    }

    private int realButtonWidth() {
        int w = getWidth();

        if (w <= 0) {
            return buttonWidth;
        }

        return Math.max(1, Math.min(buttonWidth, w / 3));
    }

    private int getZone(int x) {
        int bw = realButtonWidth();
        int w = getWidth();

        if (x < bw) {
            return -1;
        } else if (x > w - bw) {
            return 1;
        }

        return 0;
    }

    public void plus() {
        if (count < maxCount) {
            int old = count;
            setCount(count + step);

            if (old != count) {
                fireActionPerformed("plus");
            }
        }
    }

    public void minus() {
        if (count > minCount) {
            int old = count;
            setCount(count - step);

            if (old != count) {
                fireActionPerformed("minus");
            }
        }
    }

    public boolean isMin() {
        return count <= minCount;
    }

    public boolean isMax() {
        return count >= maxCount;
    }

    public boolean canPlus() {
        return count < maxCount;
    }

    public boolean canMinus() {
        return count > minCount;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int w = getWidth();
        int h = getHeight();

        if (w <= 0 || h <= 0) {
            return;
        }

        Graphics2D g2 = (Graphics2D) g.create();

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int arc = Math.min(radius * 2, Math.min(w, h));
        double inset = borderSize / 2.0;

        Shape rounded = new RoundRectangle2D.Double(
                inset,
                inset,
                w - borderSize,
                h - borderSize,
                arc,
                arc
        );

        g2.setColor(fillColor);
        g2.fill(rounded);

        g2.setClip(rounded);

        int bw = realButtonWidth();

        if (hoverZone == -1 && canMinus()) {
            g2.setColor(hoverColor);
            g2.fillRect(0, 0, bw, h);
        } else if (hoverZone == 1 && canPlus()) {
            g2.setColor(hoverColor);
            g2.fillRect(w - bw, 0, bw, h);
        }

        g2.setClip(null);

        if (borderSize > 0) {
            g2.setColor(borderColor);
            g2.setStroke(new BasicStroke(borderSize));
            g2.draw(rounded);

            g2.setStroke(new BasicStroke(1));
            g2.drawLine(bw, 5, bw, h - 5);
            g2.drawLine(w - bw, 5, w - bw, h - 5);
        }

        g2.setFont(getFont());

        Color minusFinalColor = canMinus() ? minusColor : new Color(210, 210, 210);
        Color plusFinalColor = canPlus() ? plusColor : new Color(210, 210, 210);

        drawCenterText(g2, "-", 0, bw, h, minusFinalColor);
        drawCenterText(g2, String.valueOf(count), bw, w - bw, h, countColor);
        drawCenterText(g2, "+", w - bw, w, h, plusFinalColor);

        g2.dispose();
    }

    private void drawCenterText(Graphics2D g2, String text, int x1, int x2, int h, Color color) {
        FontMetrics fm = g2.getFontMetrics();

        int width = x2 - x1;
        int textWidth = fm.stringWidth(text);

        int x = x1 + (width - textWidth) / 2;
        int y = (h - fm.getHeight()) / 2 + fm.getAscent();

        g2.setColor(color);
        g2.drawString(text, x, y);
    }

    private void fireActionPerformed(String command) {
        ActionEvent event = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, command);

        for (ActionListener listener : listenerList.getListeners(ActionListener.class)) {
            listener.actionPerformed(event);
        }
    }

    public void addActionListener(ActionListener listener) {
        listenerList.add(ActionListener.class, listener);
    }

    public void removeActionListener(ActionListener listener) {
        listenerList.remove(ActionListener.class, listener);
    }

    // =========================
    // COUNT LOGIC
    // =========================

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        int old = this.count;

        if (count < minCount) {
            this.count = minCount;
        } else if (count > maxCount) {
            this.count = maxCount;
        } else {
            this.count = count;
        }

        firePropertyChange("count", old, this.count);
        repaint();
    }

    public int getMinCount() {
        return minCount;
    }

    public void setMinCount(int minCount) {
        this.minCount = minCount;

        if (maxCount < minCount) {
            maxCount = minCount;
        }

        setCount(count);
        repaint();
    }

    public int getMaxCount() {
        return maxCount;
    }

    public void setMaxCount(int maxCount) {
        this.maxCount = Math.max(maxCount, minCount);
        setCount(count);
        repaint();
    }

    public void setLimit(int minCount, int maxCount) {
        this.minCount = minCount;
        this.maxCount = Math.max(maxCount, minCount);
        setCount(count);
        repaint();
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = Math.max(1, step);
    }

    // Biar masih aman kalau sebelumnya pakai getValue()
    public int getValue() {
        return getCount();
    }

    public void setValue(int value) {
        setCount(value);
    }

    // =========================
    // STYLE CUSTOM
    // =========================

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = Math.max(0, radius);
        repaint();
    }

    public int getBorderSize() {
        return borderSize;
    }

    public void setBorderSize(int borderSize) {
        this.borderSize = Math.max(0, borderSize);
        repaint();
    }

    public int getButtonWidth() {
        return buttonWidth;
    }

    public void setButtonWidth(int buttonWidth) {
        this.buttonWidth = Math.max(1, buttonWidth);
        repaint();
    }

    public Color getFillColor() {
        return fillColor;
    }

    public void setFillColor(Color fillColor) {
        if (fillColor != null) {
            this.fillColor = fillColor;
            repaint();
        }
    }

    public Color getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(Color borderColor) {
        if (borderColor != null) {
            this.borderColor = borderColor;
            repaint();
        }
    }

    public Color getHoverColor() {
        return hoverColor;
    }

    public void setHoverColor(Color hoverColor) {
        if (hoverColor != null) {
            this.hoverColor = hoverColor;
            repaint();
        }
    }

    public Color getMinusColor() {
        return minusColor;
    }

    public void setMinusColor(Color minusColor) {
        if (minusColor != null) {
            this.minusColor = minusColor;
            repaint();
        }
    }

    public Color getPlusColor() {
        return plusColor;
    }

    public void setPlusColor(Color plusColor) {
        if (plusColor != null) {
            this.plusColor = plusColor;
            repaint();
        }
    }

    public Color getCountColor() {
        return countColor;
    }

    public void setCountColor(Color countColor) {
        if (countColor != null) {
            this.countColor = countColor;
            repaint();
        }
    }
}
