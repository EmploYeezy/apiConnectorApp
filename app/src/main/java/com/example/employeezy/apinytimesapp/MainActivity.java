package com.example.employeezy.apinytimesapp;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity implements XkcdRetriever.ApiResponseHandler{

    TextView responseView;
    EditText searchTV;
    ProgressBar progressBar;
    ImageView imageViewer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

            final Button searchButton = (Button) findViewById(R.id.searchbutton);
            assert searchButton != null;
            searchButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    searchTV = (EditText) findViewById(R.id.searchTV);
                    int foo = Integer.parseInt(searchTV.getText().toString());
                    if (foo <= 0 || foo > 1684) {
                        searchButton.setClickable(false);
                        Toast.makeText(MainActivity.this, "Wrong Number Chochy", Toast.LENGTH_SHORT).show();

                    } else {
                        searchButton.setClickable(true);
                        XkcdRetriever.getInstance(MainActivity.this).doRequest(foo);
                    }
                }
            });
        }

    @Override
    public void onConfigurationChanged (Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Log.e("On Config Change", "LANDSCAPE");
        } else {
            Log.e("On Config change", "PORTRAIT");
        }

    }

    @Override
    public void handleResponse(String response) {
//        responseView = (TextView) findViewById(R.id.responseView);
//        responseView.setText(response);
        imageViewer = (ImageView) findViewById(R.id.imageViewer);
        Picasso.with(MainActivity.this).load(response).into(imageViewer);
    }
}