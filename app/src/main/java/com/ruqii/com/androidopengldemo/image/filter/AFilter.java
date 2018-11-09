package com.ruqii.com.androidopengldemo.image.filter;

import android.content.Context;
import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.opengl.Matrix;

import com.ruqii.com.androidopengldemo.utils.ShaderUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * author:黄汝琪 on 2018/11/8.
 * email:huangruqi88@163.com
 */
public abstract class AFilter implements GLSurfaceView.Renderer {
    private Context mContext;
    private int mProgram;
    /**
     * 顶点坐标
     */
    private int glHPosition;
    /**
     * 纹理贴图
     */
    private int glHTexture;
    /**
     * 纹理坐标（即图片所在的android的屏幕坐标系）
     */
    private int glCoordinate;
    private int glHMatrix;
    /**
     * 半张对比图的顶点坐标
     */
    private int hIsHalf;
    /**
     * 纹理的宽高比
     */
    private int glHUxy;
    private Bitmap mBitmap;

    private FloatBuffer bPos;
    private FloatBuffer bCoord;
    private int textureId;
    /**
     * 是否将图片处理一半与原图做对比
     */
    private boolean isHalf;
    /**
     * 屏幕的宽高比
     */
    private float mScreenRatio;
    private String vertex;
    private String fragment;
    private float[] mViewMartix = new float[16];
    private float[] mProjectMartix = new float[16];
    private float[] mMVPMartix = new float[16];

    private final float[] sPos = {
            -1.0f, 1.0f,
            -1.0f, -1.0f,
            1.0f, 1.0f,
            1.0f, -1.0f,
    };
    private final float[] sCoord = {
            0.0f, 0.0f,
            0.0f, 1.0f,
            1.0f, 0.0f,
            1.0f, 1.0f,
    };


    public AFilter(Context context, String vertex, String fragment) {
        this.mContext = context;
        this.vertex = vertex;
        this.fragment = fragment;
        ByteBuffer posBuffer = ByteBuffer.allocateDirect(sPos.length * 4);
        posBuffer.order(ByteOrder.nativeOrder());

        bPos = posBuffer.asFloatBuffer();
        bPos.put(sPos);
        bPos.position(0);

        ByteBuffer coordBuffer = ByteBuffer.allocateDirect(sCoord.length * 4);
        coordBuffer.order(ByteOrder.nativeOrder());

        bCoord = coordBuffer.asFloatBuffer();
        bCoord.put(sCoord);
        bCoord.position(0);

    }

    public void setHalf(boolean half) {
        isHalf = half;
    }

    public void setImageBuffer(int[] buffer, int width, int height) {
        mBitmap = Bitmap.createBitmap(buffer, width, height, Bitmap.Config.RGB_565);
    }

    public void setBitmap(Bitmap bitmap) {
        mBitmap = bitmap;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        //设置纹理贴图
        GLES20.glEnable(GLES20.GL_TEXTURE_2D);
        mProgram = ShaderUtils.createProgram(mContext.getResources(), vertex, fragment);
        glHPosition = GLES20.glGetAttribLocation(mProgram, "vPosition");
        glCoordinate = GLES20.glGetAttribLocation(mProgram, "vCoordinate");
        glHTexture = GLES20.glGetUniformLocation(mProgram, "vTexture");
        glHMatrix = GLES20.glGetUniformLocation(mProgram, "vMatrix");
        hIsHalf = GLES20.glGetUniformLocation(mProgram, "vMatrix");
        glHUxy = GLES20.glGetUniformLocation(mProgram, "uXY");
        onDrawCreate(mProgram);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);

        int w = mBitmap.getWidth();
        int h = mBitmap.getHeight();
        float bitmapRatio = (float) w / h;
        float screenRatio = (float) width / height;
        mScreenRatio = screenRatio;
        if (width > height) {
            // 竖屏状态
            if (bitmapRatio > screenRatio) {
                //设置正交投影    横向的图片  screenRatio * bitmapRatio 为了让图片以图片的以宽高比填充屏幕
                Matrix.orthoM(mProjectMartix, 0, -screenRatio * bitmapRatio, screenRatio * bitmapRatio, -1, 1, 3, 5);
            } else {
                //设置正交投影    竖向的图片
                Matrix.orthoM(mProjectMartix, 0, -screenRatio / bitmapRatio, screenRatio / bitmapRatio, -1, 1, 3, 5);
            }
        } else {
            // 横屏状态
            if (bitmapRatio > screenRatio) {
                Matrix.orthoM(mProjectMartix, 0, -1, 1, -1 / screenRatio * bitmapRatio, 1 / screenRatio / bitmapRatio, 3, 5);
            } else {
                Matrix.orthoM(mProjectMartix, 0, -1, 1, -bitmapRatio / screenRatio, bitmapRatio / screenRatio, 3, 5);
            }
            //设置相机的位置
            Matrix.setLookAtM(mViewMartix, 0, 0, 0, 5.0f, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
            //计算变换矩阵
            Matrix.multiplyMM(mMVPMartix, 0, mProjectMartix, 0, mViewMartix, 0);

        }
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        GLES20.glUseProgram(mProgram);
        onDraw();
        GLES20.glUniform1i(hIsHalf, isHalf ? 1 : 0);
        //以屏幕的宽高比作为纹理贴图的宽高比
        GLES20.glUniform1f(glHUxy, mScreenRatio);
        GLES20.glUniformMatrix4fv(glHMatrix,1,false,mMVPMartix,0);
        //启用顶点坐标的
        GLES20.glEnableVertexAttribArray(glHPosition);
        //启用文理贴图的顶点坐标
        GLES20.glEnableVertexAttribArray(glCoordinate);
        //启用纹理贴图
        GLES20.glUniform1i(glHTexture,0);
        textureId = createTexture();
    }

    /**
     * 创建纹理贴图
     * @return
     */
    private int createTexture() {
        int[] texture = new int[1];
        if (mBitmap != null && !mBitmap.isRecycled()) {
            //生成纹理
            GLES20.glGenTextures(1,texture,0);
            //绑定纹理
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,texture[0]);
            //设置缩小过滤为使用纹理中坐标最接近的一个像素的颜色作为需要绘制的像素颜色
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,GLES20.GL_TEXTURE_MIN_FILTER,GLES20.GL_NEAREST);
            //设置放大过滤为使用纹理中坐标最接近的若干个颜色，通过加权平均算法得到需要绘制的像素颜色
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,GLES20.GL_TEXTURE_MAG_FILTER,GLES20.GL_LINEAR);
            //设置环绕方向S，截取纹理坐标到[1/2n,1-1/2n]。将导致永远不会与border融合
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,GLES20.GL_TEXTURE_WRAP_S,GLES20.GL_CLAMP_TO_EDGE);
            //设置环绕方向T，截取纹理坐标到[1/2n,1-1/2n]。将导致永远不会与border融合
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,GLES20.GL_TEXTURE_WRAP_T,GLES20.GL_CLAMP_TO_EDGE);
            //根据以上指定的参数，生成一个2D纹理
            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D,0,mBitmap,0);
            return texture[0];
        }
        return 0;
    }


    /**
     * Surface 创建的声明周期
     *
     * @param program
     */
    protected abstract void onDrawCreate(int program);


    /**
     * OpenGL渲染绘制
     */
    protected abstract void onDraw();
}
