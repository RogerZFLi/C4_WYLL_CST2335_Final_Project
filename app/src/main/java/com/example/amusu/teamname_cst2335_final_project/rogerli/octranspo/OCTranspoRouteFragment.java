package com.example.amusu.teamname_cst2335_final_project.rogerli.octranspo;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import java.util.Calendar;
import java.util.Date;

/**
 * the fragment of trip list layout
 */

public class OCTranspoRouteFragment extends Fragment {

    private final static String ACTIVITY_NAME = "OCTranspoStopFragment";
    private static String routeUrl;
    private Button returnButton;
    private ArrayList<String> tripList;
    private ArrayAdapter<String> tripAdapter;
    private ListView tripListView;
    private ProgressBar loadingProgress;
    private Bundle runningBundle;
    private boolean runningOnPhone;
    private DBHelper dbHelper;
    private Context parent = getActivity();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(ACTIVITY_NAME,"in onCreate");
        runningBundle = this.getArguments();
        runningOnPhone = runningBundle.getBoolean("phone");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        Log.i(ACTIVITY_NAME,"in onCreateView");
        final View result = inflater.inflate(R.layout.activity_octranspo_trip_details,container,false);
        returnButton = result.findViewById(R.id.return_button);
        tripListView = result.findViewById(R.id.trip_listview);
        loadingProgress = result.findViewById(R.id.progress_bar);

        loadingProgress.setVisibility(View.INVISIBLE);
        dbHelper = new DBHelper(result.getContext());
        TripQuery tq = new TripQuery();
        tq.execute();
        tripList = (runningOnPhone? runningBundle.getStringArrayList("tripList2"):runningBundle.getStringArrayList("tripList"));
        tripAdapter = new TripAdapter(result.getContext());

        tripListView.setAdapter(tripAdapter);

        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(result.getContext(), OCTranspoStopFragment.class));
            }
        });


        return result;
    }

    protected class TripAdapter extends ArrayAdapter<String> {

        public TripAdapter(Context ct) {
            super(ct,0);
        }

        @Override
        public int getCount() {
            return tripList.size();
        }

        @Override
        public String getItem(int position) {
            return tripList.get(position);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = getActivity().getLayoutInflater();
            View result = inflater.inflate(R.layout.octranspo_trip_list, null);
            TextView message = result.findViewById(R.id.trip_item);
            message.setText(getItem(position));
            return result;
        }

        @Override
        public long getItemId(int position) {
            return position;
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

        private String estimatedArrivalTime;

        private String latitude;

        private String longitude;

        private String gpsSpeed;

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
            tripList = new ArrayList<>();
            XmlPullParser xmlPullParser = Xml.newPullParser();
            String stopNo = (runningOnPhone?runningBundle.getString("stopNo"): runningBundle.getString("chosenStop"));
            String routeNo = (runningOnPhone?runningBundle.getString("routeNo").substring(8,11): runningBundle.getString("chosenRoute").substring(8,11));
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
                String tagName = null;
                xmlPullParser.nextTag();


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
                                    + "Estimated Next Trip Arrive Time: " +estimatedTime(tripStartTime, adjustedScheduleTime) +"\n"
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
            loadingProgress.setVisibility(View.VISIBLE);
            super.onProgressUpdate(values);
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            loadingProgress.setVisibility(View.INVISIBLE);
            Log.i(ACTIVITY_NAME, "onPostExecute");
            tripAdapter.notifyDataSetChanged();
        }
    }
}
