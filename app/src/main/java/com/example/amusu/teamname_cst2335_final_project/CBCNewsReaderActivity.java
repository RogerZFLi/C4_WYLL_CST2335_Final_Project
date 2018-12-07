package com.example.amusu.teamname_cst2335_final_project;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.amusu.teamname_cst2335_final_project.FoodNutrition.FoodMainActivity;

public class CBCNewsReaderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cbc_news_reader);

        EditText editText =(EditText) findViewById(R.id.searchBoxes);
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Enter what you are interested in.",
                        Toast.LENGTH_LONG).show();
            }
        });
        Button button1 = (Button) findViewById(R.id.quit_button);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CBCNewsReaderActivity.this.finish();

            }
        });




        Button button2 = (Button) findViewById(R.id.save_button);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make( v, "Saving ?", Snackbar.LENGTH_LONG).show();

            }
        });
       Button button3 = (Button) findViewById(R.id.back_button);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CBCNewsReaderActivity.this,CBCMainActivity.class));
            }
        });



    }
}
