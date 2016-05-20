package com.example.employeezy.apinytimesapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements XkcdRetriever.ApiResponseHandler{

    TextView responseView;
    EditText searchTV;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button searchButton = (Button) findViewById(R.id.searchbutton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                XkcdRetriever.getIntance(MainActivity.this).doRequest();
            }
        });
    }

    @Override
    public void handleResponse(String response) {

        Toast.makeText(MainActivity.this, response, Toast.LENGTH_SHORT).show();
    }
}