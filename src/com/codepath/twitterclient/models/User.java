package com.codepath.twitterclient.models;

import org.json.JSONObject;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name="Users")
public class User extends Model {
	@Column(name="Name")
	public String name;
	
	@Column(name="UserId")
	public long id;
	
	@Column(name="ScreenName")
	public String screen_name;
	
	@Column(name="ProfileImageUrl")
	public String profile_image_url;
	
	@Column(name="ProfileBackgroundImageUrl")
	public String profile_background_image_url;
	
	@Column(name="NumTweets")
	public int num_tweets;
	
	@Column(name="FollowersCount")
	public int followers_count;
	
	@Column(name="FriendsCount")
	public int friends_count;
	
	public static User fromJson(JSONObject json){
		User newUser = new User();
		
		try{
			newUser.name = json.getString("name");
			newUser.id = json.getLong("id");
			newUser.followers_count = json.getInt("followers_count");
			newUser.friends_count = json.getInt("friends_count");
			newUser.num_tweets = json.getInt("statuses_count");
			newUser.profile_background_image_url = json.getString("profile_background_image_url");
			newUser.profile_image_url = json.getString("profile_image_url");
			newUser.screen_name = json.getString("screen_name");
		} catch (Exception e){
			e.printStackTrace();
		}
		
		return newUser;
	}
	
	public User(){
		super();
		this.name = "Not Loaded";
		this.id = -1;
		this.followers_count = 0;
		this.friends_count = 0;
		this.num_tweets = 0;
		this.profile_background_image_url = "";
		this.profile_image_url = "";
		this.screen_name = "Not Loaded";
	}
	
	public User(String name, long id, String screen_name, String profile_image_url, String profile_background_image_url, int num_tweets, int followers_count, int friends_count){
		super();
		this.name = name;
		this.id = id;
		this.followers_count = followers_count;
		this.friends_count = friends_count;
		this.num_tweets = num_tweets;
		this.profile_background_image_url = profile_background_image_url;
		this.profile_image_url = profile_image_url;
		this.screen_name = screen_name;
	}
}
