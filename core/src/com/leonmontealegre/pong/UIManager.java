package com.leonmontealegre.pong;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Vector2;

public class UIManager {

    private static final int margin = 50;

    /* Font for text */
    private BitmapFont font;

    /* 'TextViews' for each text item */
    private GlyphLayout tapToStartText;
    private GlyphLayout player1PointsText;
    private GlyphLayout player2PointsText;

    /* Texture for the 'touch here' icon */
    private Texture touchHereTexture;

    /* Colors for the 'touch here' icon */
    private Color touchedColor = new Color(Color.GREEN);
    private Color untouchedColor = new Color(Color.BLACK);

    /* Booleans for if the left and/or right side of the UI is pressed */
    private boolean leftPressed = false, rightPressed = false;

    /* The game */
    private Game game;

    /**
     * Creates a new UI manager with the given pong game.
     *
     * @param   game
     *              The game to change states on
     */
    public UIManager(Game game) {
        this.game = game;

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("font.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 60;
        parameter.color = new Color(0, 0, 0, 1);
        font = generator.generateFont(parameter);
        generator.dispose();

        tapToStartText = new GlyphLayout(font, "Tap to start");
        player1PointsText = new GlyphLayout(font, "0");
        player2PointsText = new GlyphLayout(font, "0");

        touchHereTexture = new Texture("touchHereIcon.png");
    }

    /**
     * Updates the UI to check for buttons clicks
     * and if the screen is being touched on the left
     * and/or right side of the screen.
     */
    public void update() {
        if (game.getCurrentState() == Game.GameState.Start) {
            leftPressed = rightPressed = false;
            for (Vector2 pos : Input.touchPositions) {
                if (pos.x < Gdx.graphics.getWidth() / 2)
                    leftPressed = true;
                else
                    rightPressed = true;
            }
            if (leftPressed && rightPressed)
                game.setState(Game.GameState.Playing);
        }
    }

    /**
     * Renders the general UI that is always on.
     *
     * @param   batch
     *              The SpriteBatch to render to.
     */
    public void renderUI(SpriteBatch batch) {
        final float p1PointsX = margin;
        final float p1PointsY = Gdx.graphics.getHeight() - margin;
        font.draw(batch, player1PointsText, p1PointsX, p1PointsY);

        final float p2PointsX = Gdx.graphics.getWidth() - player2PointsText.width - margin;
        final float p2PointsY = Gdx.graphics.getHeight() - margin;
        font.draw(batch, player2PointsText, p2PointsX, p2PointsY);
    }

    /**
     * Renders the UI that is only on at the start of each round.
     *
     * @param   batch
     *              The SpriteBatch to render to.
     */
    public void renderStartUI(SpriteBatch batch) {
        final float tapToStartX = Gdx.graphics.getWidth()/2 - tapToStartText.width/2;
        final float tapToStartY = Gdx.graphics.getHeight() - tapToStartText.height*2;
        font.draw(batch, tapToStartText, tapToStartX, tapToStartY);

        final float touchIconWidth = Gdx.graphics.getWidth() / 12;
        final float touchIconHeight = touchIconWidth * touchHereTexture.getHeight() / touchHereTexture.getWidth();
        final float touchIconY = tapToStartY - touchIconHeight/2 - margin;

        final float touchIcon1X = tapToStartX - touchIconWidth - margin;
        batch.setColor(leftPressed ? touchedColor : untouchedColor);
        batch.draw(touchHereTexture, touchIcon1X, touchIconY, touchIconWidth, touchIconHeight);

        final float touchIcon2X = tapToStartX + tapToStartText.width + margin;
        batch.setColor(rightPressed ? touchedColor : untouchedColor);
        batch.draw(touchHereTexture, touchIcon2X, touchIconY, touchIconWidth, touchIconHeight);

        batch.setColor(1, 1, 1, 1);
    }

    public void updatePoints(boolean player1, int points) {
        if (player1)
            player1PointsText.setText(font, ""+points);
        else
            player2PointsText.setText(font, ""+points);
    }

}
