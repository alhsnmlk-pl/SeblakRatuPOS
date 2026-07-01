/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package posseblakratu.session;

/**
 *
 * @author Al
 */

//class untuk menyimpan data pengguna yang sedang login
public class SessionLogin {

    //menyimpan id pengguna yang sedang login
    public static String idPengguna;

    //menyimpan username yang sedang login
    public static String username;

    //menyimpan role pengguna yang sedang login
    public static String role;
    
    
    

    //method untuk menyimpan id pengguna
    public static void setIdPengguna(String idPengguna) {

        //menyimpan id pengguna
        SessionLogin.idPengguna = idPengguna;

    }

    //method untuk mengambil id pengguna
    public static String getIdPengguna() {

        //mengembalikan id pengguna
        return idPengguna;

    }
    
    
    

    //method untuk menyimpan username
    public static void setUsername(String username) {

        //menyimpan username
        SessionLogin.username = username;

    }

    //method untuk mengambil username
    public static String getUsername() {

        //mengembalikan username
        return username;

    }
    
    
    

    //method untuk menyimpan role
    public static void setRole(String role) {

        //menyimpan role
        SessionLogin.role = role;

    }

    //method untuk mengambil role
    public static String getRole() {

        //mengembalikan role
        return role;

    }

}
