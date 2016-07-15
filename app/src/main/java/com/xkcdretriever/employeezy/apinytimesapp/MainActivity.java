package com.xkcdretriever.employeezy.apinytimesapp;

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
    int foo;
    int bar1;
    int bar2;
    int bar3;
    int bar4;
    int counter;
    private String altText;
//    private PointF down = new PointF();
//    private PointF up = new PointF();
//    private long downTime;
//    private long upTime;
//    public static final int SWIPE_MIN_DISTANCE = 100;
//    public static final int SWIPE_MAX_DURATION = 600;
    Button searchButton;
    Button prevButton;
    Button nextButton;
    EditText searchTV;
    static ImageView imageViewer;
    TextView currentNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        searchTV = (EditText) findViewById(R.id.searchTV);
        XkcdRetriever.getInstance(MainActivity.this).homeRequest();

            prevButton = (Button) findViewById(R.id.prevButton);
            nextButton = (Button) findViewById(R.id.nextButton);
            currentNumber = (TextView) findViewById(R.id.currentNumber);

        searchButton = (Button) findViewById(R.id.searchbutton);
            searchButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    String query = searchTV.getText().toString(); //this line and the next line is a null pointer fixer
                    if (query.isEmpty()) return; //null pointer fixer
                    foo = Integer.parseInt(query);
                    if (foo <= 0 || foo > bar4) {
                        Toast.makeText(MainActivity.this, R.string.mockingToast, Toast.LENGTH_SHORT).show();
                    } else {
                        XkcdRetriever.getInstance(MainActivity.this).doRequest(foo);
                        searchTV.setText("");
                        searchTV.setHint(getString(R.string.searchTVHing) + bar4);
                    }
                }
            });
        }

    /////--------: These Three handlers get bits of JSON from XkcdRetriever.java and bring it to this activity :--------\\\\\
    public void handleResponseNum(String responseNum) {
        currentNumber.setText(responseNum);
        currentNumber.setClickable(false);
        bar3 = Integer.parseInt(responseNum);
        counter++;
        if (counter == 1) {
            bar4 = bar3;
        }
        searchTV.setHint(getString(R.string.searchTVHing) + bar4);
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
                if (bar2 > bar4){
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

//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//
//        switch(event.getAction()){
//            case MotionEvent.ACTION_DOWN:
//                //the beginning of the touch event
//                down.set(event.getX(), event.getY());
//                downTime = new Date().getTime();
//                break;
//            case MotionEvent.ACTION_UP:
//                //the end of the touch event
//                up.set(event.getX(), event.getY());
//                upTime = new Date().getTime();
//
//                //calculate the horizontal distance of the touch
//                float distanceX = Math.abs(down.x - up.x);
//                //calculate the vertical distance of the touch
//                float distanceY = Math.abs(down.y - up.y);
//                //calculate the duration of the touch (how long it took)
//                float duration = upTime - downTime;
//
//                //break out of the switch if the distance was too short or the duration was too long (not a swipe).
//                if((distanceX < SWIPE_MIN_DISTANCE && distanceY < SWIPE_MIN_DISTANCE) || duration > SWIPE_MAX_DURATION) break;
//
//                //compare the horizontal and vertical distances to determine which takes precedence.
//                if(distanceX > distanceY) {
//                    //horizontal
//                    if (down.x > up.x) {
//                        bar1 = bar3 - 1;
//                        if (bar1 <= 0) {
//                            Toast.makeText(MainActivity.this, R.string.prevButtonToast, Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                }
//                break;
//        }
//        return true;
//    }

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
            searchTV.setHint(getString(R.string.searchTVHing) + bar4);
            imageViewer.setAdjustViewBounds(true);
        }
    }
}