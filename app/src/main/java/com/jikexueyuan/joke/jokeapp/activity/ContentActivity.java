package com.jikexueyuan.joke.jokeapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import com.jikexueyuan.joke.jokeapp.R;

public class ContentActivity extends AppCompatActivity {

    private TextView tv_content_title;
    private TextView tv_content_full;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
        tv_content_title = (TextView) findViewById(R.id.tv_content_title);
        tv_content_full = (TextView) findViewById(R.id.tv_content_full);
        Intent i = getIntent();
        Bundle bundle = i.getExtras();
        tv_content_title.setText(bundle.getString("post_title"));
        tv_content_full.setText(bundle.getString("post_content"));
        findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
