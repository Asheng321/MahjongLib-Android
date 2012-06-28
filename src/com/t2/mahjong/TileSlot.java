package com.t2.mahjong;

public class TileSlot {
	private int mCol, mRow, mLayer;

	public TileSlot(int row, int col, int layer) {
		super();
		mCol = col;
		mRow = row;
		mLayer = layer;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TileSlot other = (TileSlot) obj;
		if (mLayer != other.mLayer)
			return false;
		if (mCol != other.mCol)
			return false;
		if (mRow != other.mRow)
			return false;
		return true;
	}

	/**
	 * @return the col
	 */
	public int getCol() {
		return mCol;
	}

	/**
	 * @return the layer
	 */
	public int getLayer() {
		return mLayer;
	}

	/**
	 * @return the row
	 */
	public int getRow() {
		return mRow;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + mLayer;
		result = prime * result + mCol;
		result = prime * result + mRow;
		return result;
	}

	/**
	 * @param col
	 *            the col to set
	 */
	public void setCol(int col) {
		mCol = col;
	}

	/**
	 * @param layer
	 *            the layer to set
	 */
	public void setLayer(int layer) {
		mLayer = layer;
	}

	/**
	 * @param row
	 *            the row to set
	 */
	public void setRow(int row) {
		mRow = row;
	}

}