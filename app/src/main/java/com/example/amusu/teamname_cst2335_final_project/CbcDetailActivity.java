package com.example.amusu.teamname_cst2335_final_project;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * This page is show tha activity after we click star cbc reader
 *For this activity has 3 button link add and statistical
 * each button make different functions have been implemented
 */
import com.example.amusu.teamname_cst2335_final_project.News;

public class CbcDetailActivity extends AppCompatActivity {
    /**
     * Define the title text view
     */
    private TextView title;
    /**
     * Define the guid text view
     */
    private TextView guid;
    /**
     * Define the pubDate text view
     */
    private TextView pubDate;
    /**
     * Define the author text view
     */
    private TextView author;
    /**
     * Define the category text view
     */
    private TextView category;
    /**
     * Define the link button
     */
    private Button link;
    /**
     * Define the add button
     */
    private Button add;
    /**
     * Define the statistical button
     */
    private Button statistical;

    /**
     * Define a news entity
     */
    private News news;
    /**
     * Define the dbHeplper entity
     */
    private DBHelper dbHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cbc_detail);
        //Initialize database tool
        dbHelper = new DBHelper(this);

        //Get the corresponding control from the layout file
        title = (TextView) findViewById(R.id.title);
        guid = (TextView) findViewById(R.id.guid);
        pubDate = (TextView) findViewById(R.id.pubDate);
        author = (TextView) findViewById(R.id.author);
        category = (TextView) findViewById(R.id.category);
        link = (Button) findViewById(R.id.link);
        add = (Button) findViewById(R.id.add);
        statistical = (Button) findViewById(R.id.statistical);
        //initialize data
        initData();
        //initialize listener
        initListener();
    }
    /**
     * initialize data
     */
    private void initData() {
        news = (News) getIntent().getSerializableExtra("data");
        title.setText(news.getTitle());
        pubDate.setText(news.getPubDate());
        author.setText(news.getAuthor());
        title.setText(news.getTitle());
        guid.setText(news.getGuid());
        category.setText(news.getCategory());
    }
    /**
     * initialize listener
     */
    private void initListener() {
        /**
         * Add a click event to the link button
         */
        link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CbcDetailActivity.this, CbcDetailWeb.class);
                intent.putExtra("url", news.getLink());
                startActivity(intent);
            }
        });


        /**
         * Add a click event to the add button
         */
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean flag = dbHelper.addNews(news);
                if (flag) {
                    Toast.makeText(CbcDetailActivity.this, "add success", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(CbcDetailActivity.this, "news existing", Toast.LENGTH_LONG).show();
                }
            }
        });
        /**
         * Add a click event to the statistical button
         */


        statistical.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CbcDetailActivity.this, CBClocalTotalActivity.class);
                startActivity(intent);
            }
        });
    }
}
