/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package posseblakratu.view;

import posseblakratu.config.Koneksi;
import posseblakratu.config.FormatUang;
import java.sql.PreparedStatement;
import javax.swing.JOptionPane;
import java.sql.Connection;
import java.sql.SQLException;
import com.formdev.flatlaf.ui.FlatLineBorder;
import java.awt.Color;
import java.awt.Insets;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Al
 */
public final class PanelProduk extends javax.swing.JPanel {
    
    //untuk menyimpan status apakah panel sedang berada pada mode ubah
    private boolean modeUbah = false;
    //untuk menyimpan ID produk yang sedang dipilih
    private String idProdukTerpilih = null;
    //untuk menyimpan kategori filter produk yang sedang dipilih , nilai awal "Semua"
    private String filterDipilih = "Semua";

    /**
     * Creates new form PanelProduk
     */
    public PanelProduk() { //tempat konstruktor 
        

        
        initComponents(); //memanggil method init komponen
        reset(); //memanggil method reset untuk mengosongkan semua field inputan
        load_tabel_produk("Semua");//memanggil method untuk menampilkan tabel produk dari database dengan (semua kategori)

        panelLengkung(jPanel17);
        panelLengkung(jPanel18);
        panelLengkung(jPanel20);
        panelLengkung(jPanel19);

        filterSemuaP.setSelected(true);
        filterSemuaP.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 3, 0, new java.awt.Color(173, 0, 28)));
        filterSeblakP.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 3, 0));
        filterToppingP.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 3, 0));
        filterMinumanP.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 3, 0));

        reset();
    }
    //Method untuk membuat sudut JPanel menjadi melengkung (rounded) dan mengatur warna garis tepinya agar sama
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
    //method untuk mengaktifkan filter "Semua' sebagai filter awal pada panel produk
    void pilihFilterSemua(){
        filterSemuaP.setSelected(true);
        filterSemuaP.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 3, 0, new java.awt.Color(173, 0, 28)));
    }
    
    //method untuk mengosongkan semua inputan
    void reset() {
        
        //mengosongkan field nama produk
        tNamaProduk.setText("Contoh: Es Jeruk"); 
        
        //mengosongkan field deskripsi produk
        tDeskProduk.setText("Opsional"); 
        
        //mengosongkan pilihan kategori
        cKategoriProduk.setSelectedItem(null);
        
        //mengosongkan field harga jual
        tHargaProduk.setText(null);
        
        tblProduk.clearSelection();
        
        //penanda bahwa mode ubah tidak dinyalakan
        modeUbah = false; 
        
        // mengosongkan id produk yang dipilih
        idProdukTerpilih = null;
        
        //mengatur teks Tambah Produk pada lblTambahProduk
        lblTambahProduk.setText("Tambah Produk"); 
        
        //mengatur teks Simpan Produk pada btnSimpanProduk
        btnSimpanProduk.setText("Simpan Produk"); 
    }
    
    //method untuk mengambil id produk otomatis berdasarkan kategori
    String IDProdukOtomatis(String kategori) {
        
        //mendeklarasikan tipe data string dengan nama variabel prefix
        String prefix = "";
        
        //mengatur pilihan kategori
        switch (kategori) { 
            
            //jika user memilih kategori seblak maka , prefix akan menampilkan huruf awalan S
            case "Seblak": 
                prefix = "S";
                break;
            //jika user memilih kategori topping maka , prefix akan menampilkan huruf awalan T
            case "Topping":
                prefix = "T";
                break;
            //jika user memilih kategori minuman maka , prefix akan menampilkan huruf awalan M
            case "Minuman":
                prefix = "M";
                break;
        }
        
        //id awal jika belum ada data produk dengan kategori tersebut
        String idBaru = prefix + "001";

        //query SQL untuk mengambil id_produk terakhir berdasarkan prefix kategori,
        //diurutkan descending agar id terbesar ada di posisi pertama
        String sql = "SELECT id_produk FROM produk WHERE id_produk LIKE ? ORDER BY id_produk DESC LIMIT 1";
        
        try {
            
            //membuka koneksi ke database menggunakan method konek()
            Connection conn = Koneksi.konek();
            
            //siapkan prepared statement
            PreparedStatement ps = conn.prepareStatement(sql);
            
            //isi parameter prefix untuk pencarian id
            ps.setString(1, prefix + "%");
            
            //jalankan query dan ambil hasilnya
            ResultSet resultSet = ps.executeQuery();
            
            //jika data ditemukan
            if (resultSet.next()) {

                //ambil id_produk terakhir hasil query
                String idTerakhir = resultSet.getString("id_produk");

                //ambil angka dari id, buang huruf prefix di depan
                int angkaTerakhir = Integer.parseInt(idTerakhir.substring(1));

                //tambahkan 1 untuk mendapatkan urutan berikutnya
                int angkaBaru = angkaTerakhir + 1;

                //gabungkan kembali huruf prefix dengan angka baru
                idBaru = prefix + String.format("%03d", angkaBaru);
            }
        } catch (SQLException sQLException) {
            //menampilkan pesan error saat berinteraksi dengan database
            JOptionPane.showMessageDialog(null, "Gagal membuat ID produk");

        }
        
        //mengembalikan id produk yang telah dibuat
        return idBaru;
    }
    //method untuk mengambil data produk dari database berdasarkan kategori yang dipilih, kemudian menampilkannya ke dalam tabel produk
    void load_tabel_produk(String kategori) {

        //membuat model tabel baru
        DefaultTableModel mdl = new DefaultTableModel();

        //menambahkan kolom ke dalam model tabel
        mdl.addColumn("Nama Produk");
        mdl.addColumn("Kategori");
        mdl.addColumn("Harga Jual");
        mdl.addColumn("Status");

        String sql;
        
        //jika yang dipilih adalah tombol semua
        if (kategori.equals("Semua")) {
            
            // QUERY SQL untuk mengambil semua data dari tabel produk
            sql = "SELECT * FROM produk "
                        + "WHERE status IN ('Tersedia', 'Tidak Tersedia') "
                        + "ORDER BY CASE "
                        + "WHEN kategori='Seblak' THEN 1 "
                        + "WHEN kategori='Minuman' THEN 2 "
                        + "WHEN kategori='Topping' THEN 3 "
                        + "END, id_produk";
        } else {
            
            //filter berdasarkan kategori yang dipilih
            sql = "SELECT * FROM produk WHERE status IN ('Tersedia', 'Tidak Tersedia') AND kategori='" + kategori + "'"; 
        }

        try {
            //membuka koneksi ke database
            Connection conn = Koneksi.konek();
            
            //membuat statement biasa karena kategori sudah divalidasi secara programatik
            Statement st = conn.createStatement();

            //menjalankan query dan menyimpan hasilnya dalam result set
            ResultSet rs = st.executeQuery(sql);

            //melakukan iterasi untuk setiap baris hasil query
            while (rs.next()) {
                
                //mengambil data kolom nama_produk
                String namaProduk = rs.getString("nama_produk");

                //mengambil data kolom kategori
                String kategoriProduk = rs.getString("kategori");

                //mengambil harga jual bertipe double
                double hargaJual = rs.getDouble("harga_jual");

                //format harga dengan titik ribuan
                String hargaProduk = FormatUang.format(hargaJual);

                //mengambil data kolom status
                String statusProduk = rs.getString("status");

                //membuat array berisi data satu baris
                Object[] baris = {namaProduk, kategoriProduk, hargaProduk, statusProduk};
                
                //menambahkan array baris ke dalam model tabel
                mdl.addRow(baris);
            }
        } catch (SQLException sQLException) {
            // menampilkan pesan error jika gagal mengambil data dari database
            JOptionPane.showMessageDialog(null, "Gagal mengambil data!");
        }
        //menampilkan model data pada tabel daftar produk
        tblProduk.setModel(mdl);
        tblProduk.setColumnWidths("100,50,50,50");

        
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

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        lblTambahProduk = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jPanel21 = new javax.swing.JPanel();
        btnSimpanProduk = new javax.swing.JButton();
        jPanel10 = new javax.swing.JPanel();
        btnBatalProduk = new javax.swing.JButton();
        btnHapusProduk = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        jPanel12 = new javax.swing.JPanel();
        lblNamaProduk = new javax.swing.JLabel();
        jPanel17 = new javax.swing.JPanel();
        tNamaProduk = new javax.swing.JTextField();
        jPanel13 = new javax.swing.JPanel();
        lblNamaProduk1 = new javax.swing.JLabel();
        jPanel18 = new javax.swing.JPanel();
        tDeskProduk = new javax.swing.JTextField();
        jPanel14 = new javax.swing.JPanel();
        lblNamaProduk2 = new javax.swing.JLabel();
        jPanel20 = new javax.swing.JPanel();
        cKategoriProduk = new javax.swing.JComboBox<>();
        jPanel15 = new javax.swing.JPanel();
        lblNamaProduk3 = new javax.swing.JLabel();
        jPanel19 = new javax.swing.JPanel();
        tHargaProduk = new javax.swing.JTextField();
        lblRupiahProduk = new javax.swing.JLabel();
        jPanel16 = new javax.swing.JPanel();
        lblStatusProduk = new javax.swing.JLabel();
        btnStatusProduk = new javax.swing.JToggleButton();
        jPanel3 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jLabell = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        filterSemuaP = new javax.swing.JToggleButton();
        filterSeblakP = new javax.swing.JToggleButton();
        filterToppingP = new javax.swing.JToggleButton();
        filterMinumanP = new javax.swing.JToggleButton();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblProduk = new jtablecustom.JTableCustom();

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
        jPanel2.setPreferredSize(new java.awt.Dimension(335, 63));

        lblTambahProduk.setFont(new java.awt.Font("Plus Jakarta Sans SemiBold", 0, 18)); // NOI18N
        lblTambahProduk.setText("Tambah  Produk");
        lblTambahProduk.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblTambahProdukMouseClicked(evt);
            }
        });

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

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 20, 20, 20));
        jPanel4.setMinimumSize(new java.awt.Dimension(345, 130));
        jPanel4.setPreferredSize(new java.awt.Dimension(345, 130));
        jPanel4.setLayout(new java.awt.CardLayout());

        jPanel21.setBackground(new java.awt.Color(255, 255, 255));
        jPanel21.setLayout(new java.awt.GridLayout(2, 0, 0, 12));

        btnSimpanProduk.setBackground(new java.awt.Color(214, 4, 39));
        btnSimpanProduk.setFont(new java.awt.Font("Plus Jakarta Sans SemiBold", 0, 16)); // NOI18N
        btnSimpanProduk.setForeground(new java.awt.Color(255, 255, 255));
        btnSimpanProduk.setIcon(new javax.swing.ImageIcon(getClass().getResource("/posseblakratu/icon/IconSimpan.png"))); // NOI18N
        btnSimpanProduk.setText("Simpan Produk");
        btnSimpanProduk.setBorderPainted(false);
        btnSimpanProduk.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSimpanProduk.setFocusable(false);
        btnSimpanProduk.setPreferredSize(new java.awt.Dimension(200, 28));
        btnSimpanProduk.addActionListener(this::btnSimpanProdukActionPerformed);
        jPanel21.add(btnSimpanProduk);

        jPanel10.setBackground(new java.awt.Color(255, 255, 255));
        jPanel10.setLayout(new java.awt.GridLayout(0, 2, 12, 0));

        btnBatalProduk.setFont(new java.awt.Font("Plus Jakarta Sans SemiBold", 0, 16)); // NOI18N
        btnBatalProduk.setText("Batal");
        btnBatalProduk.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnBatalProduk.setFocusable(false);
        btnBatalProduk.addActionListener(this::btnBatalProdukActionPerformed);
        jPanel10.add(btnBatalProduk);

        btnHapusProduk.setFont(new java.awt.Font("Plus Jakarta Sans SemiBold", 0, 16)); // NOI18N
        btnHapusProduk.setForeground(new java.awt.Color(214, 4, 39));
        btnHapusProduk.setIcon(new javax.swing.ImageIcon(getClass().getResource("/posseblakratu/icon/Vector.png"))); // NOI18N
        btnHapusProduk.setText("Hapus");
        btnHapusProduk.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnHapusProduk.setFocusable(false);
        btnHapusProduk.addActionListener(this::btnHapusProdukActionPerformed);
        jPanel10.add(btnHapusProduk);

        jPanel21.add(jPanel10);

        jPanel4.add(jPanel21, "card2");

        jPanel1.add(jPanel4, java.awt.BorderLayout.PAGE_END);

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
        lblNamaProduk.setText("Nama Produk");
        lblNamaProduk.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 4, 0));
        jPanel12.add(lblNamaProduk, java.awt.BorderLayout.PAGE_START);

        jPanel17.setBackground(new java.awt.Color(255, 255, 255));
        jPanel17.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(231, 189, 187)));
        jPanel17.setLayout(new java.awt.GridBagLayout());

        tNamaProduk.setFont(new java.awt.Font("Plus Jakarta Sans", 0, 16)); // NOI18N
        tNamaProduk.setForeground(new java.awt.Color(92, 62, 60));
        tNamaProduk.setText("Contoh: Es Jeruk");
        tNamaProduk.setBorder(null);
        tNamaProduk.setMargin(new java.awt.Insets(10, 10, 10, 6));
        tNamaProduk.setPreferredSize(new java.awt.Dimension(126, 19));
        tNamaProduk.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                tNamaProdukFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                tNamaProdukFocusLost(evt);
            }
        });
        tNamaProduk.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tNamaProdukMouseClicked(evt);
            }
        });
        tNamaProduk.addActionListener(this::tNamaProdukActionPerformed);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 278;
        gridBagConstraints.ipady = 19;
        gridBagConstraints.insets = new java.awt.Insets(1, 18, 1, 12);
        jPanel17.add(tNamaProduk, gridBagConstraints);

        jPanel12.add(jPanel17, java.awt.BorderLayout.CENTER);

        jPanel11.add(jPanel12);

        jPanel13.setBackground(new java.awt.Color(255, 255, 255));
        jPanel13.setLayout(new java.awt.BorderLayout());

        lblNamaProduk1.setBackground(new java.awt.Color(255, 255, 255));
        lblNamaProduk1.setFont(new java.awt.Font("Plus Jakarta Sans", 1, 14)); // NOI18N
        lblNamaProduk1.setText("Deskripsi");
        lblNamaProduk1.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 4, 0));
        jPanel13.add(lblNamaProduk1, java.awt.BorderLayout.PAGE_START);

        jPanel18.setBackground(new java.awt.Color(255, 255, 255));
        jPanel18.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(231, 189, 187)));
        jPanel18.setLayout(new java.awt.GridBagLayout());

        tDeskProduk.setFont(new java.awt.Font("Plus Jakarta Sans", 0, 16)); // NOI18N
        tDeskProduk.setForeground(new java.awt.Color(92, 62, 60));
        tDeskProduk.setText("Opsional");
        tDeskProduk.setBorder(null);
        tDeskProduk.setMargin(new java.awt.Insets(10, 10, 10, 6));
        tDeskProduk.setPreferredSize(new java.awt.Dimension(126, 19));
        tDeskProduk.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                tDeskProdukFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                tDeskProdukFocusLost(evt);
            }
        });
        tDeskProduk.addActionListener(this::tDeskProdukActionPerformed);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 278;
        gridBagConstraints.ipady = 19;
        gridBagConstraints.insets = new java.awt.Insets(1, 18, 1, 12);
        jPanel18.add(tDeskProduk, gridBagConstraints);

        jPanel13.add(jPanel18, java.awt.BorderLayout.CENTER);

        jPanel11.add(jPanel13);

        jPanel14.setBackground(new java.awt.Color(255, 255, 255));
        jPanel14.setLayout(new java.awt.BorderLayout());

        lblNamaProduk2.setBackground(new java.awt.Color(255, 255, 255));
        lblNamaProduk2.setFont(new java.awt.Font("Plus Jakarta Sans", 1, 14)); // NOI18N
        lblNamaProduk2.setText("Kategori");
        lblNamaProduk2.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 4, 0));
        jPanel14.add(lblNamaProduk2, java.awt.BorderLayout.PAGE_START);

        jPanel20.setBackground(new java.awt.Color(255, 255, 255));
        jPanel20.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(231, 189, 187)));
        jPanel20.setLayout(new java.awt.BorderLayout());

        cKategoriProduk.setFont(new java.awt.Font("Plus Jakarta Sans", 0, 16)); // NOI18N
        cKategoriProduk.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Seblak", "Topping", "Minuman" }));
        cKategoriProduk.setToolTipText("Pilih Kategori Produk");
        cKategoriProduk.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 12, 1, 12));
        jPanel20.add(cKategoriProduk, java.awt.BorderLayout.CENTER);

        jPanel14.add(jPanel20, java.awt.BorderLayout.CENTER);

        jPanel11.add(jPanel14);

        jPanel15.setBackground(new java.awt.Color(255, 255, 255));
        jPanel15.setLayout(new java.awt.BorderLayout());

        lblNamaProduk3.setBackground(new java.awt.Color(255, 255, 255));
        lblNamaProduk3.setFont(new java.awt.Font("Plus Jakarta Sans", 1, 14)); // NOI18N
        lblNamaProduk3.setText("Harga Jual");
        lblNamaProduk3.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 4, 0));
        jPanel15.add(lblNamaProduk3, java.awt.BorderLayout.PAGE_START);

        jPanel19.setBackground(new java.awt.Color(255, 255, 255));
        jPanel19.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(231, 189, 187)));
        jPanel19.setLayout(new java.awt.BorderLayout());

        tHargaProduk.setFont(new java.awt.Font("Plus Jakarta Sans", 0, 18)); // NOI18N
        tHargaProduk.setForeground(new java.awt.Color(215, 4, 39));
        tHargaProduk.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        tHargaProduk.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 15));
        tHargaProduk.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        tHargaProduk.setMargin(new java.awt.Insets(10, 20, 10, 6));
        tHargaProduk.setOpaque(true);
        tHargaProduk.addActionListener(this::tHargaProdukActionPerformed);
        jPanel19.add(tHargaProduk, java.awt.BorderLayout.CENTER);

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

        jPanel16.setBackground(new java.awt.Color(255, 255, 255));

        lblStatusProduk.setFont(new java.awt.Font("Plus Jakarta Sans", 1, 14)); // NOI18N
        lblStatusProduk.setText(" Status Ketersediaan");

        btnStatusProduk.setIcon(new javax.swing.ImageIcon(getClass().getResource("/posseblakratu/icon/Property 1=Variant2.png"))); // NOI18N
        btnStatusProduk.setBorder(null);
        btnStatusProduk.setContentAreaFilled(false);
        btnStatusProduk.setDisabledIcon(new javax.swing.ImageIcon(getClass().getResource("/posseblakratu/icon/Property 1=Variant2.png"))); // NOI18N
        btnStatusProduk.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/posseblakratu/icon/icon on.png"))); // NOI18N

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 309, Short.MAX_VALUE)
            .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel16Layout.createSequentialGroup()
                    .addGap(0, 0, 0)
                    .addComponent(lblStatusProduk)
                    .addGap(0, 0, Short.MAX_VALUE)))
            .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel16Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(btnStatusProduk)
                    .addGap(0, 0, 0)))
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 69, Short.MAX_VALUE)
            .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel16Layout.createSequentialGroup()
                    .addGap(0, 25, Short.MAX_VALUE)
                    .addComponent(lblStatusProduk)
                    .addGap(0, 25, Short.MAX_VALUE)))
            .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel16Layout.createSequentialGroup()
                    .addGap(0, 18, Short.MAX_VALUE)
                    .addComponent(btnStatusProduk)
                    .addGap(0, 19, Short.MAX_VALUE)))
        );

        jPanel11.add(jPanel16);

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

        add(jPanel1, java.awt.BorderLayout.LINE_END);

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(231, 189, 187)));
        jPanel3.setLayout(new java.awt.BorderLayout());

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));
        jPanel5.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(231, 189, 187)));
        jPanel5.setPreferredSize(new java.awt.Dimension(681, 112));
        jPanel5.setLayout(new java.awt.BorderLayout());

        jPanel8.setBackground(new java.awt.Color(255, 255, 255));
        jPanel8.setPreferredSize(new java.awt.Dimension(681, 63));

        jLabell.setFont(new java.awt.Font("Plus Jakarta Sans SemiBold", 0, 24)); // NOI18N
        jLabell.setForeground(new java.awt.Color(24, 26, 46));
        jLabell.setText("Daftar Produk");
        jLabell.setMaximumSize(new java.awt.Dimension(198, 31));
        jLabell.setMinimumSize(new java.awt.Dimension(198, 31));
        jLabell.setName(""); // NOI18N
        jLabell.setPreferredSize(new java.awt.Dimension(198, 63));

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(jLabell, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(390, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addComponent(jLabell, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jPanel5.add(jPanel8, java.awt.BorderLayout.PAGE_START);

        jPanel9.setBackground(new java.awt.Color(255, 255, 255));

        buttonGroup1.add(filterSemuaP);
        filterSemuaP.setFont(new java.awt.Font("Plus Jakarta Sans", 1, 14)); // NOI18N
        filterSemuaP.setForeground(new java.awt.Color(92, 62, 60));
        filterSemuaP.setText("Semua");
        filterSemuaP.setAlignmentY(0.0F);
        filterSemuaP.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 3, 0, new java.awt.Color(255, 255, 255)));
        filterSemuaP.setContentAreaFilled(false);
        filterSemuaP.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        filterSemuaP.setFocusable(false);
        filterSemuaP.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        filterSemuaP.setIconTextGap(0);
        filterSemuaP.setMargin(new java.awt.Insets(0, 0, 0, 0));
        filterSemuaP.addItemListener(this::filterSemuaPItemStateChanged);

        buttonGroup1.add(filterSeblakP);
        filterSeblakP.setFont(new java.awt.Font("Plus Jakarta Sans", 1, 14)); // NOI18N
        filterSeblakP.setForeground(new java.awt.Color(92, 62, 60));
        filterSeblakP.setText("Seblak");
        filterSeblakP.setAlignmentY(0.0F);
        filterSeblakP.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 3, 0, new java.awt.Color(255, 255, 255)));
        filterSeblakP.setContentAreaFilled(false);
        filterSeblakP.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        filterSeblakP.setFocusable(false);
        filterSeblakP.setIconTextGap(0);
        filterSeblakP.setMargin(new java.awt.Insets(1, 1, 20, 1));
        filterSeblakP.addItemListener(this::filterSeblakPItemStateChanged);

        buttonGroup1.add(filterToppingP);
        filterToppingP.setFont(new java.awt.Font("Plus Jakarta Sans", 1, 14)); // NOI18N
        filterToppingP.setForeground(new java.awt.Color(92, 62, 60));
        filterToppingP.setText("Topping");
        filterToppingP.setAlignmentY(0.0F);
        filterToppingP.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 3, 0, new java.awt.Color(255, 255, 255)));
        filterToppingP.setContentAreaFilled(false);
        filterToppingP.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        filterToppingP.setFocusable(false);
        filterToppingP.setIconTextGap(0);
        filterToppingP.setMargin(new java.awt.Insets(1, 1, 20, 1));
        filterToppingP.setMaximumSize(new java.awt.Dimension(62, 48));
        filterToppingP.setMinimumSize(new java.awt.Dimension(62, 48));
        filterToppingP.setPreferredSize(new java.awt.Dimension(62, 48));
        filterToppingP.addItemListener(this::filterToppingPItemStateChanged);

        buttonGroup1.add(filterMinumanP);
        filterMinumanP.setFont(new java.awt.Font("Plus Jakarta Sans", 1, 14)); // NOI18N
        filterMinumanP.setForeground(new java.awt.Color(92, 62, 60));
        filterMinumanP.setText("Minuman");
        filterMinumanP.setAlignmentY(0.0F);
        filterMinumanP.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 3, 0, new java.awt.Color(255, 255, 255)));
        filterMinumanP.setContentAreaFilled(false);
        filterMinumanP.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        filterMinumanP.setFocusable(false);
        filterMinumanP.setIconTextGap(0);
        filterMinumanP.setMargin(new java.awt.Insets(1, 1, 20, 1));
        filterMinumanP.setMaximumSize(new java.awt.Dimension(62, 48));
        filterMinumanP.setMinimumSize(new java.awt.Dimension(62, 48));
        filterMinumanP.setPreferredSize(new java.awt.Dimension(62, 48));
        filterMinumanP.addItemListener(this::filterMinumanPItemStateChanged);

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(filterSemuaP)
                .addGap(12, 12, 12)
                .addComponent(filterSeblakP)
                .addGap(12, 12, 12)
                .addComponent(filterToppingP, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addComponent(filterMinumanP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(408, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(filterToppingP, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(filterMinumanP, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
            .addComponent(filterSeblakP, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(filterSemuaP, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jPanel5.add(jPanel9, java.awt.BorderLayout.CENTER);

        jPanel3.add(jPanel5, java.awt.BorderLayout.PAGE_START);

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));
        jPanel6.setLayout(new java.awt.BorderLayout());

        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        tblProduk.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"Es Jeruk", "Minuman", "5.000", "Tersedia"}
            },
            new String [] {
                "Nama Produk", "Kategori", "Harga Jual", "Status"
            }
        ));
        tblProduk.setCellPaddingLeft(25);
        tblProduk.setCellPaddingRight(25);
        tblProduk.setCenterColumns("1,2,3");
        tblProduk.setColumnWidths("100,50,50,50");
        tblProduk.setFont(new java.awt.Font("Plus Jakarta Sans", 0, 14)); // NOI18N
        tblProduk.setHeaderPaddingLeft(25);
        tblProduk.setHeaderPaddingRight(25);
        tblProduk.setLeftColumns("0");
        tblProduk.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblProdukMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblProduk);

        jPanel6.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jPanel3.add(jPanel6, java.awt.BorderLayout.CENTER);

        add(jPanel3, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void filterSemuaPItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_filterSemuaPItemStateChanged
        // TODO add your handling code here
        //METHOD UNTUK MENAMPILKAN SELURUH DATA PRODUK KETIKA TOMBOL "SEMUA" DIPILIH
        //cek apakah tombol filter "Semua" sedang dipilih
        if (filterSemuaP.isSelected()) {
            //beri garis bawah merah sebagai tanda tombol sedang dipilih
            filterSemuaP.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 3, 0, new java.awt.Color(173, 0, 28)));
            filterDipilih = "Semua";
            //muat ulang tabel produk untuk menampilkan seluruh produk
            load_tabel_produk(filterDipilih);
        } else {
            //hapus garis bawah jika tombol tidak dipilih
            filterSemuaP.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 3, 0));
        }
    }//GEN-LAST:event_filterSemuaPItemStateChanged

    private void filterSeblakPItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_filterSeblakPItemStateChanged
        // TODO add your handling code here:
        //METHOD UNTUK MENAMPILKAN PRODUK YANG TERMASUK KATEGORI SEBLAK KETIKA TOMBOL "SEBLAK" DI KLIK
        //cek apakah tombol filter "Seblak" sedang dipilih
        if (filterSeblakP.isSelected()) {
            //beri garis bawah merah sebagai tanda tombol sedang dipilih
            filterSeblakP.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 3, 0, new java.awt.Color(173, 0, 28)));
            filterDipilih = "Seblak";
            //muat ulang tabel produk untuk menampilkan kategori seblak saja
            load_tabel_produk(filterDipilih);
        } else {
            //hapus garis bawah jika tombol tidak dipilih
            filterSeblakP.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 3, 0));
        }
    }//GEN-LAST:event_filterSeblakPItemStateChanged

    private void filterToppingPItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_filterToppingPItemStateChanged
        // TODO add your handling code here:
        //cek apakah tombol filter "Topping" sedang dipilih
        //METHOD UNTUK MENAMPILKAN PRODUK YANG TERMASUK KATEGORI
        if (filterToppingP.isSelected()) {
            //beri garis bawah merah sebagai tanda tombol sedang dipilih
            filterToppingP.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 3, 0, new java.awt.Color(173, 0, 28)));
            filterDipilih = "Topping";
            //muat ulang tabel produk untuk menampilkan kategori topping saja
            load_tabel_produk(filterDipilih);
        } else {
            //hapus garis bawah jika tombol tidak dipilih
            filterToppingP.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 3, 0));
        }
    }//GEN-LAST:event_filterToppingPItemStateChanged

    private void filterMinumanPItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_filterMinumanPItemStateChanged
        // TODO add your handling code here:
        //METHOD UNTUK MENAMPILKAN PRODUK YANG TERMASUK KATEGORI MINUMAN KETIKA TOMBOL "MINUMAN" DIPILIH
        //cek apakah tombol filter "Minuman" sedang dipilih
        if (filterMinumanP.isSelected()) {
            //beri garis bawah merah sebagai tanda tombol sedang dipilih
            filterMinumanP.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 3, 0, new java.awt.Color(173, 0, 28)));
            filterDipilih = "Minuman";
            //muat ulang tabel produk untuk menampilkan kategori minuman saja
            load_tabel_produk(filterDipilih);
        } else {
            //hapus garis bawah jika tombol tidak dipilih
            filterMinumanP.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 3, 0));
        }
    }//GEN-LAST:event_filterMinumanPItemStateChanged

    private void tHargaProdukActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tHargaProdukActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tHargaProdukActionPerformed

    private void tDeskProdukActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tDeskProdukActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tDeskProdukActionPerformed

    private void tNamaProdukActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tNamaProdukActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tNamaProdukActionPerformed

    private void tNamaProdukMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tNamaProdukMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tNamaProdukMouseClicked

    private void btnHapusProdukActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHapusProdukActionPerformed
        // TODO add your handling code here:
        //method untuk mengubah status produk yang dipilih menjadi 'Dihapus' di database setelah pengguna melakukan konfirmasi
        //validasi
        if (!modeUbah) {
            JOptionPane.showMessageDialog(null, "Pilih data dari tabel terlebih dahulu!");
            return;
        }
        
        //mengambil input nama produk dari text field tNamaProduk
        String namaProduk = tNamaProduk.getText();
        
        
        //menampilkan dialog konfirmasi sebelum menghapus data
        int pilihan = JOptionPane.showConfirmDialog(null,
                "Apakah anda yakin ingin manghapus produk \"" + namaProduk + "\" ?",
                "Pesan Konfirmasi", JOptionPane.YES_NO_OPTION);
        
        //jika user memilih tombol Yes maka akan menghapus data produk
        switch (pilihan) {
            case JOptionPane.YES_OPTION:

                //query SQL untuk menandai produk sebagai dihapus (soft delete)
                String sql = "UPDATE produk SET status = 'Dihapus' WHERE id_produk=?";

                try {
                    //membuka koneksi ke database
                    Connection conn = Koneksi.konek();

                    //menyiapkan statement SQL dengan parameter
                    PreparedStatement ps = conn.prepareStatement(sql);

                    //mengisi parameter dengan id produk yang dipilih
                    ps.setString(1, idProdukTerpilih);

                    //menjalankan perintah UPDATE untuk menandai produk sebagai dihapus
                    ps.execute();

                    //menampilkan pesan bahwa data berhasil dihapus
                    JOptionPane.showMessageDialog(null, "Data Produk berhasil dihapus");
                } catch (SQLException sQLException) {

                    //menampilkan pesan jika terjadi kesalahan saat menghapus data
                    JOptionPane.showMessageDialog(null, "Data Produk gagal dihapus" + sQLException.getMessage());
                }
                //muat ulang tabel produk setelah penghapusan
                load_tabel_produk(filterDipilih);
                //mengembalikan posisi scroll ke paling atas
                SwingUtilities.invokeLater(() -> {
                    jScrollPane1.getVerticalScrollBar().setValue(0);
                });

                //reset semua inputan
                reset();
                break;
            case JOptionPane.NO_OPTION:
                //jika user memilih No, tidak ada aksi yang terjadi
                break;
            default:
                //jika dialog ditutup tanpa memilih, tidak ada aksi yang terjadi
                break;
        }
    }//GEN-LAST:event_btnHapusProdukActionPerformed

    private void btnBatalProdukActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBatalProdukActionPerformed
        // TODO add your handling code here:
        //method untuk mengosongkan seluruh input form dan mengembalikannya ke kondisi awal
        //memanggil method reset agar semua inputan dikosongkan saat tombol Batal diklik
        reset();
    }//GEN-LAST:event_btnBatalProdukActionPerformed

    private void btnSimpanProdukActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSimpanProdukActionPerformed
        // TODO add your handling code here:
        //method untuk menyimpan data produk baru juga memperbarui data produk ke dalam database
        //ambil input dari text field tNamaProduk DAN simpan ke variabel namaProduk 
        String namaProduk = tNamaProduk.getText();
        
        //ambil input dari text field tDeskProduk DAN simpan ke variabel deskProduk 
        String deskProduk = tDeskProduk.getText();
        if ("Opsional".equals(deskProduk)) {
            deskProduk = null;
            
        }
        
        //ambil input dari combo box, jika tidak ada yang di select, ganti dengan string kosong
        String kategoriProduk = cKategoriProduk.getSelectedItem() != null ? cKategoriProduk.getSelectedItem().toString() : "";
        
        //ambil input dari text field tHargaProduk DAN simpan ke variabel hargaProduk 
        String hargaProduk = tHargaProduk.getText();
        
        //ambil input dari togglebutton, true jika tersedia, false jika tidak tersedia
        String statusProduk = btnStatusProduk.isSelected() ? "Tersedia" : "Tidak Tersedia";
        
        //validasi: semua field wajib diisi
        if (namaProduk.isEmpty()
                || kategoriProduk.isEmpty()
                || hargaProduk.isEmpty()
                ) {
            JOptionPane.showMessageDialog(null, "Semua field harus diisi!");
            return;
        }

        //jika dalam mode mengubah data produk, penanda modeUbah adalah true
        if (modeUbah) {
            //query SQL untuk mengubah kolom nama_produk, harga_jual, kategori, deskripsi, status berdasarkan id_produk
            String sql = "UPDATE produk SET nama_produk=?, harga_jual=?, kategori=?, deskripsi=?, status=? WHERE id_produk=?";

            try {
                //buka koneksi ke database
                Connection conn = Koneksi.konek();
                //siapkan statement SQL dengan parameter
                PreparedStatement ps = conn.prepareStatement(sql);

                //mengisi parameter pertama dengan nama produk
                ps.setString(1, namaProduk);
                //mengisi parameter kedua dengan harga produk
                ps.setString(2, hargaProduk);
                //mengisi parameter ketiga dengan kategori produk
                ps.setString(3, kategoriProduk);
                //mengisi parameter keempat dengan deskripsi produk (null jika kosong)
                if(deskProduk == null){
                    ps.setNull(4, java.sql.Types.VARCHAR);
                }else{
                ps.setString(4, deskProduk);
                }
                //mengisi parameter kelima dengan status produk
                ps.setString(5, statusProduk);
                //mengisi parameter keenam dengan id produk yang dipilih
                ps.setString(6, idProdukTerpilih);

                //jalankan query untuk mengubah data di database
                int hasil = ps.executeUpdate();
                if (hasil > 0) {
                    //tampilkan pesan bahwa data berhasil diubah
                    JOptionPane.showMessageDialog(null, "Data berhasil diubah");
                } else {
                    //tampilkan pesan jika data gagal diubah
                    JOptionPane.showMessageDialog(null, "Data gagal diubah");
                }
                //muat ulang tabel produk setelah perubahan
                load_tabel_produk(filterDipilih);
                //mengembalikan posisi scroll ke paling atas
                SwingUtilities.invokeLater(() -> {
                    jScrollPane1.getVerticalScrollBar().setValue(0);
                });
                //kembalikan ke mode tambah setelah selesai mengubah data
                modeUbah = false;
                //kosongkan id produk yang sebelumnya dipilih
                idProdukTerpilih = null;
                //reset semua inputan
                reset();
            } catch (SQLException sQLException) {
                //jika terjadi kesalahan saat mengubah data , tampilkan pesan gagal
                JOptionPane.showMessageDialog(null, "Data gagal ditambahkan!");
            }
        } else {
            //jika modeUbah false, pengguna sedang di mode tambah produk baru
            //buat id produk otomatis berdasarkan kategori produk
            String idProduk = IDProdukOtomatis(kategoriProduk);
            //query SQL untuk INSERT data produk baru ke tabel produk
            String sql = "INSERT INTO produk(id_produk,nama_produk,status,harga_jual,kategori,deskripsi,id_pengguna) VALUES(?,?,?,?,?,?,?)";

            try {
                //buka koneksi ke database
                Connection conn = Koneksi.konek();
                //siapkan statement SQL dengan parameter
                PreparedStatement ps = conn.prepareStatement(sql);

                //mengisi parameter kesatu dengan id_produk (otomatis)
                ps.setString(1, idProduk);
                //mengisi parameter kedua dengan nama produk
                ps.setString(2, namaProduk);
                //mengisi parameter ketiga dengan status produk
                ps.setString(3, statusProduk);
                //mengisi parameter keempat dengan harga produk
                ps.setString(4, hargaProduk);
                //mengisi parameter kelima dengan kategori produk
                ps.setString(5, kategoriProduk);
                //mengisi parameter keenam dengan deskripsi produk(null jika kosong)
                if(deskProduk == null){
                    ps.setNull(6, java.sql.Types.VARCHAR);
                }else{
                ps.setString(6, deskProduk);
                }
                //mengisi parameter ketujuh dengan id pengguna yang sedang login
                ps.setString(7, FrameLogin.getIdPengguna());

                //jalankan query untuk menyimpan data di database
                ps.execute();

                //tampilkan pesan bahwa data berhasil disimpan
                JOptionPane.showMessageDialog(null, "Data berhasil ditambahkan!");

            } catch (SQLException sQLException) {
                //jika terjadi kesalahan saat menyimpan data , tampilkan pesan gagal
                JOptionPane.showMessageDialog(null, "Data gagal ditambahkan!");
            }
            
            //muat ulang tabel produk setelah data disimpan
            load_tabel_produk(filterDipilih);
            //mengembalikan posisi scroll ke paling atas
            SwingUtilities.invokeLater(() -> {
                jScrollPane1.getVerticalScrollBar().setValue(0);
            });

            //kembalikan ke mode tambah setelah selesai menyimpan data baru
            modeUbah = false;
            //kosongkan id produk yang sebelumnya dipilih
            idProdukTerpilih = null;
            //reset semua inputan
            reset();
        }
    }//GEN-LAST:event_btnSimpanProdukActionPerformed

    private void tblProdukMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblProdukMouseClicked
        // TODO add your handling code here:
        //method untuk mengambil data dari tabel dan menampilkannya kembali kedalam file inputan
        //ambil indeks baris yang diklik oleh pengguna
        int barisYangDipilih = tblProduk.rowAtPoint(evt.getPoint());
        //jika baris yang diklik tidak valid (di luar area baris), hentikan proses
        if (barisYangDipilih < 0) {
            return;
        }
        //mengambil nilai dari kolom pertama (indeks 0), yaitu nama produk
        String namaProduk = tblProduk.getValueAt(barisYangDipilih, 0).toString();
        //mengambil nilai dari kolom kedua (indeks 1), yaitu kategori produk
        String kategoriProduk = tblProduk.getValueAt(barisYangDipilih, 1).toString();
        //mengambil nilai dari kolom ketiga (indeks 2), yaitu harga produk
        String hargaProduk = tblProduk.getValueAt(barisYangDipilih, 2).toString();
        //mengambil nilai dari kolom keempat (indeks 3), yaitu status produk
        String statusProduk = tblProduk.getValueAt(barisYangDipilih, 3).toString();

        //tampilkan nama produk di field tNamaProduk
        tNamaProduk.setText(namaProduk);
        //tampilkan pilihan kategori produk di combo box cKategoriProduk
        cKategoriProduk.setSelectedItem(kategoriProduk);
        //strip format "Rp. " dan titik pemisah sebelum ditampilkan ke field input angka
        tHargaProduk.setText(hargaProduk.replace("Rp. ", "").replace(".", ""));
        //tampilkan status produk; bernilai true jika tersedia
        btnStatusProduk.setSelected(statusProduk.equals("Tersedia"));

        //query SQL untuk mengambil id_produk dan deskripsi berdasarkan nama produk
        String sql = "SELECT id_produk, deskripsi FROM produk WHERE nama_produk = ?";

        try {
            //buka koneksi ke database
            Connection conn = Koneksi.konek();
            //siapkan statement SQL dengan parameter
            PreparedStatement ps = conn.prepareStatement(sql);

            //isi parameter nama produk dari baris yang diklik
            ps.setString(1, namaProduk);

            //jalankan query dan ambil hasilnya
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {

                //jika data ditemukan, simpan id produk yang dipilih
                idProdukTerpilih = rs.getString("id_produk");
                //tampilkan deskripsi produk di field tDeskProduk
                tDeskProduk.setText(rs.getString("deskripsi"));
            }
        } catch (SQLException sQLException) {
            //jika terjadi kesalahan saat mengambil data, tampilkan pesan gagal
            JOptionPane.showMessageDialog(null, " Deskripsi gagal diambil!" + sQLException.getMessage());
        }
        //aktifkan mode ubah karena pengguna sedang mengedit data
        modeUbah = true;
        //ubah judul form menjadi Edit Produk
        lblTambahProduk.setText("Edit Produk");
        //ubah teks button simpan ke mode edit
        btnSimpanProduk.setText("Simpan Perubahan");
    }//GEN-LAST:event_tblProdukMouseClicked

    private void lblTambahProdukMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblTambahProdukMouseClicked
        // TODO add your handling code here:

    }//GEN-LAST:event_lblTambahProdukMouseClicked

    private void tNamaProdukFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tNamaProdukFocusGained
        // TODO add your handling code here:
        //method ini untuk menghapus teks placeholder "contoh : Es Jeruk" saat field nama produk diklik oleh pengguna
        if (!modeUbah) {
            //ambil teks yang saat ini ada di field nama produk
            String namaP = tNamaProduk.getText();

            //jika masih berisi placeholder, kosongkan agar pengguna bisa langsung mengetik
            if (namaP.equals("Contoh: Es Jeruk")) {
                tNamaProduk.setText("");
            }
        }
    }//GEN-LAST:event_tNamaProdukFocusGained

    private void tNamaProdukFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tNamaProdukFocusLost
        // TODO add your handling code here:
        //method ini untuk memunculkan kembali teks placeholder "contoh : Es Jeruk" saat field nama produk ditinggalkan dalam keadaan kosong
        if (!modeUbah) {
            //ambil teks yang ada di field nama produk
            String namaP = tNamaProduk.getText();

            //jika kosong kembalikan tulisan placeholder
            if (namaP.equals("") || namaP.equals("Contoh: Es Jeruk")) {
                tNamaProduk.setText("Contoh: Es Jeruk");
            }
        }

    }//GEN-LAST:event_tNamaProdukFocusLost

    private void tDeskProdukFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tDeskProdukFocusGained
        // TODO add your handling code here:
        // method ini untuk menghapus teks placeholder "Opsional" saat field deksripsi porduk diklik oleh pengguna
        if (!modeUbah) {
            //ambil teks yang saat ini ada di field deskripsi produk
            String namaP = tDeskProduk.getText();

            //jika masih berisi placeholder, kosongkan agar pengguna bisa langsung mengetik
            if (namaP.equals("Opsional")) {
                tDeskProduk.setText("");
            }
        }
    }//GEN-LAST:event_tDeskProdukFocusGained

    private void tDeskProdukFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tDeskProdukFocusLost
        // TODO add your handling code here:
        //method untuk memunculkan kembali teks placeholder "Opsional" saat field deskripsi produk ditinggalkan dalam keadaan kosong
        if (!modeUbah) {
            //ambil teks yang ada di field deskripsi produk
            String namaP = tDeskProduk.getText();

            //jika kosong kembalikan tulisan placeholder
            if (namaP.equals("") || namaP.equals("Opsional")) {
                tDeskProduk.setText("Opsional");
            }
        }
    }//GEN-LAST:event_tDeskProdukFocusLost


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBatalProduk;
    private javax.swing.JButton btnHapusProduk;
    private javax.swing.JButton btnSimpanProduk;
    private javax.swing.JToggleButton btnStatusProduk;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox<String> cKategoriProduk;
    private javax.swing.JToggleButton filterMinumanP;
    private javax.swing.JToggleButton filterSeblakP;
    private javax.swing.JToggleButton filterSemuaP;
    private javax.swing.JToggleButton filterToppingP;
    private javax.swing.JLabel jLabell;
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
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
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
    private javax.swing.JLabel lblNamaProduk2;
    private javax.swing.JLabel lblNamaProduk3;
    private javax.swing.JLabel lblRupiahProduk;
    private javax.swing.JLabel lblStatusProduk;
    private javax.swing.JLabel lblTambahProduk;
    private javax.swing.JTextField tDeskProduk;
    private javax.swing.JTextField tHargaProduk;
    private javax.swing.JTextField tNamaProduk;
    private jtablecustom.JTableCustom tblProduk;
    // End of variables declaration//GEN-END:variables
}
