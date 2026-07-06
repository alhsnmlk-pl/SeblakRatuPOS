/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package posseblakratu.view;

import posseblakratu.component.PopupBayar;
import com.formdev.flatlaf.ui.FlatLineBorder;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Insets;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

import posseblakratu.component.cardMenu;
import posseblakratu.component.cardTopping;
import posseblakratu.component.cardKeranjang;

import java.util.List;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.Box;

import posseblakratu.config.Koneksi;
import posseblakratu.config.FormatUang;
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
    
    //Membuat list penyimpan card untuk menyimpan semua card menu
    List<cardMenu> daftarMenu = new ArrayList<>();

    //Membuat list penyimpan card topping
    List<cardTopping> daftarTopping = new ArrayList<>();

    //membuat list penyimpan card keranjang
    List<cardKeranjang> daftarKeranjang = new ArrayList<>();

    //mendeklarasikan CardLayout menjadi cardLayout agar bisa eipakai di banyak method
    private CardLayout cardLayout;

    //deklarasikan variable untuk menyimpan data menu yg di pilih
    private cardMenu menuDipilih;

    //menyimpan level pedas yang dipilih (default -1 / belum memilih level)
    private int levelPedas = -1;

    //menyimpan subtotal seluruh pesanan
    private double subtotal = 0;

    //menyimpan nominal diskon
    private double diskon = 0;

    //menyimpan id diskon yang aktif
    private String idDiskon;

    //menyimpan total pembayaran
    private double total = 0;

    //menyimpan id transaksi
    private String idTransaksi;

    /**
     * Creates new form PanelTransaksi
     */
    public PanelTransaksi() {
        initComponents();

        //mengambil layout pada panel kostumisasi
        cardLayout = (CardLayout) containerKostumisasi.getLayout();

        //menampilkan panel kostumisasi kosong sebagai tampilan awal
        cardLayout.show(containerKostumisasi, "kostumKosong");

        buttonDesain(); //memanggil method desain button next dan reset

        panelLengkung(containerDaftarMenu); //memanggil method desain panel dgn panel yg ingin di sesuaikan
        panelLengkung(containerKostumisasi);
        panelLengkung(containerKeranjang1);

        buttonLvl(btnLvl0); //memanggil method desain button (default)
        buttonLvl(btnLvl1);
        buttonLvl(btnLvl2);
        buttonLvl(btnLvl3);
        buttonLvl(btnLvl4);
        buttonLvl(btnLvl5);

        filterSemuaM.setSelected(true); //membuat btnFilter semua bernilai true/selected 
        btnFilter(filterSemuaM); //memanggil state button filter untuk btnFilter semua

        loadMenu("Semua"); //load menu untuk kategori semua
        loadTopping(); //load toping

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
                + "hoverBorderColor:#BA1A1A");

        btnResetM.putClientProperty("FlatLaf.style",
                "borderWidth:1; "
                + "background:#FBF8FF;"
                + "pressedBackground:#F6F0FF;"
                + "arc:8;"
                + "borderColor:#E7BCBA;"
                + "focusedBorderColor:#E7BCBA; "
                + "hoverBorderColor:#E7BCBA");

        btnNextM.putClientProperty("FlatLaf.style",
                "borderWidth:1; "
                + "pressedBackground:#F6F0FF;"
                + "background:#FBF8FF;"
                + "arc:8; "
                + "borderColor:#BA1A1A; "
                + "focusedBorderColor:#BA1A1A; "
                + "hoverBorderColor:#BA1A1A");
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
        //jika button selected
        if (btn.isSelected()) {
            //maka tambahkan border warna merah di bagian bawah menggunakan matte border
            btn.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 3, 0, new java.awt.Color(173, 0, 28)));
        } else {
            //jika tidak tambahkan border sebesar 3 px juga mengguakan empety border
            btn.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 3, 0));
        }
    }

    //MENGISI DAFTAR MENU
    //method untuk menampilkan menu berdasarkan kategori
    void loadMenu(String kategori) {
        //membersihkan panel conten menu
        menuContent.removeAll();

        //membersihkan data card sebelumnya
        daftarMenu.clear();

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
            Connection conn = Koneksi.konek();

            //siapkan statement sql
            PreparedStatement ps = conn.prepareStatement(sql);

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

                //mengisi data pada card menu, pastikan urutan sesuai dengan
                //penamaan variable dan tipedata di card menu
                card.setData(
                        rs.getString("id_produk"),
                        rs.getString("nama_produk"),
                        rs.getDouble("harga_jual"),
                        rs.getString("kategori"),
                        rs.getString("deskripsi")
                );

                //menyimpan card ke dalam daftar
                daftarMenu.add(card);

                //menambahkan event klik pada card
                card.addClickListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {

                        //menjadikan card yang dipilih sebagai selected
                        selectCard(card);

                    }
                });

                //menambahkan card ke panel menuContent
                menuContent.add(card);

                //memberikan jarak antar card
                menuContent.add(Box.createVerticalStrut(10));
            }

        } catch (SQLException sQLException) {

            //jika terjadi kesalahan sql tampilkan pesan error
            JOptionPane.showMessageDialog(null, "Gagal mengambil data menu!");

        }

        //memperbarui tampilan panel
        menuContent.revalidate();
        menuContent.repaint();

        //mengembalikan posisi scroll ke paling atas
        SwingUtilities.invokeLater(() -> {
            jScrollPane1.getVerticalScrollBar().setValue(0);
        });
    }

    //MENGISI DAFTAR TOPPING
    //method untuk menampilkan topping
    void loadTopping() {

        //membersihkan daftar topping
        daftarTopping.clear();

        try {

            //query sql untuk mengambil data topping
            String sql = "SELECT * FROM produk WHERE kategori='Topping' ORDER BY id_produk";

            //membuat koneksi ke database
            Connection conn = Koneksi.konek();

            //siapkan statement sql
            PreparedStatement ps = conn.prepareStatement(sql);

            //jalankan query dan ambil hasilnya
            ResultSet rs = ps.executeQuery();

            //melakukan perulangan selama data masih ada
            while (rs.next()) {

                //membuat card topping baru
                cardTopping card = new cardTopping();

                //mengisi data topping pada card
                card.setData(
                        rs.getString("id_produk"),
                        rs.getString("nama_produk"),
                        rs.getDouble("harga_jual")
                );

                //menyimpan card topping
                daftarTopping.add(card);

                //menambahkan card ke panel topping
                toppingContent.add(card);

                //memberikan jarak antar card
                toppingContent.add(Box.createVerticalStrut(10));
            }

        } catch (SQLException sQLException) {

            //jika terjadi kesalahan sql tampilkan pesan error
            JOptionPane.showMessageDialog(null, "Gagal mengambil data topping!");

        }

        //memperbarui tampilan panel
        toppingContent.revalidate();
        toppingContent.repaint();
    }

    //USER MEMILIH MENU
    //method untuk memilih card menu
    void selectCard(cardMenu selectedCard) {

        //menyimpan menu yang dipilih
        menuDipilih = selectedCard;

        //menghapus status terpilih pada semua card
        for (cardMenu card : daftarMenu) {
            card.setSelected(false);
        }

        //menjadikan card yang dipilih sebagai card aktif
        selectedCard.setSelected(true);

        //memeriksa kategori menu yang dipilih
        if (selectedCard.getKategori().equals("Seblak")) {

            //menampilkan panel kostumisasi seblak
            cardLayout.show(containerKostumisasi, "kostumSeblak");

        } else if (selectedCard.getKategori().equals("Minuman")) {

            //menampilkan panel kostumisasi minuman
            cardLayout.show(containerKostumisasi, "kostumMinum");

        }

        //menampilkan nama menu pada panel kostumisasi seblak
        lblMenu.setText(selectedCard.getNama());

        //menampilkan nama menu pada panel kostumisasi minuman
        lblMenu2.setText(selectedCard.getNama());

    }

    //method untuk mereset panel kostumisasi ke kondisi awal
    //dipanggil setelah user klik next/reset agar panel siap untuk pesanan berikutnya
    void reset() {

        //menghapus status terpilih pada semua card menu
        for (cardMenu card : daftarMenu) {
            card.setSelected(false);
        }

        //mengembalikan selection button level ke posisi awal
        buttonGroupLvl.clearSelection();

        //mengembalikan nilai level pedas ke -1 (belum memilih)
        levelPedas = -1;

        //mengembalikan qty seluruh topping ke nilai awal
        for (cardTopping topping : daftarTopping) {
            topping.resetQty();
        }

        //menampilkan panel kostumisasi kosong
        cardLayout.show(containerKostumisasi, "kostumKosong");

        //mengembalikan posisi scroll topping ke paling atas
        SwingUtilities.invokeLater(() -> {
            jScrollPane3.getVerticalScrollBar().setValue(0);
        });

    }

    //UPDATE SUBTOTAL DAN DISKON
    //dipanggil setiap kali isi keranjang berubah (tambah item, hapus item)
    //method untuk menghitung subtotal seluruh pesanan
    private void updateSubtotal() {

        //mengosongkan subtotal sebelum dihitung ulang
        subtotal = 0;

        //melakukan perulangan seluruh card keranjang
        for (cardKeranjang card : daftarKeranjang) {

            //menambahkan subtotal setiap pesanan
            subtotal += card.getSubtotal();

        }

        //menampilkan subtotal
        lblSubtotal.setText(FormatUang.format(subtotal));

        //memperbarui diskon dgn method update diskon
        updateDiskon();

    }

    //method untuk menghitung diskon
    private void updateDiskon() {

        //query untuk mengambil diskon yang sedang aktif berdasarkan  status
        String sql = "SELECT * FROM diskon WHERE status='Aktif' LIMIT 1";

        try {
            //menghubungkan ke database
            Connection conn = Koneksi.konek();

            //menyiapkan query
            PreparedStatement ps = conn.prepareStatement(sql);

            //menjalankan query
            ResultSet rs = ps.executeQuery();

            //default jika tidak ada diskon
            idDiskon = null;
            diskon = 0;

            //memeriksa apakah ada diskon aktif
            if (rs.next()) {

                //mengambil id diskon
                idDiskon = rs.getString("id_diskon");

                //mengambil tipe diskon
                String tipeDiskon = rs.getString("tipe_diskon");

                //mengambil nilai diskon
                double nilaiDiskon = rs.getDouble("nilai_diskon");

                //jika tipe nominal maka diskon tetap
                if (tipeDiskon.equals("Nominal")) {
                    diskon = nilaiDiskon;
                } else {
                    //jika tipe persen maka diskon dihitung dari subtotal
                    diskon = subtotal * (nilaiDiskon / 100);
                }

            }

            //menghitung total akhir
            total = subtotal - diskon;

            //menampilkan diskon
            lblDiskon.setText("-" + FormatUang.format(diskon));

            //menampilkan total
            lblTotal.setText(FormatUang.format(total));

        } catch (SQLException sQLException) {
            //tampilkan pesan jika gagal mengambil data diskon
            JOptionPane.showMessageDialog(null, "Gagal mengambil data diskon!");
        }

    }

    //method untuk mengambil id diskon yang sedang aktif
    public String getIdDiskon() {

        //mengembalikan id diskon (null jika tidak ada diskon aktif)
        return idDiskon;

    }



    //RESET KERANJANG SETELAH TRANSAKSI SELESAI
    //dipanggil dari PopupBayar setelah struk berhasil ditampilkan
    public void resetTransaksi() {

        //menghapus seluruh data keranjang
        daftarKeranjang.clear();

        //menghapus seluruh card pada panel keranjang
        keranjangContent.removeAll();

        //memperbarui tampilan panel keranjang
        keranjangContent.revalidate();
        keranjangContent.repaint();

        //mengosongkan nilai subtotal
        subtotal = 0;

        //mengosongkan nilai diskon
        diskon = 0;

        //mengosongkan nilai total
        total = 0;

        //menampilkan subtotal menjadi nol
        lblSubtotal.setText(FormatUang.format(0));

        //menampilkan diskon menjadi nol
        lblDiskon.setText("-" + FormatUang.format(0));

        //menampilkan total menjadi nol
        lblTotal.setText(FormatUang.format(0));

    }

    //TAMBAH CARD KE KERANJANG
    //dipanggil dari btnNextActionPerformed dan btnNextMActionPerformed
    //memasang event hapus, event qty, update subtotal, dan generate id transaksi
    private void tambahKeKeranjang(cardKeranjang card) {

        //menyimpan card ke daftar keranjang
        daftarKeranjang.add(card);

        //mendaftarkan event pada tombol hapus
        card.addDeleteListener(e -> {

            //menghapus card dari panel keranjang
            keranjangContent.remove(card);

            //menghapus card dari daftar keranjang
            daftarKeranjang.remove(card);

            //memperbarui tampilan panel keranjang
            keranjangContent.revalidate();
            keranjangContent.repaint();

            //memperbarui subtotal seluruh pesanan
            updateSubtotal();

        });

        //mendaftarkan event ketika qty pesanan berubah
        card.addQtyListener(() -> {

            //memperbarui subtotal seluruh pesanan
            updateSubtotal();

        });

        //menambahkan card ke panel keranjang
        keranjangContent.add(card);

        //memberikan jarak antar card
        keranjangContent.add(Box.createVerticalStrut(10));

        //memperbarui tampilan panel keranjang
        keranjangContent.revalidate();
        keranjangContent.repaint();

        //memperbarui subtotal seluruh pesanan
        updateSubtotal();

        //generate id transaksi baru
        idTransaksi = generateIdTransaksi();

        //menampilkan id transaksi di atas keranjang
        lblNoTransaksi.setText(idTransaksi);

    }

    //method untuk generate id transaksi otomatis
    private String generateIdTransaksi() {
        //variabel untuk menyimpan id terakhir dari database
        String lastId = null;

        try {
            //koneksi ke database
            Connection conn = Koneksi.konek();

            //query ambil id transaksi terakhir
            String sql = "SELECT id_transaksi FROM transaksi ORDER BY "
                    + "id_transaksi DESC LIMIT 1";

            //prepare statement
            PreparedStatement ps = conn.prepareStatement(sql);

            //eksekusi query
            ResultSet rs = ps.executeQuery();

            //jika ada data
            if (rs.next()) {
                //ambil id terakhir
                lastId = rs.getString("id_transaksi");
            }

        } catch (SQLException sQLException) {
            //tampilkan pesan jika gagal mengambil id transaksi
            JOptionPane.showMessageDialog(null, "Gagal membuat id transaksi!");
        }

        //jika belum ada transaksi sama sekali
        if (lastId == null) {
            return "TRX0001";
        }

        //mengambil angka dari TRX0001 → 0001
        int angka = Integer.parseInt(lastId.substring(3));

        //increment angka
        angka++;

        //format ulang jadi TRX0002 dst
        return String.format("TRX%04d", angka);
    }

    //method untuk mengambil id transaksi
    public String getIdTransaksi() {

        //mengembalikan id transaksi
        return idTransaksi;

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
        containerKostumisasi = new javax.swing.JPanel();
        KostumisasiSeblak = new javax.swing.JPanel();
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
        KostumisasiKosong = new javax.swing.JPanel();
        jPanel19 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        lblMenu1 = new javax.swing.JLabel();
        jPanel20 = new javax.swing.JPanel();
        jPanel21 = new javax.swing.JPanel();
        btnNext1 = new javax.swing.JButton();
        btnReset1 = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        KostumisasiMinuman = new javax.swing.JPanel();
        jPanel27 = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();
        lblMenu2 = new javax.swing.JLabel();
        jPanel28 = new javax.swing.JPanel();
        jPanel29 = new javax.swing.JPanel();
        btnResetM = new javax.swing.JButton();
        btnNextM = new javax.swing.JButton();
        jPanel30 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        containerKeranjang1 = new javax.swing.JPanel();
        KeranjangIsi = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jPanel18 = new javax.swing.JPanel();
        lblNoTransaksi = new javax.swing.JLabel();
        jPanel11 = new javax.swing.JPanel();
        jPanel13 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        lblSubtotal = new javax.swing.JLabel();
        lblDiskon = new javax.swing.JLabel();
        lblDiskon2 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel11 = new javax.swing.JLabel();
        lblTotal = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        btnBayar = new javax.swing.JButton();
        jPanel12 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        keranjangContent = new javax.swing.JPanel();
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

        containerKostumisasi.setBackground(new java.awt.Color(255, 255, 255));
        containerKostumisasi.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(231, 189, 187)));
        containerKostumisasi.setLayout(new java.awt.CardLayout());

        KostumisasiSeblak.setBackground(new java.awt.Color(255, 255, 255));
        KostumisasiSeblak.setLayout(new java.awt.BorderLayout());

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

        KostumisasiSeblak.add(jPanel8, java.awt.BorderLayout.PAGE_START);

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

        KostumisasiSeblak.add(jPanel14, java.awt.BorderLayout.PAGE_END);

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
        btnLvl4.setText("4 - Ratu Marah  +1k");
        btnLvl4.setContentAreaFilled(false);
        btnLvl4.addItemListener(this::btnLvl4ItemStateChanged);
        jPanel16.add(btnLvl4);

        buttonGroupLvl.add(btnLvl5);
        btnLvl5.setFont(new java.awt.Font("Plus Jakarta Sans", 0, 13)); // NOI18N
        btnLvl5.setText("5 - Ratu Ngamuk +2k");
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

        KostumisasiSeblak.add(jPanel5, java.awt.BorderLayout.CENTER);

        containerKostumisasi.add(KostumisasiSeblak, "kostumSeblak");

        KostumisasiKosong.setBackground(new java.awt.Color(255, 255, 255));
        KostumisasiKosong.setLayout(new java.awt.BorderLayout());

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

        KostumisasiKosong.add(jPanel19, java.awt.BorderLayout.PAGE_START);

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
        btnNext1.setEnabled(false);
        btnNext1.setFocusPainted(false);
        btnNext1.setFocusable(false);
        jPanel21.add(btnNext1);

        btnReset1.setBackground(new java.awt.Color(251, 248, 255));
        btnReset1.setFont(new java.awt.Font("Plus Jakarta Sans SemiBold", 0, 16)); // NOI18N
        btnReset1.setForeground(new java.awt.Color(187, 26, 26));
        btnReset1.setText("Next");
        btnReset1.setEnabled(false);
        btnReset1.setFocusPainted(false);
        btnReset1.setFocusable(false);
        jPanel21.add(btnReset1);

        jPanel20.add(jPanel21, java.awt.BorderLayout.CENTER);

        KostumisasiKosong.add(jPanel20, java.awt.BorderLayout.PAGE_END);

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));
        jPanel7.setLayout(new java.awt.BorderLayout());

        jLabel3.setFont(new java.awt.Font("Plus Jakarta Sans", 0, 16)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Pilih Menu Terlebih Dahulu");
        jPanel7.add(jLabel3, java.awt.BorderLayout.CENTER);

        KostumisasiKosong.add(jPanel7, java.awt.BorderLayout.CENTER);

        containerKostumisasi.add(KostumisasiKosong, "kostumKosong");

        KostumisasiMinuman.setBackground(new java.awt.Color(255, 255, 255));
        KostumisasiMinuman.setLayout(new java.awt.BorderLayout());

        jPanel27.setBackground(new java.awt.Color(255, 255, 255));
        jPanel27.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(231, 189, 187)));
        jPanel27.setPreferredSize(new java.awt.Dimension(335, 63));

        jLabel18.setFont(new java.awt.Font("Plus Jakarta Sans SemiBold", 0, 18)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(24, 26, 46));
        jLabel18.setText("Kostumisasi");
        jLabel18.setPreferredSize(new java.awt.Dimension(99, 19));

        lblMenu2.setFont(new java.awt.Font("Plus Jakarta Sans", 0, 14)); // NOI18N
        lblMenu2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblMenu2.setText(" ");

        javax.swing.GroupLayout jPanel27Layout = new javax.swing.GroupLayout(jPanel27);
        jPanel27.setLayout(jPanel27Layout);
        jPanel27Layout.setHorizontalGroup(
            jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel27Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblMenu2)
                .addGap(16, 16, 16))
        );
        jPanel27Layout.setVerticalGroup(
            jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel27Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel18, javax.swing.GroupLayout.DEFAULT_SIZE, 33, Short.MAX_VALUE)
                    .addComponent(lblMenu2, javax.swing.GroupLayout.DEFAULT_SIZE, 33, Short.MAX_VALUE))
                .addGap(15, 15, 15))
        );

        KostumisasiMinuman.add(jPanel27, java.awt.BorderLayout.PAGE_START);

        jPanel28.setBackground(new java.awt.Color(255, 255, 255));
        jPanel28.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 0, 0, new java.awt.Color(231, 189, 187)));
        jPanel28.setPreferredSize(new java.awt.Dimension(335, 43));
        jPanel28.setLayout(new java.awt.BorderLayout());

        jPanel29.setBackground(new java.awt.Color(255, 255, 255));
        jPanel29.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        jPanel29.setLayout(new java.awt.GridLayout(1, 2, 5, 0));

        btnResetM.setBackground(new java.awt.Color(251, 248, 255));
        btnResetM.setFont(new java.awt.Font("Plus Jakarta Sans SemiBold", 0, 16)); // NOI18N
        btnResetM.setText("Reset");
        btnResetM.setFocusPainted(false);
        btnResetM.addActionListener(this::btnResetMActionPerformed);
        jPanel29.add(btnResetM);

        btnNextM.setBackground(new java.awt.Color(251, 248, 255));
        btnNextM.setFont(new java.awt.Font("Plus Jakarta Sans SemiBold", 0, 16)); // NOI18N
        btnNextM.setForeground(new java.awt.Color(187, 26, 26));
        btnNextM.setText("Next");
        btnNextM.setFocusPainted(false);
        btnNextM.addActionListener(this::btnNextMActionPerformed);
        jPanel29.add(btnNextM);

        jPanel28.add(jPanel29, java.awt.BorderLayout.CENTER);

        KostumisasiMinuman.add(jPanel28, java.awt.BorderLayout.PAGE_END);

        jPanel30.setBackground(new java.awt.Color(255, 255, 255));
        jPanel30.setLayout(new java.awt.BorderLayout());

        jLabel6.setFont(new java.awt.Font("Plus Jakarta Sans", 0, 16)); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("Minuman Tidak Perlu Kostumisasi");
        jPanel30.add(jLabel6, java.awt.BorderLayout.CENTER);

        KostumisasiMinuman.add(jPanel30, java.awt.BorderLayout.CENTER);

        containerKostumisasi.add(KostumisasiMinuman, "kostumMinum");

        add(containerKostumisasi);

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

        lblNoTransaksi.setFont(new java.awt.Font("Plus Jakarta Sans", 1, 12)); // NOI18N
        lblNoTransaksi.setForeground(new java.awt.Color(255, 255, 255));
        lblNoTransaksi.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblNoTransaksi.setText(" ");
        jPanel18.add(lblNoTransaksi, java.awt.BorderLayout.CENTER);

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

        lblDiskon2.setFont(new java.awt.Font("Plus Jakarta Sans", 0, 14)); // NOI18N
        lblDiskon2.setText("Diskon");

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
                .addComponent(lblDiskon2, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lblDiskon, javax.swing.GroupLayout.DEFAULT_SIZE, 133, Short.MAX_VALUE))
            .addComponent(jSeparator1)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblTotal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(btnBayar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jSeparator2, javax.swing.GroupLayout.Alignment.TRAILING)
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
                    .addComponent(lblDiskon2)
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
        keranjangContent.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 15, 0, 15));
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

        add(containerKeranjang1);
    }// </editor-fold>//GEN-END:initComponents

    private void filterSemuaMItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_filterSemuaMItemStateChanged
        //perbarui tampilan tombol filter semua
        btnFilter(filterSemuaM);
        //load menu kategori semua
        loadMenu("Semua");
    }//GEN-LAST:event_filterSemuaMItemStateChanged

    private void filterSeblakMItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_filterSeblakMItemStateChanged
        //perbarui tampilan tombol filter seblak
        btnFilter(filterSeblakM);
        //load menu kategori seblak
        loadMenu("Seblak");
    }//GEN-LAST:event_filterSeblakMItemStateChanged

    private void filterMinumanMItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_filterMinumanMItemStateChanged
        //perbarui tampilan tombol filter minuman
        btnFilter(filterMinumanM);
        //load menu kategori minuman
        loadMenu("Minuman");
    }//GEN-LAST:event_filterMinumanMItemStateChanged

    private void btnLvl0ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_btnLvl0ItemStateChanged
        //perbarui tampilan tombol level 0
        buttonLvl(btnLvl0);
        //simpan level pedas yang dipilih
        levelPedas = 0;
    }//GEN-LAST:event_btnLvl0ItemStateChanged

    private void btnLvl1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_btnLvl1ItemStateChanged
        //perbarui tampilan tombol level 1
        buttonLvl(btnLvl1);
        //simpan level pedas yang dipilih
        levelPedas = 1;
    }//GEN-LAST:event_btnLvl1ItemStateChanged

    private void btnLvl2ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_btnLvl2ItemStateChanged
        //perbarui tampilan tombol level 2
        buttonLvl(btnLvl2);
        //simpan level pedas yang dipilih
        levelPedas = 2;
    }//GEN-LAST:event_btnLvl2ItemStateChanged

    private void btnLvl3ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_btnLvl3ItemStateChanged
        //perbarui tampilan tombol level 3
        buttonLvl(btnLvl3);
        //simpan level pedas yang dipilih
        levelPedas = 3;
    }//GEN-LAST:event_btnLvl3ItemStateChanged

    private void btnLvl4ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_btnLvl4ItemStateChanged
        //perbarui tampilan tombol level 4
        buttonLvl(btnLvl4);
        //simpan level pedas yang dipilih
        levelPedas = 4;
    }//GEN-LAST:event_btnLvl4ItemStateChanged

    private void btnLvl5ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_btnLvl5ItemStateChanged
        //perbarui tampilan tombol level 5
        buttonLvl(btnLvl5);
        //simpan level pedas yang dipilih
        levelPedas = 5;
    }//GEN-LAST:event_btnLvl5ItemStateChanged

    private void btnBayarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBayarActionPerformed
        //periksa apakah keranjang masih kosong
        if (daftarKeranjang.isEmpty()) {
            //tampilkan pesan bahwa belum ada menu yang dipilih
            JOptionPane.showMessageDialog(null, "Keranjang masih kosong, silakan pilih menu terlebih dahulu!");
            return;
        }

        //buat objek popup bayar
        PopupBayar bayar = new PopupBayar((java.awt.Frame) javax.swing.SwingUtilities.getWindowAncestor(this), true);

        //kirim data subtotal, diskon, dan total ke popup
        bayar.setPembayaran(subtotal, diskon, total);

        //kirim referensi panel transaksi ke popup
        bayar.setPanelTransaksi(this);

        //kirim seluruh isi keranjang ke popup
        bayar.setKeranjang(daftarKeranjang);

        //kirim id transaksi yang sudah di-generate ke popup
        bayar.setIdTransaksi(idTransaksi);

        //tampilkan popup bayar
        bayar.setVisible(true);
    }//GEN-LAST:event_btnBayarActionPerformed

    private void btnResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetActionPerformed
        //panggil method reset untuk mengosongkan pilihan
        reset();
    }//GEN-LAST:event_btnResetActionPerformed

    private void btnNextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNextActionPerformed
        //validasi: periksa apakah ada menu yang dipilih
        if (menuDipilih == null) {
            JOptionPane.showMessageDialog(null, "Silakan pilih menu terlebih dahulu");
            return;
        }

        //validasi: periksa apakah level pedas sudah dipilih
        if (levelPedas == -1) {
            JOptionPane.showMessageDialog(null, "Silakan pilih level pedas");
            return;
        }

        //buat card keranjang baru
        cardKeranjang card = new cardKeranjang();

        //buat list untuk menyimpan data topping yang dipilih
        List<cardKeranjang.ToppingItem> toppingDipilih = new ArrayList<>();

        //kumpulkan topping yang qty-nya lebih dari nol
        for (cardTopping topping : daftarTopping) {
            if (topping.getQty() > 0) {
                //tambahkan topping yang dipilih ke list
                toppingDipilih.add(new cardKeranjang.ToppingItem(
                        topping.getIdProduk(),
                        topping.getNama(),
                        topping.getHarga(),
                        topping.getQty()
                ));
            }
        }

        //hitung biaya tambahan level pedas (level 4 = Rp1.000, level 5 = Rp2.000)
        double hargaLevel = 0;
        if (levelPedas == 4) {
            hargaLevel = 1000;
        } else if (levelPedas == 5) {
            hargaLevel = 2000;
        }

        //isi data ke card keranjang
        card.setData(
                menuDipilih.getIdProduk(),
                menuDipilih.getNama(),
                menuDipilih.getHarga(),
                levelPedas,
                hargaLevel,
                toppingDipilih
        );

        //tambahkan card ke keranjang dan perbarui tampilan
        tambahKeKeranjang(card);

        //kembalikan panel kostumisasi ke kondisi awal
        reset();
    }//GEN-LAST:event_btnNextActionPerformed

    private void btnResetMActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetMActionPerformed
        //panggil method reset untuk mengosongkan pilihan
        reset();
    }//GEN-LAST:event_btnResetMActionPerformed

    private void btnNextMActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNextMActionPerformed
        //validasi: periksa apakah ada menu yang dipilih
        if (menuDipilih == null) {
            JOptionPane.showMessageDialog(null, "Silakan pilih menu terlebih dahulu");
            return;
        }

        //buat card keranjang baru
        cardKeranjang card = new cardKeranjang();

        //buat list topping kosong karena minuman tidak memiliki topping
        List<cardKeranjang.ToppingItem> toppingDipilih = new ArrayList<>();

        //isi data ke card keranjang dengan level -1 dan hargaLevel 0 (minuman tidak ada level)
        card.setData(
                menuDipilih.getIdProduk(),
                menuDipilih.getNama(),
                menuDipilih.getHarga(),
                -1,
                0,
                toppingDipilih
        );

        //tambahkan card ke keranjang dan perbarui tampilan
        tambahKeKeranjang(card);

        //kembalikan panel kostumisasi ke kondisi awal
        reset();
    }//GEN-LAST:event_btnNextMActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel KeranjangIsi;
    private javax.swing.JPanel KostumisasiKosong;
    private javax.swing.JPanel KostumisasiMinuman;
    private javax.swing.JPanel KostumisasiSeblak;
    private javax.swing.JButton btnBayar;
    private javax.swing.JToggleButton btnLvl0;
    private javax.swing.JToggleButton btnLvl1;
    private javax.swing.JToggleButton btnLvl2;
    private javax.swing.JToggleButton btnLvl3;
    private javax.swing.JToggleButton btnLvl4;
    private javax.swing.JToggleButton btnLvl5;
    private javax.swing.JButton btnNext;
    private javax.swing.JButton btnNext1;
    private javax.swing.JButton btnNextM;
    private javax.swing.JButton btnReset;
    private javax.swing.JButton btnReset1;
    private javax.swing.JButton btnResetM;
    private javax.swing.ButtonGroup buttonGroupFilter;
    private javax.swing.ButtonGroup buttonGroupLvl;
    private javax.swing.JPanel containerDaftarMenu;
    private javax.swing.JPanel containerKeranjang1;
    private javax.swing.JPanel containerKostumisasi;
    private javax.swing.JToggleButton filterMinumanM;
    private javax.swing.JToggleButton filterSeblakM;
    private javax.swing.JToggleButton filterSemuaM;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
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
    private javax.swing.JPanel jPanel27;
    private javax.swing.JPanel jPanel28;
    private javax.swing.JPanel jPanel29;
    private javax.swing.JPanel jPanel30;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JPanel keranjangContent;
    private javax.swing.JLabel lblDiskon;
    private javax.swing.JLabel lblDiskon2;
    private javax.swing.JLabel lblMenu;
    private javax.swing.JLabel lblMenu1;
    private javax.swing.JLabel lblMenu2;
    private javax.swing.JLabel lblNoTransaksi;
    private javax.swing.JLabel lblSubtotal;
    private javax.swing.JLabel lblTotal;
    private javax.swing.JPanel menuContent;
    private javax.swing.JPanel toppingContent;
    // End of variables declaration//GEN-END:variables
}
