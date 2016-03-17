package com.leonmontealegre.pong.android;

import android.content.Intent;
import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.leonmontealegre.pong.Game;

/**
 * Launches the LibGDX application for android.
 */
public class AndroidLauncher extends AndroidApplication {

	private Game game;

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		game = new Game();
		initialize(game);
	}

	@Override
	protected void onDestroy() {
		int winner = game.getWinner();
		if (winner > 0) { // A player actually won
			Intent i = new Intent(this, WinActivity.class);
			i.putExtra("winner", winner);
			startActivity(i);
		}
		super.onDestroy();
	}
}
