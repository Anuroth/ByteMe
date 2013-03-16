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
 * A DialogFragment which is shown when the game is won.
 */
public class GameWonDialogFragment extends SherlockDialogFragment {
	/**
	 * Creating a new instance of GameWonDialogFragment with totalScore,
	 * rowScore and levelScore as arguments.
	 */
	public static GameWonDialogFragment newInstance(int totalScore, int rowScore,
			ArrayList<Integer> levelScore) {
		GameWonDialogFragment wonScreen = new GameWonDialogFragment();

		Bundle args = new Bundle();
		args.putInt("totalScore", totalScore);
		args.putInt("rowScore", rowScore);
		args.putIntegerArrayList("levelScore", levelScore);
		wonScreen.setArguments(args);

		return wonScreen;
	}

	public interface GameWonListener {
		public void onGameWonDialogContinueClick();
	}

	GameWonListener mGameWonListener;

	/**
	 * Inflate the layout and display the reached score when the game was won.
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Inflate the layout to use as dialog or embedded fragment
		View view = inflater.inflate(R.layout.game_won, container, false);

		// Constructing a multi-line string to display the bonus score earned in
		// each level.
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
		TextView levelScore = (TextView) view.findViewById(R.id.game_won_level_score);
		try {
			levelScore.setText(textLevelScore);
		} catch (NullPointerException e) {
			throw new NullPointerException(e.getMessage());
		}

		TextView rowScore = (TextView) view.findViewById(R.id.game_won_row_score);
		try {
			rowScore.setText(getString(R.string.row_score)
					+ Integer.toString(getArguments().getInt("rowScore")));
		} catch (NullPointerException e) {
			throw new NullPointerException(e.getMessage());
		}

		TextView totalScore = (TextView) view.findViewById(R.id.game_won_total_score);
		try {
			totalScore.setText(getString(R.string.total_score)
					+ Integer.toString(getArguments().getInt("totalScore")));
		} catch (NullPointerException e) {
			throw new NullPointerException(e.getMessage());
		}

		Button continueButton = (Button) view.findViewById(R.id.game_won_continue);
		continueButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				mGameWonListener.onGameWonDialogContinueClick();
				GameWonDialogFragment.this.dismiss();
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
		// GameWonListener.
		if (!(activity instanceof GameWonListener)) {
			throw new IllegalStateException("Activity must implement fragment's callbacks.");
		}

		mGameWonListener = (GameWonListener) activity;
	}
}
