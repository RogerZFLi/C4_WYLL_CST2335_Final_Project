package com.example.amusu.teamname_cst2335_final_project;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.amusu.teamname_cst2335_final_project.FoodNutrition.FoodMainActivity;
import com.example.amusu.teamname_cst2335_final_project.movie.MovieInformationActivity;
import com.example.amusu.teamname_cst2335_final_project.rogerli.octranspo.OCTranspoBusRouteActivity;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringBufferInputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class CBCMainActivity extends AppCompatActivity {
    private List<News> newsList = new ArrayList<News>();
    private List<String> newsTile = new ArrayList<String>();
    private ListView listView;
    private Toolbar toolbar;
    LinearLayout proBar;
    ArrayAdapter<String> adapter;
    String url = "https://www.cbc.ca/cmlink/rss-world";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cbc_main);
        listView = (ListView) findViewById(R.id.listView);
        proBar = (LinearLayout) findViewById(R.id.proBar);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initData();

        /**
         *click to jump over to another page
         */
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(CBCMainActivity.this, CbcDetailActivity.class);
                intent.putExtra("data", newsList.get(i));
                startActivity(intent);
            }
        });

    }


    /**
     * Initialization data
     */
    private void initData() {
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, newsTile);
        listView.setAdapter(adapter);


        new AsyncTask<Void, Integer, String>() {
            @Override
            protected void onPreExecute() {
                proBar.setVisibility(View.VISIBLE);
            }

            @Override
            protected String doInBackground(Void... params) {
                String result = CBCMainActivity.get(url);
                return result;
            }


            @Override
            protected void onPostExecute(String i) {
                proBar.setVisibility(View.GONE);
                try {
                    if (i != null) {
                        getNewsList(i);
                    } else {
                        Toast.makeText(CBCMainActivity.this, "load error", Toast.LENGTH_LONG).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }.execute();
    }


    /**
     * getNewsList
     *
     * @param s
     * @return
     * @throws Exception
     */
    public void getNewsList(String s) throws Exception {
        InputStream is = new StringBufferInputStream(s);
        newsTile.clear();
        newsList.clear();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        //retrieve Document object
        Document document = builder.parse(is);
        //get the new List
        NodeList newList = document.getElementsByTagName("item");
        //Traversing the news tag
        for (int i = 0; i < newList.getLength(); i++) {
            //Get the news tag
            Node node_news = newList.item(i);
            //create a child node
            NodeList childNodes = node_news.getChildNodes();

            //initialize news property
            String title = "";
            String link = "";
            String guid = "";
            String pubDate = "";
            String author = "";
            String category = "";
            String description = "";

            for (int j = 0; j < childNodes.getLength(); j++) {
                //get name and nickName item
                Node childNode = childNodes.item(j);
                //decide name or nickName
                if ("title".equals(childNode.getNodeName())) {
                    title = childNode.getTextContent().trim();
                } else if ("link".equals(childNode.getNodeName())) {
                    link = childNode.getTextContent().trim();
                } else if ("guid".equals(childNode.getNodeName())) {
                    guid = childNode.getTextContent().trim();
                } else if ("pubDate".equals(childNode.getNodeName())) {
                    pubDate = childNode.getTextContent().trim();
                } else if ("author".equals(childNode.getNodeName())) {
                    author = childNode.getTextContent().trim();
                } else if ("category".equals(childNode.getNodeName())) {
                    category = childNode.getTextContent().trim();
                } else if ("description".equals(childNode.getNodeName())) {
                    description = childNode.getTextContent().trim();
                }
            }
            if (!title.equals("")) {
                News news = new News(title, link, guid, pubDate, author, category, description);
                System.out.println(news.toString());
                newsList.add(news);
                newsTile.add(title);
            }
        }
        adapter.notifyDataSetChanged();
    }


    public static String get(String urlPath) {
        HttpURLConnection connection = null;
        InputStream is = null;
        String ex = "";
        try {
            URL url = new URL(urlPath);
            //retrieve URL object
            connection = (HttpURLConnection) url.openConnection();
            //retrieve HttpURLConnection object
            connection.setRequestMethod("GET");
            // make GET as default
            connection.setUseCaches(false);
            //disable cache
            connection.setConnectTimeout(10000);
            //enable timeout
            connection.setReadTimeout(10000);
            //enable read timeout
            connection.setDoInput(true);
            //set whether to read from httpUrlConnection
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                is = connection.getInputStream();
                //retrieve input stream
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                //create a new string builder object
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                return response.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
            ex = e.toString();
        } finally {
            if (connection != null) {
                connection.disconnect();
                connection = null;
            }
            if (is != null) {
                try {
                    is.close();
                    is = null;
                } catch (IOException e) {
                    e.printStackTrace();
                    ex = e.toString();
                }
            }
        }
        return ex;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }







    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int featureId = item.getItemId();
        if (featureId == R.id.bus) {
            startActivity(new Intent(CBCMainActivity.this,OCTranspoBusRouteActivity.class));
        }
        if (featureId == R.id.food) {
            startActivity(new Intent(CBCMainActivity.this,FoodMainActivity.class));
        }
        if (featureId == R.id.movie) {
            startActivity(new Intent(CBCMainActivity.this,MovieInformationActivity.class));
        }
        if (featureId == R.id.about) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle("about");
            dialog.setMessage("Author:Jiahao Yin\n" +
                    "Date:2018-11-29");
            dialog.setNegativeButton("ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            }).create().show();
        }
        if (featureId == R.id.exit) {
            CBCMainActivity.this.finish();
        }
        return super.onMenuItemSelected(featureId, item);
    }


}


