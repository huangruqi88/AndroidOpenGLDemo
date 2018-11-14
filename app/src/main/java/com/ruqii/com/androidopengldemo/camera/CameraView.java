package com.ruqii.com.androidopengldemo.camera;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * author:黄汝琪 on 2018/11/14.
 * email:huangruqi88@163.com
 */
public class CameraView extends GLSurfaceView implements GLSurfaceView.Renderer {

    private KikatCamera mKikatCamera;
    private CameraDrawer mCameraDrawer;
    private int mCameraId = 1;
    private Runnable mRunnable;


    public CameraView(Context context) {
        this(context,null);
    }

    public CameraView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setEGLContextClientVersion(2);
        setRenderer(this);
        setRenderMode(RENDERMODE_WHEN_DIRTY);
        mKikatCamera = new KikatCamera();
        mCameraDrawer = new CameraDrawer(getResources());
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
