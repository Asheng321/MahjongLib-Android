package com.t2.mahjong;

public class Tile {
	private TileSlot mSlot;

	private int mMatchId;
	private int mSubId; // used for flowers / seasons
	private boolean mVisible;

	public Tile(TileSlot slot) {
		mSlot = slot;
		mVisible = true;
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
		Tile other = (Tile) obj;
		if (mSlot == null) {
			if (other.mSlot != null)
				return false;
		} else if (!mSlot.equals(other.mSlot))
			return false;
		return true;
	}

	/**
	 * @return the matchId
	 */
	public int getMatchId() {
		return mMatchId;
	}

	public int getTileId() {
		if (mMatchId == 33) {
			return mMatchId + mSubId;
		} else if (mMatchId == 34) {
			return mMatchId + 3 + mSubId;
		} else {
			return mMatchId;
		}
	}

	/**
	 * @return the slot
	 */
	public TileSlot getSlot() {
		return mSlot;
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
		result = prime * result + ((mSlot == null) ? 0 : mSlot.hashCode());
		return result;
	}

	/**
	 * @return the visible
	 */
	public boolean isVisible() {
		return mVisible;
	}

	/**
	 * @param matchId
	 *            the matchId to set
	 */
	public void setMatchId(int matchId) {
		mMatchId = matchId;
	}

	/**
	 * @param slot
	 *            the slot to set
	 */
	public void setSlot(TileSlot slot) {
		mSlot = slot;
	}

	/**
	 * @param visible
	 *            the visible to set
	 */
	public void setVisible(boolean visible) {
		mVisible = visible;
	}

	/**
	 * @return the subId
	 */
	public int getSubId() {
		return mSubId;
	}

	/**
	 * @param subId
	 *            the subId to set
	 */
	public void setSubId(int subId) {
		mSubId = subId;
	}

}