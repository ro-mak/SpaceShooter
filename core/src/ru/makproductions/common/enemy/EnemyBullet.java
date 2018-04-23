package ru.makproductions.common.enemy;


import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.makproductions.common.Collidable;
import ru.makproductions.engine.ru.makproductions.gameproject.engine.math.Rect;
import ru.makproductions.common.Bullet;
import ru.makproductions.common.player.PlayerShip;

public class EnemyBullet extends Bullet {
    private PlayerShip playerShip;

    public void setBullet(Object owner, TextureRegion region, Vector2 position0, Vector2 speed0, float height, Rect worldBounds, int damage,PlayerShip playerShip) {
        super.setBullet(owner, region, position0, speed0, height, worldBounds, damage);
        this.playerShip = playerShip;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if (playerShip.isMoving()) {
            position.x -= playerShip.getSpeed().x/1000;
        }
    }

    @Override
    public void solveCollision(Collidable collidable2) {
        if(collidable2 instanceof PlayerShip){
            destroy();
        }
    }
}
