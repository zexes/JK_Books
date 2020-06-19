package com.zikozee.books;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.List;

class SpUtil {
    private SpUtil(){}
    public static final String PREF_NAME = "BooksPreference";
    public static final String POSITION = "position";
    public static final String QUERY = "query";


    public static SharedPreferences getPrefs(Context context) {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    //Context.MODE_PRIVATE //only application creating this will be able to access this
    }

    public static  String getPreferenceString(Context context, String key){
        return getPrefs(context).getString(key, "");
    }

    public static int getPreferenceInt(Context context, String key){
        return getPrefs(context).getInt(key, 0);
    }

    public static void setPreferenceString(Context context, String key, String value){
        SharedPreferences.Editor editor = getPrefs(context).edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static void setPreferenceInt(Context context, String key, int value){
        SharedPreferences.Editor editor = getPrefs(context).edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public static List<String> getQueryList(Context context){
        List<String> queryList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {

            String query = getPrefs(context).getString(QUERY + i,"");
            if(!query.isEmpty()){
                query = query.replace(",", "");
                queryList.add(query.trim());
            }
        }
        return queryList;
    }

}
