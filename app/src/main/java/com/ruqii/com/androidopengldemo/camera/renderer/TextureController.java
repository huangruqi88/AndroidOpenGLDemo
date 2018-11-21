package com.ruqii.com.androidopengldemo.camera.renderer;

import android.content.Context;
import android.graphics.Point;
import android.graphics.SurfaceTexture;
import android.opengl.GLSurfaceView;
import android.view.Choreographer;
import android.view.View;
import android.view.ViewGroup;

import com.ruqii.com.androidopengldemo.camera.TextureFilter;
import com.ruqii.com.androidopengldemo.filter.AFilter;
import com.ruqii.com.androidopengldemo.filter.GroupFilter;
import com.ruqii.com.androidopengldemo.filter.NoFilter;
import com.ruqii.com.androidopengldemo.utils.MatrixUtils;

import java.nio.ByteBuffer;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLDisplay;
import javax.microedition.khronos.egl.EGLSurface;
import javax.microedition.khronos.opengles.GL10;

/**
 * @author huangruqi
 * @Description:
 * @Package: com.ruqii.com.androidopengldemo.camera.renderer
 * @data 2018/11/21 10:59
 * <p>
 * 借助GLSurfaceView创建的GL环境，做渲染工作。不将内容渲染到GLSurfaceView
 * 的Surface上，而是将内容绘制到外部提供的Surface、SurfaceHolder或者SurfaceTexture上。
 */
public class TextureController implements GLSurfaceView.Renderer {

    private Object surface;

    private GLView mGLView;
    private Context mContext;
    /**
     * 用户附加的Renderer或用来监听Renderer
     */
    private GLSurfaceView.Renderer mRenderer;
    /**
     * 特效处理的Filter
     */
    private TextureFilter mEffectFilter;
    /**
     * 中间特效
     */
    private GroupFilter mGroupFilter;
    /**
     * 用来渲染输出的Filter
     */
    private AFilter mShowFilter;
    /**
     * 数据的大小
     */
    private Point mDataSize;
    /**
     * 输出视图的大小
     */
    private Point mWindowSize;
    private AtomicBoolean isParamSet = new AtomicBoolean(false);
    /**
     * 用于绘制到屏幕上的变换矩阵
     */
    private float[] SM = new float[16];
    /**
     * 输出到屏幕上的方式
     */
    private int mShowType = MatrixUtils.TYPE_CENTERCROP;
    /**
     * AiyaFilter方向flag
     */
    private int mDirectionFlag = -1;
    /**
     * 用于绘制回调缩放的矩阵
     */
    private float[] callbackOM = new float[16];
    /**
     * 创建离屏buffer，用于最后导出数据
     */
    private int[] mExportFrame = new int[1];
    private int[] mExportTexture = new int[1];
    /**
     * 录像flag
     */
    private boolean isRecord = false;
    /**
     * 一次拍摄flag
     */
    private boolean isShoot = false;
    /**
     * 用于存储回调数据的buffer
     */
    private ByteBuffer[] outPutBuffer = new ByteBuffer[3];
    /**
     * 回调
     */
    private Choreographer.FrameCallback mFrameCallback;
    /**
     * 回调数据的宽高
     */
    private int frameCallbackWidth, frameCallbackHeight;
    /**
     * 回调数据使用的buffer索引
     */
    private int indexOutput = 0;

    public TextureController(Context context) {
        this.mContext = context;
        init();
    }

    private void init() {
        mGLView = new GLView(mContext);

        //避免GLView的attachToWindow和detachFromWindow崩溃
        ViewGroup v = new ViewGroup(mContext) {
            @Override
            protected void onLayout(boolean changed, int l, int t, int r, int b) {

            }
        };
        v.addView(mGLView);
        v.setVisibility(View.GONE);
        mEffectFilter = new TextureFilter(mContext.getResources());
        mShowFilter = new NoFilter(mContext.getResources());
        mGroupFilter = new GroupFilter(mContext.getResources());
        //设置默认的DateSize，DataSize由AiyaProvider根据数据源的图像宽高进行设置
        mDataSize = new Point(720, 1280);

        mWindowSize = new Point(720, 1280);
    }

    /**
     * 在Surface创建前，应该被调用
     *
     * @param width
     * @param height
     */
    public void setDataSize(int width, int height) {
        mDataSize.x = width;
        mDataSize.y = height;
    }

    public SurfaceTexture getTexture() {
        return mEffectFilter.getTexture();
    }

    public void setRenderer(GLSurfaceView.Renderer renderer) {
        mRenderer = renderer;
    }

    public void setImageDirection(int flag) {
        this.mDirectionFlag = flag;
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

    public void onPause() {
        mGLView.onPause();
    }

    public void onResume() {
        mGLView.onResume();
    }

    public void requestRender() {
        mGLView.requestRender();
    }

    public void destroy() {
        if (mRenderer != null) {
            mRenderer.onDestroy();
        }
        mGLView.surfaceDestroyed(null);
        mGLView.detachedFromWindow();
        mGLView.clear();
    }

    /**
     * 自定义GLSurfaceView，暴露出onAttachedToWindow
     * 方法及onDetachedFromWindow方法，取消holder的默认监听
     * onAttachedToWindow及onDetachedFromWindow必须保证view
     * 存在Parent
     */
    private class GLView extends GLSurfaceView {
        public GLView(Context context) {
            super(context);
            init();
        }

        private void init() {
            getHolder().addCallback(null);
            setEGLWindowSurfaceFactory(new EGLWindowSurfaceFactory() {
                @Override
                public EGLSurface createWindowSurface(EGL10 egl, EGLDisplay display, EGLConfig config, Object nativeWindow) {
                    return egl.eglCreateWindowSurface(display, config, surface, null);
                }

                @Override
                public void destroySurface(EGL10 egl, EGLDisplay display, EGLSurface surface) {
                    egl.eglDestroySurface(display, surface);
                }
            });
            setEGLContextClientVersion(2);
            setRenderer(TextureController.this);
            setRenderMode(RENDERMODE_WHEN_DIRTY);
            setPreserveEGLContextOnPause(true);
        }

        public void attachedToWindow() {
            super.onAttachedToWindow();
        }

        public void detachedFromWindow() {
            super.onDetachedFromWindow();
        }

        public void clear() {
            try {
                finalize();
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
    }
}
