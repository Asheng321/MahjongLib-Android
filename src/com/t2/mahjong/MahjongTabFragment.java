
package com.t2.mahjong;

import com.t2.mahjong.db.MahjongContract;
import com.t2.mahjong.db.MahjongContract.Mahjong.Difficulty;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;

public class MahjongTabFragment extends ListFragment implements LoaderCallbacks<Cursor>, OnItemClickListener {

    private static final int LOADER_PUZZLES = 1;

    private MahjongPuzzleAdapter mAdapter;
    private Difficulty mDifficulty;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mDifficulty = Difficulty.valueOf(getArguments().getString("difficulty"));
        getLoaderManager().initLoader(LOADER_PUZZLES, null, this);
        mAdapter = new MahjongPuzzleAdapter(getActivity(), null);
        setListAdapter(mAdapter);
        getListView().setOnItemClickListener(this);
    }

    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(),
                MahjongContract.Mahjong.CONTENT_URI.buildUpon().appendPath(mDifficulty.name()).build(),
                new String[] {
                        MahjongContract.Mahjong.COL_TITLE, MahjongContract.Mahjong.COL_COMPLETE, MahjongContract.Mahjong._ID
                },
                null, null, null);
    }

    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        Cursor cursor = (Cursor) mAdapter.getItem(arg2);
        int id = cursor.getInt(cursor.getColumnIndex(MahjongContract.Mahjong._ID));

        PreferenceManager.getDefaultSharedPreferences(getActivity()).edit().putInt(getString(R.string.pref_mahjong_puzzle), id).commit();
        Intent intent = new Intent(getActivity(), getActivity().getClass());
        startActivity(intent);
        getActivity().finish();
    }

    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }

    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    private static final class MahjongPuzzleAdapter extends SimpleCursorAdapter {

        public MahjongPuzzleAdapter(Context context, Cursor c) {
            super(context, R.layout.mahjong_puzzle_row, c, new String[] {
                    MahjongContract.Mahjong.COL_TITLE,
                    MahjongContract.Mahjong.COL_COMPLETE
            }, new int[] {
                    R.id.lbl_name, R.id.img_complete
            }, 0);
        }

        @Override
        public void setViewImage(ImageView v, String value) {
            v.setVisibility(Integer.valueOf(value) == 1 ? View.VISIBLE : View.INVISIBLE);
        }

    }

}
