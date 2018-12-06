package com.example.amusu.teamname_cst2335_final_project.FoodNutrition;


import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.amusu.teamname_cst2335_final_project.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class FoodMainActivity extends AppCompatActivity {
    private EditText sEdit;
    private Button sButton;
    private Button favorListButton, about;
    private ListView searchListView;
    private ArrayAdapter adapter;
    private String search;
    private ArrayList<HashMap<String, String>> searchList;
    private DataBaseHelper dbhelper = new DataBaseHelper(this);

    public static SQLiteDatabase getDb() {
        return db;
    }

    private static SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_food_mainpage);


        db = dbhelper.getWritableDatabase();
       final ProgressBar progressBar = findViewById(R.id.progressBar);
        searchList = new ArrayList<HashMap<String, String>>();
        searchListView = findViewById(R.id.search_ListView);
        adapter = new MyAdapter(this, R.layout.food_list_item, searchList);
        searchListView.setAdapter(adapter);

         sEdit = (EditText) findViewById(R.id.searchBox);
        sEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Enter what you are interested in.",
                        Toast.LENGTH_LONG).show();
            }
        });

        searchListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View b, int c, long d) {
                HashMap<String, String> item = (HashMap<String, String>) adapter.getItem(c);
                ContentValues cv = new ContentValues();
                cv.put("label", item.get(""));
            }
        });


        favorListButton = (Button) findViewById(R.id.favourite);
        favorListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Do you like it ?", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        sButton = (Button) findViewById(R.id.search);
        sButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search= sEdit.getText().toString();
                search = search.replace(" ", "%20");
                MyTask newTask = new MyTask(getBaseContext(), searchList, progressBar, adapter);
                newTask.execute(search);
            }
        });

        about = findViewById(R.id.about);
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View E) {
                Intent toAbout = new Intent(FoodMainActivity.this, activity_food_help.class);
                FoodMainActivity.this.startActivity(toAbout);
            }
        });
    }


     class MyAdapter extends ArrayAdapter<HashMap<String, String>>{
        private int resourceID;

        public MyAdapter( @NonNull Context context, int resource,  @NonNull List<HashMap<String, String>> objects) {
            super(context, resource, objects);
            resourceID = resource;
        }


         @Nullable
         @Override
         public HashMap<String, String> getItem(int position) {
             return searchList.get(position);
         }

         @NonNull
         @Override
         public View getView(int position,  View convertView, ViewGroup parent) {
                    HashMap<String, String> food = getItem(position);
                View item = LayoutInflater.from(getContext()).inflate(resourceID,null);
             TextView label1 =item.findViewById(R.id.foodLabel);
             label1.setText(food.get("Label"));
             TextView label2 =item.findViewById(R.id.caloriesV);
             label2.setText(food.get("Calories"));
             TextView label3 =item.findViewById(R.id.fatV);
             label3.setText(food.get("Fat"));
             TextView label4 =item.findViewById(R.id.carbsV);
             label4.setText(food.get("Carbs"));
             TextView label5 =item.findViewById(R.id.fiberV);
             label5.setText(food.get("Fiber"));
             ImageButton add = item.findViewById(R.id.add_favorit);
                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View e) {
                    }
                });
             return item;
         }
     }

    public ArrayList<HashMap<String, String>> getSearchList() {
        return searchList;
    }
    public ArrayAdapter getAdapter() {
        return adapter;
    }
}


