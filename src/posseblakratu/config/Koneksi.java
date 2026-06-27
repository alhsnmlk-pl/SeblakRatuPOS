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
    
    //mendeklarasikan variable koneksi sbg static agar bisa di akses dimana saja di dalam class
    private static Connection mysqlconfig;
    
    //method static utk membuka koneksi ke database mysql
    public static Connection konek() {

        try {
            // url koneksi ke database
            String url = "jdbc:mysql://localhost:3306/pos_ratu_seblak"; 
            
            //usrname database
            String user = "root"; 
            
            //password databse
            String pass = ""; 
            
            //membuka koneksi ke databse dan menyimpannya di variable mysqlconfig
            mysqlconfig = DriverManager.getConnection(url, user, pass);

        } catch (SQLException sQLException) {
            //menampilkan pesan error jika koneksi gagal
            System.err.println(sQLException.getMessage());
        }
        
        //mengembalikan objek koneksi (bisa null jika gagal)
        return mysqlconfig;
    }
    
}
