package com.codepath.twitterclient;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Select;
import com.codepath.twitterclient.models.Tweet;
import com.codepath.twitterclient.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

public class TimelineActivity extends Activity {

	final static int TWEET_SAVE_LIMIT = 25;
	
	TweetAdapter adapter;
	ListView lvTweets;
	TwitterClient client;
	Object btnCompose;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timeline);
		client = TwitterClientApp.getRestClient();
		adapter = new TweetAdapter(getBaseContext(), new ArrayList<Tweet>());
		lvTweets = (ListView) findViewById(R.id.lvTweets);
		lvTweets.setAdapter(adapter);
		lvTweets.setOnScrollListener(new EndlessScrollListener(){
			@Override
			public void onLoadMore(int page, int totalItemsCount){
				long tweetId = -1;
				if(adapter.getCount() > 0)
				{
					Tweet tweet = adapter.getItem(totalItemsCount-1);
					tweetId = tweet.id;
				}
				getMoreTweets(tweetId);
			}
		});
		getMoreTweets(0);
	}
	
	protected void getMoreTweets(long oldestId){
		ConnectivityManager cm = (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);		 
		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		if(activeNetwork != null && activeNetwork.isConnectedOrConnecting()){
			client.getHomeTimeline(new JsonHttpResponseHandler() {
				@Override
				public void onSuccess(JSONArray jsonTweets){
					TimelineActivity timelineActivity = TimelineActivity.this;
					timelineActivity.deleteAllStoredTweetAndUserData();
					ArrayList<Tweet> list = Tweet.fromJSONArray(jsonTweets);
					for(int i=0;i<list.size();i++){
						adapter.add(list.get(i));
					}
				}
				
				@Override 
				public void onFailure(Throwable e, JSONArray errorResponse){
					Toast.makeText(TimelineActivity.this, "Failed to connect to Network", Toast.LENGTH_SHORT).show();
					loadStoredData();
				}
			}, oldestId);
		} else {
			Toast.makeText(TimelineActivity.this, "Not connected to Network", Toast.LENGTH_SHORT).show();
			loadStoredData();
		}
	}
	
	private void loadStoredData(){
		adapter.clear();
		List<Tweet> list = getAllStoredTweets();
		for(int i=0;i<list.size();i++){
			adapter.add(list.get(i));
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.timeline, menu);
		btnCompose = menu.findItem(R.id.compose_button);
		((MenuItem)btnCompose).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener(){
			@Override
			public boolean onMenuItemClick(MenuItem item){
				onComposeClick(item);
				return true;
			}
		});
		return true;
	}

	public void onComposeClick(MenuItem menuItem){
    	composeTweet(-1);
    }
	
	protected void composeTweet(long tweetId){
		Intent i = new Intent(this, ComposeActivity.class);
		i.putExtra("reply_to_tweet_id",tweetId);
    	startActivityForResult(i, ComposeActivity.RESULT_ID);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode == RESULT_OK){
			Toast.makeText(this, data.getExtras().getString("tweet_text"), Toast.LENGTH_SHORT).show();
			client.postNewTweet(new JsonHttpResponseHandler(){
				@Override
				public void onSuccess(JSONObject tweetJson){
					Tweet tweet = Tweet.fromJSON(tweetJson);
					adapter.insert(tweet, 0);
				}
			}, data.getStringExtra("tweet_text"));
		}
	} 
	
	@Override
	protected void onPause(){
    	super.onPause();
	}
	
	@Override
	protected void onResume(){
    	super.onResume();
	}
	
	@Override
    protected void onStop(){
    	super.onStop();
    	saveData();
    }
	
	@Override
    protected void onDestroy(){
    	super.onDestroy();
    }
	
	protected void deleteAllStoredTweetAndUserData(){
		ActiveAndroid.beginTransaction();
		List<Tweet> list = getAllStoredTweets();
		List<User> userList = getAllStoredUsers();
    	try {
    	        for (int i = 0; i < list.size(); i++) {
    	            Tweet tweet = list.get(i);
    	            tweet.delete();
    	        }
    	        for (int i=0;i<userList.size();i++){
    	        	User user = userList.get(i);
    	        	user.delete();
    	        }
    	        ActiveAndroid.setTransactionSuccessful();
    	}
    	finally {
    	        ActiveAndroid.endTransaction();
    	}
	}
	
	protected void saveData(){
		ActiveAndroid.beginTransaction();
    	try {
    			int saveLimit = Math.min(TWEET_SAVE_LIMIT, adapter.getCount());
    	        for (int i = 0; i < saveLimit; i++) {
    	            Tweet tweet = adapter.getItem(i);
    	            tweet.user.save();
    	            tweet.save();
    	        }
    	        ActiveAndroid.setTransactionSuccessful();
    	}
    	finally {
    	        ActiveAndroid.endTransaction();
    	}
	}
	
	public List<Tweet> getAllStoredTweets() {
		return new Select()
			.from(Tweet.class)
			.orderBy("TweetId DESC")
			.execute();
	}
	
	public List<User> getAllStoredUsers() {
		return new Select()
			.from(User.class)
			.orderBy("UserId ASC")
			.execute();
	}
}
