package com.example.amusu.teamname_cst2335_final_project;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


import com.example.amusu.teamname_cst2335_final_project.rogerli.octranspo.OCTranspoBusRouteActivity;

import com.example.amusu.teamname_cst2335_final_project.FoodNutrition.FoodMainActivity;
import com.example.amusu.teamname_cst2335_final_project.movie.MovieInformationActivity;



public class MainActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Button FOOD_NUTRITION_DATABASE = findViewById(R.id.food_nutrition_database);
        final Button CBC_NEWS_READER = findViewById(R.id.cbc_news_reader);
        final Button MOVIE_INFORMATION = findViewById(R.id.movie_information);
        final Button OCTRANSPO_BUS_ROUTE = findViewById(R.id.octranspo_bus_route);



        FOOD_NUTRITION_DATABASE.setOnClickListener(new View.OnClickListener() {
                @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,FoodMainActivity.class));
            }
        });

        CBC_NEWS_READER.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,CBCNewsReaderActivity.class));
            }
        });

        MOVIE_INFORMATION.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,MovieInformationActivity.class));
            }
        });

        OCTRANSPO_BUS_ROUTE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,OCTranspoBusRouteActivity.class));
            }
        });


    }
}
