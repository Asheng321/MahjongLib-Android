package com.t2.mahjong.db;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.t2.mahjong.R;
import com.t2.mahjong.db.MahjongContract.Mahjong;
import com.t2.mahjong.db.MahjongContract.Mahjong.Difficulty;

public class MahjongOpenHelper extends SQLiteOpenHelper {

	public static final int DB_VERSION = 11;
	public static final String DB_NAME = "mahjong.db";

	private Context mContext;

	private static final String CREATE_MAHJONG = ""
			+ "CREATE TABLE " + Mahjong.TABLE_NAME + "( "
			+ Mahjong._ID + " 				INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ Mahjong.COL_COMPLETE + " 		INTEGER NOT NULL DEFAULT 0,"
			+ Mahjong.COL_CURRENT + " 		TEXT,"
			+ Mahjong.COL_DIFFICULTY + " 	TEXT NOT NULL,"
			+ Mahjong.COL_PUZZLE + " 		TEXT NOT NULL,"
			+ Mahjong.COL_TITLE + " 		TEXT NOT NULL"
			+ ")";

	public MahjongOpenHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
		mContext = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_MAHJONG);
		loadData(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + Mahjong.TABLE_NAME);
		onCreate(db);
	}

	/**
	 * Puzzle file is formatted as follows difficulty|row,col,depth,match_id|...
	 * 
	 * @param db
	 */
	private void loadData(SQLiteDatabase db) {
		BufferedReader in = null;
		try {
			in = new BufferedReader(new InputStreamReader(mContext.getResources().openRawResource(R.raw.puzzle)));
			String line;
			Map<String, Integer> countMap = new HashMap<String, Integer>();
			while ((line = in.readLine()) != null) {
				ContentValues vals = new ContentValues();
				String[] elems = line.split("\\|");
				vals.put(Mahjong.COL_PUZZLE, line.substring(elems[0].length() + elems[1].length() + 2));
				Difficulty diff = Difficulty.valueOf(elems[1]);
				Integer count = countMap.get(elems[0] + diff.name());
				if (count == null) {
					count = 1;
				}
				vals.put(Mahjong.COL_DIFFICULTY, diff.name());
				vals.put(Mahjong.COL_TITLE, elems[0] + " - " + diff.toString() + " #" + count);
				countMap.put(elems[0] + diff.name(), count + 1);
				db.insert(Mahjong.TABLE_NAME, null, vals);
			}
		} catch (Exception e) {

		}

	}
}
