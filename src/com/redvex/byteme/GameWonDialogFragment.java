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
 * A DialogFragment which is shown when the game is won.
 */
public class GameWonDialogFragment extends SherlockDialogFragment {
	/**
	 * Creating a new instance of GameWonDialogFragment with score as arguments.
	 */
	static GameWonDialogFragment newInstance(int score) {
		GameWonDialogFragment wonScreen = new GameWonDialogFragment();

		Bundle args = new Bundle();
		args.putInt("score", score);
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

		TextView textView = (TextView) view.findViewById(R.id.game_won_score);
		try {
			textView.setText(getString(R.string.actionbar_score)
					+ Integer.toString(getArguments().getInt("score")));
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
