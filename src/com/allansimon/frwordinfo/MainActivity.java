package com.allansimon.frwordinfo;

import android.app.Activity;
import android.os.Bundle;

public class MainActivity extends Activity
{

    public final static String EXTRA_VERB = "com.allansimon.frwordinfo.MESSAGE";
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }
}
