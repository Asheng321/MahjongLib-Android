/*
 * 
 * MahjongLib
 * 
 * Copyright © 2009-2012 United States Government as represented by 
 * the Chief Information Officer of the National Center for Telehealth 
 * and Technology. All Rights Reserved.
 * 
 * Copyright © 2009-2012 Contributors. All Rights Reserved. 
 * 
 * THIS OPEN SOURCE AGREEMENT ("AGREEMENT") DEFINES THE RIGHTS OF USE, 
 * REPRODUCTION, DISTRIBUTION, MODIFICATION AND REDISTRIBUTION OF CERTAIN 
 * COMPUTER SOFTWARE ORIGINALLY RELEASED BY THE UNITED STATES GOVERNMENT 
 * AS REPRESENTED BY THE GOVERNMENT AGENCY LISTED BELOW ("GOVERNMENT AGENCY"). 
 * THE UNITED STATES GOVERNMENT, AS REPRESENTED BY GOVERNMENT AGENCY, IS AN 
 * INTENDED THIRD-PARTY BENEFICIARY OF ALL SUBSEQUENT DISTRIBUTIONS OR 
 * REDISTRIBUTIONS OF THE SUBJECT SOFTWARE. ANYONE WHO USES, REPRODUCES, 
 * DISTRIBUTES, MODIFIES OR REDISTRIBUTES THE SUBJECT SOFTWARE, AS DEFINED 
 * HEREIN, OR ANY PART THEREOF, IS, BY THAT ACTION, ACCEPTING IN FULL THE 
 * RESPONSIBILITIES AND OBLIGATIONS CONTAINED IN THIS AGREEMENT.
 * 
 * Government Agency: The National Center for Telehealth and Technology
 * Government Agency Original Software Designation: MahjongLib001
 * Government Agency Original Software Title: MahjongLib
 * User Registration Requested. Please send email 
 * with your contact information to: robert.kayl2@us.army.mil
 * Government Agency Point of Contact for Original Software: robert.kayl2@us.army.mil
 * 
 */
package com.t2.mahjong.db;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

import com.t2.mahjong.db.MahjongContract.Mahjong;

/**
 * @author wes
 * 
 */
public class MahjongProvider extends ContentProvider {

	private static final String TAG = "MahjongProvider";

	private static final int MAHJONG_ID = 1;
	private static final int MAHJONG_DIFFICULTY = 2;
	private static final int MAHJONG = 3;

	private static final UriMatcher sUriMatcher;

	private MahjongOpenHelper mHelper;

	/**
	 * Static Initialization
	 */
	static {
		// Initialize URI Matcher
		sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		sUriMatcher.addURI(MahjongContract.AUTHORITY, Mahjong.PATH, MAHJONG);
		sUriMatcher.addURI(MahjongContract.AUTHORITY, Mahjong.PATH_FOR_ID, MAHJONG_ID);
		sUriMatcher.addURI(MahjongContract.AUTHORITY, Mahjong.PATH_FOR_DIFFICULTY, MAHJONG_DIFFICULTY);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.content.ContentProvider#delete(android.net.Uri,
	 * java.lang.String, java.lang.String[])
	 */
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		Log.d(TAG, "Delete called on URI " + uri);

		switch (sUriMatcher.match(uri)) {
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.content.ContentProvider#getType(android.net.Uri)
	 */
	@Override
	public String getType(Uri uri) {
		int match = sUriMatcher.match(uri);
		switch (match) {
		case MAHJONG_ID:
			return Mahjong.CONTENT_ITEM_MIME_TYPE;
		case MAHJONG_DIFFICULTY:
			return Mahjong.CONTENT_MIME_TYPE;
		default:
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.content.ContentProvider#insert(android.net.Uri,
	 * android.content.ContentValues)
	 */
	@Override
	public Uri insert(Uri uri, ContentValues values) {
		Log.d(TAG, "Insert called on URI " + uri);

		Uri resultUri = null;
		String table = null;

		switch (sUriMatcher.match(uri)) {
		case MAHJONG:
			table = Mahjong.TABLE_NAME;
			resultUri = Mahjong.CONTENT_URI;
			break;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}

		SQLiteDatabase db = mHelper.getWritableDatabase();
		long rowId = db.insert(table, null, values);

		if (rowId > 0) {
			getContext().getContentResolver().notifyChange(uri, null);
			return resultUri.buildUpon().appendPath("" + rowId).build();
		}

		throw new android.database.SQLException("Unable to insert row into " + uri);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.content.ContentProvider#onCreate()
	 */
	@Override
	public boolean onCreate() {
		mHelper = new MahjongOpenHelper(getContext());
		return true;
	}

	public void refreshDatabase() {
		mHelper.close();
		mHelper = new MahjongOpenHelper(getContext());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.content.ContentProvider#query(android.net.Uri,
	 * java.lang.String[], java.lang.String, java.lang.String[],
	 * java.lang.String)
	 */
	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
			String sortOrder) {
		Log.d(TAG, "Query called on URI " + uri);

		SQLiteQueryBuilder builder = new SQLiteQueryBuilder();

		switch (sUriMatcher.match(uri)) {
		case MAHJONG_DIFFICULTY:
			String difficulty = uri.getPathSegments().get(Mahjong.MAHJONG_PUZZLE_DIFFICULTY_POSITION);
			builder.setTables(Mahjong.TABLE_NAME);
			builder.appendWhere(Mahjong.COL_DIFFICULTY + " = '" + difficulty + "'");
			break;
		case MAHJONG_ID:
			String id = uri.getPathSegments().get(Mahjong.MAHJONG_PUZZLE_ID_POSITION);
			builder.setTables(Mahjong.TABLE_NAME);
			builder.appendWhere(Mahjong._ID + " = " + id);
			break;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}

		Cursor result = builder.query(mHelper.getReadableDatabase(), projection, selection,
				selectionArgs, null, null, sortOrder);
		result.setNotificationUri(getContext().getContentResolver(), uri);

		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.content.ContentProvider#update(android.net.Uri,
	 * android.content.ContentValues, java.lang.String, java.lang.String[])
	 */
	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		Log.d(TAG, "Update called on URI " + uri);

		String table = null;
		String where = null;

		switch (sUriMatcher.match(uri)) {
		case MAHJONG_ID:
			String id = uri.getPathSegments().get(Mahjong.MAHJONG_PUZZLE_ID_POSITION);
			table = Mahjong.TABLE_NAME;
			where = Mahjong._ID + " = " + id;
			break;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}

		if (where != null && selection != null) {
			selection = where + " AND " + selection;
		} else if (where != null) {
			selection = where;
		}

		SQLiteDatabase db = mHelper.getWritableDatabase();
		int count = db.update(table, values, selection, selectionArgs);
		getContext().getContentResolver().notifyChange(uri, null);

		return count;
	}

}
