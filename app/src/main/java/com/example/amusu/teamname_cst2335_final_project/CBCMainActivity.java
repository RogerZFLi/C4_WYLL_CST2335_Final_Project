package com.example.amusu.teamname_cst2335_final_project;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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

/**
 * this class is news display main interface
 */

public class CBCMainActivity extends AppCompatActivity {
    /**
     * Define a news list
     */

    private List<News> newsList = new ArrayList<News>();
    /**
     * Define a list of news headlines
     */
    private List<String> newsTile = new ArrayList<String>();
    /**
     * Define a list display control
     */
    private ListView listView;
    /**
     * Define a toolbar
     */

    private Toolbar toolbar;
    /**
     * Define progress display controls
     */
    LinearLayout proBar;
    /**
     * Define an adapter
     */
    ArrayAdapter<String> adapter;
    /**
     * Set the web page request address
     */
    String url = "https://www.cbc.ca/cmlink/rss-world";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cbc_main);
        listView = (ListView) findViewById(R.id.listView);
        proBar = (LinearLayout) findViewById(R.id.proBar);
        toolbar = findViewById(R.id.toolbar);
        //set tool bar
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
     * Initializ data
     */
    private void initData() {
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, newsTile);
        listView.setAdapter(adapter);


        /**
         * Load the news list in the website through an asyn task
         */


        new AsyncTask<Void, Integer, String>() {
            @Override
            protected void onPreExecute() {
                //Show loading progress
                proBar.setVisibility(View.VISIBLE);
            }

            @Override
            protected String doInBackground(Void... params) {
                //Get the news data in the web page and return a String
                String result = CBCMainActivity.get(url);
                return result;
            }


            @Override
            protected void onPostExecute(String i) {
                //Turn off progress display controls
                proBar.setVisibility(View.GONE);
                try {
                    if (i != null) {
                        //Parse the retrieved news data when the returned data is not empty
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
     * Parsing news data
     * @param s
     * @return
     * @throws Exception
     */
    public void getNewsList(String s) throws Exception {
        //Convert the String you get to InputStream
        InputStream is = new StringBufferInputStream(s);
        //Clear the initialized data
        newsTile.clear();
        newsList.clear();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        //load Dom files
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

            //Parse the data of each child element through a for loop
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
            //determine if the parsed title is not empty

            if (!title.equals("")) {
                //Get every parsed news
                News news = new News(title, link, guid, pubDate, author, category, description);
                System.out.println(news.toString());
                newsList.add(news);//List of news stores
                newsTile.add(title);//Save news headlines to the list
            }
        }
        //update list display control
        adapter.notifyDataSetChanged();

    }
    /**
     * get request to load address
     * @param urlPath
     * @return
     */

    public static String get(String urlPath) {
        HttpURLConnection connection = null;
        InputStream is = null;
        String ex = "";
        try {
            URL url = new URL(urlPath);
            //retrieve the URL object
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
    /**
     * create for tool bar
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    /**
     * every click event of the tool bar
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch(id) {
            case R.id.bus:
                Log.d("Tool bar", "Option 1 select");
                startActivity(new Intent(CBCMainActivity.this,OCTranspoBusRouteActivity.class));
                break;
            case R.id.food:
                Log.d("Tool bar", "Option 2 select");
                startActivity(new Intent(CBCMainActivity.this,FoodMainActivity.class));

                break;
            case R.id.movie:
                Log.d("Tool bar", "Option 3 select");
                startActivity(new Intent(CBCMainActivity.this,MovieInformationActivity.class));
                break;
            case R.id.about:
                Log.d("Tool bar", "Option 4 select");
                AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                dialog.setTitle("about");
                dialog.setMessage("Author:Jiahao Yin\n" +
                        "Date:2018-11-29");
                dialog.setNegativeButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).create().show();
                break;
            case R.id.exit:
                Log.d("Tool bar", "Option 5 select");
                CBCMainActivity.this.finish();
                break;
        }
        return super.onMenuItemSelected(id, item);
    }
}


