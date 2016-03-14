package com.leonmontealegre.pong;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Vector2;

public class Game extends ApplicationAdapter {

	public static final int TARGET_UPS = 30;
	private static final float ns = 1000000000.0f / TARGET_UPS;
	private long lastTime = System.nanoTime(), timer = System.currentTimeMillis();
	private float delta = 0;

	private SpriteBatch batch;
	private BitmapFont font;
	private GlyphLayout tapToStartLayout;
	private GlyphLayout player1PointsLayout;
	private GlyphLayout player2PointsLayout;

	private Paddle player1;
	private Paddle player2;
	private Ball ball;

	private Texture background;
	private Texture pauseTexture;

	private Color touchedColor = new Color(Color.GREEN);
	private Color untouchedColor = new Color(Color.BLACK);
	private Texture touchHereTexture;

	private GameState currentState;

	private int player1Points = 0, player2Points = 0;

    private boolean leftPressed = false, rightPressed = false;

	@Override
	public void create () {
		Gdx.input.setInputProcessor(Input.instance);

		batch = new SpriteBatch();
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("font.ttf"));
		FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
		parameter.size = 60;
		parameter.color = new Color(0, 0, 0, 1);
		font = generator.generateFont(parameter);
		generator.dispose();

		tapToStartLayout = new GlyphLayout(font, "Tap to start");
		player1PointsLayout = new GlyphLayout(font, "0");
		player2PointsLayout = new GlyphLayout(font, "0");

		background = new Texture("background.jpg");
		pauseTexture = new Texture("pauseIcon.png");
		touchHereTexture = new Texture("touchHereIcon.png");

		float height = Gdx.graphics.getHeight()/5;
		player1 = new Paddle(50, Gdx.graphics.getHeight()/2, height, false) {
			@Override
			public void update() {
				if (Input.touchPositions.size() > 0) {
					for (Vector2 pos : Input.touchPositions) {
						if (pos.x <= Gdx.graphics.getWidth() / 2) {
							this.prevPosition.y = position.y;
							this.position.y = pos.y-size.y/2;
							break;
						}
					}
				}
			}
		};
		player2 = new Paddle(Gdx.graphics.getWidth()-50, Gdx.graphics.getHeight()/2, height, true) {
			@Override
			public void update() {
				if (Input.touchPositions.size() > 0) {
					for (Vector2 pos : Input.touchPositions) {
						if (pos.x >= Gdx.graphics.getWidth() / 2) {
							this.prevPosition.y = position.y;
							this.position.y = pos.y-size.y/2;
							break;
						}
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
		if (currentState == GameState.Start) {
			leftPressed = rightPressed = false;
			for (Vector2 pos : Input.touchPositions) {
				if (pos.x < Gdx.graphics.getWidth()/2)
					leftPressed = true;
				else
					rightPressed = true;
			}
			if (leftPressed && rightPressed)
				currentState = GameState.Playing;
		} else if (currentState == GameState.Playing) {
			player1.update();
			player2.update();
			ball.update();

			if (ball.collidesWith(player1))
				ball.onCollide(player1);
			if (ball.collidesWith(player2))
				ball.onCollide(player2);

			if (ball.isOutOfBounds()) {
				if (ball.getSide() == -1) {
					player2Points++;
					player2PointsLayout.setText(font, ""+player2Points);
				} else {
					player1Points++;
					player1PointsLayout.setText(font, ""+player1Points);
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

		final int margin = 50;
		final float p1PointsX = margin;
		final float p1PointsY = Gdx.graphics.getHeight() - margin;
		font.draw(batch, player1PointsLayout, p1PointsX, p1PointsY);

		final float p2PointsX = Gdx.graphics.getWidth() - player2PointsLayout.width - margin;
		final float p2PointsY = Gdx.graphics.getHeight() - margin;
		font.draw(batch, player2PointsLayout, p2PointsX, p2PointsY);

		if (currentState == GameState.Start) {
			final float tapToStartX = Gdx.graphics.getWidth()/2 - tapToStartLayout.width/2;
			final float tapToStartY = Gdx.graphics.getHeight() - tapToStartLayout.height*2;
			font.draw(batch, tapToStartLayout, tapToStartX, tapToStartY);

			final float touchWidth = Gdx.graphics.getWidth() / 12;
			final float touchHeight = touchWidth * touchHereTexture.getHeight() / touchHereTexture.getWidth();

			batch.setColor(leftPressed ? touchedColor : untouchedColor);
			batch.draw(touchHereTexture, (p1PointsX + tapToStartX) / 2 - touchWidth/2, (p1PointsY + tapToStartY) / 2 - touchHeight/2 - margin, touchWidth, touchHeight);
			batch.setColor(rightPressed ? touchedColor : untouchedColor);
			batch.draw(touchHereTexture, (p2PointsX + tapToStartX) / 2 + touchWidth/2, (p2PointsY + tapToStartY) / 2 - touchHeight/2 - margin, touchWidth, touchHeight);
			batch.setColor(1, 1, 1, 1);
		}

		final float pauseWidth = Gdx.graphics.getWidth() / 16;
		final float pauseHeight = pauseWidth * pauseTexture.getHeight() / pauseTexture.getWidth();
		batch.draw(pauseTexture, Gdx.graphics.getWidth() - pauseWidth - margin, margin, pauseWidth, pauseHeight);

		batch.end();

		if (System.currentTimeMillis() - timer > 1000)
			timer += 1000;
		lastTime = now;
	}

	public enum GameState {
		Start, Playing, Paused
	}

}
