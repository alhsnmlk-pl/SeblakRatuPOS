/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package posseblakratu.component;

/**
 *
 * @author Al
 */
//class untuk menyimpan data topping
public class DetailTopping {

    //menyimpan id produk topping
    private final String idProduk;

    //menyimpan nama topping
    private final String nama;

    //menyimpan harga topping
    private final double harga;

    //menyimpan qty topping
    private final int qty;

    //menyimpan subtotal topping
    private final double subtotal;

    //constructor menerima parameter untuk mengisi semua variable detail topping
    public DetailTopping(String idProduk, String nama, double harga, int qty) {

        this.idProduk = idProduk;
        this.nama = nama;
        this.harga = harga;
        this.qty = qty;
        this.subtotal = harga * qty;

    }

    //mengembalikan id produk
    public String getIdProduk() {
        return idProduk;
    }

    //mengembalikan nama topping
    public String getNama() {
        return nama;
    }

    //mengembalikan harga
    public double getHarga() {
        return harga;
    }

    //mengembalikan qty
    public int getQty() {
        return qty;
    }

    //mengembalikan subtotal topping
    public double getSubtotal() {
        return subtotal;
    }

}
