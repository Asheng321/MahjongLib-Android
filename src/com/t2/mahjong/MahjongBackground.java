package com.t2.mahjong;

public enum MahjongBackground {
	BUBBLES("Bubbles", R.drawable.bg_bubbs),
	CANVAS("Red Canvas", R.drawable.bg_canvas),
	CIRCLES("Circles", R.drawable.bg_circles),
	CLOUDS("Clouds", R.drawable.bg_clouds),
	DROPLET("Droplet", R.drawable.bg_droplet),
	PINE("Pine", R.drawable.bg_pine),
	REDWOOD("Redwood", R.drawable.bg_redwood),
	SLATS("Slats", R.drawable.bg_slats);

	private String mName;
	private int mResourceId;

	private MahjongBackground(String name, int id) {
		mName = name;
		mResourceId = id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return mName;
	}

	/**
	 * @return the id
	 */
	public int getResourceId() {
		return mResourceId;
	}

	@Override
	public String toString() {
		return mName;
	}
}
