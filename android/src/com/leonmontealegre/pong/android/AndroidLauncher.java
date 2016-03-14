package com.leonmontealegre.pong.android;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.leonmontealegre.pong.Game;

public class AndroidLauncher extends AndroidApplication {

	private Game game;

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game);

		game = new Game();
		View v = initializeForView(game);
		LinearLayout linearLayout = (LinearLayout)findViewById(R.id.game_layout);
		linearLayout.addView(v);
	}
}
