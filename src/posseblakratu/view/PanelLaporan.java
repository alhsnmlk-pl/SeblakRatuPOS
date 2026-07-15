/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package posseblakratu.view;

import com.formdev.flatlaf.ui.FlatLineBorder;
import java.awt.Color;
import java.awt.Insets;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;
import posseblakratu.config.Koneksi;
import posseblakratu.config.FormatUang;

/**
 *
 * @author Al
 */
public final class PanelLaporan extends javax.swing.JPanel {

    //id transaksi dari baris tabel yang sedang dipilih
    private String selectedIdTransaksi = null;

    //tipe baris yang sedang dipilih (Pemasukan / Pengeluaran)
    private String selectedTipe = null;
    
    /**
     * Creates new form PanelLaporan
     */
    public PanelLaporan() {
        initComponents();
        panelLengkung(jPanel9);
        panelLengkung(jPanel10);
        panelLengkung(jPanel14);
        panelLengkung(main);

        //load laporan sesuai bulan yang aktif saat ini
        loadLaporan();   
    }

    void panelLengkung(JPanel p) {

        p.setBorder(new FlatLineBorder(
                new Insets(3, 3, 3, 3),
                Color.decode("#E7BDBB"),
                1f,
                10));

    }

    //method untuk mendapatkan periode aktif dalam format yyyy-MM
    private String getPeriode() {

        //ambil tahun saat ini
        int tahun = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR);

        //ambil bulan dari komponen PeriodeBulan (0 = Januari)
        int bulan = PeriodeBulan.getMonth() + 1;

        //kembalikan periode dalam format yyyy-MM
        return String.format("%04d-%02d", tahun, bulan);
    }

    //method untuk mengambil total pemasukan dari database berdasarkan periode
    private double getTotalPemasukan(String periode) {

        double total = 0;

        //query total pemasukan
        String sql = "SELECT COALESCE(SUM(total_akhir), 0) AS total FROM transaksi "
                + "WHERE DATE_FORMAT(tanggal, '%Y-%m') = ?";

        try {
            //buka koneksi ke database
            Connection conn = Koneksi.konek();

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, periode);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                total = rs.getDouble("total");
            }

        } catch (SQLException sQLException) {
            //tampilkan pesan jika gagal mengambil total pemasukan
            JOptionPane.showMessageDialog(null, sQLException.getMessage());
        }

        return total;
    }

    //method untuk mengambil total pengeluaran dari database berdasarkan periode
    private double getTotalPengeluaran(String periode) {

        double total = 0;

        //query total pengeluaran
        String sql = "SELECT COALESCE(SUM(total), 0) AS total FROM pengeluaran "
                + "WHERE DATE_FORMAT(tanggal, '%Y-%m') = ?";

        try {
            //buka koneksi ke database
            Connection conn = Koneksi.konek();

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, periode);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                total = rs.getDouble("total");
            }

        } catch (SQLException sQLException) {
            //tampilkan pesan jika gagal mengambil total pengeluaran
            JOptionPane.showMessageDialog(null, sQLException.getMessage());
        }

        return total;
    }

    //method untuk menampilkan ringkasan keuangan ke label-label di UI
    private void tampilkanRingkasan(double totalPemasukan, double totalPengeluaran) {

        //hitung laba bersih
        double labaBersih = totalPemasukan - totalPengeluaran;

        //tampilkan label nama kartu pemasukan
        lblpemasukkan.setText("Pemasukan");

        //tampilkan total pemasukan ke label
        lblTotalPemasukan.setText(FormatUang.format(totalPemasukan));

        //tampilkan total pengeluaran ke label
        lblTotalPengeluaran.setText(FormatUang.format(totalPengeluaran));

        //tampilkan laba bersih ke label
        lblLabaBersih.setText(FormatUang.format(labaBersih));
    }

    //method untuk load laporan berdasarkan bulan yang dipilih
    public void loadLaporan() {

        //ambil periode aktif
        String periode = getPeriode();

        //ambil total pemasukan dan pengeluaran bulan ini
        double totalPemasukan = getTotalPemasukan(periode);
        double totalPengeluaran = getTotalPengeluaran(periode);

        //tampilkan ringkasan keuangan ke label-label UI
        tampilkanRingkasan(totalPemasukan, totalPengeluaran);

        //load tabel rincian transaksi
        loadTabelLaporan(periode);
    }

    //method untuk memuat tabel rincian transaksi berdasarkan periode
    public void loadTabelLaporan(String periode) {

        //buat model tabel baru
        DefaultTableModel model = new DefaultTableModel();

        //tambahkan kolom ke dalam model tabel
        model.addColumn("No Referensi");
        model.addColumn("Tanggal");
        model.addColumn("Kategori");
        model.addColumn("Tipe");
        model.addColumn("Jumlah");

        //query gabungan pemasukan dan pengeluaran diurutkan berdasarkan waktu
        String sqlGabungan
                = "SELECT id_transaksi AS no_ref, tanggal, total_akhir AS jumlah, "
                + "'Penjualan' AS kategori, 'Pemasukan' AS tipe "
                + "FROM transaksi "
                + "WHERE DATE_FORMAT(tanggal, '%Y-%m') = ? "
                + "UNION ALL "
                + "SELECT p.id_pengeluaran AS no_ref, p.tanggal, p.total AS jumlah, "
                + "s.nama_stok AS kategori, 'Pengeluaran' AS tipe "
                + "FROM pengeluaran p "
                + "JOIN stok_bahan s ON p.id_stok = s.id_stok "
                + "WHERE DATE_FORMAT(p.tanggal, '%Y-%m') = ? "
                + "ORDER BY tanggal ASC";

        //buat formatter tanggal untuk tampilan
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        try {
            //buka koneksi ke database
            Connection conn = Koneksi.konek();

            PreparedStatement psGabungan = conn.prepareStatement(sqlGabungan);
            psGabungan.setString(1, periode);
            psGabungan.setString(2, periode);
            ResultSet rsGabungan = psGabungan.executeQuery();

            //iterasi untuk setiap baris hasil query gabungan
            while (rsGabungan.next()) {

                //ambil tanggal dan format
                String tanggal = sdf.format(rsGabungan.getTimestamp("tanggal"));

                //ambil nomor referensi (id_transaksi atau id_pengeluaran)
                String noRef = rsGabungan.getString("no_ref");

                //ambil kategori (Penjualan atau nama stok)
                String kategori = rsGabungan.getString("kategori");

                //ambil tipe (Pemasukan atau Pengeluaran)
                String tipe = rsGabungan.getString("tipe");

                //format jumlah dengan tanda positif/negatif sesuai tipe
                double total = rsGabungan.getDouble("jumlah");
                String jumlah;
                if ("Pemasukan".equals(tipe)) {
                    jumlah = "+" + FormatUang.format(total);
                } else {
                    jumlah = "-" + FormatUang.format(total);
                }

                //simpan data ke dalam array baris
                Object[] baris = {noRef, tanggal, kategori, tipe, jumlah};

                //tambahkan baris ke model tabel
                model.addRow(baris);
            }

        } catch (SQLException sQLException) {
            //tampilkan pesan error jika gagal mengambil data tabel
            JOptionPane.showMessageDialog(null, "gagal mengambil data laporan!");
        }

        //tampilkan model yang sudah diisi ke dalam tabel GUI
        tblLaporan.setModel(model);
        tblLaporan.setColumnWidths("70,100,50,50,50");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        hrader = new javax.swing.JPanel();
        lblLaporan = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        PeriodeBulan = new com.toedter.calendar.JMonthChooser();
        btnUnduh = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        headerBawah = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jPanel12 = new javax.swing.JPanel();
        lblpemasukkan = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        lblTotalPemasukan = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        jPanel13 = new javax.swing.JPanel();
        Jlabel = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        lblTotalPengeluaran = new javax.swing.JLabel();
        jPanel14 = new javax.swing.JPanel();
        jPanel15 = new javax.swing.JPanel();
        lblLaba = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        lblLabaBersih = new javax.swing.JLabel();
        main = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        lblRincian = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        btnhpsDetail = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblLaporan = new jtablecustom.JTableCustom();

        setBackground(new java.awt.Color(252, 249, 255));
        setBorder(javax.swing.BorderFactory.createEmptyBorder(15, 15, 15, 15));
        setMinimumSize(new java.awt.Dimension(1075, 639));
        setLayout(new java.awt.BorderLayout());

        hrader.setBackground(new java.awt.Color(252, 249, 255));
        hrader.setMinimumSize(new java.awt.Dimension(100, 250));
        hrader.setPreferredSize(new java.awt.Dimension(1045, 75));

        lblLaporan.setFont(new java.awt.Font("Plus Jakarta Sans", 1, 36)); // NOI18N
        lblLaporan.setText(" Laporan Pemasukan");
        lblLaporan.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        lblLaporan.setMinimumSize(new java.awt.Dimension(351, 75));
        lblLaporan.setPreferredSize(new java.awt.Dimension(351, 75));

        jPanel3.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(231, 189, 187), 1, true));
        jPanel3.setLayout(new java.awt.BorderLayout());

        PeriodeBulan.setDebugGraphicsOptions(javax.swing.DebugGraphics.NONE_OPTION);
        PeriodeBulan.setDoubleBuffered(false);
        PeriodeBulan.setFocusable(false);
        PeriodeBulan.setFont(new java.awt.Font("Plus Jakarta Sans", 0, 14)); // NOI18N
        PeriodeBulan.addHierarchyListener(this::PeriodeBulanHierarchyChanged);
        PeriodeBulan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                PeriodeBulanMouseClicked(evt);
            }
        });
        PeriodeBulan.addPropertyChangeListener(this::PeriodeBulanPropertyChange);
        jPanel3.add(PeriodeBulan, java.awt.BorderLayout.CENTER);

        btnUnduh.setBackground(new java.awt.Color(234, 88, 11));
        btnUnduh.setFont(new java.awt.Font("Plus Jakarta Sans SemiBold", 0, 16)); // NOI18N
        btnUnduh.setForeground(new java.awt.Color(255, 255, 255));
        btnUnduh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/posseblakratu/icon/btnUnduh.png"))); // NOI18N
        btnUnduh.setText("Unduh Laporan");
        btnUnduh.setBorderPainted(false);
        btnUnduh.setIconTextGap(8);
        btnUnduh.addActionListener(this::btnUnduhActionPerformed);

        javax.swing.GroupLayout hraderLayout = new javax.swing.GroupLayout(hrader);
        hrader.setLayout(hraderLayout);
        hraderLayout.setHorizontalGroup(
            hraderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(hraderLayout.createSequentialGroup()
                .addComponent(lblLaporan, javax.swing.GroupLayout.PREFERRED_SIZE, 397, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 303, Short.MAX_VALUE)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(22, 22, 22)
                .addComponent(btnUnduh)
                .addContainerGap())
        );
        hraderLayout.setVerticalGroup(
            hraderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(hraderLayout.createSequentialGroup()
                .addComponent(lblLaporan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(175, 175, 175))
            .addGroup(hraderLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(hraderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnUnduh, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        add(hrader, java.awt.BorderLayout.NORTH);

        jPanel1.setBackground(new java.awt.Color(252, 249, 255));
        jPanel1.setLayout(new java.awt.BorderLayout(0, 16));

        headerBawah.setBackground(new java.awt.Color(252, 249, 255));
        headerBawah.setPreferredSize(new java.awt.Dimension(0, 175));
        headerBawah.setLayout(new java.awt.GridLayout(1, 3, 15, 0));

        jPanel9.setBackground(new java.awt.Color(255, 255, 255));
        jPanel9.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(231, 189, 187)));
        jPanel9.setLayout(new java.awt.BorderLayout());

        jPanel12.setBackground(new java.awt.Color(255, 255, 255));
        jPanel12.setBorder(javax.swing.BorderFactory.createEmptyBorder(24, 24, 24, 24));

        lblpemasukkan.setFont(new java.awt.Font("Plus Jakarta Sans Medium", 0, 16)); // NOI18N
        lblpemasukkan.setText("Pemasukan");

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/posseblakratu/icon/Background.png"))); // NOI18N

        lblTotalPemasukan.setFont(new java.awt.Font("Plus Jakarta Sans SemiBold", 0, 24)); // NOI18N
        lblTotalPemasukan.setText("Rp 0");

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                .addComponent(lblpemasukkan, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 89, Short.MAX_VALUE)
                .addComponent(jLabel3))
            .addComponent(lblTotalPemasukan, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(lblpemasukkan))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 54, Short.MAX_VALUE)
                .addComponent(lblTotalPemasukan))
        );

        jPanel9.add(jPanel12, java.awt.BorderLayout.CENTER);

        headerBawah.add(jPanel9);

        jPanel10.setBackground(new java.awt.Color(255, 255, 255));
        jPanel10.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(231, 189, 187)));
        jPanel10.setLayout(new java.awt.BorderLayout());

        jPanel13.setBackground(new java.awt.Color(255, 255, 255));
        jPanel13.setBorder(javax.swing.BorderFactory.createEmptyBorder(24, 24, 24, 24));

        Jlabel.setFont(new java.awt.Font("Plus Jakarta Sans Medium", 0, 16)); // NOI18N
        Jlabel.setText("Pengeluaran");

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/posseblakratu/icon/bdgePengeluaran.png"))); // NOI18N

        lblTotalPengeluaran.setFont(new java.awt.Font("Plus Jakarta Sans SemiBold", 0, 24)); // NOI18N
        lblTotalPengeluaran.setText("Rp 0");

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel13Layout.createSequentialGroup()
                .addComponent(Jlabel, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 89, Short.MAX_VALUE)
                .addComponent(jLabel4))
            .addComponent(lblTotalPengeluaran, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4)
                    .addComponent(Jlabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 54, Short.MAX_VALUE)
                .addComponent(lblTotalPengeluaran))
        );

        jPanel10.add(jPanel13, java.awt.BorderLayout.CENTER);

        headerBawah.add(jPanel10);

        jPanel14.setBackground(new java.awt.Color(214, 4, 39));
        jPanel14.setLayout(new java.awt.BorderLayout());

        jPanel15.setBackground(new java.awt.Color(214, 4, 39));
        jPanel15.setBorder(javax.swing.BorderFactory.createEmptyBorder(24, 24, 24, 24));

        lblLaba.setFont(new java.awt.Font("Plus Jakarta Sans Medium", 0, 16)); // NOI18N
        lblLaba.setForeground(new java.awt.Color(255, 255, 255));
        lblLaba.setText("Laba Bersih");

        jLabel11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/posseblakratu/icon/bdgeDompet.png"))); // NOI18N

        lblLabaBersih.setFont(new java.awt.Font("Plus Jakarta Sans SemiBold", 0, 24)); // NOI18N
        lblLabaBersih.setForeground(new java.awt.Color(255, 255, 255));
        lblLabaBersih.setText("Rp 0");

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel15Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lblLabaBersih, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addComponent(lblLaba, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 10, Short.MAX_VALUE)
                        .addComponent(jLabel11)))
                .addGap(0, 0, 0))
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblLaba)
                    .addComponent(jLabel11))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 48, Short.MAX_VALUE)
                .addComponent(lblLabaBersih, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );

        jPanel14.add(jPanel15, java.awt.BorderLayout.CENTER);

        headerBawah.add(jPanel14);

        jPanel1.add(headerBawah, java.awt.BorderLayout.PAGE_START);

        main.setBackground(new java.awt.Color(255, 255, 255));
        main.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(231, 189, 187)));
        main.setLayout(new java.awt.BorderLayout());

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));
        jPanel6.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(231, 189, 187)), javax.swing.BorderFactory.createEmptyBorder(14, 25, 14, 25)));
        jPanel6.setPreferredSize(new java.awt.Dimension(1043, 65));
        jPanel6.setLayout(new java.awt.BorderLayout());

        lblRincian.setFont(new java.awt.Font("Plus Jakarta Sans SemiBold", 0, 24)); // NOI18N
        lblRincian.setText("Rincian Transaksi");
        jPanel6.add(lblRincian, java.awt.BorderLayout.LINE_START);

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setLayout(new java.awt.BorderLayout());

        btnhpsDetail.setFont(new java.awt.Font("Plus Jakarta Sans SemiBold", 0, 16)); // NOI18N
        btnhpsDetail.setForeground(new java.awt.Color(214, 4, 39));
        btnhpsDetail.setIcon(new javax.swing.ImageIcon(getClass().getResource("/posseblakratu/icon/icon hapus.png"))); // NOI18N
        btnhpsDetail.setText("Hapus Transaksi");
        btnhpsDetail.setBorderPainted(false);
        btnhpsDetail.setFocusable(false);
        btnhpsDetail.setIconTextGap(8);
        btnhpsDetail.addActionListener(this::btnhpsDetailActionPerformed);
        jPanel4.add(btnhpsDetail, java.awt.BorderLayout.LINE_END);

        jPanel6.add(jPanel4, java.awt.BorderLayout.CENTER);

        main.add(jPanel6, java.awt.BorderLayout.PAGE_START);

        jPanel2.setLayout(new java.awt.BorderLayout());

        tblLaporan.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"TRX0001", "03/07/2026", "Penjualan", "Pemasukan", "+Rp. 30.000"}
            },
            new String [] {
                "No Refrensi", "Tanggal", "Kategori", "Tipe", "Jumlah"
            }
        ));
        tblLaporan.setCellPaddingLeft(25);
        tblLaporan.setCellPaddingRight(25);
        tblLaporan.setCenterColumns("1,2,3,4");
        tblLaporan.setColumnWidths("70,100,50,50,50");
        tblLaporan.setFocusable(false);
        tblLaporan.setFont(new java.awt.Font("Plus Jakarta Sans", 0, 14)); // NOI18N
        tblLaporan.setHeaderPaddingLeft(25);
        tblLaporan.setHeaderPaddingRight(25);
        tblLaporan.setLeftColumns("0");
        tblLaporan.setRowSelectionAllowed(false);
        tblLaporan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblLaporanMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblLaporan);

        jPanel2.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        main.add(jPanel2, java.awt.BorderLayout.CENTER);

        jPanel1.add(main, java.awt.BorderLayout.CENTER);

        add(jPanel1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    //method untuk menulis bagian ringkasan laporan ke file CSV
    private void tulisRingkasanCSV(FileWriter fw, String periode) throws IOException {

        //tulis deklarasi separator agar Excel otomatis mengenali delimiter titik koma
        fw.write("sep=;\n");

        //--- BAGIAN 1: RINGKASAN LAPORAN ---
        fw.write("LAPORAN PEMASUKAN\n");
        fw.write("Periode:;" + periode + "\n");
        fw.write("\n");

        //tulis ringkasan keuangan
        fw.write("Total Pemasukan:;" + lblTotalPemasukan.getText() + "\n");
        fw.write("Total Pengeluaran:;" + lblTotalPengeluaran.getText() + "\n");
        fw.write("Laba Bersih:;" + lblLabaBersih.getText() + "\n");
        fw.write("\n");
    }

    //method untuk mengambil teks daftar topping dari satu item detail
    private String getDaftarTopping(Connection conn, String idDetail) {

        //variabel untuk menggabungkan semua topping menjadi satu teks
        String daftarTopping = "";

        //query topping dari item ini
        String sqlTopping = "SELECT p.nama_produk, tp.kuantitas "
                + "FROM detail_topping tp "
                + "JOIN produk p ON tp.id_produk = p.id_produk "
                + "WHERE tp.id_detail = ?";

        try {
            PreparedStatement psTopping = conn.prepareStatement(sqlTopping);
            psTopping.setString(1, idDetail);
            ResultSet rsTopping = psTopping.executeQuery();

            //iterasi untuk setiap topping
            while (rsTopping.next()) {

                //tambahkan pemisah jika bukan topping pertama
                if (!daftarTopping.isEmpty()) {
                    daftarTopping += " | ";
                }

                //tambahkan nama topping dan qty ke teks gabungan
                daftarTopping += rsTopping.getString("nama_produk")
                        + " x" + rsTopping.getInt("kuantitas");
            }

        } catch (SQLException sQLException) {
            //tampilkan pesan jika gagal mengambil data topping
            JOptionPane.showMessageDialog(null, sQLException.getMessage());
        }

        //jika tidak ada topping kembalikan tanda strip
        return daftarTopping.isEmpty() ? "-" : daftarTopping;
    }

    //method untuk menulis bagian rincian transaksi ke file CSV
    private void tulisRincianTransaksiCSV(FileWriter fw, String periode) throws IOException {

        //--- BAGIAN 2: RINCIAN TRANSAKSI ---
        fw.write("RINCIAN TRANSAKSI\n");

        //tulis header kolom — setiap baris adalah satu item produk
        fw.write("Tanggal;No Transaksi;Subtotal;Nama Diskon;Total Diskon;Total Bayar;Metode;Jumlah Bayar;Kembalian;"
                + "Produk;Level;Qty;Harga Satuan;Subtotal Produk;Topping\n");

        //query dengan JOIN transaksi + detail_transaksi + produk
        //setiap baris mewakili satu item produk dalam satu transaksi
        String sqlJoin = "SELECT "
                + "t.tanggal, t.id_transaksi, t.subtotal, "
                + "COALESCE(d.nama_diskon, '-') AS nama_diskon, " //menggunakan COALESCE mengganti nilai null menjadi "-"
                + "(t.subtotal - t.total_akhir) AS total_diskon, " //selisih subtotal dan total akhir sebagai nilai diskon
                + "t.total_akhir, t.metode, t.jumlah_bayar, t.kembalian, "
                + "p.nama_produk, dt.level, dt.kuantitas, dt.harga_satuan, dt.subtotal_produk, "
                + "dt.id_detail "
                + "FROM transaksi t "
                + "LEFT JOIN diskon d ON t.id_diskon = d.id_diskon "
                + "JOIN detail_transaksi dt ON t.id_transaksi = dt.id_transaksi "
                + "JOIN produk p ON dt.id_produk = p.id_produk "
                + "WHERE DATE_FORMAT(t.tanggal, '%Y-%m') = ? "
                + "ORDER BY t.tanggal ASC, dt.id_detail ASC";

        //buat formatter tanggal untuk tampilan
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        try {
            //buka koneksi ke database
            Connection conn = Koneksi.konek();

            PreparedStatement psJoin = conn.prepareStatement(sqlJoin);
            psJoin.setString(1, periode);
            ResultSet rsJoin = psJoin.executeQuery();

            //iterasi untuk setiap baris hasil join
            while (rsJoin.next()) {

                //ambil dan format tanggal transaksi
                String tanggal = sdf.format(rsJoin.getTimestamp("tanggal"));

                //ambil id transaksi
                String idTrx = rsJoin.getString("id_transaksi");

                //ambil subtotal transaksi
                double subtotal = rsJoin.getDouble("subtotal");

                //ambil nama diskon
                String namaDiskon = rsJoin.getString("nama_diskon");

                //ambil total nilai diskon yang dipotong
                double totalDiskon = rsJoin.getDouble("total_diskon");

                //ambil total akhir setelah diskon
                double totalAkhir = rsJoin.getDouble("total_akhir");

                //ambil metode pembayaran
                String metode = rsJoin.getString("metode");

                //ambil jumlah yang dibayarkan pelanggan
                double jumlahBayar = rsJoin.getDouble("jumlah_bayar");

                //ambil kembalian
                double kembalian = rsJoin.getDouble("kembalian");

                //ambil nama produk
                String namaProduk = rsJoin.getString("nama_produk");

                //ambil level pedas (null jika tidak ada)
                String level = rsJoin.getString("level") != null ? rsJoin.getString("level") : "-";

                //ambil kuantitas item
                int qty = rsJoin.getInt("kuantitas");

                //ambil harga satuan item
                double hargaSatuan = rsJoin.getDouble("harga_satuan");

                //ambil subtotal produk
                double subtotalProduk = rsJoin.getDouble("subtotal_produk");

                //ambil daftar topping dari item ini menggunakan method terpisah
                String daftarTopping = getDaftarTopping(conn, rsJoin.getString("id_detail"));

                //tulis satu baris ke CSV dengan semua kolom
                fw.write(tanggal + ";"
                        + idTrx + ";"
                        + FormatUang.format(subtotal) + ";"
                        + namaDiskon + ";"
                        + FormatUang.format(totalDiskon) + ";"
                        + FormatUang.format(totalAkhir) + ";"
                        + metode + ";"
                        + FormatUang.format(jumlahBayar) + ";"
                        + FormatUang.format(kembalian) + ";"
                        + namaProduk + ";"
                        + level + ";"
                        + qty + ";"
                        + FormatUang.format(hargaSatuan) + ";"
                        + FormatUang.format(subtotalProduk) + ";"
                        + daftarTopping + "\n");
            }

        } catch (SQLException sQLException) {
            //tampilkan pesan jika gagal mengambil data transaksi
            JOptionPane.showMessageDialog(null, sQLException.getMessage());
        }
    }

    //method untuk menulis bagian rincian pengeluaran ke file CSV
    private void tulisRincianPengeluaranCSV(FileWriter fw, String periode) throws IOException {

        //--- BAGIAN 3: RINCIAN PENGELUARAN ---
        fw.write("\n");
        fw.write("RINCIAN PENGELUARAN\n");
        fw.write("Tanggal;No Pengeluaran;Nama Stok;Jumlah;Satuan;Harga Satuan;Total\n");

        //query rincian pengeluaran
        String sqlPengeluaran = "SELECT p.tanggal, p.id_pengeluaran, s.nama_stok, "
                + "p.jumlah, s.satuan, p.harga_satuan, p.total "
                + "FROM pengeluaran p "
                + "JOIN stok_bahan s ON p.id_stok = s.id_stok "
                + "WHERE DATE_FORMAT(p.tanggal, '%Y-%m') = ? "
                + "ORDER BY p.tanggal ASC";

        //buat formatter tanggal untuk tampilan
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        try {
            //buka koneksi ke database
            Connection conn = Koneksi.konek();

            PreparedStatement psPengeluaran = conn.prepareStatement(sqlPengeluaran);
            psPengeluaran.setString(1, periode);
            ResultSet rsPengeluaran = psPengeluaran.executeQuery();

            //iterasi untuk setiap baris pengeluaran
            while (rsPengeluaran.next()) {

                //ambil dan format tanggal pengeluaran
                String tanggal = sdf.format(rsPengeluaran.getTimestamp("tanggal"));

                //ambil id pengeluaran
                String idPengeluaran = rsPengeluaran.getString("id_pengeluaran");

                //ambil nama stok
                String namaStok = rsPengeluaran.getString("nama_stok");

                //ambil jumlah
                double jumlah = rsPengeluaran.getDouble("jumlah");

                //ambil satuan
                String satuan = rsPengeluaran.getString("satuan");

                //ambil harga satuan
                double hargaSatuan = rsPengeluaran.getDouble("harga_satuan");

                //ambil total pengeluaran
                double total = rsPengeluaran.getDouble("total");

                //tulis satu baris pengeluaran ke CSV
                fw.write(tanggal + ";"
                        + idPengeluaran + ";"
                        + namaStok + ";"
                        + jumlah + ";"
                        + satuan + ";"
                        + FormatUang.format(hargaSatuan) + ";"
                        + FormatUang.format(total) + "\n");
            }

        } catch (SQLException sQLException) {
            //tampilkan pesan jika gagal mengambil data pengeluaran
            JOptionPane.showMessageDialog(null, sQLException.getMessage());
        }
    }


    private void btnUnduhActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUnduhActionPerformed

        //ambil bulan dan tahun yang sedang aktif di komponen PeriodeBulan
        int bulan = PeriodeBulan.getMonth() + 1;
        int tahun = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR);

        //format periode menjadi string yyyy-MM
        String periode = String.format("%04d-%02d", tahun, bulan);

        //buat objek JFileChooser untuk memilih lokasi penyimpanan file
        JFileChooser chooser = new JFileChooser();

        //atur nama file default berdasarkan periode yang aktif
        chooser.setSelectedFile(new File("Laporan_" + periode + ".csv"));

        //tampilkan dialog simpan file
        int result = chooser.showSaveDialog(null);

        //cek apakah pengguna menekan tombol "Save"
        if (result != JFileChooser.APPROVE_OPTION) {
            return;
        }

        //ambil file tujuan yang dipilih pengguna
        File file = chooser.getSelectedFile();

        try {
            //buat objek FileWriter untuk menulis ke file CSV
            FileWriter fw = new FileWriter(file);

            //tulis bagian ringkasan laporan ke CSV
            tulisRingkasanCSV(fw, periode);

            //tulis bagian rincian transaksi ke CSV
            tulisRincianTransaksiCSV(fw, periode);

            //tulis bagian rincian pengeluaran ke CSV
            tulisRincianPengeluaranCSV(fw, periode);

            //tutup file writer
            fw.close();

            //tampilkan pesan bahwa file berhasil disimpan
            JOptionPane.showMessageDialog(null, "Laporan berhasil diunduh!");

        } catch (IOException e) {
            //tampilkan pesan jika gagal menulis file
            JOptionPane.showMessageDialog(null, e.getMessage());
        }

    }//GEN-LAST:event_btnUnduhActionPerformed

    private void PeriodeBulanMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_PeriodeBulanMouseClicked


    }//GEN-LAST:event_PeriodeBulanMouseClicked

    private void PeriodeBulanHierarchyChanged(java.awt.event.HierarchyEvent evt) {//GEN-FIRST:event_PeriodeBulanHierarchyChanged

    }//GEN-LAST:event_PeriodeBulanHierarchyChanged

    private void PeriodeBulanPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_PeriodeBulanPropertyChange
        // TODO add your handling code here:
        loadLaporan();
    }//GEN-LAST:event_PeriodeBulanPropertyChange


    private void btnhpsDetailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnhpsDetailActionPerformed

        if ("Pemasukan".equals(selectedTipe)) {
            //menampilkan dialog konfirmasi sebelum menghapus data
            int konfirmasi = JOptionPane.showConfirmDialog(
                    null,
                    "Yakin ingin menghapus transaksi pemasukan ini?",
                    "Konfirmasi",
                    JOptionPane.YES_NO_OPTION);

            //jika pengguna memilih selain tombol "Yes", proses dibatalkan
            if (konfirmasi != JOptionPane.YES_OPTION) {
                return;
            }

            //query SQL untuk menghapus data pengguna berdasarkan ID
            String sql = "DELETE FROM transaksi  WHERE id_transaksi=?";

            try {

                //membuat koneksi ke database
                Connection conn = Koneksi.konek();

                //menyiapkan query SQL
                PreparedStatement ps = conn.prepareStatement(sql);

                //mengisi parameter query dengan ID pengguna yang akan dihapus
                ps.setString(1, selectedIdTransaksi);

                //menjalankan perintah DELETE
                ps.execute();

                //menampilkan pesan bahwa data berhasil dihapus
                JOptionPane.showMessageDialog(null, "Data berhasil dihapus!");

                //menangkap error yang terjadi saat menjalankan operasi database
            } catch (SQLException e) {

                //menampilkan pesan bahwa proses penghapusan data gagal
                JOptionPane.showMessageDialog(null, "Data gagal dihapus!" + e.getMessage());

            }

            loadLaporan();

        } else {
            //menampilkan dialog konfirmasi sebelum menghapus data
            int konfirmasi = JOptionPane.showConfirmDialog(
                    null,
                    "Yakin ingin menghapus transaksi pengeluaran ini?",
                    "Konfirmasi",
                    JOptionPane.YES_NO_OPTION);

            //jika pengguna memilih selain tombol "Yes", proses dibatalkan
            if (konfirmasi != JOptionPane.YES_OPTION) {
                return;
            }

            //query SQL untuk menghapus data pengguna berdasarkan ID
            String sql = "DELETE FROM pengeluaran WHERE id_pengeluaran=?";

            try {

                //membuat koneksi ke database
                Connection conn = Koneksi.konek();

                //menyiapkan query SQL
                PreparedStatement ps = conn.prepareStatement(sql);

                //mengisi parameter query dengan ID yang akan dihapus
                ps.setString(1, selectedIdTransaksi);

                //menjalankan perintah DELETE
                ps.execute();

                //menampilkan pesan bahwa data berhasil dihapus
                JOptionPane.showMessageDialog(null, "Data berhasil dihapus!");

                //menangkap error yang terjadi saat menjalankan operasi database
            } catch (SQLException e) {

                //menampilkan pesan bahwa proses penghapusan data gagal
                JOptionPane.showMessageDialog(null, "Data gagal dihapus!" + e.getMessage());

            }

            loadLaporan();
        }

    }//GEN-LAST:event_btnhpsDetailActionPerformed

    private void tblLaporanMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblLaporanMouseClicked

        //ambil indeks baris yang diklik
        int baris = tblLaporan.rowAtPoint(evt.getPoint());

        //jika baris valid simpan id dan tipe baris yang dipilih
        if (baris >= 0) {

            //menandai baris agar ikut terpilih
            tblLaporan.setRowSelectionInterval(baris, baris);

            //ambil nilai kolom no referensi sebagai id transaksi (kolom 0)
            selectedIdTransaksi = tblLaporan.getValueAt(baris, 0).toString();

            //ambil nilai kolom tipe untuk membedakan pemasukan dan pengeluaran (kolom 3)
            selectedTipe = tblLaporan.getValueAt(baris, 3).toString();

        }
    }//GEN-LAST:event_tblLaporanMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel Jlabel;
    private com.toedter.calendar.JMonthChooser PeriodeBulan;
    private javax.swing.JButton btnUnduh;
    private javax.swing.JButton btnhpsDetail;
    private javax.swing.JPanel headerBawah;
    private javax.swing.JPanel hrader;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblLaba;
    private javax.swing.JLabel lblLabaBersih;
    private javax.swing.JLabel lblLaporan;
    private javax.swing.JLabel lblRincian;
    private javax.swing.JLabel lblTotalPemasukan;
    private javax.swing.JLabel lblTotalPengeluaran;
    private javax.swing.JLabel lblpemasukkan;
    private javax.swing.JPanel main;
    private jtablecustom.JTableCustom tblLaporan;
    // End of variables declaration//GEN-END:variables
}
