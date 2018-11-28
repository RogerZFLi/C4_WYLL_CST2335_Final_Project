package com.example.amusu.teamname_cst2335_final_project.FoodNutrition;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    private EditText searEdit;
    private Button searchButton;
    private Button favoritListButton;
    private ListView searchListV;



    private ArrayAdapter adapter;
    private String search;



    private ArrayList<HashMap<String, String>> searchList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_food_mainpage);
       final ProgressBar progressBar = findViewById(R.id.progressBar);
        searchList = new ArrayList<HashMap<String, String>>();
        searchListV = findViewById(R.id.search_ListView);
        adapter = new MyAdapter(this, R.layout.food_list_item, searchList);
        searchListV.setAdapter(adapter);
        // Edit text
         searEdit = (EditText) findViewById(R.id.searchBox);
        searEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Enter what you are interested in.",
                        Toast.LENGTH_LONG).show();

            }
        });



        // favorit list
        favoritListButton = (Button) findViewById(R.id.favourite);
        favoritListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Do you like it ?", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });



        // search button
         searchButton = (Button) findViewById(R.id.search);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search= searEdit.getText().toString();
                search = search.replace(" ", "%20");
                MyTask newTask = new MyTask(getBaseContext(), searchList, progressBar, adapter);
                newTask.execute(search);
            }
        });

        /*Button button2 = (Button) findViewById(R.id.quit);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(com.example.amusu.teamname_cst2335_final_project.FoodNutrition.FoodMainActivity.this);
                builder.setMessage(R.string.dialog_message)
                        .setTitle(R.string.dialog_title)
                        .setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent resultIntent = new Intent();
                                resultIntent.putExtra("Dialog", "Back");
                                setResult(MainActivity.RESULT_OK, resultIntent);
                                finish();
                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //Intent intent = new Intent(FoodNutritionDatabaseActivity.this, MainActivity.class);
                                //startActivity(intent);
                            }
                        })
                        .show();
            }
        });*/
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
             ImageButton add = item.findViewById(R.id.add_favorit);
                add.setOnClickListener(e->{});

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


