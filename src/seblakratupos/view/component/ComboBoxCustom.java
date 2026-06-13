package seblakratupos.view.component;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.RoundRectangle2D;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.border.AbstractBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.basic.ComboPopup;

public class ComboBoxCustom extends JComboBox<String> {

    private int round = 15;
    private int borderThickness = 1;
    private Color borderColor = new Color(180, 180, 180);

    private int popupRound = 15;
    private int popupBorderThickness = 1;
    private Color popupBorderColor = new Color(180, 180, 180);

    private Color arrowColor = Color.BLACK;
    private int arrowSize = 9;
    private int arrowStroke = 2;
    private int arrowButtonWidth = 35;
    private int arrowOffsetX = 0;
    private int arrowOffsetY = 0;

    private Color popupBackground = Color.WHITE;
    private Color selectionBackground = new Color(230, 230, 230);
    private Color selectionForeground = Color.BLACK;

    private int hoverRound = 8;
    private int hoverInset = 2;

    private int selectedTextTopPadding = 7;
    private int selectedTextLeftPadding = 10;
    private int selectedTextBottomPadding = 7;
    private int selectedTextRightPadding = 10;

    private int selectedTextOffsetX = 0;
    private int selectedTextOffsetY = 0;

    private int dropdownTextTopPadding = 7;
    private int dropdownTextLeftPadding = 10;
    private int dropdownTextBottomPadding = 7;
    private int dropdownTextRightPadding = 10;

    private int textHorizontalAlignment = JLabel.LEFT;
    private int dropdownTextHorizontalAlignment = JLabel.LEFT;

    private int popupOffsetX = 0;
    private int popupOffsetY = 0;
    private int popupExtraWidth = 0;

    public ComboBoxCustom() {
        setOpaque(false);
        setBackground(Color.WHITE);
        setForeground(Color.BLACK);
        setFocusable(false);
        setEditable(false);
        setBorder(new EmptyBorder(0, 0, 0, 0));

        setUI(new ComboCustomUI());
        setRenderer(new ComboCustomRenderer());
    }

    public int getRound() {
        return round;
    }

    public void setRound(int round) {
        this.round = Math.max(0, round);
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

    public int getPopupRound() {
        return popupRound;
    }

    public void setPopupRound(int popupRound) {
        this.popupRound = Math.max(0, popupRound);
        repaint();
    }

    public int getPopupBorderThickness() {
        return popupBorderThickness;
    }

    public void setPopupBorderThickness(int popupBorderThickness) {
        this.popupBorderThickness = Math.max(0, popupBorderThickness);
        repaint();
    }

    public Color getPopupBorderColor() {
        return popupBorderColor;
    }

    public void setPopupBorderColor(Color popupBorderColor) {
        this.popupBorderColor = popupBorderColor == null ? Color.BLACK : popupBorderColor;
        repaint();
    }

    public Color getArrowColor() {
        return arrowColor;
    }

    public void setArrowColor(Color arrowColor) {
        this.arrowColor = arrowColor == null ? Color.BLACK : arrowColor;
        repaint();
    }

    public int getArrowSize() {
        return arrowSize;
    }

    public void setArrowSize(int arrowSize) {
        this.arrowSize = Math.max(4, arrowSize);
        repaint();
    }

    public int getArrowStroke() {
        return arrowStroke;
    }

    public void setArrowStroke(int arrowStroke) {
        this.arrowStroke = Math.max(1, arrowStroke);
        repaint();
    }

    public int getArrowButtonWidth() {
        return arrowButtonWidth;
    }

    public void setArrowButtonWidth(int arrowButtonWidth) {
        this.arrowButtonWidth = Math.max(15, arrowButtonWidth);
        revalidate();
        repaint();
    }

    public int getArrowOffsetX() {
        return arrowOffsetX;
    }

    public void setArrowOffsetX(int arrowOffsetX) {
        this.arrowOffsetX = arrowOffsetX;
        repaint();
    }

    public int getArrowOffsetY() {
        return arrowOffsetY;
    }

    public void setArrowOffsetY(int arrowOffsetY) {
        this.arrowOffsetY = arrowOffsetY;
        repaint();
    }

    public Color getPopupBackground() {
        return popupBackground;
    }

    public void setPopupBackground(Color popupBackground) {
        this.popupBackground = popupBackground == null ? Color.WHITE : popupBackground;
        repaint();
    }

    public Color getSelectionBackground() {
        return selectionBackground;
    }

    public void setSelectionBackground(Color selectionBackground) {
        this.selectionBackground = selectionBackground == null
                ? new Color(230, 230, 230)
                : selectionBackground;
        repaint();
    }

    public Color getSelectionForeground() {
        return selectionForeground;
    }

    public void setSelectionForeground(Color selectionForeground) {
        this.selectionForeground = selectionForeground == null
                ? Color.BLACK
                : selectionForeground;
        repaint();
    }

    public int getHoverRound() {
        return hoverRound;
    }

    public void setHoverRound(int hoverRound) {
        this.hoverRound = Math.max(0, hoverRound);
        repaint();
    }

    public int getHoverInset() {
        return hoverInset;
    }

    public void setHoverInset(int hoverInset) {
        this.hoverInset = Math.max(0, hoverInset);
        repaint();
    }

    public int getSelectedTextTopPadding() {
        return selectedTextTopPadding;
    }

    public void setSelectedTextTopPadding(int selectedTextTopPadding) {
        this.selectedTextTopPadding = Math.max(0, selectedTextTopPadding);
        repaint();
    }

    public int getSelectedTextLeftPadding() {
        return selectedTextLeftPadding;
    }

    public void setSelectedTextLeftPadding(int selectedTextLeftPadding) {
        this.selectedTextLeftPadding = Math.max(0, selectedTextLeftPadding);
        repaint();
    }

    public int getSelectedTextBottomPadding() {
        return selectedTextBottomPadding;
    }

    public void setSelectedTextBottomPadding(int selectedTextBottomPadding) {
        this.selectedTextBottomPadding = Math.max(0, selectedTextBottomPadding);
        repaint();
    }

    public int getSelectedTextRightPadding() {
        return selectedTextRightPadding;
    }

    public void setSelectedTextRightPadding(int selectedTextRightPadding) {
        this.selectedTextRightPadding = Math.max(0, selectedTextRightPadding);
        repaint();
    }

    public int getSelectedTextOffsetX() {
        return selectedTextOffsetX;
    }

    public void setSelectedTextOffsetX(int selectedTextOffsetX) {
        this.selectedTextOffsetX = selectedTextOffsetX;
        repaint();
    }

    public int getSelectedTextOffsetY() {
        return selectedTextOffsetY;
    }

    public void setSelectedTextOffsetY(int selectedTextOffsetY) {
        this.selectedTextOffsetY = selectedTextOffsetY;
        repaint();
    }

    public int getDropdownTextTopPadding() {
        return dropdownTextTopPadding;
    }

    public void setDropdownTextTopPadding(int dropdownTextTopPadding) {
        this.dropdownTextTopPadding = Math.max(0, dropdownTextTopPadding);
        repaint();
    }

    public int getDropdownTextLeftPadding() {
        return dropdownTextLeftPadding;
    }

    public void setDropdownTextLeftPadding(int dropdownTextLeftPadding) {
        this.dropdownTextLeftPadding = Math.max(0, dropdownTextLeftPadding);
        repaint();
    }

    public int getDropdownTextBottomPadding() {
        return dropdownTextBottomPadding;
    }

    public void setDropdownTextBottomPadding(int dropdownTextBottomPadding) {
        this.dropdownTextBottomPadding = Math.max(0, dropdownTextBottomPadding);
        repaint();
    }

    public int getDropdownTextRightPadding() {
        return dropdownTextRightPadding;
    }

    public void setDropdownTextRightPadding(int dropdownTextRightPadding) {
        this.dropdownTextRightPadding = Math.max(0, dropdownTextRightPadding);
        repaint();
    }

    public int getTextHorizontalAlignment() {
        return textHorizontalAlignment;
    }

    public void setTextHorizontalAlignment(int textHorizontalAlignment) {
        this.textHorizontalAlignment = textHorizontalAlignment;
        repaint();
    }

    public int getDropdownTextHorizontalAlignment() {
        return dropdownTextHorizontalAlignment;
    }

    public void setDropdownTextHorizontalAlignment(int dropdownTextHorizontalAlignment) {
        this.dropdownTextHorizontalAlignment = dropdownTextHorizontalAlignment;
        repaint();
    }

    public int getPopupOffsetX() {
        return popupOffsetX;
    }

    public void setPopupOffsetX(int popupOffsetX) {
        this.popupOffsetX = popupOffsetX;
    }

    public int getPopupOffsetY() {
        return popupOffsetY;
    }

    public void setPopupOffsetY(int popupOffsetY) {
        this.popupOffsetY = popupOffsetY;
    }

    public int getPopupExtraWidth() {
        return popupExtraWidth;
    }

    public void setPopupExtraWidth(int popupExtraWidth) {
        this.popupExtraWidth = popupExtraWidth;
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

        Shape shape = new RoundRectangle2D.Double(
                inset,
                inset,
                width - thickness,
                height - thickness,
                round,
                round
        );

        g2.setColor(getBackground());
        g2.fill(shape);

        if (borderThickness > 0) {
            g2.setColor(borderColor);
            g2.setStroke(new BasicStroke(
                    borderThickness,
                    BasicStroke.CAP_ROUND,
                    BasicStroke.JOIN_ROUND
            ));
            g2.draw(shape);
        }

        g2.dispose();

        super.paintComponent(graphic);
    }

    private class ComboCustomUI extends BasicComboBoxUI {

        @Override
        protected void installDefaults() {
            super.installDefaults();
            comboBox.setOpaque(false);
            comboBox.setBorder(new EmptyBorder(0, 0, 0, 0));
        }

        @Override
        public void paintCurrentValueBackground(Graphics g, Rectangle bounds, boolean hasFocus) {
            // Jangan diisi.
            // Kalau diisi, background abu-abu bawaan Swing bisa muncul lagi.
        }

        @Override
        protected Rectangle rectangleForCurrentValue() {
            Insets insets = comboBox.getInsets();

            int width = comboBox.getWidth();
            int height = comboBox.getHeight();

            int x = insets.left + selectedTextOffsetX;
            int y = insets.top + selectedTextOffsetY;

            int w = width
                    - insets.left
                    - insets.right
                    - arrowButtonWidth
                    - selectedTextOffsetX;

            int h = height
                    - insets.top
                    - insets.bottom
                    - selectedTextOffsetY;

            return new Rectangle(x, y, w, h);
        }

        @Override
        protected JButton createArrowButton() {
            JButton button = new JButton() {

                @Override
                public Dimension getPreferredSize() {
                    return new Dimension(arrowButtonWidth, 0);
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

                    int centerX = width / 2 + arrowOffsetX;
                    int centerY = height / 2 + arrowOffsetY;

                    int half = arrowSize / 2;

                    g2.setColor(arrowColor);
                    g2.setStroke(new BasicStroke(
                            arrowStroke,
                            BasicStroke.CAP_ROUND,
                            BasicStroke.JOIN_ROUND
                    ));

                    g2.drawLine(
                            centerX - half,
                            centerY - half / 2,
                            centerX,
                            centerY + half / 2
                    );

                    g2.drawLine(
                            centerX,
                            centerY + half / 2,
                            centerX + half,
                            centerY - half / 2
                    );

                    g2.dispose();
                }
            };

            button.setOpaque(false);
            button.setContentAreaFilled(false);
            button.setBorder(new EmptyBorder(0, 0, 0, 0));
            button.setFocusPainted(false);
            button.setFocusable(false);

            return button;
        }

        @Override
        protected ComboPopup createPopup() {
            return new RoundedComboPopup(comboBox);
        }
    }

    private class RoundedComboPopup extends BasicComboPopup {

        public RoundedComboPopup(JComboBox combo) {
            super(combo);

            setOpaque(false);
            setBackground(new Color(0, 0, 0, 0));
            setBorder(new RoundedPopupBorder());
        }

        @Override
        protected JList createList() {
            JList list = super.createList();

            list.setOpaque(false);
            list.setBackground(new Color(0, 0, 0, 0));
            list.setSelectionBackground(new Color(0, 0, 0, 0));
            list.setSelectionForeground(selectionForeground);
            list.setBorder(new EmptyBorder(0, 0, 0, 0));

            return list;
        }

        @Override
        protected JScrollPane createScroller() {
            JScrollPane scrollPane = super.createScroller();

            scrollPane.setOpaque(false);
            scrollPane.setBorder(BorderFactory.createEmptyBorder());
            scrollPane.getViewport().setOpaque(false);
            scrollPane.getViewport().setBorder(null);

            if (scrollPane.getVerticalScrollBar() != null) {
                scrollPane.getVerticalScrollBar().setOpaque(false);
            }

            if (scrollPane.getHorizontalScrollBar() != null) {
                scrollPane.getHorizontalScrollBar().setOpaque(false);
            }

            return scrollPane;
        }

        @Override
        public void show() {
            int width = Math.max(40, comboBox.getWidth() + popupExtraWidth);
            int height = getPopupHeightForRowCount(comboBox.getMaximumRowCount());

            Rectangle popupBounds = computePopupBounds(
                    popupOffsetX,
                    comboBox.getBounds().height + popupOffsetY,
                    width,
                    height
            );

            Dimension popupSize = popupBounds.getSize();

            scroller.setMaximumSize(popupSize);
            scroller.setPreferredSize(popupSize);
            scroller.setMinimumSize(popupSize);

            list.invalidate();

            int selectedIndex = comboBox.getSelectedIndex();

            if (selectedIndex >= 0) {
                list.setSelectedIndex(selectedIndex);
                list.ensureIndexIsVisible(selectedIndex);
            } else {
                list.clearSelection();
            }

            show(comboBox, popupBounds.x, popupBounds.y);
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

            g2.setColor(popupBackground);
            g2.fillRoundRect(
                    0,
                    0,
                    width - 1,
                    height - 1,
                    popupRound,
                    popupRound
            );

            g2.dispose();

            super.paintComponent(graphic);
        }

        @Override
        protected void paintChildren(Graphics graphic) {
            Graphics2D g2 = (Graphics2D) graphic.create();

            Shape clip = new RoundRectangle2D.Double(
                    0,
                    0,
                    getWidth(),
                    getHeight(),
                    popupRound,
                    popupRound
            );

            g2.setClip(clip);
            super.paintChildren(g2);
            g2.dispose();
        }
    }

    private class RoundedPopupBorder extends AbstractBorder {

        @Override
        public Insets getBorderInsets(Component c) {
            int size = Math.max(0, popupBorderThickness);
            return new Insets(size, size, size, size);
        }

        @Override
        public Insets getBorderInsets(Component c, Insets insets) {
            int size = Math.max(0, popupBorderThickness);

            insets.top = size;
            insets.left = size;
            insets.bottom = size;
            insets.right = size;

            return insets;
        }

        @Override
        public void paintBorder(
                Component c,
                Graphics graphic,
                int x,
                int y,
                int width,
                int height
        ) {
            if (popupBorderThickness <= 0) {
                return;
            }

            Graphics2D g2 = (Graphics2D) graphic.create();

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            int thickness = popupBorderThickness;
            double inset = thickness / 2.0;

            g2.setColor(popupBorderColor);
            g2.setStroke(new BasicStroke(
                    thickness,
                    BasicStroke.CAP_ROUND,
                    BasicStroke.JOIN_ROUND
            ));

            g2.draw(new RoundRectangle2D.Double(
                    x + inset,
                    y + inset,
                    width - thickness,
                    height - thickness,
                    popupRound,
                    popupRound
            ));

            g2.dispose();
        }
    }

    private class ComboCustomRenderer extends JLabel implements javax.swing.ListCellRenderer<Object> {

        private boolean selected;
        private boolean popupItem;

        public ComboCustomRenderer() {
            setOpaque(false);
        }

        @Override
        public Component getListCellRendererComponent(
                JList<?> list,
                Object value,
                int index,
                boolean isSelected,
                boolean cellHasFocus
        ) {
            setText(value == null ? "" : value.toString());
            setFont(ComboBoxCustom.this.getFont());

            this.selected = isSelected;
            this.popupItem = index != -1;

            if (index == -1) {
                setForeground(ComboBoxCustom.this.getForeground());
                setHorizontalAlignment(textHorizontalAlignment);
                setBorder(new EmptyBorder(
                        selectedTextTopPadding,
                        selectedTextLeftPadding,
                        selectedTextBottomPadding,
                        selectedTextRightPadding
                ));
            } else {
                setForeground(isSelected ? selectionForeground : ComboBoxCustom.this.getForeground());
                setHorizontalAlignment(dropdownTextHorizontalAlignment);
                setBorder(new EmptyBorder(
                        dropdownTextTopPadding,
                        dropdownTextLeftPadding,
                        dropdownTextBottomPadding,
                        dropdownTextRightPadding
                ));
            }

            return this;
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

            if (popupItem && selected) {
                int inset = Math.min(hoverInset, Math.min(width, height) / 2);

                g2.setColor(selectionBackground);
                g2.fillRoundRect(
                        inset,
                        inset,
                        width - inset * 2,
                        height - inset * 2,
                        hoverRound,
                        hoverRound
                );
            }

            g2.dispose();

            super.paintComponent(graphic);
        }
    }
}