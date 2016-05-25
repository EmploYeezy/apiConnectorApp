package com.example.employeezy.apinytimesapp;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by EmployYeezy on 5/20/16.
 */

public class XkcdRetriever {

    private static XkcdRetriever instance;
    private static ApiResponseHandler responseHandler;

    private XkcdRetriever() {}

    public static XkcdRetriever getInstance(ApiResponseHandler handler) {
        responseHandler = handler;
        if (instance == null) {
            instance = new XkcdRetriever();
        }
        return instance;
    }

    public void doRequest(int urlNumber) { //int urlNumber refers to int foo passed as doRequest(foo) from Main.
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(
                "http://xkcd.com/" + urlNumber + "/info.0.json",
                null,
                new JsonHttpResponseHandler() {
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        try {
                            responseHandler.handleResponseAlt(response.getString("alt"));
                            responseHandler.handleResponse(response.getString("img"));
                            responseHandler.handleResponseNum(response.getString("num"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    public interface ApiResponseHandler {
        void handleResponseAlt(String response);
        void handleResponse(String response);
        void handleResponseNum(String response);
    }
}