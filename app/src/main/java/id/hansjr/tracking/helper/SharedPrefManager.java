package id.hansjr.tracking.helper;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefManager {
    public final static String SP_TRACKING = "spTracking";

    public final static String SP_NAME = "spName";
    public final static String SP_EMAIL = "spEmail";
    public final static String SP_USERNAME = "spUsername";
    public final static String SP_ROLE = "spRole";

    public final static String SP_LOGIN_VALIDATE = "spLoginValidate";

    SharedPreferences sp;
    SharedPreferences.Editor spEditor;

    public SharedPrefManager(Context context){
        sp = context.getSharedPreferences(SP_TRACKING, Context.MODE_PRIVATE);
        spEditor = sp.edit();
    }
    public void saveSPString(String keySP, String value){
        spEditor.putString(keySP, value);
        spEditor.commit();
    }

    public void saveSPInt(String keySP, int value){
        spEditor.putInt(keySP, value);
        spEditor.commit();
    }

    public void saveSPBoolean(String keySP, boolean value){
        spEditor.putBoolean(keySP, value);
        spEditor.commit();
    }

    public String getSpName(){
        return sp.getString(SP_NAME, "");
    }

    public String getSpEmail(){
        return sp.getString(SP_EMAIL, "");
    }

    public String getSpUsername(){
        return sp.getString(SP_USERNAME, "");
    }

    public String getSpRole(){
        return sp.getString(SP_ROLE, "");
    }

    public Boolean getSpLoginValidate(){
        return sp.getBoolean(SP_LOGIN_VALIDATE, false);
    }
}
