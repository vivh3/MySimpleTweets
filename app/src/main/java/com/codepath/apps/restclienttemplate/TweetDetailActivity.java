package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Tweet;

import org.parceler.Parcels;


public class TweetDetailActivity extends AppCompatActivity {

    TextView tvBodyDetails;
    TextView tvNameDetails;
    TextView tvScreenNameDetails;
    TextView tvCreatedAtDetails;
    ImageView ivProfileImageDetails;
    ImageView ivFirstImage;
    ImageView ivReplyDetails;
    ImageView ivFavoriteDetails;
    ImageView ivRetweetDetails;
    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet_detail);


        tvBodyDetails = (TextView) findViewById(R.id.tvBodyDetails);
        tvNameDetails = (TextView) findViewById(R.id.tvNameDetails);
        tvScreenNameDetails = (TextView) findViewById(R.id.tvScreenNameDetails);
        ivProfileImageDetails = (ImageView) findViewById(R.id.ivProfileImageDetails);
        ivReplyDetails = (ImageView) findViewById(R.id.ivReplyDetails);
        ivFavoriteDetails = (ImageView) findViewById(R.id.ivFavoriteDetails);
        ivRetweetDetails = (ImageView) findViewById(R.id.ivRetweetDetails);
        ivFirstImage = (ImageView) findViewById(R.id.ivFirstImage);
        tvCreatedAtDetails = (TextView) findViewById(R.id.tvCreatedAtDetails);

        Intent i = getIntent();
        final Tweet tweet = (Tweet) Parcels.unwrap(i.getParcelableExtra("tweet"));

        tvBodyDetails.setText(tweet.body);
        tvNameDetails.setText(tweet.user.name);
        tvScreenNameDetails.setText(tweet.screenName);
        tvCreatedAtDetails.setText(tweet.createdAt);


        Glide.with(context).load(tweet.user.profileImageUrl).into(ivProfileImageDetails);

        ivReplyDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ReplyActivity.class);
                intent.putExtra("screenName", tweet.user.screenName);
                context.startActivity(intent);
            }
        });

        Glide.with(context)
                .load(tweet.user.profileImageUrl)
                .into(ivProfileImageDetails);

        if (tweet.hasMedia) {
            Glide.with(context)
                    .load(tweet.mediaUrl)
                    .into(ivFirstImage);
        }
        else {
            ivFirstImage.setVisibility(View.GONE);
        }
    }
}


/*package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;

public class TweetDetailActivity extends AppCompatActivity implements View.OnClickListener {
    Context context = this;
    private TwitterClient client;
    Tweet tweet;
    TextView tvBodyDetails;
    TextView tvNameDetails;
    TextView tvScreenNameDetails;
    TextView tvCreatedAtDetails;
    ImageView ivProfileImageDetails;
    ImageView ivFirstImage;
    ImageView ivReplyDetails;
    ImageView ivFavoriteDetails;
    ImageView ivRetweetDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet_detail);
        client = TwitterApp.getRestClient(this);


        // Find the toolbar view inside the activity layout
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        // Sets the Toolbar to act as the ActionBar for this Activity window.
        // Make sure the toolbar exists in the activity and is not null
        setSupportActionBar(toolbar);
        getSupportActionBar().setIcon(R.drawable.ic_vector_twitter_bird);
        getSupportActionBar().setTitle("");

        tvBodyDetails = (TextView) findViewById(R.id.tvBodyDetails);
        tvNameDetails = (TextView) findViewById(R.id.tvNameDetails);
        tvScreenNameDetails = (TextView) findViewById(R.id.tvScreenNameDetails);
        ivProfileImageDetails = (ImageView) findViewById(R.id.ivProfileImageDetails);
        ivReplyDetails = (ImageView) findViewById(R.id.ivReplyDetails);
        ivFavoriteDetails = (ImageView) findViewById(R.id.ivFavoriteDetails);
        ivRetweetDetails = (ImageView) findViewById(R.id.ivRetweetDetails);
        ivFirstImage = (ImageView) findViewById(R.id.ivFirstImage);

        // Set on click listeners
        ivReplyDetails.setOnClickListener(this);
        ivFavoriteDetails.setOnClickListener(this);
        ivRetweetDetails.setOnClickListener(this);

        Long tweetId = getIntent().getLongExtra("tweet_id", 0);

        // GET UPDATED TWEET INFO
        if (tweetId != 0) {
            client.getTweetInfo(tweetId, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    try {
                        tweet = Tweet.fromJSON(response);
                        onTweetLoad();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    Log.d("TwitterClient", response.toString());
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
                    Log.d("TwitterClient", errorResponse.toString());
                    throwable.printStackTrace();
                }
            });
        } else {
            Log.e("TweetDetailActivity", "Unable to obtain tweet ID");

        }
    }


    private void onTweetLoad() {
        // UPDATE PAGE VIEWS
        if (tweet != null) {
            tvBodyDetails.setText(tweet.body);
            tvNameDetails.setText(tweet.user.name);
            tvScreenNameDetails.setText(tweet.user.screenName);
            tvCreatedAtDetails.setText(formatTime(tweet.createdAt));

            Glide.with(context)
                    .load(tweet.user.profileImageUrl)
                    .into(ivProfileImageDetails);
        } else {
            Log.e("TweetDetailActivity", "Unable to load tweet");
        }


        // UPDATE ICON COLORS
        if (tweet.favorited) {
            ivFavoriteDetails.setColorFilter(ContextCompat.getColor(context, R.color.icon_pink), PorterDuff.Mode.SRC_IN);
        } else {
            ivFavoriteDetails.setColorFilter(ContextCompat.getColor(context, R.color.icon_gray), PorterDuff.Mode.SRC_IN);

        }

        if (tweet.retweeted) {
            ivRetweetDetails.setColorFilter(ContextCompat.getColor(context, R.color.icon_green), PorterDuff.Mode.SRC_IN);
        } else {
            ivRetweetDetails.setColorFilter(ContextCompat.getColor(context, R.color.icon_gray), PorterDuff.Mode.SRC_IN);

        }
    }

    public String formatTime(String twitterTime) {
        DateFormat originalFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss ZZZZZ yyyy");
        DateFormat targetFormat = new SimpleDateFormat("h:mm a \u2022 dd MMM yy", Locale.ENGLISH);
        Date date = null;
        try {
            date = originalFormat.parse(twitterTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String formattedDate = targetFormat.format(date);  // 20120821
        return formattedDate;
    }

    // listeners for clicking on the icons
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
*//*            case R.id.ivReplyDetails:
                ModalFragment modalFragment = ModalFragment.newInstance(tweet.uid, "@" + tweet.user.screenName + " ");
//                modalFragment.onAttach(context);
                modalFragment.openComposeModal(context);
                break;*//*

            case R.id.ivRetweetDetails:
                if (!tweet.retweeted) {
                    client.retweet(tweet.uid, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            Toast.makeText(context, "retweeted", Toast.LENGTH_LONG).show();
                            ivRetweetDetails.setColorFilter(ContextCompat.getColor(context, R.color.icon_green), PorterDuff.Mode.SRC_IN);
                            tweet.retweeted = true;

                        }

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                            Log.d("TwitterClient", response.toString());
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
                            Log.d("TwitterClient", errorResponse.toString());
                            throwable.printStackTrace();
                        }
                    });
                } else {
                    client.unretweet(tweet.uid, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            Toast.makeText(context, "unretweeted", Toast.LENGTH_LONG).show();
                            ivRetweetDetails.setColorFilter(ContextCompat.getColor(context, R.color.icon_gray), PorterDuff.Mode.SRC_IN);
                            tweet.retweeted = false;

                        }

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                            Log.d("TwitterClient", response.toString());
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
                            Log.d("TwitterClient", errorResponse.toString());
                            throwable.printStackTrace();
                        }
                    });
                }
                break;

            case R.id.ivFavoriteDetails:
                if (!tweet.favorited) {
                    client.favorite(tweet.uid, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            Toast.makeText(context, "favorited", Toast.LENGTH_LONG).show();
                            ivFavoriteDetails.setColorFilter(ContextCompat.getColor(context, R.color.icon_pink), PorterDuff.Mode.SRC_IN);
                            tweet.favorited = true;

                        }

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                            Log.d("TwitterClient", response.toString());
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
                            Log.d("TwitterClient", errorResponse.toString());
                            throwable.printStackTrace();
                        }
                    });
                } else {
                    client.unfavorite(tweet.uid, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            Toast.makeText(context, "unfavorited", Toast.LENGTH_LONG).show();
                            ivFavoriteDetails.setColorFilter(ContextCompat.getColor(context, R.color.icon_gray), PorterDuff.Mode.SRC_IN);
                            tweet.favorited = false;

                        }

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                            Log.d("TwitterClient", response.toString());
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
                            Log.d("TwitterClient", errorResponse.toString());
                            throwable.printStackTrace();
                        }

                    });
                    break;


                }
        }

    }
}*/
