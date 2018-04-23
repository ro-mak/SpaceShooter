package ru.makproductions.screens.menu.ui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.makproductions.engine.Font;
import ru.makproductions.engine.buttons.ActionListener;
import ru.makproductions.engine.buttons.ScaledTouchUpButton;
import ru.makproductions.engine.ru.makproductions.gameproject.engine.math.Rect;


    public class ButtonStartGameMENU extends ScaledTouchUpButton{

    private static Font font = new Font("fonts/font.fnt","fonts/font.png");

    private final float BUTTON_APPEARANCE_HEIGHT = 0.1f;

    public ButtonStartGameMENU(float height, ActionListener actionListener) {
        super(height, actionListener);
        regions = new TextureRegion[1];
        regions[0] = font.getRegion();
        setHeightProportion(height);
        font.setWorldSize(0.1f);
        scaleSize = 0.0003f;
        font.getData().setScale(0.001f);
    }

    @Override
    public void draw(SpriteBatch batch) {
        font.draw(batch,"Start! ", position.x - 0.2f ,position.y);
    }

    private Vector2 changePosition = new Vector2(-0.05f,0.01f);
    private Vector2 returnPosition = new Vector2(0.05f,-0.01f);
    @Override
    protected void scaleUp() {
       font.getData().setScale(font.getScaleX()+scaleSize,font.getScaleY()+scaleSize);
        position.mulAdd(changePosition,1f);
        //System.out.println("Scale up"+position);
    }

    @Override
    protected void scaleDown() {
        font.getData().setScale(font.getScaleX()-scaleSize,font.getScaleY()-scaleSize);
        position.mulAdd(returnPosition,1f);
      //  System.out.println("Scale down"+position);
    }

    @Override
    protected void returnScale() {
        font.getData().setScale(0.001f);
        position.mulAdd(changePosition,1f);
       // System.out.println("Return scale"+position);
    }

    @Override
    public void resize(Rect worldBounds) {
        setTop(worldBounds.getTop()-BUTTON_APPEARANCE_HEIGHT);
    }
}
