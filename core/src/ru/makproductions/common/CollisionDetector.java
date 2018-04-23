package ru.makproductions.common;

import java.util.ArrayList;

import ru.makproductions.engine.sprites.Sprite;

public class CollisionDetector <T extends Sprite & Collidable> {
    private ArrayList<T> activeObjects;
    public CollisionDetector() {
        activeObjects = new ArrayList<T>();
    }

    public void freeAllActiveObjects(){
        activeObjects.clear();
    }

    public void addActiveObjects(ArrayList<T> sprites){
        for (int i = 0; i < sprites.size(); i++) {
            T sprite = sprites.get(i);
            if(!sprite.isDestroyed()
                    &&!this.activeObjects.contains(sprite)) this.activeObjects.add(sprite);
        }
    }
    public void addActiveObjects(T sprite){
        if(!sprite.isDestroyed()&&!this.activeObjects.contains(sprite)) this.activeObjects.add(sprite);
    }

    public void removeDestroyed(){
        for (int i = 0; i < activeObjects.size(); i++) {
            if (activeObjects.get(i).isDestroyed()){
                activeObjects.remove(activeObjects.get(i));
            }
        }
    }

    public void detectCollisions(){
        for (int i = 0; i < activeObjects.size(); i++) {
            for (int j = 0; j < activeObjects.size(); j++) {
                if(!activeObjects.get(i).equals(activeObjects.get(j))){
                    if(!activeObjects.get(i).isOutSide(activeObjects.get(j))){
                        activeObjects.get(i).solveCollision(activeObjects.get(j));
                    }
                }
            }
        }
    }
}
