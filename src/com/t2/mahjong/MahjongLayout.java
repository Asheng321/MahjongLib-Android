package com.t2.mahjong;

import java.util.ArrayList;
import java.util.List;

public abstract class MahjongLayout {
	protected List<TileSlot> mSlots;

	public MahjongLayout() {
		mSlots = new ArrayList<TileSlot>();
	}

	/**
	 * @return the slots
	 */
	public List<TileSlot> getSlots() {
		return mSlots;
	}

	protected void addGrid(int topLeftRow, int topLeftCol, int width, int height, int startDepth) {
		for (int row = 0; row < height * 2; row += 2) {
			for (int col = 0; col < width * 2; col += 2) {
				addStack(topLeftRow + row, topLeftCol + col, 1, startDepth);
			}
		}
	}

	protected void addPyramid(int topLeftRow, int topLeftCol, int size, int height, boolean halfstep, int startDepth) {
		int step = halfstep ? 1 : 2;
		for (int depth = 0; depth < height; depth++) {
			addGrid(topLeftRow + (depth * step), topLeftCol + (depth * step), size - (depth * step), size - (depth * step), depth
					+ startDepth);
		}
	}

	protected void addStack(int row, int col, int height, int startDepth) {
		for (int i = 0; i < height; i++) {
			mSlots.add(new TileSlot(row, col, i + startDepth));
		}

		if (mSlots.size() > 144) {
			throw new IllegalArgumentException("More tiles than tile graphics support");
		}
	}

	protected void offset(int rows, int cols) {
		for (TileSlot slot : mSlots) {
			slot.setRow(slot.getRow() + rows);
			slot.setCol(slot.getCol() + cols);
		}
	}
}