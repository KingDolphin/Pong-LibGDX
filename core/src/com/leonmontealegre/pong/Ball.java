package com.leonmontealegre.pong;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class Ball {

    private static final float START_SPEED = 24f;

    private Texture texture;

    private Vector2 position;
    private float size;

    private Vector2 velocity;

    public Ball(float xPos, float yPos, float size) {
        this.texture = new Texture("ball.png");
        this.position = new Vector2(xPos, yPos);
        this.velocity = new Vector2(MathUtils.randomSign() * START_SPEED,
                                    MathUtils.randomSign() * (MathUtils.random(START_SPEED/2)+START_SPEED/2));
        this.size = size;
    }

    public void update() {
        this.position.x += velocity.x;
        this.position.y += velocity.y;

        if (this.position.y <= 0) {
            this.velocity.y *= -1;
            this.position.y = 1;
        } else if (this.position.y+this.size >= Gdx.graphics.getHeight()) {
            this.velocity.y *= -1;
            this.position.y = Gdx.graphics.getHeight()-this.size-1;
        }
    }

    public void onCollide(Paddle paddle) {
        if (velocity.x > 0)
            this.position.x = paddle.position.x - this.size - 1;
        else
            this.position.x = paddle.position.x + paddle.size.x + 1;
        this.velocity.x *= -1;
        System.out.println(paddle.getVelocity().y);
        if ((paddle.getVelocity().y < 0 && this.velocity.y > 0) || (paddle.getVelocity().y > 0 && this.velocity.y < 0))
            this.velocity.y *= -1;
        this.velocity.y += paddle.getVelocity().y;
    }

    public boolean collidesWith(Paddle paddle) {
        return  this.position.x < paddle.position.x+paddle.size.x && this.position.x+size > paddle.position.x &&
                this.position.y < paddle.position.y+paddle.size.y && this.position.y+size > paddle.position.y;
    }

    public boolean isOutOfBounds() {
        return this.position.x <= 0 || this.position.x+this.size >= Gdx.graphics.getWidth();
    }

    public int getSide() {
        return this.position.x <= Gdx.graphics.getWidth()/2 ? -1 : 1;
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x, position.y, size, size);
    }

}
