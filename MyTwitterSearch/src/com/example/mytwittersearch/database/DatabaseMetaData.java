package com.example.mytwittersearch.database;

import android.net.Uri;
import android.provider.BaseColumns;

public class DatabaseMetaData {
	public static final String AUTHORITY = "com.example.mytwittersearch.tweetcontentprovider";
	public static final String DATABASE_NAME = "mytwittersearch";
	public static final int DATABASE_VERSION = 1;
	
	public static interface TweetTableMetaData extends BaseColumns {
		public static final String TABLE_NAME = "tweets";
		public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME);
		
		// Table columns matches JSON attributes from search.twitter.com
		public static interface Columns {
			public static final String FROM_USER = "from_user";
			public static final String CREATED_AT = "created_at";
			public static final String IMAGE_URL = "profile_image_url";
			public static final String TEXT = "text";
		}
		
		public static final String CONTENT_LIST = "vnd.android.cursor.dir/vnd.tweetcontnetprovider.tweets";
		public static final String CONTENT_ITEM = "vnd.android.cursor.item/vnd.tweetcontnetprovider.tweets";
	}
	
	public static interface JSON_ATTRIBUTES {
		public static final String FROM_USER = "from_user";
		public static final String CREATED_AT = "created_at";
		public static final String IMAGE_URL = "profile_image_url";
		public static final String TEXT = "text";
	}	
}
