package com.ruqii.com.androidopengldemo.camera;

import android.content.Context;
import android.graphics.Point;
import android.graphics.SurfaceTexture;
import android.opengl.GLES20;
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
        this(context, null);
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
        mCameraDrawer.onSurfaceCreated(gl, config);
        if (mRunnable != null) {
            mRunnable.run();
            mRunnable = null;
        }
        mKikatCamera.open(mCameraId);
        mCameraDrawer.setCameraId(mCameraId);
        Point point = mKikatCamera.getPicSize();
        mCameraDrawer.setDataSize(point.x, point.y);
        mKikatCamera.setPreviewTexture(mCameraDrawer.getSurfaceTexture());
        mCameraDrawer.getSurfaceTexture().setOnFrameAvailableListener(new SurfaceTexture.OnFrameAvailableListener() {
            @Override
            public void onFrameAvailable(SurfaceTexture surfaceTexture) {
                requestRender();
            }
        });
        mKikatCamera.preview();
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        mCameraDrawer.setViewSize(width, height);
        GLES20.glViewport(0, 0, width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        mCameraDrawer.onDrawFrame(gl);
    }

    @Override
    public void onPause() {
        super.onPause();
        mKikatCamera.close();
    }

    /**
     * 切换摄像头
     */
    public void switchCamera() {
        mRunnable = new Runnable() {
            @Override
            public void run() {
                mKikatCamera.close();
                mCameraId = mCameraId == 1 ? 0 : 1;
            }
        };
        onPause();
        onResume();
    }
}
