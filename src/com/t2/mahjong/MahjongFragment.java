package com.t2.mahjong;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.t2.mahjong.MahjongViewGroup.MahjongListener;
import com.t2.mahjong.db.MahjongContract;

public class MahjongFragment extends Fragment implements LoaderCallbacks<Cursor>, MahjongListener {

	private static final int LOADER_MAHJONG = 1;

	private int mPuzzleId;
	private boolean mCompleted;

	private MahjongListener mMahjongListener;

	public void setMahjongListener(MahjongListener mahjongListener) {
		mMahjongListener = mahjongListener;
	}

	public MahjongViewGroup getPuzzle() {
		return (MahjongViewGroup) getView().findViewById(R.id.lay_mahjong_tiles);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mPuzzleId = PreferenceManager.getDefaultSharedPreferences(getActivity()).getInt(getString(R.string.pref_mahjong_puzzle), -1);
		getPuzzle().setMahjonggListener(this);
		getLoaderManager().initLoader(LOADER_MAHJONG, null, this);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		return new CursorLoader(getActivity(), ContentUris.withAppendedId(MahjongContract.Mahjong.CONTENT_URI, mPuzzleId), null, null,
				null, null);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.mnu_mahjong, menu);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.mahjong_view, null);
		MahjongBackground background;
		try {
			background = MahjongBackground.valueOf(PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(
					getString(R.string.pref_mahjong_background), MahjongBackground.REDWOOD.name()));
		} catch (Exception e) {
			background = MahjongBackground.REDWOOD;
		}
		v.findViewById(R.id.lay_mahjong_board).setBackgroundResource(background.getResourceId());
		return v;
	}

	public void onGameComplete() {
		if (mMahjongListener != null) {
			mMahjongListener.onGameComplete();
		}
		mCompleted = true;
		Toast.makeText(getActivity(), "Game Complete", Toast.LENGTH_SHORT).show();
		ContentValues cv = new ContentValues();
		cv.put(MahjongContract.Mahjong.COL_COMPLETE, true);
		cv.put(MahjongContract.Mahjong.COL_CURRENT, (String) null);
		getActivity().getContentResolver().update(ContentUris.withAppendedId(MahjongContract.Mahjong.CONTENT_URI, mPuzzleId), cv, null,
				null);
	}

	public void onGameIncompletable() {
		if (mMahjongListener != null) {
			mMahjongListener.onGameIncompletable();
		}
		Toast.makeText(getActivity(), "No Solutions Left!", Toast.LENGTH_SHORT).show();
		resetPuzzle();
	}

	public void resetPuzzle() {
		ContentValues cv = new ContentValues();
		cv.put(MahjongContract.Mahjong.COL_CURRENT, (String) null);
		getActivity().getContentResolver().update(ContentUris.withAppendedId(MahjongContract.Mahjong.CONTENT_URI, mPuzzleId), cv, null,
				null);
	}

	@Override
	public void onDestroyView() {
		getPuzzle().clearCache();
		super.onDestroyView();
	}

	public void onLoaderReset(Loader<Cursor> loader) {
	}

	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		data.moveToFirst();
		getActivity().setTitle(data.getString(data.getColumnIndex(MahjongContract.Mahjong.COL_TITLE)));
		MahjongCursorLayout layout = new MahjongCursorLayout(data);
		getPuzzle().setGame(new Mahjong(layout));
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.mnu_hint) {
			getPuzzle().showHint();
			return true;
		} else if (item.getItemId() == R.id.mnu_change_puzzle) {
			PreferenceManager.getDefaultSharedPreferences(getActivity()).edit().remove(getString(R.string.pref_mahjong_puzzle)).commit();
			Intent intent = new Intent(getActivity(), getActivity().getClass());
			startActivity(intent);
			getActivity().finish();
			return true;
		} else if (item.getItemId() == R.id.mnu_reset_puzzle) {
			resetPuzzle();
			return true;
		} else if (item.getItemId() == R.id.mnu_highlight) {
			getPuzzle().setShowFreeTiles(!getPuzzle().isShowFreeTiles());
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onPause() {
		super.onPause();
		if (getPuzzle() != null && !mCompleted) {
			ContentValues cv = new ContentValues();
			cv.put(MahjongContract.Mahjong.COL_CURRENT, getPuzzle().getCurrentState());
			getActivity().getContentResolver().update(ContentUris.withAppendedId(MahjongContract.Mahjong.CONTENT_URI, mPuzzleId), cv, null,
					null);
		}
	}
}
