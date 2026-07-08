/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package posseblakratu.component;

import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.ui.FlatLineBorder;
import java.awt.Color;
import java.awt.Image;
import java.awt.Insets;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import posseblakratu.config.Koneksi;
import posseblakratu.config.FormatUang;

import javax.swing.Box;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;

/**
 *
 * @author Al
 */
public final class PopupDetail extends javax.swing.JDialog {

    //menyimpan subtotal transaksi
    private double subtotal;

    //menyimpan total diskon
    private double diskon;

    //menyimpan total pembayaran
    private double total;

    //menyimpan nominal uang pelanggan
    private double tunai;

    //menyimpan total kembalian
    private double kembalian;

    //menyimpan metode pembayaran
    private String metodePembayaran;

    //id transaksi
    private String idTransaksi;

    //user login
    private String pengguna;

    //waktu transaksi
    private String waktu;

    /**
     * Creates new form
     *
     * @param parent
     * @param modal
     */
    public PopupDetail(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();

        this.setBackground(new Color(0, 0, 0, 0));
        if (this.getContentPane() instanceof javax.swing.JComponent jComponent) {
            jComponent.setOpaque(false);
        }

        panelLengkung(popupStruk); //memberikan border rounded pada panel utama

        buttonDesain(); //mengatur styling tombol tutup dan cetak

        loadPengaturan(); //mengambil nama toko, alamat, dan footer dari database

        loadStrukFromDatabase(); //memanggil pertama kali saat idTransaksi masih null

        strukContent.setSize(printStruk.getPreferredSize());

    }

    void panelLengkung(JPanel p) {

        p.setBorder(new FlatLineBorder(
                new Insets(5, 5, 5, 5),
                Color.decode("#E7BDBB"),
                2f,
                15));

    }

    void buttonDesain() {
        btnTutup.putClientProperty("FlatLaf.style",
                "borderWidth:1; "
                + "background:#FBF8FF;"
                + "foreground:#B03A2E;"
                + "hoverBackground:#F3EDFF;"
                + "pressedBackground:#F6F0FF;"
                + "arc:8;"
                + "borderColor:#E7BCBA;"
                + "focusedBorderColor:#E7BCBA; "
                + "hoverBorderColor:#E7BCBA");

        btnCetak.putClientProperty("FlatLaf.style",
                "borderWidth:1; "
                + "arc:8; "
        );
    }

    //method untuk mengambil data pengaturan toko dari database
    private void loadPengaturan() {

        //query untuk mengambil data pengaturan toko
        String sql = "SELECT * FROM pengaturan LIMIT 1";

        try {
            //buka koneksi ke database
            Connection conn = Koneksi.konek();

            //siapkan statement
            PreparedStatement ps = conn.prepareStatement(sql);

            //jalankan query
            ResultSet rs = ps.executeQuery();

            //jika data ditemukan
            if (rs.next()) {

                //tampilkan nama toko di label
                lblNama.setText(rs.getString("nama_umkm"));

                //ambil alamat dan nomor telepon dari database
                String alamat = rs.getString("alamat");
                String telp = rs.getString("no_telepon");

                //ganti karakter enter dengan tag br agar bisa tampil di label html
                alamat = alamat.replaceAll("\\r?\\n", "<br>");

                //tampilkan alamat dan nomor telepon
                lblInfo.setText(
                        "<html><center>"
                        + alamat
                        + "<br>Telp: " + telp
                        + "</center></html>"
                );

                //ambil teks footer struk dari database
                String footer = rs.getString("footer_struk");

                //ganti karakter enter dengan tag br
                footer = footer.replaceAll("\\r?\\n", "<br>");

                //pisah footer menjadi maksimal dua bagian
                String[] baris = footer.split("<br>", 2);

                //jika footer memiliki dua baris
                if (baris.length == 2) {

                    //tampilkan baris pertama tebal dan baris kedua biasa
                    lblFooter.setText(
                            "<html><center>"
                            + "<b>" + baris[0] + "</b><br>"
                            + baris[1]
                            + "</center></html>"
                    );

                } else {

                    //tampilkan satu baris dengan format tebal
                    lblFooter.setText(
                            "<html><center><b>"
                            + footer
                            + "</b></center></html>"
                    );

                }

                //mengambil path logo toko dari database
                String logo = rs.getString("logo_toko");

                //jika path logo tidak kosong, tampilkan gambar ke label logo struk
                if (logo != null && !logo.isEmpty()) {

                    //membuat ImageIcon dari path logo
                    ImageIcon icon = new ImageIcon(logo);

                    //mengubah ukuran gambar agar sesuai dengan label logo struk
                    Image image = icon.getImage().getScaledInstance(
                            97,
                            97,
                            Image.SCALE_SMOOTH
                    );

                    //menampilkan gambar logo ke dalam label struk
                    jLabel3.setIcon(new ImageIcon(image));

                } else {

                    //jika tidak ada logo, hapus icon dan biarkan label kosong
                    jLabel3.setIcon(null);

                }

            }

            //tutup result set dan statement
            rs.close();
            ps.close();

        } catch (SQLException sQLException) {
            //tampilkan pesan jika gagal mengambil data pengaturan
            JOptionPane.showMessageDialog(null, "Gagal mengambil data pengaturan!");
        }

    }

    //method untuk menerima data pembayaran dari PopupBayar
    public void setPembayaran(
            double subtotal,
            double diskon,
            double total,
            double tunai,
            double kembalian,
            String metodePembayaran
    ) {

        //menyimpan subtotal transaksi
        this.subtotal = subtotal;

        //menyimpan total diskon
        this.diskon = diskon;

        //menyimpan total pembayaran
        this.total = total;

        //menyimpan nominal uang pelanggan
        this.tunai = tunai;

        //menyimpan total kembalian
        this.kembalian = kembalian;

        //menyimpan metode pembayaran
        this.metodePembayaran = metodePembayaran;

        //menampilkan data pembayaran ke label struk
        tampilkanPembayaran();

    }

    //method untuk menerima data header struk dari PopupBayar
    public void setHeaderData(String idTransaksi, String pengguna, String waktu) {

        //menyimpan id transaksi
        this.idTransaksi = idTransaksi;

        //menyimpan username kasir
        this.pengguna = pengguna;

        //menyimpan waktu transaksi
        this.waktu = waktu;

        //tampilkan id transaksi di label
        lblNoTrx.setText(idTransaksi);

        //tampilkan username kasir di label
        lblUser.setText(pengguna);

        //tampilkan waktu transaksi di label
        lblJam.setText(waktu);

        //load item pesanan dari database setelah id transaksi terisi
        loadStrukFromDatabase();

    }

    //method untuk menampilkan semua nilai pembayaran ke label struk
    private void tampilkanPembayaran() {

        //menampilkan subtotal transaksi
        stkSub.setText(FormatUang.format(subtotal));

        //menampilkan total diskon
        stkDiskon.setText("-" + FormatUang.format(diskon));

        //menampilkan total pembayaran
        stkTotalA.setText(FormatUang.format(total));

        //menampilkan metode pembayaran
        stkMetode.setText(metodePembayaran);

        //menampilkan nominal uang pelanggan
        stkBayar.setText(FormatUang.format(tunai));

        //menampilkan total kembalian
        stkKembali.setText(FormatUang.format(kembalian));

    }

    //method untuk mengambil dan menampilkan item pesanan dari database ke struk
    //dipanggil dua kali: pertama saat konstruktor (idTransaksi null), kedua saat setHeaderData()
    private void loadStrukFromDatabase() {

        //query untuk mengambil detail item pesanan berdasarkan id transaksi
        String sql = "SELECT dt.id_detail, p.nama_produk, dt.kuantitas, dt.level, dt.harga_satuan, dt.subtotal_produk "
                + "FROM detail_transaksi dt "
                + "JOIN produk p ON dt.id_produk = p.id_produk "
                + "WHERE dt.id_transaksi = ?";

        try {
            //buka koneksi ke database
            Connection conn = Koneksi.konek();

            //bersihkan tampilan struk sebelum diisi ulang
            strukContent.removeAll();

            //siapkan statement dengan parameter id transaksi
            PreparedStatement ps = conn.prepareStatement(sql);

            //isi parameter id transaksi
            ps.setString(1, idTransaksi);

            //jalankan query dan ambil hasilnya
            ResultSet rs = ps.executeQuery();

            //melakukan iterasi untuk setiap item pesanan
            while (rs.next()) {

                //ambil id detail untuk dipakai query topping
                String idDetail = rs.getString("id_detail");

                //ambil nama produk
                String namaMenu = rs.getString("nama_produk");

                //jika menu memiliki level pedas, tambahkan ke nama
                if (rs.getString("level") != null) {
                    namaMenu += " Lvl " + rs.getString("level");
                }

                //query untuk mengambil topping dari item ini
                String sqlTopping = "SELECT p.nama_produk, tp.kuantitas "
                        + "FROM detail_topping tp "
                        + "JOIN produk p ON tp.id_produk = p.id_produk "
                        + "WHERE tp.id_detail = ?";

                //siapkan statement topping
                PreparedStatement pst = conn.prepareStatement(sqlTopping);

                //isi parameter id detail
                pst.setString(1, idDetail);

                //jalankan query topping
                ResultSet rsT = pst.executeQuery();

                //variabel untuk menyimpan teks topping
                String detailTopping = "";

                //melakukan iterasi untuk setiap topping
                while (rsT.next()) {

                    //tambahkan koma jika bukan topping pertama
                    if (!detailTopping.isEmpty()) {
                        detailTopping += ", ";
                    }

                    //tambahkan nama topping dan qty ke teks detail
                    detailTopping += rsT.getString("nama_produk") + " x" + rsT.getInt("kuantitas");

                }

                //membuat card struk baru untuk item ini
                cardStruk item = new cardStruk();

                //isi data card struk
                item.setData(
                        namaMenu,
                        detailTopping,
                        rs.getInt("kuantitas"),
                        rs.getDouble("harga_satuan"),
                        rs.getDouble("subtotal_produk")
                );

                //tambahkan jarak sebelum card
                strukContent.add(Box.createVerticalStrut(7));

                //tambahkan card ke panel struk
                strukContent.add(item);

                //tutup result set dan statement topping
                rsT.close();
                pst.close();

            }

            //perbarui tampilan panel struk
            strukContent.revalidate();
            strukContent.repaint();

        } catch (SQLException sQLException) {
            //tampilkan pesan jika gagal mengambil data detail transaksi
            JOptionPane.showMessageDialog(null, "Gagal mengambil data struk!");
        }

    }

    //method untuk mencetak struk menggunakan printer
    private void cetakStruk() {

        try {

            //ambil objek printer job
            PrinterJob job = PrinterJob.getPrinterJob();

            //atur konten yang akan dicetak
            job.setPrintable(new Printable() {

                @Override
                public int print(Graphics g, PageFormat pf, int pageIndex)
                        throws PrinterException {

                    //hanya cetak satu halaman
                    if (pageIndex > 0) {
                        return NO_SUCH_PAGE;
                    }

                    //cast graphics ke graphics2d untuk rendering
                    Graphics2D g2d = (Graphics2D) g;

                    //geser posisi sesuai margin halaman
                    g2d.translate(pf.getImageableX(), pf.getImageableY());

                    //render panel struk ke printer
                    printStruk.printAll(g2d);

                    return PAGE_EXISTS;

                }

            });

            //tampilkan dialog pilih printer
            boolean doPrint = job.printDialog();

            //jika pengguna mengkonfirmasi cetak
            if (doPrint) {

                //jalankan proses cetak
                job.print();

                //tutup dialog pratinjau setelah cetak
                dispose();

            }

        } catch (PrinterException e) {
            //tampilkan pesan jika gagal mencetak
            JOptionPane.showMessageDialog(null, "Gagal mencetak struk!");
        }

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        popupStruk = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        btnTutup = new javax.swing.JButton();
        btnCetak = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        printStruk = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        lblNama = new javax.swing.JLabel();
        lblInfo = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        lblJam = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        lblNoTrx = new javax.swing.JLabel();
        lblUser = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jPanel11 = new javax.swing.JPanel();
        jPanel13 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        stkSub = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        stkDiskon = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        stkTotalA = new javax.swing.JLabel();
        jPanel14 = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();
        stkMetode = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        stkBayar = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        stkKembali = new javax.swing.JLabel();
        lblFooter = new javax.swing.JLabel();
        strukContent = new javax.swing.JPanel();
        cardStruk1 = new posseblakratu.component.cardStruk();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(345, 675));
        setUndecorated(true);
        setResizable(false);

        popupStruk.setBackground(new java.awt.Color(255, 255, 255));
        popupStruk.setOpaque(false);
        popupStruk.setLayout(new java.awt.BorderLayout());

        jPanel1.setBackground(new java.awt.Color(244, 242, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(231, 189, 187)));
        jPanel1.setPreferredSize(new java.awt.Dimension(345, 60));

        jLabel1.setFont(new java.awt.Font("Plus Jakarta Sans Medium", 0, 20)); // NOI18N
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/posseblakratu/icon/IconParkSolidTransactionOrder 1.png"))); // NOI18N
        jLabel1.setText("Detail Transaksi");
        jLabel1.setIconTextGap(10);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 321, Short.MAX_VALUE)
                .addGap(15, 15, 15))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(18, Short.MAX_VALUE))
        );

        popupStruk.add(jPanel1, java.awt.BorderLayout.PAGE_START);

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setLayout(new java.awt.BorderLayout());

        jPanel8.setBackground(new java.awt.Color(251, 248, 255));
        jPanel8.setBorder(javax.swing.BorderFactory.createEmptyBorder(15, 13, 15, 13));
        jPanel8.setMinimumSize(new java.awt.Dimension(346, 81));
        jPanel8.setPreferredSize(new java.awt.Dimension(346, 81));
        jPanel8.setLayout(new java.awt.BorderLayout());

        jPanel6.setOpaque(false);
        jPanel6.setLayout(new java.awt.GridLayout(1, 2, 10, 10));

        btnTutup.setBackground(new java.awt.Color(251, 248, 255));
        btnTutup.setFont(new java.awt.Font("Plus Jakarta Sans SemiBold", 0, 16)); // NOI18N
        btnTutup.setText("Tutup");
        btnTutup.setFocusPainted(false);
        btnTutup.addActionListener(this::btnTutupActionPerformed);
        jPanel6.add(btnTutup);

        btnCetak.setBackground(new java.awt.Color(187, 26, 26));
        btnCetak.setFont(new java.awt.Font("Plus Jakarta Sans SemiBold", 0, 16)); // NOI18N
        btnCetak.setForeground(new java.awt.Color(255, 255, 255));
        btnCetak.setText("Cetak");
        btnCetak.setBorderPainted(false);
        btnCetak.setFocusPainted(false);
        btnCetak.addActionListener(this::btnCetakActionPerformed);
        jPanel6.add(btnCetak);

        jPanel8.add(jPanel6, java.awt.BorderLayout.CENTER);

        jPanel2.add(jPanel8, java.awt.BorderLayout.PAGE_END);

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(231, 189, 187)));
        jPanel3.setPreferredSize(new java.awt.Dimension(345, 539));
        jPanel3.setLayout(new java.awt.CardLayout());

        jPanel9.setLayout(new java.awt.CardLayout());

        jScrollPane2.setBorder(null);
        jScrollPane2.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        printStruk.setBackground(new java.awt.Color(255, 255, 255));
        printStruk.setBorder(javax.swing.BorderFactory.createEmptyBorder(16, 24, 16, 24));
        printStruk.setLayout(new java.awt.BorderLayout());

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));
        jPanel5.setPreferredSize(new java.awt.Dimension(298, 195));

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/posseblakratu/icon/1776967434637 1.png"))); // NOI18N
        jLabel3.setMaximumSize(new java.awt.Dimension(97, 97));
        jLabel3.setMinimumSize(new java.awt.Dimension(97, 97));
        jLabel3.setPreferredSize(new java.awt.Dimension(97, 97));

        lblNama.setFont(new java.awt.Font("Plus Jakarta Sans SemiBold", 0, 20)); // NOI18N
        lblNama.setForeground(new java.awt.Color(214, 4, 39));
        lblNama.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblNama.setText("RATU SEBLAK");

        lblInfo.setFont(new java.awt.Font("Plus Jakarta Sans", 0, 14)); // NOI18N
        lblInfo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblInfo.setText("Jl. Lawu, No.06, RT 03 / RW 01");
        lblInfo.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        lblInfo.setMaximumSize(new java.awt.Dimension(188, 40));
        lblInfo.setMinimumSize(new java.awt.Dimension(188, 40));
        lblInfo.setPreferredSize(new java.awt.Dimension(188, 40));

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblNama, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(lblInfo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(100, 100, 100)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(100, 100, 100))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(lblNama, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(lblInfo, javax.swing.GroupLayout.DEFAULT_SIZE, 64, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        printStruk.add(jPanel5, java.awt.BorderLayout.PAGE_START);

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));
        jPanel7.setLayout(new java.awt.BorderLayout());

        jPanel10.setBackground(new java.awt.Color(255, 255, 255));
        jPanel10.setPreferredSize(new java.awt.Dimension(294, 85));

        jLabel4.setFont(new java.awt.Font("Plus Jakarta Sans Medium", 0, 12)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("-------------------------------------------------");

        lblJam.setFont(new java.awt.Font("Plus Jakarta Sans", 0, 14)); // NOI18N
        lblJam.setText("19 Mei 2026  |  19:32");
        lblJam.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 2, 0, 0));

        jLabel6.setFont(new java.awt.Font("Plus Jakarta Sans", 0, 14)); // NOI18N
        jLabel6.setText("No.TRX : ");
        jLabel6.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 2, 0, 0));

        lblNoTrx.setFont(new java.awt.Font("Plus Jakarta Sans", 1, 14)); // NOI18N
        lblNoTrx.setText("TRX-0001");

        lblUser.setFont(new java.awt.Font("Plus Jakarta Sans", 0, 14)); // NOI18N
        lblUser.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblUser.setText("owner");
        lblUser.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        lblUser.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 2, 2));

        jLabel9.setFont(new java.awt.Font("Plus Jakarta Sans Medium", 0, 12)); // NOI18N
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setText("-------------------------------------------------");

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addGap(0, 0, 0)
                        .addComponent(lblNoTrx, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(lblJam, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 78, Short.MAX_VALUE)
                .addComponent(lblUser))
            .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 6, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(14, 14, 14)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblJam)
                    .addComponent(lblUser))
                .addGap(8, 8, 8)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(lblNoTrx))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 6, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel7.add(jPanel10, java.awt.BorderLayout.PAGE_START);

        jPanel11.setBackground(new java.awt.Color(255, 255, 255));
        jPanel11.setPreferredSize(new java.awt.Dimension(297, 270));

        jPanel13.setBackground(new java.awt.Color(255, 255, 255));
        jPanel13.setPreferredSize(new java.awt.Dimension(294, 85));

        jLabel11.setFont(new java.awt.Font("Plus Jakarta Sans Medium", 0, 12)); // NOI18N
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel11.setText("-------------------------------------------------");

        jLabel12.setFont(new java.awt.Font("Plus Jakarta Sans", 0, 14)); // NOI18N
        jLabel12.setText("Subtotal");
        jLabel12.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 2, 0, 0));

        jLabel13.setFont(new java.awt.Font("Plus Jakarta Sans", 0, 14)); // NOI18N
        jLabel13.setText("Diskon");
        jLabel13.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 2, 0, 0));

        stkSub.setFont(new java.awt.Font("Plus Jakarta Sans", 0, 14)); // NOI18N
        stkSub.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        stkSub.setText("Rp. 13000");
        stkSub.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        stkSub.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 2, 2));

        jLabel16.setFont(new java.awt.Font("Plus Jakarta Sans Medium", 0, 12)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(204, 204, 204));
        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel16.setText("-------------------------------------------------");

        stkDiskon.setFont(new java.awt.Font("Plus Jakarta Sans", 0, 14)); // NOI18N
        stkDiskon.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        stkDiskon.setText("- Rp. 0");
        stkDiskon.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        stkDiskon.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 2, 2));

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, 78, Short.MAX_VALUE)
                    .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(stkSub, javax.swing.GroupLayout.DEFAULT_SIZE, 155, Short.MAX_VALUE)
                    .addComponent(stkDiskon, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(17, 17, 17))
            .addComponent(jLabel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 6, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(14, 14, 14)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(stkSub))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel13)
                    .addComponent(stkDiskon))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 6, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel10.setFont(new java.awt.Font("Plus Jakarta Sans", 1, 18)); // NOI18N
        jLabel10.setText("Total");

        stkTotalA.setFont(new java.awt.Font("Plus Jakarta Sans", 1, 18)); // NOI18N
        stkTotalA.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        stkTotalA.setText("Rp. 13000");

        jPanel14.setBackground(new java.awt.Color(255, 255, 255));
        jPanel14.setPreferredSize(new java.awt.Dimension(294, 85));

        jLabel18.setFont(new java.awt.Font("Plus Jakarta Sans Medium", 0, 12)); // NOI18N
        jLabel18.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel18.setText("-------------------------------------------------");

        stkMetode.setFont(new java.awt.Font("Plus Jakarta Sans", 0, 14)); // NOI18N
        stkMetode.setText("Tunai");
        stkMetode.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 2, 0, 0));

        jLabel20.setFont(new java.awt.Font("Plus Jakarta Sans SemiBold", 1, 14)); // NOI18N
        jLabel20.setText("Kembali");
        jLabel20.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 2, 0, 0));

        stkBayar.setFont(new java.awt.Font("Plus Jakarta Sans", 0, 14)); // NOI18N
        stkBayar.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        stkBayar.setText("Rp. 15000");
        stkBayar.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        stkBayar.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 2, 2));

        jLabel23.setFont(new java.awt.Font("Plus Jakarta Sans Medium", 0, 12)); // NOI18N
        jLabel23.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel23.setText("-------------------------------------------------");

        stkKembali.setFont(new java.awt.Font("Plus Jakarta Sans SemiBold", 1, 14)); // NOI18N
        stkKembali.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        stkKembali.setText("Rp. 2000");
        stkKembali.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 2));

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jLabel23, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel14Layout.createSequentialGroup()
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(stkMetode, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(stkBayar, javax.swing.GroupLayout.DEFAULT_SIZE, 102, Short.MAX_VALUE)
                    .addComponent(stkKembali, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(16, 16, 16))
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 6, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(14, 14, 14)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(stkMetode)
                    .addComponent(stkBayar, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel20)
                    .addComponent(stkKembali))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 6, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        lblFooter.setFont(new java.awt.Font("Plus Jakarta Sans", 0, 14)); // NOI18N
        lblFooter.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblFooter.setText("Terima Kasih Atas Kunjungan Anda!");
        lblFooter.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addComponent(jPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, 314, Short.MAX_VALUE)
            .addComponent(lblFooter, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(stkTotalA, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(16, 16, 16))
            .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel11Layout.createSequentialGroup()
                    .addGap(0, 1, Short.MAX_VALUE)
                    .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, 297, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 16, Short.MAX_VALUE)))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGap(95, 95, 95)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(stkTotalA))
                .addGap(14, 14, 14)
                .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(lblFooter, javax.swing.GroupLayout.DEFAULT_SIZE, 43, Short.MAX_VALUE))
            .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel11Layout.createSequentialGroup()
                    .addGap(0, 0, 0)
                    .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 185, Short.MAX_VALUE)))
        );

        jPanel7.add(jPanel11, java.awt.BorderLayout.PAGE_END);

        strukContent.setBackground(new java.awt.Color(255, 255, 255));
        strukContent.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 0, 8, 0));
        strukContent.setLayout(new javax.swing.BoxLayout(strukContent, javax.swing.BoxLayout.Y_AXIS));
        strukContent.add(cardStruk1);

        jPanel7.add(strukContent, java.awt.BorderLayout.CENTER);

        printStruk.add(jPanel7, java.awt.BorderLayout.CENTER);

        jScrollPane2.setViewportView(printStruk);

        jPanel9.add(jScrollPane2, "card2");

        jPanel3.add(jPanel9, "card2");

        jPanel2.add(jPanel3, java.awt.BorderLayout.CENTER);

        popupStruk.add(jPanel2, java.awt.BorderLayout.CENTER);

        getContentPane().add(popupStruk, java.awt.BorderLayout.CENTER);

        setSize(new java.awt.Dimension(360, 680));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnTutupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTutupActionPerformed
        //menutup dialog pratinjau struk dengan tombol tutup
        dispose();
    }//GEN-LAST:event_btnTutupActionPerformed

    private void btnCetakActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCetakActionPerformed
        //memanggil method cetak struk
        cetakStruk();
    }//GEN-LAST:event_btnCetakActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (UnsupportedLookAndFeelException unsupportedLookAndFeelException) {
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                PratinjauStruk dialog = new PratinjauStruk(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCetak;
    private javax.swing.JButton btnTutup;
    private javax.swing.ButtonGroup buttonGroup1;
    private posseblakratu.component.cardStruk cardStruk1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblFooter;
    private javax.swing.JLabel lblInfo;
    private javax.swing.JLabel lblJam;
    private javax.swing.JLabel lblNama;
    private javax.swing.JLabel lblNoTrx;
    private javax.swing.JLabel lblUser;
    private javax.swing.JPanel popupStruk;
    private javax.swing.JPanel printStruk;
    private javax.swing.JLabel stkBayar;
    private javax.swing.JLabel stkDiskon;
    private javax.swing.JLabel stkKembali;
    private javax.swing.JLabel stkMetode;
    private javax.swing.JLabel stkSub;
    private javax.swing.JLabel stkTotalA;
    private javax.swing.JPanel strukContent;
    // End of variables declaration//GEN-END:variables
}
