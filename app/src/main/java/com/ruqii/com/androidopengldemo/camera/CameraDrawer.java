package com.ruqii.com.androidopengldemo.camera;

import android.content.res.Resources;
import android.graphics.SurfaceTexture;
import android.opengl.GLES11Ext;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import com.ruqii.com.androidopengldemo.filter.AFilter;
import com.ruqii.com.androidopengldemo.filter.OesFilter;
import com.ruqii.com.androidopengldemo.utils.Gl2Utils;
import com.ruqii.com.androidopengldemo.utils.MatrixUtils;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * author:黄汝琪 on 2018/11/14.
 * email:huangruqi88@163.com
 */
public class CameraDrawer implements GLSurfaceView.Renderer {

    private float[] mMatrix = new float[16];
    private SurfaceTexture mSurfaceTexture;
    private int width, height;
    private int dataWidth, dataHeight;
    private AFilter mOesFilter;
    private int cameraId = 1;

    public CameraDrawer(Resources resources) {
        mOesFilter = new OesFilter(resources);
    }

    /**
     * 设置数据的宽高
     *
     * @param dataWidth
     * @param dataHeight
     */
    public void setDataSize(int dataWidth, int dataHeight) {
        this.dataWidth = dataWidth;
        this.dataHeight = dataHeight;
        calculateMatrix();
    }

    /**
     * 设置View的宽高
     *
     * @param width
     * @param height
     */
    public void setViewSize(int width, int height) {
        this.width = width;
        this.height = height;
        calculateMatrix();
    }

    public SurfaceTexture getSurfaceTexture() {
        return mSurfaceTexture;
    }

    /**
     * 设置前置或者后置摄像头
     *
     * @param id
     */
    public void setCameraId(int id) {
        this.cameraId = id;
        calculateMatrix();
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        int texture = createTextureID();
        mSurfaceTexture = new SurfaceTexture(texture);
        mOesFilter.create();
        mOesFilter.setTextureId(texture);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        setViewSize(width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        if (mSurfaceTexture != null) {
            mSurfaceTexture.updateTexImage();
        }
        mOesFilter.draw();
    }

    /**
     * 计算矩阵
     */
    private void calculateMatrix() {
        MatrixUtils.getShowMatrix(mMatrix, this.dataWidth, this.dataHeight, this.width, this.height);
        if (cameraId == 1) {
            MatrixUtils.flip(mMatrix, false, true);
            MatrixUtils.rotate(mMatrix, 90);
        } else {
            MatrixUtils.rotate(mMatrix, 270);
        }
        mOesFilter.setMatrix(mMatrix);
    }


    /**
     * 创建纹理D
     *
     * @return
     */
    private int createTextureID() {
        int[] texture = new int[1];
        GLES20.glGenTextures(1, texture, 0);
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, texture[0]);
        GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
                GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);
        GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
                GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
        GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
                GL10.GL_TEXTURE_WRAP_S, GL10.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
                GL10.GL_TEXTURE_WRAP_T, GL10.GL_CLAMP_TO_EDGE);
        return texture[0];
    }
}
