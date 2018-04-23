package ru.makproductions.common;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

import ru.makproductions.engine.ru.makproductions.gameproject.engine.math.Rect;
import ru.makproductions.engine.sprites.Sprite;

public class Background  extends Sprite{
    public Background(TextureRegion region) {
        super(region);
    }

    @Override
    public void resize(Rect worldBounds) {
        setHeightProportion(worldBounds.getHeight());
        position.set(worldBounds.position);
    }
}
