package com.ruqii.com.androidopengldemo.redener;

import android.opengl.GLES20;
import android.util.Log;
import android.view.View;

import java.lang.reflect.Constructor;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * author:黄汝琪 on 2018/10/31.
 * email:huangruqi88@163.com
 */
public class FGLRedener extends Shape{
    private Shape mShape;
    private Class<? extends Shape> clazz = Triangle.class;

    public FGLRedener(View view) {
        super(view);
    }

    public void setShap(Class<? extends Shape> shap) {
        this.clazz = shap;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(0.5f,0.5f,0.5f,1.0f);
        Log.d("huanruqi","onSurfaceCreated");

        try {
            Constructor constructor = clazz.getDeclaredConstructor(View.class);
            constructor.setAccessible(true);
            mShape = (Shape) constructor.newInstance(mView);
        } catch (Exception e) {
            e.printStackTrace();
            mShape = new Triangle(mView);
        }
        mShape.onSurfaceCreated(gl,config);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        Log.d("huanruqi","onSurfaceChanged");
        GLES20.glViewport(0,0,width,height);

        mShape.onSurfaceChanged(gl,width,height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        Log.d("huanruqi","onDrawFrame");
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        mShape.onDrawFrame(gl);
    }
}
