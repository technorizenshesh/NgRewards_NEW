package main.com.ngrewards.activity;

import android.content.Context;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class PreferenceConnector {

    public static final String PREF_NAME = "DOCTOR";
    public static final int MODE = Context.MODE_PRIVATE;
    public static final String Status = "Status";
    public static final String UR_ID = "user_id";
    public static final String Api_key = "Api_key";
    public static final String LoginStatus = "login";
    public static String FaceBook_Name = "FaceBook_Name";
    public static String facebook_image = "facebook_image";
    public static String FaceBook_Email = "FaceBook_Email";
    public static String Phone = "Phone";
    public static String UserNAme = "UserNAme";
    public static String Invited = "Invited";
    public static String Full_Name = "Full_Name";
    public static String Delevry = "Delevry";
    public static String TaxAmout = "TaxAmout";
    public static String guest_no = "guest_no";
    public static String table_no = "table_no";
    public static String order_Address_Id = "order_Address_Id";
    public static String order_Date = "order_Date";
    public static String order_Time = "order_Time";
    public static String order_special_request = "order_special_request";
    public static String AfflitedName = "AfflitedName";
    public static String Create_Profile = "create_profile";
    public static String UserNAme1 = "UserNAme1";
    public static String UserNAme123 = "UserNAme123";
    public static String Logout_Status = "Logout_Status";
    public static String Status_Facebook = "Status_Facebook";
    public static String Profile_com = "Profile_com";
    public static String employee_id = "employee_id";
    public static String employee_name = "employee_name";
    public static String Who_invited = "Who_invited";


    public static void writeInteger(Context context, String key, int value) {
        getEditor(context).putInt(key, value).commit();

    }

    public static int readInteger(Context context, String key, int defValue) {
        return getPreferences(context).getInt(key, defValue);
    }


    public static String writeString(Context context, String key, String value) {
        getEditor(context).putString(key, value).commit();

        return key;
    }

    public static String readString(Context context, String key, String defValue) {
        return getPreferences(context).getString(key, defValue);
    }


    public static void writeArraylist(Context context, String key,
                                      List<String> arryid) {
        Set<String> set = new HashSet<String>(arryid);
        getEditor(context).putStringSet(key, set).commit();

    }

    public static List<String> ReadArraylist(Context context, String key, String s) {
        Set<String> stock_Set = getPreferences(context).getStringSet(key,
                new HashSet<String>());
        List<String> demo = new ArrayList<String>(stock_Set);

        return demo;

    }

    public static void writeArrayModellist(Context context, String key,
                                           List<String> arryid) {
        Set<String> set = new HashSet<String>(arryid);
        getEditor(context).putStringSet(key, set).commit();

    }

    public static List<String> ReadArrayModellist(Context context, String key, String s) {
        Set<String> stock_Set = getPreferences(context).getStringSet(key,
                new HashSet<String>());
        List<String> demo = new ArrayList<String>(stock_Set);

        return demo;

    }

    public static long readLong(Context context, String key, long defValue) {
        return getPreferences(context).getLong(key, defValue);
    }

    public static SharedPreferences getPreferences(Context context) {
        return context.getSharedPreferences(PREF_NAME, MODE);
    }

    public static Editor getEditor(Context context) {
        return getPreferences(context).edit();
    }

    public static void remove(Context context, String key) {
        getEditor(context).remove(key);
    }


}