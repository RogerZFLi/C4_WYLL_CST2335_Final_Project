package com.example.amusu.teamname_cst2335_final_project;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MovieInformationActivity extends AppCompatActivity {
    ListView movieListView;
    ArrayList<String> movieMessage;
//    MovieListViewAdapter movieListAdapter;
    protected Button searchBtn;
    String searchStr = "Find your favourite movies!";

    protected EditText searchEditText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_information);

        searchBtn = (Button)findViewById(R.id.searchBtnID);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), searchStr,Toast.LENGTH_LONG).show();
            }
        });

        searchEditText = (EditText)findViewById(R.id.searchEditTextID);
        searchEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar snackbar = Snackbar.make(searchEditText,"Please check your enter",
                                                    Snackbar.LENGTH_LONG);
                Snackbar.make(searchEditText,"check your enter",Snackbar.LENGTH_LONG).show();
                snackbar.setAction("OK", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(MovieInformationActivity.this, "action click",
                                Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

 }

//    private class MovieListViewAdapter extends ArrayAdapter<String> {
//        public MovieListViewAdapter(Context ctx) {
//            super(ctx, 0);
//        }
//
//        public int getCount() {
//            return movieMessage.size();
//        }
//
//        public String getItem(int position) {
//            return movieMessage.get(position);
//        }
//
//        public View getView(int position, View convertView, ViewGroup parent) {
//            LayoutInflater inflater = MovieInformationActivity.this.getLayoutInflater();
//
//            View result;
//            if (position % 2 == 0) {
//                result = inflater.inflate(R.layout.chat_row_incoming, null);
//                TextView messageIn = result.findViewById(R.id.messageTextIn);
//                messageIn.setText(getItem(position)); // get the string at position
//            } else {
//                result = inflater.inflate(R.layout.chat_row_outgoing, null);
//                TextView messageOut =  result.findViewById(R.id.messageTextOut);
//                messageOut.setText(getItem(position)); // get the string at position
//            }
//            return result;
//        }
//    }
}
