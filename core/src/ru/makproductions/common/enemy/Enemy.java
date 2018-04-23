package ru.makproductions.common.enemy;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import ru.makproductions.common.Bullet;
import ru.makproductions.common.Collidable;
import ru.makproductions.common.Ship;
import ru.makproductions.common.explosions.ExplosionPool;
import ru.makproductions.common.player.PlayerShip;
import ru.makproductions.engine.ru.makproductions.gameproject.engine.math.Rect;
import ru.makproductions.engine.ru.makproductions.gameproject.engine.math.Rnd;
import ru.makproductions.engine.sprites.Sprite;

public class Enemy extends Ship {

    private enum State {DESCEND, ACTION}
    private int stage;

    private PlayerShip playerShip;
    private EnemyBulletPool bulletPool;
    private State state;
    private final Vector2 descendSpeed = new Vector2(0f, -0.5f);

    public Enemy() {
    }

    public void setEnemy(TextureRegion[] enemyTexture, float vx, float vy, float height,
                         EnemyBulletPool bulletPool, Rect worldBounds,
                         PlayerShip playerShip, ExplosionPool explosionPool, Sound shotSound, int stage) {
        this.shotSound = shotSound;
        regions = enemyTexture;
        fireTexture = enemyTexture[1];
        bulletTexture = enemyTexture[2];

        this.playerShip = playerShip;
        this.worldBounds = worldBounds;
        this.height = height;
        setHeightProportion(height);
        speed0.set(vx, vy / (height * 10));
//        System.out.println("VY: "+vy);
//        System.out.println("Height: "+height);
//        System.out.println("speed0: "+speed0);
        float positionX = Rnd.nextFloat(worldBounds.getLeft() + halfWidth, worldBounds.getRight() - halfWidth);
        position.set(positionX, worldBounds.getTop());
        setEngineStarted(true);
        this.stage = stage;
        hp = (int) (stage*1000000 * (height*height*height));
      //  System.out.println(hp);
        bulletHeight = getHalfHeight();
        bullet_margin = -(getHalfHeight());
        bulletSpeed.set(0f, -0.05f / (height));
        bulletDamage = (int) (stage*100000 * (height*height*height));
        reloadInterval = height * 30;
        this.bulletPool = bulletPool;
        this.explosionPool = explosionPool;
        state = State.DESCEND;
        damaged = false;
        currentFrame = 0;
        fire = new Sprite(fireTexture);
    }

    @Override
    protected void shoot() {
            if (state == State.ACTION) {
                EnemyBullet bullet1 = bulletPool.obtain();
                bulletPosition.x = getLeft() + getHalfWidth() / 2;
                bulletPosition.y = position.y + bullet_margin;
                bullet1.setBullet(this, bulletTexture, bulletPosition, bulletSpeed,
                        bulletHeight, worldBounds, bulletDamage, playerShip);
                if (shotSound.play(0.3f) == -1) throw new RuntimeException("shotSound.play()==-1");
                EnemyBullet bullet2 = bulletPool.obtain();
                bulletPosition.x = getRight() - getHalfWidth() / 2;
                bulletPosition.y = position.y + bullet_margin;
                bullet2.setBullet(this, bulletTexture, bulletPosition, bulletSpeed,
                        bulletHeight, worldBounds, bulletDamage, playerShip);
                if (shotSound.play(0.3f) == -1) throw new RuntimeException("shotSound.play()==-1");
            }
    }

    @Override
    protected void startEngine(SpriteBatch batch) {
        fire.position.y = this.getTop() + getHalfWidth() / 4;
        fire.position.x = this.position.x + Rnd.nextFloat(-0.001f, 0.001f);
        fire.setHeightProportion(height);
        fire.draw(batch);
    }

    @Override
    public void solveCollision(Collidable collidable2) {
        if(!isDestroyed()) {
            if (collidable2 instanceof PlayerShip) {
                destroy();
            }
            if (collidable2 instanceof Bullet) {
                Bullet bullet = (Bullet) collidable2;
                if (bullet.getOwner() instanceof PlayerShip) {
                    damage(bullet.getDamage());
                    damaged = true;
                }
            }
        }
    }

    @Override
    public void update(float deltaTime) {
        if(!isDestroyed()) {
            if (this.getTop() <= worldBounds.getTop()) {
                state = State.ACTION;
            }
            if (state == State.ACTION) {
                if (hp <= 0) {
                    hp = 0;
                    playerShip.plusFrag();
                    destroy();
                    boom();
                    return;
                }
                if (this.getBottom() <= worldBounds.getBottom()) {
                    playerShip.damage(bulletDamage);
                    destroy();
                    boom();
                    return;
                }

                damageAnimation(deltaTime);

                position.mulAdd(speed0, deltaTime);
                if (playerShip.isMoving()) {
                    position.x -= playerShip.getSpeed().x / 1000;
                }

                reloadTimer += deltaTime;
                if (reloadTimer >= reloadInterval) {
                    reloadTimer = 0f;
                    shoot();
                }
            } else if (state == State.DESCEND) {
                position.mulAdd(descendSpeed, deltaTime);
                if (playerShip.isMoving()) {
                    position.x -= playerShip.getSpeed().x / 1000;
                }
            } else {
                throw new RuntimeException("Unknown state: " + state);
            }
        }
    }


    private final float DAMAGE_ANIMATION_INTERVAL = 0.1f;
    private float damageTimer;

    private void damageAnimation(float delta) {
        if (damaged) {
            damageTimer += delta;
            currentFrame = 3;
        }
        if(damageTimer >= DAMAGE_ANIMATION_INTERVAL){
            damaged = false;
            currentFrame = 0;
            damageTimer = 0;
        }
    }

    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);
        this.worldBounds = worldBounds;
    }
}
