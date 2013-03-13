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
	private int mTotalScore = 0;
	private ArrayList<Integer> mLevelScore = new ArrayList<Integer>();
	private int mRowScore = 0;
	private int mScoreForSolvedRow = 15;
	private boolean mWon = false;
	private boolean mLost = false;
	private boolean mPaused = false;
	private long mStartTime = 0;
	private long mStopTime = 0;
	private long mElapsedTimeAtLevel = 0;

	public Game(GameType mGameType) {
		this.mGameType = mGameType;
		mLevelController = new LevelController(mGameType, mLevel);
		mRowsLeftAtLevel = mLevelController.getRowsLeftAtLevel();
		mLevelScore.add(0);
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

	/**
	 * @return The combined score earned by solving rows and the bonus score for
	 *         solving the level.
	 */
	public int getTotalScore() {
		return mTotalScore;
	}

	/**
	 * @return The bonus score for solving the level.
	 */
	public ArrayList<Integer> getLevelScore() {
		return mLevelScore;
	}

	/**
	 * @return The score earned by solving rows.
	 */
	public int getRowScore() {
		return mRowScore;
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
		if (mActiveRows >= mMaxActiveRows) {
			mLost = true;
		}

		int mRange = mLevelController.getRange();
		ArrayList<GameField> mFixedValues = mLevelController.getFixedValues();

		mActiveRows++;

		return new GameRow(mGameType, mRange, mFixedValues);
	}

	private void calculateBonusLevelScore() {
		long maxTime = ((long) (mLevelController.getRowsLeftAtLevel() - 2) + (long) mMaxActiveRows)
				* (mLevelController.getTimeInterval() / 1000);
		long neededTime = mElapsedTimeAtLevel / 1000;
		long bonusLevelScore = (maxTime - neededTime);

		mLevelScore.set(mLevel - 1, (int) bonusLevelScore);
		mLevelScore.add(0);

		mTotalScore += bonusLevelScore;
	}

	/**
	 * @return True if a new level is reached, otherwise false.
	 */
	public boolean removeRow() {
		mActiveRows--;
		mRowsLeftAtLevel--;
		mTotalRowsKilled++;
		mRowScore += mScoreForSolvedRow;
		mTotalScore += mScoreForSolvedRow;

		if (mRowsLeftAtLevel == 0) {
			// New level is reached.
			stopLevelTimer();
			calculateBonusLevelScore();
			mLevel++;
			mLevelController.setLevel(mLevel);
			mActiveRows = 0;
			mRowsLeftAtLevel = mLevelController.getRowsLeftAtLevel();

			if (mRowsLeftAtLevel == 0) {
				mWon = true;
			}

			return true;
		}

		return false;
	}

	/**
	 * Removes a row from the mRowsLeftAtLevel and adds bonus points to the
	 * score.
	 * 
	 * @return True if a new level is reached, otherwise false.
	 */
	public boolean boardCleared() {
		mRowsLeftAtLevel--;
		mRowScore += 2 * mScoreForSolvedRow;
		mTotalScore += 2 * mScoreForSolvedRow;

		if (mRowsLeftAtLevel == 0) {
			// New level is reached.
			stopLevelTimer();
			calculateBonusLevelScore();
			mLevel++;
			mLevelController.setLevel(mLevel);
			mActiveRows = 0;
			mRowsLeftAtLevel = mLevelController.getRowsLeftAtLevel();

			if (mRowsLeftAtLevel == 0) {
				mWon = true;
			}
			return true;
		}

		return false;
	}

	public void startLevelTimer() {
		mStartTime = System.currentTimeMillis();
		mElapsedTimeAtLevel = 0;
	}

	public void pauseLevelTimer() {
		mStopTime = System.currentTimeMillis();
		elapsedLevelTime();
	}

	public void resumeLevelTimer() {
		mStartTime = System.currentTimeMillis();
	}

	private void stopLevelTimer() {
		mStopTime = System.currentTimeMillis();
		elapsedLevelTime();
	}

	private void elapsedLevelTime() {
		mElapsedTimeAtLevel += mStartTime - mStopTime;
	}
}
