package ru.makproductions.screens.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;


import ru.makproductions.common.Background;
import ru.makproductions.common.BulletPool;
import ru.makproductions.common.CollisionDetector;
import ru.makproductions.common.enemy.EnemyBulletPool;
import ru.makproductions.common.enemy.EnemyFabric;
import ru.makproductions.common.enemy.EnemyPool;
import ru.makproductions.common.explosions.ExplosionPool;
import ru.makproductions.common.player.PlayerShip;
import ru.makproductions.common.stars.StarsOfGame;
import ru.makproductions.engine.Base2DScreen;
import ru.makproductions.engine.Font;
import ru.makproductions.engine.Sprite2DTexture;
import ru.makproductions.engine.StrBuilder;
import ru.makproductions.engine.buttons.ActionListener;
import ru.makproductions.engine.ru.makproductions.gameproject.engine.math.Rect;
import ru.makproductions.engine.ru.makproductions.gameproject.engine.math.Rnd;
import ru.makproductions.screens.game.ui.RestartButton;


public class GameScreen extends Base2DScreen implements ActionListener {

    @Override
    public void actionPerformed(Object source) {
        if(source == restartButton){
            startNewGame();
        }
    }

    private enum State {GAME_ON,GAME_OVER}

    private State state;
    public GameScreen(Game game) {
        super(game);
    }

    private CollisionDetector collisionDetector;

    private EnemyPool enemyPool;
    private EnemyFabric enemyFabric;
    private TextureRegion[] enemyTextures = new TextureRegion[4];

    private final EnemyBulletPool enemyBulletPool = new EnemyBulletPool();

    private final BulletPool bulletPool = new BulletPool();
    private ExplosionPool explosionPool;

    private Music music_level1;
    private Sound soundExplosion;
    private Sound playerShotSound;
    private Sound enemyShotSound;

    private TextureAtlas gameAtlas;
    private Sprite2DTexture textureBackground;
    private Background background;
    private Font font;


    private StarsOfGame[] stars;
    private final int STARS_COUNT = 50;
    private final float STARS_HEIGHT = 0.05f;
    private PlayerShip playerShip;
    private final float SHIP_HEIGHT = 0.15f;

    private RestartButton restartButton;
    private final float BUTTON_RESTART_HEIGHT = 0.25f;

    @Override
    public void show() {
        super.show();
        font = new Font("fonts/font.fnt","fonts/font.png");
        font.setWorldSize(0.05f);
        soundExplosion = Gdx.audio.newSound(Gdx.files.internal("sounds/Explosion.wav"));
        playerShotSound = Gdx.audio.newSound(Gdx.files.internal("sounds/PlayerShot.wav"));
        enemyShotSound = Gdx.audio.newSound(Gdx.files.internal("sounds/EnemyShot.wav"));
        gameAtlas = new TextureAtlas("textures/gameAtlas.pack");
        textureBackground = new Sprite2DTexture("textures/Galaxies.png");
        background = new Background(new TextureRegion(textureBackground));
        stars = new StarsOfGame[STARS_COUNT];
        explosionPool = new ExplosionPool(gameAtlas, soundExplosion);
        enemyPool = new EnemyPool();
        enemyFabric = new EnemyFabric(enemyPool);
        playerShip =
                new PlayerShip
                        (gameAtlas, 0, 0, SHIP_HEIGHT, new Vector2(0f, 0f), bulletPool, explosionPool, playerShotSound);
        playerShip.setEngineStarted(true);
        for (int i = 0; i < stars.length; i++) {
            float starHeight = STARS_HEIGHT * Rnd.nextFloat(0.3f, 0.8f);
            float vx = Rnd.nextFloat(-0.0008f, 0.0008f);
            float vy = Rnd.nextFloat(-0.0001f, -0.02f);
            stars[i] = new StarsOfGame(gameAtlas.findRegion("Star"), playerShip, vx, vy, starHeight);
        }
        collisionDetector = new CollisionDetector();
        enemyTextures[0] = gameAtlas.findRegion("Enemy");
        enemyTextures[1] = gameAtlas.findRegion("EnemyEngineFire");
        enemyTextures[2] = gameAtlas.findRegion("EnemyBullet");
        enemyTextures[3] = gameAtlas.findRegion("EnemyShipDamaged");
        restartButton = new RestartButton(BUTTON_RESTART_HEIGHT,this);
        state = State.GAME_ON;
        isStagePrinted = true;
        playMusic();
    }


    private void startNewGame(){
        isStagePrinted = true;
        bulletPool.freeAllActiveObjects();
        enemyBulletPool.freeAllActiveObjects();
        enemyPool.freeAllActiveObjects();
        explosionPool.freeAllActiveObjects();
        collisionDetector.freeAllActiveObjects();
        state = State.GAME_ON;
        playerShip.onStartNewGame();
        enemyFabric.onStartNewGame();

    }

    @Override
    protected void resize(Rect worldBounds) {
        background.resize(worldBounds);
        playerShip.resize(worldBounds);
        for (int i = 0; i < stars.length; i++) {
            stars[i].resize(worldBounds);
        }
        enemyPool.resizeActiveSprites(worldBounds);
    }

    private void playMusic() {
        music_level1 = Gdx.audio.newMusic(Gdx.files.internal("music/WarriorDrums1.wav"));
        music_level1.setLooping(true);
        music_level1.play();
    }

    @Override
    public void render(float delta) {
        update(delta);
        if(state == State.GAME_ON)checkCollisions();
        deleteAllDestroyed();
        draw();
    }

    private float enemyCreateTimer;
    private float stagePrintTimer;
    private int previousStage = 1;
    private int stage;

    private void update(float delta) {
        background.update(delta);
        for (int i = 0; i < stars.length; i++) {
            stars[i].update(delta);
        }
        explosionPool.updateActiveSprites(delta);
        if(state == State.GAME_ON) {
            if ((enemyCreateTimer += delta) >= 3) {
                enemyCreateTimer = 0;
                enemyFabric.createEnemy(enemyTextures, enemyBulletPool, playerShip, explosionPool, enemyShotSound);
            }
            bulletPool.updateActiveSprites(delta);
            enemyBulletPool.updateActiveSprites(delta);
            enemyPool.updateActiveSprites(delta);
            playerShip.update(delta);
            if (playerShip.isDestroyed()) {
                state = State.GAME_OVER;
            }
        }
        updateStages(delta);
    }

    private void updateStages(float delta){
        if(playerShip.getFrags()>=10){
            playerShip.newStage();
            enemyFabric.newStage();
        }
        stage = enemyFabric.getStage();
        if(stage != previousStage){
            isStagePrinted = true;
            System.out.println("stage changed " + stage);
            previousStage = stage;
        }
        if(isStagePrinted){
            stagePrintTimer+=delta;
        }
        if(stagePrintTimer >=2){
            isStagePrinted = false;
            stagePrintTimer = 0;
        }

    }

    private void checkCollisions() {
        if(state == State.GAME_ON) {
            collisionDetector.detectCollisions();
            collisionDetector.addActiveObjects(playerShip);
            collisionDetector.addActiveObjects(enemyPool.getActiveObjects());
            collisionDetector.addActiveObjects(bulletPool.getActiveObjects());
            collisionDetector.addActiveObjects(enemyBulletPool.getActiveObjects());
        }
    }

    private void deleteAllDestroyed() {
        collisionDetector.removeDestroyed();
        enemyBulletPool.freeAllDestroyedActiveObjects();
        bulletPool.freeAllDestroyedActiveObjects();
        explosionPool.freeAllDestroyedActiveObjects();
        enemyPool.freeAllDestroyedActiveObjects();
    }

    private void draw() {
        Gdx.gl.glClearColor(0.7f, 0.7f, 0.7f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        background.draw(batch);
        for (int i = 0; i < stars.length; i++) {
            stars[i].draw(batch);
        }
        explosionPool.drawActiveObjects(batch);
        if(state == State.GAME_ON) {
            bulletPool.drawActiveObjects(batch);
            enemyBulletPool.drawActiveObjects(batch);
            enemyPool.drawActiveObjects(batch);
            playerShip.draw(batch);
        }else if(state == State.GAME_OVER){
            restartButton.draw(batch);
        }
        printInfo();
        batch.end();
    }

    private static final String FRAGS = "Frags: ";
    private static final String HP = "HP: ";
    private static final String STAGE = "STAGE ";
    private static final float HP_INFO_MARGIN_X = 0.25f;
    private StrBuilder strBuilder = new StrBuilder();

    private boolean isStagePrinted;
    private void printInfo(){
        if(isStagePrinted){
            font.draw(batch,strBuilder.clear().append(STAGE).append(stage),
                    worldBounds.position.x - 0.15f,worldBounds.position.y + 0.1f);
        }
        font.draw(batch,strBuilder.clear().append(FRAGS).append(playerShip.getFrags()),
                worldBounds.getLeft(),worldBounds.getTop());
        font.draw(batch,strBuilder.clear().append(HP).append(playerShip.getHp()),
                worldBounds.getRight() - HP_INFO_MARGIN_X,worldBounds.getTop());
    }

    @Override
    public void dispose() {
        gameAtlas.dispose();
        music_level1.dispose();
        soundExplosion.dispose();
        playerShotSound.dispose();
        enemyShotSound.dispose();
        bulletPool.dispose();
        enemyBulletPool.dispose();
        explosionPool.dispose();
        enemyPool.dispose();
        super.dispose();
    }

    @Override
    protected void touchDown(Vector2 touch, int pointer) {

        if(state == State.GAME_ON)playerShip.touchDown(touch, pointer);
        if(state == State.GAME_OVER) restartButton.touchDown(touch,pointer);
    }

    @Override
    protected void touchUp(Vector2 touch, int pointer)
    {
        if(state == State.GAME_ON)   playerShip.touchUp(touch, pointer);
        if(state == State.GAME_OVER) restartButton.touchUp(touch,pointer);
    }

    @Override
    protected void touchMove(Vector2 touch, int pointer)  {

        if(state == State.GAME_ON)playerShip.touchMove(touch, pointer);
    }

    @Override
    public boolean keyDown(int keycode) {
        if(state == State.GAME_ON)playerShip.keyDown(keycode);
        return super.keyDown(keycode);
    }

    @Override
    public boolean keyUp(int keycode) {
        if(state == State.GAME_ON)playerShip.keyUp(keycode);
        return super.keyUp(keycode);
    }

}
