/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
//menentukan package tempat class PanelPengguna berada
package posseblakratu.view;

//mengimpor class FlatLineBorder untuk membuat border dengan sudut melengkung
import com.formdev.flatlaf.ui.FlatLineBorder;
//mengimpor class color untuk mengatur warna komponen
import java.awt.Color;
//mengimpor class Insets untuk mengatur jarak (padding) pada border
import java.awt.Insets;
//mengimpor class Connection untuk membuat koneksi ke database
import java.sql.Connection;
//mengimpor class PreparedStatement untuk menjalankan query SQL yang memiliki parameter
import java.sql.PreparedStatement;
//mengimpor class ResultSet untuk menampung hasil query SELECT
import java.sql.ResultSet;
//mengimpor class SQLException untuk menangani error yang terjadi pada database
import java.sql.SQLException;
//mengimpor JOptionPane untuk menampilkan dialog pesan kepada pengguna
import javax.swing.JOptionPane;
//mengimpor JPanel sebagai komponen panel pada tampilan
import javax.swing.JPanel;
//mengimpor DefaultTableModel untuk mengelola data yang ditampilkan pada JTable
import javax.swing.table.DefaultTableModel;
//mengimpor class Koneksi yang digunakan untuk menghubungkan aplikasi dengan database
import posseblakratu.config.Koneksi;

/**
 *
 * @author Al
 */

//mendefinisikan class PanelPengguna yang merupakan turunan dari JPanel
public class PanelPengguna extends javax.swing.JPanel {
    //menyimpan ID pengguna yang sedang dipilih pada tabel
    private String idPenggunaDipilih = "";
    //menandai apakah form sedang berada di mode edit(true) atau mode tambah data(false)
    private boolean sedangEdit = false;
    /**
     * Creates new form PanelPengguna
     */
    
    public PanelPengguna() {
        //membuat dan menginisialisasi seluruh komponen yang didesain di NetBeans
        initComponents();
        
        //memberikan border melengkung pada panel input Username
        panelLengkung(jPanel17);
        //memberikan border melengkung pada panel input Password
        panelLengkung(jPanel18);
        //memberikan border melengkung pada panel ComboBox Role
        panelLengkung(jPanel20);
        
        //memuat seluruh data pengguna dari database ke dalam JTable
        load_tabel_pengguna();
        //mengosongkan seluruh isian from dan mengembalikannya ke kondisi awal
        reset();
    }
    
    // Method untuk memberikan border melengkung pada panel
    void panelLengkung(JPanel p) {

        //mengatur border pada panel yang dikirim sbg parameter (p)
        p.setBorder(new FlatLineBorder(
                //memberikan jarak (padding) border sebesar 3 piksel di setiap sisi
                new Insets(3, 3, 3, 3),
                //mengatur warna border menggunakan kode warna hexadecimal
                Color.decode("#E7BDBB"),
                //mengatur ketebalan border menjadi 1 piksel
                1f,
                //mengatur radius sudut border menjadi 10 piksel agar terlihat melengkung
                10));

        //mengatur border melengkung pada panel utama sebelah kanan (form input)
        jPanel1.setBorder(new FlatLineBorder(
                //memberikan jarak (padding) sebesar 5 piksel di setiap sisi
                new Insets(5, 5, 5, 5),
                //menggunakan warna border yang sama
                Color.decode("#E7BDBB"),
                //ketebalan border 1 piksel
                1f,
                //radius sudut border 10 piksel
                10));

        //mengatur border melengkung pada panel daftar pengguna
        jPanel3.setBorder(new FlatLineBorder(
                //memberikan jarak (padding) sebesar 5 piksel di setiap sisi
                new Insets(5, 5, 5, 5),
                //menggunakan warna border yang sama
                Color.decode("#E7BDBB"),
                //ketebalan border 1 piksel
                1f,
                //radius sudut border 10 piksel
                10));
    }

    //method untuk mengembalikan form ke kondisi awal       
    void reset() {
        
        //memberi judul form menjadi "Tambah Pengguna"
        lblTambahPengguna.setText("Tambah Pengguna");
        //memberi tulisan tombol menjadi "Simpan Pengguna"
        btnSimpanPengguna.setText("Simpan Pengguna");
        //mengosongkan input username
        tUsername.setText("Contoh: kasir_depan");
        // placeholder dibuat jadi terlihat dan menampilkan placeholder
        tPasswordPengguna.setEchoChar((char) 0); 
        tPasswordPengguna.setText("Masukkan Password Baru"); 
        //mengosongkan pilihan pada ComboBox role
        cRolePengguna.setSelectedItem(null);
        //menjadikan status akun "Aktif" sebagai pilihan default
        btnPenggunaAktif.setSelected(true);
        //menghilangkan pilihan (selection) pada tabel
        tblPengguna.clearSelection();
        //mengubah mode form menjadi mode tambah data
        sedangEdit = false;
        //mengosongkan ID pengguna yang sebelumnya dipilih
        idPenggunaDipilih = "";
        
        
    }
        //metthod untuk membuat ID pengguna secara otomatis
        String generateIdPengguna(){
            
        //variabel untuk menyimpan ID pengguna terakhir dari database  
        String lastId = null;

        try{
            //membuat koneksi ke database
            Connection conn = Koneksi.konek();
            
            //Query untuk mengambil ID pengguna terakhir
            String sql = "SELECT id_pengguna FROM pengguna ORDER BY id_pengguna DESC LIMIT 1";

            //menyimpan query SQL
            PreparedStatement ps = conn.prepareStatement(sql);

            //menjalankan query
            ResultSet rs = ps.executeQuery();

            //jika data ditemukan
            if(rs.next()){
                //mengambil nilai ID pengguna terakhir
                lastId = rs.getString("id_pengguna");
            }

        }catch(SQLException e){
            //menampilkan pesan jika terjadi kesalahan saat mengambil ID
            JOptionPane.showMessageDialog(null,"Gagal membuat ID!");

        }
        //jika belum ada data pengguna di databasee
        if(lastId==null){
            //mengembalikan ID pertama
            return "PG01";
        }
        //mengambil bagian angka dari ID 
        int angka = Integer.parseInt(lastId.substring(2));

        //menambahkan angfka ID sebanyak 1
        angka++;

        //mengembalikan ID baru dengan format PG01, PG02, dan seterusnya
        return String.format("PG%02d", angka);

    }
        //method untuk menampilkan data pengguna dari database kedalam JTable
        void load_tabel_pengguna(){

       //membuat model tabel baru
       DefaultTableModel model = new DefaultTableModel();

       //menambahkan kolom Username pada tabel
       model.addColumn("Username");
       //menambahkan kolom Role pada tabel
       model.addColumn("Role");
       //menambahkan kolom Status pada tabel
       model.addColumn("Status");
       
       //Query untuk mengambil data username, role, dan status dari tabel pengguna
       String sql = "SELECT username,role,status FROM pengguna";

       try{

           //membuat koneksi ke database
           Connection conn = Koneksi.konek();

           //menyiapkan query SQL
           PreparedStatement ps = conn.prepareStatement(sql);

           //menjalankan query dan menyimpan hasilnya ke ResultSet
           ResultSet rs = ps.executeQuery();

           //melakukan perulangan selama masih ada data pada ResultSet
           while(rs.next()){

               //menyimpan data dari setiap baris hasil query ke dalam array Object
               Object[] baris = {
                   
                   //mengambil nilai username
                   rs.getString("username"),
                   //mengambil nilai Role
                   rs.getString("role"),
                   //mengambil nilai status
                   rs.getString("status")

               };

               //menambahkan data ke model tabel
               model.addRow(baris);

           }
       
       //menangkap error yang terjadi saat menjalankan operasi database
       }catch(SQLException e){
           
           //menampilkan pesan jika terjadi kesalahan saat mengambil data
           JOptionPane.showMessageDialog(null,"Gagal mengambil data pengguna!");

       }
       
       //menampilkan model yang telah berisi data ke JTable
       tblPengguna.setModel(model);
       tblPengguna.setColumnWidths("140,50,50");
            

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

        GbuttonStatus = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        lblTambahPengguna = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jPanel15 = new javax.swing.JPanel();
        btnSimpanPengguna = new javax.swing.JButton();
        jPanel9 = new javax.swing.JPanel();
        btnBatalPengguna = new javax.swing.JButton();
        btnHapusPengguna = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        jPanel12 = new javax.swing.JPanel();
        lblNamaProduk = new javax.swing.JLabel();
        jPanel17 = new javax.swing.JPanel();
        tUsername = new javax.swing.JTextField();
        jPanel13 = new javax.swing.JPanel();
        lblNamaProduk1 = new javax.swing.JLabel();
        jPanel18 = new javax.swing.JPanel();
        tPasswordPengguna = new javax.swing.JPasswordField();
        btnMata = new javax.swing.JToggleButton();
        jPanel14 = new javax.swing.JPanel();
        lblNamaProduk2 = new javax.swing.JLabel();
        jPanel20 = new javax.swing.JPanel();
        cRolePengguna = new javax.swing.JComboBox<>();
        jPanel16 = new javax.swing.JPanel();
        lblStatusProduk = new javax.swing.JLabel();
        btnPenggunaAktif = new javax.swing.JToggleButton();
        btnPenggunaNonaktif = new javax.swing.JToggleButton();
        jPanel3 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        lblDaftarProduk = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblPengguna = new jtablecustom.JTableCustom();

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

        lblTambahPengguna.setFont(new java.awt.Font("Plus Jakarta Sans SemiBold", 0, 18)); // NOI18N
        lblTambahPengguna.setText("Tambah  Pengguna");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(lblTambahPengguna, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblTambahPengguna, javax.swing.GroupLayout.DEFAULT_SIZE, 62, Short.MAX_VALUE)
        );

        jPanel1.add(jPanel2, java.awt.BorderLayout.PAGE_START);

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 20, 20, 20));
        jPanel4.setMinimumSize(new java.awt.Dimension(345, 130));
        jPanel4.setPreferredSize(new java.awt.Dimension(345, 130));
        jPanel4.setLayout(new java.awt.CardLayout());

        jPanel15.setBackground(new java.awt.Color(255, 255, 255));
        jPanel15.setLayout(new java.awt.GridLayout(2, 0, 0, 12));

        btnSimpanPengguna.setBackground(new java.awt.Color(214, 4, 39));
        btnSimpanPengguna.setFont(new java.awt.Font("Plus Jakarta Sans SemiBold", 0, 16)); // NOI18N
        btnSimpanPengguna.setForeground(new java.awt.Color(255, 255, 255));
        btnSimpanPengguna.setIcon(new javax.swing.ImageIcon(getClass().getResource("/posseblakratu/icon/IconSimpan.png"))); // NOI18N
        btnSimpanPengguna.setText("Simpan Pengguna");
        btnSimpanPengguna.setBorderPainted(false);
        btnSimpanPengguna.addActionListener(this::btnSimpanPenggunaActionPerformed);
        jPanel15.add(btnSimpanPengguna);

        jPanel9.setBackground(new java.awt.Color(255, 255, 255));
        jPanel9.setLayout(new java.awt.GridLayout(0, 2, 12, 0));

        btnBatalPengguna.setFont(new java.awt.Font("Plus Jakarta Sans SemiBold", 0, 16)); // NOI18N
        btnBatalPengguna.setText("Batal");
        btnBatalPengguna.setFocusable(false);
        btnBatalPengguna.addActionListener(this::btnBatalPenggunaActionPerformed);
        jPanel9.add(btnBatalPengguna);

        btnHapusPengguna.setFont(new java.awt.Font("Plus Jakarta Sans SemiBold", 0, 16)); // NOI18N
        btnHapusPengguna.setForeground(new java.awt.Color(214, 4, 39));
        btnHapusPengguna.setIcon(new javax.swing.ImageIcon(getClass().getResource("/posseblakratu/icon/Vector.png"))); // NOI18N
        btnHapusPengguna.setText("Hapus");
        btnHapusPengguna.setFocusable(false);
        btnHapusPengguna.addActionListener(this::btnHapusPenggunaActionPerformed);
        jPanel9.add(btnHapusPengguna);

        jPanel15.add(jPanel9);

        jPanel4.add(jPanel15, "card2");

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
        lblNamaProduk.setText("Username");
        lblNamaProduk.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 4, 0));
        jPanel12.add(lblNamaProduk, java.awt.BorderLayout.PAGE_START);

        jPanel17.setBackground(new java.awt.Color(255, 255, 255));
        jPanel17.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(231, 189, 187)));
        jPanel17.setLayout(new java.awt.GridBagLayout());

        tUsername.setFont(new java.awt.Font("Plus Jakarta Sans", 0, 16)); // NOI18N
        tUsername.setForeground(new java.awt.Color(92, 62, 60));
        tUsername.setBorder(null);
        tUsername.setMargin(new java.awt.Insets(10, 10, 10, 6));
        tUsername.setPreferredSize(new java.awt.Dimension(126, 19));
        tUsername.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                tUsernameFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                tUsernameFocusLost(evt);
            }
        });
        tUsername.addActionListener(this::tUsernameActionPerformed);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 278;
        gridBagConstraints.ipady = 19;
        gridBagConstraints.insets = new java.awt.Insets(1, 18, 1, 12);
        jPanel17.add(tUsername, gridBagConstraints);

        jPanel12.add(jPanel17, java.awt.BorderLayout.CENTER);

        jPanel11.add(jPanel12);

        jPanel13.setBackground(new java.awt.Color(255, 255, 255));
        jPanel13.setLayout(new java.awt.BorderLayout());

        lblNamaProduk1.setBackground(new java.awt.Color(255, 255, 255));
        lblNamaProduk1.setFont(new java.awt.Font("Plus Jakarta Sans", 1, 14)); // NOI18N
        lblNamaProduk1.setText("Password");
        lblNamaProduk1.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 4, 0));
        jPanel13.add(lblNamaProduk1, java.awt.BorderLayout.PAGE_START);

        jPanel18.setBackground(new java.awt.Color(255, 255, 255));
        jPanel18.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(231, 189, 187)));

        tPasswordPengguna.setFont(new java.awt.Font("Plus Jakarta Sans", 0, 16)); // NOI18N
        tPasswordPengguna.setForeground(new java.awt.Color(92, 62, 60));
        tPasswordPengguna.setText("jPasswordField1");
        tPasswordPengguna.setBorder(null);
        tPasswordPengguna.setPreferredSize(new java.awt.Dimension(90, 50));
        tPasswordPengguna.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                tPasswordPenggunaFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                tPasswordPenggunaFocusLost(evt);
            }
        });

        btnMata.setIcon(new javax.swing.ImageIcon(getClass().getResource("/posseblakratu/icon/IconMata.png"))); // NOI18N
        btnMata.setBorder(null);
        btnMata.setContentAreaFilled(false);
        btnMata.setDisabledIcon(new javax.swing.ImageIcon(getClass().getResource("/posseblakratu/icon/IconMata.png"))); // NOI18N
        btnMata.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/posseblakratu/icon/DisIconMata2.png"))); // NOI18N
        btnMata.addActionListener(this::btnMataActionPerformed);

        javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(tPasswordPengguna, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnMata, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(72, 72, 72))
        );
        jPanel18Layout.setVerticalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tPasswordPengguna, javax.swing.GroupLayout.DEFAULT_SIZE, 44, Short.MAX_VALUE)
                    .addComponent(btnMata, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 0, 0))
        );

        jPanel13.add(jPanel18, java.awt.BorderLayout.CENTER);

        jPanel11.add(jPanel13);

        jPanel14.setBackground(new java.awt.Color(255, 255, 255));
        jPanel14.setLayout(new java.awt.BorderLayout());

        lblNamaProduk2.setBackground(new java.awt.Color(255, 255, 255));
        lblNamaProduk2.setFont(new java.awt.Font("Plus Jakarta Sans", 1, 14)); // NOI18N
        lblNamaProduk2.setText("Role");
        lblNamaProduk2.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 4, 0));
        jPanel14.add(lblNamaProduk2, java.awt.BorderLayout.PAGE_START);

        jPanel20.setBackground(new java.awt.Color(255, 255, 255));
        jPanel20.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(231, 189, 187)));
        jPanel20.setLayout(new java.awt.BorderLayout());

        cRolePengguna.setFont(new java.awt.Font("Plus Jakarta Sans", 0, 16)); // NOI18N
        cRolePengguna.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Kasir", "Owner" }));
        cRolePengguna.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 12, 1, 12));
        jPanel20.add(cRolePengguna, java.awt.BorderLayout.CENTER);

        jPanel14.add(jPanel20, java.awt.BorderLayout.CENTER);

        jPanel11.add(jPanel14);

        jPanel16.setBackground(new java.awt.Color(255, 255, 255));

        lblStatusProduk.setFont(new java.awt.Font("Plus Jakarta Sans", 1, 14)); // NOI18N
        lblStatusProduk.setText("Status Akun");

        GbuttonStatus.add(btnPenggunaAktif);
        btnPenggunaAktif.setFont(new java.awt.Font("Plus Jakarta Sans", 0, 16)); // NOI18N
        btnPenggunaAktif.setIcon(new javax.swing.ImageIcon(getClass().getResource("/posseblakratu/icon/statusNonaktif.png"))); // NOI18N
        btnPenggunaAktif.setText("Aktif");
        btnPenggunaAktif.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 3, 1, 1));
        btnPenggunaAktif.setContentAreaFilled(false);
        btnPenggunaAktif.setDisabledIcon(new javax.swing.ImageIcon(getClass().getResource("/posseblakratu/icon/statusNonaktif.png"))); // NOI18N
        btnPenggunaAktif.setDisabledSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/posseblakratu/icon/statusAktif.png"))); // NOI18N
        btnPenggunaAktif.setFocusCycleRoot(true);
        btnPenggunaAktif.setFocusPainted(false);
        btnPenggunaAktif.setHideActionText(true);
        btnPenggunaAktif.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnPenggunaAktif.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnPenggunaAktif.setIconTextGap(10);
        btnPenggunaAktif.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/posseblakratu/icon/statusAktif.png"))); // NOI18N

        GbuttonStatus.add(btnPenggunaNonaktif);
        btnPenggunaNonaktif.setFont(new java.awt.Font("Plus Jakarta Sans", 0, 16)); // NOI18N
        btnPenggunaNonaktif.setIcon(new javax.swing.ImageIcon(getClass().getResource("/posseblakratu/icon/statusNonaktif.png"))); // NOI18N
        btnPenggunaNonaktif.setText("Nonaktif");
        btnPenggunaNonaktif.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 3, 1, 1));
        btnPenggunaNonaktif.setContentAreaFilled(false);
        btnPenggunaNonaktif.setDisabledIcon(new javax.swing.ImageIcon(getClass().getResource("/posseblakratu/icon/statusNonaktif.png"))); // NOI18N
        btnPenggunaNonaktif.setDisabledSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/posseblakratu/icon/statusAktif.png"))); // NOI18N
        btnPenggunaNonaktif.setFocusCycleRoot(true);
        btnPenggunaNonaktif.setFocusPainted(false);
        btnPenggunaNonaktif.setHideActionText(true);
        btnPenggunaNonaktif.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnPenggunaNonaktif.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnPenggunaNonaktif.setIconTextGap(10);
        btnPenggunaNonaktif.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/posseblakratu/icon/statusAktif.png"))); // NOI18N

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addComponent(btnPenggunaAktif, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnPenggunaNonaktif, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 90, Short.MAX_VALUE))
            .addComponent(lblStatusProduk, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addComponent(lblStatusProduk, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnPenggunaAktif, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnPenggunaNonaktif, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)))
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
        jPanel5.setPreferredSize(new java.awt.Dimension(681, 63));
        jPanel5.setLayout(new java.awt.BorderLayout());

        jPanel8.setBackground(new java.awt.Color(255, 255, 255));
        jPanel8.setPreferredSize(new java.awt.Dimension(681, 63));

        lblDaftarProduk.setFont(new java.awt.Font("Plus Jakarta Sans SemiBold", 0, 24)); // NOI18N
        lblDaftarProduk.setForeground(new java.awt.Color(24, 26, 46));
        lblDaftarProduk.setText("Daftar Pengguna");
        lblDaftarProduk.setName(""); // NOI18N
        lblDaftarProduk.setPreferredSize(new java.awt.Dimension(198, 63));

        jPanel10.setBackground(new java.awt.Color(255, 255, 255));
        jPanel10.setBorder(javax.swing.BorderFactory.createEmptyBorder(16, 0, 16, 0));
        jPanel10.setPreferredSize(new java.awt.Dimension(150, 60));
        jPanel10.setLayout(new java.awt.BorderLayout());

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(lblDaftarProduk, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 225, Short.MAX_VALUE)
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

        tblPengguna.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"kasir1", "Kasir", "Aktif"}
            },
            new String [] {
                "Username", "Role", "Status"
            }
        ));
        tblPengguna.setCellPaddingLeft(25);
        tblPengguna.setCellPaddingRight(25);
        tblPengguna.setCenterColumns("1,2");
        tblPengguna.setColumnWidths("140,50,50");
        tblPengguna.setFont(new java.awt.Font("Plus Jakarta Sans", 0, 14)); // NOI18N
        tblPengguna.setHeaderPaddingLeft(25);
        tblPengguna.setHeaderPaddingRight(25);
        tblPengguna.setLeftColumns("0");
        tblPengguna.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblPenggunaMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblPengguna);

        jPanel6.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jPanel3.add(jPanel6, java.awt.BorderLayout.CENTER);

        add(jPanel3, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void tUsernameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tUsernameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tUsernameActionPerformed

    //method yang dijalankan ketika pengguna mengklik salah satu baris pada tabel
    private void tblPenggunaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblPenggunaMouseClicked
        // TODO add your handling code here:
        //Mendapatkan indeks baris yang diklik oleh pengguna
        int baris = tblPengguna.rowAtPoint(evt.getPoint());
        
        //jika tidak ada baris yang dipilih, maka keluar dari method
        if(baris==-1){
            return;
        }

        //mengambil username dari kolom pertama pada baris yang dipilih
        String username = tblPengguna.getValueAt(baris,0).toString();

        //Query untuk mengambil seluruh data pengguna berdasarkan username
        String sql = "SELECT * FROM pengguna WHERE username=?";

        try{

            //membuat koneksi ke database
            Connection conn = Koneksi.konek();

            //menyiapkan query SQL
            PreparedStatement ps = conn.prepareStatement(sql);

            //mengisi parameter username pada query
            ps.setString(1, username);

            //menjalankan query
            ResultSet rs = ps.executeQuery();

            //jika data pengguna ditemukan
            if(rs.next()){

            //menyimpan ID pengguna yang dipilih
            idPenggunaDipilih = rs.getString("id_pengguna");

            //mengubah mode form menjadi mode edit
            sedangEdit = true;

            //mengubah judul form menjadi "Edit Pengguna"
            lblTambahPengguna.setText("Edit Pengguna");
            //mengubah teks tombol menjadi "Simpan Perubahan"
            btnSimpanPengguna.setText("Simpan Perubahan");

            //menampilkan username ke dalam field username
            tUsername.setText(rs.getString("username"));
            //menghilangkan karakter penyamaran password agar teks dapat terlihat
            tPasswordPengguna.setEchoChar((char) 0);
            //menampilkan placeholder agar pengguna memasukkan password baru
            tPasswordPengguna.setText("Masukkan Password Baru");

            //memilih role sesuai data yang ada di database
            cRolePengguna.setSelectedItem(rs.getString("role"));

            // Mengecek status akun pengguna
            if(rs.getString("status").equals("Aktif")){
                // Memilih tombol status Aktif
                btnPenggunaAktif.setSelected(true);
                
            }else{
                //memilih tombol status Nonaktif
                btnPenggunaNonaktif.setSelected(true);
            }

        }
        
        //menangkap error yang terjadi saat menjalankan operasi database 
        }catch(SQLException e){
            //menampilkan pesan kepada pengguna bahwa proses pengambilan data gagal
            JOptionPane.showMessageDialog(null,"Gagal mengambil data!");

        }

      
    }//GEN-LAST:event_tblPenggunaMouseClicked

    //method yang dijalankan ketika tombol mata diklik
    private void btnMataActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMataActionPerformed
        // TODO add your handling code here:
        
        //mengecek apakah tombol mata dalam keadaan dipilih
        if (btnMata.isSelected()) {
            
            //tampilkan teks password dengan menghilangkan karakter penyamaran
            tPasswordPengguna.setEchoChar((char) 0);
            
        } else {
        //sembunyikan teks password dengan karakter bullet (*)
            tPasswordPengguna.setEchoChar('•');
        }
    }//GEN-LAST:event_btnMataActionPerformed

    //Method yang dijalankan ketika tombol Hapus Pengguna diklik
    private void btnHapusPenggunaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHapusPenggunaActionPerformed
           // TODO add your handling code here:
           
           //mengecek apakah pengguna sedang memilih data untuk diedit
           if (!sedangEdit) {

               //menampilkan pesan bahwa pengguna harus memilih data terlebih dahulu
               JOptionPane.showMessageDialog(null, "Pilih data terlebih dahulu!");

               //menghentikan proses penghapusan
               return;
           }

           //menampilkan dialog konfirmasi sebelum menghapus data
           int konfirmasi = JOptionPane.showConfirmDialog(
                   null,
                   "Yakin ingin menghapus pengguna?",
                   "Konfirmasi",
                   JOptionPane.YES_NO_OPTION);

           //jika pengguna memilih selain tombol "Yes", proses dibatalkan
           if (konfirmasi != JOptionPane.YES_OPTION) {
               return;
           }

           //query SQL untuk menghapus data pengguna berdasarkan ID
           String sql = "DELETE FROM pengguna WHERE id_pengguna=?";

           try {

               //membuat koneksi ke database
               Connection conn = Koneksi.konek();

               //menyiapkan query SQL
               PreparedStatement ps = conn.prepareStatement(sql);

               //mengisi parameter query dengan ID pengguna yang akan dihapus
               ps.setString(1, idPenggunaDipilih);

               //menjalankan perintah DELETE
               ps.execute();

               //menampilkan pesan bahwa data berhasil dihapus
               JOptionPane.showMessageDialog(null, "Data berhasil dihapus!");

           //menangkap error yang terjadi saat menjalankan operasi database
           } catch (SQLException e) {

               //menampilkan pesan bahwa proses penghapusan data gagal
               JOptionPane.showMessageDialog(null, "Data gagal dihapus!");

           }

           //memuat kembali data pengguna ke dalam tabel
           load_tabel_pengguna();

           //mengembalikan form ke kondisi awal
           reset();

        
    }//GEN-LAST:event_btnHapusPenggunaActionPerformed

    //method yang dijalankan ketika tombol Batal diklik
    private void btnBatalPenggunaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBatalPenggunaActionPerformed
        // TODO add your handling code here:
        
        //mengembalikan form ke kondisi awal
        reset();
    }//GEN-LAST:event_btnBatalPenggunaActionPerformed

    //method yang dijalankan ketika tombol Simpan Pengguna diklik
    private void btnSimpanPenggunaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSimpanPenggunaActionPerformed
        // TODO add your handling code here:

        //mengambil data username dari field input
        String username = tUsername.getText();

        //mengambil data password dari field input
        String password = tPasswordPengguna.getText();

        //ambil input dari combo box, jika tidak ada yang di select, ganti dengan string kosong
        String role = cRolePengguna.getSelectedItem() != null ? cRolePengguna.getSelectedItem().toString() : "";

        //mendeklarasikan variabel untuk menyimpan status pengguna
        String status;

        //mengecek status akun yang dipilih
        if (btnPenggunaAktif.isSelected()) {

            //jika tombol Aktif dipilih, status menjadi "Aktif"
            status = "Aktif";

        } else {

            //jika tidak, status menjadi "Tidak Aktif"
            status = "Tidak Aktif";
        }

        //memastikan username tidak kosong
        if (username.isEmpty()) {

            //menampilkan pesan bahwa username harus diisi
            JOptionPane.showMessageDialog(null, "Semua field harus diisi!");

            //mengarahkan kursor ke field username
            tUsername.requestFocus();

            //menghentikan proses penyimpanan
            return;
        }

        //memastikan password tidak kosong
        if (password.isEmpty()) {

            //menampilkan pesan bahwa password harus diisi
            JOptionPane.showMessageDialog(null, "Semua field harus diisi!");

            //mengarahkan kursor ke field password
            tPasswordPengguna.requestFocus();

            //menghentikan proses penyimpanan
            return;
        }
        
        //memastikan role tidak kosong
        if (role.isEmpty()) {

            //menampilkan pesan bahwa password harus diisi
            JOptionPane.showMessageDialog(null, "Semua field harus diisi!");

            //mengarahkan kursor ke field password
            cRolePengguna.requestFocus();

            //menghentikan proses penyimpanan
            return;
        }

        //mengecek apakah form sedang dalam mode edit
        if (sedangEdit) {

            //Query SQL untuk mengubah data pengguna
            String sql =
            "UPDATE pengguna SET username=?, password=MD5(?), role=?, status=? WHERE id_pengguna=?";

            try {

                //membuat koneksi ke database
                Connection conn = Koneksi.konek();

                //menyiapkan query SQL
                PreparedStatement ps = conn.prepareStatement(sql);

                //mengisi parameter username
                ps.setString(1, username);
                //mengisi parameter password yang akan di-hash menggunakan MD5
                ps.setString(2, password);
                //mengisi parameter role
                ps.setString(3, role);
                //mengisi parameter status
                ps.setString(4, status);
                //mengisi parameter ID pengguna yang akan diubah
                ps.setString(5, idPenggunaDipilih);

                //menjalankan perintah UPDATE
                ps.execute();

                //menampilkan pesan bahwa data berhasil diubah
                JOptionPane.showMessageDialog(null, "Data berhasil diubah!");

            //menangkap error yang terjadi saat menjalankan operasi database
            } catch (SQLException e) {

                //menampilkan pesan bahwa proses perubahan data gagal
                JOptionPane.showMessageDialog(null, "Data gagal diubah!");

            }

        } else {

            //membuat ID pengguna baru secara otomatis
            String idPengguna = generateIdPengguna();

            //Query SQL untuk menambahkan data pengguna baru
            String sql =
            "INSERT INTO pengguna(id_pengguna, username, password, role, status) VALUES(?, ?, MD5(?), ?, ?)";

            try {

                //membuat koneksi ke database
                Connection conn = Koneksi.konek();

                //menyiapkan query SQL
                PreparedStatement ps = conn.prepareStatement(sql);

                //mengisi parameter ID pengguna
                ps.setString(1, idPengguna);
                //mengisi parameter username
                ps.setString(2, username);
                //mengisi parameter password yang akan di-hash menggunakan MD5
                ps.setString(3, password);
                //mengisi parameter role
                ps.setString(4, role);
                //mengisi parameter status
                ps.setString(5, status);
                
                //menjalankan perintah INSERT
                ps.execute();

                //menampilkan pesan bahwa data berhasil disimpan
                JOptionPane.showMessageDialog(null, "Data berhasil disimpan!");

            //menangkap error yang terjadi saat menjalankan operasi database
            } catch (SQLException e) {

                //menampilkan pesan bahwa proses penyimpanan data gagal
                JOptionPane.showMessageDialog(null, "Data gagal disimpan!");

            }

        }

        //memuat kembali data pengguna ke dalam tabel
        load_tabel_pengguna();

        //mengembalikan form ke kondisi awal
        reset();

    

    }//GEN-LAST:event_btnSimpanPenggunaActionPerformed

    private void tUsernameFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tUsernameFocusGained
        // TODO add your handling code here:
        //ambil teks yang saat ini ada di field username
        String username = tUsername.getText();

        //jika masih berisi placeholder, kosongkan agar pengguna bisa langsung mengetik
        if (username.equals("Contoh: kasir_depan")) {
            tUsername.setText("");
        }
    }//GEN-LAST:event_tUsernameFocusGained

    private void tUsernameFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tUsernameFocusLost
        // TODO add your handling code here:
        String username = tUsername.getText();

        //jika kosong kembalikan tulisan placeholder
        if (username.equals("") || username.equals("Contoh: kasir_depan")) {
            tUsername.setText("Contoh: kasir_depan");
        }
    }//GEN-LAST:event_tUsernameFocusLost

    private void tPasswordPenggunaFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tPasswordPenggunaFocusGained
        // TODO add your handling code here:
             //ambil teks password dan ubah dari char[] menjadi String
        String password = String.valueOf(tPasswordPengguna.getPassword());

        //jika masih berisi placeholder kosongkan dan aktifkan karakter bullet
        if (password.equals("Masukkan Password Baru")) {
            tPasswordPengguna.setText("");
            tPasswordPengguna.setEchoChar('•');
        }
    }//GEN-LAST:event_tPasswordPenggunaFocusGained

    private void tPasswordPenggunaFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tPasswordPenggunaFocusLost
        // TODO add your handling code here:
            String password = String.valueOf(tPasswordPengguna.getPassword());

        //jika kosong kembalikan ke placeholder
        if (password.isEmpty()) {
            //nonaktifkan echo char agar teks placeholder bisa terbaca
            tPasswordPengguna.setEchoChar((char) 0);
            //tampilkan kembali teks placeholder
            tPasswordPengguna.setText("Masukkan Password Baru");
        }
    }//GEN-LAST:event_tPasswordPenggunaFocusLost


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup GbuttonStatus;
    private javax.swing.JButton btnBatalPengguna;
    private javax.swing.JButton btnHapusPengguna;
    private javax.swing.JToggleButton btnMata;
    private javax.swing.JToggleButton btnPenggunaAktif;
    private javax.swing.JToggleButton btnPenggunaNonaktif;
    private javax.swing.JButton btnSimpanPengguna;
    private javax.swing.JComboBox<String> cRolePengguna;
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
    private javax.swing.JLabel lblDaftarProduk;
    private javax.swing.JLabel lblNamaProduk;
    private javax.swing.JLabel lblNamaProduk1;
    private javax.swing.JLabel lblNamaProduk2;
    private javax.swing.JLabel lblStatusProduk;
    private javax.swing.JLabel lblTambahPengguna;
    private javax.swing.JPasswordField tPasswordPengguna;
    private javax.swing.JTextField tUsername;
    private jtablecustom.JTableCustom tblPengguna;
    // End of variables declaration//GEN-END:variables
}
