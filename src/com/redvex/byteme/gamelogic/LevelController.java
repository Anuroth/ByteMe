package com.redvex.byteme.gamelogic;

import java.util.ArrayList;
import java.util.Random;

import com.redvex.byteme.gamelogic.Helper.*;

public class LevelController {
	private GameType mGameType;
	private int mLevel;
	private int mRowsLeftAtLevel;
	private int mRange;
	private long mTimeInterval;
	private Random mRandom = new Random();

	public LevelController(GameType mGameType, int mLevel) {
		this.mGameType = mGameType;
		this.mLevel = mLevel;
		assignValues();
	}

	public int getLevel() {
		return mLevel;
	}

	public void setLevel(int mLevel) {
		this.mLevel = mLevel;
		assignValues();
	}

	public int getRowsLeftAtLevel() {
		return mRowsLeftAtLevel;
	}

	public int getRange() {
		return mRange;
	}

	public long getTimeInterval() {
		return mTimeInterval;
	}

	public ArrayList<GameField> getFixedValues() {
		ArrayList<GameField> mFixedValues = new ArrayList<GameField>();

		switch (mGameType) {
		case BinDec:
			if (mRandom.nextInt(2) == 0) {
				mFixedValues.add(GameField.Bin);
			} else {
				mFixedValues.add(GameField.Dec);
			}
			break;
		case BinHex:
			if (mRandom.nextInt(2) == 0) {
				mFixedValues.add(GameField.Bin);
			} else {
				mFixedValues.add(GameField.Hex);
			}
			break;
		case HexDec:
			if (mRandom.nextInt(2) == 0) {
				mFixedValues.add(GameField.Hex);
			} else {
				mFixedValues.add(GameField.Dec);
			}
			break;
		case BinHexDec:
			if (mLevel < 2) {
				// Two of the three GameFields is are fixed values.
				int mRandomValue = 99;
				int mTmpRandomValue = 99;

				for (int i = 0; i != 2; i++) {
					do {
						mRandomValue = mRandom.nextInt(3);
					} while (mTmpRandomValue == mRandomValue);

					mTmpRandomValue = mRandomValue;

					if (mRandomValue == 0) {
						mFixedValues.add(GameField.Bin);
					} else if (mRandomValue == 1) {
						mFixedValues.add(GameField.Hex);
					} else {
						mFixedValues.add(GameField.Dec);
					}
				}
			} else if (mLevel < 5) {
				// There is a 50:50 chance that one or two of the three
				// GameFields are fixed values.
				if (mRandom.nextInt(2) == 0) {
					// Two of the three GameFields is are fixed values.
					int mRandomValue = 99;
					int mTmpRandomValue = 99;

					for (int i = 0; i != 2; i++) {
						do {
							mRandomValue = mRandom.nextInt(3);
						} while (mTmpRandomValue == mRandomValue);

						mTmpRandomValue = mRandomValue;

						if (mRandomValue == 0) {
							mFixedValues.add(GameField.Bin);
						} else if (mRandomValue == 1) {
							mFixedValues.add(GameField.Hex);
						} else {
							mFixedValues.add(GameField.Dec);
						}
					}
				} else {
					// Just one the three GameFields is a fixed value.
					int mRandomValue = mRandom.nextInt(3);

					if (mRandomValue == 0) {
						mFixedValues.add(GameField.Bin);
					} else if (mRandomValue == 1) {
						mFixedValues.add(GameField.Hex);
					} else {
						mFixedValues.add(GameField.Dec);
					}
				}
			} else {
				// Just one the three GameFields is a fixed value.
				int mRandomValue = mRandom.nextInt(3);

				if (mRandomValue == 0) {
					mFixedValues.add(GameField.Bin);
				} else if (mRandomValue == 1) {
					mFixedValues.add(GameField.Hex);
				} else {
					mFixedValues.add(GameField.Dec);
				}
			}
		}

		return mFixedValues;
	}

	private void assignValues() {
		switch (mLevel) {
		case 1:
			mRowsLeftAtLevel = 16;
			mRange = 128;
			mTimeInterval = 10000;
			break;
		case 2:
			mRowsLeftAtLevel = 18;
			mRange = 256;
			mTimeInterval = 10000;
			break;
		case 3:
			mRowsLeftAtLevel = 20;
			mRange = 256;
			mTimeInterval = 95000;
			break;
		case 4:
			mRowsLeftAtLevel = 22;
			mRange = 256;
			mTimeInterval = 90000;
			break;
		case 5:
			mRowsLeftAtLevel = 24;
			mRange = 256;
			mTimeInterval = 85000;
			break;
		case 6:
			mRowsLeftAtLevel = 26;
			mRange = 256;
			mTimeInterval = 80000;
			break;
		case 7:
			mRowsLeftAtLevel = 28;
			mRange = 256;
			mTimeInterval = 75000;
			break;
		case 8:
			mRowsLeftAtLevel = 30;
			mRange = 256;
			mTimeInterval = 70000;
			break;
		case 9:
			mRowsLeftAtLevel = 0;
			mRange = 0;
			mTimeInterval = 10000;
		}
	}
}
