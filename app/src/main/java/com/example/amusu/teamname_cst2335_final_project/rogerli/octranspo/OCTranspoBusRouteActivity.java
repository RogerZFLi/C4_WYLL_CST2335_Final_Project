package com.example.amusu.teamname_cst2335_final_project.rogerli.octranspo;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.amusu.teamname_cst2335_final_project.CBCNewsReaderActivity;
import com.example.amusu.teamname_cst2335_final_project.FoodNutrition.FoodMainActivity;
import com.example.amusu.teamname_cst2335_final_project.MainActivity;
import com.example.amusu.teamname_cst2335_final_project.movie.MovieInformationActivity;
import com.example.amusu.teamname_cst2335_final_project.R;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 *Main interface of OCTranspo searching tool
 */
public class OCTranspoBusRouteActivity extends AppCompatActivity {
    /**
     * Name of this layout
     */
    private final static String ACTIVITY_NAME = "OCTranspoBusRoute";

    /**
     * the URL of data source
     */

    private static String stopUrl;


    /**
     *List view of showing searched stop numbers
     */
    private ListView searchingHistory;

    /**
     *the text field to type stop  number
     */
    private EditText enterToSearch;

    /**
     * the entered stop number
     */

    private String enteredStopNo;

    /**
     *the toolbar showing the entrances of other apps and return and help menu items
     */
    private Toolbar octranspoToolbar;
    /**
     *the button to search
     */
    private Button searchButton;

    /**
     * the button to show searched history
     */

    private Button showSearchHistoryButton;

    /**
     * stop number list
     */

    private ArrayList<String> stopInfo;

    /**
     * route number list at a specific stop
     */
    private ArrayList<String> routeList;


    /**
     * the adapter to show stop number list
     */

    private ListViewAdapter stopInfoAdapter;

    /**
     * Dialog builder
     */
    private AlertDialog.Builder builder;
    /**
     * Dialog showing when clicking a button or menu item
     */
    private AlertDialog dialog;

    /**
     * Database helper to create or manipulate a database
     */

    private DBHelper dbHelper;

    /**
     * to verify if the frame exists
     */

    private boolean frameExists;

    /**
     * the stop number choosing to pass to anther layouts
     */

    private String chosenStopNo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_octranspo_bus_route);

        Log.i(ACTIVITY_NAME, "In onCreate");
        final Context context = this;
        dbHelper = new DBHelper(context);
        frameExists = findViewById(R.id.detail_frame)!=null;
        searchButton = findViewById(R.id.get_stop_button);
        searchingHistory = findViewById(R.id.list_view);

        enterToSearch = findViewById(R.id.input_stop_number);
        final SharedPreferences sp = getSharedPreferences("search stops", Context.MODE_PRIVATE);
        enterToSearch.setText(sp.getString("initialInput", getString(R.string.initial_input)));
        octranspoToolbar = findViewById(R.id.octranspo_toolbar);
        routeList = new ArrayList<>();
        setSupportActionBar(octranspoToolbar);
        stopInfoAdapter = new ListViewAdapter(this);
        stopInfo = new ArrayList<>();
        builder = new AlertDialog.Builder(this);

        showSearchHistoryButton = findViewById(R.id.show_search_history_button);

        showSearchHistoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(searchingHistory.getAdapter()==null) {
                    stopInfo = dbHelper.getAllStops();
                    searchingHistory.setAdapter(stopInfoAdapter);
                    showSearchHistoryButton.setText(R.string.oc_transpo_hide_search_history);
                }else {
                    searchingHistory.setAdapter(null);
                    showSearchHistoryButton.setText(R.string.octranspo_show_search_history);
                }
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Snackbar.make(searchButton, "Searching routes", Snackbar.LENGTH_LONG).show();
                CharSequence verifyInput;
                int duration;
                SharedPreferences.Editor edit = sp.edit();
                edit.putString("initialInput", enterToSearch.getText().toString());
                edit.apply();
                enteredStopNo = enterToSearch.getText().toString();
                if (isNumeric(enteredStopNo)) {
                    verifyInput = "Search complete!";
                    duration = Toast.LENGTH_LONG;
                    stopInfo.add(enteredStopNo);
                    dbHelper.insertToStations(enteredStopNo);
                    stopInfoAdapter.notifyDataSetChanged();
                    chosenStopNo = enteredStopNo;
                    RouteQuery rq = new RouteQuery();
                    rq.execute();
                } else {
                    verifyInput = "Invalid input!";
                    duration = Toast.LENGTH_SHORT;
                }

                Toast toast = Toast.makeText(OCTranspoBusRouteActivity.this, verifyInput, duration);
                toast.show();
                enterToSearch.setText(null);
                if(frameExists) {
                    OCTranspoStopFragment of = new OCTranspoStopFragment();
                    Bundle fragmentArgs = new Bundle();
                    fragmentArgs.putString("chosenStop", enteredStopNo);
                    fragmentArgs.putStringArrayList("routeList", routeList);
                    fragmentArgs.putBoolean("isFrame", true);
                    of.setArguments(fragmentArgs);
                    FragmentManager fm = getFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.replace(R.id.detail_frame,of);
                    ft.commit();
                }else {
                    Intent intent = new Intent(OCTranspoBusRouteActivity.this, OCTranspoRouteDetails.class);
                    intent.putExtra("chosenStop", enteredStopNo);
                    intent.putStringArrayListExtra("routeList", routeList);

                    startActivity(intent);
                }
            }
        });

        searchingHistory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                chosenStopNo = stopInfo.get(position);
                RouteQuery rq = new RouteQuery();
                rq.execute();

                if(frameExists) {

                    OCTranspoStopFragment of = new OCTranspoStopFragment();
                    Bundle fragmentArgs = new Bundle();
                    fragmentArgs.putString("chosenStop", stopInfo.get(position));
                    fragmentArgs.putStringArrayList("routeList", routeList);
                    fragmentArgs.putBoolean("isFrame", true);
                    of.setArguments(fragmentArgs);
                    FragmentManager fm = getFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.replace(R.id.detail_frame,of);
                    ft.commit();
                }else {

                    Log.d("Item clicked", "details will be shown");
                    builder.setTitle(R.string.octranspo_dialog_details);
                    builder.setPositiveButton(R.string.octranspo_ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent resultIntent = new Intent(OCTranspoBusRouteActivity.this,OCTranspoRouteDetails.class);
                            resultIntent.putExtra("chosenStop", stopInfo.get(position));
                            resultIntent.putStringArrayListExtra("routeList", routeList);
                            startActivityForResult(resultIntent, 11);
                            finish();
                        }
                    });
                    builder.setNegativeButton(R.string.octranspo_cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User cancelled the dialog
                        }
                    });
                    dialog = builder.create();
                    dialog.show();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.octranspo_toolbar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem mi) {
        builder =new AlertDialog.Builder(OCTranspoBusRouteActivity.this);
        switch (mi.getItemId()) {
            case R.id.action_cbc:
                Log.i("Toolbar", "going to cbc news app");
                startActivity(new Intent(OCTranspoBusRouteActivity.this, CBCNewsReaderActivity.class));
                return true;
            case R.id.action_food:
                Log.i("Toolbar", "going to food nutrition app");
                startActivity(new Intent(OCTranspoBusRouteActivity.this, FoodMainActivity.class));
                return true;
            case R.id.action_movie:
                Log.i("Toolbar", "going to movie app");

                startActivity(new Intent(OCTranspoBusRouteActivity.this, MovieInformationActivity.class));
                return true;
            case R.id.action_return:
                Log.i("Toolbar", "return to main window");
                startActivity(new Intent(OCTranspoBusRouteActivity.this, MainActivity.class));
                return true;
            case R.id.action_help:
                Log.i("Toolbar", "help message");
                builder.setTitle(getString(R.string.octranspo_info))
                        .setMessage(getString(R.string.octranspo_app_name)+getString(R.string.octranspo_app_version)+getString(R.string.octranspo_details)).setPositiveButton(R.string.octranspo_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {}

                }).create().show();
                return true;
            default:
                return super.onOptionsItemSelected(mi);
        }

    }

    /**
     * to verify if the input data is numeric
     * @param str input data
     * @return true if input is numeric
     */
    private boolean isNumeric(String str){
        Pattern pattern = Pattern.compile("[0-9]*");

        return pattern.matcher(str).matches();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbHelper.closeDB();
    }

    /**
     *ListView adapter class
     */
    protected class ListViewAdapter extends ArrayAdapter {

        public ListViewAdapter(Context ct) {
            super(ct,0);
        }

        @Override
        public int getCount() {
            return stopInfo.size();
        }

        @Override
        public String getItem(int position) {
            return stopInfo.get(position);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = OCTranspoBusRouteActivity.this.getLayoutInflater();
            View result = inflater.inflate(R.layout.octranspo_stop_list, null);
            TextView message = result.findViewById(R.id.item_text);
            message.setText(getItem(position));
            return result;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }
    }

    /**
     * to delete the input stop number
     * @param stopNo -input stop number
     */
    protected void deleteStop(String stopNo) {
        dbHelper.deleteStation(stopNo);
        stopInfo = new ArrayList<>();
        stopInfo = dbHelper.getAllStops();
        stopInfoAdapter.notifyDataSetChanged();
    }


    /**
     * query class to get data from the website ( data source)
     */

    private class RouteQuery extends AsyncTask<String,Integer,String> {

        /**
         *
         */
        private String routeHeading;
        /**
         *
         */
        private String routeNo;
        /**
         *
         */
        private String direction;

        /**
         *
         */
        private String errorMessage;

        /**
         *
         * @param args
         * @return
         */

        @Override
        protected String doInBackground(String... args){
            XmlPullParser xmlPullParser = Xml.newPullParser();
            errorMessage = null;
            String stopNo = chosenStopNo;
//            stopUrl = args[0] + stopNo;

            stopUrl = "https://api.octranspo1.com/v1.2/GetRouteSummaryForStop?appID=223eb5c3&&apiKey=ab27db5b435b8c8819ffb8095328e775&stopNo="+stopNo.trim();
//            stopNo=null;
            try {
                java.net.URL url = new URL(stopUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                conn.connect();
                InputStream in = conn.getInputStream();
                xmlPullParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                xmlPullParser.setInput(in,"UTF-8");
                xmlPullParser.nextTag();
                String tagName = null;

                while (xmlPullParser.next() != XmlPullParser.END_DOCUMENT){
                    tagName = xmlPullParser.getName();
                    if(xmlPullParser.getEventType()==xmlPullParser.START_TAG) {
                        Log.i(ACTIVITY_NAME, "Iterating the XML tags");

                        if( tagName.equals("RouteNo")) {
                            if (xmlPullParser.next() == xmlPullParser.TEXT)
                                routeNo = xmlPullParser.getText();
                        }else if(tagName.equals("Direction")) {
                            if(xmlPullParser.next()==xmlPullParser.TEXT)
                                direction = xmlPullParser.getText();
                        }else if (tagName.equals("RouteHeading")) {
                            if(xmlPullParser.next()==xmlPullParser.TEXT) {
                                routeHeading = xmlPullParser.getText();
                            }
                        }

                    }else if (xmlPullParser.getEventType()==xmlPullParser.END_TAG) {
                        if(tagName.equals("Route")) {
                            routeList.add("RouteNo: " + routeNo + " " + "RouteHeading: " + routeHeading);
                            routeNo = null;
                            direction = null;
                            routeHeading = null;
                            publishProgress(20%100);
                        }
                    }

                }
                publishProgress(100);
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Log.i(ACTIVITY_NAME, "Background processing completed");


            return "Done";
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.i(ACTIVITY_NAME, "onPostExecute");
//            routeListAdapter.notifyDataSetChanged();

        }
    }


}
