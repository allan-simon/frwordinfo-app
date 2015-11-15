package com.allansimon.frwordinfo;

import android.app.Activity;

import android.widget.ArrayAdapter;
import android.database.Cursor;
import android.content.Context;

import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import android.widget.LinearLayout;

import com.allansimon.frwordinfo.SearchResult;

public class SearchResultAdapter extends ArrayAdapter<SearchResult>
{
    //TODO replace by a enum
    private static final int WORD_COLUMN = 0 ;
    private static final int ASCII_COLUMN = 1 ;

    public String types[];

    public SearchResultAdapter(
        Context context,
        int resource,
        String searchedWord,
        Cursor cursor
    ) {
        super(context, resource);

        types = context.getResources().getStringArray(R.array.types);

        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {

            String word = cursor.getString(WORD_COLUMN);
            String ascii = cursor.getString(ASCII_COLUMN);

            // as we use a (not-so-great) hash to perform the search
            // it may happen that we got some collision, so we check
            // that the word returned and the searched word do match
            if (!word.equals(searchedWord) && !ascii.equals(searchedWord)) {
                continue;
            }

            add(new SearchResult(cursor));
        }
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        SearchResult result = this.getItem(position);

        LinearLayout linearLayout = createResultLayout(parent);
        TextView textView = createTypeTitle(linearLayout);
        textView.setText(result.word + ": " + types[result.type]);
        linearLayout.addView(textView);

        if (!result.isFlexion()) {
            String dictionnaryLine = result.getDictionnaryInfoLine();
            if (dictionnaryLine.isEmpty()) {
                return linearLayout;
            }
            TextView otherInfo = createOtherInfo(linearLayout);
            otherInfo.setText(dictionnaryLine);
            linearLayout.addView(otherInfo);               
            return linearLayout;
        }

        return linearLayout;
    }

    /**
     *
     */
    private LinearLayout createResultLayout(ViewGroup container)
    {
        return (LinearLayout) ((Activity)getContext()).getLayoutInflater().inflate(
            R.layout.searchresult,
            container,
            false
        ).findViewById(R.id.item_result);
    }

    /**
     *
     */
    private TextView createTypeTitle(ViewGroup container)
    {
        return (TextView) ((Activity)getContext()).getLayoutInflater().inflate(
            R.layout.word_type_title,
            container,
            false
        );
    }

    /**
     *
     */
    private TextView createOtherInfo(ViewGroup container)
    {
        return (TextView) ((Activity)getContext()).getLayoutInflater().inflate(
            R.layout.word_other_info,
            container,
            false
        );
    }

}
