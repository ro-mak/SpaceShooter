package ru.makproductions.common.enemy;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import ru.makproductions.common.explosions.ExplosionPool;
import ru.makproductions.engine.ru.makproductions.gameproject.engine.math.Rnd;
import ru.makproductions.common.player.PlayerShip;

public class EnemyFabric {
    private EnemyPool enemyPool;
    private Enemy enemy;
    private int stage = 1;

    public EnemyFabric(EnemyPool enemyPool) {
        this.enemyPool = enemyPool;
    }

    public int getStage() {
        return stage;
    }

    public void newStage(){
        stage++;
    }

    public void createEnemy(TextureRegion[] enemyTexture, EnemyBulletPool bulletPool,
                            PlayerShip playerShip, ExplosionPool explosionPool, Sound shotSound) {
        float vy = Rnd.nextFloat(-0.01f, -0.1f);
        float height = Rnd.nextFloat(0.05f, 0.1f);
        enemy = enemyPool.obtain();
        if (enemy == null) throw new RuntimeException("trying to invoke method setEnemy to null");
        enemy.setEnemy(enemyTexture, 0f, vy, height, bulletPool,
                enemyPool.getWorldBounds(),playerShip,explosionPool,shotSound,stage);
    }

    public void onStartNewGame(){
        stage = 1;
    }

}
