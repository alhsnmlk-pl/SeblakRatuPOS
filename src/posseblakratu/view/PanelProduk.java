/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package posseblakratu.view;
import posseblakratu.config.Koneksi;
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
import javax.swing.table.DefaultTableModel;
import posseblakratu.view.FrameLogin;



/**
 *
 * @author Al
 */
public final class PanelProduk extends javax.swing.JPanel {
private boolean modeUbah =false;
private String idProdukTerpilih = null;
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
        filterSemuaP.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 3, 0, new java.awt.Color(173,0,28)));
        filterSeblakP.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 3, 0));
        filterToppingP.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 3, 0));
        filterMinumanP.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 3, 0));
        
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
    void reset(){ //method untuk mengosongkan semua inputan
        tNamaProduk.setText(null); //mengosongkan field nama produk
        tNamaProduk.setEditable(true);//setelah di kosongan , field nama produk dapat di isi inputan kembali
        tDeskProduk.setText(null); //mengosongkan field deskripsi produk
        tDeskProduk.setEditable(true);//setelah di kosongan , field deskripsi produk dapat di isi inputan kembali
        cKategoriProduk.setSelectedItem("Seblak");//mengembalikan pilihan kategori ke default yaitu Seblak
        tHargaProduk.setText(null); //mengosongkan field harga jual
        tHargaProduk.setEditable(true);//setelah di kosongan , field harga jual dapat di isi inputan kembali
        modeUbah = false; //penanda bahwa mode ubah tidak dinyalakan
        idProdukTerpilih=null;// mengosongkan id produk
        lblTambahProduk.setText("Tambah Produk"); //mengatur teks Tambah Produk padaa lblTambahProduk
        btnSimpanProduk.setText("Simpan Perubahan"); ////mengatur teks Simpan Perubahan pada lblTambahProduk
    }
    String IDProdukOtomatis(String kategori) { ///method untuk mengambil id produk otomatis berdasarkan kategori
        String prefix = "";//mendeklarasikan tipe data string dengan nama variabel prefix
        switch (kategori){ //mengatur pilihan kategori
            case "Seblak": //jika user memilih kategori seblak maka , prefix akan menampilkan huruf awalan S
                prefix = "S";
                break; // mengehntikan perulangan
            case "Topping"://jika user memilih kategori topping maka , prefix akan menampilkan huruf awalan T
                prefix = "T";
                break;// mengehntikan perulangan
            case "Minuman"://jika user memilih kategori minuman maka , prefix akan menampilkan huruf awalan M
                prefix = "M";
                break;   // mengehntikan perulangan
        }
        String idBaru =prefix + "001"; //id awal / otomatis jika belum ada data produk
        //query SQL untuk menampilkan id_produk, dari tabel produk, dimana id produk sesuai huruf yang di prexif lalu mengurutkan id produk secara descending limitnya 1
        String sql = "SELECT id_produk FROM produk WHERE id_produk LIKE ? ORDER BY id_produk DESC LIMIT 1"; //Query SQL untuk mengambil id_produk terakhir berdasarkan prefix
        try{
            Connection conn = Koneksi.konek();//membuka koneksi ke database menggunakan method konek()

            PreparedStatement ps = conn.prepareStatement(sql);//siapkan prepared statement
            
            ps.setString(1,prefix + "%");//isi parameter prefix untuk pencarian id
            
            ResultSet resultSet = ps.executeQuery();//jalankan query dan ambil hasilnya

            if(resultSet.next()) { //jika data ditemukan 
                String idTerakhir = resultSet.getString("id_produk");//ambil id_produk terakhir hasil query
                int angkaTerakhir = Integer.parseInt(idTerakhir.substring(1));// melakukan parsing untuk membuang huruf prefix 
                int angkaBaru =angkaTerakhir + 1 ;//menambahkan 1 dan angka terakhir, untuk dapatkan urutan berikutnya 
                
                idBaru = prefix + String.format("%03d", angkaBaru);//gabungkan kembali huruf prefix dengan angka baru
            }
        } catch (SQLException sQLException) {
            //menampilkan pesan error saat berinteraksi dengan database
           JOptionPane.showMessageDialog(null,"Gagal membuat ID produk");
            
        }
        return idBaru; //mengembalikan id produk yang telah dibuat
    }
    void load_tabel_produk(String kategori){
        DefaultTableModel mdl =new DefaultTableModel(); //membuat object mdl tabel baru
        //menambahkan kolom ke dalam mdl tabel
        mdl.addColumn("ID");//Kolom pertama untuk ID
        mdl.addColumn("Nama Produk"); //Kolom kedua untuk nama produk
        mdl.addColumn("Kategori"); //Kolom ketiga untuk kategori
        mdl.addColumn("Harga Jual"); //Kolom keempat untuk harga jual
        mdl.addColumn("Status"); //Kolom kelima untuk status
        
        String sql; 
        if(kategori.equals("Semua")){ ///jika yang dipilih adalah tombol semua
            sql = "SELECT * FROM produk";// QUERY SQL untuk mengambil semua data dari tabel produk
        }else{
            sql = "SELECT * FROM produk WHERE kategori='"+kategori+"'"; //filter berdasarkan kategori yang dipilih
        }
        
        try {
            //membuka koneksi ke database
            Connection conn = Koneksi.konek();

            Statement st = conn.createStatement();//membuat statement untuk menjalankan Query

            ResultSet rs = st.executeQuery(sql);//menjalankan query dan menyimpan hasilnya dalam result set

            while (rs.next()) { //mengambil setiap baris data hasil query
                String idproduk = rs.getString("id_produk"); //mengambil data kolom id_produk
                String namaProduk = rs.getString("nama_produk"); //mengambil data kolom nama_produk
                String kategoriProduk = rs.getString("kategori"); //mengambil data kolom kategori
                double hargaJual = rs.getDouble("harga_jual");//mengambil harga jual bertipe double
                String hargaProduk = String.valueOf((long)hargaJual); //maengubah harga menjadi string tanpa angka desimal
                String statusProduk = rs.getString("status"); //mengambil data kolom status

                Object[] baris = {idproduk, namaProduk,kategoriProduk,hargaProduk,statusProduk};//membuat array berisi data satu baris

                mdl.addRow(baris);//menambahkan array baris ke dalam model tabel
            }
        } catch (SQLException sQLException) {
            // menampilkan pesan error jika gagal mengambil data dari database
            JOptionPane.showMessageDialog(null,"Gagal mengambil data!");
        }
        //menampilkan model data pada tabel daftar produk
        tblProduk.setModel(mdl);
        //sembunyikan kolom ID PRODUK dari tampilan , tapi datanya tetep ada di mdl 
        tblProduk.getColumnModel().getColumn(0).setWidth(0);
        tblProduk.getColumnModel().getColumn(0).setMinWidth(0);
        tblProduk.getColumnModel().getColumn(0).setMaxWidth(0);
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
        btnSimpanProduk = new javax.swing.JButton();
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
        jPanel4.setBorder(javax.swing.BorderFactory.createEmptyBorder(21, 21, 20, 21));
        jPanel4.setMinimumSize(new java.awt.Dimension(345, 130));
        jPanel4.setPreferredSize(new java.awt.Dimension(345, 130));
        jPanel4.setLayout(new java.awt.GridBagLayout());

        btnSimpanProduk.setBackground(new java.awt.Color(214, 4, 39));
        btnSimpanProduk.setFont(new java.awt.Font("Plus Jakarta Sans SemiBold", 0, 16)); // NOI18N
        btnSimpanProduk.setForeground(new java.awt.Color(255, 255, 255));
        btnSimpanProduk.setIcon(new javax.swing.ImageIcon(getClass().getResource("/posseblakratu/icon/IconSimpan.png"))); // NOI18N
        btnSimpanProduk.setText("Simpan Perubahan");
        btnSimpanProduk.setBorderPainted(false);
        btnSimpanProduk.addActionListener(this::btnSimpanProdukActionPerformed);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.ipadx = 111;
        gridBagConstraints.ipady = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(21, 21, 0, 21);
        jPanel4.add(btnSimpanProduk, gridBagConstraints);

        btnBatalProduk.setFont(new java.awt.Font("Plus Jakarta Sans SemiBold", 0, 16)); // NOI18N
        btnBatalProduk.setText("Batal");
        btnBatalProduk.addActionListener(this::btnBatalProdukActionPerformed);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipadx = 73;
        gridBagConstraints.ipady = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(12, 21, 20, 0);
        jPanel4.add(btnBatalProduk, gridBagConstraints);

        btnHapusProduk.setFont(new java.awt.Font("Plus Jakarta Sans SemiBold", 0, 16)); // NOI18N
        btnHapusProduk.setForeground(new java.awt.Color(214, 4, 39));
        btnHapusProduk.setIcon(new javax.swing.ImageIcon(getClass().getResource("/posseblakratu/icon/Vector.png"))); // NOI18N
        btnHapusProduk.setText("Hapus");
        btnHapusProduk.addActionListener(this::btnHapusProdukActionPerformed);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipadx = 51;
        gridBagConstraints.ipady = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(12, 12, 20, 0);
        jPanel4.add(btnHapusProduk, gridBagConstraints);

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
                {"M001", "Es Jeruk", "Minuman", "5.000", "Tersedia"},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "ID", "Nama Produk", "Kategori", "Harga Jual", "Status"
            }
        ));
        tblProduk.setCellPaddingLeft(25);
        tblProduk.setCellPaddingRight(25);
        tblProduk.setCenterColumns("2,3,4");
        tblProduk.setColumnWidths("90,200,110,110,110");
        tblProduk.setFont(new java.awt.Font("Plus Jakarta Sans", 0, 14)); // NOI18N
        tblProduk.setHeaderPaddingLeft(25);
        tblProduk.setHeaderPaddingRight(25);
        tblProduk.setLeftColumns("0,1");
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
        // TODO add your handling code here:
        //cek apakah tombol filter "Semua" sedang dipilih oleh user
        if (filterSemuaP.isSelected()) {
            ///beri garis bawah merah sebagai tanda  tombol sedang di pilih
            filterSemuaP.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 3, 0, new java.awt.Color(173,0,28)));
            load_tabel_produk("Semua"); //muat ulang tabel produk untuk menampilkan seluruh produk dari berbagai kategori
        } else {
            //hapus garis bawah jika tombol "Semua" sedang tidak dipilih
            filterSemuaP.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 3, 0));
        }
    }//GEN-LAST:event_filterSemuaPItemStateChanged

    private void filterSeblakPItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_filterSeblakPItemStateChanged
        // TODO add your handling code here:
        //cek apakah tombol filter "Seblak" sedang dipilih oleh user
        if (filterSeblakP.isSelected()) {
            ///beri garis bawah merah sebagai tanda  tombol sedang di pilih
            filterSeblakP.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 3, 0, new java.awt.Color(173,0,28)));
            load_tabel_produk("Seblak");//muat ulang tabel produk untuk menampilkan kategori seblak aja
        } else {
            //hapus garis bawah jika tombol "Seblak" sedang tidak dipilih
            filterSeblakP.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 3, 0));
        }
    }//GEN-LAST:event_filterSeblakPItemStateChanged

    private void filterToppingPItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_filterToppingPItemStateChanged
        // TODO add your handling code here:
        if (filterToppingP.isSelected()) { ////cek apakah tombol filter "Topping" sedang dipilih oleh user
            ///beri garis bawah merah sebagai tanda  tombol sedang di pilih
            filterToppingP.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 3, 0, new java.awt.Color(173,0,28)));
            load_tabel_produk("Topping");//muat ulang tabel produk untuk menampilkan kategori topping aja
        } else {
            //hapus garis bawah jika tombol "Topping" sedang tidak dipilih
            filterToppingP.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 3, 0));
        }
    }//GEN-LAST:event_filterToppingPItemStateChanged

    private void filterMinumanPItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_filterMinumanPItemStateChanged
        // TODO add your handling code here:
        if (filterMinumanP.isSelected()) {//cek apakah tombol filter "Minuman" sedang dipilih oleh user
            ///beri garis bawah merah sebagai tanda  tombol sedang di pilih
            filterMinumanP.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 3, 0, new java.awt.Color(173,0,28)));
            load_tabel_produk("Minuman");//muat ulang tabel produk untuk menampilkan kategori minuman aja
        } else {
            //hapus garis bawah jika tombol "Minuman" sedang tidak dipilih
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
       //mengambil input nama produk dari text field tNamaProduk
        String namaProduk= tNamaProduk.getText();
        //menampilkan dialog konfirmasi sebelum menghapus data
        int pilihan = JOptionPane.showConfirmDialog(null,
                "Apakah anda yakin ingin manghapus produk \"" + namaProduk + "\" ?",
                "Pesan Konfirmasi", JOptionPane.YES_NO_OPTION);
        //jika user memilih tombol PILIHAN yes maka akan menghapus data produk dari tabel produk 
        switch (pilihan) {
            case JOptionPane.YES_OPTION:
               //menyusun perintah / query SQL untuk mengahapus data produk berdasarkan nama_produk
                String sql ="DELETE FROM produk WHERE nama_produk=?";
                
            try {
            //membuka koneksi ke database menggunakan method konek()
            Connection conn = Koneksi.konek();
            //mempersiapkan query SQL dengan parameter (preparedStatement)
            PreparedStatement ps = conn.prepareStatement(sql);
            
            ps.setString(1,namaProduk);//mengisi parameter ke satu dengan namaProduk
            
            ps.execute();//menjalankan perintah DELETE untuk menghapus data di database
            
            JOptionPane.showMessageDialog(null, "Data Produk berhasil dihapus");//untuk menampilkan pesan bahwa data berhasil di hapus
        } catch (SQLException sQLException) {
            //menampilkan pesan jika terjadi kesalahan saat menghapus data
            JOptionPane.showMessageDialog(null,"Data Produk gagal dihapus");
        } 
        load_tabel_produk("Semua");//memuat ulang data produk di tabel tampilan
        
        reset();//mereset semua input agar kosong kembali
        break; 
            case JOptionPane.NO_OPTION: //jika user memilih pilihan no maka tidak terjadi aksi apapun
                break;
            default: //jika dialog konfirmasi ditutup memilih yes / no, maka tidak ada aksi apapun yang terjadi
                break;
        } 
    }//GEN-LAST:event_btnHapusProdukActionPerformed

    private void btnBatalProdukActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBatalProdukActionPerformed
        // TODO add your handling code here:
        reset();//memanggil method reset , agar saat tombol batal di klik akan dapat mengosogkan semua inputan 
    }//GEN-LAST:event_btnBatalProdukActionPerformed

    private void btnSimpanProdukActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSimpanProdukActionPerformed
        // TODO add your handling code here:
        //cek apakah yang login adalah owner, jika bukan maka akses di tolak
        if (!FrameLogin.getRole().equals("Owner")){
            JOptionPane.showMessageDialog(null,"Hanya Owner yang dapat mengubah data produk");
            return;  //hentikan proses 
        }
        //ambil input dari text field tNamaProduk DAN simpan ke variabel namaProduk 
        String namaProduk = tNamaProduk.getText();
        //ambil input dari text field tDeskProduk DAN simpan ke variabel deskProduk 
        String deskProduk = tDeskProduk.getText();
        //ambil input dari combo box cKategori Produk DAN simpan ke variabel kategoriProduk 
        String kategoriProduk =cKategoriProduk.getSelectedItem().toString();
        //ambil input dari text field tHargaProduk DAN simpan ke variabel hargaProduk 
        String hargaProduk = tHargaProduk.getText();
        //ambil input dari togglebutton, true jika tersedia, false jika tidak tersedia
        String statusProduk = btnStatusProduk.isSelected()?"Tersedia":"Tidak Tersedia";
        
        //jika dalam mode mengubah data produk, mska penanda modeUbah adalah true
        if (modeUbah){
            //Query  SQL untuk mengubah kolom nama_produk, harga_jual, kategori, deskripsi, status beerdasarkan id_produk
            String sql = "UPDATE produk SET nama_produk=?, harga_jual=?, kategori=?, deskripsi=?, status=? WHERE id_produk=?";
            
            try {
            //buat koneksi ke database menggunakan method konek() dari class koneksi
            Connection conn = Koneksi.konek();
            //siapkan query SQL untuk dieksekusi dengan paramater
            PreparedStatement ps = conn.prepareStatement(sql); 
            
            ps.setString(1, namaProduk);//isi paramater pertama (?)  dengan nama produk 
            ps.setString(2, hargaProduk);//isi paramater kedua (?)  dengan harga produk
            ps.setString(3, kategoriProduk);//isi paramater ketiga (?)  dengan kategori produk
            ps.setString(4, deskProduk);//isi paramater keempat (?)  dengan deskripsi produk
            ps.setString(5, statusProduk);//isi paramater kelima (?)  dengan status produk
            ps.setString(6, idProdukTerpilih);//isi paramater keenam (?)  dengan id_produk yang dipilih
            
            int hasil = ps.executeUpdate();//jalankan query untuk mengubah data di database
            if(hasil>0){
                JOptionPane.showMessageDialog(null, "Data berhasil diubah");//tampilkan pesan bahwa data berhasil diubah
            }else{
                JOptionPane.showMessageDialog(null, "Data gagal diubah");//tampilkan pesan bahwa data berhasil diubah
            }
            load_tabel_produk("Semua");//memanggil method untuk memuat ulang semua data pada tabel produk       
            modeUbah = false; //modeUbah menjadi false (kemabli ke mode tambah produk) setelah user selesai mengubah data produk
            idProdukTerpilih=null;//reset idProdukTerpilih jika tidak ada produk yang dipilih untuk diubah
            reset();//memanggil method untuk mereset atau mengosongkan inputan 
        } catch (SQLException sQLException) {
            //jika terjadi kesalahan saat mengubah data , tampilkan pesan gagal
            JOptionPane.showMessageDialog(null, "Data gagal ditambahkan!");
        }
        }else {
            //jika modeUbah = false , maka user sedang di mode tambah produk
        String idProduk = IDProdukOtomatis(kategoriProduk);  //membuat id produk otomatis berdasarkan kategori produk 
        //Query SQL untuk INSERT kolom id_produk,nama_produk,status,harga_jual,kategori,deskripsi,id_pengguna di tabel produk
        String sql = "INSERT INTO produk(id_produk,nama_produk,status,harga_jual,kategori,deskripsi,id_pengguna) VALUES(?,?,?,?,?,?,?)";
        
        try {
            //buat koneksi ke database menggunakan method konek() dari class koneksi
            Connection conn = Koneksi.konek();
            //siapkan query SQL untuk dieksekusi dengan paramater
            PreparedStatement ps = conn.prepareStatement(sql); 
            
            ps.setString(1, idProduk);//isi parameter kesatu (?) dengan  id_produk (otomatis)
            ps.setString(2, namaProduk);//isi paramater kedua (?)  dengan nama produk
            ps.setString(3, statusProduk);//isi paramater ketiga (?)  dengan status produk
            ps.setString(4, hargaProduk);//isi paramater keempat (?)  dengan harga produk
            ps.setString(5, kategoriProduk);//isi paramater kelima (?)  dengan kategori produk
            ps.setString(6, deskProduk);//isi paramater keenam (?)  dengan deskripsi produk
            ps.setString(7, FrameLogin.getIdPengguna());//isi paramater ketujuh (?)  dengan id_pengguna yang sedang login
            
            ps.execute();//jalankan query untuk menyimpan data di database
            
            JOptionPane.showMessageDialog(null, "Data berhasil ditambahkan");//tampilkan pesan bahwa data berhasil disimpan
            
        } catch (SQLException sQLException) {
            //jika terjadi kesalahan saat menyimpan data , tampilkan pesan gagal
            JOptionPane.showMessageDialog(null, "Data gagal ditambahkan!");
        } 
        load_tabel_produk("Semua");//memanggil method untuk memuat ulang semua data pada tabel produk       
        modeUbah = false;//modeUbah menjadi false (kemabli ke mode tambah produk) setelah user selesai mengubah data produk
        idProdukTerpilih=null;//reset idProdukTerpilih jika tidak ada produk yang dipilih untuk diubah
        reset();//memanggil method untuk mereset atau mengosongkan inputan 
        }                                      
    }//GEN-LAST:event_btnSimpanProdukActionPerformed

    private void tblProdukMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblProdukMouseClicked
        // TODO add your handling code here:
        //ambil indeks baris yang di klik oleh pengguna di tabel tblProduk
        int barisYangDipilih=tblProduk.rowAtPoint(evt.getPoint());
        //ambil nilai dari kolom pertama (indeks 0) pada baris yang dipilih, yaitu 'kode_jur
        if(barisYangDipilih<0){ //jika baris yang dipilih lebih kecil dari 0 , maka tidak melakukan aksi apa apa
            return;
        }
        String idProduk =tblProduk.getValueAt(barisYangDipilih,0).toString();
        //ambil nilai dari kolom ke satu (indeks 0) pada baris yang dipilih, yaitu 'idProduk
        String namaProduk =tblProduk.getValueAt(barisYangDipilih,1).toString();
        //ambil nilai dari kolom kedua (indeks 1) pada baris yang dipilih, yaitu 'namaProduk
        String kategoriProduk =tblProduk.getValueAt(barisYangDipilih,2).toString();
        //ambil nilai dari kolom ketiga (indeks 2) pada baris yang dipilih, yaitu 'kategoriProduk
        String hargaProduk =tblProduk.getValueAt(barisYangDipilih,3).toString();
        //ambil nilai dari kolom keempat (indeks 3) pada baris yang dipilih, yaitu 'hargaProduk
        String statusProduk =tblProduk.getValueAt(barisYangDipilih,4).toString();
        //ambil nilai dari kolom kelima (indeks 4) pada baris yang dipilih, yaitu 'statusProduk
        
        tNamaProduk.setText(namaProduk);//tampilkan namaProduk di text field tNamaProduk
        cKategoriProduk.setSelectedItem(kategoriProduk);//tampilkan pilihan kategori produk di combo box cKategoriProduk
        tHargaProduk.setText(hargaProduk);//tampilkan hargaProduk di text field tHargaProduk
        btnStatusProduk.setSelected(statusProduk.equals("Tersedia"));//tampilkan btnStatusProduk jika tersedia, maka bernilai true

        String sql = "SELECT deskripsi FROM produk WHERE id_produk=?";//Query SQL untuk menampilkan deskripsi dari tabel produk berdasarkan id_produk
        
        try {
            //buat koneksi ke database menggunakan method konek() dari class koneksi
            Connection conn = Koneksi.konek();
            //siapkan query SQL untuk dieksekusi dengan paramater
            PreparedStatement ps = conn.prepareStatement(sql);
            
            ps.setString(1, idProduk);//isi paramater pertama (?)  dengan id_produk
            ResultSet rs = ps.executeQuery();//menjalankan perintah SELECT untuk menampilkan data deskripsi di database, dan ambil hasilnya
            if ( rs.next()){ ///jika data ditemukan , maka ambil deskripsi
                String deskripsiProduk = rs.getString("deskripsi");
                tDeskProduk.setText(deskripsiProduk); //tampilkan deskripsi yang diambil dalam tDeskProduk
            }
        } catch (SQLException sQLException) {
            //jika terjadi kesalahan saat mengambil data , tampilkan pesan gagal
            JOptionPane.showMessageDialog(null, " Deskripsi gagal di ambil!");
        }
        modeUbah =true; //user sedang mengubah data
        idProdukTerpilih = idProduk; //simpan idProduk sebagai idProdukTerpilih, saat melakukan menyimpan perubahan
        lblTambahProduk.setText("Mengubah Produk"); //menganti text dari Tambah produk menjadi "Mengubah Produk" pada lblTambahProduk
        btnSimpanProduk.setText("Mengubah Produk ");//menganti text dari Simpan Perubahan menjadi "Mengubah Produk" pada btnSimpanProduk
    }//GEN-LAST:event_tblProdukMouseClicked

    private void lblTambahProdukMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblTambahProdukMouseClicked
        // TODO add your handling code here:
        
    }//GEN-LAST:event_lblTambahProdukMouseClicked


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
