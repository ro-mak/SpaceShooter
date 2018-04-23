package ru.makproductions.common.explosions;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import ru.makproductions.engine.pool.SpritesPool;


public class ExplosionPool extends SpritesPool<Explosion> {

    private final TextureRegion explosionRegion;
    private Sound soundExplosion;

    public ExplosionPool(TextureAtlas atlas,Sound soundExplosion){
        String regionName = "ExplosionParticle";
        explosionRegion = atlas.findRegion(regionName);
        if(explosionRegion == null) throw new RuntimeException("Region not found: "+regionName);
        this.soundExplosion = soundExplosion;
    }

    @Override
    protected void debugLog() {
//        System.out.println("ExplosionPool change active/free: "
//                + activeObjects.size() + " / "
//                + freeObjects.size());
    }

    @Override
    protected Explosion newObject() {
        return new Explosion(explosionRegion,5,1,5,soundExplosion);
    }
}
