package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.RetweetActivity;
import com.codepath.apps.restclienttemplate.models.Tweet;

import org.parceler.Parcels;

import java.util.List;

public class TweetAdapter extends RecyclerView.Adapter<TweetAdapter.ViewHolder> {

    private List<Tweet> mTweets;
    TwitterClient client;
    Context context;
    private TweetAdapterListener mListener;

    // define an interface required by the ViewHolder
    public interface TweetAdapterListener {
        public void onItemSelected(View view, int position);
    }

    // pass in the Tweets array in the constructor
    public TweetAdapter(List<Tweet> tweets) {
        mTweets = tweets;
    }

    public TweetAdapter(List<Tweet> tweets, TweetAdapterListener listener) {
        mTweets = tweets;
        mListener = listener;
    }

    // for each row, inflate the layout and cache references into ViewHolder

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View tweetView = inflater.inflate(R.layout.item_tweet, parent, false);
        ViewHolder viewHolder = new ViewHolder(tweetView);
        return viewHolder;
    }

    // bind the values based on the position of the element

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        // get the data according to position
        final Tweet tweet = mTweets.get(position);

        // populate the views according to this data
        holder.tvName.setText(tweet.user.name);
        //holder.tvScreenName.setText("@" + tweet.user.screenName);
        holder.tvBody.setText(tweet.body);
        holder.tvCreatedAt.setText(tweet.createdAt);
        holder.tvRtCount.setText(Integer.toString(tweet.rtCount));
        holder.tvFavCount.setText(Integer.toString(tweet.favCount));
        Glide.with(context)
                .load(tweet.user.profileImageUrl)
                .into(holder.ivProfileImage);

        if (tweet.hasMedia) {
            Glide.with(context)
                    .load(tweet.mediaUrl)
                    .into(holder.ivMedia);
        }
        else {
            holder.ivMedia.setVisibility(View.GONE);
        }

        holder.ivReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ReplyActivity.class);
                intent.putExtra("screenName", tweet.screenName);
                context.startActivity(intent);
            }
        });

        holder.tvBody.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, TweetDetailActivity.class);
                intent.putExtra("tweet", Parcels.wrap(tweet));
                context.startActivity(intent);
            }
        });


        holder.ivRetweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                Intent i = new Intent(context, RetweetActivity.class);
                i.putExtra("tweet", tweet.uid);
                i.putExtra("body", tweet.body);
                context.startActivity(i);
            }
        });
    }


    @Override
    public int getItemCount() {
        return mTweets.size();
    }

    // create ViewHolder class

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView ivProfileImage;
        public TextView tvName;
        public TextView tvBody;
        public TextView tvScreenName;
        public TextView tvCreatedAt;
        public ImageView ivReply;
        public RelativeLayout itemTweet;
        public ImageView ivMedia;
        public TextView tvRtCount;
        public TextView tvFavCount;
        public ImageView ivRetweet;
        public ImageView ivFavorite;

        public ViewHolder (View itemView) {
            super(itemView);

            // perform findViewById lookups

            ivProfileImage = (ImageView) itemView.findViewById(R.id.ivProfileImage);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            tvBody = (TextView) itemView.findViewById(R.id.tvBody);
            //tvScreenName = (TextView) itemView.findViewById(R.id.tvScreenName);
            ivReply = (ImageView) itemView.findViewById(R.id.ivReply);
            itemTweet = (RelativeLayout) itemView.findViewById(R.id.itemTweet);
            ivMedia = (ImageView) itemView.findViewById(R.id.ivMedia);
            tvRtCount = (TextView) itemView.findViewById(R.id.tvRtCount);
            tvFavCount = (TextView) itemView.findViewById(R.id.tvFavCount);
            ivRetweet = (ImageView) itemView.findViewById(R.id.ivRetweet);
            ivFavorite = (ImageView) itemView.findViewById(R.id.ivFavorite);
            tvCreatedAt = (TextView) itemView.findViewById(R.id.tvCreatedAt);

            // handle row click event
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mListener != null) {
                        // get the position of row element
                        int position = getAdapterPosition();
                        // fire the listener callback
                        mListener.onItemSelected(view, position);
                    }
                }
            });

            ivRetweet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick (View v) {
                    ivRetweet.setImageResource(R.drawable.ic_vector_retweet);
                    Intent i = new Intent(context, RetweetActivity.class);
                }
            });

        }
    }


    // Clean all elements of the recycler
    public void clear() {
        mTweets.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Tweet> list) {
        mTweets.addAll(list);
        notifyDataSetChanged();
    }
}
