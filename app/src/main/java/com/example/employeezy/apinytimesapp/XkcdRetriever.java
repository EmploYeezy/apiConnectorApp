package com.example.employeezy.apinytimesapp;

import android.widget.EditText;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by EmployYeezy on 5/20/16.
 */

public class XkcdRetriever {

   EditText searchValue;
    String idNumber;

    private static XkcdRetriever intance;
    private static ApiResponseHandler responseHandler;

    private XkcdRetriever() {
    }

    public static XkcdRetriever getIntance(ApiResponseHandler handler) {
        responseHandler = handler;
        if (intance == null) {
            intance = new XkcdRetriever();
        }
        return intance;
    }

    public void doRequest(int urlNumber) {

        AsyncHttpClient client = new AsyncHttpClient();

        client.get(
                "http://xkcd.com/" + urlNumber + "/info.0.json",
                null,
                new JsonHttpResponseHandler() {
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                        try {
                            responseHandler.handleResponse(response.getString("month"));
                            responseHandler.handleResponse(response.getString("img"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
    }

    public interface ApiResponseHandler {
        void handleResponse(String response);
    }
}