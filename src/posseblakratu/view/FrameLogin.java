/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package posseblakratu.view;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.ui.FlatLineBorder;
import java.awt.Color;
import java.awt.Insets;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import posseblakratu.config.Koneksi;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import java.util.prefs.Preferences;

/**
 *
 * @author Al
 */
public final class FrameLogin extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(FrameLogin.class.getName());

    // Session data pengguna yang sedang login
    private static String idPengguna;
    private static String username;
    private static String role;

    //mengambil id pengguna yang sedang login
    public static String getIdPengguna() {
        return idPengguna;
    }

    //mengambil username yang sedang login
    public static String getUsername() {
        return username;
    }

    //mengambil role pengguna yang sedang login
    public static String getRole() {
        return role;
    }

    //kunci preferences untuk menyimpan sesi login
    private static final String PREF_USERNAME = "sesi_username";
    private static final String PREF_PASSWORD = "sesi_password";
    private static final String PREF_ROLE = "sesi_role";

    //method untuk menyimpan data login ke preferences sistem
    static void simpanSesi(String username, String password, String role) {
        Preferences pref = Preferences.userNodeForPackage(FrameLogin.class);
        pref.put(PREF_USERNAME, username);
        pref.put(PREF_PASSWORD, password);
        pref.put(PREF_ROLE, role);
    }

    //method untuk menghapus data sesi dari preferences sistem
    public static void hapusSesi() {
        Preferences pref = Preferences.userNodeForPackage(FrameLogin.class);
        pref.remove(PREF_USERNAME);
        pref.remove(PREF_PASSWORD);
        pref.remove(PREF_ROLE);
    }

    //method untuk memuat sesi tersimpan ke dalam field login
    //method untuk memuat dan memverifikasi sesi tersimpan
    //mengembalikan true jika sesi valid dan session sudah diisi
    public static boolean muatSesi() {
        Preferences pref = Preferences.userNodeForPackage(FrameLogin.class);

        //ambil data sesi yang tersimpan, kembalikan string kosong jika tidak ada
        String savedUsername = pref.get(PREF_USERNAME, "");
        String savedPassword = pref.get(PREF_PASSWORD, "");
        String savedRole = pref.get(PREF_ROLE, "");

        //jika tidak ada sesi tersimpan, kembalikan false
        if (savedUsername.isEmpty()) {
            return false;
        }

        try {
            //query untuk memverifikasi sesi yang tersimpan masih valid
            String sql = "SELECT * FROM pengguna WHERE username=? AND password=MD5(?) AND role=? AND status='Aktif'";

            Connection conn = Koneksi.konek();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, savedUsername);
            ps.setString(2, savedPassword);
            ps.setString(3, savedRole);
            ResultSet rs = ps.executeQuery();

            //jika data masih valid, simpan ke session dan kembalikan true
            if (rs.next()) {
                FrameLogin.idPengguna = rs.getString("id_pengguna");
                FrameLogin.username = rs.getString("username");
                FrameLogin.role = rs.getString("role");
                return true;
            }

            //jika sesi sudah tidak valid, hapus sesi dan kembalikan false
            hapusSesi();
            return false;

        } catch (SQLException sQLException) {
            //jika gagal verifikasi, anggap sesi tidak valid
            return false;
        }
    }

    /**
     * Creates new form FrameLogin
     */
    public FrameLogin() {
        initComponents();

        //memanggil panel melengkung pada setiap panel yg di lengkungkan
        panelLengkung(usrContainer);
        panelLengkung(passContainer);
        panelLengkung(roleContainer);

        tPassword.setEchoChar((char) 0); //nonaktifkan echo char agar teks placeholder dapat terbaca
        tPassword.setText("Masukkan Password"); //isi placeholder password

        //memanggil method untuk menampilkan logo toko dari database
        loadLogin();
    }

    //method untuk mengatur style panel agar terlihat melengkung
    //menerapkan border dengan warna dan radius tertentu
    void panelLengkung(JPanel p) {

        //mengatur border panel utama yang dikirim sebagai parameter
        p.setBorder(new FlatLineBorder(
                new Insets(0, 0, 0, 0),
                Color.decode("#E7BDBB"),
                1f,
                10));

        //mengatur border container login agar lebih melengkung
        containerLogin.setBorder(new FlatLineBorder(
                new Insets(0, 0, 0, 0),
                Color.decode("#E7BDBB"),
                1f,
                20));
    }


    //membuat method load login untuk menampilkan logo dan nama toko dari database
    void loadLogin() {

        //query SQL untuk mengambil data pengaturan toko dari database
        String sql = "SELECT * FROM pengaturan LIMIT 1";

        try {
            //membuka koneksi ke database
            Connection conn = Koneksi.konek();

            //menyiapkan statement SQL
            PreparedStatement ps = conn.prepareStatement(sql);

            //menjalankan query dan menyimpan hasilnya
            ResultSet rs = ps.executeQuery();

            //jika data ditemukan
            if (rs.next()) {

                //menampilkan nama toko dari database ke label lblToko
                lblToko.setText(rs.getString("nama_umkm"));

                //mengambil path logo dari database
                String logo = rs.getString("logo_toko");

                //jika path logo tidak kosong, tampilkan gambar ke label logo
                if (logo != null && !logo.isEmpty()) {

                    //membuat ImageIcon dari path logo
                    ImageIcon icon = new ImageIcon(logo);

                    //mengambil ukuran dari icon default yang sudah terpasang di label
                    int w = lblLogo.getIcon() != null ? lblLogo.getIcon().getIconWidth() : 150;
                    int h = lblLogo.getIcon() != null ? lblLogo.getIcon().getIconHeight() : 150;

                    //mengubah ukuran gambar agar sesuai dengan label lblLogo
                    Image image = icon.getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH);

                    //menghapus teks pada label logo
                    lblLogo.setText("");

                    //menampilkan gambar logo ke dalam label lblLogo
                    lblLogo.setIcon(new ImageIcon(image));

                } else {

                    //jika tidak ada logo, tampilkan teks default dan hapus icon
                    lblLogo.setText("Logo Tidak Ada");
                    lblLogo.setIcon(null);

                }
            }

        } catch (SQLException sQLException) {
            //menampilkan pesan error jika gagal mengambil data
            JOptionPane.showMessageDialog(null, "gagal mengambil data!");
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
        java.awt.GridBagConstraints gridBagConstraints;

        mainPanel = new javax.swing.JPanel();
        rightContainer = new javax.swing.JPanel();
        containerLogin = new javax.swing.JPanel();
        jJpanelll = new javax.swing.JPanel();
        usrContainer = new javax.swing.JPanel();
        tUsername = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        Jpanell = new javax.swing.JPanel();
        lblPassword = new javax.swing.JLabel();
        passContainer = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        tPassword = new javax.swing.JPasswordField();
        btnMata = new javax.swing.JToggleButton();
        Jpanelll = new javax.swing.JPanel();
        lblMasukSebagai = new javax.swing.JLabel();
        roleContainer = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        cRole = new javax.swing.JComboBox<>();
        lblUsernameLogin = new javax.swing.JLabel();
        btnLogin = new javax.swing.JButton();
        txtCopyright = new javax.swing.JLabel();
        leftContainer = new javax.swing.JPanel();
        lblLogo = new javax.swing.JLabel();
        lblToko = new javax.swing.JLabel();
        tDetail = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        mainPanel.setBackground(new java.awt.Color(252, 249, 255));
        mainPanel.setMinimumSize(new java.awt.Dimension(1280, 720));
        mainPanel.setPreferredSize(new java.awt.Dimension(1280, 720));
        mainPanel.setLayout(new java.awt.GridBagLayout());

        rightContainer.setBackground(new java.awt.Color(252, 249, 255));
        rightContainer.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 0, 22));

        containerLogin.setBackground(new java.awt.Color(255, 255, 255));
        containerLogin.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(231, 189, 187)));

        jJpanelll.setBackground(new java.awt.Color(255, 255, 255));
        jJpanelll.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        usrContainer.setBackground(new java.awt.Color(255, 255, 255));
        usrContainer.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(231, 189, 187)));

        tUsername.setFont(new java.awt.Font("Plus Jakarta Sans", 0, 16)); // NOI18N
        tUsername.setBorder(null);
        tUsername.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                tUsernameFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                tUsernameFocusLost(evt);
            }
        });

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setIcon(new FlatSVGIcon("posseblakratu/icon/IconUsr.svg")
        );

        javax.swing.GroupLayout usrContainerLayout = new javax.swing.GroupLayout(usrContainer);
        usrContainer.setLayout(usrContainerLayout);
        usrContainerLayout.setHorizontalGroup(
            usrContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(usrContainerLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tUsername, javax.swing.GroupLayout.DEFAULT_SIZE, 379, Short.MAX_VALUE)
                .addContainerGap())
        );
        usrContainerLayout.setVerticalGroup(
            usrContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(usrContainerLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(usrContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tUsername, javax.swing.GroupLayout.DEFAULT_SIZE, 37, Short.MAX_VALUE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jJpanelll.add(usrContainer, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 430, -1));

        Jpanell.setBackground(new java.awt.Color(255, 255, 255));
        Jpanell.setAlignmentX(1.0F);
        Jpanell.setAlignmentY(1.0F);

        lblPassword.setFont(new java.awt.Font("Plus Jakarta Sans", 1, 14)); // NOI18N
        lblPassword.setText("Password");

        passContainer.setBackground(new java.awt.Color(255, 255, 255));
        passContainer.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(231, 189, 187)));

        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setIcon(new FlatSVGIcon("posseblakratu/icon/IconPass.svg")
        );

        tPassword.setFont(new java.awt.Font("Plus Jakarta Sans", 0, 16)); // NOI18N
        tPassword.setBorder(null);
        tPassword.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                tPasswordFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                tPasswordFocusLost(evt);
            }
        });

        btnMata.setIcon(new javax.swing.ImageIcon(getClass().getResource("/posseblakratu/icon/IconMata.png"))); // NOI18N
        btnMata.setBorder(null);
        btnMata.setContentAreaFilled(false);
        btnMata.setDisabledIcon(new javax.swing.ImageIcon(getClass().getResource("/posseblakratu/icon/IconMata.png"))); // NOI18N
        btnMata.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/posseblakratu/icon/DisIconMata2.png"))); // NOI18N
        btnMata.addActionListener(this::btnMataActionPerformed);

        javax.swing.GroupLayout passContainerLayout = new javax.swing.GroupLayout(passContainer);
        passContainer.setLayout(passContainerLayout);
        passContainerLayout.setHorizontalGroup(
            passContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(passContainerLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 265, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnMata, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        passContainerLayout.setVerticalGroup(
            passContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(passContainerLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(passContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, 37, Short.MAX_VALUE)
                    .addComponent(tPassword))
                .addContainerGap())
            .addComponent(btnMata, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout JpanellLayout = new javax.swing.GroupLayout(Jpanell);
        Jpanell.setLayout(JpanellLayout);
        JpanellLayout.setHorizontalGroup(
            JpanellLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblPassword, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(passContainer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        JpanellLayout.setVerticalGroup(
            JpanellLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JpanellLayout.createSequentialGroup()
                .addComponent(lblPassword)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(passContainer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        Jpanelll.setBackground(new java.awt.Color(255, 255, 255));
        Jpanelll.setAlignmentX(1.0F);
        Jpanelll.setAlignmentY(1.0F);

        lblMasukSebagai.setFont(new java.awt.Font("Plus Jakarta Sans", 1, 14)); // NOI18N
        lblMasukSebagai.setText("Masuk Sebagai");

        roleContainer.setBackground(new java.awt.Color(255, 255, 255));
        roleContainer.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(231, 189, 187)));

        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setIcon(new FlatSVGIcon("posseblakratu/icon/IconRole.svg")
        );

        cRole.setFont(new java.awt.Font("Plus Jakarta Sans", 0, 16)); // NOI18N
        cRole.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Kasir", "Owner" }));
        cRole.setBorder(null);

        javax.swing.GroupLayout roleContainerLayout = new javax.swing.GroupLayout(roleContainer);
        roleContainer.setLayout(roleContainerLayout);
        roleContainerLayout.setHorizontalGroup(
            roleContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(roleContainerLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cRole, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        roleContainerLayout.setVerticalGroup(
            roleContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(roleContainerLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(roleContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, 39, Short.MAX_VALUE)
                    .addComponent(cRole))
                .addContainerGap())
        );

        javax.swing.GroupLayout JpanelllLayout = new javax.swing.GroupLayout(Jpanelll);
        Jpanelll.setLayout(JpanelllLayout);
        JpanelllLayout.setHorizontalGroup(
            JpanelllLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblMasukSebagai, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(roleContainer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        JpanelllLayout.setVerticalGroup(
            JpanelllLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JpanelllLayout.createSequentialGroup()
                .addComponent(lblMasukSebagai)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(roleContainer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        lblUsernameLogin.setFont(new java.awt.Font("Plus Jakarta Sans", 1, 14)); // NOI18N
        lblUsernameLogin.setText("Username");

        btnLogin.setBackground(new java.awt.Color(214, 4, 39));
        btnLogin.setFont(new java.awt.Font("Plus Jakarta Sans SemiBold", 0, 16)); // NOI18N
        btnLogin.setForeground(new java.awt.Color(255, 255, 255));
        btnLogin.setIcon(new FlatSVGIcon("posseblakratu/icon/IconLogin.svg")
        );
        btnLogin.setText("Login");
        btnLogin.setBorderPainted(false);
        btnLogin.setFocusPainted(false);
        btnLogin.setFocusable(false);
        btnLogin.addActionListener(this::btnLoginActionPerformed);

        javax.swing.GroupLayout containerLoginLayout = new javax.swing.GroupLayout(containerLogin);
        containerLogin.setLayout(containerLoginLayout);
        containerLoginLayout.setHorizontalGroup(
            containerLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(containerLoginLayout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(containerLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jJpanelll, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(Jpanell, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(Jpanelll, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblUsernameLogin, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnLogin, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(24, 24, 24))
        );
        containerLoginLayout.setVerticalGroup(
            containerLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(containerLoginLayout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(lblUsernameLogin)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jJpanelll, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20)
                .addComponent(Jpanell, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20)
                .addComponent(Jpanelll, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(btnLogin, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30))
        );

        rightContainer.add(containerLogin);

        txtCopyright.setFont(new java.awt.Font("Plus Jakarta Sans", 0, 14)); // NOI18N
        txtCopyright.setForeground(new java.awt.Color(92, 62, 60));
        txtCopyright.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txtCopyright.setText("© 2026 Ratu Seblak POS System. All rights reserved.");
        txtCopyright.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        rightContainer.add(txtCopyright);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = -298;
        gridBagConstraints.ipady = 61;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(110, 70, 130, 200);
        mainPanel.add(rightContainer, gridBagConstraints);

        leftContainer.setBackground(new java.awt.Color(252, 249, 255));

        lblLogo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblLogo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/posseblakratu/icon/logo.png"))); // NOI18N
        lblLogo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        leftContainer.add(lblLogo);

        lblToko.setFont(new java.awt.Font("Plus Jakarta Sans SemiBold", 0, 40)); // NOI18N
        lblToko.setForeground(new java.awt.Color(214, 4, 39));
        lblToko.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblToko.setText("RATU SEBLAK");
        leftContainer.add(lblToko);

        tDetail.setFont(new java.awt.Font("Plus Jakarta Sans", 0, 14)); // NOI18N
        tDetail.setForeground(new java.awt.Color(92, 62, 60));
        tDetail.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        tDetail.setText("Sistem Point of Sale");
        leftContainer.add(tDetail);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = -254;
        gridBagConstraints.ipady = 81;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(220, 190, 100, 0);
        mainPanel.add(leftContainer, gridBagConstraints);

        getContentPane().add(mainPanel, java.awt.BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnMataActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMataActionPerformed
        //jika tombol mata diklik maka tampilkan password, jika diklik lagi sembunyikan
        if (btnMata.isSelected()) {
            //tampilkan teks password
            tPassword.setEchoChar((char) 0);
        } else {
            //sembunyikan teks password dengan karakter bullet
            tPassword.setEchoChar('•');
        }
    }//GEN-LAST:event_btnMataActionPerformed

    private void btnLoginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLoginActionPerformed
        //ambil teks yang dimasukkan user pada field username
        String username = tUsername.getText();

        //ambil teks yang dimasukkan user pada field password
        String password = String.valueOf(tPassword.getPassword());

        //ambil role yang dipilih pada combobox
        String role = cRole.getSelectedItem().toString();

        //periksa apakah username dan password tidak kosong
        if (username.length() != 0 && password.length() != 0) {
            try {
                //query sql untuk mencari user dengan username, password, role dan status aktif
                String sql = "SELECT * FROM pengguna WHERE username=? AND password=MD5(?) AND role=? AND status='Aktif'";

                //membuat koneksi ke database
                Connection conn = Koneksi.konek();

                //siapkan statement sql dengan parameter
                PreparedStatement ps = conn.prepareStatement(sql);

                //isi parameter pertama dengan username
                ps.setString(1, username);

                //isi parameter kedua dengan password yang akan di hash MD5
                ps.setString(2, password);

                //isi parameter ketiga dengan role
                ps.setString(3, role);

                //jalankan query dan ambil hasilnya
                ResultSet rs = ps.executeQuery();

                //jika hasil query memiliki data berarti login berhasil
                if (rs.next()) {

                    //menyimpan id pengguna yang sedang login ke session
                    FrameLogin.idPengguna = rs.getString("id_pengguna");

                    //menyimpan username yang sedang login ke session
                    FrameLogin.username = rs.getString("username");

                    //menyimpan role pengguna ke session
                    FrameLogin.role = rs.getString("role");

                    //tanya pengguna apakah ingin menyimpan data login
                    int simpan = JOptionPane.showConfirmDialog(
                            null,
                            "Simpan data login di perangkat ini?",
                            "Simpan Sesi",
                            JOptionPane.YES_NO_OPTION
                    );

                    //jika pengguna memilih ya, simpan sesi ke preferences
                    if (simpan == JOptionPane.YES_OPTION) {
                        simpanSesi(username, password, role);
                    } else {
                        //jika tidak, hapus sesi lama yang mungkin tersimpan
                        hapusSesi();
                    }

                    //menyimpan state window sebelum dispose (maximized atau normal)
                    int windowState = getExtendedState();
                    java.awt.Rectangle bounds = getBounds();

                    //menutup form login
                    dispose();

                    //buka frame dengan state dan ukuran yang sama
                    FrameMain frameOwner = new FrameMain();
                    if (windowState == javax.swing.JFrame.MAXIMIZED_BOTH) {
                        frameOwner.setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);
                    } else {
                        frameOwner.setBounds(bounds);
                    }
                    frameOwner.setVisible(true);

                } else {
                    //jika data tidak ditemukan tampilkan pesan error
                    JOptionPane.showMessageDialog(null, "Username/Password/Role salah atau akun tidak aktif");
                }

            } catch (SQLException sQLException) {
                //jika terjadi kesalahan sql tampilkan pesan error
                JOptionPane.showMessageDialog(null, "Gagal melakukan login!");
            }
        } else {
            //jika username atau password kosong beri peringatan ke user
            JOptionPane.showMessageDialog(null, "Username/password tidak boleh kosong");
        }
    }//GEN-LAST:event_btnLoginActionPerformed

    private void tUsernameFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tUsernameFocusGained
        //ambil teks yang saat ini ada di field username
        String username = tUsername.getText();

        //jika masih berisi placeholder, kosongkan agar pengguna bisa langsung mengetik
        if (username.equals("Masukkan Username")) {
            tUsername.setText("");
        }
    }//GEN-LAST:event_tUsernameFocusGained

    private void tUsernameFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tUsernameFocusLost
        //ambil teks yang ada di field username
        String username = tUsername.getText();

        //jika kosong kembalikan tulisan placeholder
        if (username.equals("") || username.equals("Masukkan Username")) {
            tUsername.setText("Masukkan Username");
        }
    }//GEN-LAST:event_tUsernameFocusLost

    private void tPasswordFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tPasswordFocusGained
        //ambil teks password dan ubah dari char[] menjadi String
        String password = String.valueOf(tPassword.getPassword());

        //jika masih berisi placeholder kosongkan dan aktifkan karakter bullet
        if (password.equals("Masukkan Password")) {
            tPassword.setText("");
            tPassword.setEchoChar('•');
        }
    }//GEN-LAST:event_tPasswordFocusGained

    private void tPasswordFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tPasswordFocusLost
        //ambil teks password untuk dicek
        String password = String.valueOf(tPassword.getPassword());

        //jika kosong kembalikan ke placeholder
        if (password.isEmpty()) {
            //nonaktifkan echo char agar teks placeholder bisa terbaca
            tPassword.setEchoChar((char) 0);
            //tampilkan kembali teks placeholder
            tPassword.setText("Masukkan Password");
        }
    }//GEN-LAST:event_tPasswordFocusLost

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
            //mengatur warna background selection pada combobox
            UIManager.put("ComboBox.selectionBackground", new java.awt.Color(252, 232, 230));
            //mengatur warna teks pada item yang dipilih di combobox
            UIManager.put("ComboBox.selectionForeground", new java.awt.Color(0, 0, 0));
            //membuat sudut button menjadi melengkung
            UIManager.put("Button.arc", 10);
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (UnsupportedLookAndFeelException unsupportedLookAndFeelException) {
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {

            //cek sesi tersimpan sebelum menampilkan apapun
            if (FrameLogin.muatSesi()) {
                //jika sesi valid, langsung buka FrameMain tanpa menampilkan form login
                new FrameMain().setVisible(true);
            } else {
                //jika tidak ada sesi, tampilkan form login seperti biasa
                new FrameLogin().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel Jpanell;
    private javax.swing.JPanel Jpanelll;
    private javax.swing.JButton btnLogin;
    private javax.swing.JToggleButton btnMata;
    private javax.swing.JComboBox<String> cRole;
    private javax.swing.JPanel containerLogin;
    private javax.swing.JPanel jJpanelll;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel lblLogo;
    private javax.swing.JLabel lblMasukSebagai;
    private javax.swing.JLabel lblPassword;
    private javax.swing.JLabel lblToko;
    private javax.swing.JLabel lblUsernameLogin;
    private javax.swing.JPanel leftContainer;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JPanel passContainer;
    private javax.swing.JPanel rightContainer;
    private javax.swing.JPanel roleContainer;
    private javax.swing.JLabel tDetail;
    private javax.swing.JPasswordField tPassword;
    private javax.swing.JTextField tUsername;
    private javax.swing.JLabel txtCopyright;
    private javax.swing.JPanel usrContainer;
    // End of variables declaration//GEN-END:variables

}
