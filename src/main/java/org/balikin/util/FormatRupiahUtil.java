package org.balikin.util;

import jakarta.enterprise.context.ApplicationScoped;

import java.text.NumberFormat;
import java.util.Locale;

@ApplicationScoped
public class FormatRupiahUtil {
    public String formatRupiah (Double rupiah)  {
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
        return numberFormat.format(rupiah);

    }
}
