/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package posseblakratu.component;

import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.ui.FlatLineBorder;
import java.awt.Color;
import java.awt.Insets;
import java.util.List;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import posseblakratu.config.Koneksi;
import posseblakratu.config.FormatUang;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import posseblakratu.view.FrameLogin;
import posseblakratu.view.PanelTransaksi;

/**
 *
 * @author Al
 */
public final class PopupBayar extends javax.swing.JDialog {

    //menyimpan subtotal transaksi
    private double subtotal;

    //menyimpan diskon transaksi
    private double diskon;

    //menyimpan total pembayaran
    private double totalBayar = 0;

    //menyimpan metode pembayaran
    private String metodePembayaran = "Tunai";

    //menyimpan nominal pembayaran
    private double nominalBayar = 0;

    //menyimpan kembalian
    private double kembalian = 0;

    //menyimpan seluruh data keranjang dari panel transaksi
    private List<cardKeranjang> daftarKeranjang;

    //menyimpan id transaksi
    private String idTransaksi;

    //menyimpan object PanelTransaksi
    private PanelTransaksi panelTransaksi;

    /**
     * Creates new form
     *
     * @param parent
     * @param modal
     */
    //SAAT POPUP BAYAR DI TAMPILKAN
    public PopupBayar(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();

        this.setBackground(new Color(0, 0, 0, 0));
        if (this.getContentPane() instanceof javax.swing.JComponent) {
            ((javax.swing.JComponent) this.getContentPane()).setOpaque(false);
        }

        panelLengkung();

        stateBtnBayar(btnQris);
        btnTunai.setSelected(true); //default pemilihan tunai
        stateBtnBayar(btnTunai);

        buttonDesain();

    }

    void panelLengkung() {

        panelHitung.setBorder(new FlatLineBorder(
                new Insets(0, 0, 0, 0),
                Color.decode("#E7BDBB"),
                1f,
                10));

        popupBayar.setBorder(new FlatLineBorder(
                new Insets(5, 5, 5, 5),
                Color.decode("#E7BDBB"),
                2f,
                15));

        panelInput.setBorder(new FlatLineBorder(
                new Insets(0, 0, 0, 0),
                Color.decode("#E7BDBB"),
                2f,
                15));

        panelKembalian.setBorder(new FlatLineBorder(
                new Insets(0, 0, 0, 0),
                Color.decode("#E7BDBB"),
                1f,
                10));
    }

    void buttonDesain() {
        btnBatal.putClientProperty("FlatLaf.style",
                "borderWidth:1; "
                + "background:#FBF8FF;"
                + "foreground:#B03A2E;"
                + "hoverBackground:#F3EDFF;"
                + "pressedBackground:#F6F0FF;"
                + "arc:8;"
                + "borderColor:#E7BCBA;"
                + "focusedBorderColor:#E7BCBA; "
                + "hoverBorderColor:#E7BCBA");

        btnProses.putClientProperty("FlatLaf.style",
                "borderWidth:1; "
                + "arc:8; "
        );
    }

    void stateBtnBayar(javax.swing.JToggleButton tombol) {
        tombol.setUI(new javax.swing.plaf.basic.BasicToggleButtonUI());

        if (tombol.isSelected()) {
            //state aktif
            tombol.setBackground(new java.awt.Color(255, 241, 241));
            tombol.setForeground(new java.awt.Color(216, 3, 41));

            tombol.setBorder(new FlatLineBorder(
                    new Insets(0, 0, 0, 0),
                    Color.decode("#D80329"),
                    2f,
                    10
            ));
        } else {
            //state nonaktif
            tombol.setBackground(new java.awt.Color(255, 255, 255));
            tombol.setForeground(new java.awt.Color(93, 63, 61));

            tombol.setBorder(new FlatLineBorder(
                    new Insets(1, 1, 1, 1),
                    Color.decode("#E6BBBA"),
                    1f,
                    10
            ));
        }
    }

    //MENERIMA DATA DARI PANEL TRANSAKSI
    //dipanggil dari btnBayarActionPerformed untuk mengisi label subtotal, diskon, dan total
    public void setPembayaran(double subtotal, double diskon, double total) {

        //menyimpan subtotal
        this.subtotal = subtotal;

        //menyimpan diskon
        this.diskon = diskon;

        //menyimpan total pembayaran
        this.totalBayar = total;

        //menampilkan subtotal
        lblSubtotal.setText(FormatUang.format(subtotal));

        //menampilkan diskon
        lblDiskon.setText("-" + FormatUang.format(diskon));

        //menampilkan total pembayaran
        lblTotal.setText(FormatUang.format(total));

    }

    //MENERIMA DAFTAR KERANJANG DARI PANEL TRANSAKSI
    //dipanggil dari btnBayarActionPerformed sebelum popup ditampilkan
    public void setKeranjang(List<cardKeranjang> daftarKeranjang) {

        //menyimpan daftar keranjang
        this.daftarKeranjang = daftarKeranjang;

    }

    //MENERIMA ID TRANSAKSI DARI PANEL TRANSAKSI
    //dipanggil dari btnBayarActionPerformed
    public void setIdTransaksi(String idTransaksi) {

        //menyimpan id transaksi
        this.idTransaksi = idTransaksi;

    }

    //MENERIMA REFERENSI PANEL TRANSAKSI
    //dibutuhkan untuk mengambil idDiskon dan memanggil resetTransaksi() setelah bayar
    public void setPanelTransaksi(PanelTransaksi panelTransaksi) {

        //menyimpan object panel transaksi
        this.panelTransaksi = panelTransaksi;

    }

    //USER MEMILIH METODE PEMBAYARAN
    //pilihTunai() mengaktifkan input nominal dan mengosongkan kembalian
    //pilihQris() menonaktifkan input nominal dan langsung mengisi nominal dengan total
    private void pilihTunai() {

        //menyimpan metode pembayaran
        metodePembayaran = "Tunai";

        //mengaktifkan textfield pembayaran
        txtNominal.setEditable(true);

        //mengosongkan input pembayaran
        txtNominal.setText("");

        //mengosongkan nominal pembayaran
        nominalBayar = 0;

        //mengosongkan kembalian
        kembalian = 0;

        //menampilkan kembalian
        lblKembalian.setText(FormatUang.format(0));

    }

    //method memilih pembayaran QRIS
    private void pilihQris() {

        //menyimpan metode pembayaran
        metodePembayaran = "QRIS";

        //menonaktifkan input pembayaran
        txtNominal.setEditable(false);

        //nominal pembayaran sama dengan total
        nominalBayar = totalBayar;

        //menampilkan nominal pembayaran
        txtNominal.setText(String.valueOf((int) totalBayar));

        //qris tidak memiliki kembalian
        kembalian = 0;

        //menampilkan kembalian
        lblKembalian.setText(FormatUang.format(0));

    }

    //dipanggil setiap kali ada keyReleased di txtNominal
    //menghitung kembalian = nominalBayar - totalBayar
    private void hitungKembalian() {

        try {

            //mengambil nominal pembayaran dari textfield
            nominalBayar = Double.parseDouble(txtNominal.getText());

            //menghitung kembalian
            kembalian = nominalBayar - totalBayar;

            //menampilkan kembalian
            lblKembalian.setText(FormatUang.format(kembalian));

        } catch (NumberFormatException e) {

            //menganggap nominal pembayaran nol apabila input kosong
            nominalBayar = 0;

            //mengosongkan nilai kembalian
            kembalian = 0;

            //menampilkan nilai nol
            lblKembalian.setText(FormatUang.format(0));

        }

    }

    //method untuk menyimpan header transaksi ke database
    private boolean simpanTransaksi() {

        //query untuk menyimpan data transaksi
        String sql = "INSERT INTO transaksi VALUES (?,?,?,?,?,?,?,?,?)";

        try {
            //buka koneksi ke database
            Connection conn = Koneksi.konek();

            //siapkan statement dengan parameter
            PreparedStatement pst = conn.prepareStatement(sql);

            //isi parameter id transaksi
            pst.setString(1, idTransaksi);

            //isi parameter tanggal dan waktu transaksi
            pst.setTimestamp(2, new java.sql.Timestamp(System.currentTimeMillis()));

            //isi parameter subtotal transaksi
            pst.setDouble(3, subtotal);

            //isi parameter metode pembayaran
            pst.setString(4, metodePembayaran);

            //isi parameter jumlah bayar dari pelanggan
            pst.setDouble(5, nominalBayar);

            //isi parameter kembalian
            pst.setDouble(6, kembalian);

            //isi parameter total akhir
            pst.setDouble(7, totalBayar);

            //isi parameter id pengguna yang sedang login dari session
            pst.setString(8, FrameLogin.getIdPengguna());

            //ambil id diskon dari panel transaksi
            String idDiskon = panelTransaksi.getIdDiskon();

            //jika ada diskon aktif isi id diskon
            if (idDiskon != null) {
                pst.setString(9, idDiskon);
            } else {
                //jika tidak ada diskon simpan NULL
                pst.setNull(9, java.sql.Types.VARCHAR);
            }

            //jalankan proses penyimpanan
            pst.executeUpdate();

            //kembalikan status berhasil
            return true;

        } catch (SQLException sQLException) {
            //tampilkan pesan jika gagal menyimpan transaksi
            JOptionPane.showMessageDialog(null, "Gagal menyimpan transaksi!");
        }

        //kembalikan status gagal
        return false;

    }

    //method untuk generate id detail transaksi otomatis berformat DTL0001, DTL0002, dst
    private String generateIdDetail() {

        //variabel untuk menyimpan id detail transaksi
        String idDetail = "";

        //query untuk mengambil id detail transaksi terakhir
        String sql = "SELECT id_detail FROM detail_transaksi ORDER BY id_detail DESC LIMIT 1";

        try {
            //buka koneksi ke database
            Connection conn = Koneksi.konek();

            //siapkan statement
            PreparedStatement ps = conn.prepareStatement(sql);

            //jalankan query
            ResultSet rs = ps.executeQuery();

            //jika sudah ada data ambil id terakhir
            if (rs.next()) {

                //ambil id terakhir
                String idTerakhir = rs.getString("id_detail");

                //ambil angka setelah tulisan DTL
                int nomor = Integer.parseInt(idTerakhir.substring(3));

                //increment nomor
                nomor++;

                //format ulang jadi DTL0002 dst
                idDetail = String.format("DTL%04d", nomor);

            } else {

                //jika belum ada data mulai dari DTL0001
                idDetail = "DTL0001";

            }

        } catch (SQLException sQLException) {
            //tampilkan pesan jika gagal membuat id detail
            JOptionPane.showMessageDialog(null, "Gagal membuat id detail transaksi!");
        }

        //kembalikan id detail transaksi
        return idDetail;

    }
    private String generateIdDetailTopping() {

        //variabel untuk menyimpan id terakhir dari database
        String lastId = null;

        //query untuk mengambil id detail topping terakhir
        String sql = "SELECT id_detail_topping FROM detail_topping ORDER BY id_detail_topping DESC LIMIT 1";

        try {
            //buka koneksi ke database
            Connection conn = Koneksi.konek();

            //siapkan statement
            PreparedStatement ps = conn.prepareStatement(sql);

            //jalankan query
            ResultSet rs = ps.executeQuery();

            //jika ada data ambil id terakhirnya
            if (rs.next()) {
                lastId = rs.getString("id_detail_topping");
            }

        } catch (SQLException sQLException) {
            //tampilkan pesan jika gagal mengambil id detail topping
            JOptionPane.showMessageDialog(null, "Gagal membuat id detail topping!");
        }

        //jika belum ada data sama sekali mulai dari TTP0001
        if (lastId == null) {
            return "TTP0001";
        }

        //ambil angka dari TTP0001 lalu increment
        int angka = Integer.parseInt(lastId.substring(3));
        angka++;

        //format ulang jadi TTP0002 dst
        return String.format("TTP%04d", angka);

    }

    //SIMPAN DETAIL TRANSAKSI DAN TOPPING KE DATABASE
    //dipanggil setelah simpanTransaksi() berhasil
    //loop seluruh keranjang lalu insert ke detail_transaksi dan detail_topping
    private boolean simpanDetailTransaksi() {

        //membuat query untuk menyimpan detail transaksi
        String sql = "INSERT INTO detail_transaksi VALUES (?,?,?,?,?,?,?,?)";

        try {

            //membuka koneksi ke database
            Connection conn = Koneksi.konek();

            //melakukan perulangan seluruh isi keranjang
            for (cardKeranjang card : daftarKeranjang) {

                //menyiapkan query
                PreparedStatement pst = conn.prepareStatement(sql);

                //membuat id detail transaksi baru
                String idDetail = generateIdDetail();

                //mengisi parameter id detail
                pst.setString(1, idDetail);

                //mengisi parameter jumlah pesanan
                pst.setInt(2, card.getQty());

                //mengubah level menjadi format database
                String level = null;

                //memeriksa apakah menu memiliki level
                if (card.getLevel() != -1) {

                    //mengubah level menjadi format enum database
                    level = card.getLevel() + "-Ratu "
                            + switch (card.getLevel()) {
                        case 0 ->
                            "Takut Pedas";
                        case 1 ->
                            "Baik";
                        case 2 ->
                            "Santuy";
                        case 3 ->
                            "Judes";
                        case 4 ->
                            "Marah";
                        default ->
                            "Ngamuk";
                    };

                }

                //mengisi parameter level
                pst.setString(3, level);

                //mengisi parameter harga level
                pst.setDouble(4, card.getHargaLevel());

                //mengisi parameter harga satuan
                pst.setDouble(5, card.getHargaSatuan());

                //mengisi parameter subtotal produk
                pst.setDouble(6, card.getSubtotal());

                //mengisi parameter id transaksi
                pst.setString(7, idTransaksi);

                //mengisi parameter id produk
                pst.setString(8, card.getIdProduk());

                //menjalankan proses penyimpanan
                pst.executeUpdate();

                //loop semua topping di dalam 1 card
                for (cardKeranjang.ToppingItem topping : card.getDaftarTopping()) {

                    //query insert topping
                    String sqlTopping = "INSERT INTO detail_topping VALUES (?,?,?,?,?,?)";

                    PreparedStatement pstTopping = conn.prepareStatement(sqlTopping);

                    //isi parameter id topping
                    pstTopping.setString(1, generateIdDetailTopping());

                    //isi parameter kuantitas topping
                    pstTopping.setInt(2, topping.getQty());

                    //isi parameter harga satuan topping
                    pstTopping.setDouble(3, topping.getHarga());

                    //isi parameter subtotal topping
                    pstTopping.setDouble(4, topping.getSubtotal());

                    //isi parameter id detail transaksi sebagai relasi
                    pstTopping.setString(5, idDetail);

                    //isi parameter id produk topping
                    pstTopping.setString(6, topping.getIdProduk());

                    //jalankan insert topping
                    pstTopping.executeUpdate();
                }

            }

            //kembalikan status berhasil
            return true;

        } catch (SQLException sQLException) {
            //tampilkan pesan jika gagal menyimpan detail transaksi
            JOptionPane.showMessageDialog(null, "Gagal menyimpan detail transaksi!");
        }

        //kembalikan status gagal
        return false;

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
        popupBayar = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        panelHitung = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        lblSubtotal = new javax.swing.JLabel();
        lblDiskon = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel11 = new javax.swing.JLabel();
        lblTotal = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        btnTunai = new javax.swing.JToggleButton();
        btnQris = new javax.swing.JToggleButton();
        jPanel6 = new javax.swing.JPanel();
        btnBatal = new javax.swing.JButton();
        btnProses = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        panelInput = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        txtNominal = new javax.swing.JTextField();
        panelKembalian = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        lblKembalian = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(450, 575));
        setUndecorated(true);
        setResizable(false);

        popupBayar.setBackground(new java.awt.Color(255, 255, 255));
        popupBayar.setOpaque(false);
        popupBayar.setLayout(new java.awt.BorderLayout());

        jPanel1.setBackground(new java.awt.Color(244, 242, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(231, 189, 187)));
        jPanel1.setPreferredSize(new java.awt.Dimension(448, 60));

        jLabel1.setFont(new java.awt.Font("Plus Jakarta Sans Medium", 0, 20)); // NOI18N
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/posseblakratu/icon/ikondompet.png"))); // NOI18N
        jLabel1.setText("Pembayaran");
        jLabel1.setIconTextGap(10);

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/posseblakratu/icon/IconX.png"))); // NOI18N
        jLabel2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jLabel2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel2MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 333, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 17, Short.MAX_VALUE)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(18, Short.MAX_VALUE))
        );

        popupBayar.add(jPanel1, java.awt.BorderLayout.PAGE_START);

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createEmptyBorder(15, 15, 15, 15));

        panelHitung.setBackground(new java.awt.Color(251, 248, 255));
        panelHitung.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(231, 189, 187)));

        jLabel14.setFont(new java.awt.Font("Plus Jakarta Sans", 0, 14)); // NOI18N
        jLabel14.setText("Subtotal");

        lblSubtotal.setFont(new java.awt.Font("Plus Jakarta Sans", 0, 14)); // NOI18N
        lblSubtotal.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblSubtotal.setText("Rp 0");
        lblSubtotal.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 3));

        lblDiskon.setFont(new java.awt.Font("Plus Jakarta Sans", 0, 14)); // NOI18N
        lblDiskon.setForeground(new java.awt.Color(214, 4, 39));
        lblDiskon.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblDiskon.setText("- Rp 0");
        lblDiskon.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 3));

        jLabel15.setFont(new java.awt.Font("Plus Jakarta Sans", 0, 14)); // NOI18N
        jLabel15.setText("Diskon ");

        jSeparator1.setForeground(new java.awt.Color(231, 189, 187));

        jLabel11.setFont(new java.awt.Font("Plus Jakarta Sans SemiBold", 0, 18)); // NOI18N
        jLabel11.setText("Total");

        lblTotal.setFont(new java.awt.Font("Plus Jakarta Sans SemiBold", 0, 24)); // NOI18N
        lblTotal.setForeground(new java.awt.Color(214, 4, 39));
        lblTotal.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblTotal.setText("Rp 0");

        javax.swing.GroupLayout panelHitungLayout = new javax.swing.GroupLayout(panelHitung);
        panelHitung.setLayout(panelHitungLayout);
        panelHitungLayout.setHorizontalGroup(
            panelHitungLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelHitungLayout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(panelHitungLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelHitungLayout.createSequentialGroup()
                        .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(98, 98, 98)
                        .addComponent(lblTotal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelHitungLayout.createSequentialGroup()
                        .addGroup(panelHitungLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, 75, Short.MAX_VALUE)
                            .addComponent(jLabel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(panelHitungLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(lblDiskon, javax.swing.GroupLayout.DEFAULT_SIZE, 189, Short.MAX_VALUE)
                            .addComponent(lblSubtotal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addGap(24, 24, 24))
        );
        panelHitungLayout.setVerticalGroup(
            panelHitungLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelHitungLayout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(panelHitungLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblSubtotal, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel14, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelHitungLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblDiskon)
                    .addComponent(jLabel15))
                .addGap(15, 15, 15)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelHitungLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTotal)
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(19, 19, 19))
        );

        jPanel3.setOpaque(false);
        jPanel3.setPreferredSize(new java.awt.Dimension(0, 95));
        jPanel3.setLayout(new java.awt.BorderLayout());

        jLabel3.setFont(new java.awt.Font("Plus Jakarta Sans SemiBold", 0, 14)); // NOI18N
        jLabel3.setText("Metode Pembayaran");
        jLabel3.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 8, 1));
        jPanel3.add(jLabel3, java.awt.BorderLayout.PAGE_START);

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setLayout(new java.awt.GridLayout(1, 0, 12, 0));

        buttonGroup1.add(btnTunai);
        btnTunai.setFont(new java.awt.Font("Plus Jakarta Sans SemiBold", 0, 14)); // NOI18N
        btnTunai.setIcon(new javax.swing.ImageIcon(getClass().getResource("/posseblakratu/icon/icontunainonaktif.png"))); // NOI18N
        btnTunai.setText("Tunai");
        btnTunai.setDisabledIcon(new javax.swing.ImageIcon(getClass().getResource("/posseblakratu/icon/icontunainonaktif.png"))); // NOI18N
        btnTunai.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/posseblakratu/icon/iconvector.png"))); // NOI18N
        btnTunai.addItemListener(this::btnTunaiItemStateChanged);
        jPanel4.add(btnTunai);

        buttonGroup1.add(btnQris);
        btnQris.setFont(new java.awt.Font("Plus Jakarta Sans SemiBold", 0, 14)); // NOI18N
        btnQris.setIcon(new javax.swing.ImageIcon(getClass().getResource("/posseblakratu/icon/iconqris.png"))); // NOI18N
        btnQris.setText("Qris");
        btnQris.setDisabledIcon(new javax.swing.ImageIcon(getClass().getResource("/posseblakratu/icon/iconqris.png"))); // NOI18N
        btnQris.setIconTextGap(6);
        btnQris.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/posseblakratu/icon/iconqrisactive.png"))); // NOI18N
        btnQris.addItemListener(this::btnQrisItemStateChanged);
        jPanel4.add(btnQris);

        jPanel3.add(jPanel4, java.awt.BorderLayout.CENTER);

        jPanel6.setOpaque(false);
        jPanel6.setLayout(new java.awt.GridLayout(1, 2, 10, 10));

        btnBatal.setBackground(new java.awt.Color(251, 248, 255));
        btnBatal.setFont(new java.awt.Font("Plus Jakarta Sans SemiBold", 0, 16)); // NOI18N
        btnBatal.setText("Batal");
        btnBatal.setFocusPainted(false);
        btnBatal.addActionListener(this::btnBatalActionPerformed);
        jPanel6.add(btnBatal);

        btnProses.setBackground(new java.awt.Color(187, 26, 26));
        btnProses.setFont(new java.awt.Font("Plus Jakarta Sans SemiBold", 0, 16)); // NOI18N
        btnProses.setForeground(new java.awt.Color(255, 255, 255));
        btnProses.setText("Proses & Cetak Struk");
        btnProses.setBorderPainted(false);
        btnProses.setFocusPainted(false);
        btnProses.addActionListener(this::btnProsesActionPerformed);
        jPanel6.add(btnProses);

        jPanel5.setOpaque(false);
        jPanel5.setLayout(new java.awt.BorderLayout());

        jLabel4.setFont(new java.awt.Font("Plus Jakarta Sans SemiBold", 0, 14)); // NOI18N
        jLabel4.setText("Nominal Pembayaran");
        jLabel4.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 8, 1));
        jPanel5.add(jLabel4, java.awt.BorderLayout.PAGE_START);

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));
        jPanel7.setOpaque(false);
        jPanel7.setLayout(new java.awt.GridLayout(2, 0, 10, 10));

        panelInput.setBackground(new java.awt.Color(255, 255, 255));
        panelInput.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(231, 189, 187)));

        jLabel5.setFont(new java.awt.Font("Plus Jakarta Sans SemiBold", 0, 16)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(93, 63, 61));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("Rp");
        jLabel5.setPreferredSize(new java.awt.Dimension(50, 21));

        txtNominal.setBackground(new java.awt.Color(255, 255, 255));
        txtNominal.setFont(new java.awt.Font("Plus Jakarta Sans SemiBold", 0, 19)); // NOI18N
        txtNominal.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        txtNominal.setText("0");
        txtNominal.setBorder(null);
        txtNominal.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtNominalKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNominalKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout panelInputLayout = new javax.swing.GroupLayout(panelInput);
        panelInput.setLayout(panelInputLayout);
        panelInputLayout.setHorizontalGroup(
            panelInputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelInputLayout.createSequentialGroup()
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtNominal, javax.swing.GroupLayout.DEFAULT_SIZE, 324, Short.MAX_VALUE)
                .addContainerGap())
        );
        panelInputLayout.setVerticalGroup(
            panelInputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelInputLayout.createSequentialGroup()
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelInputLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtNominal)
                .addContainerGap())
        );

        jPanel7.add(panelInput);

        panelKembalian.setBackground(new java.awt.Color(251, 248, 255));
        panelKembalian.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(231, 189, 187)));
        panelKembalian.setLayout(new java.awt.BorderLayout());

        jLabel7.setFont(new java.awt.Font("Plus Jakarta Sans SemiBold", 0, 16)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(93, 63, 61));
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("Kembalian");
        jLabel7.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 15, 1, 1));
        jLabel7.setPreferredSize(new java.awt.Dimension(100, 21));
        panelKembalian.add(jLabel7, java.awt.BorderLayout.LINE_START);

        lblKembalian.setFont(new java.awt.Font("Plus Jakarta Sans SemiBold", 0, 19)); // NOI18N
        lblKembalian.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblKembalian.setText("0");
        lblKembalian.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 24));
        panelKembalian.add(lblKembalian, java.awt.BorderLayout.CENTER);

        jPanel7.add(panelKembalian);

        jPanel5.add(jPanel7, java.awt.BorderLayout.CENTER);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelHitung, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(panelHitung, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(19, 19, 19)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(19, 19, 19)
                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, 167, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        popupBayar.add(jPanel2, java.awt.BorderLayout.CENTER);

        getContentPane().add(popupBayar, java.awt.BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jLabel2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel2MouseClicked
        //menutup popup bayar dengan klik ikon X
        dispose();
    }//GEN-LAST:event_jLabel2MouseClicked

    private void btnTunaiItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_btnTunaiItemStateChanged
        //perbarui tampilan tombol tunai
        stateBtnBayar(btnTunai);
        //aktifkan mode pembayaran tunai
        pilihTunai();
    }//GEN-LAST:event_btnTunaiItemStateChanged

    private void btnQrisItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_btnQrisItemStateChanged
        //perbarui tampilan tombol qris
        stateBtnBayar(btnQris);
        //aktifkan mode pembayaran qris
        pilihQris();
    }//GEN-LAST:event_btnQrisItemStateChanged

    private void btnProsesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProsesActionPerformed
        //simpan header transaksi ke tabel transaksi
        boolean berhasil = simpanTransaksi();

        //hentikan jika gagal menyimpan header
        if (!berhasil) {
            return;
        }

        //simpan detail item dan topping ke tabel detail_transaksi dan detail_topping
        boolean berhasilDetail = simpanDetailTransaksi();

        //hentikan jika gagal menyimpan detail
        if (!berhasilDetail) {
            return;
        }

        //ambil waktu transaksi saat ini dalam format yang sesuai
        String waktu = new java.text.SimpleDateFormat("dd-MM-yyyy | HH:mm")
                .format(new java.util.Date());

        //buat objek pratinjau struk
        PratinjauStruk struk = new PratinjauStruk(
                (java.awt.Frame) javax.swing.SwingUtilities.getWindowAncestor(this),
                true
        );

        //kirim data pembayaran ke struk
        struk.setPembayaran(
                subtotal,
                diskon,
                totalBayar,
                nominalBayar,
                kembalian,
                metodePembayaran
        );

        //kirim header struk berisi id transaksi, username kasir, dan waktu
        struk.setHeaderData(
                idTransaksi,
                FrameLogin.getUsername(),
                waktu
        );

        //tutup popup bayar
        dispose();

        //tampilkan pratinjau struk
        struk.setVisible(true);

        //refresh tabel laporan karena ada pemasukan baru
        posseblakratu.view.PanelLaporan.refresh();

        //reset keranjang di panel transaksi setelah transaksi selesai
        panelTransaksi.resetTransaksi();
    }//GEN-LAST:event_btnProsesActionPerformed

    private void txtNominalKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNominalKeyReleased
        //jika metode pembayaran tunai hitung kembalian setiap kali ada input
        if (metodePembayaran.equals("Tunai")) {
            hitungKembalian();
        }
    }//GEN-LAST:event_txtNominalKeyReleased

    private void txtNominalKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNominalKeyTyped
        //hanya izinkan karakter angka yang diketik
        char huruf = evt.getKeyChar();
        if (!Character.isDigit(huruf)) {
            evt.consume();
        }
    }//GEN-LAST:event_txtNominalKeyTyped

    private void btnBatalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBatalActionPerformed
        //tutup popup bayar tanpa menyimpan apapun
        dispose();
    }//GEN-LAST:event_btnBatalActionPerformed

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
                PopupBayar dialog = new PopupBayar(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton btnBatal;
    private javax.swing.JButton btnProses;
    private javax.swing.JToggleButton btnQris;
    private javax.swing.JToggleButton btnTunai;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel lblDiskon;
    private javax.swing.JLabel lblKembalian;
    private javax.swing.JLabel lblSubtotal;
    private javax.swing.JLabel lblTotal;
    private javax.swing.JPanel panelHitung;
    private javax.swing.JPanel panelInput;
    private javax.swing.JPanel panelKembalian;
    private javax.swing.JPanel popupBayar;
    private javax.swing.JTextField txtNominal;
    // End of variables declaration//GEN-END:variables
}
