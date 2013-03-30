package com.example.mytwittersearch.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mytwittersearch.R;
import com.example.mytwittersearch.utils.ConstantValues;

public class TweetActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tweet);
		Intent intent = getIntent();
		ImageView imageView = (ImageView) findViewById(R.id.tweet_user_photo);
		Bitmap bitmap = intent.getParcelableExtra(ConstantValues.TWEET_PHOTO);
		imageView.setImageDrawable(new BitmapDrawable(bitmap)); 
		TextView textView = (TextView) findViewById(R.id.tweet_text);
		textView.setText(intent.getStringExtra(ConstantValues.TWEET_CONTENT));
		textView = (TextView) findViewById(R.id.tweet_created_at);
		textView.setText(intent.getStringExtra(ConstantValues.TWEET_TIME));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.tweet, menu);
		return true;
	}

}
