package com.example.amusu.teamname_cst2335_final_project.rogerli.octranspo;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.amusu.teamname_cst2335_final_project.R;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * the fragment of the route list layout
 */

public class OCTranspoStopFragment extends Fragment {

    private final static String ACTIVITY_NAME = "OCTranspoStopFragment";
    private static String stopUrl;
    private static String routeUrl;

    private Button deleteStopButton;
    private Button returnButton;
    private ArrayList<String> routeList;
    private ArrayList<String> tripList;

    private ArrayAdapter<String> routeAdapter;

    private ListView routeListView;
    private ProgressBar loadingProgress;
    private Bundle runningBundle;
    private boolean runningOnPhone;
    private DBHelper dbHelper;
    private boolean frameExists;
    private  String routeNum;
    private SQLiteDatabase db;

    private Context parent = getActivity();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(ACTIVITY_NAME,"in onCreate");
        runningBundle = this.getArguments();
        runningOnPhone = runningBundle.getBoolean("phone");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        Log.i(ACTIVITY_NAME,"in onCreateView");
        final View result = inflater.inflate(R.layout.activity_octranspo_route_details,container,false);
        deleteStopButton = result.findViewById(R.id.deleteStop_button);
        returnButton = result.findViewById(R.id.button_return);
        routeListView = result.findViewById(R.id.route_listview);
        loadingProgress = result.findViewById(R.id.progress_bar);

        loadingProgress.setVisibility(View.INVISIBLE);
        frameExists = result.findViewById(R.id.trip_detail_frame)!=null;
        dbHelper = new DBHelper(result.getContext());
        routeList = new ArrayList<>();
        tripList = new ArrayList<>();
        RouteQuery rq = new RouteQuery();
        rq.execute();

        routeList = (runningOnPhone? runningBundle.getStringArrayList("routeList2"):runningBundle.getStringArrayList("routeList"));
        routeAdapter = new RouteAdapter(result.getContext());

        routeListView.setAdapter(routeAdapter);


        routeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                Log.d("Item clicked", "details will be shown");
                TripQuery tq = new TripQuery();
                tq.execute();
                String chosenStop = null;
                if(frameExists) {
                    OCTranspoRouteFragment of = new OCTranspoRouteFragment();
                    routeNum = routeList.get(position);
                    Bundle fragmentArgs = new Bundle();
                    chosenStop = runningBundle.getString("chosenStop");
                    fragmentArgs.putString("chosenStop",chosenStop);
                    fragmentArgs.putString("chosenRoute", routeList.get(position));
                    fragmentArgs.putStringArrayList("tripList", tripList);
                    fragmentArgs.putBoolean("isFrame", true);
                    of.setArguments(fragmentArgs);
                    FragmentManager fm = getFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.replace(R.id.trip_detail_frame,of);
                    ft.commit();
                }else{
                    Intent resultIntent = new Intent(getActivity(), OCTranspoTripDetails.class);
                    routeNum = routeList.get(position);
                    chosenStop = runningBundle.getString("stopNo");

                    resultIntent.putExtra("stopNo", chosenStop);
                    resultIntent.putExtra("chosenRoute", routeList.get(position));
                    resultIntent.putStringArrayListExtra("tripList", tripList);
                    startActivityForResult(resultIntent, 22);
                }
            }
        });

        deleteStopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!runningOnPhone){
                    OCTranspoBusRouteActivity oc = (OCTranspoBusRouteActivity) result.getContext();
                    oc.deleteStop(runningBundle.getString("choenStop"));
                    FragmentManager fm = getFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.remove(OCTranspoStopFragment.this).commit();
                }else{
                    dbHelper.deleteStation(runningBundle.getString("stopNo"));
                    routeAdapter.notifyDataSetChanged();
                    startActivity(new Intent(result.getContext(), OCTranspoBusRouteActivity.class));
                }
            }
        });



       if(!runningOnPhone) {
           returnButton.setVisibility(View.INVISIBLE);
       } else {
           returnButton.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   startActivity(new Intent(result.getContext(), OCTranspoBusRouteActivity.class));
               }

           });
       }

        return result;
    }




    protected class RouteAdapter extends ArrayAdapter<String> {

        public RouteAdapter(Context ct) {
            super(ct,0);
        }

        @Override
        public int getCount() {
            return routeList.size();
        }

        @Override
        public String getItem(int position) {
            return routeList.get(position);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = getActivity().getLayoutInflater();
            View result = inflater.inflate(R.layout.octranspo_route_list, null);
            TextView message = result.findViewById(R.id.route_item);
            message.setText(getItem(position));
            return result;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }
    }

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
         * @param args
         * @return
         */

        @Override
        protected String doInBackground(String... args) {
            XmlPullParser xmlPullParser = Xml.newPullParser();
            errorMessage = null;
            routeList = new ArrayList<>();
            String stopNo = (runningOnPhone? runningBundle.getString("stopNo"): runningBundle.getString("chosenStop"));
//            stopUrl = args[0] + stopNo;

            stopUrl = "https://api.octranspo1.com/v1.2/GetRouteSummaryForStop?appID=223eb5c3&&apiKey=ab27db5b435b8c8819ffb8095328e775&stopNo=" + stopNo.trim();
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
                xmlPullParser.setInput(in, "UTF-8");
                String tagName = null;
                xmlPullParser.nextTag();


                while (xmlPullParser.next() != XmlPullParser.END_DOCUMENT) {
                    tagName = xmlPullParser.getName();
                    if (xmlPullParser.getEventType() == xmlPullParser.START_TAG) {
                        Log.i(ACTIVITY_NAME, "Iterating the XML tags");

                        if (tagName.equals("RouteNo")) {
                            if (xmlPullParser.next() == xmlPullParser.TEXT)
                                routeNo = xmlPullParser.getText();
                        } else if (tagName.equals("Direction")) {
                            if (xmlPullParser.next() == xmlPullParser.TEXT)
                                direction = xmlPullParser.getText();
                        } else if (tagName.equals("RouteHeading")) {
                            if (xmlPullParser.next() == xmlPullParser.TEXT) {
                                routeHeading = xmlPullParser.getText();
                            }
                        }

                    } else if (xmlPullParser.getEventType() == xmlPullParser.END_TAG) {
                        if (tagName.equals("Route")) {
                            routeList.add("RouteNo: " + routeNo + " " + "RouteHeading: " + routeHeading);
                            routeNo = null;
                            direction = null;
                            routeHeading = null;
                            publishProgress(20 % 100);
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
            routeAdapter.notifyDataSetChanged();

        }
    }

    private String estimatedTime(String startTime, String adustNum) {
        String[] timePieces = startTime.split(":");
        int hour = Integer.valueOf(timePieces[0].trim());
        int minute = Integer.valueOf(timePieces[1].trim());
        int tempMin = minute + Integer.valueOf(adustNum.trim());
        if(tempMin < 60) {
            minute = tempMin;
        }else {
            minute = tempMin - 60;
            if(hour >=23) {
                hour = hour -24 + 1;
            }else
                hour +=1;
        }
        String h = null;
        String m = null;
        if(hour < 10) {
            h = "0" + hour;
        }else
            h = Integer.toString(hour);

        if (minute < 10) {
            m = "0" + minute;
        }else
            m = Integer.toString(minute);
        return h +":" + m;

    }

//
    private class TripQuery extends AsyncTask<String,Integer,String> {

        /**
         *
         */
        private String tripDestination;
        /**
         *
         */
        private String tripStartTime;
        /**
         *
         */
        private String adjustedScheduleTime;


        private String latitude;

        private String longitude;

        private String gpsSpeed;



        /**
         *
         * @param args
         * @return
         */

        @Override
        protected String doInBackground(String... args){
            tripList = new ArrayList<>();

            XmlPullParser xmlPullParser = Xml.newPullParser();
            String stopNo = (runningOnPhone?runningBundle.getString("stopNo"): runningBundle.getString("chosenStop"));
            String routeNo = (runningOnPhone?routeNum.substring(8,11): runningBundle.getString("chosenRoute").substring(8,11));
            String stopLabel = null;


            routeUrl = "https://api.octranspo1.com/v1.2/GetNextTripsForStop?appID=223eb5c3&&apiKey=ab27db5b435b8c8819ffb8095328e775&stopNo="+stopNo.trim()+"&routeNo="+routeNo.trim();
    //            stopNo=null;
    //            routeNo=null;
            try {
                java.net.URL url = new URL(routeUrl);
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
    //            boolean stopLabelAdded = false;


                while (xmlPullParser.next() != XmlPullParser.END_DOCUMENT){
                    tagName = xmlPullParser.getName();
                    if(xmlPullParser.getEventType()==xmlPullParser.START_TAG) {
                        Log.i(ACTIVITY_NAME, "Iterating the XML tags");

                        if (tagName.equals("StopLabel")){
                            if(stopLabel==null) {
                                if (xmlPullParser.next() == xmlPullParser.TEXT)
                                    stopLabel = xmlPullParser.getText();
                            }
                        }else if( tagName.equals("TripDestination")) {
                            if (xmlPullParser.next() == xmlPullParser.TEXT)
                                tripDestination = xmlPullParser.getText();
                        }else if(tagName.equals("TripStartTime")) {
                            if(xmlPullParser.next()==xmlPullParser.TEXT)
                                tripStartTime = xmlPullParser.getText();
                        }else if (tagName.equals("AdjustedScheduleTime")) {
                            if(xmlPullParser.next()==xmlPullParser.TEXT) {
                                adjustedScheduleTime = xmlPullParser.getText();
                            }
                        }else if(tagName.equals("Latitude")){
                            if(xmlPullParser.next()==xmlPullParser.TEXT)
                                latitude = xmlPullParser.getText();
                        }else if(tagName.equals("Longitude")){
                            if(xmlPullParser.next()==xmlPullParser.TEXT)
                                longitude = xmlPullParser.getText();
                        }else if(tagName.equals("GPSSpeed")) {
                            if(xmlPullParser.next()==xmlPullParser.TEXT)
                                gpsSpeed = xmlPullParser.getText();
                        }


                    }else if (xmlPullParser.getEventType()==xmlPullParser.END_TAG) {
                        if(tagName.equals("Trip")) {
                            tripList.add("Route Number" + routeNo +"\n"
                                    + "Destination: " + tripDestination + "\n"
                                    + "StartTime: " + tripStartTime +"\n"
                                    + "AdjustedScheduleTime: " + adjustedScheduleTime +"\n"
                                    + "Estimated Next Trip Arrive Time: " + estimatedTime(tripStartTime, adjustedScheduleTime) +"\n"
                                    + "Latitude /Longitude: " + latitude + " /" +longitude +"\n"
                                    + "GPS Speed: " + gpsSpeed +" km/h");
                            tripDestination = null;
                            tripStartTime = null;
                            adjustedScheduleTime = null;
                            latitude = null;
                            longitude = null;
                            gpsSpeed = null;
                            publishProgress(33);
                        }
                    }

                }
                tripList.add(0, "Stop Number: " + stopNo + "\n" + "Station Name: " + stopLabel +"\n");
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
    //        routeAdapter.notifyDataSetChanged();
        }
    }

}
