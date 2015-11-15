package com.allansimon.frwordinfo;

import android.database.Cursor;

import android.widget.TextView;
import android.widget.ListView;
import android.view.ViewGroup;


import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.content.Intent;
import android.util.Log;

import com.allansimon.frwordinfo.SearchWordDao;

public class MainActivity extends ActionBarActivity
{

    SearchWordDao dao;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        dao = new SearchWordDao(this);
    }

    public void search(View view)
    {
        EditText enterWord = (EditText) findViewById(R.id.enter_word);
        String word = enterWord.getText().toString().trim();
        if (word == "") {
            return;
        }
        Log.w(this.getClass().toString(), "performSearch");
        performSearch(word);
    }

    private void performSearch(String searchedWord)
    {
        Cursor cursor = dao.searchWord(searchedWord);
        SearchResultAdapter adapter = new SearchResultAdapter(
            this,
            R.id.full_result,
            searchedWord,
            cursor
        );
        ListView listView = (ListView) findViewById(
            R.id.full_result
        );
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}
