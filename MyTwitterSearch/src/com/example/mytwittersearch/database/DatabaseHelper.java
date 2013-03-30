package com.example.mytwittersearch.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

	private static final String SQL_TWEETS_CREATE = "CREATE TABLE "
			+ DatabaseMetaData.TweetTableMetaData.TABLE_NAME + " ( "
			+ DatabaseMetaData.TweetTableMetaData._ID
			+ " INTEGER PRIMARY KEY, "
			+ DatabaseMetaData.TweetTableMetaData.Columns.FROM_USER
			+ " VAR(50) NOT NULL, "
			+ DatabaseMetaData.TweetTableMetaData.Columns.CREATED_AT
			+ " VAR(50) NOT NULL, "
			+ DatabaseMetaData.TweetTableMetaData.Columns.IMAGE_URL
			+ " VAR(100), " + DatabaseMetaData.TweetTableMetaData.Columns.TEXT
			+ " TEXT NOT NULL " + " ) ";
	private static final String SQL_TWEETS_DROP = "DROP TABLE IF EXISTS "
			+ DatabaseMetaData.TweetTableMetaData.TABLE_NAME;

	public DatabaseHelper(Context context) {
		super(context, DatabaseMetaData.DATABASE_NAME, null,
				DatabaseMetaData.DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(SQL_TWEETS_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(SQL_TWEETS_DROP);
		onCreate(db);
	}
}
