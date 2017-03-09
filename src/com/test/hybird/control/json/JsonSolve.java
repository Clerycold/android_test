package com.test.hybird.control.json;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by clery on 2017/1/19.
 */

public class JsonSolve {
    public static String createJson(String[] jsonname,String[] jsonvalue) {

        JSONObject jso = new JSONObject();
        try{
            for (int i = 0,j = jsonname.length ; i<j;i++){
                jso.put(jsonname[i],jsonvalue[i]);
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }
        return jso.toString();
    }

    public static String JsonTokener(String json){

        if(json != null){
            String jsonString = json.substring(json.indexOf("["), json.lastIndexOf("]") + 1);
            jsonString = jsonString.replace("\\","");
            return jsonString;
        }
        return null;
    }

    public static String[] JsonStringToIntArray(JSONArray jsonArray){
        JSONArray json = jsonArray;
        String[] tourImg = new String[json.length()];
        for (int i=0 ,j=json.length(); i<j ; i++){
            try {
                tourImg[i] = json.get(i).toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return tourImg;
    }
}