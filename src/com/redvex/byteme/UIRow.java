package com.redvex.byteme;

import java.util.ArrayList;

/**
 * @author Anuroth
 * 
 *         This class contains all Strings for a single row. An object of this
 *         class may be used for the communication of the UI handling class and
 *         the game logic class.
 */
public class UIRow {
	private ArrayList<String> mBinRow;
	private boolean mFixedBinRow;
	private String mDecRow;
	private boolean mFixedDecRow;
	private String mHexRow;
	private boolean mFixedHexRow;

	/**
	 * Constructor filling every String with "0"
	 */
	public UIRow() {
		this.mBinRow = new ArrayList<String>();
		for (int i = 0; i < 8; i++) {
			this.mBinRow.add("0");
		}
		this.mDecRow = "0";
		this.mHexRow = "0";

		this.mFixedBinRow = true;
		this.mFixedDecRow = true;
		this.mFixedHexRow = true;
	}

	/**
	 * Constructor setting all variables with custom values.
	 * 
	 * @param mBinRow
	 *            String array containing the binary row values.
	 * @param mDecRow
	 *            String containing the decimal row value.
	 * @param mHexRow
	 *            String containing the hexadecimal row value.
	 */
	public UIRow(ArrayList<String> mBinRow, String mDecRow, String mHexRow, boolean mFixedBinRow,
			boolean mFixedDecRow, boolean mFixedHexRow) {
		this.mBinRow = mBinRow;
		this.mDecRow = mDecRow;
		this.mHexRow = mHexRow;

		this.mFixedBinRow = mFixedBinRow;
		this.mFixedDecRow = mFixedDecRow;
		this.mFixedHexRow = mFixedHexRow;
	}

	/**
	 * @return the binRow
	 */
	public ArrayList<String> getBinRow() {
		return mBinRow;
	}

	/**
	 * @param mBinRow
	 *            the mBinRow to set
	 */
	public void setBinRow(ArrayList<String> mbinRow) {
		this.mBinRow = mbinRow;
	}

	public boolean isFixedBinRow() {
		return mFixedBinRow;
	}

	public void setFixedBinRow(boolean mFixedBinRow) {
		this.mFixedBinRow = mFixedBinRow;
	}

	/**
	 * @return the mDecRow
	 */
	public String getDecRow() {
		return mDecRow;
	}

	/**
	 * @param mDecRow
	 *            the mDecRow to set
	 */
	public void setDecRow(String mDecRow) {
		this.mDecRow = mDecRow;
	}

	public boolean isFixedDecRow() {
		return mFixedDecRow;
	}

	public void setFixedDecRow(boolean mFixedDecRow) {
		this.mFixedDecRow = mFixedDecRow;
	}

	/**
	 * @return the mHexRow
	 */
	public String getHexRow() {
		return mHexRow;
	}

	/**
	 * @param mHexRow
	 *            the mHexRow to set
	 */
	public void setHexRow(String mHexRow) {
		this.mHexRow = mHexRow;
	}

	public boolean isFixedHexRow() {
		return mFixedHexRow;
	}

	public void setFixedHexRow(boolean mFixedHexRow) {
		this.mFixedHexRow = mFixedHexRow;
	}
}
