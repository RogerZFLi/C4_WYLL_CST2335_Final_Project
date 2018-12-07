package com.example.amusu.teamname_cst2335_final_project;

import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.amusu.teamname_cst2335_final_project.News;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is for statistical interface fragment
 */
public class CBClocalTotalFragment extends Fragment {
    /**
     * Define a database tool
     */
    DBHelper dbHelper;
    /**
     * Define a TextView that displays statistics
     */
    TextView total;
    /**
     * Define a list control
     */
    ListView listView;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup
            container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fm_cbc_total, container, false);
        //Initialize the content on the view
        initView(view);
        //Return view
        return view;
    }

    /**
     * Initialize interface content
     * @param view
     */

    private void initView(View view) {
        //get database tool
        dbHelper = new DBHelper(getActivity());
        //get the statistics control from the layout file

        total = (TextView) view.findViewById(R.id.total);
        //get the statistics list from the layout file
        listView = (ListView) view.findViewById(R.id.listView);
        //define a list to load all the news
        final List<News> newsList = new ArrayList<News>();

        //define a list to load all the titles
        List<String> titleList = new ArrayList<String>();
        //get database cursor
        Cursor c = dbHelper.query();
        //determine if there is data in the cursor to do the loop
        while (c.moveToNext()) {

            //get the corresponding data according to the field name in the database

            String title = c.getString(c.getColumnIndex("title"));
            String link = c.getString(c.getColumnIndex("link"));
            String guid = c.getString(c.getColumnIndex("guid"));

            String pubDate = c.getString(c.getColumnIndex("pubDate"));
            String author = c.getString(c.getColumnIndex("author"));
            String category = c.getString(c.getColumnIndex("category"));
            String description = c.getString(c.getColumnIndex("description"));

            //用一个News对象来封装查询出来的数据
            News news = new News(title, link, guid, pubDate, author, category, description);
            newsList.add(news);

        }
        //sort the news list
        sortList(newsList);
        //get all the length of the news
        int totals = newsList.size();
        //get the largest length of description
        int max = newsList.get(0).getDescription().length();
        //to the description length is the smallest
        int min = newsList.get(newsList.size() - 1).getDescription().length();

        int sum = 0;
        //calculate the total length
        for (int k=0;k<newsList.size();k++) {
            News n = newsList.get(k);
            titleList.add(n.getTitle());
            sum += n.getDescription().length();
        }
        //get average
        int avg = sum / totals;

        //define the return result

        String reslut = "average:" + avg + "\n" + "max:" + max + "\n" + "min:" + min + "\nnews count:" + totals + "\n\n------------------------------";
        total.setText(reslut);

        //definition list display adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, titleList);
        listView.setAdapter(adapter);
         /**
          * Set unit click event for list
          */
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), CbcDesWeb.class);
                intent.putExtra("data", newsList.get(i).getDescription());
                startActivity(intent);
            }
        });
    }

    /**
     * News list obtained by sorting
     *
     * @param list
     */
    private void sortList(List<News> list) {
        for (int i = 0; i < list.size() - 1; i++) {// 外层循环控制排序趟数
            for (int j = 0; j < list.size() - 1 - i; j++) {// 内层循环控制每一趟排序多少次

                News n1 = list.get(j);
                News n2 = list.get(j+1);

                if (n1.getDescription().length() <n2.getDescription().length()) {
                    News temp = list.get(j);
                    list.set(j, list.get(j + 1));
                    list.set(j + 1, temp);
                }
            }
        }
    }




}
