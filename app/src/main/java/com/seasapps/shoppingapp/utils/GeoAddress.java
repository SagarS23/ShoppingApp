package com.seasapps.shoppingapp.utils;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import java.util.List;
import java.util.Locale;


public class GeoAddress {

    private static final String TAG = "GeoAddress";

    public static String countryName = "";
    public static String stateName = "";
    public static String mPostalCode = "";
    public static String address = "";
    public static String city = "";

    public static String subLocality = "";
    public static String subAdminArea = "";
    public static String whatAddressIs = "";


    public static String getAddressData(Context context, double latitude, double longitude) {
        String city = "";
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());

        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null) {
                Address returnAddresses = addresses.get(0);

                address = addresses.get(0).getAddressLine(0);

                subAdminArea = addresses.get(0).getSubAdminArea();
                subLocality = addresses.get(0).getSubLocality();



                city = addresses.get(0).getLocality();

                stateName = addresses.get(0).getAdminArea();
                countryName = addresses.get(0).getCountryName();
                mPostalCode = addresses.get(0).getPostalCode();
                Log.e(TAG, "getAddressData: getSubLocality  : " + addresses.get(0).getSubLocality());
                Log.e(TAG, "getAddressData: getSubAdminArea : " + addresses.get(0).getSubAdminArea());
                Log.e(TAG, "getAddressData: getAdminArea : " + addresses.get(0).getAdminArea());
                Log.e(TAG, "getAddressData: getLocality : " + addresses.get(0).getLocality());


            } else {
                city = "";
                Log.e(TAG, "getCityByLatLong: ");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("location address", "Cannot get Address!");
        }
        return city;
    }

}
