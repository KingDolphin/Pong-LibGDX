package com.leonmontealegre.pong.android;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.leonmontealegre.pong.Game;

/**
 * Launches the LibGDX application for android.
 */
public class AndroidLauncher extends AndroidApplication {

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initialize(new Game());
	}

}
