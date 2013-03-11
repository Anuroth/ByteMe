package com.redvex.byteme;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.redvex.byteme.gamelogic.GameLogicFragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;

/**
 * An activity representing a list of Games. This activity has different
 * presentations for handset and tablet-size devices. On handsets, the activity
 * presents a list of items, which when touched, lead to a
 * {@link HandsetDeviceGameActivity} representing item details. On tablets, the
 * activity presents the list of items and item details side-by-side using two
 * vertical panes.
 * <p>
 * The activity makes heavy use of fragments. The list of items is a
 * {@link GameListFragment} and the item details (if present) is a
 * {@link GameDetailFragment}.
 * <p>
 * This activity also implements the required {@link GameListFragment.Callbacks}
 * interface to listen for item selections.
 */
public class GameActivity extends SherlockFragmentActivity implements GameTypeSelectionFragment.Callbacks,
		OutOfGameFragment.GameInit, InGameFragment.GameLogic, GameLogicFragment.UI,
		GameLostDialogFragment.GameLostListener, GameWonDialogFragment.GameWonListener {

	/**
	 * Whether or not the activity is in two-pane mode, i.e. running on a tablet
	 * device.
	 */
	private boolean mTwoPane;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_type_selection);

		if (findViewById(R.id.game_field_container) != null) {
			// The detail container view will be present only in the
			// large-screen layouts (res/values-large and
			// res/values-sw600dp). If this view is present, then the
			// activity should be in two-pane mode.
			mTwoPane = true;

			// In two-pane mode, list items should be given the
			// 'activated' state when touched.
			((GameTypeSelectionFragment) getSupportFragmentManager().findFragmentById(R.id.game_type_selection))
					.setActivateOnItemClick(true);
		}
	}

	/**
	 * Callback method from {@link GameTypeSelectionFragment.Callbacks} indicating that
	 * the item with the given ID was selected.
	 */
	@Override
	public void onItemSelected(String id) {
		// Pause a running game.
		FragmentManager fragmentManager = getSupportFragmentManager();
		GameLogicFragment gameLogic = (GameLogicFragment) fragmentManager
				.findFragmentByTag("gameLogic");

		if (gameLogic != null) {
			if (!gameLogic.isGamePaused()) {
				gameLogic.pauseGame();
				getSupportFragmentManager().popBackStack();
			}
		}

		if (mTwoPane) {
			// In two-pane mode, show the detail view in this activity by
			// adding or replacing the detail fragment using a
			// fragment transaction.
			Bundle arguments = new Bundle();
			arguments.putString(OutOfGameFragment.ARG_ITEM_ID, id);
			OutOfGameFragment fragment = new OutOfGameFragment();
			fragment.setArguments(arguments);
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.game_field_container, fragment).commit();
		} else {
			// In single-pane mode, simply start the detail activity
			// for the selected item ID.
			Intent detailIntent = new Intent(this, HandsetDeviceGameActivity.class);
			detailIntent.putExtra(OutOfGameFragment.ARG_ITEM_ID, id);
			startActivity(detailIntent);
		}
	}

	/**
	 * Callback method from {@link OutOfGameFragment.GameInit} which is used to
	 * start the InGameDetailFragment.
	 */
	public void startGame(String gameType) {
		Bundle arguments = new Bundle();
		arguments.putString(InGameFragment.GAME_TYPE, gameType);
		InGameFragment fragment = new InGameFragment();
		fragment.setArguments(arguments);
		getSupportFragmentManager().beginTransaction().replace(R.id.game_field_container, fragment)
				.addToBackStack(null).commit();
	}

	/**
	 * Callback method from {@link GameLogicFragment.UI} which is used to add a
	 * new row to the User Interface.
	 */
	@Override
	public void addUIRow(UIRow row) {
		InGameFragment fragment = (InGameFragment) getSupportFragmentManager().findFragmentById(
				R.id.game_field_container);
		fragment.addUIRow(row);
	}

	/**
	 * Callback method from {@link GameLogicFragment.UI} which is used to remove
	 * a single row at a specific position of the User Interface.
	 */
	@Override
	public void removeUIRow(int index) {
		InGameFragment fragment = (InGameFragment) getSupportFragmentManager().findFragmentById(
				R.id.game_field_container);
		fragment.removeUIRow(index);
	}

	/**
	 * Callback method from {@link GameLogicFragment.UI} which is used to remove
	 * all rows from the User Interface.
	 */
	@Override
	public void clearUIBoard() {
		InGameFragment fragment = (InGameFragment) getSupportFragmentManager().findFragmentById(
				R.id.game_field_container);
		fragment.clearUIBoard();
	}

	/**
	 * Callback method from {@link GameLogicFragment.UI} which is used to
	 * display the current level.
	 */
	@Override
	public void displayLevel(int level) {
		InGameFragment fragment = (InGameFragment) getSupportFragmentManager().findFragmentById(
				R.id.game_field_container);
		fragment.updateActionbarLevel(level);
	}

	/**
	 * Callback method from {@link GameLogicFragment.UI} which is used to
	 * display the current score.
	 */
	@Override
	public void displayScore(int score) {
		InGameFragment fragment = (InGameFragment) getSupportFragmentManager().findFragmentById(
				R.id.game_field_container);
		fragment.updateActionbarScore(score);
	}

	/**
	 * Callback method from {@link GameLogicFragment.UI} which is used to
	 * display the lines which still has to be solved at the current level.
	 */
	@Override
	public void displayLinesLeft(int linesLeft) {
		InGameFragment fragment = (InGameFragment) getSupportFragmentManager().findFragmentById(
				R.id.game_field_container);
		fragment.updateActionbarLinesLeft(linesLeft);
	}

	/**
	 * Callback method from {@link GameLogicFragment.UI} which is used to
	 * display the win message.
	 */
	@Override
	public void displayWinScreen(int score) {
		GameWonDialogFragment winScreen = GameWonDialogFragment.newInstance(score);
		winScreen.setCancelable(false);
		winScreen.show(getSupportFragmentManager(), "GameWonDialogFragment");
	}

	/**
	 * Callback method from {@link GameLogicFragment.UI} which is used to
	 * display the lost screen.
	 */
	@Override
	public void displayLostScreen(int level, int score) {
		GameLostDialogFragment lostScreen = GameLostDialogFragment.newInstance(level, score);
		lostScreen.setCancelable(false);
		lostScreen.show(getSupportFragmentManager(), "GameLostDialogFragment");
	}

	/**
	 * Callback method from {@link GameLostDialogFragment.GameLostListener}
	 * which is used to display the lost screen.
	 */
	@Override
	public void onGameLostDialogContinueClick() {
		getSupportFragmentManager().popBackStack();
	}

	/**
	 * Callback method from {@link GameWonDialogFragment.GameWonListener} which
	 * is used to display the win screen.
	 */
	@Override
	public void onGameWonDialogContinueClick() {
		getSupportFragmentManager().popBackStack();
	}

	/**
	 * Callback method from {@link InGameFragment.GameLogic} which is used to
	 * pass the new values row to the GameLogicFragment after the user
	 * interacted with this row.
	 */
	@Override
	public void updateRow(int index, UIRow row) {
		GameLogicFragment fragment = (GameLogicFragment) getSupportFragmentManager()
				.findFragmentByTag("gameLogic");
		fragment.updateRow(index, row);
	}

	/**
	 * Callback method from {@link InGameFragment.GameLogic} which is used to
	 * start or resume the GameLogicFragment.
	 */
	@Override
	public void startGameLogic(String gameType) {
		FragmentManager fragmentManager = getSupportFragmentManager();
		GameLogicFragment gameLogic = (GameLogicFragment) fragmentManager
				.findFragmentByTag("gameLogic");
		String gameLogicGameType = "null";
		boolean gameWonOrLost = false;

		if (gameLogic != null) {
			gameLogicGameType = gameLogic.getArguments().getString(gameLogic.GAME_TYPE);

			if (gameLogic.isGameWon() || gameLogic.isGameLost()) {
				gameWonOrLost = true;
			}
		}

		// If there hasn't been a game started yet, or the paused game type
		// isn't equal to the game type which is going to be started or the last
		// game is not resumed a new game gets started.
		if (gameLogic == null || gameWonOrLost || !gameLogicGameType.equals(gameType)) {
			Bundle arguments = new Bundle();
			arguments.putString(GameLogicFragment.GAME_TYPE, gameType);
			gameLogic = new GameLogicFragment();
			gameLogic.setArguments(arguments);
			getSupportFragmentManager().beginTransaction().add(gameLogic, "gameLogic").commit();
		} else {
			gameLogic.resumeGame();
		}
	}

	/**
	 * Callback method from {@link InGameFragment.GameLogic} which is used to
	 * pause the GameLogicFragment.
	 */
	@Override
	public void pauseGameLogic() {
		GameLogicFragment fragment = (GameLogicFragment) getSupportFragmentManager()
				.findFragmentByTag("gameLogic");
		fragment.pauseGame();
	}
}
