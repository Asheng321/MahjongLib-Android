package com.t2.mahjong;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

public class TestMahjongActivity extends FragmentActivity {

	private static final String TAG = "MahjongActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		int puzzleId = PreferenceManager.getDefaultSharedPreferences(this).getInt(getString(R.string.pref_mahjong_puzzle), -1);
		if (getSupportFragmentManager().findFragmentByTag(TAG) == null) {
			if (puzzleId >= 0) {
				Fragment fragment = new MahjongFragment();
				FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
				ft.add(R.id.lay_mahjong, fragment, TAG);
				ft.commit();
			} else {
				Fragment fragment = new MahjongPuzzlesFragment();
				FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
				ft.add(R.id.lay_mahjong, fragment, TAG);
				ft.commit();
			}
		}
	}

}
