/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package posseblakratu.component;

//import library list, lib ini berfungsi utuk menyimpan kumpulan data dalam satu variable
import java.util.List;  //Untuk menyimpan daftar topping

/**
 *
 * @author Al
 */
public class cardKeranjang extends javax.swing.JPanel {

    // -------------------------------------------------------------------------
    // Inner class pengganti DetailTopping
    // menyimpan data satu item topping pada pesanan
    // -------------------------------------------------------------------------
    public static class ToppingItem {

        //id produk topping
        private final String idProduk;

        //nama topping
        private final String nama;

        //harga satuan topping
        private final double harga;

        //qty topping
        private final int qty;

        //subtotal topping (harga * qty)
        private final double subtotal;

        //constructor menerima semua data topping
        public ToppingItem(String idProduk, String nama, double harga, int qty) {
            this.idProduk = idProduk;
            this.nama = nama;
            this.harga = harga;
            this.qty = qty;
            this.subtotal = harga * qty;
        }

        //mengembalikan id produk
        public String getIdProduk() { return idProduk; }

        //mengembalikan nama topping
        public String getNama() { return nama; }

        //mengembalikan harga satuan
        public double getHarga() { return harga; }

        //mengembalikan qty
        public int getQty() { return qty; }

        //mengembalikan subtotal
        public double getSubtotal() { return subtotal; }
    }
    // -------------------------------------------------------------------------

    //id produk menu
    private String idProduk;

    //nama menu
    private String namaMenu;

    //harga satuan menu
    private double hargaSatuan;

    //jumlah menu
    private int qty = 1;
    
    //menyimpan event ketika qty berubah
    private Runnable qtyListener;

    //level pedas
    private int level;

    //harga level
    private double hargaLevel;

    //subtotal menu
    private double subtotalMenu;

    //menyimpan daftar topping pesanan menggunakan list
    private List<ToppingItem> daftarTopping;
    
    

    /**
     * Creates new form cardKeranjang
     */
    public cardKeranjang() {
        initComponents();
    }

    //method untuk mengisi data pada card keranjang
    public void setData(String idProduk, String namaMenu, double hargaSatuan, int level, double hargaLevel, List<ToppingItem> daftarTopping) {

        //menyimpan data menu
        this.idProduk = idProduk;
        this.namaMenu = namaMenu;
        this.hargaSatuan = hargaSatuan;
        this.level = level;
        this.hargaLevel = hargaLevel;
        this.daftarTopping = daftarTopping;

        //menampilkan nama menu
        lblMenuPesan.setText(namaMenu);

        //mengatur qty awal menjadi satu
        qtyKeranjang.setValue(1);

        //menampilkan detail pesanan
        tampilkanDetail();

        //menghitung subtotal
        updateHarga();

    }

    //method untuk menampilkan detail pesanan
    void tampilkanDetail() {

        //menyimpan detail pesanan
        String detail = "";

        //memeriksa apakah menu memiliki level pedas
        if (level != -1) {

            //menambahkan level pedas
            detail = "Lvl " + level;

        }

        //melakukan perulangan seluruh topping
        for (ToppingItem topping : daftarTopping) {

            //menambahkan tanda koma apabila detail tidak kosong
            if (!detail.isEmpty()) {

                detail += ", ";

            }

            //menambahkan nama topping beserta qty
            detail += topping.getNama()
                    + " x"
                    + topping.getQty();

        }

        //menampilkan detail pesanan
        lblKetPesan.setText(detail);

    }

    //method untuk menghitung subtotal pesanan
    private void updateHarga() {

        //mengambil jumlah pesanan langsung dari qtyStepper
        qty = qtyKeranjang.getValue();

        //menghitung harga satu porsi
        double totalPerPorsi = hargaSatuan + hargaLevel;

        //menambahkan harga seluruh topping
        for (ToppingItem topping : daftarTopping) {

            totalPerPorsi += topping.getHarga() * topping.getQty();

        }

        //menghitung subtotal sesuai qty
        subtotalMenu = totalPerPorsi * qty;

        //menampilkan subtotal
        lblHarga.setText("Rp. " + (int) subtotalMenu);

    }

    //method untuk menambahkan event pada tombol hapus
    public void addDeleteListener(java.awt.event.ActionListener listener) {

        //menambahkan event pada tombol hapus
        btnHapusKeranjang.addActionListener(listener);

    }
    
    //method untuk menambahkan event ketika qty berubah
    public void addQtyListener(Runnable listener) {

        //menyimpan listener
        this.qtyListener = listener;

    }

    //mengembalikan id produk
    public String getIdProduk() {
        return idProduk;
    }
    
    //mengembalikan nama menu
    public String getNamaMenu() {
        return namaMenu;
    }

    //mengembalikan harga satuan
    public double getHargaSatuan() {
        return hargaSatuan;
    }

    //mengembalikan level pedas
    public int getLevel() {
        return level;
    }

    //mengembalikan harga level
    public double getHargaLevel() {
        return hargaLevel;
    }

    //mengembalikan qty menu
    public int getQty() {
        return qtyKeranjang.getValue();
    }

    //mengembalikan subtotal menu
    public double getSubtotal() {
        return subtotalMenu;
    }

    //mengembalikan daftar topping
    public List<ToppingItem> getDaftarTopping() {
        return daftarTopping;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        lblMenuPesan = new javax.swing.JLabel();
        btnHapusKeranjang = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        qtyKeranjang = new qtystepper.qtyStepper();
        lblHarga = new javax.swing.JLabel();
        lblKetPesan = new javax.swing.JTextArea();
        jSeparator1 = new javax.swing.JSeparator();

        setBackground(new java.awt.Color(255, 255, 255));
        setMinimumSize(new java.awt.Dimension(262, 100));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setMaximumSize(new java.awt.Dimension(262, 200));
        jPanel1.setMinimumSize(new java.awt.Dimension(262, 93));
        jPanel1.setName(""); // NOI18N

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        lblMenuPesan.setFont(new java.awt.Font("Plus Jakarta Sans", 1, 14)); // NOI18N
        lblMenuPesan.setText("Seblak Original");

        btnHapusKeranjang.setIcon(new javax.swing.ImageIcon(getClass().getResource("/posseblakratu/icon/IconX.png"))); // NOI18N
        btnHapusKeranjang.setBorder(null);
        btnHapusKeranjang.setBorderPainted(false);
        btnHapusKeranjang.setContentAreaFilled(false);
        btnHapusKeranjang.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        btnHapusKeranjang.setOpaque(true);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(lblMenuPesan, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnHapusKeranjang)
                .addGap(0, 0, 0))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnHapusKeranjang, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblMenuPesan))
                .addGap(0, 0, 0))
        );

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 10, 0));

        qtyKeranjang.setFont(new java.awt.Font("Plus Jakarta Sans SemiBold", 0, 14)); // NOI18N
        qtyKeranjang.addActionListener(this::qtyKeranjangActionPerformed);

        javax.swing.GroupLayout qtyKeranjangLayout = new javax.swing.GroupLayout(qtyKeranjang);
        qtyKeranjang.setLayout(qtyKeranjangLayout);
        qtyKeranjangLayout.setHorizontalGroup(
            qtyKeranjangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 93, Short.MAX_VALUE)
        );
        qtyKeranjangLayout.setVerticalGroup(
            qtyKeranjangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 35, Short.MAX_VALUE)
        );

        lblHarga.setFont(new java.awt.Font("Plus Jakarta Sans", 0, 16)); // NOI18N
        lblHarga.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblHarga.setText("Rp 13.000");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(qtyKeranjang, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 41, Short.MAX_VALUE)
                .addComponent(lblHarga, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(qtyKeranjang, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblHarga, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0))
        );

        lblKetPesan.setEditable(false);
        lblKetPesan.setBackground(new java.awt.Color(255, 255, 255));
        lblKetPesan.setColumns(100);
        lblKetPesan.setFont(new java.awt.Font("Plus Jakarta Sans", 0, 12)); // NOI18N
        lblKetPesan.setLineWrap(true);
        lblKetPesan.setRows(2);
        lblKetPesan.setTabSize(0);
        lblKetPesan.setBorder(null);
        lblKetPesan.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        lblKetPesan.setEnabled(false);
        lblKetPesan.setFocusable(false);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(lblKetPesan, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblKetPesan, javax.swing.GroupLayout.DEFAULT_SIZE, 17, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jSeparator1.setBackground(new java.awt.Color(255, 255, 255));
        jSeparator1.setForeground(new java.awt.Color(231, 189, 187));
        jSeparator1.setPreferredSize(new java.awt.Dimension(50, 11));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jSeparator1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void qtyKeranjangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_qtyKeranjangActionPerformed
        // TODO add your handling code here:
        //memperbarui subtotal card
        updateHarga();

        //memanggil event apabila tersedia
        if (qtyListener != null) {

            qtyListener.run();

        }

    }//GEN-LAST:event_qtyKeranjangActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnHapusKeranjang;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel lblHarga;
    private javax.swing.JTextArea lblKetPesan;
    private javax.swing.JLabel lblMenuPesan;
    private qtystepper.qtyStepper qtyKeranjang;
    // End of variables declaration//GEN-END:variables
}
