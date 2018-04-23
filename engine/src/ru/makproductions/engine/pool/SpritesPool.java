package ru.makproductions.engine.pool;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;

import ru.makproductions.engine.sprites.Sprite;

public abstract class SpritesPool<T extends Sprite> {
    protected final ArrayList<T> activeObjects = new ArrayList<T>();
    protected final ArrayList<T> freeObjects = new ArrayList<T>();

    protected abstract T newObject();

    public T obtain() {
        T object;
        if (freeObjects.isEmpty()) {
            object = newObject();
        } else {
            object = freeObjects.remove(freeObjects.size() - 1);
        }
        activeObjects.add(object);
        debugLog();
        return object;
    }

    private void free(T object) {
        if (!activeObjects.remove(object))
            throw new RuntimeException("Attempt to delete a non-existing object = " + object);
        freeObjects.add(object);
        debugLog();
    }

    public void freeAllActiveObjects() {
                    final int numberOfActiveObjects = activeObjects.size();
                    for (int i = 0; i < numberOfActiveObjects; i++){
                        T object = activeObjects.get(i);
                        freeObjects.add(object);
                        object.cancelDestruction();
                    }
                      activeObjects.clear();

    }

    public void updateActiveSprites(float deltaTime) {
        final int count = activeObjects.size();
        for (int i = 0; i < count; i++) {
            Sprite sprite = activeObjects.get(i);
            if (sprite.isDestroyed())
                throw new RuntimeException("Update of a destroyed sprite " + sprite.getClass().getSimpleName() + sprite.isDestroyed());
            sprite.update(deltaTime);
        }
    }

    public ArrayList<T> getActiveObjects() {
        return activeObjects;
    }

    public void freeAllDestroyedActiveObjects() {
        for (int i = 0; i < activeObjects.size(); i++) {
            T sprite = activeObjects.get(i);
            if (sprite.isDestroyed()) {
                free(sprite);
                i--;
                sprite.cancelDestruction();
            }
        }
    }

    public void drawActiveObjects(SpriteBatch batch) {
        final int count = activeObjects.size();
        for (int i = 0; i < count; i++) {
            Sprite sprite = activeObjects.get(i);
            if (sprite.isDestroyed()) {
                throw new RuntimeException("Attempt to draw destroyed sprite " + sprite.getClass().getSimpleName());
            }
            sprite.draw(batch);
        }
    }

    protected void debugLog() {
    }

    public void dispose() {
        freeObjects.clear();
        activeObjects.clear();
    }
}
