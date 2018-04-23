package ru.makproductions.common.explosions;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.makproductions.engine.sprites.Sprite;

public class Explosion extends Sprite {

    private final Sound soundExplosion;
    private float animateInterval = 0.017f;
    private float animateTimer;

    public Explosion(TextureRegion region, int rows, int cols, int frames,Sound soundExplosion) {
        super(region, rows, cols, frames);
        this.soundExplosion = soundExplosion;
    }

    public void setExplosion(float height, Vector2 position){
        currentFrame = 0;
        this.position.set(position);
        setHeightProportion(height);
        if((soundExplosion.play(0.1f)) == -1) throw new RuntimeException("soundExplosion.play() == -1");
    }

    @Override
    public void update(float deltaTime) {
        animateTimer += deltaTime;
        if(animateTimer >= animateInterval){
            animateTimer = 0f;
            if(++currentFrame == regions.length)destroy();
        }
    }
}
