package com.ruqii.com.androidopengldemo.redener;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

/**
 * author:黄汝琪 on 2018/10/31.
 * email:huangruqi88@163.com
 */
public class FGLView extends GLSurfaceView {

    private FGLRedener mRedener;

    public FGLView(Context context) {
        super(context);
    }

    public FGLView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setEGLContextClientVersion(2);
        mRedener = new FGLRedener(this);
        setRenderer(mRedener);
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

    public void setShape(Class<? extends Shape> clazz) {
        mRedener.setShap(clazz);
    }
}
