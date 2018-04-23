package ru.makproductions.common.enemy;

import ru.makproductions.engine.pool.SpritesPool;
import ru.makproductions.engine.ru.makproductions.gameproject.engine.math.Rect;

public class EnemyPool extends SpritesPool<Enemy> {
    private Rect worldBounds;

    public Rect getWorldBounds() {
        return worldBounds;
    }

    @Override
    protected Enemy newObject() {
        return new Enemy();
    }

    public void resizeActiveSprites(Rect worldBounds){
        this.worldBounds = worldBounds;
//        final int count = activeObjects.size();
//        for (int i = 0; i < count; i++) {
//            Enemy enemy = activeObjects.get(i);
//            if(enemy.isDestroyed())throw new RuntimeException("Resize of a destroyed sprite");
//            enemy.resize(worldBounds);
//        }
    }

    @Override
    protected void debugLog() {
//        System.out.println("EnemyPool change active/free: "
//                + activeObjects.size() + " / "
//                + freeObjects.size());
    }
}
