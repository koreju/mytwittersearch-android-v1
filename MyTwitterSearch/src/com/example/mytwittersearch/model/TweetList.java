package com.example.mytwittersearch.model;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;

public class TweetList {
	private final List<Tweet> mTweetList = new ArrayList<Tweet>();

	/**
	 * @return the mTweetList
	 */
	public List<Tweet> getTweetList() {
		return mTweetList;
	}
	
	public int getSize() {
		return mTweetList.size();
	}

	public void add(Tweet tweet) {
		mTweetList.add(tweet);
	}
	
	public Tweet getTweet(int position) {
		return mTweetList.get(position);
	}
	
	public void clear() {
		mTweetList.clear();
	}

	public ContentValues[] toContentValues() {
		if (mTweetList == null) {
			return null;
		}

		ContentValues[] values = new ContentValues[mTweetList.size()];
		for (int i = 0; i < values.length; ++i) {
			if (mTweetList.get(i) != null) {
				values[i] = mTweetList.get(i).toContentValues();
			}
		}
		return values;
	}
}
