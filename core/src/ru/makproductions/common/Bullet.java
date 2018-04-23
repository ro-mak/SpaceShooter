package ru.makproductions.common;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.makproductions.engine.ru.makproductions.gameproject.engine.math.Rect;
import ru.makproductions.engine.sprites.Sprite;

public class Bullet extends Sprite implements Collidable {

    protected Rect worldBounds;
    protected final Vector2 speed = new Vector2();
    protected int damage;
    protected Object owner;

    public Bullet() {
        regions = new TextureRegion[1];
    }

    public void setBullet(
            Object owner,
            TextureRegion region,
            Vector2 position0,
            Vector2 speed0,
            float height,
            Rect worldBounds,
            int damage
    ){
        this.owner = owner;
        regions[0] = region;
        position.set(position0);
        speed.set(speed0);
        setHeightProportion(height);
        this.worldBounds = worldBounds;
        this.damage = damage;
    }

    @Override
    public void update(float deltaTime) {
        position.mulAdd(speed,deltaTime);
        if(isOutSide(worldBounds))destroy();
    }

    public int getDamage() {
        return damage;
    }

    public Object getOwner() {
        return owner;
    }

    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);
    }

    @Override
    public void solveCollision(Collidable collidable2) {
        if(!(collidable2 instanceof Bullet)&& collidable2!=owner)destroy();
    }
}
