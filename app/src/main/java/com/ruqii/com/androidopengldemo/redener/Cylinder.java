package com.ruqii.com.androidopengldemo.redener;

import android.opengl.GLES20;
import android.opengl.Matrix;
import android.view.View;

import com.ruqii.com.androidopengldemo.utils.ShaderUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * author:黄汝琪 on 2018/11/6.
 * email:huangruqi88@163.com
 */
public class Cylinder extends Shape {

    private int mProgram;
    private Oval mBottomOval;
    private Oval mTopOval;
    private FloatBuffer vertexBuffer;
    private float[] mViewMatrix = new float[16];
    private float[] mProjectMatrix = new float[16];
    private float[] mMVPMatrix = new float[16];

    //切割份数
    private int n = 360;
    //圆柱高度
    private float height = 2.0f;
    //圆柱底部半径
    private float radius = 1.0f;

    private int vSize;


    public Cylinder(View view) {
        super(view);

        mBottomOval = new Oval(view);
        mTopOval = new Oval(view, height);

        ArrayList<Float> pos = new ArrayList<>();
        float angDegSpan = 360f / n;
        for (int i = 0; i < 360 + angDegSpan; i += angDegSpan) {
            pos.add((float) (radius * Math.sin(i * Math.PI / 180f)));
            pos.add((float) (radius * Math.cos(i * Math.PI / 180f)));
            pos.add(height);
            pos.add((float) (radius * Math.sin(i * Math.PI / 180f)));
            pos.add((float) (radius * Math.cos(i * Math.PI / 180f)));
            pos.add(0.0f);
        }
        float[] d = new float[pos.size()];
        for (int i = 0; i < d.length; i++) {
            d[i] = pos.get(i);
        }
        vSize = d.length / 3;
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(d.length * 4);
        byteBuffer.order(ByteOrder.nativeOrder());
        vertexBuffer = byteBuffer.asFloatBuffer();
        vertexBuffer.put(d);
        vertexBuffer.position(0);

    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        //开启深度检测测试
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        mProgram = ShaderUtils.createProgram(mView.getResources(), "vshader/Cone.glsl", "fshader/Cone.glsl");
        mBottomOval.onSurfaceCreated(gl, config);
        mTopOval.onSurfaceCreated(gl, config);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        //计算宽高比
        float ratio = (float) width / height;
        //设置投影透视
        Matrix.frustumM(mProjectMatrix, 0, -ratio, ratio, -1, 1, 3, 20);
        //设置相机位置
        Matrix.setLookAtM(mViewMatrix, 0, 1.0f, -10.0f, -4.0f, 0f, 0f, 0f, 0f, 1.0f, 0f);
        //计算变换矩阵
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectMatrix, 0, mViewMatrix, 0);

    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glUseProgram(mProgram);
        int mMatrix = GLES20.glGetUniformLocation(mProgram, "vMatrix");
        GLES20.glUniformMatrix4fv(mMatrix, 1, false, mMVPMatrix, 0);
        int mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
        GLES20.glEnableVertexAttribArray(mPositionHandle);
        GLES20.glVertexAttribPointer(mPositionHandle, 3, GLES20.GL_FLOAT, false, 0, vertexBuffer);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, vSize);
        GLES20.glDisableVertexAttribArray(mPositionHandle);

        mBottomOval.setMVPMatrix(mMVPMatrix);
        mBottomOval.onDrawFrame(gl);
        mTopOval.setMVPMatrix(mMVPMatrix);
        mTopOval.onDrawFrame(gl);
    }
}
