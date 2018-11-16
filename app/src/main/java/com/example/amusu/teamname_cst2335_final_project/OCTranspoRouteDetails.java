package com.example.amusu.teamname_cst2335_final_project;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class OCTranspoRouteDetails extends Activity {

    private final static String ACTIVITY_NAME = "OCTranspoRouteDetails";

    private final static String URL = "view-source:https://api.octranspo1.com/v1.2/GetRouteSummaryForStop?appID=223eb5c3&&apiKey=ab27db5b435b8c8819ffb8095328e775&stopNo=3050";
    private TextView routeHeadingText;
    private TextView routeNuberText;
    private TextView routeDirectionText;
    private TextView routeDirectionIDText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_octranspo_route_details);
        Log.i(ACTIVITY_NAME, "In onCreate");

        routeHeadingText = findViewById(R.id.route_heading);
        routeNuberText = findViewById(R.id.route_number);
        routeDirectionText = findViewById(R.id.route_direction);
        routeDirectionIDText = findViewById(R.id.route_direction_id);

        RouteQuery routeQuery = new RouteQuery();
        routeQuery.execute(URL);
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
        private String directionID;

        @Override
        protected String doInBackground(String... args){
            XmlPullParser xmlPullParser = Xml.newPullParser();
            try {
                InputStream in = downloadUrl(URL);
                xmlPullParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                xmlPullParser.setInput(in,"UTF-8");
                xmlPullParser.nextTag();

                while (xmlPullParser.next() != XmlPullParser.END_DOCUMENT){
                    if(xmlPullParser.getEventType()==xmlPullParser.START_TAG) {
                        Log.i(ACTIVITY_NAME, "Iterating the XML tags");
                        System.out.println(xmlPullParser.getName());
                        if (xmlPullParser.getName().equals("RouteNo")) {
                            routeNo = xmlPullParser.getAttributeValue(null, "value");
                            publishProgress(25);
                        }
                        if (xmlPullParser.getName().equals("DirectionID")) {
                            directionID = xmlPullParser.getAttributeValue(null, "value");
                            publishProgress(50);
                        }
                        if (xmlPullParser.getName().equals("Direction")) {
                            direction = xmlPullParser.getAttributeValue(null, "value");
                            publishProgress(75);
                        }
                        if (xmlPullParser.getName().equals("RouteHeading")) {
                            routeHeading = xmlPullParser.getAttributeValue(null, "value");
                            publishProgress(100);
                        }

                    }
                }
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Log.i(ACTIVITY_NAME, "Background processing completed");


            return "Done";
        }

        private InputStream downloadUrl(String urlStr) throws IOException {
            java.net.URL url = new URL(URL);
            HttpURLConnection conn  = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.connect();
            return conn.getInputStream();

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            routeHeadingText.setText("Route Heading: " + routeHeading);
            routeNuberText.setText("Route Number: " + routeNo);
            routeDirectionText.setText("Route Direction: " + direction);
            routeDirectionIDText.setText("Route Direction ID: " + directionID);


        }
    }
}
