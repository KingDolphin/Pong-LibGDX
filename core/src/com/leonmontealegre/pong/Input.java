package com.leonmontealegre.pong;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;

public class Input implements InputProcessor {

    public static final Input instance = new Input();

    public static List<Vector2> touchPositions = new ArrayList<Vector2>();

    private Input() {}

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        touchPositions.add(new Vector2(screenX, Gdx.graphics.getHeight()-screenY));
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (pointer < touchPositions.size())
            touchPositions.remove(pointer);
        else
            touchPositions.remove(touchPositions.size()-1);
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        touchPositions.set(pointer % touchPositions.size(), new Vector2(screenX, Gdx.graphics.getHeight()-screenY));
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) { return false;}

    @Override
    public boolean scrolled(int amount) { return false; }

    @Override
    public boolean keyDown(int keycode) { return false; }

    @Override
    public boolean keyUp(int keycode) { return false; }

    @Override
    public boolean keyTyped(char character) { return false; }

}
