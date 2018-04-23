package ru.makproductions.engine.buttons;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import ru.makproductions.engine.sprites.Sprite;

public class ScaledTouchUpButton extends Sprite {

    private int pointer;
    private final ActionListener actionListener;
    private float originalScale;
    protected float scaleSize = 0.1f;
    private boolean touched;

    public ScaledTouchUpButton(TextureRegion region, float height, ActionListener actionListener) {
        super(region);
        this.actionListener = actionListener;
        setHeightProportion(height);
        originalScale = getScale();
    }
        public ScaledTouchUpButton(float height,ActionListener actionListener) {
        super();
        this.actionListener = actionListener;
        originalScale = getScale();
    }

    protected void scaleUp(){
        setScale(getScale() + scaleSize);
    }
    protected void scaleDown(){
        setScale(getScale() - scaleSize);
    }

    protected void returnScale(){
        scale = 1f;
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer) {
        if(touched || !isMe(touch)) return  false;
        touched = true;
        this.pointer = pointer;
        scaleDown();
        return true;
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer) {
        if(!touched || this.pointer != pointer)return false;
        touched = false;
        if(isMe(touch)) {
            actionListener.actionPerformed(this);
        }
        returnScale();
        return true;
    }

    @Override
    public boolean touchMove(Vector2 touch, int pointer) {
        return false;
    }
}
