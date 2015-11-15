package com.allansimon.frwordinfo;

import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;

import android.content.Context;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class SearchWordDao {
    private static final String DB_NAME = "word.db";

    private SQLiteDatabase database;

    public SearchWordDao(Context context)
    {
        ExternalDbOpenHelper dbOpenHelper = new ExternalDbOpenHelper(
            context,
            DB_NAME
        );
        database = dbOpenHelper.openDataBase();
    }


    /**
     *
     */
    public Cursor searchWord(String word)
    {

        String hash = String.valueOf(hash32Bits(word));

        //TODO replace rawQuery by call to the right methods
        return database.rawQuery(
            //concatenation of static strings is optimized at compile
            //times, so no need for string building ;-)
            "SELECT " +
            "    word, " +
            "    word_ascii, " +
            "    type, " +
            "    meta " +
            "FROM word " +
            "WHERE " +
            "    hashword = " + hash + " OR " +
            "    hashascii = " + hash + " " +
            "ORDER BY word, type",
            null
        );
    }

    /**
     *
     */
    private static long hash32Bits(String input)
    {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(input.getBytes());
            byte[] digest = messageDigest.digest();


            return convertFourBytesToLong(
                digest[12],
                digest[13],
                digest[14],
                digest[15]
            );

        } catch (NoSuchAlgorithmException e) {
            return 42;
        }
    }

    /**
     *
     */
    private static long convertFourBytesToLong(
        byte b1,
        byte b2,
        byte b3,
        byte b4
    ) {
        return (
            ((long)(b1 & 0xFF) << 24) |
            ((b2 & 0xFF) << 16) |
            ((b3 & 0xFF) << 8)  |
             (b4 & 0xFF)
        );
    }
}
