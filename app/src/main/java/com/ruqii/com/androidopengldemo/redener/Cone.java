package com.ruqii.com.androidopengldemo.redener;

import android.opengl.GLES20;
import android.opengl.Matrix;
import android.util.Log;
import android.view.View;

import com.ruqii.com.androidopengldemo.utils.ShaderUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * @author huangruqi
 * @Description:
 * @data 2018/11/6 16:12
 */
public class Cone extends Shape {
    private static final String TAG = "Cone";
    private int mProgram;
    private Oval mOval;

    private FloatBuffer vertexBuffer;
    private float[] mViewMatrix = new float[16];
    private float[] mProjectMatrix = new float[16];
    private float[] mMVPMatrix = new float[16];

    /**
     * 切割份数
     */
    private int n = 360;
    /**
     * 圆锥的高低
     */
    private float height = 2.0f;
    private float radius = 1.0f;
    private float[] colors = {1.0f, 1.0f, 1.0f, 1.0f};

    private int vSize;

    public Cone(View view) {
        super(view);
        mOval = new Oval(view);
        ArrayList<Float> pos = new ArrayList<>();
        pos.add(0.0f);
        pos.add(0.0f);
        pos.add(height);

        float angDegSpan = 360f / n;
        for (int i = 0; i < 360 + angDegSpan; i += angDegSpan) {
            pos.add((float) (radius * Math.sin(i * Math.PI / 180f)));
            pos.add((float) (radius * Math.cos(i * Math.PI / 180f)));
            pos.add(0.0f);
        }

        float[] d = new float[pos.size()];
        for (int i = 0; i < d.length; i++) {
            d[i] = pos.get(i);
        }

        vSize = d.length / 3;
        ByteBuffer bb = ByteBuffer.allocateDirect(d.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(d);
        vertexBuffer.position(0);

    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        //开启深度测试
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        //加载着色器文件并创建OpenGL程序
        mProgram = ShaderUtils.createProgram(mView.getResources(), "vshader/Cone.glsl", "fshader/Cone.glsl");
        mOval.onSurfaceCreated(gl, config);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        //计算宽高比
        float ratio = (float) width / height;
        //设置透视投影
        Matrix.frustumM(mProjectMatrix, 0, -ratio, ratio, -1, 1, 3, 20);
        //设置摄像头的位置
        Matrix.setLookAtM(mViewMatrix, 0, 1.0f, -10.0f, -4.0f, 0f, 0f, 0f, 0f, 1.0f, 0f);
        //计算变换矩阵
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectMatrix, 0, mViewMatrix, 0);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glUseProgram(mProgram);
        Log.d(TAG, "mProgram:" + mProgram);
        int mMatrix = GLES20.glGetUniformLocation(mProgram, "vMatrix");
        GLES20.glUniformMatrix4fv(mMatrix, 1, false, mMVPMatrix, 0);
        int mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
        Log.d(TAG, "mPositionHandle:" + mPositionHandle);
        GLES20.glEnableVertexAttribArray(mPositionHandle);
        GLES20.glVertexAttribPointer(mPositionHandle, 3, GLES20.GL_FLOAT, false, 0, vertexBuffer);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, vSize);
        GLES20.glDisableVertexAttribArray(mPositionHandle);

        mOval.setMVPMatrix(mMVPMatrix);
        mOval.onDrawFrame(gl);
    }
}
