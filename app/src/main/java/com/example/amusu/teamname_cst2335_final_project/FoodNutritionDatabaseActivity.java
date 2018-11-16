package com.example.amusu.teamname_cst2335_final_project;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.sax.StartElementListener;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class FoodNutritionDatabaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_food_nutrition_database);

        EditText editText = (EditText) findViewById(R.id.searchBox);
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Enter what you are interested in.",
                        Toast.LENGTH_LONG).show();
                }
        });

                Button button = (Button) findViewById(R.id.favourite);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Snackbar.make(view, "Do you like it ?", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                });

                Button button1 = (Button) findViewById(R.id.search);
                button1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(FoodNutritionDatabaseActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                });

                Button button2 = (Button) findViewById(R.id.quit);
                button2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(FoodNutritionDatabaseActivity.this);
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
                });
            }
        }

