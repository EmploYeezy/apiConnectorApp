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
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity implements XkcdRetriever.ApiResponseHandler{

    EditText searchTV;
    ImageView imageViewer;
    Button searchButton;
    Button prevButton;
    Button nextButton;
    TextView currentNumber;
    private String altText;
    int foo;
    int bar1;
    int bar2;
    int bar3;
    private String number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

            prevButton = (Button) findViewById(R.id.prevButton);
            prevButton.setVisibility(View.INVISIBLE);
            nextButton = (Button) findViewById(R.id.nextButton);
            nextButton.setVisibility(View.INVISIBLE);
            currentNumber = (TextView) findViewById(R.id.currentNumber);


        searchButton = (Button) findViewById(R.id.searchbutton);
            searchButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    searchTV = (EditText) findViewById(R.id.searchTV);
                    String query = searchTV.getText().toString(); //this line and the next line is a null pointer fixer
                    if (query.isEmpty()) return; //null pointer fixer
                    foo = Integer.parseInt(query);
                    if (foo <= 0 || foo > 1684) {
                        Toast.makeText(MainActivity.this, R.string.mockingToast, Toast.LENGTH_SHORT).show();
                    } else {
                        XkcdRetriever.getInstance(MainActivity.this).doRequest(foo);
                        searchTV.setText("");
                        searchTV.setHint(R.string.secondaryHint);
                    }
                }
            });
        }

    /////--------: These two things get bits of info from XkcdRetriever.java JSON and bring it to this activity :--------\\\\\
    public void handleResponseNum(String responseNum) {
        number = responseNum;
        currentNumber.setText(number);
        bar3 = Integer.parseInt(number);
    }

    @Override
    public void handleResponseImg(String response) {
        imageViewer = (ImageView) findViewById(R.id.imageViewer);
        imageViewer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, altText, Toast.LENGTH_LONG).show();
            }
        });
        prevButton.setVisibility(View.VISIBLE);
        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bar1 = bar3 -1;
                if (bar1 <= 0) {
                    Toast.makeText(MainActivity.this, R.string.prevButtonToast, Toast.LENGTH_SHORT).show();
                } else {
                    XkcdRetriever.getInstance(MainActivity.this).doRequest(bar1);
                }
            }
        });
        nextButton.setVisibility(View.VISIBLE);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bar2 = bar3 +1;
                if (bar2 > 1684){
                    Toast.makeText(MainActivity.this, R.string.nextButtonToast, Toast.LENGTH_SHORT).show();
                } else {
                    XkcdRetriever.getInstance(MainActivity.this).doRequest(bar2);
                }
            }
        });
        Picasso.with(MainActivity.this).load(response).into(imageViewer);
    }
    @Override
    public void handleResponseAlt(String responseAlt) {
        altText = responseAlt;
    }

    /////--------: This whole block of code just makes sure that the comic is displayed well :--------\\\\\
    @Override
    public void onConfigurationChanged (Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Log.e("On Config Change", "LANDSCAPE");
            imageViewer.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
            imageViewer.setAdjustViewBounds(true);
            searchTV.setVisibility(View.INVISIBLE);
            searchButton.setVisibility(View.INVISIBLE);
            prevButton.setVisibility(View.INVISIBLE);
            nextButton.setVisibility(View.INVISIBLE);
            currentNumber.setVisibility(View.INVISIBLE);
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            Log.e("On Config change", "PORTRAIT");
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) imageViewer.getLayoutParams();
            params.addRule(RelativeLayout.BELOW, R.id.searchbutton);
            params.setMargins(45,45,45,45); // padding for left, top, right, bottom
            searchTV.setVisibility(View.VISIBLE);
            searchButton.setVisibility(View.VISIBLE);
            prevButton.setVisibility(View.VISIBLE);
            nextButton.setVisibility(View.VISIBLE);
            currentNumber.setVisibility(View.VISIBLE);
            searchTV.setText("");
            searchTV.setHint(R.string.secondaryHint);
            imageViewer.setAdjustViewBounds(true);
        }
    }
}