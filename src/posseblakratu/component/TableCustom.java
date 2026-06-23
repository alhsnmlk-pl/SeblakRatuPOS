package posseblakratu.component;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.Beans;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

public class TableCustom extends JTable {

    private boolean ready = false;

    private boolean designPreviewData = true;

    private Color tableBackgroundCustom = Color.WHITE;

    private boolean autoStyleScrollPane = true;
    private Color outerBorderColor = new Color(245, 226, 225);
    private int outerBorderThickness = 1;

    private Color headerBackgroundCustom = new Color(248, 248, 248);
    private Color headerForegroundCustom = new Color(24, 25, 43);
    private Color headerBorderColor = new Color(245, 226, 225);
    private int headerBorderThickness = 1;
    private int headerHeight = 48;
    private boolean headerBold = true;

    private int headerHorizontalAlignment = SwingConstants.LEFT;
    private boolean headerFollowColumnAlignment = true;

    private String headerLeftColumns = "";
    private String headerCenterColumns = "";
    private String headerRightColumns = "";

    private int headerPaddingLeft = 20;
    private int headerPaddingRight = 16;

    private Color rowBorderColor = new Color(245, 226, 225);
    private int rowBorderThickness = 1;
    private boolean showRowBorder = true;

    private Color textColorCustom = new Color(24, 25, 43);

    private int cellPaddingTop = 0;
    private int cellPaddingLeft = 20;
    private int cellPaddingBottom = 0;
    private int cellPaddingRight = 16;

    private int cellHorizontalAlignment = SwingConstants.LEFT;

    private String leftColumns = "";
    private String centerColumns = "";
    private String rightColumns = "";
    private String boldColumns = "";

    private String iconColumns = "";
    private String iconRows = "";
    private Icon cellIcon = null;
    private boolean showCellIconOnly = true;
    private int cellIconTextGap = 8;

    private boolean rowClickable = true;
    private boolean iconCellClickable = true;

    private boolean clickedRowBackgroundEnabled = true;
    private Color clickedRowBackgroundCustom = new Color(255, 247, 245);
    private Color clickedRowForegroundCustom = new Color(24, 25, 43);

    private String rowActionCommand = "rowClicked";
    private String iconActionCommand = "iconClicked";

    private int lastClickedModelRow = -1;
    private int lastClickedModelColumn = -1;
    private int clickedModelRow = -1;

    private boolean horizontalScrollEnabled = false;
    private int defaultColumnWidth = 120;
    private String columnWidths = "";

    public TableCustom() {
        super();
        initTableCustom();
    }

    public TableCustom(TableModel model) {
        super(model);
        initTableCustom();
    }

    public TableCustom(Object[][] rowData, Object[] columnNames) {
        super(rowData, columnNames);
        initTableCustom();
    }

    @Override
    protected JTableHeader createDefaultTableHeader() {
        return new ReadOnlyTableHeader(columnModel);
    }

    @Override
    public void setModel(TableModel dataModel) {
        super.setModel(dataModel);

        if (ready) {
            refreshDesign();
        }
    }

    private void initTableCustom() {
        setOpaque(true);
        setBackground(tableBackgroundCustom);
        setForeground(textColorCustom);

        setFont(new Font("SansSerif", Font.PLAIN, 14));
        setRowHeight(56);

        setShowGrid(false);
        setShowHorizontalLines(false);
        setShowVerticalLines(false);
        setIntercellSpacing(new Dimension(0, 0));
        setFillsViewportHeight(true);

        setFocusable(false);
        setRequestFocusEnabled(false);
        setRowSelectionAllowed(false);
        setColumnSelectionAllowed(false);
        setCellSelectionEnabled(false);
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JTableHeader header = getTableHeader();
        header.setOpaque(true);
        header.setBackground(headerBackgroundCustom);
        header.setForeground(headerForegroundCustom);
        header.setFont(new Font("SansSerif", Font.BOLD, 14));
        header.setPreferredSize(new Dimension(0, headerHeight));
        header.setDefaultRenderer(new HeaderRenderer());
        header.setReorderingAllowed(false);
        header.setResizingAllowed(false);
        header.setFocusable(false);
        header.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        header.setBorder(null);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleTableClick(e);
            }
        });

        installDesignPreviewData();

        ready = true;
        refreshDesign();
    }

    @Override
    public void addNotify() {
        super.addNotify();
        refreshDesign();
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }

    @Override
    public void changeSelection(int rowIndex, int columnIndex, boolean toggle, boolean extend) {
        /*
         * Sengaja kosong.
         * Biar selection default JTable tidak muncul.
         */
    }

    @Override
    public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
        Component component = super.prepareRenderer(renderer, row, column);

        int modelColumn = convertColumnIndexToModel(column);
        int modelRow = convertRowIndexToModel(row);
        Object value = getValueAt(row, column);

        if (component instanceof JLabel) {
            JLabel label = (JLabel) component;

            label.setOpaque(true);
            label.setForeground(getRowTextColor(modelRow));
            label.setBackground(getRowBackground(modelRow));

            label.setHorizontalAlignment(getCellAlignment(modelColumn));
            label.setVerticalAlignment(SwingConstants.CENTER);
            label.setIconTextGap(cellIconTextGap);

            boolean showIcon = shouldShowIcon(modelRow, modelColumn);

            if (showIcon && cellIcon != null) {
                label.setIcon(cellIcon);

                if (showCellIconOnly) {
                    label.setText("");
                } else {
                    label.setText(value == null ? "" : value.toString());
                }
            } else if (value instanceof Icon) {
                label.setIcon((Icon) value);
                label.setText("");
            } else {
                label.setIcon(null);
                label.setText(value == null ? "" : value.toString());
            }

            if (isNumberInList(boldColumns, modelColumn)) {
                label.setFont(getFont().deriveFont(Font.BOLD));
            } else {
                label.setFont(getFont().deriveFont(Font.PLAIN));
            }
        }

        decorateCell(component, modelRow);

        return component;
    }

    private Color getRowBackground(int modelRow) {
        if (clickedRowBackgroundEnabled && modelRow == clickedModelRow) {
            return clickedRowBackgroundCustom;
        }

        return tableBackgroundCustom;
    }

    private Color getRowTextColor(int modelRow) {
        if (clickedRowBackgroundEnabled && modelRow == clickedModelRow) {
            return clickedRowForegroundCustom;
        }

        return textColorCustom;
    }

    private void handleTableClick(MouseEvent e) {
        Point point = e.getPoint();

        int viewRow = rowAtPoint(point);
        int viewColumn = columnAtPoint(point);

        if (viewRow < 0 || viewColumn < 0) {
            return;
        }

        int modelRow = convertRowIndexToModel(viewRow);
        int modelColumn = convertColumnIndexToModel(viewColumn);

        lastClickedModelRow = modelRow;
        lastClickedModelColumn = modelColumn;
        clickedModelRow = modelRow;

        repaint();

        Object value = getValueAt(viewRow, viewColumn);

        boolean clickedIconFromProperty = shouldShowIcon(modelRow, modelColumn) && cellIcon != null;
        boolean clickedIconFromValue = value instanceof Icon;
        boolean clickedIconCell = clickedIconFromProperty || clickedIconFromValue;

        if (iconCellClickable && clickedIconCell) {
            fireActionPerformed(new ActionEvent(
                    this,
                    ActionEvent.ACTION_PERFORMED,
                    iconActionCommand
            ));
            return;
        }

        if (rowClickable) {
            fireActionPerformed(new ActionEvent(
                    this,
                    ActionEvent.ACTION_PERFORMED,
                    rowActionCommand
            ));
        }
    }

    private void decorateCell(Component component, int modelRow) {
        component.setBackground(getRowBackground(modelRow));
        component.setForeground(getRowTextColor(modelRow));

        if (component instanceof JComponent) {
            JComponent jc = (JComponent) component;
            jc.setOpaque(true);

            Border padding = BorderFactory.createEmptyBorder(
                    cellPaddingTop,
                    cellPaddingLeft,
                    cellPaddingBottom,
                    cellPaddingRight
            );

            if (showRowBorder && rowBorderThickness > 0) {
                Border line = BorderFactory.createMatteBorder(
                        0,
                        0,
                        rowBorderThickness,
                        0,
                        rowBorderColor
                );

                jc.setBorder(BorderFactory.createCompoundBorder(line, padding));
            } else {
                jc.setBorder(padding);
            }
        }
    }

    private boolean shouldShowIcon(int modelRow, int modelColumn) {
        boolean hasIconRows = iconRows != null && !iconRows.trim().isEmpty();
        boolean hasIconColumns = iconColumns != null && !iconColumns.trim().isEmpty();

        if (hasIconRows && hasIconColumns) {
            return isNumberInList(iconRows, modelRow) && isNumberInList(iconColumns, modelColumn);
        }

        if (hasIconRows) {
            return isNumberInList(iconRows, modelRow);
        }

        if (hasIconColumns) {
            return isNumberInList(iconColumns, modelColumn);
        }

        return false;
    }

    private int getCellAlignment(int modelColumn) {
        if (isNumberInList(leftColumns, modelColumn)) {
            return SwingConstants.LEFT;
        }

        if (isNumberInList(centerColumns, modelColumn)) {
            return SwingConstants.CENTER;
        }

        if (isNumberInList(rightColumns, modelColumn)) {
            return SwingConstants.RIGHT;
        }

        return cellHorizontalAlignment;
    }

    private int getHeaderAlignment(int modelColumn) {
        if (isNumberInList(headerLeftColumns, modelColumn)) {
            return SwingConstants.LEFT;
        }

        if (isNumberInList(headerCenterColumns, modelColumn)) {
            return SwingConstants.CENTER;
        }

        if (isNumberInList(headerRightColumns, modelColumn)) {
            return SwingConstants.RIGHT;
        }

        if (headerFollowColumnAlignment) {
            return getCellAlignment(modelColumn);
        }

        return headerHorizontalAlignment;
    }

    private boolean isNumberInList(String text, int number) {
        if (text == null || text.trim().isEmpty()) {
            return false;
        }

        String[] parts = text.split(",");

        for (String part : parts) {
            part = part.trim();

            if (part.isEmpty()) {
                continue;
            }

            try {
                if (part.contains("-")) {
                    String[] range = part.split("-");

                    if (range.length == 2) {
                        int start = Integer.parseInt(range[0].trim());
                        int end = Integer.parseInt(range[1].trim());

                        if (number >= start && number <= end) {
                            return true;
                        }
                    }
                } else {
                    int value = Integer.parseInt(part);

                    if (number == value) {
                        return true;
                    }
                }
            } catch (Exception e) {
                /*
                 * Kalau property salah, abaikan.
                 * Jangan sampai koma goblok bikin program mati.
                 */
            }
        }

        return false;
    }

    private void installDesignPreviewData() {
        if (!designPreviewData) {
            return;
        }

        if (!Beans.isDesignTime()) {
            return;
        }

        /*
         * Preview hanya dipasang kalau model masih kosong.
         * Jadi kalau kamu isi dari Table Model NetBeans, tidak ditimpa.
         */
        if (getColumnCount() > 0 || getRowCount() > 0) {
            return;
        }

        setModel(new DefaultTableModel(
                new Object[][]{
                    {"M001", "Es Jeruk", "Minuman", "Rp 4.000", "Tersedia", ""},
                    {"M002", "Es Teh", "Minuman", "Rp 3.000", "Tersedia", ""},
                    {"S001", "Seblak Original", "Seblak", "Rp 9.000", "Tersedia", ""}
                },
                new String[]{
                    "ID", "Nama Produk", "Kategori", "Harga Jual", "Status", "Aksi"
                }
        ));
    }

    private void refreshDesign() {
        setBackground(tableBackgroundCustom);
        setForeground(textColorCustom);

        if (rowClickable || iconCellClickable) {
            setCursor(new Cursor(Cursor.HAND_CURSOR));
        } else {
            setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }

        JTableHeader header = getTableHeader();

        if (header != null) {
            header.setOpaque(true);
            header.setBorder(null);
            header.setBackground(headerBackgroundCustom);
            header.setForeground(headerForegroundCustom);
            header.setPreferredSize(new Dimension(0, headerHeight));
            header.setReorderingAllowed(false);
            header.setResizingAllowed(false);
            header.setFocusable(false);
            header.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            header.repaint();
            header.revalidate();
        }

        applyHorizontalScroll();
        applyColumnWidths();
        updateScrollPaneStyle();

        revalidate();
        repaint();

        Component parent = getParent();

        if (parent != null) {
            parent.revalidate();
            parent.repaint();

            Component grandParent = parent.getParent();

            if (grandParent != null) {
                grandParent.revalidate();
                grandParent.repaint();
            }
        }
    }

    private void applyHorizontalScroll() {
        if (horizontalScrollEnabled) {
            setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        } else {
            setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
        }
    }

    private void applyColumnWidths() {
        if (getColumnModel() == null) {
            return;
        }

        if (defaultColumnWidth > 0) {
            for (int i = 0; i < getColumnModel().getColumnCount(); i++) {
                getColumnModel().getColumn(i).setPreferredWidth(defaultColumnWidth);
            }
        }

        if (columnWidths == null || columnWidths.trim().isEmpty()) {
            return;
        }

        String[] widths = columnWidths.split(",");

        for (int i = 0; i < widths.length; i++) {
            if (i >= getColumnModel().getColumnCount()) {
                break;
            }

            try {
                int width = Integer.parseInt(widths[i].trim());

                if (width > 0) {
                    getColumnModel().getColumn(i).setPreferredWidth(width);
                }
            } catch (Exception e) {
                /*
                 * Kalau salah isi width, abaikan.
                 */
            }
        }
    }

    private void updateScrollPaneStyle() {
        if (!autoStyleScrollPane) {
            return;
        }

        JScrollPane scrollPane = (JScrollPane) SwingUtilities.getAncestorOfClass(JScrollPane.class, this);

        if (scrollPane == null) {
            Component parent = getParent();

            if (parent instanceof JViewport) {
                Component scroll = parent.getParent();

                if (scroll instanceof JScrollPane) {
                    scrollPane = (JScrollPane) scroll;
                }
            }
        }

        if (scrollPane != null) {
            scrollPane.setBorder(BorderFactory.createLineBorder(outerBorderColor, outerBorderThickness));
            scrollPane.setBackground(tableBackgroundCustom);

            if (scrollPane.getViewport() != null) {
                scrollPane.getViewport().setBackground(tableBackgroundCustom);
                scrollPane.getViewport().revalidate();
                scrollPane.getViewport().repaint();
            }

            scrollPane.revalidate();
            scrollPane.repaint();
        }
    }

    private class HeaderRenderer extends JLabel implements TableCellRenderer {

        public HeaderRenderer() {
            setOpaque(true);
            setBorder(null);
            setVerticalAlignment(SwingConstants.CENTER);
        }

        @Override
        public Component getTableCellRendererComponent(
                JTable table,
                Object value,
                boolean isSelected,
                boolean hasFocus,
                int row,
                int column
        ) {
            int modelColumn = table.convertColumnIndexToModel(column);

            setText(value == null ? "" : value.toString());
            setIcon(null);

            setOpaque(true);
            setBackground(headerBackgroundCustom);
            setForeground(headerForegroundCustom);

            Font font = table.getTableHeader().getFont();

            if (headerBold) {
                setFont(font.deriveFont(Font.BOLD));
            } else {
                setFont(font.deriveFont(Font.PLAIN));
            }

            setHorizontalAlignment(getHeaderAlignment(modelColumn));
            setVerticalAlignment(SwingConstants.CENTER);

            Border padding = BorderFactory.createEmptyBorder(
                    0,
                    headerPaddingLeft,
                    0,
                    headerPaddingRight
            );

            if (headerBorderThickness > 0) {
                Border bottomLine = BorderFactory.createMatteBorder(
                        0,
                        0,
                        headerBorderThickness,
                        0,
                        headerBorderColor
                );

                setBorder(BorderFactory.createCompoundBorder(bottomLine, padding));
            } else {
                setBorder(padding);
            }

            return this;
        }
    }

    private static class ReadOnlyTableHeader extends JTableHeader {

        public ReadOnlyTableHeader(TableColumnModel columnModel) {
            super(columnModel);
            setReorderingAllowed(false);
            setResizingAllowed(false);
            setFocusable(false);
            setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            setOpaque(true);
            setBorder(null);
        }

        @Override
        protected void processMouseEvent(MouseEvent e) {
            /*
             * Header tidak klik, hover, press, resize, reorder.
             */
        }

        @Override
        protected void processMouseMotionEvent(MouseEvent e) {
            /*
             * Header tidak hover.
             */
        }
    }

    public void addActionListener(ActionListener listener) {
        listenerList.add(ActionListener.class, listener);
    }

    public void removeActionListener(ActionListener listener) {
        listenerList.remove(ActionListener.class, listener);
    }

    protected void fireActionPerformed(ActionEvent event) {
        Object[] listeners = listenerList.getListenerList();

        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == ActionListener.class) {
                ((ActionListener) listeners[i + 1]).actionPerformed(event);
            }
        }
    }

    public Object getValueAtLastClickedRow(int column) {
        if (lastClickedModelRow < 0) {
            return null;
        }

        return getModel().getValueAt(lastClickedModelRow, column);
    }

    public void clearClickedRow() {
        clickedModelRow = -1;
        repaint();
    }

    public boolean isDesignPreviewData() {
        return designPreviewData;
    }

    public void setDesignPreviewData(boolean designPreviewData) {
        boolean old = this.designPreviewData;
        this.designPreviewData = designPreviewData;

        installDesignPreviewData();

        firePropertyChange("designPreviewData", old, this.designPreviewData);
        refreshDesign();
    }

    public Color getTableBackgroundCustom() {
        return tableBackgroundCustom;
    }

    public void setTableBackgroundCustom(Color tableBackgroundCustom) {
        Color old = this.tableBackgroundCustom;
        this.tableBackgroundCustom = tableBackgroundCustom;

        firePropertyChange("tableBackgroundCustom", old, this.tableBackgroundCustom);
        refreshDesign();
    }

    public boolean isAutoStyleScrollPane() {
        return autoStyleScrollPane;
    }

    public void setAutoStyleScrollPane(boolean autoStyleScrollPane) {
        boolean old = this.autoStyleScrollPane;
        this.autoStyleScrollPane = autoStyleScrollPane;

        firePropertyChange("autoStyleScrollPane", old, this.autoStyleScrollPane);
        refreshDesign();
    }

    public Color getOuterBorderColor() {
        return outerBorderColor;
    }

    public void setOuterBorderColor(Color outerBorderColor) {
        Color old = this.outerBorderColor;
        this.outerBorderColor = outerBorderColor;

        firePropertyChange("outerBorderColor", old, this.outerBorderColor);
        refreshDesign();
    }

    public int getOuterBorderThickness() {
        return outerBorderThickness;
    }

    public void setOuterBorderThickness(int outerBorderThickness) {
        int old = this.outerBorderThickness;
        this.outerBorderThickness = Math.max(0, outerBorderThickness);

        firePropertyChange("outerBorderThickness", old, this.outerBorderThickness);
        refreshDesign();
    }

    public Color getHeaderBackgroundCustom() {
        return headerBackgroundCustom;
    }

    public void setHeaderBackgroundCustom(Color headerBackgroundCustom) {
        Color old = this.headerBackgroundCustom;
        this.headerBackgroundCustom = headerBackgroundCustom;

        firePropertyChange("headerBackgroundCustom", old, this.headerBackgroundCustom);
        refreshDesign();
    }

    public Color getHeaderForegroundCustom() {
        return headerForegroundCustom;
    }

    public void setHeaderForegroundCustom(Color headerForegroundCustom) {
        Color old = this.headerForegroundCustom;
        this.headerForegroundCustom = headerForegroundCustom;

        firePropertyChange("headerForegroundCustom", old, this.headerForegroundCustom);
        refreshDesign();
    }

    public Color getHeaderBorderColor() {
        return headerBorderColor;
    }

    public void setHeaderBorderColor(Color headerBorderColor) {
        Color old = this.headerBorderColor;
        this.headerBorderColor = headerBorderColor;

        firePropertyChange("headerBorderColor", old, this.headerBorderColor);
        refreshDesign();
    }

    public int getHeaderBorderThickness() {
        return headerBorderThickness;
    }

    public void setHeaderBorderThickness(int headerBorderThickness) {
        int old = this.headerBorderThickness;
        this.headerBorderThickness = Math.max(0, headerBorderThickness);

        firePropertyChange("headerBorderThickness", old, this.headerBorderThickness);
        refreshDesign();
    }

    public int getHeaderHeight() {
        return headerHeight;
    }

    public void setHeaderHeight(int headerHeight) {
        int old = this.headerHeight;
        this.headerHeight = Math.max(1, headerHeight);

        firePropertyChange("headerHeight", old, this.headerHeight);
        refreshDesign();
    }

    public boolean isHeaderBold() {
        return headerBold;
    }

    public void setHeaderBold(boolean headerBold) {
        boolean old = this.headerBold;
        this.headerBold = headerBold;

        firePropertyChange("headerBold", old, this.headerBold);
        refreshDesign();
    }

    public int getHeaderHorizontalAlignment() {
        return headerHorizontalAlignment;
    }

    public void setHeaderHorizontalAlignment(int headerHorizontalAlignment) {
        int old = this.headerHorizontalAlignment;
        this.headerHorizontalAlignment = headerHorizontalAlignment;

        firePropertyChange("headerHorizontalAlignment", old, this.headerHorizontalAlignment);
        refreshDesign();
    }

    public boolean isHeaderFollowColumnAlignment() {
        return headerFollowColumnAlignment;
    }

    public void setHeaderFollowColumnAlignment(boolean headerFollowColumnAlignment) {
        boolean old = this.headerFollowColumnAlignment;
        this.headerFollowColumnAlignment = headerFollowColumnAlignment;

        firePropertyChange("headerFollowColumnAlignment", old, this.headerFollowColumnAlignment);
        refreshDesign();
    }

    public String getHeaderLeftColumns() {
        return headerLeftColumns;
    }

    public void setHeaderLeftColumns(String headerLeftColumns) {
        String old = this.headerLeftColumns;
        this.headerLeftColumns = headerLeftColumns;

        firePropertyChange("headerLeftColumns", old, this.headerLeftColumns);
        refreshDesign();
    }

    public String getHeaderCenterColumns() {
        return headerCenterColumns;
    }

    public void setHeaderCenterColumns(String headerCenterColumns) {
        String old = this.headerCenterColumns;
        this.headerCenterColumns = headerCenterColumns;

        firePropertyChange("headerCenterColumns", old, this.headerCenterColumns);
        refreshDesign();
    }

    public String getHeaderRightColumns() {
        return headerRightColumns;
    }

    public void setHeaderRightColumns(String headerRightColumns) {
        String old = this.headerRightColumns;
        this.headerRightColumns = headerRightColumns;

        firePropertyChange("headerRightColumns", old, this.headerRightColumns);
        refreshDesign();
    }

    public int getHeaderPaddingLeft() {
        return headerPaddingLeft;
    }

    public void setHeaderPaddingLeft(int headerPaddingLeft) {
        int old = this.headerPaddingLeft;
        this.headerPaddingLeft = Math.max(0, headerPaddingLeft);

        firePropertyChange("headerPaddingLeft", old, this.headerPaddingLeft);
        refreshDesign();
    }

    public int getHeaderPaddingRight() {
        return headerPaddingRight;
    }

    public void setHeaderPaddingRight(int headerPaddingRight) {
        int old = this.headerPaddingRight;
        this.headerPaddingRight = Math.max(0, headerPaddingRight);

        firePropertyChange("headerPaddingRight", old, this.headerPaddingRight);
        refreshDesign();
    }

    public Color getRowBorderColor() {
        return rowBorderColor;
    }

    public void setRowBorderColor(Color rowBorderColor) {
        Color old = this.rowBorderColor;
        this.rowBorderColor = rowBorderColor;

        firePropertyChange("rowBorderColor", old, this.rowBorderColor);
        refreshDesign();
    }

    public int getRowBorderThickness() {
        return rowBorderThickness;
    }

    public void setRowBorderThickness(int rowBorderThickness) {
        int old = this.rowBorderThickness;
        this.rowBorderThickness = Math.max(0, rowBorderThickness);

        firePropertyChange("rowBorderThickness", old, this.rowBorderThickness);
        refreshDesign();
    }

    public boolean isShowRowBorder() {
        return showRowBorder;
    }

    public void setShowRowBorder(boolean showRowBorder) {
        boolean old = this.showRowBorder;
        this.showRowBorder = showRowBorder;

        firePropertyChange("showRowBorder", old, this.showRowBorder);
        refreshDesign();
    }

    public Color getTextColorCustom() {
        return textColorCustom;
    }

    public void setTextColorCustom(Color textColorCustom) {
        Color old = this.textColorCustom;
        this.textColorCustom = textColorCustom;

        firePropertyChange("textColorCustom", old, this.textColorCustom);
        refreshDesign();
    }

    public int getCellPaddingTop() {
        return cellPaddingTop;
    }

    public void setCellPaddingTop(int cellPaddingTop) {
        int old = this.cellPaddingTop;
        this.cellPaddingTop = Math.max(0, cellPaddingTop);

        firePropertyChange("cellPaddingTop", old, this.cellPaddingTop);
        refreshDesign();
    }

    public int getCellPaddingLeft() {
        return cellPaddingLeft;
    }

    public void setCellPaddingLeft(int cellPaddingLeft) {
        int old = this.cellPaddingLeft;
        this.cellPaddingLeft = Math.max(0, cellPaddingLeft);

        firePropertyChange("cellPaddingLeft", old, this.cellPaddingLeft);
        refreshDesign();
    }

    public int getCellPaddingBottom() {
        return cellPaddingBottom;
    }

    public void setCellPaddingBottom(int cellPaddingBottom) {
        int old = this.cellPaddingBottom;
        this.cellPaddingBottom = Math.max(0, cellPaddingBottom);

        firePropertyChange("cellPaddingBottom", old, this.cellPaddingBottom);
        refreshDesign();
    }

    public int getCellPaddingRight() {
        return cellPaddingRight;
    }

    public void setCellPaddingRight(int cellPaddingRight) {
        int old = this.cellPaddingRight;
        this.cellPaddingRight = Math.max(0, cellPaddingRight);

        firePropertyChange("cellPaddingRight", old, this.cellPaddingRight);
        refreshDesign();
    }

    public int getCellHorizontalAlignment() {
        return cellHorizontalAlignment;
    }

    public void setCellHorizontalAlignment(int cellHorizontalAlignment) {
        int old = this.cellHorizontalAlignment;
        this.cellHorizontalAlignment = cellHorizontalAlignment;

        firePropertyChange("cellHorizontalAlignment", old, this.cellHorizontalAlignment);
        refreshDesign();
    }

    public String getLeftColumns() {
        return leftColumns;
    }

    public void setLeftColumns(String leftColumns) {
        String old = this.leftColumns;
        this.leftColumns = leftColumns;

        firePropertyChange("leftColumns", old, this.leftColumns);
        refreshDesign();
    }

    public String getCenterColumns() {
        return centerColumns;
    }

    public void setCenterColumns(String centerColumns) {
        String old = this.centerColumns;
        this.centerColumns = centerColumns;

        firePropertyChange("centerColumns", old, this.centerColumns);
        refreshDesign();
    }

    public String getRightColumns() {
        return rightColumns;
    }

    public void setRightColumns(String rightColumns) {
        String old = this.rightColumns;
        this.rightColumns = rightColumns;

        firePropertyChange("rightColumns", old, this.rightColumns);
        refreshDesign();
    }

    public String getBoldColumns() {
        return boldColumns;
    }

    public void setBoldColumns(String boldColumns) {
        String old = this.boldColumns;
        this.boldColumns = boldColumns;

        firePropertyChange("boldColumns", old, this.boldColumns);
        refreshDesign();
    }

    public String getIconColumns() {
        return iconColumns;
    }

    public void setIconColumns(String iconColumns) {
        String old = this.iconColumns;
        this.iconColumns = iconColumns;

        firePropertyChange("iconColumns", old, this.iconColumns);
        refreshDesign();
    }

    public String getIconRows() {
        return iconRows;
    }

    public void setIconRows(String iconRows) {
        String old = this.iconRows;
        this.iconRows = iconRows;

        firePropertyChange("iconRows", old, this.iconRows);
        refreshDesign();
    }

    public Icon getCellIcon() {
        return cellIcon;
    }

    public void setCellIcon(Icon cellIcon) {
        Icon old = this.cellIcon;
        this.cellIcon = cellIcon;

        firePropertyChange("cellIcon", old, this.cellIcon);
        refreshDesign();
    }

    public boolean isShowCellIconOnly() {
        return showCellIconOnly;
    }

    public void setShowCellIconOnly(boolean showCellIconOnly) {
        boolean old = this.showCellIconOnly;
        this.showCellIconOnly = showCellIconOnly;

        firePropertyChange("showCellIconOnly", old, this.showCellIconOnly);
        refreshDesign();
    }

    public int getCellIconTextGap() {
        return cellIconTextGap;
    }

    public void setCellIconTextGap(int cellIconTextGap) {
        int old = this.cellIconTextGap;
        this.cellIconTextGap = Math.max(0, cellIconTextGap);

        firePropertyChange("cellIconTextGap", old, this.cellIconTextGap);
        refreshDesign();
    }

    public boolean isRowClickable() {
        return rowClickable;
    }

    public void setRowClickable(boolean rowClickable) {
        boolean old = this.rowClickable;
        this.rowClickable = rowClickable;

        firePropertyChange("rowClickable", old, this.rowClickable);
        refreshDesign();
    }

    public boolean isIconCellClickable() {
        return iconCellClickable;
    }

    public void setIconCellClickable(boolean iconCellClickable) {
        boolean old = this.iconCellClickable;
        this.iconCellClickable = iconCellClickable;

        firePropertyChange("iconCellClickable", old, this.iconCellClickable);
        refreshDesign();
    }

    public boolean isClickedRowBackgroundEnabled() {
        return clickedRowBackgroundEnabled;
    }

    public void setClickedRowBackgroundEnabled(boolean clickedRowBackgroundEnabled) {
        boolean old = this.clickedRowBackgroundEnabled;
        this.clickedRowBackgroundEnabled = clickedRowBackgroundEnabled;

        firePropertyChange("clickedRowBackgroundEnabled", old, this.clickedRowBackgroundEnabled);
        refreshDesign();
    }

    public Color getClickedRowBackgroundCustom() {
        return clickedRowBackgroundCustom;
    }

    public void setClickedRowBackgroundCustom(Color clickedRowBackgroundCustom) {
        Color old = this.clickedRowBackgroundCustom;
        this.clickedRowBackgroundCustom = clickedRowBackgroundCustom;

        firePropertyChange("clickedRowBackgroundCustom", old, this.clickedRowBackgroundCustom);
        refreshDesign();
    }

    public Color getClickedRowForegroundCustom() {
        return clickedRowForegroundCustom;
    }

    public void setClickedRowForegroundCustom(Color clickedRowForegroundCustom) {
        Color old = this.clickedRowForegroundCustom;
        this.clickedRowForegroundCustom = clickedRowForegroundCustom;

        firePropertyChange("clickedRowForegroundCustom", old, this.clickedRowForegroundCustom);
        refreshDesign();
    }

    public String getRowActionCommand() {
        return rowActionCommand;
    }

    public void setRowActionCommand(String rowActionCommand) {
        String old = this.rowActionCommand;
        this.rowActionCommand = rowActionCommand;

        firePropertyChange("rowActionCommand", old, this.rowActionCommand);
        refreshDesign();
    }

    public String getIconActionCommand() {
        return iconActionCommand;
    }

    public void setIconActionCommand(String iconActionCommand) {
        String old = this.iconActionCommand;
        this.iconActionCommand = iconActionCommand;

        firePropertyChange("iconActionCommand", old, this.iconActionCommand);
        refreshDesign();
    }

    public boolean isHorizontalScrollEnabled() {
        return horizontalScrollEnabled;
    }

    public void setHorizontalScrollEnabled(boolean horizontalScrollEnabled) {
        boolean old = this.horizontalScrollEnabled;
        this.horizontalScrollEnabled = horizontalScrollEnabled;

        firePropertyChange("horizontalScrollEnabled", old, this.horizontalScrollEnabled);
        refreshDesign();
    }

    public int getDefaultColumnWidth() {
        return defaultColumnWidth;
    }

    public void setDefaultColumnWidth(int defaultColumnWidth) {
        int old = this.defaultColumnWidth;
        this.defaultColumnWidth = Math.max(1, defaultColumnWidth);

        firePropertyChange("defaultColumnWidth", old, this.defaultColumnWidth);
        refreshDesign();
    }

    public String getColumnWidths() {
        return columnWidths;
    }

    public void setColumnWidths(String columnWidths) {
        String old = this.columnWidths;
        this.columnWidths = columnWidths;

        firePropertyChange("columnWidths", old, this.columnWidths);
        refreshDesign();
    }

    public int getLastClickedModelRow() {
        return lastClickedModelRow;
    }

    public int getLastClickedModelColumn() {
        return lastClickedModelColumn;
    }

    public int getClickedModelRow() {
        return clickedModelRow;
    }

    public void setClickedModelRow(int clickedModelRow) {
        int old = this.clickedModelRow;
        this.clickedModelRow = clickedModelRow;

        firePropertyChange("clickedModelRow", old, this.clickedModelRow);
        refreshDesign();
    }
}