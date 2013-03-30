package com.example.mytwittersearch.activity;

import java.util.concurrent.TimeUnit;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mytwittersearch.R;
import com.example.mytwittersearch.adapter.JsonAdapter;
import com.example.mytwittersearch.database.DatabaseMetaData;
import com.example.mytwittersearch.model.Tweet;
import com.example.mytwittersearch.model.TweetList;
import com.example.mytwittersearch.operation.DownloadTask;
import com.example.mytwittersearch.utils.ConstantValues;

public class MainActivity extends Activity {
	public static final int UPDATE_DATA = 1;
	private static final int MENU_EXIT_ID = Menu.FIRST;
	private static final int MENU_ABOUT_ID = MENU_EXIT_ID + 1;

	private EditText mKeywordText = null;
	private ImageButton mSearchBtn = null;
	private TextView mTweetHeader = null;
	private InputMethodManager mInputMethodManager = null;
	private ListView mListView = null;

	private JsonAdapter mJsonAdapter = null;
	private TweetList mTweetList = null;

	private ContentResolver mContentResolver = null;

	private OnClickListener mSearchHandler = new OnClickListener() {
		@Override
		public void onClick(View v) {
			onSearchStart();
		}
	};

	private OnKeyListener mEnterKeyHandler = new OnKeyListener() {
		@Override
		public boolean onKey(View v, int keyCode, KeyEvent event) {
			if (keyCode == KeyEvent.KEYCODE_ENTER) {
				onSearchStart();
				return true;
			}
			return false;
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mKeywordText = (EditText) findViewById(R.id.search_key);
		mSearchBtn = (ImageButton) findViewById(R.id.btn_search);
		mTweetHeader = (TextView) findViewById(R.id.tweet_header);
		mListView = (ListView) findViewById(R.id.tweet_list);

		mKeywordText.setOnKeyListener(mEnterKeyHandler);
		mSearchBtn.setOnClickListener(mSearchHandler);

		mInputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

		mTweetList = new TweetList();
		mJsonAdapter = new JsonAdapter(LayoutInflater.from(MainActivity.this),
				mTweetList);
		mListView.setAdapter(mJsonAdapter);
		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(MainActivity.this,
						TweetActivity.class);
				ImageView imageView = (ImageView) view
						.findViewById(R.id.user_photo);
				Bitmap bitmap = ((BitmapDrawable) (imageView.getDrawable()))
						.getBitmap();
				intent.putExtra(ConstantValues.TWEET_PHOTO, (Parcelable) bitmap);
				TextView textView = (TextView) view.findViewById(R.id.text);
				intent.putExtra(ConstantValues.TWEET_CONTENT,
						textView.getText());
				textView = (TextView) view.findViewById(R.id.created_at);
				intent.putExtra(ConstantValues.TWEET_TIME, textView.getText());
				startActivity(intent);
			}
		});

		mContentResolver = getContentResolver();

		mContentResolver.registerContentObserver(
				Uri.parse("content://" + DatabaseMetaData.AUTHORITY), true,
				new TweetObserver(new Handler()));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, MENU_EXIT_ID, 0, R.string.menu_exit);
		menu.add(0, MENU_ABOUT_ID, 0, R.string.menu_about);
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case MENU_EXIT_ID:
			finish();
			return true;
		case MENU_ABOUT_ID:
			Toast.makeText(getApplicationContext(), "About this app...",
					Toast.LENGTH_LONG).show();
			return true;
		}
		return super.onContextItemSelected(item);
	}

	private void onSearchStart() {
		// Hide the soft keyboard
		mInputMethodManager.hideSoftInputFromWindow(
				mKeywordText.getWindowToken(), 0);
		DownloadTask downloadTask = new DownloadTask(mContentResolver);
		downloadTask.execute("?rpp=50&q="
				+ Uri.encode(mKeywordText.getText().toString()));
		try {
			downloadTask.get(ConstantValues.TIMEOUT, TimeUnit.MILLISECONDS);
		} catch (Exception e) {
			downloadTask.cancel(true);
			alert("Cannot retrieve tweets...");
		}
		Toast.makeText(getApplicationContext(), mKeywordText.getText(),
				Toast.LENGTH_LONG).show();
	}

	private void alert(String message) {
		Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG)
				.show();
	}

	private final class TweetObserver extends ContentObserver {

		public TweetObserver(Handler handler) {
			super(handler);
		}

		@Override
		public void onChange(boolean selfChange) {
			super.onChange(selfChange);
			Cursor cursor = mContentResolver.query(
					DatabaseMetaData.TweetTableMetaData.CONTENT_URI, null,
					null, null, null);
			mTweetHeader.setText(cursor.getCount() + " tweets");
			mTweetList.clear();
			while (cursor.moveToNext()) {
				Tweet tweet = new Tweet(
						cursor.getString(cursor
								.getColumnIndex(DatabaseMetaData.TweetTableMetaData.Columns.FROM_USER)),
						cursor.getString(cursor
								.getColumnIndex(DatabaseMetaData.TweetTableMetaData.Columns.CREATED_AT)),
						cursor.getString(cursor
								.getColumnIndex(DatabaseMetaData.TweetTableMetaData.Columns.IMAGE_URL)),
						cursor.getString(cursor
								.getColumnIndex(DatabaseMetaData.TweetTableMetaData.Columns.TEXT)));
				mTweetList.add(tweet);
			}
			mJsonAdapter.notifyDataSetChanged();
		}
	}
}
