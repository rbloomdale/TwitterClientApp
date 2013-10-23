package com.codepath.twitterclient;

import java.util.List;

import android.content.Context;
import java.text.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.twitterclient.models.Tweet;
import com.nostra13.universalimageloader.core.ImageLoader;

public class TweetAdapter extends ArrayAdapter<Tweet> {
	public TweetAdapter(Context context, List<Tweet> list){
		super(context,0,0,list);
	}

	public View getView(int position, View convertView, ViewGroup parent){
		Tweet tweet = getItem(position);
		View newView;
		if(convertView == null){
			LayoutInflater inflater = LayoutInflater.from(getContext());
			newView = inflater.inflate(R.layout.tweet, parent, false);
		}
		else{
			newView = convertView;
		}
		
		TextView tvBody = (TextView) newView.findViewById(R.id.tvBody);
		TextView tvName = (TextView) newView.findViewById(R.id.tvName);
		TextView tvDate = (TextView) newView.findViewById(R.id.tvDate);
		ImageView ivProfile = (ImageView) newView.findViewById(R.id.ivProfile);
		
		DateFormat dateFormat = DateFormat.getDateTimeInstance();
		
		tvBody.setText(tweet.text);
		tvName.setText(tweet.user.screen_name);
		tvDate.setText(dateFormat.format(tweet.created_date));
		ImageLoader.getInstance().displayImage(tweet.user.profile_image_url,ivProfile);
		
		return newView;
	}
}
