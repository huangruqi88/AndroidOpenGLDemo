package com.ruqii.com.androidopengldemo.camera.renderer;

import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.opengl.GLSurfaceView;

import com.ruqii.com.androidopengldemo.camera.Renderer;

import java.io.IOException;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * author:黄汝琪 on 2018/11/21.
 * email:huangruqi88@163.com
 */
public class Camera1Renderer implements Renderer {
    private Camera mCamera;
    private int cameraId;
    private TextureController mController;

    public Camera1Renderer(int cameraId, TextureController controller) {
        this.cameraId = cameraId;
        this.mController = controller;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
        mCamera = Camera.open(cameraId);
        mController.setImageDirection(cameraId);
        Camera.Size size = mCamera.getParameters().getPreviewSize();
        mController.setDataSize(size.height, size.width);
        try {
            mCamera.setPreviewTexture(mController.getTexture());
            mController.getTexture().setOnFrameAvailableListener(new SurfaceTexture.OnFrameAvailableListener() {
                @Override
                public void onFrameAvailable(SurfaceTexture surfaceTexture) {
                    mController.requestRender();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        mCamera.startPreview();
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {

    }

    @Override
    public void onDrawFrame(GL10 gl) {

    }

    public void onDestroy() {
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }
}
