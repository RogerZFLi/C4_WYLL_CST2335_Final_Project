package com.example.amusu.teamname_cst2335_final_project;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 *
 */
public class OCTranspoBusRouteActivity extends AppCompatActivity {
    private final static String ACTIVITY_NAME = "OCTranspoBusRoute";


    /**
     *
     */
    private ListView searchingHistory;
    /**
     *
     */
    private ProgressBar loadingProgress;
    /**
     *
     */
    private EditText enterToSearch;
    /**
     *
     */
    private Button getRouteButton;


    private ArrayList<String> routeInfo;

    private ListViewAdapter routeInfoAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_octranspo_bus_route);

        Log.i(ACTIVITY_NAME, "In onCreate");


        getRouteButton = findViewById(R.id.get_route_button);
        searchingHistory = findViewById(R.id.list_view);
        loadingProgress = findViewById(R.id.progress_bar);
        enterToSearch = findViewById(R.id.input_line_number);
        getRouteButton = findViewById(R.id.get_route_button);
        routeInfoAdapter = new ListViewAdapter(this, routeInfo);
        routeInfo = new ArrayList<>();
        loadingProgress.setVisibility(View.VISIBLE);
        final Context context = this;


        getRouteButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Snackbar.make(getRouteButton,"Searching routes",Snackbar.LENGTH_LONG).show();
                CharSequence verifyInput;
                int duration;

                if(isNumeric(enterToSearch.getText().toString())){
                    verifyInput = "Search complete!";
                    duration = Toast.LENGTH_LONG;
                    routeInfo.add(enterToSearch.getText().toString());
                }else {
                    verifyInput = "Invalid input!";
                    duration = Toast.LENGTH_SHORT;
                }
                Toast toast = Toast.makeText(OCTranspoBusRouteActivity.this,verifyInput,duration);
                toast.show();

                enterToSearch.setText(null);
            }

        });
        searchingHistory.setAdapter(routeInfoAdapter);




        searchingHistory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //from: https://stackoverflow.com/questions/16982218/launch-a-custom-dialog-when-clicked-on-notification
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.layout_dialog);
                dialog.setTitle("Get Route Details");
                TextView text = (TextView) dialog.findViewById(R.id.dialog_text);
                text.setText("Show Route Details");

                Button dialogButton = (Button) dialog.findViewById(R.id.dialog_buttton);
                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        startActivity(new Intent(OCTranspoBusRouteActivity.this, OCTranspoRouteDetails.class));
                    }
                });

                dialog.show();

            }
        });

    }

    private boolean isNumeric(String str){
        Pattern pattern = Pattern.compile("[0-9]*");

        return pattern.matcher(str).matches();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     *
     */
    protected class ListViewAdapter extends BaseAdapter {
        private Context cxt;

        public ListViewAdapter(Context ct, ArrayList<String> list) {
            this.cxt = ct;
            routeInfo = list;
        }

        @Override
        public int getCount() {
            return routeInfo.size();
        }

        @Override
        public String getItem(int position) {
            return routeInfo.get(position);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = OCTranspoBusRouteActivity.this.getLayoutInflater();
            View result = inflater.inflate(R.layout.octranspo_list_route_items, null);
            TextView message = result.findViewById(R.id.item_text);
            message.setText(getItem(position));
            return result;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }
    }
}
