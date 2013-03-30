package com.example.mytwittersearch.model;

import android.content.ContentValues;

import com.example.mytwittersearch.database.DatabaseMetaData;
import com.google.gson.annotations.SerializedName;

public class Tweet {
	@SerializedName("from_user")
	String mFromUser;
	@SerializedName("created_at")
	String mCreatedAt;
	@SerializedName("image_url")
	String mImageUrl;
	@SerializedName("text")
	String mText;

	public Tweet(String fromUser, String createdAt, String imageUrl, String text) {
		mFromUser = fromUser;
		mCreatedAt = createdAt;
		mImageUrl = imageUrl;
		mText = text;
	}

	public ContentValues toContentValues() {
		ContentValues values = new ContentValues();
		values.put(DatabaseMetaData.TweetTableMetaData.Columns.FROM_USER,
				getFromUser());
		values.put(DatabaseMetaData.TweetTableMetaData.Columns.CREATED_AT,
				getCreatedAt());
		values.put(DatabaseMetaData.TweetTableMetaData.Columns.IMAGE_URL,
				getImageUrl());
		values.put(DatabaseMetaData.TweetTableMetaData.Columns.TEXT, getText());
		return values;
	}

	/**
	 * @return the mFromUser
	 */
	public String getFromUser() {
		return mFromUser;
	}

	/**
	 * @param fromUser
	 *            the mFromUser to set
	 */
	public void setFromUser(String fromUser) {
		mFromUser = fromUser;
	}

	/**
	 * @return the mCreatedAt
	 */
	public String getCreatedAt() {
		return mCreatedAt;
	}

	/**
	 * @param createdAt
	 *            the mCreatedAt to set
	 */
	public void setmCreatedAt(String createdAt) {
		mCreatedAt = createdAt;
	}

	/**
	 * @return the mImageUrl
	 */
	public String getImageUrl() {
		return mImageUrl;
	}

	/**
	 * @param imageUrl
	 *            the mImageUrl to set
	 */
	public void setmImageUrl(String imageUrl) {
		mImageUrl = imageUrl;
	}

	/**
	 * @return the mText
	 */
	public String getText() {
		return mText;
	}

	/**
	 * @param mText
	 *            the mText to set
	 */
	public void setText(String text) {
		mText = text;
	}
}
