package com.codepath.apps.restclienttemplate.models;

import android.text.format.DateUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

@Parcel
public class Tweet {

    // list out the attributes
    public String body;
    public long uid; // database ID for the tweet
    public User user;
    public String createdAt;
    public String screenName;
    public int rtCount;
    public int favCount;
    public boolean favorited;
    public boolean retweeted;
    public boolean hasEntities;
    public boolean hasMedia;
    public String mediaUrl;

    // empty constructor needed by the Parceler library
    public Tweet() {}

    // deserialize the JSON
    public static Tweet fromJSON(JSONObject jsonObject) throws JSONException {
        Tweet tweet = new Tweet();

        // extract the values from JSON
        tweet.body = jsonObject.getString("full_text");
        tweet.uid = jsonObject.getLong("id");
        tweet.createdAt = getRelativeTimeAgo(jsonObject.getString("created_at"));
        tweet.user = User.fromJSON(jsonObject.getJSONObject("user"));
        tweet.screenName = "@" + tweet.user.screenName;
        tweet.rtCount = jsonObject.getInt("retweet_count");
        tweet.favCount = jsonObject.getInt("favorite_count");
        tweet.favorited = false;
        tweet.retweeted = false;
        tweet.hasEntities = jsonObject.has("entities");
        tweet.hasMedia = jsonObject.getJSONObject("entities").has("media");
        if (tweet.hasEntities && tweet.hasMedia) {
            String url = jsonObject.getJSONObject("entities").getJSONArray("media").getJSONObject(0).getString("media_url");
            tweet.mediaUrl = url + ":large";
        }
        else tweet.mediaUrl = null;

        return tweet;
    }

    // getRelativeTimeAgo("Mon Apr 01 21:16:23 +0000 2014");
    public static String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return relativeDate;
    }
}
