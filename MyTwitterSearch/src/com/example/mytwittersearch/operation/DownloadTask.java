package com.example.mytwittersearch.operation;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.os.AsyncTask;
import android.util.Log;

import com.example.mytwittersearch.database.DatabaseMetaData;
import com.example.mytwittersearch.json.JsonParser;
import com.example.mytwittersearch.model.TweetList;
import com.example.mytwittersearch.network.NetworkRequestExecutor;
import com.example.mytwittersearch.network.NetworkRequestExecutor.DownloadException;
import com.example.mytwittersearch.utils.ConstantValues;

public class DownloadTask extends AsyncTask<String, Integer, String> {

	private ContentResolver mContentResolver = null;

	public DownloadTask(ContentResolver contentResolver) {
		super();
		mContentResolver = contentResolver;
	}

	@Override
	protected String doInBackground(String... params) {
		try {
			return NetworkRequestExecutor.downloadFromServer(params[0]);
		} catch (DownloadException e) {
			return "";
		}
	}

	@Override
	protected void onPostExecute(String result) {
		if (mContentResolver == null) {
			Log.e(ConstantValues.LOG_TAG,
					"onPostExecute(): ContentResolver is null");
			return;
		}

		if (result == null || result.length() == 0) {
			Log.e(ConstantValues.LOG_TAG,
					"onPostExecute(): Result is null/empty");
			return;
		}

		// Insert into the database
		TweetList tweetList = JsonParser.parseJson(result);
		ContentValues[] values = tweetList.toContentValues();
		// Remove old data
		mContentResolver.delete(
				DatabaseMetaData.TweetTableMetaData.CONTENT_URI, null, null);
		// Insert new data
		mContentResolver.bulkInsert(
				DatabaseMetaData.TweetTableMetaData.CONTENT_URI, values);
		// Notify data change
		mContentResolver.notifyChange(
				DatabaseMetaData.TweetTableMetaData.CONTENT_URI, null);
	}
}
