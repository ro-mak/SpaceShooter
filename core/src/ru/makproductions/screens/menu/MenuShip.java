package ru.makproductions.screens.menu;


import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import ru.makproductions.engine.ru.makproductions.gameproject.engine.math.Rnd;
import ru.makproductions.engine.sprites.Sprite;
import ru.makproductions.common.Ship;

public class MenuShip extends Ship {

    public MenuShip(TextureAtlas atlas, float vx, float vy, float height, Vector2 position) {
        super(atlas.findRegion("PlayerShipFullVersion2"), vx, vy, height, position,null);
        acceleration = 0.0003f;
        fireTexture = atlas.findRegion("Fire");
    }

    @Override
    public void update(float delta){
        if(engineStarted) {
            speed0.y += acceleration;
            this.position.x += Rnd.nextFloat(-0.0001f, 0.0001f);
            this.position.y += speed0.y;
        }
    }
    @Override
    protected void startEngine(SpriteBatch batch){
        fire = new Sprite(fireTexture);
        fire.position.y = this.getBottom() - 0.1f;
        fire.position.x = this.position.x - 0.02f + Rnd.nextFloat(-0.01f,0.01f);
        fire.setHeightProportion(height);
        fire.draw(batch);
    }

}
