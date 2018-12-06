package com.example.amusu.teamname_cst2335_final_project;

import android.app.Fragment;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class CBClocalTotalFragment extends Fragment {
    DBHelper dbHelper;
    TextView total;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup
            container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fm_cbc_total, container, false);
        initView(view);
        return view;
    }

    private void initView(View view){
        dbHelper = new DBHelper(getActivity());
        total = (TextView) view.findViewById(R.id.total);
        List<News> newsList = new ArrayList<News>();
        Cursor c = dbHelper.query();
        while (c.moveToNext()) {
            String title = c.getString(c.getColumnIndex("title"));
            String link = c.getString(c.getColumnIndex("link"));
            String guid = c.getString(c.getColumnIndex("guid"));
            String pubDate = c.getString(c.getColumnIndex("pubDate"));
            String author = c.getString(c.getColumnIndex("author"));
            String category = c.getString(c.getColumnIndex("category"));
            String description = c.getString(c.getColumnIndex("description"));

            //use News object to retrieve data
            News news = new News(title, link, guid, pubDate, author, category, description);
            newsList.add(news);

        }
        sortList(newsList);
        int totals = newsList.size();
        int max = newsList.get(0).getDescription().length();
        int min = newsList.get(newsList.size() - 1).getDescription().length();
        int sum = 0;
        for (News n : newsList) {
            sum += n.getDescription().length();
        }
        int avg = sum / totals;
        String reslut = "average:" +
                avg + "\n" + "max:" +
                max + "\n" + "min:" +
                min + "\nnews count:" +
                totals;
        total.setText(reslut);
    }



    private void sortList(List<News> list) {
        //Outer loop control sorting number
        for (int i = 0; i < list.size() - 1; i++) {
            //Inner loop control how many times each order is sorted
            for (int j = 0; j < list.size() - 1 - i; j++) {
                if (list.get(j).getDescription().length() < list.get(j + 1).getDescription().length()) {
                    News temp = list.get(j);
                    list.set(j, list.get(j + 1));
                    list.set(j + 1, temp);
                }
            }
        }
    }
}
