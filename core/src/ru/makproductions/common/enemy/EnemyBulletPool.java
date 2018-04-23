package ru.makproductions.common.enemy;


import ru.makproductions.engine.pool.SpritesPool;

public class EnemyBulletPool extends SpritesPool<EnemyBullet> {
    @Override
    protected EnemyBullet newObject() {
        return new EnemyBullet();
    }
}
