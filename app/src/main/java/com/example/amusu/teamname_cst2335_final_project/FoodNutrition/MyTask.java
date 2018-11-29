package com.example.amusu.teamname_cst2335_final_project.FoodNutrition;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;

import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;

import android.widget.Toast;

import com.example.amusu.teamname_cst2335_final_project.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class MyTask extends AsyncTask<String, Integer, String>  {

    private static final  String ACTIVITY_NAME = "MYTASK";
    JSONArray jArray;
    private Context mainPage;
    private ArrayList<HashMap<String, String>> foodItemList;
    private String text;
    private ProgressBar progress;
    private ArrayAdapter adapter;

   public MyTask(Context ctx, ArrayList<HashMap<String, String>> list, ProgressBar pbar, ArrayAdapter adapter){
       mainPage = ctx;
       foodItemList = list;
       progress = pbar;
       this.adapter = adapter;

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        /*Snackbar.make(((FoodMainActivity) mainPage).findViewById(android.R.id.content),R.string.jsonDL, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();*/


    }

    @Override
    protected String doInBackground(String... strings) {
        //whenever new search, clear list first
        foodItemList.clear();
            text = strings[0];
        try {
            URL url = new URL("https://api.edamam.com/api/food-database/parser?app_id=e5bc806d&app_key=5f7521ffeefe491b936cea6271e13d3d&ingr=" + text);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            InputStream response = urlConnection.getInputStream();

            BufferedReader reader = new BufferedReader(new InputStreamReader(response, "UTF-8"), 8);
            StringBuilder jsonResults = new StringBuilder();

            String line = null;
            while ((line = reader.readLine()) != null) {
                jsonResults.append(line + "\n");
            }
            String result = jsonResults.toString();



            JSONObject jsonObj = new JSONObject(result);
            publishProgress(20);

            jArray = jsonObj.getJSONArray("hints");

            for (int index = 0; index < jArray.length(); index++)
                try {
                    JSONObject indexObject = jArray.getJSONObject(index);
                    JSONObject foodObject = indexObject.getJSONObject("food");
                    // Pulling items from the array
                    String  label = foodObject.getString("label");
                    //打印 LABEL

                    publishProgress(20);
                    JSONObject nutriObject = foodObject.getJSONObject("nutrients");
                    Log.i(ACTIVITY_NAME, nutriObject.toString());
                    publishProgress(60);
                    String calorieValue = nutriObject.getString("ENERC_KCAL");

                    publishProgress(80);
                    String  fatValue = nutriObject.getString("FAT");

                    publishProgress(90);
                    String  carbValue = nutriObject.getString("CHOCDF");

                    publishProgress(100);

                    HashMap<String, String> food = new HashMap<>();
                    food.put("Label", label);
                    food.put("Calories", "Calories: "+ formatOutput(calorieValue) );

                    food.put("Fat", "Fat: " + formatOutput(fatValue) + "g");

                    food.put("Carbs", "Carb: " + formatOutput(carbValue)+ "g");

                    foodItemList.add(food);

                } catch (JSONException e) {
                    // Oops
                }
        }catch (Exception e)
        {
            Log.i("Exception", e.getMessage());
        }
        return "done";
    }


    @Override
    public void onProgressUpdate(Integer ... args){
        progress.setVisibility(View.VISIBLE);
        progress.setProgress(args[0]);
    }

    @Override
    public void onPostExecute(String s){
        super.onPostExecute(s);
        if (jArray == null || jArray.isNull(0)) {
            Toast toast = Toast.makeText(mainPage, R.string.Error, Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
            View view = toast.getView();
            view.setBackgroundColor(Color.RED);
            toast.show();
        }else{


            adapter.notifyDataSetChanged();

        }
        progress.setVisibility(View.INVISIBLE);
    }



    private String formatOutput(String ss){
        Double dec = Double.parseDouble(ss);

        DecimalFormat df = new DecimalFormat("##.00");
        return ""+df.format(dec);
    }

}

