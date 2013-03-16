package com.redvex.byteme.gamelogic;

import java.util.ArrayList;
import java.util.Locale;

import com.actionbarsherlock.app.SherlockFragment;
import com.redvex.byteme.gamelogic.Helper.*;
import com.redvex.byteme.ui.UIRow;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;

public class GameLogicFragment extends SherlockFragment {
	/**
	 * The fragment argument representing the item ID that this fragment
	 * represents.
	 */
	public static final String GAME_TYPE = "game_id";

	private Handler mHandler = new Handler();

	private Runnable mRunnable = new Runnable() {
		@Override
		public void run() {
			gameLoop();
		}
	};

	private Game mGame = null;

	private GameType mGameType;

	private ArrayList<GameRow> mGameRows = new ArrayList<GameRow>();

	/**
	 * The fragment's current UI object. It is used to interact with the User
	 * Interface/ Game Field.
	 */
	private UI mUI = sUIDummy;

	public interface UI {
		public void addUIRow(UIRow row);

		public void removeUIRow(int index);

		public void clearUIBoard();

		public void displayLevel(int level);

		public void displayScore(int score);

		public void displayLinesLeft(int linesLeft);

		public void displayWinScreen(int totalScore, int rowScore, ArrayList<Integer> levelScore);

		public void displayLostScreen(int level, int totalScore, int rowScore,
				ArrayList<Integer> levelScore);
	}

	/**
	 * A dummy implementation of the {@link UI} interface that does nothing.
	 * Used only when this fragment is not attached to an activity.
	 */
	private static UI sUIDummy = new UI() {
		@Override
		public void addUIRow(UIRow row) {
		}

		@Override
		public void removeUIRow(int index) {
		}

		@Override
		public void clearUIBoard() {
		}

		@Override
		public void displayLevel(int level) {
		}

		@Override
		public void displayScore(int score) {
		}

		@Override
		public void displayLinesLeft(int linesLeft) {
		}

		@Override
		public void displayWinScreen(int totalScore, int rowScore, ArrayList<Integer> levelScore) {
		}

		@Override
		public void displayLostScreen(int level, int totalScore, int rowScore,
				ArrayList<Integer> levelScore) {
		}
	};

	private void startGame() {
		if (mGame.getActiveRows() == 0 && mGame.getTotalRowsKilled() == 0) {
			mUI.displayLevel(mGame.getLevel());
			mUI.displayScore(mGame.getTotalScore());
			mUI.displayLinesLeft(mGame.getRowsLeftAtLevel());
			addRow();
		}
	}

	public void pauseGame() {
		try {
			mGame.pauseLevelTimer();
		} catch (NullPointerException e) {
			throw new NullPointerException("No mGame object to pause the game.");
		}
	}

	public void resumeGame() {
		mUI.clearUIBoard();
		for (int i = 0; i != mGameRows.size(); i++) {
			mUI.addUIRow(gameRow2UIRow(mGameRows.get(i)));
		}
		try {
			mGame.resumeLevelTimer();
		} catch (NullPointerException e) {
			throw new NullPointerException("No mGame object to resume the game.");
		}
		mUI.displayLevel(mGame.getLevel());
		mUI.displayScore(mGame.getTotalScore());
		mUI.displayLinesLeft(mGame.getRowsLeftAtLevel());
	}

	public boolean isGamePaused() {
		try {
			return mGame.isPaused();
		} catch (NullPointerException e) {
			throw new NullPointerException("No mGame object to check if the game is paused.");
		}
	}

	public boolean isGameWon() {
		try {
			return mGame.isWon();
		} catch (NullPointerException e) {
			throw new NullPointerException("No mGame object to check if the game is won.");
		}
	}

	public boolean isGameLost() {
		try {
			return mGame.isLost();
		} catch (NullPointerException e) {
			throw new NullPointerException("No mGame object to check if the game is lost.");
		}
	}

	public void updateRow(int index, UIRow row) {
		String tempBinRow = "";

		// Storing the eight string bits into a single string representing the
		// byte.
		for (int i = 0; i != 8; i++) {
			String tempBit = row.getBinRow().get(i);
			tempBinRow += tempBit;
		}

		// Getting the integers out of the strings.
		mGameRows.get(index).setBin(Integer.parseInt(tempBinRow, 2));
		if (!row.getDecRow().isEmpty()) {
			mGameRows.get(index).setDec(Integer.parseInt(row.getDecRow()));
		}
		if (!row.getHexRow().isEmpty()) {
			mGameRows.get(index).setHex(Integer.parseInt(row.getHexRow(), 16));
		}

		// If the values of the current game type match the row gets removed.
		if (mGameRows.get(index).compareRow()) {
			removeRow(index);
		}
	}

	private UIRow gameRow2UIRow(GameRow gameRow) {
		ArrayList<String> binUIRow = new ArrayList<String>();
		String tempBin = Integer.toBinaryString(gameRow.getBin());
		int difference = 8 - tempBin.length();

		for (int i = 0; i != 8; i++) {
			String tempBit = "0";

			if (i >= difference) {
				tempBit = String.valueOf(tempBin.charAt(i - difference));
			}

			binUIRow.add(tempBit);
		}

		String decUIRow = Integer.toString(gameRow.getDec());
		String hexUIRow = Integer.toHexString(gameRow.getHex()).toUpperCase(Locale.US);

		boolean fixedBinUIRow = false;
		boolean fixedDecUIRow = false;
		boolean fixedHexUIRow = false;

		ArrayList<GameField> fixedValues = gameRow.getFixedValues();
		for (int i = 0; i != fixedValues.size(); i++) {
			switch (fixedValues.get(i)) {
			case Bin:
				fixedBinUIRow = true;
				break;
			case Dec:
				fixedDecUIRow = true;
				break;
			case Hex:
				fixedHexUIRow = true;
			}
		}

		return new UIRow(binUIRow, decUIRow, hexUIRow, fixedBinUIRow, fixedDecUIRow, fixedHexUIRow);
	}

	private void addRow() {
		GameRow tempGameRow = mGame.addRow();

		if (!mGame.isLost()) {
			mGameRows.add(tempGameRow);
			mUI.addUIRow(gameRow2UIRow(tempGameRow));
		} else {
			mUI.displayLostScreen(mGame.getLevel(), mGame.getTotalScore(), mGame.getRowScore(),
					mGame.getLevelScore());
		}
	}

	private void removeRow(int index) {
		boolean levelStatus = mGame.removeRow();

		mUI.displayScore(mGame.getTotalScore());
		mUI.displayLinesLeft(mGame.getRowsLeftAtLevel());

		if (mGame.isWon()) {
			mUI.removeUIRow(index);
			mUI.displayWinScreen(mGame.getTotalScore(), mGame.getRowScore(), mGame.getLevelScore());
		} else {
			if (levelStatus) {
				// Level finished and new level reached.
				// Timer is reseted.
				mHandler.removeCallbacks(mRunnable);
				mUI.displayLevel(mGame.getLevel());
				clearBoard();
				mGame.startLevelTimer();
				addRow();
				addRow();
				mHandler.postDelayed(mRunnable, mGame.getTimeInterval());
			} else {
				mGameRows.remove(index);
				mUI.removeUIRow(index);
			}

			// Wait until the first line is solved to start the timer.
			if (mGame.getTotalRowsKilled() == 1) {
				mRunnable.run();
				mGame.startLevelTimer();
			}

			// If the first line has already been solved and the user
			// cleared the whole board two new rows are added. Also the
			// timer of the gameLoop is reseted.
			if (mGame.getTotalRowsKilled() != 1 && mGame.getActiveRows() == 0) {
				mHandler.removeCallbacks(mRunnable);
				if (mGame.boardCleared()) {
					// Level finished and new level reached.
					mUI.displayLevel(mGame.getLevel());
					mGame.startLevelTimer();
				}
				addRow();
				addRow();
				mUI.displayScore(mGame.getTotalScore());
				mUI.displayLinesLeft(mGame.getRowsLeftAtLevel());
				mHandler.postDelayed(mRunnable, mGame.getTimeInterval());
			}
		}
	}

	private void clearBoard() {
		mUI.clearUIBoard();
		while (!mGameRows.isEmpty()) {
			mGameRows.remove(0);
		}
	}

	private void gameLoop() {
		if (!mGame.isPaused() && !mGame.isWon() && !mGame.isLost()) {
			addRow();
		}
		if (!mGame.isWon() && !mGame.isLost()) {
			mHandler.postDelayed(mRunnable, mGame.getTimeInterval());
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mGameType = GameType.values()[Integer.parseInt(getArguments().getString(GAME_TYPE)) - 1];
		mGame = new Game(mGameType);
	}

	@Override
	public void onStart() {
		super.onStart();

		startGame();
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		// Activities containing this fragment must implement its UI.
		if (!(activity instanceof UI)) {
			throw new IllegalStateException("Activity must implement fragment's UI.");
		}

		mUI = (UI) activity;
	}

	@Override
	public void onDetach() {
		super.onDetach();

		// Reset the active UI interface to the dummy implementation.
		mUI = sUIDummy;
	}
}
