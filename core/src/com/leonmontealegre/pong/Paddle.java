package com.leonmontealegre.pong;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public abstract class Paddle {

    private Texture texture;

    protected Vector2 position;
    protected Vector2 prevPosition;
    protected Vector2 size;

    private boolean flip;

    public Paddle(float xPos, float yPos, float height, boolean flip) {
        texture = new Texture("paddle.png");
        size = new Vector2(height * texture.getWidth()/texture.getHeight(), height);
        position = new Vector2(xPos-size.x/2, yPos-size.y/2);
        prevPosition = new Vector2(position);
        this.flip = flip;
    }

    public abstract void update();

    public Vector2 getVelocity() {
        return new Vector2(position.x-prevPosition.x, position.y-prevPosition.y).scl(1f / Game.TARGET_UPS);
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x, position.y, size.x, size.y, 0, 0, texture.getWidth(), texture.getHeight(), flip, false);
    }

}
