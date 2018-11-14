package com.ruqii.com.androidopengldemo.camera;

import android.content.res.Resources;
import android.graphics.SurfaceTexture;
import android.opengl.GLSurfaceView;
import android.view.SurfaceView;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * author:黄汝琪 on 2018/11/14.
 * email:huangruqi88@163.com
 */
public class CameraDrawer implements GLSurfaceView.Renderer {

    private float[] mMatrix = new float[16];
    private SurfaceTexture mSurfaceTexture;
    private int width,height;
    private int dataWidth,dataHeight;

    public CameraDrawer(Resources resources) {

    }
//    private

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
