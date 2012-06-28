/**
 * 
 */
package com.t2.mahjong;

import android.app.backup.BackupAgentHelper;
import android.app.backup.FileBackupHelper;
import android.app.backup.SharedPreferencesBackupHelper;

import com.t2.mahjong.db.MahjongOpenHelper;

/**
 * @author wes
 * 
 */
public class MahjongBackupAgent extends BackupAgentHelper {

	static final String KEY_DB = MahjongOpenHelper.DB_NAME;
	static final String KEY_PREFS = "prefs";

	@Override
	public void onCreate() {
		super.onCreate();
		FileBackupHelper dbHelper = new FileBackupHelper(this, "../databases/" + MahjongOpenHelper.DB_NAME);
		addHelper(KEY_DB, dbHelper);
		SharedPreferencesBackupHelper helper = new SharedPreferencesBackupHelper(this, getPackageName() + "_preferences");
		addHelper(KEY_PREFS, helper);
	}

}
