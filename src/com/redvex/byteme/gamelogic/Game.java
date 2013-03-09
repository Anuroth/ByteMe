package com.redvex.byteme.gamelogic;

import java.util.ArrayList;

import com.redvex.byteme.gamelogic.Helper.*;

public class Game {
	private GameType mGameType;
	private LevelController mLevelController;
	private int mLevel = 1;
	private int mActiveRows = 0;
	private int mMaxActiveRows = 10;
	private int mRowsLeftAtLevel;
	private int mTotalRowsKilled = 0;
	private int mScore = 0;
	private boolean mWon = false;
	private boolean mLost = false;
	private boolean mPaused = false;

	public Game(GameType mGameType) {
		this.mGameType = mGameType;
		mLevelController = new LevelController(mGameType, mLevel);
		mRowsLeftAtLevel = mLevelController.getRowsLeftAtLevel();
	}

	public GameType getGameType() {
		return mGameType;
	}

	public int getLevel() {
		return mLevel;
	}

	public int getActiveRows() {
		return mActiveRows;
	}

	public int getMaxActiveRows() {
		return mMaxActiveRows;
	}

	public int getRowsLeftAtLevel() {
		return mRowsLeftAtLevel;
	}

	public int getTotalRowsKilled() {
		return mTotalRowsKilled;
	}

	public int getScore() {
		return mScore;
	}

	public boolean isWon() {
		return mWon;
	}

	public boolean isLost() {
		return mLost;
	}

	public boolean isPaused() {
		return mPaused;
	}

	public void setPaused(boolean mPaused) {
		this.mPaused = mPaused;
	}

	public long getTimeInterval() {
		return mLevelController.getTimeInterval();
	}

	public GameRow addRow() {
		if (mActiveRows == mMaxActiveRows) {
			mLost = true;
		}

		int mRange = mLevelController.getRange();
		ArrayList<GameField> mFixedValues = mLevelController.getFixedValues();

		mActiveRows++;

		return new GameRow(mGameType, mRange, mFixedValues);
	}

	public int removeRow() {
		mActiveRows--;
		mRowsLeftAtLevel--;
		mTotalRowsKilled++;
		mScore += 10;

		if (mRowsLeftAtLevel == 0) {
			mLevel++;
			mLevelController.setLevel(mLevel);
			mRowsLeftAtLevel = mLevelController.getRowsLeftAtLevel();

			if (mRowsLeftAtLevel == 0) {
				mWon = true;
			}

			return 1;
		}

		return 0;
	}
}
