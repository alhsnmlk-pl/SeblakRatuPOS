/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package posseblakratu.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;



/**
 *
 * @author Al
 */
public class Koneksi {

    //mendeklarasikan variable koneksi sebagai static agar bisa diakses di seluruh class
    private static Connection mysqlconfig;

    //method static untuk membuka koneksi ke database MySQL
    public static Connection konek() {

        try {
            //url koneksi ke database
            String url = "jdbc:mysql://localhost:3306/pos_ratu_seblak";

            //username database
            String user = "root";

            //password database
            String pass = "";

            //membuka koneksi ke database dan menyimpannya di variable mysqlconfig
            mysqlconfig = DriverManager.getConnection(url, user, pass);

        } catch (SQLException sQLException) {
            //menampilkan pesan error jika koneksi gagal
            System.err.println(sQLException.getMessage());
        }

        //mengembalikan objek koneksi (bisa null jika gagal)
        return mysqlconfig;
    }
    
        
}
