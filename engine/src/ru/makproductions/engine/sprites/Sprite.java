package ru.makproductions.engine.sprites;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.makproductions.engine.ru.makproductions.gameproject.engine.math.Rect;
import ru.makproductions.engine.utils.Regions;

public class Sprite extends Rect {

    private boolean isDestroyed;
    protected float angle;
    protected float scale = 1f;
    protected TextureRegion[] regions;
    protected int currentFrame;

    public Sprite(){}

    public Sprite(TextureRegion region){
        if(region == null)
            throw new RuntimeException("Created Sprite with null region");
        regions = new TextureRegion[1];
        regions[0] = region;

    }
    public Sprite(TextureRegion region,int length){
        if(region == null)
            throw new RuntimeException("Created Sprite with null region");
        regions = new TextureRegion[length];
        regions[0] = region;

    }

    public Sprite(TextureRegion region, int rows, int cols, int frames) {
                regions = Regions.split(region, rows, cols, frames);
            }

    public void draw(SpriteBatch batch) {
        if(!isDestroyed()) {
            batch.draw(regions[currentFrame],
                    getLeft(), getBottom(),
                    halfWidth, halfHeight,
                    getWidth(), getHeight(),
                    scale, scale, angle);
        }
    }

    public void setWidthProportion(float width){
        setWidth(width);
        float aspectRatio = regions[currentFrame].getRegionWidth()
                / (float)regions[currentFrame].getRegionHeight();
        setHeight(width/aspectRatio);
    }

    public void setHeightProportion(float height){
        setHeight(height);
        float aspectRatio = regions[currentFrame].getRegionWidth()
                / (float)regions[currentFrame].getRegionHeight();
        setWidth(height * aspectRatio);
    }

    public void destroy(){
        isDestroyed = true;
    }

    public boolean isDestroyed(){
        return isDestroyed;
    }

    public void cancelDestruction(){
        isDestroyed = false;
    }

    public boolean touchDown(Vector2 touch, int pointer) {
        return false;
    }
    public boolean touchUp(Vector2 touch, int pointer) {
        return false;
    }
    public boolean touchMove(Vector2 touch, int pointer) {
        return false;
    }

    public void update(float deltaTime){

    }

    public void resize(Rect worldBounds){

    }

    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    @Override
    public String toString() {
        return "Sprite: " + " angle = " + angle + " scale = " + scale + " " + super.toString();
    }
}
