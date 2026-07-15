/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package posseblakratu.view;

import com.formdev.flatlaf.ui.FlatLineBorder;
import java.awt.Color;
import java.awt.Insets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;
import posseblakratu.config.Koneksi;
import posseblakratu.config.FormatUang;

/**
 *
 * @author Al
 */
public class PanelStok extends javax.swing.JPanel {

    //menyimpan id_stok dari baris tabel yang sedang dipilih
    private String idStokDipilih = "";

    //true jika pengguna sedang mode edit, false jika mode tambah
    private boolean sedangEdit = false;

    /**
     * Creates new form PanelStok
     */
    public PanelStok() {
        initComponents();
        
        panelLengkung(jPanel17);
        panelLengkung(jPanel9);
        panelLengkung(jPanel14);
        panelLengkung(jPanel19);

        //memanggil method untuk menampilkan data stok ke tabel
        load_tabel_stok();
        reset();
    }
    
    
    
    void panelLengkung(JPanel p) {
        
        p.setBorder(new FlatLineBorder(
                new Insets(3, 3, 3, 3),
                Color.decode("#E7BDBB"),
                1f,
                10));
        
        
        jPanel1.setBorder(new FlatLineBorder(
                new Insets(5, 5, 5, 5),
                Color.decode("#E7BDBB"),
                1f,
                10));
        
        jPanel3.setBorder(new FlatLineBorder(
                new Insets(5, 5, 5, 5),
                Color.decode("#E7BDBB"),
                1f,
                10));
    }


    void reset() {

        //ubah judul panel kembali ke tambah stok
        lblTambahProduk.setText("Tambah Stok");

        //kembalikan text button simpan ke mode tambah
        btnSimpanTambahStok.setText("Simpan Stok     ");

        //kosongkan semua field input
        tNamaStok.setText("Contoh: Bawang");
        tJmlStok.setText(null);
        tSatuanStok.setText("Satuan");
        tHargaStok.setText(null);

        //hapus seleksi pada tabel
        tblStok.clearSelection();

        //kembalikan ke mode tambah
        sedangEdit = false;
        idStokDipilih = "";
    }


    //membuat method untuk generate id stok otomatis
    String generateIdStok() {

        //variabel untuk menyimpan id terakhir dari database
        String lastId = null;

        try {
            //buka koneksi ke database
            Connection conn = Koneksi.konek();

            //query untuk mengambil id stok terakhir
            String sql = "SELECT id_stok FROM stok_bahan ORDER BY id_stok DESC LIMIT 1";

            //siapkan statement
            PreparedStatement ps = conn.prepareStatement(sql);

            //jalankan query
            ResultSet rs = ps.executeQuery();

            //jika ada data
            if (rs.next()) {
                 //ambil id terakhirnya
                lastId = rs.getString("id_stok");
            }

        } catch (SQLException sQLException) {
            //tampilkan error jika gagal
            JOptionPane.showMessageDialog(null, "Gagal membuat id stok!");
        }

        //jika belum ada stok sama sekali
        if (lastId == null) {
            return "STK0001";
        }

        //mengambil angka dari STK0001 → 0001
        int angka = Integer.parseInt(lastId.substring(3));

        //increment angka
        angka++;

        //format ulang jadi STK0002 dst
        return String.format("STK%04d", angka);
    }


    //membuat method load tabel stok
    void load_tabel_stok() {

        //membuat model tabel baru
        DefaultTableModel model = new DefaultTableModel();

        //menambahkan kolom ke dalam model tabel
        model.addColumn("Nama Bahan");
        model.addColumn("Stok");
        model.addColumn("Harga Satuan");
        model.addColumn("Satuan");


        //query SQL untuk mengambil seluruh data stok bahan yang masih tersedia
        String sql = "SELECT * FROM stok_bahan WHERE status IN ('Ada')";

        try {
            //membuka koneksi ke database
            Connection conn = Koneksi.konek();

            //siapkan statement dengan parameter
            PreparedStatement ps = conn.prepareStatement(sql);


            //jalankan query dan ambil hasilnya
            ResultSet rs = ps.executeQuery();

            //melakukan iterasi untuk setiap baris hasil query
            while (rs.next()) {

                //mengambil data dari setiap kolom
                String namaStok = rs.getString("nama_stok");
                int jumlahStok = rs.getInt("jumlah_stok");
                String hargaStok = FormatUang.format(rs.getDouble("harga_satuan"));
                String satuanStok = rs.getString("satuan");

                //menyimpan data ke dalam array
                Object[] baris = {namaStok, jumlahStok, hargaStok, satuanStok};

                //menambahkan baris ke model tabel
                model.addRow(baris);
            }
        } catch (SQLException sQLException) {
            //menampilkan pesan error jika gagal mengambil data
            JOptionPane.showMessageDialog(null, "Gagal mengambil data stok!");
        }

        //menampilkan model yang sudah diisi ke dalam tabel GUI
        tblStok.setModel(model);
        tblStok.setColumnWidths("230,50,100,50");
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        lblTambahProduk = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        jPanel12 = new javax.swing.JPanel();
        lblNamaProduk = new javax.swing.JLabel();
        jPanel17 = new javax.swing.JPanel();
        tNamaStok = new javax.swing.JTextField();
        jPanel13 = new javax.swing.JPanel();
        lblNamaProduk1 = new javax.swing.JLabel();
        jPanel18 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        tJmlStok = new javax.swing.JTextField();
        jPanel14 = new javax.swing.JPanel();
        tSatuanStok = new javax.swing.JTextField();
        jPanel15 = new javax.swing.JPanel();
        lblNamaProduk3 = new javax.swing.JLabel();
        jPanel19 = new javax.swing.JPanel();
        tHargaStok = new javax.swing.JTextField();
        lblRupiahProduk = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jPanel16 = new javax.swing.JPanel();
        btnSimpanTambahStok = new javax.swing.JButton();
        jPanel10 = new javax.swing.JPanel();
        btnBatalStok = new javax.swing.JButton();
        btnHapusStok = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        JLabell = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblStok = new jtablecustom.JTableCustom();

        setBackground(new java.awt.Color(252, 249, 255));
        setBorder(javax.swing.BorderFactory.createEmptyBorder(15, 15, 15, 15));
        setMinimumSize(new java.awt.Dimension(1075, 639));
        setLayout(new java.awt.BorderLayout(15, 0));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(231, 189, 187)));
        jPanel1.setPreferredSize(new java.awt.Dimension(347, 611));
        jPanel1.setLayout(new java.awt.BorderLayout());

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(231, 189, 187)));
        jPanel2.setMinimumSize(new java.awt.Dimension(335, 63));

        lblTambahProduk.setFont(new java.awt.Font("Plus Jakarta Sans SemiBold", 0, 18)); // NOI18N
        lblTambahProduk.setText("Tambah  Stok");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(lblTambahProduk, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblTambahProduk, javax.swing.GroupLayout.DEFAULT_SIZE, 62, Short.MAX_VALUE)
        );

        jPanel1.add(jPanel2, java.awt.BorderLayout.PAGE_START);

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));
        jPanel7.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(231, 189, 187)));

        jPanel11.setBackground(new java.awt.Color(255, 255, 255));
        jPanel11.setBorder(javax.swing.BorderFactory.createEmptyBorder(18, 18, 0, 18));
        jPanel11.setMaximumSize(new java.awt.Dimension(32767, 350));
        jPanel11.setPreferredSize(new java.awt.Dimension(345, 340));
        jPanel11.setLayout(new java.awt.GridLayout(5, 1, 0, 10));

        jPanel12.setBackground(new java.awt.Color(255, 255, 255));
        jPanel12.setLayout(new java.awt.BorderLayout());

        lblNamaProduk.setBackground(new java.awt.Color(255, 255, 255));
        lblNamaProduk.setFont(new java.awt.Font("Plus Jakarta Sans", 1, 14)); // NOI18N
        lblNamaProduk.setText("Nama Stok");
        lblNamaProduk.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 4, 0));
        jPanel12.add(lblNamaProduk, java.awt.BorderLayout.PAGE_START);

        jPanel17.setBackground(new java.awt.Color(255, 255, 255));
        jPanel17.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(231, 189, 187)));
        jPanel17.setLayout(new java.awt.GridBagLayout());

        tNamaStok.setFont(new java.awt.Font("Plus Jakarta Sans", 0, 16)); // NOI18N
        tNamaStok.setForeground(new java.awt.Color(92, 62, 60));
        tNamaStok.setBorder(null);
        tNamaStok.setMargin(new java.awt.Insets(10, 10, 10, 6));
        tNamaStok.setPreferredSize(new java.awt.Dimension(126, 19));
        tNamaStok.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                tNamaStokFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                tNamaStokFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 278;
        gridBagConstraints.ipady = 19;
        gridBagConstraints.insets = new java.awt.Insets(1, 18, 1, 12);
        jPanel17.add(tNamaStok, gridBagConstraints);

        jPanel12.add(jPanel17, java.awt.BorderLayout.CENTER);

        jPanel11.add(jPanel12);

        jPanel13.setBackground(new java.awt.Color(255, 255, 255));
        jPanel13.setLayout(new java.awt.BorderLayout());

        lblNamaProduk1.setBackground(new java.awt.Color(255, 255, 255));
        lblNamaProduk1.setFont(new java.awt.Font("Plus Jakarta Sans", 1, 14)); // NOI18N
        lblNamaProduk1.setText("Jumlah Stok");
        lblNamaProduk1.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 4, 0));
        jPanel13.add(lblNamaProduk1, java.awt.BorderLayout.PAGE_START);

        jPanel18.setBackground(new java.awt.Color(255, 255, 255));
        jPanel18.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 1, 0, 1));
        jPanel18.setLayout(new java.awt.GridLayout(1, 2, 10, 5));

        jPanel9.setBackground(new java.awt.Color(255, 255, 255));
        jPanel9.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(231, 189, 187)));
        jPanel9.setForeground(new java.awt.Color(92, 62, 60));

        tJmlStok.setFont(new java.awt.Font("Plus Jakarta Sans", 0, 16)); // NOI18N
        tJmlStok.setBorder(null);
        tJmlStok.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                tJmlStokKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addGap(13, 13, 13)
                .addComponent(tJmlStok, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tJmlStok, javax.swing.GroupLayout.DEFAULT_SIZE, 44, Short.MAX_VALUE)
        );

        jPanel18.add(jPanel9);

        jPanel14.setBackground(new java.awt.Color(255, 255, 255));
        jPanel14.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(231, 189, 187)));

        tSatuanStok.setFont(new java.awt.Font("Plus Jakarta Sans", 0, 16)); // NOI18N
        tSatuanStok.setBorder(null);
        tSatuanStok.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                tSatuanStokFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                tSatuanStokFocusLost(evt);
            }
        });

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel14Layout.createSequentialGroup()
                .addContainerGap(13, Short.MAX_VALUE)
                .addComponent(tSatuanStok, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(tSatuanStok, javax.swing.GroupLayout.DEFAULT_SIZE, 44, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        jPanel18.add(jPanel14);

        jPanel13.add(jPanel18, java.awt.BorderLayout.CENTER);

        jPanel11.add(jPanel13);

        jPanel15.setBackground(new java.awt.Color(255, 255, 255));
        jPanel15.setLayout(new java.awt.BorderLayout());

        lblNamaProduk3.setBackground(new java.awt.Color(255, 255, 255));
        lblNamaProduk3.setFont(new java.awt.Font("Plus Jakarta Sans", 1, 14)); // NOI18N
        lblNamaProduk3.setText("Harga Satuan Stok");
        lblNamaProduk3.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 4, 0));
        jPanel15.add(lblNamaProduk3, java.awt.BorderLayout.PAGE_START);

        jPanel19.setBackground(new java.awt.Color(255, 255, 255));
        jPanel19.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(231, 189, 187)));
        jPanel19.setLayout(new java.awt.BorderLayout());

        tHargaStok.setFont(new java.awt.Font("Plus Jakarta Sans", 0, 18)); // NOI18N
        tHargaStok.setForeground(new java.awt.Color(215, 4, 39));
        tHargaStok.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        tHargaStok.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 15));
        tHargaStok.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        tHargaStok.setMargin(new java.awt.Insets(10, 20, 10, 6));
        tHargaStok.setOpaque(true);
        tHargaStok.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                tHargaStokKeyTyped(evt);
            }
        });
        jPanel19.add(tHargaStok, java.awt.BorderLayout.CENTER);

        lblRupiahProduk.setBackground(new java.awt.Color(255, 255, 255));
        lblRupiahProduk.setFont(new java.awt.Font("Plus Jakarta Sans", 0, 18)); // NOI18N
        lblRupiahProduk.setForeground(new java.awt.Color(215, 4, 39));
        lblRupiahProduk.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblRupiahProduk.setText("Rp");
        lblRupiahProduk.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 0, 1, new java.awt.Color(231, 189, 187)), javax.swing.BorderFactory.createEmptyBorder(1, 12, 1, 12)));
        lblRupiahProduk.setOpaque(true);
        jPanel19.add(lblRupiahProduk, java.awt.BorderLayout.LINE_START);

        jPanel15.add(jPanel19, java.awt.BorderLayout.CENTER);

        jPanel11.add(jPanel15);

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, 407, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel7, java.awt.BorderLayout.CENTER);

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 20, 20, 20));
        jPanel4.setMinimumSize(new java.awt.Dimension(345, 130));
        jPanel4.setPreferredSize(new java.awt.Dimension(345, 130));
        jPanel4.setLayout(new java.awt.CardLayout());

        jPanel16.setBackground(new java.awt.Color(255, 255, 255));
        jPanel16.setLayout(new java.awt.GridLayout(2, 0, 0, 12));

        btnSimpanTambahStok.setBackground(new java.awt.Color(214, 4, 39));
        btnSimpanTambahStok.setFont(new java.awt.Font("Plus Jakarta Sans SemiBold", 0, 16)); // NOI18N
        btnSimpanTambahStok.setForeground(new java.awt.Color(255, 255, 255));
        btnSimpanTambahStok.setIcon(new javax.swing.ImageIcon(getClass().getResource("/posseblakratu/icon/IconSimpan.png"))); // NOI18N
        btnSimpanTambahStok.setText("Simpan Perubahan");
        btnSimpanTambahStok.setBorderPainted(false);
        btnSimpanTambahStok.addActionListener(this::btnSimpanTambahStokActionPerformed);
        jPanel16.add(btnSimpanTambahStok);

        jPanel10.setBackground(new java.awt.Color(255, 255, 255));
        jPanel10.setLayout(new java.awt.GridLayout(0, 2, 12, 0));

        btnBatalStok.setFont(new java.awt.Font("Plus Jakarta Sans SemiBold", 0, 16)); // NOI18N
        btnBatalStok.setText("Batal");
        btnBatalStok.setFocusable(false);
        btnBatalStok.addActionListener(this::btnBatalStokActionPerformed);
        jPanel10.add(btnBatalStok);

        btnHapusStok.setFont(new java.awt.Font("Plus Jakarta Sans SemiBold", 0, 16)); // NOI18N
        btnHapusStok.setForeground(new java.awt.Color(214, 4, 39));
        btnHapusStok.setIcon(new javax.swing.ImageIcon(getClass().getResource("/posseblakratu/icon/Vector.png"))); // NOI18N
        btnHapusStok.setText("Hapus");
        btnHapusStok.setFocusable(false);
        btnHapusStok.addActionListener(this::btnHapusStokActionPerformed);
        jPanel10.add(btnHapusStok);

        jPanel16.add(jPanel10);

        jPanel4.add(jPanel16, "card2");

        jPanel1.add(jPanel4, java.awt.BorderLayout.PAGE_END);

        add(jPanel1, java.awt.BorderLayout.LINE_END);

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(231, 189, 187)));
        jPanel3.setLayout(new java.awt.BorderLayout());

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));
        jPanel5.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(231, 189, 187)));
        jPanel5.setPreferredSize(new java.awt.Dimension(681, 63));
        jPanel5.setLayout(new java.awt.BorderLayout());

        jPanel8.setBackground(new java.awt.Color(255, 255, 255));
        jPanel8.setPreferredSize(new java.awt.Dimension(681, 63));

        JLabell.setFont(new java.awt.Font("Plus Jakarta Sans SemiBold", 0, 24)); // NOI18N
        JLabell.setForeground(new java.awt.Color(24, 26, 46));
        JLabell.setText("Daftar Stok");
        JLabell.setMaximumSize(new java.awt.Dimension(198, 31));
        JLabell.setMinimumSize(new java.awt.Dimension(198, 31));
        JLabell.setName(""); // NOI18N
        JLabell.setPreferredSize(new java.awt.Dimension(198, 63));

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(JLabell, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(390, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addComponent(JLabell, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jPanel5.add(jPanel8, java.awt.BorderLayout.CENTER);

        jPanel3.add(jPanel5, java.awt.BorderLayout.PAGE_START);

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));
        jPanel6.setLayout(new java.awt.BorderLayout());

        tblStok.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Nama Bahan", "Stok", "Harga Satuan", "Satuan"
            }
        ));
        tblStok.setCellPaddingLeft(25);
        tblStok.setCellPaddingRight(25);
        tblStok.setCenterColumns("1,2,3");
        tblStok.setColumnWidths("230,50,100,50");
        tblStok.setFont(new java.awt.Font("Plus Jakarta Sans", 0, 14)); // NOI18N
        tblStok.setHeaderPaddingLeft(25);
        tblStok.setHeaderPaddingRight(25);
        tblStok.setLeftColumns("0");
        tblStok.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblStokMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblStok);

        jPanel6.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jPanel3.add(jPanel6, java.awt.BorderLayout.CENTER);

        add(jPanel3, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void tblStokMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblStokMouseClicked
        //ambil indeks baris yang diklik pada tabel stok
        int baris = tblStok.rowAtPoint(evt.getPoint());

        //abaikan jika klik tidak mengenai baris manapun
        if (baris == -1) {
            return;
        }

        //ambil nilai dari kolom 0 (nama stok)
        String namaStok = tblStok.getValueAt(baris, 0).toString();

        //ambil nilai dari kolom 1 (jumlah stok)
        String jumlahStok = tblStok.getValueAt(baris, 1).toString();

        //ambil nilai dari kolom 2 (harga satuan)
        String hargaStok = tblStok.getValueAt(baris, 2).toString();

        //ambil nilai dari kolom 3 (satuan)
        String satuanStok = tblStok.getValueAt(baris, 3).toString();

        //query ke database untuk mengambil id_stok berdasarkan nama stok dan id pengguna
        String sql = "SELECT id_stok FROM stok_bahan WHERE nama_stok = ? AND id_pengguna = ?";

        try {
            //buka koneksi ke database
            Connection conn = Koneksi.konek();

            //siapkan statement dengan parameter
            PreparedStatement ps = conn.prepareStatement(sql);

            //isi parameter nama stok dari baris yang diklik
            ps.setString(1, namaStok);

            //isi parameter id pengguna dari session login
            ps.setString(2, FrameLogin.getIdPengguna());

            //jalankan query
            ResultSet rs = ps.executeQuery();

            //jika data ditemukan, simpan id_stok ke variabel
            if (rs.next()) {
                idStokDipilih = rs.getString("id_stok");
            }

        } catch (SQLException sQLException) {
            //tampilkan pesan jika gagal mengambil id stok
            JOptionPane.showMessageDialog(null, "Gagal mengambil id stok!");
            return;
        }

        //aktifkan mode edit
        sedangEdit = true;

        //ubah judul panel menjadi Edit Stok
        lblTambahProduk.setText("Edit Stok");

        //ubah text button simpan ke mode edit
        btnSimpanTambahStok.setText("Simpan Perubahan");

        //tampilkan data dari baris yang dipilih ke form input
        tNamaStok.setText(namaStok);
        tJmlStok.setText(jumlahStok);
        tSatuanStok.setText(satuanStok);

        //strip format "Rp. " dan titik pemisah sebelum ditampilkan ke field input angka
        tHargaStok.setText(hargaStok.replace("Rp. ", "").replace(".", ""));
    }//GEN-LAST:event_tblStokMouseClicked

    private void btnBatalStokActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBatalStokActionPerformed
        //panggil method reset untuk mengosongkan form dan kembali ke mode tambah
        reset();
    }//GEN-LAST:event_btnBatalStokActionPerformed

    //method untuk generate id pengeluaran otomatis
    private String generateIdPengeluaran() {
        //nomor urut transaksi, default dimulai dari 1
        int nomor = 1;

        try {
            //koneksi ke database
            Connection conn = Koneksi.konek();

            //query mengambil id transaksi terakhir pada bulan ini
            String sql = "SELECT id_pengeluaran FROM pengeluaran "
                    + "WHERE id_pengeluaran LIKE CONCAT('EXP-', DATE_FORMAT(CURDATE(), '%Y%m'), '%') "
                    + "ORDER BY id_pengeluaran DESC LIMIT 1";

            //prepare statement
            PreparedStatement ps = conn.prepareStatement(sql);

            //eksekusi query
            ResultSet rs = ps.executeQuery();

            //jika ada data pada bulan ini
            if (rs.next()) {
                //ambil id transaksi terakhir
                String lastId = rs.getString("id_pengeluaran");

                //ambil nomor urut lalu tambah 1
                nomor = Integer.parseInt(lastId.substring(13)) + 1;
            }

        } catch (SQLException sQLException) {
            //tampilkan pesan jika gagal membuat id transaksi
            JOptionPane.showMessageDialog(null, "Gagal membuat id pengeluaran!");
            return null;
        }

        //mengambil tanggal hari ini dengan format yyyyMMdd
        String tanggal = java.time.LocalDate.now()
                .format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd"));

        //mengembalikan id pengeluaran dengan format EXP-yyyyMMdd-0001
        return String.format("EXP-%s-%04d", tanggal, nomor);
    }


    //membuat method untuk menyimpan pengeluaran stok
    void simpanPengeluaran(String idStok, int jumlah, double hargaSatuan) {

        try {
            //buka koneksi ke database
            Connection conn = Koneksi.konek();

            //generate id pengeluaran baru
            String idPengeluaran = generateIdPengeluaran();

            //hitung total pengeluaran
            double total = jumlah * hargaSatuan;

            //ambil id pengguna yang sedang login dari session
            String idPengguna = FrameLogin.getIdPengguna();

            //query SQL untuk menyisipkan data pengeluaran
            String sql = "INSERT INTO pengeluaran (id_pengeluaran, total, harga_satuan, jumlah, tanggal, id_stok, id_pengguna) VALUES (?,?,?,?,NOW(),?,?)";

            //siapkan statement dengan parameter
            PreparedStatement ps = conn.prepareStatement(sql);

            //isi parameter id pengeluaran
            ps.setString(1, idPengeluaran);

            //isi parameter total
            ps.setDouble(2, total);

            //isi parameter harga satuan
            ps.setDouble(3, hargaSatuan);

            //isi parameter jumlah
            ps.setDouble(4, jumlah);

            //isi parameter id stok
            ps.setString(5, idStok);

            //isi parameter id pengguna dari session login
            ps.setString(6, idPengguna);

            //jalankan query insert
            ps.execute();

        } catch (SQLException sQLException) {
            //tampilkan pesan jika gagal menyimpan pengeluaran
            JOptionPane.showMessageDialog(null, "Gagal menyimpan pengeluaran!");
        }
    }

    private void btnSimpanTambahStokActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSimpanTambahStokActionPerformed
        //ambil input dari semua field form
        String namaStok = tNamaStok.getText();
        String jumlahStok = tJmlStok.getText();
        String satuanStok = tSatuanStok.getText();
        String hargaStok = tHargaStok.getText();

        //validasi: semua field wajib diisi
        if (namaStok.isEmpty() || jumlahStok.isEmpty() || satuanStok.isEmpty() || hargaStok.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Semua field harus diisi!");
            return;
        }

        //konversi jumlah dan harga ke tipe numerik
        int jumlahInt = Integer.parseInt(jumlahStok);
        double hargaDouble = Double.parseDouble(hargaStok);

        if (sedangEdit) {
            //MODE EDIT: update data yang sudah ada berdasarkan id_stok

            try {
                //buka koneksi ke database
                Connection conn = Koneksi.konek();

                //ambil jumlah stok lama untuk menghitung selisih
                String sqlCek = "SELECT jumlah_stok FROM stok_bahan WHERE id_stok=?";
                PreparedStatement psCek = conn.prepareStatement(sqlCek);
                psCek.setString(1, idStokDipilih);
                ResultSet rsCek = psCek.executeQuery();

                //variabel untuk menyimpan jumlah stok lama
                int jumlahStokLama = 0;
                if (rsCek.next()) {
                    jumlahStokLama = rsCek.getInt("jumlah_stok");
                }

                //hitung selisih penambahan stok
                int selisih = jumlahInt - jumlahStokLama;

                //query SQL untuk mengubah data stok
                String sql = "UPDATE stok_bahan SET nama_stok=?, jumlah_stok=?, harga_satuan=?, satuan=? WHERE id_stok=?";

                //siapkan statement dengan parameter
                PreparedStatement ps = conn.prepareStatement(sql);

                //isi parameter nama stok baru
                ps.setString(1, namaStok);

                //isi parameter jumlah stok baru
                ps.setInt(2, jumlahInt);

                //isi parameter harga satuan baru
                ps.setDouble(3, hargaDouble);

                //isi parameter satuan baru
                ps.setString(4, satuanStok);

                //isi parameter kondisi WHERE dengan id_stok yang dipilih
                ps.setString(5, idStokDipilih);

                //jalankan query update
                ps.execute();

                //jika ada penambahan stok, catat sebagai pengeluaran
                if (selisih > 0) {
                    simpanPengeluaran(idStokDipilih, selisih, hargaDouble);
                }

                //tampilkan pesan berhasil
                JOptionPane.showMessageDialog(null, "Data stok berhasil diubah!");

            } catch (SQLException sQLException) {
                //tampilkan pesan jika gagal mengubah data
                JOptionPane.showMessageDialog(null, "Data stok gagal diubah!");
            }

        } else {
            //MODE TAMBAH: insert data baru ke database

            //generate id_stok baru secara otomatis
            String idStokBaru = generateIdStok();

            //ambil id pengguna yang sedang login dari session
            String idPenggunaLogin = FrameLogin.getIdPengguna();

            //query SQL untuk menyisipkan data stok baru
            String sql = "INSERT INTO stok_bahan (id_stok, nama_stok, jumlah_stok, satuan, harga_satuan, id_pengguna) VALUES (?,?,?,?,?,?)";

            try {
                //buka koneksi ke database
                Connection conn = Koneksi.konek();

                //siapkan statement dengan parameter
                PreparedStatement ps = conn.prepareStatement(sql);

                //isi parameter id_stok yang sudah di-generate
                ps.setString(1, idStokBaru);

                //isi parameter nama stok
                ps.setString(2, namaStok);

                //isi parameter jumlah stok
                ps.setInt(3, jumlahInt);

                //isi parameter satuan
                ps.setString(4, satuanStok);

                //isi parameter harga satuan
                ps.setDouble(5, hargaDouble);

                //isi parameter id pengguna dari session login
                ps.setString(6, idPenggunaLogin);

                //jalankan query insert
                ps.execute();

                //catat penambahan stok sebagai pengeluaran
                simpanPengeluaran(idStokBaru, jumlahInt, hargaDouble);

                //tampilkan pesan berhasil
                JOptionPane.showMessageDialog(null, "Data stok berhasil disimpan!");

            } catch (SQLException sQLException) {
                //tampilkan pesan jika gagal menyimpan data
                JOptionPane.showMessageDialog(null, "Data stok gagal disimpan!");
            }
        }

        //muat ulang tabel agar perubahan tampil
        load_tabel_stok();

        //reset form kembali ke mode tambah
        reset();
    }//GEN-LAST:event_btnSimpanTambahStokActionPerformed

    private void btnHapusStokActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHapusStokActionPerformed
        //cek apakah ada baris yang dipilih dari tabel
        if (!sedangEdit) {
            JOptionPane.showMessageDialog(null, "Pilih data dari tabel terlebih dahulu!");
            return;
        }
        
        String namaStok = tNamaStok.getText();

        //tampilkan dialog konfirmasi sebelum menghapus
        int konfirmasi = JOptionPane.showConfirmDialog(null,
                "Yakin ingin menghapus stok " + namaStok+"?",
                "Konfirmasi Hapus",
                JOptionPane.YES_NO_OPTION);

        //jika pengguna memilih tidak, batalkan hapus
        if (konfirmasi != JOptionPane.YES_OPTION) {
            return;
        }

        //query SQL untuk menghapus data stok berdasarkan id_stok
        String sql = "DELETE FROM stok_bahan WHERE id_stok=?";

        try {
            //buka koneksi ke database
            Connection conn = Koneksi.konek();

            //siapkan statement dengan parameter
            PreparedStatement ps = conn.prepareStatement(sql);

            //isi parameter id_stok dari baris yang dipilih
            ps.setString(1, idStokDipilih);

            //jalankan query delete
            ps.execute();

            //tampilkan pesan berhasil
            JOptionPane.showMessageDialog(null, "Data stok berhasil dihapus!");

        } catch (SQLException sQLException) {
            //tampilkan pesan jika gagal menghapus data
            JOptionPane.showMessageDialog(null, "Data stok gagal dihapus!");
        }

        //muat ulang tabel agar perubahan tampil
        load_tabel_stok();

        //reset form kembali ke mode tambah
        reset();
    }//GEN-LAST:event_btnHapusStokActionPerformed

    private void tJmlStokKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tJmlStokKeyTyped
        // TODO add your handling code here:
        char huruf = evt.getKeyChar();
        if(!Character.isDigit(huruf)){
            evt.consume();
        }
    }//GEN-LAST:event_tJmlStokKeyTyped

    private void tHargaStokKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tHargaStokKeyTyped
        // TODO add your handling code here:
        char huruf = evt.getKeyChar();
        if(!Character.isDigit(huruf)){
            evt.consume();
        }
    }//GEN-LAST:event_tHargaStokKeyTyped

    private void tNamaStokFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tNamaStokFocusGained
        // TODO add your handling code here:
        if (!sedangEdit) {
            //ambil teks yang saat ini ada di field nama stok
            String namaP = tNamaStok.getText();

            //jika masih berisi placeholder, kosongkan agar pengguna bisa langsung mengetik
            if (namaP.equals("Contoh: Bawang")) {
                tNamaStok.setText("");
            }
        }
    }//GEN-LAST:event_tNamaStokFocusGained

    private void tNamaStokFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tNamaStokFocusLost
        // TODO add your handling code here:
        if (!sedangEdit) {
            //ambil teks yang ada di field nama stok
            String namaP = tNamaStok.getText();

            //jika kosong kembalikan tulisan placeholder
            if (namaP.equals("") || namaP.equals("Contoh: Bawang")) {
                tNamaStok.setText("Contoh: Bawang");
            }
        }
    }//GEN-LAST:event_tNamaStokFocusLost

    private void tSatuanStokFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tSatuanStokFocusGained
        // TODO add your handling code here:
        if (!sedangEdit) {
            //ambil teks yang saat ini ada di field satuan stok
            String namaP = tSatuanStok.getText();

            //jika masih berisi placeholder, kosongkan agar pengguna bisa langsung mengetik
            if (namaP.equals("Satuan")) {
                tSatuanStok.setText("");
            }
        }
    }//GEN-LAST:event_tSatuanStokFocusGained

    private void tSatuanStokFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tSatuanStokFocusLost
        // TODO add your handling code here:
        if (!sedangEdit) {
            //ambil teks yang ada di field satuan stok
            String namaP = tSatuanStok.getText();

            //jika kosong kembalikan tulisan placeholder
            if (namaP.equals("") || namaP.equals("Satuan")) {
                tSatuanStok.setText("Satuan");
            }
        }
    }//GEN-LAST:event_tSatuanStokFocusLost


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel JLabell;
    private javax.swing.JButton btnBatalStok;
    private javax.swing.JButton btnHapusStok;
    private javax.swing.JButton btnSimpanTambahStok;
    private javax.swing.JPanel jPanel1;
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
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblNamaProduk;
    private javax.swing.JLabel lblNamaProduk1;
    private javax.swing.JLabel lblNamaProduk3;
    private javax.swing.JLabel lblRupiahProduk;
    private javax.swing.JLabel lblTambahProduk;
    private javax.swing.JTextField tHargaStok;
    private javax.swing.JTextField tJmlStok;
    private javax.swing.JTextField tNamaStok;
    private javax.swing.JTextField tSatuanStok;
    private jtablecustom.JTableCustom tblStok;
    // End of variables declaration//GEN-END:variables
}
