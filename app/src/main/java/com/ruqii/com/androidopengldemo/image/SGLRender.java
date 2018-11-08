package com.ruqii.com.androidopengldemo.image;

import android.graphics.Bitmap;
import android.opengl.GLSurfaceView;
import android.view.View;

import com.ruqii.com.androidopengldemo.image.filter.AFilter;
import com.ruqii.com.androidopengldemo.image.filter.ColorFilter;
import com.ruqii.com.androidopengldemo.image.filter.ContrastColorFilter;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * author:黄汝琪 on 2018/11/8.
 * email:huangruqi88@163.com
 */
public class SGLRender implements GLSurfaceView.Renderer {

    private AFilter mFilter;
    private Bitmap mBitmap;
    private int width, height;
    private boolean refreshFlag = false;
    private EGLConfig mConfig;

    public SGLRender(View mView) {
        mFilter = new ContrastColorFilter(mView.getContext(), ColorFilter.Filter.NONE);
    }

    private void setFilter(AFilter filter) {
        refreshFlag = true;
        mFilter = filter;
        //TODO:待写
    }

    public AFilter getFilter() {
        return mFilter;
    }
    public void setImage(Bitmap bitmap) {
        this.mBitmap = bitmap;
        mFilter.setBitmap(bitmap);
    }
    public void refresh() {
        refreshFlag = true;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        this.mConfig = config;
        mFilter.onSurfaceCreated(gl, config);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        this.width = width;
        this.height = height;
        mFilter.onSurfaceChanged(gl, width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        if (refreshFlag && width != 0 && height != 0) {
            mFilter.onSurfaceCreated(gl, mConfig);
            mFilter.onSurfaceChanged(gl, width, height);
            refreshFlag = false;
        }
        mFilter.onDrawFrame(gl);
    }
}
