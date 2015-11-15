package com.allansimon.frwordinfo;
import android.database.Cursor;

import org.json.JSONObject;
import org.json.JSONException;

import android.util.Log;

public class SearchResult {
    // see the python script generating the database
    // used to know if the word is a flexion or not
    private static final String LEMMA = "l";

    //TODO replace by a enum
    private static final int WORD_COLUMN = 0 ;
    private static final int ASCII_COLUMN = 1 ;
    private static final int TYPE_COLUMN = 2 ;
    private static final int INFO_COLUMN = 3 ;

    public String word;
    public int type;
    public JSONObject info;
    private String ascii;



    public SearchResult(Cursor cursor)
    {
        word = cursor.getString(WORD_COLUMN);
        ascii = cursor.getString(ASCII_COLUMN);

        info = new JSONObject();
        type = cursor.getInt(TYPE_COLUMN);
        try {
            info = new JSONObject(cursor.getString(INFO_COLUMN));

        } catch (JSONException e) {
            Log.e(this.getClass().toString(), e.toString());
            return;
        }
    }

    public boolean isFlexion()
    {
        return info.has(LEMMA);
    }

    public String getLemma()
    {
        try {
            return info.getString(LEMMA);
        } catch (JSONException e) {
            Log.e(this.getClass().toString(), e.toString());
            return "";
        }
    }

    public boolean isNoun()
    {
        // 1 = noum
        return type == 1;
    }

    public boolean isVerb()
    {
        // 6 = verb 
        return type == 6;
    }

    
    public String getDictionnaryInfoLine()
    {

        try {
            if (isNoun() && info.has("g")) {
                return "n." + info.getString("g");
            }

            if (isVerb() && info.has("gr")) {
                return "v." + info.getString("gr") + "gr";
            }

        } catch (JSONException e) {
            Log.e(this.getClass().toString(), e.toString());
            return "";
        }
        return "";
    }
}
