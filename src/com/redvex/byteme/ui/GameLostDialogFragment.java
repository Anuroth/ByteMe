package com.redvex.byteme.ui;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockDialogFragment;
import com.redvex.byteme.R;

/**
 * A DialogFragment which is shown when the game is lost.
 */
public class GameLostDialogFragment extends SherlockDialogFragment {
	/**
	 * Creating a new instance of GameLostDialogFragment with level, totalScore,
	 * rowScore and levelScore as arguments.
	 */
	public static GameLostDialogFragment newInstance(int level, int totalScore, int rowScore,
			ArrayList<Integer> levelScore) {
		GameLostDialogFragment lostScreen = new GameLostDialogFragment();

		Bundle args = new Bundle();
		args.putInt("level", level);
		args.putInt("totalScore", totalScore);
		args.putInt("rowScore", rowScore);
		args.putIntegerArrayList("levelScore", levelScore);
		lostScreen.setArguments(args);

		return lostScreen;
	}

	public interface GameLostListener {
		public void onGameLostDialogContinueClick();
	}

	GameLostListener mGameLostListener;

	/**
	 * Inflate the layout and display the reached level and score when the game
	 * was lost.
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Inflate the layout to use as dialog or embedded fragment
		View view = inflater.inflate(R.layout.game_lost, container, false);

		TextView lostLevel = (TextView) view.findViewById(R.id.game_lost_level);
		try {
			lostLevel.setText(getString(R.string.reached_level)
					+ Integer.toString(getArguments().getInt("level")));
		} catch (NullPointerException e) {
			throw new NullPointerException(e.getMessage());
		}

		TextView levelScore = (TextView) view.findViewById(R.id.game_lost_level_score);
		if (getArguments().getIntegerArrayList("levelScore").size() > 1) {
			// Constructing a multi-line string to display the bonus score
			// earned in each level.
			String textLevelScore = "";
			for (int i = 0; i != getArguments().getIntegerArrayList("levelScore").size(); i++) {
				if (getArguments().getIntegerArrayList("levelScore").get(i) != 0) {
					try {
						textLevelScore += getString(R.string.level_score)
								+ Integer.toString(i + 1)
								+ ": "
								+ Integer.toString(getArguments().getIntegerArrayList("levelScore")
										.get(i));
						if (getArguments().getIntegerArrayList("levelScore").get(i + 1) != 0) {
							textLevelScore += "\n";
						}
					} catch (NullPointerException e) {
						throw new NullPointerException(e.getMessage());
					}
				}
			}
			try {
				levelScore.setText(textLevelScore);
			} catch (NullPointerException e) {
				throw new NullPointerException(e.getMessage());
			}
		} else {
			// If no level has been finished and therefore no bonus score has
			// been earned the TextView of the bonus score is set to View.GONE.
			levelScore.setVisibility(View.GONE);
		}

		TextView rowScore = (TextView) view.findViewById(R.id.game_lost_row_score);
		try {
			rowScore.setText(getString(R.string.row_score)
					+ Integer.toString(getArguments().getInt("rowScore")));
			// If no level has been finished and therefore no bonus score has
			// been earned the TextView of the row score doesn't have to be
			// shown and is set to View.GONE.
			if (getArguments().getInt("rowScore") == getArguments().getInt("totalScore")) {
				rowScore.setVisibility(View.GONE);
			}
		} catch (NullPointerException e) {
			throw new NullPointerException(e.getMessage());
		}

		TextView totalScore = (TextView) view.findViewById(R.id.game_lost_total_score);
		try {
			totalScore.setText(getString(R.string.total_score)
					+ Integer.toString(getArguments().getInt("totalScore")));
		} catch (NullPointerException e) {
			throw new NullPointerException(e.getMessage());
		}

		Button continueButton = (Button) view.findViewById(R.id.game_lost_continue);
		continueButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				mGameLostListener.onGameLostDialogContinueClick();
				GameLostDialogFragment.this.dismiss();
			}
		});

		return view;
	}

	/**
	 * Remove the title of the dialog.
	 */
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		Dialog dialog = super.onCreateDialog(savedInstanceState);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		return dialog;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		// Activities containing this fragment must implement its
		// GameLostListener.
		if (!(activity instanceof GameLostListener)) {
			throw new IllegalStateException("Activity must implement fragment's callbacks.");
		}

		mGameLostListener = (GameLostListener) activity;
	}
}
