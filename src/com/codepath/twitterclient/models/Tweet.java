package com.codepath.twitterclient.models;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name="Tweets")
public class Tweet extends Model {
	
	@Column(name="RetweetCount")
	public int retweet_count;
	
	@Column(name="User")
	public User user;
	
	@Column(name="TweetId")
	public long id;
	
	@Column(name="TweetText")
	public String text;
	
	@Column(name="TweetDate")
	public Date created_date;
	

	public Tweet(){
		super();
		this.retweet_count = 0;
		this.user = new User();
		this.id = -1;
		this.text = "Default Text";
		this.created_date = new Date();
	}
	
	public static Tweet fromJSON(JSONObject json){
		Tweet newTweet = new Tweet();
		try{
			newTweet.retweet_count = json.getInt("retweet_count");
			newTweet.id = json.getLong("id");
			newTweet.user = User.fromJson(json.getJSONObject("user"));
			newTweet.text = json.getString("text");
			newTweet.created_date = getTwitterDate(json.getString("created_at"));
		} catch (Exception e){
			e.printStackTrace();
		}
		return newTweet;
	}
	
	protected static Date getTwitterDate(String date) throws ParseException {
	  final String TWITTER="EEE MMM dd HH:mm:ss ZZZZZ yyyy";
	  SimpleDateFormat sf = new SimpleDateFormat(TWITTER);
	  sf.setLenient(true);
	  return sf.parse(date);
	}
	
	public static ArrayList<Tweet> fromJSONArray(JSONArray tweets){
		ArrayList<Tweet> list = new ArrayList<Tweet>();
		try{
			for(int i=0;i<tweets.length();i++){
				list.add(fromJSON(tweets.getJSONObject(i)));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;
	}
}
