package com.redvex.byteme.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.redvex.byteme.GameActivity;
import com.redvex.byteme.HandsetDeviceGameActivity;
import com.redvex.byteme.R;
import com.redvex.byteme.ui.dummy.DummyContent;

/**
 * A fragment representing the Game field while the game isn't running.
 * This fragment is either contained in a {@link GameActivity} in two-pane mode
 * (on tablets) or a {@link HandsetDeviceGameActivity} on handsets.
 * <p>
 * Activities containing this fragment MUST implement the {@link GameInit}
 * interface.
 */
public class OutOfGameFragment extends SherlockFragment {
	/**
	 * The fragment argument representing the item ID that this fragment
	 * represents.
	 */
	public static final String ARG_ITEM_ID = "item_id";

	/**
	 * The dummy content this fragment is presenting.
	 */
	private DummyContent.DummyItem mItem;

	/**
	 * The fragment's current UI object. It is used to interact with the User
	 * Interface/ Game Field.
	 */
	private GameInit mGameInit = sGameInitDummy;

	public interface GameInit {
		public boolean isGameLostOrWon();

		public String getGameLogicGameType();

		public void startGame(String gameType);
	}

	/**
	 * A dummy implementation of the {@link GameInit} interface that does
	 * nothing. Used only when this fragment is not attached to an activity.
	 */
	private static GameInit sGameInitDummy = new GameInit() {
		@Override
		public boolean isGameLostOrWon() {
			return false;
		}

		@Override
		public String getGameLogicGameType() {
			return "null";
		}

		@Override
		public void startGame(String gameType) {
		}
	};

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public OutOfGameFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (getArguments().containsKey(ARG_ITEM_ID)) {
			// Load the dummy content specified by the fragment
			// arguments. In a real-world scenario, use a Loader
			// to load content from a content provider.
			mItem = DummyContent.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_out_of_game, container, false);

		// Show the dummy content as text in a TextView.
		if (mItem != null) {
			((TextView) rootView.findViewById(R.id.out_of_game)).setText(mItem.content);
		}

		// Register for the "Start Game" Buttons OnClick event
		Button startGame = (Button) rootView.findViewById(R.id.out_of_game_start_game);
		startGame.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				mGameInit.startGame(getArguments().getString(ARG_ITEM_ID));
			}
		});

		// Checking if the GAME_TYPE of the current game matches with the
		// currently displayed OutOfGameFragment ARM_ITEM_ID.
		if (mGameInit.getGameLogicGameType().equals(getArguments().getString(ARG_ITEM_ID))
				&& !mGameInit.isGameLostOrWon()) {
			startGame.setText(R.string.button_resume_game);
		} else {
			startGame.setText(R.string.button_start_game);
		}

		return rootView;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		// Activities containing this fragment must implement its UI.
		if (!(activity instanceof GameInit)) {
			throw new IllegalStateException("Activity must implement fragment's GameInit.");
		}

		mGameInit = (GameInit) activity;
	}

	@Override
	public void onDetach() {
		super.onDetach();

		// Reset the active UI interface to the dummy implementation.
		mGameInit = sGameInitDummy;
	}
}
