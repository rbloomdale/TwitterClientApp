package com.codepath.twitterclient;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

public class ComposeActivity extends Activity {
	
	public static int RESULT_ID = 1337;
	protected Button btnTweet;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_compose);
		btnTweet = (Button) findViewById(R.id.btnTweet);
		btnTweet.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View view){
				onSaveTweet(view);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.compose, menu);
		return true;
	}
	
	public void onSaveTweet(View view){
		AutoCompleteTextView actvTweet = (AutoCompleteTextView) findViewById(R.id.actvComposeTweet);
		Intent data = this.getIntent();
		data.putExtra("tweet_text", actvTweet.getText().toString());
		setResult(RESULT_OK, data);
		finish();
	}
	
	@Override
	public void onBackPressed(){
		super.onBackPressed();
		Intent data = this.getIntent();
		setResult(RESULT_CANCELED, data);
		finish();
	}

}
