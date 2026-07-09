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
import posseblakratu.component.PopupDetail;

/**
 *
 * @author Al
 */
public class PanelLaporan extends javax.swing.JPanel {

    //menyimpan id transaksi yang dipilih dari tabel laporan
    private String selectedIdTransaksi = null;

    //menyimpan tipe baris yang dipilih dari tabel laporan
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


        //memanggil method untuk menampilkan laporan sesuai bulan yang aktif saat ini
        loadLaporan();
    }

    void panelLengkung(JPanel p) {

        p.setBorder(new FlatLineBorder(
                new Insets(3, 3, 3, 3),
                Color.decode("#E7BDBB"),
                1f,
                10));

    }


    //membuat method untuk memformat persentase perubahan vs bulan lalu
    private String formatPersen(double bulanIni, double bulanLalu) {

        //jika bulan lalu tidak ada data, persentase dianggap 0
        if (bulanLalu == 0) {
            return "+0% vs bln lalu";
        }

        //menghitung selisih persentase perubahan
        double persen = ((bulanIni - bulanLalu) / bulanLalu) * 100;

        //menentukan tanda positif atau negatif
        String tanda = persen >= 0 ? "+" : "";

        //mengembalikan string persentase
        return tanda + String.format("%.0f", persen) + "% vs bln lalu";
    }


    //membuat method untuk load laporan berdasarkan bulan yang dipilih
    public void loadLaporan() {
        
        //mengambil tahun saat ini
        int tahun = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR);

        //mengambil bulan yang dipilih dari komponen PeriodeBulan (0 = Januari)
        int bulan = PeriodeBulan.getMonth() + 1;

        //memformat bulan dan tahun menjadi string dua digit
        String periodeIni = String.format("%04d-%02d", tahun, bulan);

        //menghitung bulan sebelumnya untuk perbandingan
        int tahunLalu = tahun;
        int bulanLalu = bulan - 1;

        //jika bulan januari, bulan lalu adalah desember tahun sebelumnya
        if (bulanLalu == 0) {
            bulanLalu = 12;
            tahunLalu = tahun - 1;
        }

        //memformat periode bulan lalu
        String periodeLalu = String.format("%04d-%02d", tahunLalu, bulanLalu);

        try {
            //membuka koneksi ke database
            Connection conn = Koneksi.konek();

            //query untuk mengambil total pemasukan bulan ini
            String sqlPemasukanIni = "SELECT COALESCE(SUM(total_akhir), 0) AS total FROM transaksi "
                    + "WHERE DATE_FORMAT(tanggal, '%Y-%m') = ?";

            //menyiapkan statement pemasukan bulan ini
            PreparedStatement psPemasukanIni = conn.prepareStatement(sqlPemasukanIni);
            psPemasukanIni.setString(1, periodeIni);
            ResultSet rsPemasukanIni = psPemasukanIni.executeQuery();

            //mengambil total pemasukan bulan ini
            double totalPemasukanIni = 0;
            if (rsPemasukanIni.next()) {
                totalPemasukanIni = rsPemasukanIni.getDouble("total");
            }

            //query untuk mengambil total pemasukan bulan lalu
            PreparedStatement psPemasukanLalu = conn.prepareStatement(sqlPemasukanIni);
            psPemasukanLalu.setString(1, periodeLalu);
            ResultSet rsPemasukanLalu = psPemasukanLalu.executeQuery();

            //mengambil total pemasukan bulan lalu
            double totalPemasukanLalu = 0;
            if (rsPemasukanLalu.next()) {
                totalPemasukanLalu = rsPemasukanLalu.getDouble("total");
            }

            //pengeluaran diambil dari tabel pengeluaran
            String sqlPengeluaranIni = "SELECT COALESCE(SUM(total), 0) AS total FROM pengeluaran "
                    + "WHERE DATE_FORMAT(tanggal, '%Y-%m') = ?";

            //menyiapkan statement pengeluaran bulan ini
            PreparedStatement psPengeluaranIni = conn.prepareStatement(sqlPengeluaranIni);
            psPengeluaranIni.setString(1, periodeIni);
            ResultSet rsPengeluaranIni = psPengeluaranIni.executeQuery();

            //mengambil total pengeluaran bulan ini
            double totalPengeluaranIni = 0;
            if (rsPengeluaranIni.next()) {
                totalPengeluaranIni = rsPengeluaranIni.getDouble("total");
            }

            //query untuk mengambil total pengeluaran bulan lalu
            PreparedStatement psPengeluaranLalu = conn.prepareStatement(sqlPengeluaranIni);
            psPengeluaranLalu.setString(1, periodeLalu);
            ResultSet rsPengeluaranLalu = psPengeluaranLalu.executeQuery();

            //mengambil total pengeluaran bulan lalu
            double totalPengeluaranLalu = 0;
            if (rsPengeluaranLalu.next()) {
                totalPengeluaranLalu = rsPengeluaranLalu.getDouble("total");
            }

            //menghitung laba bersih bulan ini
            double labaBersih = totalPemasukanIni - totalPengeluaranIni;

            //menghitung margin keuntungan
            double margin = totalPemasukanIni > 0 ? (labaBersih / totalPemasukanIni) * 100 : 0;

            //menampilkan label nama kartu pemasukan
            lblpemasukkan.setText("Pemasukan");

            //menampilkan total pemasukan ke label
            lblTotalPemasukan.setText(FormatUang.format(totalPemasukanIni));

            //menampilkan persentase perubahan pemasukan vs bulan lalu
            lblPersentasePemasukan.setText(formatPersen(totalPemasukanIni, totalPemasukanLalu));

            //menampilkan total pengeluaran ke label
            lblTotalPengeluaran.setText(FormatUang.format(totalPengeluaranIni));

            //menampilkan persentase perubahan pengeluaran vs bulan lalu
            lblPeresentasePengeluaran.setText(formatPersen(totalPengeluaranIni, totalPengeluaranLalu));

            //menampilkan laba bersih ke label
            lblLabaBersih.setText(FormatUang.format(labaBersih));

            //menampilkan margin keuntungan ke label
            lblMargin.setText(String.format("%.0f%%", margin));

            //memanggil method untuk memuat tabel rincian transaksi
            loadTabelLaporan(conn, periodeIni);

        } catch (SQLException sQLException) {
            //menampilkan pesan error jika gagal mengambil data
            JOptionPane.showMessageDialog(null, sQLException.getMessage());
        }
    }


    //membuat method untuk memuat tabel rincian transaksi berdasarkan periode
    void loadTabelLaporan(Connection conn, String periode) {

        //membuat model tabel baru
        DefaultTableModel model = new DefaultTableModel();

        //menambahkan kolom ke dalam model tabel
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

        try {
            //membuat formatter tanggal untuk tampilan
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

            //mengambil semua data pemasukan dan pengeluaran sekaligus, diurutkan berdasarkan waktu
            PreparedStatement psGabungan = conn.prepareStatement(sqlGabungan);
            psGabungan.setString(1, periode);
            psGabungan.setString(2, periode);
            ResultSet rsGabungan = psGabungan.executeQuery();

            //melakukan iterasi untuk setiap baris hasil query gabungan
            while (rsGabungan.next()) {

                //mengambil tanggal dan memformatnya
                String tanggal = sdf.format(rsGabungan.getTimestamp("tanggal"));

                //mengambil nomor referensi (id_transaksi atau id_pengeluaran)
                String noRef = rsGabungan.getString("no_ref");

                //mengambil kategori (Penjualan atau nama stok)
                String kategori = rsGabungan.getString("kategori");

                //mengambil tipe (Pemasukan atau Pengeluaran)
                String tipe = rsGabungan.getString("tipe");

                //memformat jumlah dengan tanda positif/negatif sesuai tipe
                double total = rsGabungan.getDouble("jumlah");
                String jumlah;
                if ("Pemasukan".equals(tipe)) {
                    jumlah = "+" + FormatUang.format(total);
                } else {
                    jumlah = "-" + FormatUang.format(total);
                }

                //menyimpan data ke dalam array baris
                Object[] baris = {noRef, tanggal, kategori, tipe, jumlah};

                //menambahkan baris ke model tabel
                model.addRow(baris);
            }

        } catch (SQLException sQLException) {
            //menampilkan pesan error jika gagal mengambil data tabel
            JOptionPane.showMessageDialog(null, "gagal mengambil data laporan!\n" + sQLException.getMessage());
        }

        //menampilkan model yang sudah diisi ke dalam tabel GUI
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
        lblPersentasePemasukan = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        jPanel13 = new javax.swing.JPanel();
        Jlabel = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        lblTotalPengeluaran = new javax.swing.JLabel();
        lblPeresentasePengeluaran = new javax.swing.JLabel();
        jPanel14 = new javax.swing.JPanel();
        jPanel15 = new javax.swing.JPanel();
        lblLaba = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        lblLabaBersih = new javax.swing.JLabel();
        JLabel111 = new javax.swing.JLabel();
        lblMargin = new javax.swing.JLabel();
        main = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        lblRincian = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        btnLihatDetail = new javax.swing.JButton();
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
        lblpemasukkan.setText("JLabel");

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/posseblakratu/icon/Background.png"))); // NOI18N

        lblTotalPemasukan.setFont(new java.awt.Font("Plus Jakarta Sans SemiBold", 0, 24)); // NOI18N
        lblTotalPemasukan.setText("Rp 0");

        lblPersentasePemasukan.setFont(new java.awt.Font("Plus Jakarta Sans", 1, 14)); // NOI18N
        lblPersentasePemasukan.setForeground(new java.awt.Color(19, 115, 51));
        lblPersentasePemasukan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/posseblakratu/icon/IconPemasukan2.png"))); // NOI18N
        lblPersentasePemasukan.setText("+0% vs bln lalu");

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                .addComponent(lblpemasukkan, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel3))
            .addComponent(lblPersentasePemasukan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(lblTotalPemasukan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(lblpemasukkan))
                .addGap(0, 0, 0)
                .addComponent(lblTotalPemasukan)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblPersentasePemasukan))
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

        lblPeresentasePengeluaran.setFont(new java.awt.Font("Plus Jakarta Sans", 1, 14)); // NOI18N
        lblPeresentasePengeluaran.setForeground(new java.awt.Color(214, 4, 39));
        lblPeresentasePengeluaran.setIcon(new javax.swing.ImageIcon(getClass().getResource("/posseblakratu/icon/IconPengeluaran.png"))); // NOI18N
        lblPeresentasePengeluaran.setText("+0% vs bln lalu");

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel13Layout.createSequentialGroup()
                .addComponent(Jlabel, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel4))
            .addComponent(lblPeresentasePengeluaran, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(lblTotalPengeluaran, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4)
                    .addComponent(Jlabel))
                .addGap(0, 0, 0)
                .addComponent(lblTotalPengeluaran)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblPeresentasePengeluaran))
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

        JLabel111.setFont(new java.awt.Font("Plus Jakarta Sans", 0, 14)); // NOI18N
        JLabel111.setForeground(new java.awt.Color(255, 255, 255));
        JLabel111.setText("Margin Keuntungan :");

        lblMargin.setFont(new java.awt.Font("Plus Jakarta Sans", 1, 14)); // NOI18N
        lblMargin.setForeground(new java.awt.Color(255, 255, 255));
        lblMargin.setText("0%");

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
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel11))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel15Layout.createSequentialGroup()
                        .addComponent(JLabel111, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(lblMargin, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(0, 0, 0))
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblLaba)
                    .addComponent(jLabel11))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblLabaBersih)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(JLabel111)
                    .addComponent(lblMargin))
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

        btnLihatDetail.setBackground(new java.awt.Color(234, 88, 11));
        btnLihatDetail.setFont(new java.awt.Font("Plus Jakarta Sans SemiBold", 0, 16)); // NOI18N
        btnLihatDetail.setForeground(new java.awt.Color(255, 255, 255));
        btnLihatDetail.setIcon(new javax.swing.ImageIcon(getClass().getResource("/posseblakratu/icon/Fa7SolidEye (1).png"))); // NOI18N
        btnLihatDetail.setText("Lihat Detail Transaksi");
        btnLihatDetail.setBorderPainted(false);
        btnLihatDetail.setIconTextGap(8);
        btnLihatDetail.addActionListener(this::btnLihatDetailActionPerformed);
        jPanel4.add(btnLihatDetail, java.awt.BorderLayout.LINE_END);

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

    private void btnUnduhActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUnduhActionPerformed

        //mengambil bulan dan tahun yang sedang aktif di komponen PeriodeBulan
        int bulan = PeriodeBulan.getMonth() + 1;
        int tahun = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR);

        //memformat periode menjadi string yyyy-MM
        String periode = String.format("%04d-%02d", tahun, bulan);

        //membuat objek JFileChooser untuk memilih lokasi penyimpanan file
        JFileChooser chooser = new JFileChooser();

        //mengatur nama file default berdasarkan periode yang aktif
        chooser.setSelectedFile(new File("Laporan_" + periode + ".csv"));

        //menampilkan dialog simpan file
        int result = chooser.showSaveDialog(null);

        //mengecek apakah pengguna menekan tombol "Save"
        if (result != JFileChooser.APPROVE_OPTION) {
            return;
        }

        //mengambil file tujuan yang dipilih pengguna
        File file = chooser.getSelectedFile();

        try {
            //membuka koneksi ke database
            Connection conn = Koneksi.konek();

            //membuat objek FileWriter untuk menulis ke file CSV
            FileWriter fw = new FileWriter(file);

            //menulis deklarasi separator agar Excel otomatis mengenali delimiter titik koma
            fw.write("sep=;\n");

            //--- BAGIAN 1: RINGKASAN LAPORAN ---
            fw.write("LAPORAN PEMASUKAN\n");
            fw.write("Periode:;" + periode + "\n");
            fw.write("\n");

            //menulis ringkasan keuangan
            fw.write("Total Pemasukan:;" + lblTotalPemasukan.getText() + "\n");
            fw.write("Total Pengeluaran:;" + lblTotalPengeluaran.getText() + "\n");
            fw.write("Laba Bersih:;" + lblLabaBersih.getText() + "\n");
            fw.write("Margin Keuntungan:;" + lblMargin.getText() + "\n");
            fw.write("\n");

            //--- BAGIAN 2: RINCIAN TRANSAKSI ---
            fw.write("RINCIAN TRANSAKSI\n");

            //menulis header kolom — setiap baris adalah satu item produk
            fw.write("Tanggal;No Transaksi;Subtotal;Diskon;Total Bayar;Metode;Jumlah Bayar;Kembalian;"
                    + "Produk;Level;Qty;Harga Satuan;Subtotal Produk;Topping\n");

            //query dengan JOIN transaksi + detail_transaksi + produk
            //setiap baris mewakili satu item produk dalam satu transaksi
            String sqlJoin = "SELECT "
                    + "t.tanggal, t.id_transaksi, t.subtotal, "
                    + "COALESCE(d.nama_diskon, '-') AS nama_diskon, " //menggunakan COALESCE mengganti nilai null menjadi "-"
                    + "t.total_akhir, t.metode, t.jumlah_bayar, t.kembalian, "
                    + "p.nama_produk, dt.level, dt.kuantitas, dt.harga_satuan, dt.subtotal_produk, "
                    + "dt.id_detail "
                    + "FROM transaksi t "
                    + "LEFT JOIN diskon d ON t.id_diskon = d.id_diskon "
                    + "JOIN detail_transaksi dt ON t.id_transaksi = dt.id_transaksi "
                    + "JOIN produk p ON dt.id_produk = p.id_produk "
                    + "WHERE DATE_FORMAT(t.tanggal, '%Y-%m') = ? "
                    + "ORDER BY t.tanggal ASC, dt.id_detail ASC";

            //menyiapkan statement query join
            PreparedStatement psJoin = conn.prepareStatement(sqlJoin);

            //mengisi parameter periode
            psJoin.setString(1, periode);

            //menjalankan query join
            ResultSet rsJoin = psJoin.executeQuery();

            //membuat formatter tanggal untuk tampilan
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

            //melakukan iterasi untuk setiap baris hasil join
            while (rsJoin.next()) {

                //mengambil dan memformat tanggal transaksi
                String tanggal = sdf.format(rsJoin.getTimestamp("tanggal"));

                //mengambil id transaksi
                String idTrx = rsJoin.getString("id_transaksi");

                //mengambil subtotal transaksi
                double subtotal = rsJoin.getDouble("subtotal");

                //mengambil nama diskon
                String namaDiskon = rsJoin.getString("nama_diskon");

                //mengambil total akhir setelah diskon
                double totalAkhir = rsJoin.getDouble("total_akhir");

                //mengambil metode pembayaran
                String metode = rsJoin.getString("metode");

                //mengambil jumlah yang dibayarkan pelanggan
                double jumlahBayar = rsJoin.getDouble("jumlah_bayar");

                //mengambil kembalian
                double kembalian = rsJoin.getDouble("kembalian");

                //mengambil nama produk
                String namaProduk = rsJoin.getString("nama_produk");

                //mengambil level pedas (null jika tidak ada)
                String level = rsJoin.getString("level") != null ? rsJoin.getString("level") : "-";

                //mengambil kuantitas item
                int qty = rsJoin.getInt("kuantitas");

                //mengambil harga satuan item
                double hargaSatuan = rsJoin.getDouble("harga_satuan");

                //mengambil subtotal produk
                double subtotalProduk = rsJoin.getDouble("subtotal_produk");

                //mengambil id detail untuk query topping
                String idDetail = rsJoin.getString("id_detail");

                //query untuk mengambil topping dari item ini
                String sqlTopping = "SELECT p.nama_produk, tp.kuantitas "
                        + "FROM detail_topping tp "
                        + "JOIN produk p ON tp.id_produk = p.id_produk "
                        + "WHERE tp.id_detail = ?";

                //menyiapkan statement query topping
                PreparedStatement psTopping = conn.prepareStatement(sqlTopping);

                //mengisi parameter id detail
                psTopping.setString(1, idDetail);

                //menjalankan query topping
                ResultSet rsTopping = psTopping.executeQuery();

                //variabel untuk menggabungkan semua topping menjadi satu teks
                String daftarTopping = "";

                //melakukan iterasi untuk setiap topping
                while (rsTopping.next()) {

                    //menambahkan pemisah jika bukan topping pertama
                    if (!daftarTopping.isEmpty()) {
                        daftarTopping += " | ";
                    }

                    //menambahkan nama topping dan qty ke teks gabungan
                    daftarTopping += rsTopping.getString("nama_produk")
                            + " x" + rsTopping.getInt("kuantitas");
                }

                //jika tidak ada topping tampilkan tanda strip
                if (daftarTopping.isEmpty()) {
                    daftarTopping = "-";
                }

                //menutup result set dan statement topping
                rsTopping.close();
                psTopping.close();

                //menulis satu baris ke CSV dengan semua kolom
                fw.write(tanggal + ";"
                        + idTrx + ";"
                        + FormatUang.format(subtotal) + ";"
                        + namaDiskon + ";"
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

            //menutup result set dan statement join
            rsJoin.close();
            psJoin.close();

            //--- BAGIAN 3: RINCIAN PENGELUARAN ---
            fw.write("\n");
            fw.write("RINCIAN PENGELUARAN\n");
            fw.write("Tanggal;No Pengeluaran;Nama Stok;Jumlah;Satuan;Harga Satuan;Total\n");

            //query untuk mengambil rincian pengeluaran
            String sqlPengeluaran = "SELECT p.tanggal, p.id_pengeluaran, s.nama_stok, "
                    + "p.jumlah, s.satuan, p.harga_satuan, p.total "
                    + "FROM pengeluaran p "
                    + "JOIN stok_bahan s ON p.id_stok = s.id_stok "
                    + "WHERE DATE_FORMAT(p.tanggal, '%Y-%m') = ? "
                    + "ORDER BY p.tanggal ASC";

            //menyiapkan statement query pengeluaran
            PreparedStatement psPengeluaran = conn.prepareStatement(sqlPengeluaran);
            psPengeluaran.setString(1, periode);
            ResultSet rsPengeluaran = psPengeluaran.executeQuery();

            //melakukan iterasi untuk setiap baris pengeluaran
            while (rsPengeluaran.next()) {

                //mengambil dan memformat tanggal pengeluaran
                String tanggal = sdf.format(rsPengeluaran.getTimestamp("tanggal"));

                //mengambil id pengeluaran
                String idPengeluaran = rsPengeluaran.getString("id_pengeluaran");

                //mengambil nama stok
                String namaStok = rsPengeluaran.getString("nama_stok");

                //mengambil jumlah
                double jumlah = rsPengeluaran.getDouble("jumlah");

                //mengambil satuan
                String satuan = rsPengeluaran.getString("satuan");

                //mengambil harga satuan
                double hargaSatuan = rsPengeluaran.getDouble("harga_satuan");

                //mengambil total pengeluaran
                double total = rsPengeluaran.getDouble("total");

                //menulis satu baris pengeluaran ke CSV
                fw.write(tanggal + ";"
                        + idPengeluaran + ";"
                        + namaStok + ";"
                        + jumlah + ";"
                        + satuan + ";"
                        + FormatUang.format(hargaSatuan) + ";"
                        + FormatUang.format(total) + "\n");
            }

            //menutup result set dan statement pengeluaran
            rsPengeluaran.close();
            psPengeluaran.close();

            //menutup file writer
            fw.close();

            //menampilkan pesan bahwa file berhasil disimpan
            JOptionPane.showMessageDialog(null, "Laporan berhasil diunduh!");

        } catch (IOException e) {
            //menampilkan pesan jika gagal menulis file
            JOptionPane.showMessageDialog(null, e.getMessage());

        } catch (SQLException e) {
            //menampilkan pesan jika gagal mengambil data dari database
            JOptionPane.showMessageDialog(null, e.getMessage());
        }

    }//GEN-LAST:event_btnUnduhActionPerformed

    private void tblLaporanMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblLaporanMouseClicked

        //mengambil indeks baris yang diklik
        int baris = tblLaporan.rowAtPoint(evt.getPoint());

        //jika baris valid simpan id dan tipe baris yang dipilih
        if (baris >= 0) {

            //memilih baris secara programatik agar ter-highlight
            tblLaporan.setRowSelectionInterval(baris, baris);

            //mengambil nilai kolom no referensi sebagai id transaksi
            selectedIdTransaksi = tblLaporan.getValueAt(baris, 1).toString();

            //mengambil nilai kolom tipe untuk membedakan pemasukan dan pengeluaran
            selectedTipe = tblLaporan.getValueAt(baris, 3).toString();

        }

    }//GEN-LAST:event_tblLaporanMouseClicked

    private void PeriodeBulanMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_PeriodeBulanMouseClicked

        

    }//GEN-LAST:event_PeriodeBulanMouseClicked

    private void PeriodeBulanHierarchyChanged(java.awt.event.HierarchyEvent evt) {//GEN-FIRST:event_PeriodeBulanHierarchyChanged
        
    }//GEN-LAST:event_PeriodeBulanHierarchyChanged

    private void PeriodeBulanPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_PeriodeBulanPropertyChange
        // TODO add your handling code here:
        loadLaporan();
    }//GEN-LAST:event_PeriodeBulanPropertyChange

    private void btnLihatDetailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLihatDetailActionPerformed

        //menampilkan pesan jika belum ada baris yang dipilih
        if (selectedIdTransaksi == null || selectedTipe == null) {
            JOptionPane.showMessageDialog(null, "Pilih baris transaksi terlebih dahulu.");
            return;
        }

        //menampilkan pesan jika baris yang dipilih bukan pemasukan
        if (!selectedTipe.equals("Pemasukan")) {
            JOptionPane.showMessageDialog(null, "Detail hanya tersedia untuk transaksi pemasukan.");
            return;
        }

        try {
            //membuka koneksi ke database
            Connection conn = Koneksi.konek();

            //query untuk mengambil data transaksi berdasarkan id yang dipilih
            String sql = "SELECT t.id_transaksi, t.tanggal, t.subtotal, "
                    + "(t.subtotal - t.total_akhir) AS total_diskon, "
                    + "t.total_akhir, t.metode, t.jumlah_bayar, t.kembalian, "
                    + "u.username "
                    + "FROM transaksi t "
                    + "LEFT JOIN pengguna u ON t.id_pengguna = u.id_pengguna "
                    + "WHERE t.id_transaksi = ?";

            //menyiapkan statement dengan id transaksi yang dipilih
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, selectedIdTransaksi);
            ResultSet rs = ps.executeQuery();

            //jika data ditemukan tampilkan popup detail
            if (rs.next()) {

                //mengambil id transaksi dari hasil query
                String idTrx = rs.getString("id_transaksi");

                //mengambil dan memformat waktu transaksi
                String waktu = new SimpleDateFormat("dd MMM yyyy  |  HH:mm")
                        .format(rs.getTimestamp("tanggal"));

                //mengambil subtotal transaksi
                double subtotal = rs.getDouble("subtotal");

                //mengambil total diskon dari selisih subtotal dan total akhir
                double diskon = rs.getDouble("total_diskon");

                //mengambil total akhir setelah diskon
                double total = rs.getDouble("total_akhir");

                //mengambil jumlah yang dibayarkan pelanggan
                double jumlahBayar = rs.getDouble("jumlah_bayar");

                //mengambil kembalian
                double kembalian = rs.getDouble("kembalian");

                //mengambil metode pembayaran
                String metode = rs.getString("metode");

                //mengambil username kasir, ganti null dengan strip
                String pengguna = rs.getString("username") != null ? rs.getString("username") : "-";

                //menutup result set dan statement setelah data diambil
                rs.close();
                ps.close();

                //membuat objek popup detail dengan parent frame
                PopupDetail popup = new PopupDetail(
                        (java.awt.Frame) javax.swing.SwingUtilities.getWindowAncestor(this),
                        true
                );

                //mengisi data pembayaran ke popup
                popup.setPembayaran(subtotal, diskon, total, jumlahBayar, kembalian, metode);

                //mengisi data header struk berupa id transaksi, kasir, dan waktu
                popup.setHeaderData(idTrx, pengguna, waktu);

                //menampilkan popup di tengah layar
                popup.setLocationRelativeTo(null);
                popup.setVisible(true);

            } else {

                //menutup result set dan statement jika data tidak ditemukan
                rs.close();
                ps.close();

                //menampilkan pesan jika data transaksi tidak ditemukan
                JOptionPane.showMessageDialog(null, "Data transaksi tidak ditemukan.");

            }

        } catch (SQLException sQLException) {
            //menampilkan pesan jika gagal mengambil data dari database
            JOptionPane.showMessageDialog(null, sQLException.getMessage());
        }

    }//GEN-LAST:event_btnLihatDetailActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel JLabel111;
    private javax.swing.JLabel Jlabel;
    private com.toedter.calendar.JMonthChooser PeriodeBulan;
    private javax.swing.JButton btnLihatDetail;
    private javax.swing.JButton btnUnduh;
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
    private javax.swing.JLabel lblMargin;
    private javax.swing.JLabel lblPeresentasePengeluaran;
    private javax.swing.JLabel lblPersentasePemasukan;
    private javax.swing.JLabel lblRincian;
    private javax.swing.JLabel lblTotalPemasukan;
    private javax.swing.JLabel lblTotalPengeluaran;
    private javax.swing.JLabel lblpemasukkan;
    private javax.swing.JPanel main;
    private jtablecustom.JTableCustom tblLaporan;
    // End of variables declaration//GEN-END:variables
}
