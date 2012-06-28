package com.t2.mahjong;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;

import com.t2.mahjong.db.MahjongContract;

public class MahjongCursorLayout extends MahjongLayout implements MahjongStatefulLayout {
	private List<Tile> mTiles;

	public MahjongCursorLayout(Cursor cursor) {
		super();
		String board = cursor.getString(cursor.getColumnIndex(MahjongContract.Mahjong.COL_PUZZLE));
		String state = cursor.getString(cursor.getColumnIndex(MahjongContract.Mahjong.COL_CURRENT));
		mTiles = new ArrayList<Tile>();
		String[] elems = board.split("\\|");
		for (int i = 0; i < elems.length; i++) {
			String elem = elems[i];
			boolean visible = state != null ? state.charAt(i) == '1' : true;
			String[] vals = elem.split(",");
			TileSlot slot = new TileSlot(Integer.valueOf(vals[0]), Integer.valueOf(vals[1]), Integer.valueOf(vals[2]));
			Tile tile = new Tile(slot);
			tile.setVisible(visible);
			tile.setMatchId(Integer.valueOf(vals[3]));
			tile.setSubId(Integer.valueOf(vals[4]));
			mSlots.add(slot);
			mTiles.add(tile);
		}
	}

	public List<Tile> getTiles() {
		return mTiles;
	}
}