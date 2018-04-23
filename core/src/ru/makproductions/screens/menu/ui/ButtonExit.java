package ru.makproductions.screens.menu.ui;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import ru.makproductions.engine.buttons.ActionListener;
import ru.makproductions.engine.buttons.ScaledTouchUpButton;
import ru.makproductions.engine.ru.makproductions.gameproject.engine.math.Rect;


public class ButtonExit extends ScaledTouchUpButton{
    private final float BUTTON_APPEARANCE_HEIGHT = 0.01f;
    public ButtonExit(TextureAtlas atlas, float height, ActionListener actionListener) {
        super(atlas.findRegion("ExitButton"), height, actionListener);
        setHeightProportion(height);
    }

    @Override
    public void resize(Rect worldBounds) {
        setBottom(worldBounds.getBottom()+BUTTON_APPEARANCE_HEIGHT);
        setRight(worldBounds.getRight());
    }

}
