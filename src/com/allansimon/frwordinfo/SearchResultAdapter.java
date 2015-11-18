package com.allansimon.frwordinfo;

import android.app.Activity;

import android.widget.ArrayAdapter;
import android.database.Cursor;
import android.content.Context;

import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import android.widget.LinearLayout;

import android.util.Log;

import com.allansimon.frwordinfo.SearchResult;

public class SearchResultAdapter extends ArrayAdapter<SearchResult>
{
    //TODO replace by a enum
    private static final int WORD_COLUMN = 0 ;
    private static final int ASCII_COLUMN = 1 ;

    private static final int NO_INFO = 0;

    public String types[];
    public String flexionStrings[][];

    public SearchResultAdapter(
        Context context,
        int resource,
        String searchedWord,
        Cursor cursor
    ) {
        super(context, resource);

        types = context.getResources().getStringArray(R.array.types);

        flexionStrings = new String[5][];
        flexionStrings[SearchResult.NUMBER_INFO_INDEX] = context.getResources().getStringArray(R.array.numbers);
        flexionStrings[SearchResult.GENDER_INFO_INDEX] = context.getResources().getStringArray(R.array.genders);
        flexionStrings[SearchResult.MODE_INFO_INDEX] = context.getResources().getStringArray(R.array.modes);
        flexionStrings[SearchResult.TENSE_INFO_INDEX] = context.getResources().getStringArray(R.array.tenses);
        flexionStrings[SearchResult.PERSON_INFO_INDEX] = context.getResources().getStringArray(R.array.persons);


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

        if (result.isFlexion()) {
            String lemmaStr = result.getLemma();
            LinearLayout flexionLine = createLineFlexionOf(linearLayout);
            TextView lemma = (TextView) flexionLine.findViewById(R.id.lemma);
            lemma.setText(lemmaStr);
            linearLayout.addView(flexionLine);

            String infoText = getFlexionInfoText(result.getFlexionInfos());
            TextView flexionInfoLine = createOtherInfo(linearLayout);
            flexionInfoLine.setText(infoText);
            linearLayout.addView(flexionInfoLine);

            return linearLayout;
        }

        String dictionnaryLine = result.getDictionnaryInfoLine();
        if (dictionnaryLine.isEmpty()) {
            return linearLayout;
        }
        TextView otherInfo = createOtherInfo(linearLayout);
        otherInfo.setText(dictionnaryLine);
        linearLayout.addView(otherInfo);
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
    private LinearLayout createLineFlexionOf(ViewGroup container)
    {
        return (LinearLayout) ((Activity)getContext()).getLayoutInflater().inflate(
            R.layout.word_flexion,
            container,
            false
        );
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

    /**
     *
     */
    private String getFlexionInfoText(int[] flexionInfos)
    {
        //TODO use a string builder
        String infoLine = "";
        for (int i = 0; i < flexionInfos.length; i++) {
            if (flexionInfos[i] == NO_INFO) {
                continue;
            }
            infoLine += flexionStrings[i][flexionInfos[i]] + " ";
        }
        return infoLine;
    }
}
