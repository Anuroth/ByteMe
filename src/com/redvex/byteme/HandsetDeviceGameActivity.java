package com.redvex.byteme;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.redvex.byteme.gamelogic.GameLogicFragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;

/**
 * An activity representing a single Game detail screen. This activity is only
 * used on handset devices. On tablet-size devices, item details are presented
 * side-by-side with a list of items in a {@link GameListActivity}.
 * <p>
 * This activity is mostly just a 'shell' activity containing nothing more than
 * a {@link GameDetailFragment}.
 */
public class HandsetDeviceGameActivity extends SherlockFragmentActivity implements
		OutOfGameFragment.GameInit, InGameFragment.GameLogic, GameLogicFragment.UI {

	public static final String IN_GAME = "false";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_field);

		// Show the Up button in the action bar.
		getActionBar().setDisplayHomeAsUpEnabled(true);

		// savedInstanceState is non-null when there is fragment state
		// saved from previous configurations of this activity
		// (e.g. when rotating the screen from portrait to landscape).
		// In this case, the fragment will automatically be re-added
		// to its container so we don't need to manually add it.
		// For more information, see the Fragments API guide at:
		//
		// http://developer.android.com/guide/components/fragments.html
		//
		if (getIntent().getBooleanExtra(IN_GAME, false)) {
			Bundle arguments = new Bundle();
			arguments.putString(InGameFragment.GAME_TYPE,
					getIntent().getStringExtra(InGameFragment.GAME_TYPE));
			InGameFragment fragment = new InGameFragment();
			fragment.setArguments(arguments);
			getSupportFragmentManager().beginTransaction().add(R.id.game_field_container, fragment)
					.commit();
		}
		if (savedInstanceState == null) {
			// Create the detail fragment and add it to the activity
			// using a fragment transaction.
			Bundle arguments = new Bundle();
			arguments.putString(OutOfGameFragment.ARG_ITEM_ID,
					getIntent().getStringExtra(OutOfGameFragment.ARG_ITEM_ID));
			OutOfGameFragment fragment = new OutOfGameFragment();
			fragment.setArguments(arguments);
			getSupportFragmentManager().beginTransaction().add(R.id.game_field_container, fragment)
					.commit();
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpTo(this, new Intent(this, GameActivity.class));
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * Callback method from {@link OutOfGameFragment.GameInit} which is used to
	 * start the InGameDetailFragment.
	 */
	public void startGame(String gameType) {
		Intent detailIntent = getIntent();
		detailIntent.putExtra(InGameFragment.GAME_TYPE, gameType);
		detailIntent.putExtra(HandsetDeviceGameActivity.IN_GAME, true);
		recreate();
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
		fragment.clearUIBoard();
	}

	/**
	 * Callback method from {@link GameLogicFragment.UI} which is used to
	 * display the current score.
	 */
	@Override
	public void displayScore(int score) {
		InGameFragment fragment = (InGameFragment) getSupportFragmentManager().findFragmentById(
				R.id.game_field_container);
		fragment.clearUIBoard();
	}

	/**
	 * Callback method from {@link GameLogicFragment.UI} which is used to
	 * display the lines which still has to be solved at the current level.
	 */
	@Override
	public void displayLinesLeft(int linesLeft) {
		InGameFragment fragment = (InGameFragment) getSupportFragmentManager().findFragmentById(
				R.id.game_field_container);
		fragment.clearUIBoard();
	}

	/**
	 * Callback method from {@link GameLogicFragment.UI} which is used to
	 * display the win message.
	 */
	@Override
	public void displayWinScreen(int score) {

	}

	/**
	 * Callback method from {@link GameLogicFragment.UI} which is used to
	 * display the lost screen.
	 */
	@Override
	public void displayLostScreen(int level, int score) {

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
		GameLogicFragment gameLogic = (GameLogicFragment) getSupportFragmentManager()
				.findFragmentByTag("gameLogic");
		String gameLogicGameType = "null";

		if (gameLogic != null) {
			gameLogicGameType = gameLogic.getArguments().getString(gameLogic.GAME_TYPE);
		}

		// If there hasn't been a game started yet, or the paused game type
		// isn't equal to the game type which is going to be started or the last
		// game is not resumed a new game gets started.
		if (gameLogic == null || !gameLogicGameType.equals(gameType)) {
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
