package com.test.hybird.control;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by clery on 2017/1/18.
 */

public class SaveData {
    private SharedPreferences settings;
    private Boolean aBoolean;
    private String aString;

    private Context context;

    public SaveData(Context context){

        this.context=context;

    }
    public void saveBooleanData(String dataname,String filename,Boolean data){
        settings = context.getSharedPreferences(dataname, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=settings.edit();
        editor.putBoolean(filename,data);
        editor.apply();
    }
    public Boolean readDataBoolean(String dataname,String filename){
        settings = context.getSharedPreferences(dataname, Context.MODE_PRIVATE);
        //取得相应的值，如果没有该值，说明还未写入，用true作为默认值
        aBoolean= settings.getBoolean(filename,true);

        return aBoolean;
    }
    public void saveStringData(String dataname,String filename,String string){
        settings = context.getSharedPreferences(dataname, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=settings.edit();
        editor.putString(filename,string);
        editor.apply();
    }
    public String getStringData(String dataname,String filename){
        settings = context.getSharedPreferences(dataname, Context.MODE_PRIVATE);
        //取得相应的值，如果没有该值，说明还未写入，用true作为默认值
        aString= settings.getString(filename,"");

        return aString;
    }

}
