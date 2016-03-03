package is.aiga.bordid;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SaveSharedPreference {

    static final String PREF_USER_NAME = "username";
    static final String PREF_NAME = "name";
    static final String PREF_EMAIL = "email";
    static final String PREF_PHONE_NUMBER = "phone_number";
    static final String PREF_RESTAURANT_NAME = "restaurant_name";
    static final String PREF_ADDRESS = "address";
    static final String PREF_ZIP = "zip";
    static final String PREF_CITY = "city";
    static final String PREF_LATITUDE = "latitude";
    static final String PREF_LONGITUDE = "longitude";
    static final String PREF_RESTAURANT_PHONE_NUMBER = "restaurant_phone_number";
    static final String PREF_URL = "url";
    static final String PREF_NUMSEATS = "num_seats";
    static final String PREF_RESTAURANT_EMAIL = "restaurant_email";

    static SharedPreferences getSharedPreferences(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    public static void setUserName(Context ctx, String userName, String name)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_USER_NAME, userName);
        editor.putString(PREF_NAME, name);
        editor.commit();
    }

    public static String getUserName(Context ctx)
    {
        return getSharedPreferences(ctx).getString(PREF_USER_NAME, "");
    }

    public static String getName(Context ctx)
    {
        return getSharedPreferences(ctx).getString(PREF_NAME, "");
    }

    public static void clearUserName(Context ctx)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.clear(); //clear all stored data
        editor.commit();
    }
}
