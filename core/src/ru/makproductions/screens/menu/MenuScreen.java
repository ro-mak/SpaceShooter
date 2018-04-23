package ru.makproductions.screens.menu;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;

import ru.makproductions.engine.Base2DScreen;
import ru.makproductions.engine.buttons.ActionListener;
import ru.makproductions.engine.ru.makproductions.gameproject.engine.math.Rect;
import ru.makproductions.engine.ru.makproductions.gameproject.engine.math.Rnd;
import ru.makproductions.common.stars.StarsOfMenu;
import ru.makproductions.common.Background;
import ru.makproductions.screens.game.GameScreen;
import ru.makproductions.screens.menu.ui.ButtonExit;
import ru.makproductions.screens.menu.ui.ButtonStartGameMENU;

public class MenuScreen extends Base2DScreen implements ActionListener {

    private static final float STAR_HEIGHT = 0.03f;
    private static final int STAR_COUNT = 25;

    private TextureAtlas menuAtlas;

    private Background background;

    private StarsOfMenu[] stars;

    private MenuShip ship;
    private final float SHIP_HEIGHT = 0.35f;

    private ButtonStartGameMENU startButton;
    private final float BUTTON_START_HEIGHT = 0.2f;

    private ButtonExit exitButton;
    private final float BUTTON_EXIT_HEIGHT = 0.2f;

    private Music music_level1;

    public MenuScreen(Game game){
        super(game);
    }

    @Override
    public void show () {
        super.show();
        menuAtlas = new TextureAtlas("textures/menuAtlas.pack");

        background = new Background(menuAtlas.findRegion("StartOfStarFighter"));

        stars = new StarsOfMenu[STAR_COUNT];
        for(int i = 0; i < stars.length; i++) {
            float starHeight = STAR_HEIGHT * Rnd.nextFloat(0.5f,0.8f);
            float vx = Rnd.nextFloat(0,0.006f);
            float vy = Rnd.nextFloat(-0.0008f,0.0008f);
            stars[i] = new StarsOfMenu(menuAtlas.findRegion("Star")
                    , vx, vy, starHeight);
        }
        startButton = new ButtonStartGameMENU(BUTTON_START_HEIGHT,this);
        exitButton = new ButtonExit(menuAtlas,BUTTON_EXIT_HEIGHT,this);
        ship = new MenuShip(menuAtlas,0,0,SHIP_HEIGHT,new Vector2(0f,-0.2f));
        playMusic();
    }

    @Override
    protected void resize(Rect worldBounds) {
        background.resize(worldBounds);
        for (int i = 0; i < stars.length; i++) {
            stars[i].resize(worldBounds);
        }
        ship.resize(worldBounds);
        startButton.resize(worldBounds);
        exitButton.resize(worldBounds);
    }

    @Override
    public void render (float delta) {
        update(delta);
        draw();
        if (ship != null&&ship.position.y > 1f){
            game.setScreen(new GameScreen(game));
        }
    }

    private void playMusic(){
        music_level1 = Gdx.audio.newMusic(Gdx.files.internal("music/SpacePortMusicOGG.ogg"));
        music_level1.setLooping(true);
        music_level1.play();
    }

    private void update(float deltaTime){
        for (int i = 0; i < stars.length; i++) {
            stars[i].update(deltaTime);
        }
        ship.update(deltaTime);
    }

    private void draw(){
        Gdx.gl.glClearColor(0.7f, 0.7f, 0.7f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        background.draw(batch);
        for (int i = 0; i < stars.length; i++) {
            stars[i].draw(batch);
        }
        startButton.draw(batch);
        exitButton.draw(batch);
        ship.draw(batch);
        batch.end();
    }

    @Override
    public void dispose () {
        menuAtlas.dispose();
        music_level1.dispose();
        super.dispose();
    }

    @Override
    public boolean keyDown(int keycode) {
        return super.keyDown(keycode);
    }

    @Override
    public boolean keyUp(int keycode) {
        return super.keyUp(keycode);
    }

    @Override
    public boolean keyTyped(char character) {
        return super.keyTyped(character);
    }
    @Override
    public void actionPerformed(Object source) {
        if(source == startButton){
            startButton.setTop(20f);
            ship.setEngineStarted(true);
        }else if(source == exitButton){
            Gdx.app.exit();
        }else{
            throw new RuntimeException("Unknown source =" + source);
        }
    }

    @Override
    protected void touchUp(Vector2 touch, int pointer) {
        super.touchUp(touch, pointer);
        startButton.touchUp(touch,pointer);
        exitButton.touchUp(touch,pointer);

    }
    @Override
    protected void touchDown(Vector2 touch, int pointer) {
        super.touchDown(touch,pointer);
        startButton.touchDown(touch,pointer);
        exitButton.touchDown(touch,pointer);
    }

    @Override
    protected void touchMove(Vector2 touch, int pointer) {
        super.touchMove(touch, pointer);
        startButton.touchMove(touch,pointer);
        exitButton.touchMove(touch,pointer);
    }
}
