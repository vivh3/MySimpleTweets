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

import butterknife.BindView;
import butterknife.ButterKnife;


public class TweetDetailActivity extends AppCompatActivity {

    @BindView(R.id.tvBodyDetails) TextView tvBodyDetails;
    @BindView(R.id.tvNameDetails) TextView tvNameDetails;
    @BindView(R.id.tvScreenNameDetails) TextView tvScreenNameDetails;
    @BindView(R.id.tvCreatedAtDetails) TextView tvCreatedAtDetails;
    @BindView(R.id.ivProfileImageDetails) ImageView ivProfileImageDetails;
    @BindView(R.id.ivFirstImage) ImageView ivFirstImage;
    @BindView(R.id.ivReplyDetails) ImageView ivReplyDetails;
    @BindView(R.id.ivFavoriteDetails) ImageView ivFavoriteDetails;
    @BindView(R.id.ivRetweetDetails) ImageView ivRetweetDetails;
    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet_detail);

        ButterKnife.bind(this);

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

