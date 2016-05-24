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
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity implements XkcdRetriever.ApiResponseHandler{

    EditText searchTV;
    ImageView imageViewer;
    Button searchButton;
    private String altText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

            searchButton = (Button) findViewById(R.id.searchbutton);
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
                        searchTV.setText("");
                        searchTV.setHint("Would you like another? Enter 1 - 1684");
                    }
                }
            });
        }

    @Override
    public void handleResponse(String response) {
        imageViewer = (ImageView) findViewById(R.id.imageViewer);
        Picasso.with(MainActivity.this).load(response).into(imageViewer);
    }
    @Override
    public void handleResponseAlt(String responseAlt) {
        altText = responseAlt;
    }

    /////--------: This whole block of code just makes sure that the comic is displayed well :-------\\\\\
    @Override
    public void onConfigurationChanged (Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Log.e("On Config Change", "LANDSCAPE");
            imageViewer.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
            imageViewer.setAdjustViewBounds(true);
            searchTV.setVisibility(View.INVISIBLE);
            searchButton.setVisibility(View.INVISIBLE);
            imageViewer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(MainActivity.this, altText, Toast.LENGTH_LONG).show();
                }
            });
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            Log.e("On Config change", "PORTRAIT");
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) imageViewer.getLayoutParams();
            params.addRule(RelativeLayout.BELOW, R.id.searchbutton);
            params.setMargins(45,45,45,45); // padding for left, top, right, bottom
            searchTV.setVisibility(View.VISIBLE);
            searchButton.setVisibility(View.VISIBLE);
            searchTV.setText("");
            searchTV.setHint("Would you like another? Enter 1 - 1684");
            imageViewer.setAdjustViewBounds(true);
            imageViewer.setClickable(false);
        }
    }
}