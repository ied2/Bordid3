package is.aiga.bordid;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SaveSharedPreference {

    static final String PREF_USER_ID = "id";
    static final String PREF_USER_NAME = "username";
    static final String PREF_NAME = "name";
    static final String PREF_EMAIL = "email";
    static final String PREF_PHONE_NUMBER = "phone_number";
    static final String PREF_RESTAURANT_ID = "restaurant_id";
    static final String PREF_RESTAURANT_NAME = "restaurant_name";
    static final String PREF_ADDRESS = "address";
    static final String PREF_ZIP = "zip";
    static final String PREF_CITY = "city";
    static final String PREF_LATITUDE = "latitude";
    static final String PREF_LONGITUDE = "longitude";
    static final String PREF_RESTAURANT_PHONE_NUMBER = "restaurant_phoneNumber";
    static final String PREF_URL = "url";
    static final String PREF_NUMSEATS = "num_seats";
    static final String PREF_RESTAURANT_EMAIL = "restaurant_email";
    static final String PREF_PROFILE_IMAGE = "profile_image";

    static SharedPreferences getSharedPreferences(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    public static void setUserId(Context ctx, String id){
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_USER_ID, id);
        editor.commit();
    }

    public static void setUserName(Context ctx, String username){
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_USER_NAME, username);
        editor.commit();
    }

    public static void setName(Context ctx, String name){
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_NAME, name);
        editor.commit();
    }

    public static void setEmail(Context ctx, String email){
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_EMAIL, email);
        editor.commit();
    }

    public static void setPhoneNumber(Context ctx, String phoneNumber){
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_PHONE_NUMBER, phoneNumber);
        editor.commit();
    }

    public static void setRestaurantId(Context ctx, String restId){
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_RESTAURANT_ID, restId);
        editor.commit();
    }

    public static void setRestaurantName(Context ctx, String restaurantName){
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_RESTAURANT_NAME, restaurantName);
        editor.commit();
    }

    public static void setAddress(Context ctx, String address){
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_ADDRESS, address);
        editor.commit();
    }

    public static void setZip(Context ctx, String zip){
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_ZIP, zip);
        editor.commit();
    }

    public static void setCity(Context ctx, String city){
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_CITY, city);
        editor.commit();
    }

    public static void setLatitude(Context ctx, String latitude){
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_LATITUDE, latitude);
        editor.commit();
    }

    public static void setLongitude(Context ctx, String longitude){
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_LONGITUDE, longitude);
        editor.commit();
    }

    public static void setRestaurantPhoneNumber(Context ctx, String restaurantPhoneNumber){
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_RESTAURANT_PHONE_NUMBER, restaurantPhoneNumber);
        editor.commit();
    }

    public static void setUrl(Context ctx, String url){
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_URL, url);
        editor.commit();
    }

    public static void setNumSeats(Context ctx, String numSeats){
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_NUMSEATS, numSeats);
        editor.commit();
    }

    public static void setRestaurantEmail(Context ctx, String restaurantEmail){
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_RESTAURANT_EMAIL, restaurantEmail);
        editor.commit();
    }

    public static void setProfileImage(Context ctx, String profileImage){
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_PROFILE_IMAGE, profileImage);
        editor.commit();
    }

    public static String getUserId(Context ctx)
    {
        return getSharedPreferences(ctx).getString(PREF_USER_ID, "");
    }

    public static String getUserName(Context ctx)
    {
        return getSharedPreferences(ctx).getString(PREF_USER_NAME, "");
    }

    public static String getName(Context ctx)
    {
        return getSharedPreferences(ctx).getString(PREF_NAME, "");
    }

    public static String getEmail(Context ctx)
    {
        return getSharedPreferences(ctx).getString(PREF_EMAIL, "");
    }

    public static String getPhoneNumber(Context ctx)
    {
        return getSharedPreferences(ctx).getString(PREF_PHONE_NUMBER, "");
    }

    public static String getRestaurantId(Context ctx)
    {
        return getSharedPreferences(ctx).getString(PREF_RESTAURANT_ID, "");
    }

    public static String getRestaurantName(Context ctx)
    {
        return getSharedPreferences(ctx).getString(PREF_RESTAURANT_NAME, "");
    }

    public static String getAddress(Context ctx)
    {
        return getSharedPreferences(ctx).getString(PREF_ADDRESS, "");
    }

    public static String getZip(Context ctx)
    {
        return getSharedPreferences(ctx).getString(PREF_ZIP, "");
    }

    public static String getCity(Context ctx)
    {
        return getSharedPreferences(ctx).getString(PREF_CITY, "");
    }

    public static String getLatitude(Context ctx)
    {
        return getSharedPreferences(ctx).getString(PREF_LATITUDE, "");
    }

    public static String getLongitude(Context ctx)
    {
        return getSharedPreferences(ctx).getString(PREF_LONGITUDE, "");
    }

    public static String getRestaurantPhoneNumber(Context ctx)
    {
        return getSharedPreferences(ctx).getString(PREF_RESTAURANT_PHONE_NUMBER, "");
    }

    public static String getUrl(Context ctx)
    {
        return getSharedPreferences(ctx).getString(PREF_URL, "");
    }

    public static String getNumSeats(Context ctx)
    {
        return getSharedPreferences(ctx).getString(PREF_NUMSEATS, "");
    }

    public static String getRestaurantEmail(Context ctx)
    {
        return getSharedPreferences(ctx).getString(PREF_RESTAURANT_EMAIL, "");
    }

    public static String getProfileImage(Context ctx)
    {
        return getSharedPreferences(ctx).getString(PREF_PROFILE_IMAGE, "");
    }

    public static void clearUser(Context ctx)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.clear(); //clear all stored data
        editor.commit();
    }
}