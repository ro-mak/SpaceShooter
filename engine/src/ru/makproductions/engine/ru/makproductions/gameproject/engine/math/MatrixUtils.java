package ru.makproductions.engine.ru.makproductions.gameproject.engine.math;

import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;

public final class MatrixUtils {

    private MatrixUtils(){

    }

    public static void calcTransitionMatrix(Matrix4 matrix4,Rect src, Rect dst){
        float scaleX = dst.getWidth() / src.getWidth();
        float scaleY = dst.getHeight() / src.getHeight();
        matrix4.idt().translate(dst.position.x,dst.position.y,0)
                .scale(scaleX,scaleY,1).translate(-src.position.x,-src.position.y,0);
    }

    public static void calcTransitionMatrix(Matrix3 matrix3,Rect src, Rect dst){
        float scaleX = dst.getWidth() / src.getWidth();
        float scaleY = dst.getHeight() / src.getHeight();
        matrix3.idt().translate(dst.position.x,dst.position.y)
                .scale(scaleX,scaleY).translate(-src.position.x,-src.position.y);
    }
}
