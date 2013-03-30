package com.example.mytwittersearch.json;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.example.mytwittersearch.database.DatabaseMetaData;
import com.example.mytwittersearch.model.Tweet;
import com.example.mytwittersearch.model.TweetList;
import com.example.mytwittersearch.utils.ConstantValues;

public class JsonParser {

	public static TweetList parseJson(String json) {
		JSONObject obj;
		TweetList tweetList = null;
		try {
			obj = new JSONObject(json);
			JSONArray results = obj.getJSONArray("results");
			if (results != null && results.length() > 0) {
				tweetList = new TweetList();
				for (int i = 0; i < results.length(); ++i) {
					JSONObject entry = results.getJSONObject(i);
					Tweet tweet = new Tweet(
							entry.getString(DatabaseMetaData.JSON_ATTRIBUTES.FROM_USER),
							entry.getString(DatabaseMetaData.JSON_ATTRIBUTES.CREATED_AT),
							entry.getString(DatabaseMetaData.JSON_ATTRIBUTES.IMAGE_URL),
							entry.getString(DatabaseMetaData.JSON_ATTRIBUTES.TEXT));
					tweetList.add(tweet);
				}
			}
		} catch (JSONException e) {
			Log.e(ConstantValues.LOG_TAG, "parseJson(): " + e.getMessage());
		}
		return tweetList;
	}
}
