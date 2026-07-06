/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package posseblakratu.config;

import java.text.NumberFormat;
import java.util.Locale;

/**
 *
 * @author Al
 */
public class FormatUang {

    //locale Indonesia untuk format angka dengan titik sebagai pemisah ribuan
    private static final Locale LOCALE_ID = Locale.forLanguageTag("id-ID");

    //method static untuk memformat angka menjadi format "Rp. 10.000"
    public static String format(double nominal) {

        //membuat formatter angka dengan locale Indonesia
        NumberFormat nf = NumberFormat.getNumberInstance(LOCALE_ID);

        //mengembalikan string dengan format "Rp. xxx.xxx"
        return "Rp. " + nf.format((long) nominal);
    }
}
