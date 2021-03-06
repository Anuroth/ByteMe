package com.redvex.byteme.gamelogic;

import java.util.ArrayList;
import java.util.Random;

import com.redvex.byteme.gamelogic.Helper.*;

public class GameRow {
	private GameType mGameType;
	private int mBin;
	private int mDec;
	private int mHex;
	private ArrayList<GameField> mFixedValues;
	private Random mRandom = new Random();

	public GameRow(GameType mGameType, ArrayList<GameField> mFixedValues) {
		this.mGameType = mGameType;
		mBin = 0;
		mDec = 1;
		mHex = 2;

		if (mFixedValues.size() < 3) {
			this.mFixedValues = mFixedValues;
		}
	}

	public GameRow(GameType mGameType, int mRange, ArrayList<GameField> mFixedValues) {
		this.mGameType = mGameType;

		if (mRange > 256) {
			mRange = 256;
		}

		// The values for the bin/dec/hex row are generated and
		// assigned, until every field contains different values.
		if (mFixedValues.contains(GameField.Bin)) {
			mBin = mRandom.nextInt(mRange);
		} else {
			// If the binary row has to be solved every bit is filled with
			// zeros.
			mBin = 0;
		}

		do {
			mDec = mRandom.nextInt(mRange);
		} while (mBin == mDec);

		do {
			mHex = mRandom.nextInt(mRange);
		} while (mBin == mHex || mDec == mHex);

		if (mFixedValues.size() < 3) {
			// Since there are just three fields (bin/dec/hex) the number of
			// fixed values must be below three.
			this.mFixedValues = mFixedValues;

			if (mFixedValues.size() > 1) {
				// If more than one field is fixed they must contain the same
				// value.
				if (mFixedValues.contains(GameField.Bin) && mFixedValues.contains(GameField.Dec)) {
					mBin = mDec;
				} else if (mFixedValues.contains(GameField.Bin)
						&& mFixedValues.contains(GameField.Hex)) {
					mBin = mHex;
				} else if (mFixedValues.contains(GameField.Dec)
						&& mFixedValues.contains(GameField.Hex)) {
					mDec = mHex;
				}
			}
		}
	}

	public int getBin() {
		return mBin;
	}

	public void setBin(int mBin) {
		this.mBin = mBin;
	}

	public int getDec() {
		return mDec;
	}

	public void setDec(int mDec) {
		this.mDec = mDec;
	}

	public int getHex() {
		return mHex;
	}

	public void setHex(int mHex) {
		this.mHex = mHex;
	}

	public ArrayList<GameField> getFixedValues() {
		return mFixedValues;
	}

	public void setFixedValues(ArrayList<GameField> mFixedValues) {
		if (mFixedValues.size() < 3) {
			this.mFixedValues = mFixedValues;
		}
	}

	public boolean compareRow() {
		switch (mGameType) {
		case BinDec:
			if (mBin == mDec) {
				return true;
			} else {
				return false;
			}
		case BinHex:
			if (mBin == mHex) {
				return true;
			} else {
				return false;
			}
		case HexDec:
			if (mHex == mDec) {
				return true;
			} else {
				return false;
			}
		case BinHexDec:
			if (mBin == this.mDec && mDec == mHex) {
				return true;
			} else {
				return false;
			}
		default:
			return false;
		}
	}
}
