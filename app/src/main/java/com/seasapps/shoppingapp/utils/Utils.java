package com.seasapps.shoppingapp.utils;

import android.content.Context;
import android.graphics.Color;
import android.widget.Toast;

import com.seasapps.shoppingapp.R;
import com.seasapps.shoppingapp.widget.styleabletoast.StyleableToast;

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
}
