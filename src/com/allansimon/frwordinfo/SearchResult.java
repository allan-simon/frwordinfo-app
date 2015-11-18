package com.allansimon.frwordinfo;
import android.database.Cursor;

import org.json.JSONObject;
import org.json.JSONException;

import android.util.Log;

public class SearchResult {
    // see the python script generating the database
    // used to know if the word is a flexion or not
    private static final String LEMMA = "l";

    // key for the object containing all info relative
    // to the word flexion
    private static final String FLEXION = "f";

    // used to know if the word is plural or singular
    private static final String NUMBER = "n";
    // used to know if the word is masculin or feminin (or both)
    private static final String GENDER = "g";
    // used to know which tense the verb is
    private static final String TENSE = "t";

    private static final String MODE = "m";
    private static final String PERSON = "p";

    // used to know if the verb is 1st, 2nd or 3rd group
    private static final String GROUP = "gr";

    //TODO replace by a enum
    private static final int WORD_COLUMN = 0 ;
    private static final int ASCII_COLUMN = 1 ;
    private static final int TYPE_COLUMN = 2 ;
    private static final int INFO_COLUMN = 3 ;

    //TODO replace by yet an other enum
    public static final int PERSON_INFO_INDEX = 0 ;
    public static final int NUMBER_INFO_INDEX = 1 ;
    public static final int GENDER_INFO_INDEX = 2 ;
    public static final int MODE_INFO_INDEX = 3 ;
    public static final int TENSE_INFO_INDEX = 4 ;

    public String word;
    public int type;
    public JSONObject info;
    private String ascii;

    /**
     *
     */
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

    /**
     *
     */
    public boolean isFlexion()
    {
        return info.has(LEMMA);
    }

    /**
     *
     */
    public String getLemma()
    {
        try {
            return info.getString(LEMMA);
        } catch (JSONException e) {
            Log.e(this.getClass().toString(), e.toString());
            return "";
        }
    }

    /**
     *
     */
    public boolean isNoun()
    {
        // 1 = noum
        return type == 1;
    }

    /**
     *
     */
    public boolean isVerb()
    {
        // 6 = verb 
        return type == 6;
    }

    /**
     *
     */
    public int[] getFlexionInfos()
    {
        int[] infos = {0, 0, 0, 0, 0};
        try {

            JSONObject flexion = info.getJSONObject(FLEXION);
            Log.w(this.getClass().toString(), "getinfos");

            if (flexion.has(NUMBER)) {
                // here "number" is to express "singular"/"plural"
                int number = flexion.getInt(NUMBER);
                infos[NUMBER_INFO_INDEX] = number;
            }

            if (flexion.has(GENDER)) {
                int gender = flexion.getInt(GENDER);
                infos[GENDER_INFO_INDEX] = gender;
            }

            if (isNoun()) {
                return infos;
            }

            if (flexion.has(MODE)) {
                int mode = flexion.getInt(MODE);
                infos[MODE_INFO_INDEX] = mode;
            }

            if (flexion.has(TENSE)) {
                int tense = flexion.getInt(TENSE);
                infos[TENSE_INFO_INDEX] = tense;
            }

            if (flexion.has(PERSON)) {
                int person = flexion.getInt(PERSON);
                infos[PERSON_INFO_INDEX] = person;
            }

            return infos;
        } catch (JSONException e) {
            Log.e(this.getClass().toString(), e.toString());
            return infos;
        }
    }
    
    /**
     *
     */
    public String getDictionnaryInfoLine()
    {

        try {
            if (isNoun() && info.has(GENDER)) {
                return "n." + info.getString(GENDER);
            }

            if (isVerb() && info.has(GROUP)) {
                return "v." + info.getString(GROUP) + "gr";
            }

        } catch (JSONException e) {
            Log.e(this.getClass().toString(), e.toString());
            return "";
        }
        return "";
    }
}
