package ru.makproductions.common.player;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;

import ru.makproductions.common.Bullet;
import ru.makproductions.common.BulletPool;
import ru.makproductions.common.Collidable;
import ru.makproductions.common.enemy.Enemy;
import ru.makproductions.common.explosions.ExplosionPool;
import ru.makproductions.engine.ru.makproductions.gameproject.engine.math.Rect;
import ru.makproductions.engine.ru.makproductions.gameproject.engine.math.Rnd;
import ru.makproductions.engine.sprites.Sprite;
import ru.makproductions.common.Ship;


public class PlayerShip extends Ship {
    private static final int NUMBER_OF_REGIONS = 4;
    private static final int INVALID_POINTER = -1;
    private int leftPointer = INVALID_POINTER;
    private int rightPointer = INVALID_POINTER;
    private int frags;
    private boolean isMoving;
    private boolean firstGame;

    public boolean isMoving() {
        return isMoving;
    }

    private final float BOTTOM_MARGIN = 0.1f;
    private Vector2 speed = new Vector2();

    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);
        setBottom(worldBounds.getBottom() + BOTTOM_MARGIN);
    }

    public PlayerShip(TextureAtlas atlas, float vx, float vy, float height,
                      Vector2 position, BulletPool bulletPool, ExplosionPool explosionPool, Sound shotSound) {
        super(atlas.findRegion("PlayerShipFullVersion2"), NUMBER_OF_REGIONS, vx, vy, height, position, shotSound);
        this.bulletPool = bulletPool;
        this.explosionPool = explosionPool;
        fireTexture = atlas.findRegion("Fire");
        bulletTexture = atlas.findRegion("PlayerBullet");
        regions[1] = atlas.findRegion("PlayerShipFullVersion2Damage1");
        regions[2] = atlas.findRegion("PlayerShipFullVersion2Damage2");
        regions[3] = atlas.findRegion("PlayerShipFullVersion2Damage3");
        fire = new Sprite(fireTexture);
        firstGame = true;
        onStartNewGame();
    }

    public void onStartNewGame(){
        stop();
        frags = 0;
        this.speed0.x = -0.5f;
        bulletSpeed.set(0f, 0.5f);
        bulletDamage = 500;
        bulletHeight = 0.05f;
        reloadInterval = 0.25f;
        if(!firstGame){
            position.x = worldBounds.position.x;
            setEngineStarted(true);
        }
        hp = 5000;
        fullHP = hp;
        cancelDestruction();
        firstGame = false;
    }

    public void newStage(){
        frags = 0;
    }

    @Override
    protected void startEngine(SpriteBatch batch) {
        fire.position.y = this.getBottom() - 0.05f;
        fire.position.x = this.position.x - 0.01f + Rnd.nextFloat(-0.001f, 0.001f);
        fire.setHeightProportion(height);
        fire.draw(batch);
        this.speed0.x = -0.5f;
    }

    public Vector2 getSpeed() {
        return speed;
    }

    private void moveLeft() {
        speed.set(speed0);
        isMoving = true;
    }

    private void moveRight() {
        speed.set(speed0).rotate(180);
        isMoving = true;
    }

    private void stop() {
        speed.setZero();
        isMoving = false;
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer) {
        if (touch.x < worldBounds.position.x) {
            if (leftPointer != INVALID_POINTER) return false;
            leftPointer = pointer;
            moveLeft();
        } else if (touch.x > worldBounds.position.x) {
            if (rightPointer != INVALID_POINTER) return false;
            rightPointer = pointer;
            moveRight();
        }
        return super.touchDown(touch, pointer);
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer) {
        if (pointer == leftPointer) {
            leftPointer = INVALID_POINTER;
            if (rightPointer != INVALID_POINTER) moveRight();
            else stop();
        } else if (pointer == rightPointer) {

            rightPointer = INVALID_POINTER;
            if (leftPointer != INVALID_POINTER) moveLeft();
            else stop();
        }
        return super.touchUp(touch, pointer);
    }

    @Override
    public boolean touchMove(Vector2 touch, int pointer) {
        return super.touchMove(touch, pointer);
    }

    private boolean pressedLeft;
    private boolean pressedRight;


    public void keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.A:
            case Input.Keys.LEFT:
                pressedLeft = true;
                moveLeft();
                break;
            case Input.Keys.D:
            case Input.Keys.RIGHT:
                pressedRight = true;
                moveRight();
                break;
        }
    }

    public void keyUp(int keycode) {
        switch (keycode) {
            case Input.Keys.A:
            case Input.Keys.LEFT:
                pressedLeft = false;
                if (pressedRight) moveRight();
                else stop();
                break;
            case Input.Keys.D:
            case Input.Keys.RIGHT:
                pressedRight = false;
                if (pressedLeft) moveLeft();
                else stop();
                break;
        }
    }

    private final float DAMAGE_ANIMATION_INTERVAL = 0.05f;
    private float damageTimer;

    public int getFrags() {
        return frags;
    }

    // The margin to remove a bug on real devices, when the ship gets stuck on the sides of the screen
    private final float BOUNDARY_MARGIN = 0.01f;

    @Override
    public void update(float delta) {

        if (hp <= 0 & !isDestroyed()){
            hp = 0;
            destroy();
            boom();
        }

        damageAnimation(delta);
        if (getLeft() < worldBounds.getLeft() - BOUNDARY_MARGIN) {
            setLeft(worldBounds.getLeft());
            stop();
        } else if (getRight() > worldBounds.getRight() + BOUNDARY_MARGIN) {
            setRight(worldBounds.getRight());
            stop();
        }
        position.mulAdd(speed, delta);
        reloadTimer += delta;
        if (reloadTimer >= reloadInterval) {
            reloadTimer = 0f;
            shoot();
        }
    }

    private void damageAnimation(float delta) {
        if (damaged) {
            damageTimer += delta;
        }
        if (damageTimer >= DAMAGE_ANIMATION_INTERVAL && damageTimer < DAMAGE_ANIMATION_INTERVAL * 2) {
            currentFrame = 1;
        } else if (damageTimer >= DAMAGE_ANIMATION_INTERVAL * 2 && damageTimer < DAMAGE_ANIMATION_INTERVAL * 3) {
            currentFrame = 2;
        } else if (damageTimer >= DAMAGE_ANIMATION_INTERVAL * 3 && damageTimer < DAMAGE_ANIMATION_INTERVAL * 4) {
            currentFrame = 3;
        } else if (damageTimer >= DAMAGE_ANIMATION_INTERVAL * 4) {
            currentFrame = 0;
            damageTimer = 0;
            damaged = false;
        }
//        System.out.println("Damage Timer: " + damageTimer);
//        System.out.println("Current Frame" + currentFrame);
    }

    @Override
    public void destroy() {
        super.destroy();
        stop();
    }

    @Override
    public void solveCollision(Collidable collidable2) {
        if (collidable2 instanceof Enemy) {
            destroy();
            boom();
        } else if (collidable2 instanceof Bullet) {
            Bullet bullet = (Bullet) collidable2;
            if (bullet.getOwner() != this) {
                damage(bullet.getDamage());
                damaged = true;
            }
        }
    }

    public void plusFrag(){
        frags += 1;
    }
}
