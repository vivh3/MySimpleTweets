package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import cz.msebera.android.httpclient.Header;


public class ReplyActivity extends AppCompatActivity {

    EditText etTweetBody;
    Button btTweet;

    private TwitterClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);

        client = TwitterApp.getRestClient(this);

        btTweet = (Button) findViewById(R.id.btTweet);
        etTweetBody = (EditText) findViewById(R.id.etTweetBody);

        String screenName = getIntent().getStringExtra("screenName");
        etTweetBody.append("@" + screenName + " ");

        btTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                client.sendTweet(etTweetBody.getText().toString(), new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        try {
                            Tweet tweet = Tweet.fromJSON(response);
                            Intent data = new Intent();
                            data.putExtra("tweet", Parcels.wrap(tweet));
                            setResult(RESULT_OK, data);
                            finish();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        Log.d("TwitterClient", responseString);
                        throwable.printStackTrace();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        Log.d("TwitterClient", errorResponse.toString());
                        throwable.printStackTrace();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                    }
                });
            }
        });
    }
}
