package com.leonmontealegre.pong;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Game extends ApplicationAdapter {

	public static final int TARGET_UPS = 30;
	private static final float ns = 1000000000.0f / TARGET_UPS;
	private long lastTime = System.nanoTime();
	private float delta = 0;

	private SpriteBatch batch;

	private Paddle player1;
	private Paddle player2;
	private Ball ball;

	private Texture background;

	private GameState currentState;

	private UIManager ui;

	private int player1Points = 0, player2Points = 0;

	@Override
	public void create() {
		Gdx.input.setInputProcessor(Input.instance);

		batch = new SpriteBatch();
		ui = new UIManager(this);

		background = new Texture("background.jpg");

		float height = Gdx.graphics.getHeight()/5;
		player1 = new Paddle(50, Gdx.graphics.getHeight()/2, height, false) {
			@Override
			public void update() {
				for (Vector2 pos : Input.touchPositions) {
					if (pos.x <= Gdx.graphics.getWidth() / 2) {
						this.prevPosition.y = position.y;
						this.position.y = pos.y - size.y / 2;
						break;
					}
				}
			}
		};
		player2 = new Paddle(Gdx.graphics.getWidth()-50, Gdx.graphics.getHeight()/2, height, true) {
			@Override
			public void update() {
				for (Vector2 pos : Input.touchPositions) {
					if (pos.x >= Gdx.graphics.getWidth() / 2) {
						this.prevPosition.y = position.y;
						this.position.y = pos.y - size.y / 2;
						break;
					}
				}
			}
		};

		reset();
	}

	private void reset() {
		currentState = GameState.Start;
		float size = Gdx.graphics.getWidth()/40;
		ball = new Ball(Gdx.graphics.getWidth()/2-size/2, Gdx.graphics.getHeight()/2-size/2, size);
	}

	public void update() {
		ui.update();
		if (currentState == GameState.Playing) {
			player1.update();
			player2.update();
			ball.update();

			if (ball.collidesWith(player1))
				ball.onCollide(player1);
			if (ball.collidesWith(player2))
				ball.onCollide(player2);

			if (ball.isOutOfBounds()) {
				if (ball.getSide() == -1) { // if on left side
					player2Points++;
					ui.updatePoints(false, player2Points);
				} else {
					player1Points++;
					ui.updatePoints(true, player1Points);
				}
				reset();
			}
		}
	}

	@Override
	public void render() {
		long now = System.nanoTime();
		delta += (now - lastTime) / ns;
		while (delta >= 1) {
			this.update();
			delta--;
		}

		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();

		batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		player1.render(batch);
		player2.render(batch);
		ball.render(batch);

		ui.renderUI(batch);
		if (currentState == GameState.Start)
			ui.renderStartUI(batch);

		batch.end();

		lastTime = now;
	}

	public void setState(GameState state) {
		currentState = state;
	}

	public GameState getCurrentState() {
		return currentState;
	}

	public enum GameState {
		Start, Playing, Paused
	}

}
