package com.example.amusu.teamname_cst2335_final_project;

import android.app.Activity;
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
               AlertDialog.Builder builder = new AlertDialog.Builder(CBCNewsReaderActivity.this);
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

                           }
                       })
                       .show();



           }
       });

//        Intent intent = new Intent(CBCNewsReaderActivity.this,MainActivity.class);
//        startActivity(intent);


    }
}
