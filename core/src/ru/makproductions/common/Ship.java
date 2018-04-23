package ru.makproductions.common;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.makproductions.common.explosions.Explosion;
import ru.makproductions.common.explosions.ExplosionPool;
import ru.makproductions.engine.ru.makproductions.gameproject.engine.math.Rect;
import ru.makproductions.engine.sprites.Sprite;


public class Ship extends Sprite implements Collidable {

    protected ExplosionPool explosionPool;
    protected BulletPool bulletPool;
    protected TextureRegion bulletTexture;
    protected Sound bulletSound;
    protected float hp;

    protected TextureRegion fireTexture;
    protected Sprite fire;

    protected Rect worldBounds;
    protected Vector2 speed0 = new Vector2();
    protected boolean engineStarted;
    protected float acceleration;
    protected float height;
    protected Sound shotSound;

    protected boolean damaged;

    public Vector2 getSpeed0() {
        return this.speed0;
    }

    public Ship(){
    }

    public Ship(TextureRegion shipTexture, float vx, float vy, float height,
                Vector2 position, Sound shotSound) {
        super(shipTexture);
        this.shotSound = shotSound;
        this.height = height;
        this.position.set(position);
        speed0.set(vx, vy);
        setHeightProportion(height);
    }
    public Ship(TextureRegion shipTexture,int length, float vx, float vy, float height,
                Vector2 position, Sound shotSound) {
        super(shipTexture,length);
        this.shotSound = shotSound;
        this.height = height;
        this.position.set(position);
        speed0.set(vx, vy);
        setHeightProportion(height);
    }

    @Override
    public void resize(Rect worldBounds) {
        this.worldBounds = worldBounds;
    }

    protected float bulletHeight;
    protected final Vector2 bulletSpeed = new Vector2();
    protected int bulletDamage;
    protected final Vector2 bulletPosition = new Vector2();
    protected float reloadInterval;
    protected float reloadTimer;
    protected float bullet_margin = 0.1f;

    protected void shoot() {
        if(!isDestroyed()) {
            Bullet bullet = bulletPool.obtain();
            bulletPosition.x = position.x;
            bulletPosition.y = position.y + bullet_margin;
            bullet.setBullet(this, bulletTexture, bulletPosition, bulletSpeed,
                    bulletHeight, worldBounds, bulletDamage);
            if(shotSound.play(0.1f)==-1)throw new RuntimeException("shotSound.play() == -1");
        }
    }

    public void setEngineStarted(boolean engineStarted) {
        this.engineStarted = engineStarted;
    }

    public void draw(SpriteBatch batch) {
            if (engineStarted) startEngine(batch);
            super.draw(batch);
    }

    public void boom(){
        setEngineStarted(false);
        Explosion explosion = explosionPool.obtain();
        explosion.setExplosion(this.height, this.position);
        speed0.setZero();
    }

    protected void startEngine(SpriteBatch batch) {
    }

    protected float fullHP;
    public int getHp() {
        return (int)(((hp/fullHP)) * 100);
    }

    public void update(float delta) {
        if(isDestroyed())boom();
    }

    @Override
    public void solveCollision(Collidable collidable2) {
    }

    public void damage(int damage){
        hp-=damage;
    }
}
