package com.ruqii.com.androidopengldemo.image.filter;

import android.content.Context;
import android.graphics.Bitmap;
import android.opengl.GLSurfaceView;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * author:黄汝琪 on 2018/11/8.
 * email:huangruqi88@163.com
 */
public class AFilter implements GLSurfaceView.Renderer {
    private Bitmap mBitmap;

    public void setBitmap(Bitmap bitmap) {
        mBitmap = bitmap;
    }

    public AFilter(Context context, String vertex, String fragment) {

    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {

    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {

    }

    @Override
    public void onDrawFrame(GL10 gl) {

    }
}
