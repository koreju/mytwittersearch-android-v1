package com.example.mytwittersearch.provider;

import com.example.mytwittersearch.database.DatabaseHelper;
import com.example.mytwittersearch.database.DatabaseMetaData;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.net.Uri.Builder;

public class TweetContentProvider extends ContentProvider {
	public static final class Segment {
		public static final int GET_TWEET_LIST = 1;
		public static final int GET_TWEET_ITEM = 2;
	}

	private static UriMatcher mUriMatcher = null;

	static {
		mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		mUriMatcher.addURI(DatabaseMetaData.AUTHORITY,
				DatabaseMetaData.TweetTableMetaData.TABLE_NAME,
				Segment.GET_TWEET_LIST);
		mUriMatcher.addURI(DatabaseMetaData.AUTHORITY,
				DatabaseMetaData.TweetTableMetaData.TABLE_NAME + "/#",
				Segment.GET_TWEET_ITEM);
	}

	private DatabaseHelper mDatabaseHelper = null;

	@Override
	public boolean onCreate() {
		if (mDatabaseHelper == null) {
			mDatabaseHelper = new DatabaseHelper(getContext());
		}
		return true;
	}

	@Override
	public String getType(Uri uri) {
		switch (mUriMatcher.match(uri)) {
		case Segment.GET_TWEET_LIST:
			return DatabaseMetaData.TweetTableMetaData.CONTENT_LIST;
		case Segment.GET_TWEET_ITEM:
			return DatabaseMetaData.TweetTableMetaData.CONTENT_ITEM;
		default:
			throw new UnsupportedOperationException("Not supported type: "
					+ uri);
		}
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
		long id = 0;

		switch (mUriMatcher.match(uri)) {
		case Segment.GET_TWEET_LIST:
			id = db.insert(DatabaseMetaData.TweetTableMetaData.TABLE_NAME,
					DatabaseMetaData.TweetTableMetaData._ID, values);
			Builder builder = uri.buildUpon();
			builder.appendEncodedPath(String.valueOf(id));
			return builder.build();
		case Segment.GET_TWEET_ITEM:
			return null;
		default:
			throw new UnsupportedOperationException("Not supported type: "
					+ uri);
		}
	}

	@Override
	public int bulkInsert(Uri uri, ContentValues[] values) {
		SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
		int numValues = values.length;
		switch (mUriMatcher.match(uri)) {
		case Segment.GET_TWEET_LIST:
			db.beginTransaction();
			try {
				for (ContentValues value : values) {
					insert(uri, value);
				}
				db.setTransactionSuccessful();
			} finally {
				db.endTransaction();
			}
			break;
		case Segment.GET_TWEET_ITEM:
			return 0;
		default:
			throw new UnsupportedOperationException("Not supported type: "
					+ uri);
		}
		return numValues;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
		int result = 0;

		switch (mUriMatcher.match(uri)) {
		case Segment.GET_TWEET_LIST:
			result = db.delete(DatabaseMetaData.TweetTableMetaData.TABLE_NAME,
					selection, selectionArgs);
			break;
		case Segment.GET_TWEET_ITEM:
			selection = DatabaseMetaData.TweetTableMetaData._ID + " = "
					+ ContentUris.parseId(uri);
			result = db.delete(DatabaseMetaData.TweetTableMetaData.TABLE_NAME,
					selection, selectionArgs);
			break;
		default:
			throw new UnsupportedOperationException("Not supported type: "
					+ uri);
		}
		return result;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
		Cursor cursor = null;

		switch (mUriMatcher.match(uri)) {
		case Segment.GET_TWEET_LIST:
			cursor = db
					.query(DatabaseMetaData.TweetTableMetaData.TABLE_NAME,
							projection, selection, selectionArgs, null, null,
							sortOrder);
			break;
		case Segment.GET_TWEET_ITEM:
			String where = DatabaseMetaData.TweetTableMetaData._ID + " = "
					+ ContentUris.parseId(uri);
			cursor = db.query(DatabaseMetaData.TweetTableMetaData.TABLE_NAME,
					projection, where, selectionArgs, null, null, sortOrder);
			break;
		default:
			throw new UnsupportedOperationException("Not supported type: "
					+ uri);
		}
		return cursor;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
		int result = 0;

		switch (mUriMatcher.match(uri)) {
		case Segment.GET_TWEET_LIST:
			result = db.update(DatabaseMetaData.TweetTableMetaData.TABLE_NAME,
					values, selection, selectionArgs);
			break;
		case Segment.GET_TWEET_ITEM:
			selection = DatabaseMetaData.TweetTableMetaData._ID + " = "
					+ ContentUris.parseId(uri);
			result = db.update(DatabaseMetaData.TweetTableMetaData.TABLE_NAME,
					values, selection, selectionArgs);
			break;
		default:
			throw new UnsupportedOperationException("Not supported type: "
					+ uri);
		}
		return result;
	}

}
