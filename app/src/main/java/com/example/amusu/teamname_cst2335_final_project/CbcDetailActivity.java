package com.example.amusu.teamname_cst2335_final_project;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class CbcDetailActivity extends AppCompatActivity {
    private TextView title;
    private TextView guid;
    private TextView pubDate;
    private TextView author;
    private TextView category;
    private Button link;
    private Button goDescription;
    private Button add;
    private Button statistical;


    private News news;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cbc_detail);
        dbHelper = new DBHelper(this);
        title = (TextView) findViewById(R.id.title);
        guid = (TextView) findViewById(R.id.guid);
        pubDate = (TextView) findViewById(R.id.pubDate);
        author = (TextView) findViewById(R.id.author);
        category = (TextView) findViewById(R.id.category);
        link = (Button) findViewById(R.id.link);
        goDescription = (Button) findViewById(R.id.goDescription);
        add = (Button) findViewById(R.id.add);
        statistical = (Button) findViewById(R.id.statistical);
        initData();
        initListener();
    }

    private void initData() {
        news = (News) getIntent().getSerializableExtra("data");
        title.setText(news.getTitle());
        pubDate.setText(news.getPubDate());
        author.setText(news.getAuthor());
        title.setText(news.getTitle());
        guid.setText(news.getGuid());
        category.setText(news.getCategory());
    }

    private void initListener() {
        link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CbcDetailActivity.this, CbcDetailWeb.class);
                intent.putExtra("type", "1");
                intent.putExtra("url", news.getLink());
                startActivity(intent);
            }
        });

        goDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CbcDetailActivity.this, CbcDetailWeb.class);
                intent.putExtra("type", "2");
                intent.putExtra("url", news.getDescription());
                startActivity(intent);
            }
        });

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

        statistical.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CbcDetailActivity.this, CBClocalTotalActivity.class);
                startActivity(intent);
            }
        });
    }
}
