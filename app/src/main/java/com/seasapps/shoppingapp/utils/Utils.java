package com.seasapps.shoppingapp.utils;

import android.content.Context;
import android.graphics.Color;
import android.widget.Toast;

import com.seasapps.shoppingapp.R;
import com.seasapps.shoppingapp.widget.styleabletoast.StyleableToast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Utils {

    public static String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    public static boolean isValidEmail(String email) {
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static void showSuccessToast(Context context, String message) {
        StyleableToast styleableToast = new StyleableToast.Builder(context).text(message).textColor(Color.WHITE)
                .duration(Toast.LENGTH_LONG).cornerRadius(23)
                .backgroundColor(context.getResources().getColor(R.color.colorAccent)).build();
        styleableToast.show();
    }

    public static void showErrorToast(Context context, String message) {
        StyleableToast styleableToast = new StyleableToast.Builder(context).text(message).textColor(Color.WHITE)
                .duration(Toast.LENGTH_LONG).cornerRadius(23)
                .backgroundColor(Color.RED).build();
        styleableToast.show();
    }

    public static void showLongErrorToast(Context context, String message) {
        StyleableToast styleableToast = new StyleableToast.Builder(context).text(message).textColor(Color.WHITE)
                .duration(Toast.LENGTH_LONG).cornerRadius(23)
                .backgroundColor(Color.RED).build();
        styleableToast.show();
    }

    public static void showOtherToast(Context context, String message) {
        StyleableToast styleableToast = new StyleableToast.Builder(context).text(message).textColor(Color.WHITE)
                .duration(Toast.LENGTH_SHORT).cornerRadius(23)
                .backgroundColor(Color.BLUE).build();
        styleableToast.show();
    }

    public static void showShortToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void showLongToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    public static int[] mResources = {
            R.drawable.ic_baby_cerelac,
            R.drawable.ic_britannia_toast,
            R.drawable.ic_cadbury_celebrations,
            R.drawable.ic_corn_flakes,
            R.drawable.ic_kelloggs_chocos,
            R.drawable.ic_kelloggs_fruit_nut,
            R.drawable.ic_safal,
            R.drawable.ic_sunfeast_biscuit,
            R.drawable.ic_tasties_biscuits,
            R.drawable.ic_tomato_soup
    };

    public static String getCurrentTimeStamp() {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
            return dateFormat.format(new Date());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
