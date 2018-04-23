package ru.makproductions.common.stars;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.makproductions.engine.ru.makproductions.gameproject.engine.math.Rect;
import ru.makproductions.engine.ru.makproductions.gameproject.engine.math.Rnd;
import ru.makproductions.engine.sprites.Sprite;
import ru.makproductions.common.player.PlayerShip;

public class StarsOfGame extends Sprite {

    private Rect worldBounds;
    private final Vector2 speed = new Vector2();
    private PlayerShip ship;

    public StarsOfGame(TextureRegion region, PlayerShip ship, float vx, float vy, float height) {
        super(region);
        this.ship = ship;
        speed.set(vx, vy);
        setHeightProportion(height);
    }

    @Override
    public void resize(Rect worldBounds) {
        this.worldBounds = worldBounds;
        float posX = Rnd.nextFloat(worldBounds.getLeft(), worldBounds.getRight());
        float posY = Rnd.nextFloat(worldBounds.getBottom(), worldBounds.getTop());
        position.set(posX, posY);
    }

    @Override
    public void update(float deltaTime) {
        position.mulAdd(speed, deltaTime);
        if (ship.isMoving()) {
            position.x -= ship.getSpeed().x/1000;
        }
        checkAndHandleBounds();
    }

    protected void checkAndHandleBounds() {
        if (getRight() < worldBounds.getLeft()) setLeft(worldBounds.getRight());
        if (getLeft() > worldBounds.getRight()) setRight(worldBounds.getLeft());
        if (getTop() < worldBounds.getBottom()) setBottom(worldBounds.getTop());
        if (getBottom() > worldBounds.getTop()) setTop(worldBounds.getBottom());
    }

}
