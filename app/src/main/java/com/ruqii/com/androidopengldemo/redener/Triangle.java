package com.ruqii.com.androidopengldemo.redener;

import android.opengl.GLES20;
import android.util.Log;
import android.view.View;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * author:黄汝琪 on 2018/10/31.
 * email:huangruqi88@163.com
 */
public class Triangle extends Shape {

    private static final String TAG = "Triangle";

    private FloatBuffer vertexBuffer;
    private String vertexShaderCode =
            "attribute vec4 vPosition;" +
                    "void main() {" +
                    "  gl_Position = vPosition;" +
                    "}";
    private String fragmentShaderCode =
            "precision mediump float;" +
                    "uniform vec4 vColor;" +
                    "void main() {" +
                    "  gl_FragColor = vColor;" +
                    "}";


    private int mProgram;

    public static final int COORDS_PRE_VERTEX = 3;
    public static float triangleCoods[] = {
            //top
            0.5f, 0.5f, 0.0f,
            //bottom left
            -0.5f, -0.5f, 0.0f,
            //bottom right
            0.5f, -0.5f, 0.0f
    };


    static final int COORDS_PER_VERTEX = 3;
    static float triangleCoords[] = {
            0.5f,  0.5f, 0.0f, // top
            -0.5f, -0.5f, 0.0f, // bottom left
            0.5f, -0.5f, 0.0f  // bottom right
    };

    private int mPositionHandle;
    private int mColorHandle;


    //顶点个数
    private final int vertexCount = triangleCoords.length / COORDS_PER_VERTEX;
    //顶点之间的偏移量
    private final int vertexStride = COORDS_PER_VERTEX * 4; // 每个顶点四个字节


    //设置颜色，依次为红绿蓝和透明通道
    float color[] = { 1.0f, 1.0f, 1.0f, 1.0f };
    public Triangle(View view) {
        super(view);
        //写入高效字节缓冲区
        ByteBuffer bb = ByteBuffer.allocateDirect(triangleCoods.length * 4);
        //ByteOrder.nativeOrder()检索此缓冲区的字符顺序
        bb.order(ByteOrder.nativeOrder());

        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(triangleCoods);
        vertexBuffer.position(0);
        int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
        if (vertexShader == 0) {
            Log.d(TAG, "loadShader vertex failed");
        }

        int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);
        if (fragmentShader == 0) {
            Log.d(TAG, "loadShader fragment failed");
        }

        //创建一个空的OpenGLES程序
        mProgram = GLES20.glCreateProgram();
        Log.d(TAG, "Could not link program:" + GLES20.glGetProgramInfoLog(mProgram));
        //将顶点着色器加入到OpenGLES程序中
        GLES20.glAttachShader(mProgram, vertexShader);
        //将片元着色器加入到OpenGLES程序中
        GLES20.glAttachShader(mProgram, fragmentShader);
        //连接到OpenGLES程序中
        GLES20.glLinkProgram(mProgram);

    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {

    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {

    }

    @Override
    public void onDrawFrame(GL10 gl) {

        //将程序加入到OpenGLES程序中
        GLES20.glUseProgram(mProgram);

        //获取顶点着色器的vPosition程序句柄
        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
        //启用三角形顶点的句柄
        GLES20.glEnableVertexAttribArray(mPositionHandle);
        //准备三角形的坐标数据
        GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PRE_VERTEX, GLES20.GL_FLOAT,
                false, vertexStride, vertexBuffer);
        //获取片元着色器的vColor成员句柄
        mColorHandle = GLES20.glGetUniformLocation(mProgram,"vColor");
        //设置绘制三角形的颜色
        GLES20.glUniform4fv(mColorHandle, 1, color, 0);
        //绘制三角形
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES,0,vertexCount);
        //禁止顶点数据句柄
        GLES20.glDisableVertexAttribArray(mPositionHandle);

    }
}
