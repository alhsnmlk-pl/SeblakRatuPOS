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
import javax.swing.table.DefaultTableModel;
import javax.swing.JPanel;
import posseblakratu.config.Koneksi;


/**
 *
 * @author Al
 */
public final class PanelDiskon extends javax.swing.JPanel {

    //menyimpan id_diskon dari baris tabel yang sedang dipilih
    private String idDiskonDipilih = "";

    //true jika pengguna sedang mode edit, false jika mode tambah
    private boolean sedangEdit = false;

    /**
     * Creates new form PanelDiskon
     */
    public PanelDiskon() {
        initComponents();

        //membuat panel memiliki sudut membulat
        panelLengkung(jPanel23);
        panelLengkung(jPanel18);
        panelLengkung(jPanel20);

        //memanggil method untuk menampilkan data diskon ke tabel
        load_tabel_diskon();
        
        reset();
    }

    void panelLengkung(JPanel p) {

        p.setBorder(new FlatLineBorder(
                new Insets(3, 3, 3, 3),
                Color.decode("#E7BDBB"),
                1f,
                10));

        tambahDiskon.setBorder(new FlatLineBorder(
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

        //ubah judul panel kembali ke tambah diskon
        lblTambahDiskon.setText("Tambah Diskon");

        //kembalikan text button simpan ke mode tambah
        btnSimpanDiskon.setText("Simpan Diskon");

        //kosongkan field nama diskon
        tNamaDiskon.setText("Contoh: jumat berkah");

        //kembalikan combobox tipe ke pilihan pertama
        cTipeDiskon.setSelectedItem(null);

        //kosongkan field nilai diskon
        tValueDiskon.setText(null);

        //kembalikan toggle status ke posisi nonaktif
        btnStatusProduk.setSelected(false);

        //hapus seleksi pada tabel
        tblDiskon.clearSelection();

        //kembalikan ke mode tambah
        sedangEdit = false;

        //kosongkan id yang dipilih
        idDiskonDipilih = "";
    }


    //membuat method untuk generate id diskon otomatis
    String generateIdDiskon() {

        //variabel untuk menyimpan id terakhir dari database
        String lastId = null;

        try {
            //buka koneksi ke database
            Connection conn = Koneksi.konek();

            //query untuk mengambil id diskon terakhir
            String sql = "SELECT id_diskon FROM diskon ORDER BY id_diskon DESC LIMIT 1";

            //siapkan statement
            PreparedStatement ps = conn.prepareStatement(sql);

            //jalankan query
            ResultSet rs = ps.executeQuery();

            //jika ada data ambil id terakhirnya
            if (rs.next()) {
                lastId = rs.getString("id_diskon");
            }

        } catch (SQLException sQLException) {
            //tampilkan error jika gagal
            JOptionPane.showMessageDialog(null, "Gagal membuat id diskon!");
        }

        //jika belum ada diskon sama sekali mulai dari DS001
        if (lastId == null) {
            return "DS001";
        }

        //mengambil angka dari DS001 menjadi 001
        int angka = Integer.parseInt(lastId.substring(2));

        //increment angka
        angka++;

        //format ulang jadi DS002 dst
        return String.format("DS%03d", angka);
    }


    //membuat method load tabel diskon
    void load_tabel_diskon() {

        //membuat model tabel baru
        DefaultTableModel model = new DefaultTableModel();

        //menambahkan kolom ke dalam model tabel
        model.addColumn("Nama Diskon");
        model.addColumn("Tipe");
        model.addColumn("Nilai");
        model.addColumn("Status");

        //query SQL untuk mengambil semua data diskon
        String sql = "SELECT * FROM diskon";

        try {
            //membuka koneksi ke database
            Connection conn = Koneksi.konek();

            //menyiapkan statement
            PreparedStatement ps = conn.prepareStatement(sql);

            //menjalankan query dan menyimpan hasilnya
            ResultSet rs = ps.executeQuery();

            //melakukan iterasi untuk setiap baris hasil query
            while (rs.next()) {

                //mengambil data dari setiap kolom
                String namaDiskon = rs.getString("nama_diskon");
                String tipeDiskon = rs.getString("tipe_diskon");
                int nilaiDiskon = (int) rs.getDouble("nilai_diskon");
                String statusDiskon = rs.getString("status");

                //menyimpan data ke dalam array
                Object[] baris = {namaDiskon, tipeDiskon, nilaiDiskon, statusDiskon};

                //menambahkan baris ke model tabel
                model.addRow(baris);
            }

        } catch (SQLException sQLException) {
            //menampilkan pesan error jika gagal mengambil data
            JOptionPane.showMessageDialog(null, "gagal mengambil data!");
        }

        //menampilkan model yang sudah diisi ke dalam tabel GUI
        tblDiskon.setModel(model);
        tblDiskon.setColumnWidths("100,50,50,50");
    }
    
    
    
    
    
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jCheckBox1 = new javax.swing.JCheckBox();
        tambahDiskon = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        lblTambahDiskon = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        btnSimpanDiskon = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        btnBatalDiskon = new javax.swing.JButton();
        btnHapusDiskon = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        jPanel12 = new javax.swing.JPanel();
        lblNamaProduk = new javax.swing.JLabel();
        jPanel23 = new javax.swing.JPanel();
        tNamaDiskon = new javax.swing.JTextField();
        jPanel13 = new javax.swing.JPanel();
        lblNamaProduk1 = new javax.swing.JLabel();
        jPanel18 = new javax.swing.JPanel();
        cTipeDiskon = new javax.swing.JComboBox<>();
        jPanel14 = new javax.swing.JPanel();
        lblNamaProduk2 = new javax.swing.JLabel();
        jPanel20 = new javax.swing.JPanel();
        tValueDiskon = new javax.swing.JTextField();
        jPanel19 = new javax.swing.JPanel();
        lblStatusProduk = new javax.swing.JLabel();
        btnStatusProduk = new javax.swing.JToggleButton();
        jPanel3 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        lblDaftarProduk = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblDiskon = new jtablecustom.JTableCustom();

        jCheckBox1.setText("jCheckBox1");

        setBackground(new java.awt.Color(252, 249, 255));
        setBorder(javax.swing.BorderFactory.createEmptyBorder(15, 15, 15, 15));
        setMinimumSize(new java.awt.Dimension(1075, 639));
        setLayout(new java.awt.BorderLayout(15, 0));

        tambahDiskon.setBackground(new java.awt.Color(255, 255, 255));
        tambahDiskon.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(231, 189, 187)));
        tambahDiskon.setPreferredSize(new java.awt.Dimension(347, 611));
        tambahDiskon.setLayout(new java.awt.BorderLayout());

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(231, 189, 187)));
        jPanel2.setMinimumSize(new java.awt.Dimension(335, 63));

        lblTambahDiskon.setFont(new java.awt.Font("Plus Jakarta Sans SemiBold", 0, 18)); // NOI18N
        lblTambahDiskon.setText("Tambah  Diskon");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(lblTambahDiskon, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(126, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblTambahDiskon, javax.swing.GroupLayout.DEFAULT_SIZE, 62, Short.MAX_VALUE)
        );

        tambahDiskon.add(jPanel2, java.awt.BorderLayout.PAGE_START);

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 20, 20, 20));
        jPanel4.setMinimumSize(new java.awt.Dimension(345, 130));
        jPanel4.setPreferredSize(new java.awt.Dimension(345, 130));
        jPanel4.setLayout(new java.awt.CardLayout());

        jPanel9.setBackground(new java.awt.Color(255, 255, 255));
        jPanel9.setLayout(new java.awt.GridLayout(2, 0, 0, 12));

        btnSimpanDiskon.setBackground(new java.awt.Color(214, 4, 39));
        btnSimpanDiskon.setFont(new java.awt.Font("Plus Jakarta Sans SemiBold", 0, 16)); // NOI18N
        btnSimpanDiskon.setForeground(new java.awt.Color(255, 255, 255));
        btnSimpanDiskon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/posseblakratu/icon/IconSimpan.png"))); // NOI18N
        btnSimpanDiskon.setText("Simpan Diskon");
        btnSimpanDiskon.setBorderPainted(false);
        btnSimpanDiskon.addActionListener(this::btnSimpanDiskonActionPerformed);
        jPanel9.add(btnSimpanDiskon);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new java.awt.GridLayout(0, 2, 12, 0));

        btnBatalDiskon.setFont(new java.awt.Font("Plus Jakarta Sans SemiBold", 0, 16)); // NOI18N
        btnBatalDiskon.setText("Batal");
        btnBatalDiskon.addActionListener(this::btnBatalDiskonActionPerformed);
        jPanel1.add(btnBatalDiskon);

        btnHapusDiskon.setFont(new java.awt.Font("Plus Jakarta Sans SemiBold", 0, 16)); // NOI18N
        btnHapusDiskon.setForeground(new java.awt.Color(214, 4, 39));
        btnHapusDiskon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/posseblakratu/icon/Vector.png"))); // NOI18N
        btnHapusDiskon.setText("Hapus");
        btnHapusDiskon.addActionListener(this::btnHapusDiskonActionPerformed);
        jPanel1.add(btnHapusDiskon);

        jPanel9.add(jPanel1);

        jPanel4.add(jPanel9, "card2");

        tambahDiskon.add(jPanel4, java.awt.BorderLayout.PAGE_END);

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));
        jPanel7.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(231, 189, 187)));

        jPanel11.setBackground(new java.awt.Color(255, 255, 255));
        jPanel11.setBorder(javax.swing.BorderFactory.createEmptyBorder(18, 18, 0, 18));
        jPanel11.setMaximumSize(new java.awt.Dimension(32767, 350));
        jPanel11.setPreferredSize(new java.awt.Dimension(345, 340));
        jPanel11.setLayout(new java.awt.GridLayout(5, 1, 0, 10));

        jPanel12.setBackground(new java.awt.Color(255, 255, 255));
        jPanel12.setBorder(javax.swing.BorderFactory.createEmptyBorder(2, 0, 0, 0));
        jPanel12.setPreferredSize(new java.awt.Dimension(128, 42));
        jPanel12.setLayout(new java.awt.BorderLayout());

        lblNamaProduk.setBackground(new java.awt.Color(255, 255, 255));
        lblNamaProduk.setFont(new java.awt.Font("Plus Jakarta Sans", 1, 14)); // NOI18N
        lblNamaProduk.setText("Nama Diskon");
        lblNamaProduk.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 4, 0));
        jPanel12.add(lblNamaProduk, java.awt.BorderLayout.PAGE_START);

        jPanel23.setBackground(new java.awt.Color(255, 255, 255));
        jPanel23.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(231, 189, 187)));
        jPanel23.setLayout(new java.awt.BorderLayout());

        tNamaDiskon.setFont(new java.awt.Font("Plus Jakarta Sans", 0, 16)); // NOI18N
        tNamaDiskon.setForeground(new java.awt.Color(92, 62, 60));
        tNamaDiskon.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 17, 0, 0));
        tNamaDiskon.setMargin(new java.awt.Insets(10, 10, 10, 6));
        tNamaDiskon.setOpaque(true);
        tNamaDiskon.setPreferredSize(new java.awt.Dimension(126, 19));
        tNamaDiskon.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                tNamaDiskonFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                tNamaDiskonFocusLost(evt);
            }
        });
        tNamaDiskon.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tNamaDiskonMouseClicked(evt);
            }
        });
        tNamaDiskon.addActionListener(this::tNamaDiskonActionPerformed);
        jPanel23.add(tNamaDiskon, java.awt.BorderLayout.CENTER);

        jPanel12.add(jPanel23, java.awt.BorderLayout.CENTER);

        jPanel11.add(jPanel12);

        jPanel13.setBackground(new java.awt.Color(255, 255, 255));
        jPanel13.setLayout(new java.awt.BorderLayout());

        lblNamaProduk1.setBackground(new java.awt.Color(255, 255, 255));
        lblNamaProduk1.setFont(new java.awt.Font("Plus Jakarta Sans", 1, 14)); // NOI18N
        lblNamaProduk1.setText("Tipe Diskon");
        lblNamaProduk1.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 4, 0));
        jPanel13.add(lblNamaProduk1, java.awt.BorderLayout.PAGE_START);

        jPanel18.setBackground(new java.awt.Color(255, 255, 255));
        jPanel18.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(231, 189, 187)));
        jPanel18.setLayout(new java.awt.BorderLayout());

        cTipeDiskon.setFont(new java.awt.Font("Plus Jakarta Sans", 0, 16)); // NOI18N
        cTipeDiskon.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Nominal", "Persentase" }));
        cTipeDiskon.setToolTipText("Silahkan Pilih Tipe Diskon");
        cTipeDiskon.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 12, 1, 12));
        cTipeDiskon.setOpaque(true);
        jPanel18.add(cTipeDiskon, java.awt.BorderLayout.CENTER);

        jPanel13.add(jPanel18, java.awt.BorderLayout.CENTER);

        jPanel11.add(jPanel13);

        jPanel14.setBackground(new java.awt.Color(255, 255, 255));
        jPanel14.setLayout(new java.awt.BorderLayout());

        lblNamaProduk2.setBackground(new java.awt.Color(255, 255, 255));
        lblNamaProduk2.setFont(new java.awt.Font("Plus Jakarta Sans", 1, 14)); // NOI18N
        lblNamaProduk2.setText("Nominal / Persentase");
        lblNamaProduk2.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 4, 0));
        jPanel14.add(lblNamaProduk2, java.awt.BorderLayout.PAGE_START);

        jPanel20.setBackground(new java.awt.Color(255, 255, 255));
        jPanel20.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(231, 189, 187)));
        jPanel20.setLayout(new java.awt.BorderLayout());

        tValueDiskon.setFont(new java.awt.Font("Plus Jakarta Sans", 0, 16)); // NOI18N
        tValueDiskon.setForeground(new java.awt.Color(92, 62, 60));
        tValueDiskon.setText("0");
        tValueDiskon.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 18, 1, 1));
        tValueDiskon.setMargin(new java.awt.Insets(10, 10, 10, 6));
        tValueDiskon.setOpaque(true);
        tValueDiskon.setPreferredSize(new java.awt.Dimension(126, 19));
        tValueDiskon.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tValueDiskonMouseClicked(evt);
            }
        });
        tValueDiskon.addActionListener(this::tValueDiskonActionPerformed);
        jPanel20.add(tValueDiskon, java.awt.BorderLayout.CENTER);

        jPanel14.add(jPanel20, java.awt.BorderLayout.CENTER);

        jPanel11.add(jPanel14);

        jPanel19.setBackground(new java.awt.Color(255, 255, 255));

        lblStatusProduk.setFont(new java.awt.Font("Plus Jakarta Sans", 1, 14)); // NOI18N
        lblStatusProduk.setText(" Status Diskon");

        btnStatusProduk.setIcon(new javax.swing.ImageIcon(getClass().getResource("/posseblakratu/icon/Property 1=Variant2.png"))); // NOI18N
        btnStatusProduk.setBorder(null);
        btnStatusProduk.setContentAreaFilled(false);
        btnStatusProduk.setDisabledIcon(new javax.swing.ImageIcon(getClass().getResource("/posseblakratu/icon/Property 1=Variant2.png"))); // NOI18N
        btnStatusProduk.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/posseblakratu/icon/icon on.png"))); // NOI18N

        javax.swing.GroupLayout jPanel19Layout = new javax.swing.GroupLayout(jPanel19);
        jPanel19.setLayout(jPanel19Layout);
        jPanel19Layout.setHorizontalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 309, Short.MAX_VALUE)
            .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel19Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(lblStatusProduk, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(181, Short.MAX_VALUE)))
            .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel19Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(btnStatusProduk)
                    .addGap(0, 0, 0)))
        );
        jPanel19Layout.setVerticalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 70, Short.MAX_VALUE)
            .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel19Layout.createSequentialGroup()
                    .addContainerGap(25, Short.MAX_VALUE)
                    .addComponent(lblStatusProduk)
                    .addContainerGap(26, Short.MAX_VALUE)))
            .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel19Layout.createSequentialGroup()
                    .addGap(0, 19, Short.MAX_VALUE)
                    .addComponent(btnStatusProduk)
                    .addGap(0, 19, Short.MAX_VALUE)))
        );

        jPanel11.add(jPanel19);

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
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, 408, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tambahDiskon.add(jPanel7, java.awt.BorderLayout.CENTER);

        add(tambahDiskon, java.awt.BorderLayout.LINE_END);

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(231, 189, 187)));
        jPanel3.setLayout(new java.awt.BorderLayout());

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));
        jPanel5.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(231, 189, 187)));
        jPanel5.setPreferredSize(new java.awt.Dimension(681, 63));
        jPanel5.setLayout(new java.awt.BorderLayout());

        jPanel8.setBackground(new java.awt.Color(255, 255, 255));
        jPanel8.setPreferredSize(new java.awt.Dimension(681, 63));

        lblDaftarProduk.setFont(new java.awt.Font("Plus Jakarta Sans SemiBold", 0, 24)); // NOI18N
        lblDaftarProduk.setForeground(new java.awt.Color(24, 26, 46));
        lblDaftarProduk.setText("Daftar Diskon");
        lblDaftarProduk.setName(""); // NOI18N
        lblDaftarProduk.setPreferredSize(new java.awt.Dimension(198, 63));

        jPanel10.setBackground(new java.awt.Color(255, 255, 255));
        jPanel10.setBorder(javax.swing.BorderFactory.createEmptyBorder(16, 0, 16, 0));
        jPanel10.setLayout(new java.awt.BorderLayout());

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(lblDaftarProduk, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblDaftarProduk, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jPanel5.add(jPanel8, java.awt.BorderLayout.CENTER);

        jPanel3.add(jPanel5, java.awt.BorderLayout.PAGE_START);

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));
        jPanel6.setLayout(new java.awt.BorderLayout());

        tblDiskon.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"Uang", "Nominal", "1000", "Aktif"},
                {"Persen", "Persentase", "10", "Tidak Aktif"}
            },
            new String [] {
                "Nama Diskon", "Tipe", "Nilai", "Status"
            }
        ));
        tblDiskon.setCellPaddingLeft(25);
        tblDiskon.setCellPaddingRight(25);
        tblDiskon.setCenterColumns("1,2,3");
        tblDiskon.setColumnWidths("100,50,50,50");
        tblDiskon.setFont(new java.awt.Font("Plus Jakarta Sans", 0, 14)); // NOI18N
        tblDiskon.setHeaderPaddingLeft(25);
        tblDiskon.setHeaderPaddingRight(25);
        tblDiskon.setLeftColumns("0");
        tblDiskon.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblDiskonMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblDiskon);

        jPanel6.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jPanel3.add(jPanel6, java.awt.BorderLayout.CENTER);

        add(jPanel3, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void btnTambahDiskonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTambahDiskonActionPerformed

        //tidak ada aksi tambahan pada tombol ini

    }//GEN-LAST:event_btnTambahDiskonActionPerformed

    private void tValueDiskonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tValueDiskonMouseClicked

        //tidak ada aksi tambahan saat field nilai diklik

    }//GEN-LAST:event_tValueDiskonMouseClicked

    private void tValueDiskonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tValueDiskonActionPerformed

        //tidak ada aksi tambahan pada field nilai diskon

    }//GEN-LAST:event_tValueDiskonActionPerformed

    private void tNamaDiskonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tNamaDiskonActionPerformed

        //tidak ada aksi tambahan pada field nama diskon

    }//GEN-LAST:event_tNamaDiskonActionPerformed

    private void tNamaDiskonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tNamaDiskonMouseClicked

        //tidak ada aksi tambahan saat field nama diklik

    }//GEN-LAST:event_tNamaDiskonMouseClicked

    private void btnBatalDiskonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBatalDiskonActionPerformed

        //memanggil method reset untuk mengosongkan form dan kembali ke mode tambah
        reset();

    }//GEN-LAST:event_btnBatalDiskonActionPerformed

    private void btnSimpanDiskonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSimpanDiskonActionPerformed

        //mengambil input dari semua field form
        String namaDiskon = tNamaDiskon.getText();
        String tipeDiskon = cTipeDiskon.getSelectedItem() != null ? cTipeDiskon.getSelectedItem().toString() : "";
        String nilaiDiskonStr = tValueDiskon.getText();

        //mengambil status dari toggle button
        String statusDiskon = btnStatusProduk.isSelected() ? "Aktif" : "Nonaktif";

        //validasi: semua field wajib diisi
        if (namaDiskon.isEmpty() || tipeDiskon.isEmpty() || nilaiDiskonStr.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Semua field harus diisi!");
            return;
        }

        //konversi nilai diskon ke tipe numerik
        double nilaiDouble = Double.parseDouble(nilaiDiskonStr);

        if (sedangEdit) {
            //MODE EDIT: update data yang sudah ada berdasarkan id_diskon

            //query SQL untuk mengubah data diskon
            String sql = "UPDATE diskon SET nama_diskon=?, tipe_diskon=?, nilai_diskon=?, status=? WHERE id_diskon=?";

            try {
                //buka koneksi ke database
                Connection conn = Koneksi.konek();

                //siapkan statement dengan parameter
                PreparedStatement ps = conn.prepareStatement(sql);

                //isi parameter nama diskon baru
                ps.setString(1, namaDiskon);

                //isi parameter tipe diskon baru
                ps.setString(2, tipeDiskon);

                //isi parameter nilai diskon baru
                ps.setDouble(3, nilaiDouble);

                //isi parameter status baru
                ps.setString(4, statusDiskon);

                //isi parameter kondisi WHERE dengan id_diskon yang dipilih
                ps.setString(5, idDiskonDipilih);

                //jalankan query update
                ps.execute();

                //tampilkan pesan berhasil
                JOptionPane.showMessageDialog(null, "Data diskon berhasil diubah!");

            } catch (SQLException sQLException) {
                //tampilkan pesan jika gagal mengubah data
                JOptionPane.showMessageDialog(null, sQLException.getMessage());
            }

        } else {
            //MODE TAMBAH: insert data baru ke database

            //generate id_diskon baru secara otomatis
            String idDiskonBaru = generateIdDiskon();

            //ambil id pengguna yang sedang login dari session
            String idPenggunaLogin = FrameLogin.getIdPengguna();

            //query SQL untuk menyisipkan data diskon baru
            String sql = "INSERT INTO diskon (id_diskon, nama_diskon, tipe_diskon, nilai_diskon, status, id_pengguna) VALUES (?,?,?,?,?,?)";

            try {
                //buka koneksi ke database
                Connection conn = Koneksi.konek();

                //siapkan statement dengan parameter
                PreparedStatement ps = conn.prepareStatement(sql);

                //isi parameter id_diskon yang sudah di-generate
                ps.setString(1, idDiskonBaru);

                //isi parameter nama diskon
                ps.setString(2, namaDiskon);

                //isi parameter tipe diskon
                ps.setString(3, tipeDiskon);

                //isi parameter nilai diskon
                ps.setDouble(4, nilaiDouble);

                //isi parameter status diskon
                ps.setString(5, statusDiskon);

                //isi parameter id pengguna dari session login
                ps.setString(6, idPenggunaLogin);

                //jalankan query insert
                ps.execute();

                //tampilkan pesan berhasil
                JOptionPane.showMessageDialog(null, "Data diskon berhasil disimpan!");

            } catch (SQLException sQLException) {
                //tampilkan pesan jika gagal menyimpan data
                JOptionPane.showMessageDialog(null, sQLException.getMessage());
            }
        }

        //muat ulang tabel agar perubahan tampil
        load_tabel_diskon();

        //reset form kembali ke mode tambah
        reset();

    }//GEN-LAST:event_btnSimpanDiskonActionPerformed

    private void tblDiskonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDiskonMouseClicked

        //ambil indeks baris yang diklik pada tabel diskon
        int baris = tblDiskon.rowAtPoint(evt.getPoint());

        //abaikan jika klik tidak mengenai baris manapun
        if (baris == -1) {
            return;
        }

        //ambil nilai dari kolom 0 (nama diskon)
        String namaDiskon = tblDiskon.getValueAt(baris, 0).toString();

        //ambil nilai dari kolom 1 (tipe diskon)
        String tipeDiskon = tblDiskon.getValueAt(baris, 1).toString();

        //ambil nilai dari kolom 2 (nilai diskon)
        String nilaiDiskon = tblDiskon.getValueAt(baris, 2).toString();

        //ambil nilai dari kolom 3 (status diskon)
        String statusDiskon = tblDiskon.getValueAt(baris, 3).toString();

        //query ke database untuk mengambil id_diskon berdasarkan nama diskon
        String sql = "SELECT id_diskon FROM diskon WHERE nama_diskon = ?";

        try {
            //buka koneksi ke database
            Connection conn = Koneksi.konek();

            //siapkan statement dengan parameter
            PreparedStatement ps = conn.prepareStatement(sql);

            //isi parameter nama diskon dari baris yang diklik
            ps.setString(1, namaDiskon);

            //jalankan query
            ResultSet rs = ps.executeQuery();

            //jika data ditemukan simpan id_diskon ke variabel
            if (rs.next()) {
                idDiskonDipilih = rs.getString("id_diskon");
            }

        } catch (SQLException sQLException) {
            //tampilkan pesan jika gagal mengambil id diskon
            JOptionPane.showMessageDialog(null, "Gagal mengambil id diskon!");
            return;
        }

        //aktifkan mode edit
        sedangEdit = true;

        //ubah judul panel menjadi Edit Diskon
        lblTambahDiskon.setText("Edit Diskon");

        //ubah text button simpan ke mode edit
        btnSimpanDiskon.setText("Simpan Perubahan");

        //tampilkan data dari baris yang dipilih ke form input
        tNamaDiskon.setText(namaDiskon);
        cTipeDiskon.setSelectedItem(tipeDiskon);
        tValueDiskon.setText(nilaiDiskon);

        //set toggle status sesuai nilai dari database
        btnStatusProduk.setSelected(statusDiskon.equals("Aktif"));

    }//GEN-LAST:event_tblDiskonMouseClicked

    private void btnHapusDiskonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHapusDiskonActionPerformed

        //cek apakah ada baris yang dipilih dari tabel
        if (!sedangEdit) {
            JOptionPane.showMessageDialog(null, "Pilih data dari tabel terlebih dahulu!");
            return;
        }

        //tampilkan dialog konfirmasi sebelum menghapus
        int konfirmasi = JOptionPane.showConfirmDialog(null,
                "Yakin ingin menghapus diskon ini?",
                "Konfirmasi Hapus",
                JOptionPane.YES_NO_OPTION);

        //jika pengguna memilih tidak batalkan hapus
        if (konfirmasi != JOptionPane.YES_OPTION) {
            return;
        }

        //query SQL untuk menghapus data diskon berdasarkan id_diskon
        String sql = "DELETE FROM diskon WHERE id_diskon=?";

        try {
            //buka koneksi ke database
            Connection conn = Koneksi.konek();

            //siapkan statement dengan parameter
            PreparedStatement ps = conn.prepareStatement(sql);

            //isi parameter id_diskon dari baris yang dipilih
            ps.setString(1, idDiskonDipilih);

            //jalankan query delete
            ps.execute();

            //tampilkan pesan berhasil
            JOptionPane.showMessageDialog(null, "Data diskon berhasil dihapus!");

        } catch (SQLException sQLException) {
            //tampilkan pesan jika gagal menghapus data
            JOptionPane.showMessageDialog(null, "Data diskon gagal dihapus!");
        }

        //muat ulang tabel agar perubahan tampil
        load_tabel_diskon();

        //reset form kembali ke mode tambah
        reset();

    }//GEN-LAST:event_btnHapusDiskonActionPerformed

    private void tNamaDiskonFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tNamaDiskonFocusGained
        // TODO add your handling code here:
        if (!sedangEdit) {
            //ambil teks yang saat ini ada di field username
            String namaP = tNamaDiskon.getText();

            //jika masih berisi placeholder, kosongkan agar pengguna bisa langsung mengetik
            if (namaP.equals("Contoh: jumat berkah")) {
                tNamaDiskon.setText("");
            }
        }
        
    }//GEN-LAST:event_tNamaDiskonFocusGained

    private void tNamaDiskonFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tNamaDiskonFocusLost
        // TODO add your handling code here:
        if (!sedangEdit) {
            //ambil teks yang ada di field username
            String namaP = tNamaDiskon.getText();

            //jika kosong kembalikan tulisan placeholder
            if (namaP.equals("") || namaP.equals("Contoh: jumat berkah")) {
                tNamaDiskon.setText("Contoh: jumat berkah");
            }
        }
    }//GEN-LAST:event_tNamaDiskonFocusLost


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBatalDiskon;
    private javax.swing.JButton btnHapusDiskon;
    private javax.swing.JButton btnSimpanDiskon;
    private javax.swing.JToggleButton btnStatusProduk;
    private javax.swing.JComboBox<String> cTipeDiskon;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblDaftarProduk;
    private javax.swing.JLabel lblNamaProduk;
    private javax.swing.JLabel lblNamaProduk1;
    private javax.swing.JLabel lblNamaProduk2;
    private javax.swing.JLabel lblStatusProduk;
    private javax.swing.JLabel lblTambahDiskon;
    private javax.swing.JTextField tNamaDiskon;
    private javax.swing.JTextField tValueDiskon;
    private javax.swing.JPanel tambahDiskon;
    private jtablecustom.JTableCustom tblDiskon;
    // End of variables declaration//GEN-END:variables
}
