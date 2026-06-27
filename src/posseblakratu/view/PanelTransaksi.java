/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package posseblakratu.view;

import com.formdev.flatlaf.ui.FlatLineBorder;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Insets;
import java.util.List;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import posseblakratu.component.cardMenu;
import javax.swing.Box;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import posseblakratu.component.cardKeranjang;
import posseblakratu.component.cardTopping;
import posseblakratu.config.Koneksi;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 *
 * @author Al
 */
public final class PanelTransaksi extends javax.swing.JPanel {

    private final List<cardMenu> daftarCard = new ArrayList<>(); //Membuat list penyimpan card untuk menyimpan semua card menu
    
    private CardLayout cardLayoutC;

    /**
     * Creates new form PanelTransaksi
     */
    public PanelTransaksi() {
        initComponents();
        
        cardLayoutC = (CardLayout)
        containerKostumisasi1.getLayout();
        cardLayoutC.show(containerKostumisasi1, "kostumKosong");

        buttonDesain();

        panelLengkung(containerDaftarMenu);
        panelLengkung(containerKostumisasi1);
        panelLengkung(containerKeranjang1);

        buttonLvl(btnLvl0);
        buttonLvl(btnLvl1);
        buttonLvl(btnLvl2);
        buttonLvl(btnLvl3);
        buttonLvl(btnLvl4);
        buttonLvl(btnLvl5);

        filterSemuaM.setSelected(true);
        btnFilter(filterSemuaM);

        loadMenu("Semua");
        loadTopping();
        
    }
    
    
    
    
    
    

    
    
    
    
    //method styling panel
    void panelLengkung(JPanel p) {
        p.setBorder(new FlatLineBorder(
                new Insets(5, 5, 5, 5),
                Color.decode("#E7BDBB"),
                1f,
                10));
    }
    
    
    
    
    
    //method styling button
    void buttonDesain() {
        btnReset.putClientProperty("FlatLaf.style",
                "borderWidth:1; "
                + "background:#FBF8FF;"
                + "pressedBackground:#F6F0FF;"
                + "arc:8;"
                + "borderColor:#E7BCBA;"
                + "focusedBorderColor:#E7BCBA; "
                + "hoverBorderColor:#E7BCBA");

        btnNext.putClientProperty("FlatLaf.style",
                "borderWidth:1; "
                + "pressedBackground:#F6F0FF;"
                + "background:#FBF8FF;"
                + "arc:8; "
                + "borderColor:#BA1A1A; "
                + "focusedBorderColor:#BA1A1A; "
                + "hoverBorderColor:#BA1A1A"
        );
    }
    
    
    
    
    //method styling selected dan !selected pd button lvl
    void buttonLvl(JToggleButton btn) {
        if (btn.isSelected()) {
            btn.setBorder(new FlatLineBorder(
                    new Insets(0, 0, 0, 0),
                    Color.decode("#EA580C"),
                    2f,
                    10));
        } else {
            btn.setBorder(new FlatLineBorder(
                    new Insets(1, 1, 1, 1),
                    Color.decode("#E7BCBA"),
                    1f,
                    10));
        }
    }
    
    
    
    
    //method styling button filter menu
    void btnFilter(JToggleButton btn) {
        if (btn.isSelected()) {
            btn.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 3, 0, new java.awt.Color(173, 0, 28)));
        } else {
            btn.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 3, 0));
        }
    }
    
    
    
    
    //method untuk menampilkan menu berdasarkan kategori
    private void loadMenu(String kategori) {

        //membersihkan panel
        menuContent.removeAll();

        //membersihkan data card sebelumnya
        daftarCard.clear();

        try {

            String sql;

            //memeriksa kategori yang dipilih
            if (kategori.equals("Semua")) {

                //query sql untuk mengambil seluruh menu selain topping
                sql = "SELECT * FROM produk "
                        + "WHERE kategori IN ('Seblak','Minuman') "
                        + "ORDER BY CASE "
                        + "WHEN kategori='Seblak' THEN 1 "
                        + "WHEN kategori='Minuman' THEN 2 "
                        + "END, id_produk";

            } else {

                //query sql untuk mengambil menu berdasarkan kategori
                sql = "SELECT * FROM produk WHERE kategori=? ORDER BY id_produk";

            }

            //membuat koneksi ke database
            Connection con = Koneksi.konek();

            //siapkan statement sql
            PreparedStatement ps = con.prepareStatement(sql);

            //mengisi parameter kategori jika bukan semua
            if (!kategori.equals("Semua")) {
                ps.setString(1, kategori);
            }

            //jalankan query dan ambil hasilnya
            ResultSet rs = ps.executeQuery();

            //melakukan perulangan selama data masih ada
            while (rs.next()) {

                //membuat card menu baru
                cardMenu card = new cardMenu();

                //mengisi data pada card menu
                card.setData(
                        rs.getString("nama_produk"),
                        rs.getDouble("harga_jual"),
                        rs.getString("deskripsi")
                );

                //menyimpan card ke dalam daftar
                daftarCard.add(card);

                //menambahkan event klik pada card
                card.addClickListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {

                        //menjadikan card yang dipilih sebagai selected
                        selectCard(card);

                    }
                });

                //menambahkan card ke panel menu
                menuContent.add(card);

                //memberikan jarak antar card
                menuContent.add(Box.createVerticalStrut(10));
            }

        } catch (SQLException sQLException) {

            //jika terjadi kesalahan sql, tampilkan pesan error
            JOptionPane.showMessageDialog(null, sQLException.getMessage());

        }

        //memperbarui tampilan panel
        menuContent.revalidate();
        menuContent.repaint();

        //mengembalikan posisi scroll ke paling atas
        SwingUtilities.invokeLater(() -> {
            jScrollPane1.getVerticalScrollBar().setValue(0);
        });
    }
    
    
    
    

    //membuat method selected card
    private void selectCard(cardMenu selectedCard) {
        for (cardMenu card : daftarCard) {
            card.setSelected(false);
        }
        selectedCard.setSelected(true);
        showKostum();
    }

    void showKostum() {
        cardLayoutC.show(containerKostumisasi1, "kostum");
    }

    void reset() {

        for (cardMenu card : daftarCard) {
            card.setSelected(false);
        }
        cardLayoutC.show(containerKostumisasi1, "kostumKosong");
    }
    
    
    

    //method untuk menampilkan topping
    private void loadTopping() {

        toppingContent.removeAll();

        for (int i = 1; i <= 10; i++) {

            cardTopping card = new cardTopping();

            toppingContent.add(card);

            toppingContent.add(
                    Box.createVerticalStrut(10)
            );
        }

        toppingContent.revalidate();
        toppingContent.repaint();
    }
    
    
    

    //method untuk menampilkan keranjang
    private void loadKeranjang() {

        keranjangContent.removeAll();

        for (int i = 1; i <= 1; i++) {

            cardKeranjang card = new cardKeranjang();

            keranjangContent.add(card);

            keranjangContent.add(
                    Box.createVerticalStrut(10)
            );

        }

        keranjangContent.revalidate();
        keranjangContent.repaint();
    }
    
    
    
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroupFilter = new javax.swing.ButtonGroup();
        buttonGroupLvl = new javax.swing.ButtonGroup();
        containerDaftarMenu = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        filterSemuaM = new javax.swing.JToggleButton();
        filterSeblakM = new javax.swing.JToggleButton();
        filterMinumanM = new javax.swing.JToggleButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jScrollPane1.getVerticalScrollBar().setUnitIncrement(10);
        menuContent = new javax.swing.JPanel();
        containerKostumisasi1 = new javax.swing.JPanel();
        containerKostumisasi = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        lblMenu = new javax.swing.JLabel();
        jPanel14 = new javax.swing.JPanel();
        jPanel17 = new javax.swing.JPanel();
        btnReset = new javax.swing.JButton();
        btnNext = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jPanel16 = new javax.swing.JPanel();
        btnLvl0 = new javax.swing.JToggleButton();
        btnLvl1 = new javax.swing.JToggleButton();
        btnLvl2 = new javax.swing.JToggleButton();
        btnLvl3 = new javax.swing.JToggleButton();
        btnLvl4 = new javax.swing.JToggleButton();
        btnLvl5 = new javax.swing.JToggleButton();
        jLabel8 = new javax.swing.JLabel();
        jPanel15 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jScrollPane3.getVerticalScrollBar().setUnitIncrement(10);
        toppingContent = new javax.swing.JPanel();
        containerKostumisasiKosong = new javax.swing.JPanel();
        jPanel19 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        lblMenu1 = new javax.swing.JLabel();
        jPanel20 = new javax.swing.JPanel();
        jPanel21 = new javax.swing.JPanel();
        btnNext1 = new javax.swing.JButton();
        btnReset1 = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        containerKeranjang1 = new javax.swing.JPanel();
        KeranjangIsi = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jPanel18 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jPanel11 = new javax.swing.JPanel();
        jPanel13 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        lblSubtotal = new javax.swing.JLabel();
        lblDiskon = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel11 = new javax.swing.JLabel();
        lblTotal = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        btnBayar = new javax.swing.JButton();
        jPanel12 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        keranjangContent = new javax.swing.JPanel();
        jScrollPane2.getVerticalScrollBar().setUnitIncrement(10);
        KeranjangKosong = new javax.swing.JPanel();
        jPanel22 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jPanel23 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jPanel24 = new javax.swing.JPanel();
        jPanel25 = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        lblSubtotal1 = new javax.swing.JLabel();
        lblDiskon1 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jSeparator3 = new javax.swing.JSeparator();
        jLabel13 = new javax.swing.JLabel();
        lblTotal1 = new javax.swing.JLabel();
        jSeparator4 = new javax.swing.JSeparator();
        btnBayar1 = new javax.swing.JButton();
        jPanel26 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        keranjangContent1 = new javax.swing.JPanel();
        jScrollPane2.getVerticalScrollBar().setUnitIncrement(10);

        setBackground(new java.awt.Color(252, 249, 255));
        setBorder(javax.swing.BorderFactory.createEmptyBorder(15, 15, 15, 15));
        setMinimumSize(new java.awt.Dimension(1075, 639));
        setPreferredSize(new java.awt.Dimension(1075, 639));
        setLayout(new java.awt.GridLayout(1, 3, 15, 15));

        containerDaftarMenu.setBackground(new java.awt.Color(255, 255, 255));
        containerDaftarMenu.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(231, 189, 187)));
        containerDaftarMenu.setLayout(new java.awt.BorderLayout());

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(231, 189, 187)));
        jPanel4.setOpaque(false);
        jPanel4.setPreferredSize(new java.awt.Dimension(337, 87));
        jPanel4.setLayout(new java.awt.BorderLayout());

        jLabel1.setFont(new java.awt.Font("Plus Jakarta Sans SemiBold", 0, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(24, 26, 46));
        jLabel1.setText("Daftar Menu");
        jLabel1.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jLabel1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 16, 1, 1));
        jLabel1.setPreferredSize(new java.awt.Dimension(37, 40));
        jPanel4.add(jLabel1, java.awt.BorderLayout.PAGE_START);

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));
        jPanel6.setOpaque(false);

        buttonGroupFilter.add(filterSemuaM);
        filterSemuaM.setFont(new java.awt.Font("Plus Jakarta Sans", 1, 14)); // NOI18N
        filterSemuaM.setForeground(new java.awt.Color(92, 62, 60));
        filterSemuaM.setText("Semua");
        filterSemuaM.setAlignmentY(0.0F);
        filterSemuaM.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 3, 0, new java.awt.Color(255, 255, 255)));
        filterSemuaM.setContentAreaFilled(false);
        filterSemuaM.setFocusable(false);
        filterSemuaM.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        filterSemuaM.setIconTextGap(0);
        filterSemuaM.setMargin(new java.awt.Insets(0, 0, 0, 0));
        filterSemuaM.addItemListener(this::filterSemuaMItemStateChanged);

        buttonGroupFilter.add(filterSeblakM);
        filterSeblakM.setFont(new java.awt.Font("Plus Jakarta Sans", 1, 14)); // NOI18N
        filterSeblakM.setForeground(new java.awt.Color(92, 62, 60));
        filterSeblakM.setText("Seblak");
        filterSeblakM.setAlignmentY(0.0F);
        filterSeblakM.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 3, 0, new java.awt.Color(255, 255, 255)));
        filterSeblakM.setContentAreaFilled(false);
        filterSeblakM.setFocusable(false);
        filterSeblakM.setIconTextGap(0);
        filterSeblakM.setMargin(new java.awt.Insets(1, 1, 20, 1));
        filterSeblakM.addItemListener(this::filterSeblakMItemStateChanged);

        buttonGroupFilter.add(filterMinumanM);
        filterMinumanM.setFont(new java.awt.Font("Plus Jakarta Sans", 1, 14)); // NOI18N
        filterMinumanM.setForeground(new java.awt.Color(92, 62, 60));
        filterMinumanM.setText("Minuman");
        filterMinumanM.setAlignmentY(0.0F);
        filterMinumanM.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 3, 0, new java.awt.Color(255, 255, 255)));
        filterMinumanM.setContentAreaFilled(false);
        filterMinumanM.setFocusable(false);
        filterMinumanM.setIconTextGap(0);
        filterMinumanM.setMargin(new java.awt.Insets(1, 1, 20, 1));
        filterMinumanM.addItemListener(this::filterMinumanMItemStateChanged);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(filterSemuaM)
                .addGap(12, 12, 12)
                .addComponent(filterSeblakM)
                .addGap(12, 12, 12)
                .addComponent(filterMinumanM)
                .addContainerGap(140, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(filterMinumanM, javax.swing.GroupLayout.DEFAULT_SIZE, 46, Short.MAX_VALUE)
            .addComponent(filterSeblakM, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(filterSemuaM, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jPanel4.add(jPanel6, java.awt.BorderLayout.CENTER);

        containerDaftarMenu.add(jPanel4, java.awt.BorderLayout.PAGE_START);

        jScrollPane1.setBackground(new java.awt.Color(255, 255, 255));
        jScrollPane1.setBorder(null);
        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        jScrollPane1.setOpaque(false);

        menuContent.setBackground(new java.awt.Color(255, 255, 255));
        menuContent.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 0, 10));
        menuContent.setLayout(new javax.swing.BoxLayout(menuContent, javax.swing.BoxLayout.Y_AXIS));
        jScrollPane1.setViewportView(menuContent);

        containerDaftarMenu.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        add(containerDaftarMenu);

        containerKostumisasi1.setBackground(new java.awt.Color(255, 255, 255));
        containerKostumisasi1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(231, 189, 187)));
        containerKostumisasi1.setLayout(new java.awt.CardLayout());

        containerKostumisasi.setBackground(new java.awt.Color(255, 255, 255));
        containerKostumisasi.setLayout(new java.awt.BorderLayout());

        jPanel8.setBackground(new java.awt.Color(255, 255, 255));
        jPanel8.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(231, 189, 187)));
        jPanel8.setPreferredSize(new java.awt.Dimension(335, 63));

        jLabel5.setFont(new java.awt.Font("Plus Jakarta Sans SemiBold", 0, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(24, 26, 46));
        jLabel5.setText("Kostumisasi");
        jLabel5.setPreferredSize(new java.awt.Dimension(99, 19));

        lblMenu.setFont(new java.awt.Font("Plus Jakarta Sans", 0, 14)); // NOI18N
        lblMenu.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblMenu.setText("Seblak Original");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblMenu)
                .addGap(16, 16, 16))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, 33, Short.MAX_VALUE)
                    .addComponent(lblMenu, javax.swing.GroupLayout.DEFAULT_SIZE, 33, Short.MAX_VALUE))
                .addGap(15, 15, 15))
        );

        containerKostumisasi.add(jPanel8, java.awt.BorderLayout.PAGE_START);

        jPanel14.setBackground(new java.awt.Color(255, 255, 255));
        jPanel14.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 0, 0, new java.awt.Color(231, 189, 187)));
        jPanel14.setPreferredSize(new java.awt.Dimension(335, 43));
        jPanel14.setLayout(new java.awt.BorderLayout());

        jPanel17.setBackground(new java.awt.Color(255, 255, 255));
        jPanel17.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        jPanel17.setLayout(new java.awt.GridLayout(1, 2, 5, 0));

        btnReset.setBackground(new java.awt.Color(251, 248, 255));
        btnReset.setFont(new java.awt.Font("Plus Jakarta Sans SemiBold", 0, 16)); // NOI18N
        btnReset.setText("Reset");
        btnReset.setFocusPainted(false);
        btnReset.addActionListener(this::btnResetActionPerformed);
        jPanel17.add(btnReset);

        btnNext.setBackground(new java.awt.Color(251, 248, 255));
        btnNext.setFont(new java.awt.Font("Plus Jakarta Sans SemiBold", 0, 16)); // NOI18N
        btnNext.setForeground(new java.awt.Color(187, 26, 26));
        btnNext.setText("Next");
        btnNext.setFocusPainted(false);
        btnNext.addActionListener(this::btnNextActionPerformed);
        jPanel17.add(btnNext);

        jPanel14.add(jPanel17, java.awt.BorderLayout.CENTER);

        containerKostumisasi.add(jPanel14, java.awt.BorderLayout.PAGE_END);

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));
        jPanel5.setLayout(new java.awt.BorderLayout());

        jPanel9.setBackground(new java.awt.Color(255, 255, 255));
        jPanel9.setBorder(javax.swing.BorderFactory.createEmptyBorder(13, 20, 0, 20));
        jPanel9.setPreferredSize(new java.awt.Dimension(335, 200));

        jLabel7.setFont(new java.awt.Font("Plus Jakarta Sans SemiBold", 0, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(24, 26, 46));
        jLabel7.setText("Level Pedas");
        jLabel7.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jLabel7.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 1, 0, 0));

        jPanel16.setBackground(new java.awt.Color(255, 255, 255));
        jPanel16.setLayout(new java.awt.GridLayout(3, 0, 6, 6));

        buttonGroupLvl.add(btnLvl0);
        btnLvl0.setFont(new java.awt.Font("Plus Jakarta Sans", 0, 13)); // NOI18N
        btnLvl0.setText("0 - Ratu Takut");
        btnLvl0.setContentAreaFilled(false);
        btnLvl0.addItemListener(this::btnLvl0ItemStateChanged);
        jPanel16.add(btnLvl0);

        buttonGroupLvl.add(btnLvl1);
        btnLvl1.setFont(new java.awt.Font("Plus Jakarta Sans", 0, 13)); // NOI18N
        btnLvl1.setText("1 - Ratu Baik");
        btnLvl1.setContentAreaFilled(false);
        btnLvl1.addItemListener(this::btnLvl1ItemStateChanged);
        jPanel16.add(btnLvl1);

        buttonGroupLvl.add(btnLvl2);
        btnLvl2.setFont(new java.awt.Font("Plus Jakarta Sans", 0, 13)); // NOI18N
        btnLvl2.setText("2 - Ratu Santuy");
        btnLvl2.setContentAreaFilled(false);
        btnLvl2.addItemListener(this::btnLvl2ItemStateChanged);
        jPanel16.add(btnLvl2);

        buttonGroupLvl.add(btnLvl3);
        btnLvl3.setFont(new java.awt.Font("Plus Jakarta Sans", 0, 13)); // NOI18N
        btnLvl3.setText("3 - Ratu Judes");
        btnLvl3.setContentAreaFilled(false);
        btnLvl3.addItemListener(this::btnLvl3ItemStateChanged);
        jPanel16.add(btnLvl3);

        buttonGroupLvl.add(btnLvl4);
        btnLvl4.setFont(new java.awt.Font("Plus Jakarta Sans", 0, 13)); // NOI18N
        btnLvl4.setText("4 - Ratu Marah");
        btnLvl4.setContentAreaFilled(false);
        btnLvl4.addItemListener(this::btnLvl4ItemStateChanged);
        jPanel16.add(btnLvl4);

        buttonGroupLvl.add(btnLvl5);
        btnLvl5.setFont(new java.awt.Font("Plus Jakarta Sans", 0, 13)); // NOI18N
        btnLvl5.setText("5 - Ratu Ngamuk");
        btnLvl5.setContentAreaFilled(false);
        btnLvl5.addItemListener(this::btnLvl5ItemStateChanged);
        jPanel16.add(btnLvl5);

        jLabel8.setFont(new java.awt.Font("Plus Jakarta Sans SemiBold", 0, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(24, 26, 46));
        jLabel8.setText("Topping");
        jLabel8.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jLabel8.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 1, 0, 0));

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel16, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15)
                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel5.add(jPanel9, java.awt.BorderLayout.PAGE_START);

        jPanel15.setBackground(new java.awt.Color(255, 255, 255));

        jScrollPane3.setBorder(null);
        jScrollPane3.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        toppingContent.setBackground(new java.awt.Color(255, 255, 255));
        toppingContent.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 20, 0, 20));
        toppingContent.setLayout(new javax.swing.BoxLayout(toppingContent, javax.swing.BoxLayout.Y_AXIS));
        jScrollPane3.setViewportView(toppingContent);

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 336, Short.MAX_VALUE)
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel15Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 301, Short.MAX_VALUE))
        );

        jPanel5.add(jPanel15, java.awt.BorderLayout.CENTER);

        containerKostumisasi.add(jPanel5, java.awt.BorderLayout.CENTER);

        containerKostumisasi1.add(containerKostumisasi, "kostum");

        containerKostumisasiKosong.setBackground(new java.awt.Color(255, 255, 255));
        containerKostumisasiKosong.setLayout(new java.awt.BorderLayout());

        jPanel19.setBackground(new java.awt.Color(255, 255, 255));
        jPanel19.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(231, 189, 187)));
        jPanel19.setPreferredSize(new java.awt.Dimension(335, 63));

        jLabel10.setFont(new java.awt.Font("Plus Jakarta Sans SemiBold", 0, 18)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(24, 26, 46));
        jLabel10.setText("Kostumisasi");
        jLabel10.setPreferredSize(new java.awt.Dimension(99, 19));

        lblMenu1.setFont(new java.awt.Font("Plus Jakarta Sans", 0, 14)); // NOI18N
        lblMenu1.setForeground(new java.awt.Color(255, 255, 255));
        lblMenu1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblMenu1.setText("Seblak Original");

        javax.swing.GroupLayout jPanel19Layout = new javax.swing.GroupLayout(jPanel19);
        jPanel19.setLayout(jPanel19Layout);
        jPanel19Layout.setHorizontalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblMenu1)
                .addGap(16, 16, 16))
        );
        jPanel19Layout.setVerticalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, 33, Short.MAX_VALUE)
                    .addComponent(lblMenu1, javax.swing.GroupLayout.DEFAULT_SIZE, 33, Short.MAX_VALUE))
                .addGap(15, 15, 15))
        );

        containerKostumisasiKosong.add(jPanel19, java.awt.BorderLayout.PAGE_START);

        jPanel20.setBackground(new java.awt.Color(255, 255, 255));
        jPanel20.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 0, 0, new java.awt.Color(231, 189, 187)));
        jPanel20.setPreferredSize(new java.awt.Dimension(335, 43));
        jPanel20.setLayout(new java.awt.BorderLayout());

        jPanel21.setBackground(new java.awt.Color(255, 255, 255));
        jPanel21.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        jPanel21.setLayout(new java.awt.GridLayout(1, 2, 5, 0));

        btnNext1.setBackground(new java.awt.Color(251, 248, 255));
        btnNext1.setFont(new java.awt.Font("Plus Jakarta Sans SemiBold", 0, 16)); // NOI18N
        btnNext1.setText("Reset");
        btnNext1.setFocusPainted(false);
        btnNext1.addActionListener(this::btnNext1ActionPerformed);
        jPanel21.add(btnNext1);

        btnReset1.setBackground(new java.awt.Color(251, 248, 255));
        btnReset1.setFont(new java.awt.Font("Plus Jakarta Sans SemiBold", 0, 16)); // NOI18N
        btnReset1.setForeground(new java.awt.Color(187, 26, 26));
        btnReset1.setText("Next");
        btnReset1.setFocusPainted(false);
        jPanel21.add(btnReset1);

        jPanel20.add(jPanel21, java.awt.BorderLayout.CENTER);

        containerKostumisasiKosong.add(jPanel20, java.awt.BorderLayout.PAGE_END);

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));
        jPanel7.setLayout(new java.awt.BorderLayout());

        jLabel3.setFont(new java.awt.Font("Plus Jakarta Sans", 0, 16)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Pilih Menu Terlebih Dahulu");
        jPanel7.add(jLabel3, java.awt.BorderLayout.CENTER);

        containerKostumisasiKosong.add(jPanel7, java.awt.BorderLayout.CENTER);

        containerKostumisasi1.add(containerKostumisasiKosong, "kostumKosong");

        add(containerKostumisasi1);

        containerKeranjang1.setBackground(new java.awt.Color(255, 255, 255));
        containerKeranjang1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(231, 189, 187)));
        containerKeranjang1.setLayout(new java.awt.CardLayout());

        KeranjangIsi.setBackground(new java.awt.Color(255, 255, 255));
        KeranjangIsi.setLayout(new java.awt.BorderLayout());

        jPanel10.setBackground(new java.awt.Color(255, 255, 255));
        jPanel10.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(231, 189, 187)));
        jPanel10.setMinimumSize(new java.awt.Dimension(335, 63));
        jPanel10.setPreferredSize(new java.awt.Dimension(335, 63));

        jLabel9.setFont(new java.awt.Font("Plus Jakarta Sans SemiBold", 0, 18)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(24, 26, 46));
        jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/posseblakratu/icon/IconKeranjang.png"))); // NOI18N
        jLabel9.setText("Keranjang");
        jLabel9.setIconTextGap(10);

        jPanel18.setBackground(new java.awt.Color(187, 26, 26));
        jPanel18.setBorder(javax.swing.BorderFactory.createCompoundBorder(null, new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 3, true)));
        jPanel18.setPreferredSize(new java.awt.Dimension(80, 23));
        jPanel18.setLayout(new java.awt.BorderLayout());

        jLabel2.setFont(new java.awt.Font("Plus Jakarta Sans", 1, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("TRX-0001");
        jPanel18.add(jLabel2, java.awt.BorderLayout.CENTER);

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(16, 16, 16))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, 32, Short.MAX_VALUE))
                .addGap(15, 15, 15))
        );

        KeranjangIsi.add(jPanel10, java.awt.BorderLayout.PAGE_START);

        jPanel11.setBackground(new java.awt.Color(255, 255, 255));
        jPanel11.setBorder(javax.swing.BorderFactory.createEmptyBorder(15, 20, 15, 20));
        jPanel11.setPreferredSize(new java.awt.Dimension(335, 212));
        jPanel11.setLayout(new java.awt.BorderLayout());

        jPanel13.setBackground(new java.awt.Color(255, 255, 255));

        jLabel14.setFont(new java.awt.Font("Plus Jakarta Sans", 0, 14)); // NOI18N
        jLabel14.setText("Subtotal");

        lblSubtotal.setFont(new java.awt.Font("Plus Jakarta Sans", 0, 14)); // NOI18N
        lblSubtotal.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblSubtotal.setText("Rp 0");

        lblDiskon.setFont(new java.awt.Font("Plus Jakarta Sans", 0, 14)); // NOI18N
        lblDiskon.setForeground(new java.awt.Color(214, 4, 39));
        lblDiskon.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblDiskon.setText("- Rp 0");

        jLabel15.setFont(new java.awt.Font("Plus Jakarta Sans", 0, 14)); // NOI18N
        jLabel15.setText("Diskon 10% (jumat berkah)");

        jSeparator1.setForeground(new java.awt.Color(231, 189, 187));

        jLabel11.setFont(new java.awt.Font("Plus Jakarta Sans SemiBold", 0, 18)); // NOI18N
        jLabel11.setText("Total");

        lblTotal.setFont(new java.awt.Font("Plus Jakarta Sans SemiBold", 0, 24)); // NOI18N
        lblTotal.setForeground(new java.awt.Color(214, 4, 39));
        lblTotal.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblTotal.setText("Rp 0");

        jSeparator2.setForeground(new java.awt.Color(231, 189, 187));

        btnBayar.setBackground(new java.awt.Color(214, 4, 39));
        btnBayar.setFont(new java.awt.Font("Plus Jakarta Sans SemiBold", 0, 16)); // NOI18N
        btnBayar.setForeground(new java.awt.Color(255, 255, 255));
        btnBayar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/posseblakratu/icon/Icon Bayar.png"))); // NOI18N
        btnBayar.setText("Bayar");
        btnBayar.setBorderPainted(false);
        btnBayar.setIconTextGap(8);
        btnBayar.addActionListener(this::btnBayarActionPerformed);

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addComponent(jLabel14)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblSubtotal, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblDiskon, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(jSeparator1)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(btnBayar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel13Layout.createSequentialGroup()
                .addComponent(jSeparator2)
                .addContainerGap())
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel13Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(lblSubtotal))
                .addGap(10, 10, 10)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(lblDiskon))
                .addGap(13, 13, 13)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 3, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(13, 13, 13)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lblTotal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 9, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 9, Short.MAX_VALUE)
                .addComponent(btnBayar, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );

        jPanel11.add(jPanel13, java.awt.BorderLayout.CENTER);

        KeranjangIsi.add(jPanel11, java.awt.BorderLayout.PAGE_END);

        jPanel12.setBackground(new java.awt.Color(255, 255, 255));
        jPanel12.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(231, 189, 187)));

        jScrollPane2.setBorder(null);
        jScrollPane2.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        keranjangContent.setBackground(new java.awt.Color(255, 255, 255));
        keranjangContent.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 15, 10, 15));
        keranjangContent.setLayout(new javax.swing.BoxLayout(keranjangContent, javax.swing.BoxLayout.Y_AXIS));
        jScrollPane2.setViewportView(keranjangContent);

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 336, Short.MAX_VALUE)
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 331, Short.MAX_VALUE)
        );

        KeranjangIsi.add(jPanel12, java.awt.BorderLayout.CENTER);

        containerKeranjang1.add(KeranjangIsi, "keranjang");

        KeranjangKosong.setBackground(new java.awt.Color(255, 255, 255));
        KeranjangKosong.setLayout(new java.awt.BorderLayout());

        jPanel22.setBackground(new java.awt.Color(255, 255, 255));
        jPanel22.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(231, 189, 187)));
        jPanel22.setMinimumSize(new java.awt.Dimension(335, 63));

        jLabel12.setFont(new java.awt.Font("Plus Jakarta Sans SemiBold", 0, 18)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(24, 26, 46));
        jLabel12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/posseblakratu/icon/IconKeranjang.png"))); // NOI18N
        jLabel12.setText("Keranjang");
        jLabel12.setIconTextGap(10);

        jPanel23.setBackground(new java.awt.Color(255, 255, 255));
        jPanel23.setBorder(javax.swing.BorderFactory.createCompoundBorder(null, new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 3, true)));
        jPanel23.setPreferredSize(new java.awt.Dimension(80, 23));
        jPanel23.setLayout(new java.awt.BorderLayout());

        jLabel4.setFont(new java.awt.Font("Plus Jakarta Sans", 1, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("TRX-0001");
        jPanel23.add(jLabel4, java.awt.BorderLayout.CENTER);

        javax.swing.GroupLayout jPanel22Layout = new javax.swing.GroupLayout(jPanel22);
        jPanel22.setLayout(jPanel22Layout);
        jPanel22Layout.setHorizontalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel22Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel23, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(16, 16, 16))
        );
        jPanel22Layout.setVerticalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel22Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel23, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, 32, Short.MAX_VALUE))
                .addGap(15, 15, 15))
        );

        KeranjangKosong.add(jPanel22, java.awt.BorderLayout.PAGE_START);

        jPanel24.setBackground(new java.awt.Color(255, 255, 255));
        jPanel24.setBorder(javax.swing.BorderFactory.createEmptyBorder(15, 20, 15, 20));
        jPanel24.setPreferredSize(new java.awt.Dimension(335, 212));
        jPanel24.setLayout(new java.awt.BorderLayout());

        jPanel25.setBackground(new java.awt.Color(255, 255, 255));

        jLabel16.setFont(new java.awt.Font("Plus Jakarta Sans", 0, 14)); // NOI18N
        jLabel16.setText("Subtotal");

        lblSubtotal1.setFont(new java.awt.Font("Plus Jakarta Sans", 0, 14)); // NOI18N
        lblSubtotal1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblSubtotal1.setText("Rp 0");

        lblDiskon1.setFont(new java.awt.Font("Plus Jakarta Sans", 0, 14)); // NOI18N
        lblDiskon1.setForeground(new java.awt.Color(214, 4, 39));
        lblDiskon1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblDiskon1.setText("- Rp 0");

        jLabel17.setFont(new java.awt.Font("Plus Jakarta Sans", 0, 14)); // NOI18N
        jLabel17.setText("Diskon 10% (jumat berkah)");

        jSeparator3.setForeground(new java.awt.Color(231, 189, 187));

        jLabel13.setFont(new java.awt.Font("Plus Jakarta Sans SemiBold", 0, 18)); // NOI18N
        jLabel13.setText("Total");

        lblTotal1.setFont(new java.awt.Font("Plus Jakarta Sans SemiBold", 0, 24)); // NOI18N
        lblTotal1.setForeground(new java.awt.Color(214, 4, 39));
        lblTotal1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblTotal1.setText("Rp 0");

        jSeparator4.setForeground(new java.awt.Color(231, 189, 187));

        btnBayar1.setBackground(new java.awt.Color(214, 4, 39));
        btnBayar1.setFont(new java.awt.Font("Plus Jakarta Sans SemiBold", 0, 16)); // NOI18N
        btnBayar1.setForeground(new java.awt.Color(255, 255, 255));
        btnBayar1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/posseblakratu/icon/Icon Bayar.png"))); // NOI18N
        btnBayar1.setText("Bayar");
        btnBayar1.setBorderPainted(false);
        btnBayar1.setIconTextGap(8);
        btnBayar1.addActionListener(this::btnBayar1ActionPerformed);

        javax.swing.GroupLayout jPanel25Layout = new javax.swing.GroupLayout(jPanel25);
        jPanel25.setLayout(jPanel25Layout);
        jPanel25Layout.setHorizontalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel25Layout.createSequentialGroup()
                .addComponent(jLabel16)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblSubtotal1, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(jPanel25Layout.createSequentialGroup()
                .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblDiskon1, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(jSeparator3)
            .addGroup(jPanel25Layout.createSequentialGroup()
                .addComponent(jLabel13)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblTotal1, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(btnBayar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel25Layout.createSequentialGroup()
                .addComponent(jSeparator4)
                .addContainerGap())
        );
        jPanel25Layout.setVerticalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel25Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(lblSubtotal1))
                .addGap(10, 10, 10)
                .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17)
                    .addComponent(lblDiskon1))
                .addGap(13, 13, 13)
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 3, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(13, 13, 13)
                .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lblTotal1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 9, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 9, Short.MAX_VALUE)
                .addComponent(btnBayar1, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );

        jPanel24.add(jPanel25, java.awt.BorderLayout.CENTER);

        KeranjangKosong.add(jPanel24, java.awt.BorderLayout.PAGE_END);

        jPanel26.setBackground(new java.awt.Color(255, 255, 255));
        jPanel26.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(231, 189, 187)));

        jScrollPane4.setBorder(null);
        jScrollPane4.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        keranjangContent1.setBackground(new java.awt.Color(255, 255, 255));
        keranjangContent1.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 15, 10, 15));
        keranjangContent1.setLayout(new javax.swing.BoxLayout(keranjangContent1, javax.swing.BoxLayout.Y_AXIS));
        jScrollPane4.setViewportView(keranjangContent1);

        javax.swing.GroupLayout jPanel26Layout = new javax.swing.GroupLayout(jPanel26);
        jPanel26.setLayout(jPanel26Layout);
        jPanel26Layout.setHorizontalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 336, Short.MAX_VALUE)
        );
        jPanel26Layout.setVerticalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 331, Short.MAX_VALUE)
        );

        KeranjangKosong.add(jPanel26, java.awt.BorderLayout.CENTER);

        containerKeranjang1.add(KeranjangKosong, "keranjangKosong");

        add(containerKeranjang1);
    }// </editor-fold>//GEN-END:initComponents

    private void filterSemuaMItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_filterSemuaMItemStateChanged
        // TODO add your handling code here:
        btnFilter(filterSemuaM);
        loadMenu("Semua");
    }//GEN-LAST:event_filterSemuaMItemStateChanged

    private void filterSeblakMItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_filterSeblakMItemStateChanged
        // TODO add your handling code here:
        btnFilter(filterSeblakM);
        loadMenu("Seblak");
    }//GEN-LAST:event_filterSeblakMItemStateChanged

    private void filterMinumanMItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_filterMinumanMItemStateChanged
        // TODO add your handling code here:
        btnFilter(filterMinumanM);
        loadMenu("Minuman");
    }//GEN-LAST:event_filterMinumanMItemStateChanged

    private void btnLvl0ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_btnLvl0ItemStateChanged
        // TODO add your handling code here:
        buttonLvl(btnLvl0);


    }//GEN-LAST:event_btnLvl0ItemStateChanged

    private void btnLvl1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_btnLvl1ItemStateChanged
        // TODO add your handling code here:
        buttonLvl(btnLvl1);
    }//GEN-LAST:event_btnLvl1ItemStateChanged

    private void btnLvl2ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_btnLvl2ItemStateChanged
        // TODO add your handling code here:
        buttonLvl(btnLvl2);
    }//GEN-LAST:event_btnLvl2ItemStateChanged

    private void btnLvl3ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_btnLvl3ItemStateChanged
        // TODO add your handling code here:
        buttonLvl(btnLvl3);
    }//GEN-LAST:event_btnLvl3ItemStateChanged

    private void btnLvl4ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_btnLvl4ItemStateChanged
        // TODO add your handling code here:
        buttonLvl(btnLvl4);
    }//GEN-LAST:event_btnLvl4ItemStateChanged

    private void btnLvl5ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_btnLvl5ItemStateChanged
        // TODO add your handling code here:
        buttonLvl(btnLvl5);
    }//GEN-LAST:event_btnLvl5ItemStateChanged

    private void btnBayarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBayarActionPerformed
        // TODO add your handling code here:
        PopupBayar bayar = new PopupBayar(
                (java.awt.Frame) javax.swing.SwingUtilities.getWindowAncestor(this), true
        );

        bayar.setVisible(true);

    }//GEN-LAST:event_btnBayarActionPerformed

    private void btnNext1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNext1ActionPerformed
        // TODO add your handling code here:


    }//GEN-LAST:event_btnNext1ActionPerformed

    private void btnResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetActionPerformed
        // TODO add your handling code here:
        reset();
    }//GEN-LAST:event_btnResetActionPerformed

    private void btnNextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNextActionPerformed
        // TODO add your handling code here:

    }//GEN-LAST:event_btnNextActionPerformed

    private void btnBayar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBayar1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnBayar1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel KeranjangIsi;
    private javax.swing.JPanel KeranjangKosong;
    private javax.swing.JButton btnBayar;
    private javax.swing.JButton btnBayar1;
    private javax.swing.JToggleButton btnLvl0;
    private javax.swing.JToggleButton btnLvl1;
    private javax.swing.JToggleButton btnLvl2;
    private javax.swing.JToggleButton btnLvl3;
    private javax.swing.JToggleButton btnLvl4;
    private javax.swing.JToggleButton btnLvl5;
    private javax.swing.JButton btnNext;
    private javax.swing.JButton btnNext1;
    private javax.swing.JButton btnReset;
    private javax.swing.JButton btnReset1;
    private javax.swing.ButtonGroup buttonGroupFilter;
    private javax.swing.ButtonGroup buttonGroupLvl;
    private javax.swing.JPanel containerDaftarMenu;
    private javax.swing.JPanel containerKeranjang1;
    private javax.swing.JPanel containerKostumisasi;
    private javax.swing.JPanel containerKostumisasi1;
    private javax.swing.JPanel containerKostumisasiKosong;
    private javax.swing.JToggleButton filterMinumanM;
    private javax.swing.JToggleButton filterSeblakM;
    private javax.swing.JToggleButton filterSemuaM;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel25;
    private javax.swing.JPanel jPanel26;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JPanel keranjangContent;
    private javax.swing.JPanel keranjangContent1;
    private javax.swing.JLabel lblDiskon;
    private javax.swing.JLabel lblDiskon1;
    private javax.swing.JLabel lblMenu;
    private javax.swing.JLabel lblMenu1;
    private javax.swing.JLabel lblSubtotal;
    private javax.swing.JLabel lblSubtotal1;
    private javax.swing.JLabel lblTotal;
    private javax.swing.JLabel lblTotal1;
    private javax.swing.JPanel menuContent;
    private javax.swing.JPanel toppingContent;
    // End of variables declaration//GEN-END:variables
}
