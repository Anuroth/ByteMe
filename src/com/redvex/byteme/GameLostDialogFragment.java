package com.redvex.byteme;

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

/**
 * A DialogFragment which is shown when the game is lost.
 */
public class GameLostDialogFragment extends SherlockDialogFragment {
	/**
	 * Creating a new instance of GameLostDialogFragment with level and score as
	 * arguments.
	 */
	static GameLostDialogFragment newInstance(int level, int score) {
		GameLostDialogFragment lostScreen = new GameLostDialogFragment();

		Bundle args = new Bundle();
		args.putInt("level", level);
		args.putInt("score", score);
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

		TextView textView = (TextView) view.findViewById(R.id.game_lost_level);
		try {
			textView.setText(getString(R.string.actionbar_level)
					+ Integer.toString(getArguments().getInt("level")));
		} catch (NullPointerException e) {
			throw new NullPointerException(e.getMessage());
		}

		textView = (TextView) view.findViewById(R.id.game_lost_score);
		try {
			textView.setText(getString(R.string.actionbar_score)
					+ Integer.toString(getArguments().getInt("score")));
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
