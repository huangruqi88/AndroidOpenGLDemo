package com.ruqii.com.androidopengldemo.image;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

import com.ruqii.com.androidopengldemo.image.filter.AFilter;

import java.io.IOException;

/**
 * author:黄汝琪 on 2018/11/8.
 * email:huangruqi88@163.com
 */
public class SGLiew extends GLSurfaceView {
    private SGLRender mRender;
    public SGLiew(Context context) {
        this(context,null);
    }

    public SGLiew(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {

        setEGLContextClientVersion(2);
        mRender = new SGLRender(this);
        setRenderer(mRender);
        //GLSurfaceView会显示设备的刷新频率不断地渲染，也可以配置按请求渲染，
        // GLSurfaceVIew.RENDERMODE_WHEN_DIRTY 在数据有变化的时候渲染
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);

        try {
            mRender.setImage(BitmapFactory.decodeStream(getResources().getAssets().open("texture/fengj.png")));
            //绘制成功后阻止重绘
            requestRender();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public SGLRender getRender() {
        return mRender;
    }

    public void setFilter(AFilter filter) {
        mRender.setFilter(filter);
    }
}
