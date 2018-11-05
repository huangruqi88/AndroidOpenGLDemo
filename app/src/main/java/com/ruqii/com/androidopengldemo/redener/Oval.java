package com.ruqii.com.androidopengldemo.redener;

import android.opengl.GLES20;
import android.opengl.Matrix;
import android.view.View;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * @author: 黄汝琪
 * @dete 2018/11/1.
 * @email: huangruqi88@163.com
 * 绘制圆形
 */
public class Oval extends Shape {
    private FloatBuffer vertexBuffer;
    private final String vertexShaderCode =
            "attribute vec4 vPosition;" +
                    "uniform mat4 vMatrix;" +
                    "void main(){" +
                    "   gl_Position = vMatrix * vPosition;" +
                    "}";
    private final String fragmentShaderCode =
            "precision mediump float;" +
                    "uniform vec4 vColor;" +
                    "void main(){" +
                    "   gl_FragColor = vColor;" +
                    "}";

    private int mProgram;

    public static final int COORDS_PER_VERTEX = 3;

    private int mPositionHandle;
    private int mColorHandle;
    private float[] mViewMatrix = new float[16];
    private float[] mProjectMatrix = new float[16];
    private float[] mMVPMatrix = new float[16];

    /**
     * 顶点之间的偏移量
     */
    private final int vertexStride = 0;
    private int mMatrixHandler;

    private float radius = 1.0f;
    /**
     * 切割份数
     */
    private int n = 360;

    private float[] shapePos;

    private float height = 0.0f;

    /**
     * 设置颜色通道
     */
    float[] color = {1.0f,1.0f,1.0f,1.0f};

    public Oval(View view) {
        this(view,0.0f);
    }

    public Oval(View view, float height) {
        super(view);
        this.height = height;
        shapePos = createPositions();
        ByteBuffer bb = ByteBuffer.allocateDirect(shapePos.length * 4);
        bb.order(ByteOrder.nativeOrder());

        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(shapePos);
        vertexBuffer.position(0);

        int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER,vertexShaderCode);
        int fragmetShader = loadShader(GLES20.GL_FRAGMENT_SHADER,fragmentShaderCode);

        //创建一个空的OpenGL程序
        mProgram = GLES20.glCreateProgram();
        //将顶点着色器加入到OpenGL程序中
        GLES20.glAttachShader(mProgram,vertexShader);
        //将片元着色器加入到OpenGL程序
        GLES20.glAttachShader(mProgram,fragmetShader);
        //连接到着色程序
        GLES20.glLinkProgram(mProgram);

    }

    /**
     * 初始化组成圆形所需要的三角形的顶点位置
     * @return
     */
    private float[] createPositions() {
        ArrayList<Float> data = new ArrayList<>();
        //设置圆心坐标
        data.add(0.0f);
        data.add(0.0f);
        data.add(height);

        float angDegSpan = 360f / n;
        for (int i = 0; i < 360 + angDegSpan; i += angDegSpan) {
            data.add((float) (radius * Math.sin(i * Math.PI / 180f)));
            data.add((float) (radius * Math.cos(i * Math.PI / 180f)));
            data.add(height);
        }
        float[] f = new float[data.size()];
        for (int i = 0; i < f.length; i++) {
            f[i] = data.get(i);
        }

        return f;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {

    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        //计算宽高比
        float ratio = (float) width / height;
        //设置透视投影
        Matrix.frustumM(mProjectMatrix,0,-ratio,ratio,-1,1,3,7);
        //设置相机位置
        Matrix.setLookAtM(mViewMatrix,0,0.0f,0.0f,7.0f,0,0,0,0,1.0f,0.0f);
        //计算变换矩阵
        Matrix.multiplyMM(mMVPMatrix,0,mProjectMatrix,0,mViewMatrix,0);
    }

    /**
     * https://blog.csdn.net/xiajun07061225/article/details/7455283
     *  GLES20.GL_TRIANGLE_STRIP 保证所有的三角形都是按照相同的方向绘制的，
     *                           使这个三角形串能够正确形成表面的一部分。对于某些操作，
     *                           维持方向是很重要的，比如剔除。
     *                           注意：顶点个数n至少要大于3，否则不能绘制任何三角形。
     *  GLES20.GL_TRIANGLE_FAN 三角形以一个顶点绘制最终形成的结果为扇形
     * @param gl
     */
    @Override
    public void onDrawFrame(GL10 gl) {
        //将程序加入到OpenGL2.0环境中
        GLES20.glUseProgram(mProgram);
        //获取变换矩阵的成员句柄
        mMatrixHandler = GLES20.glGetUniformLocation(mProgram,"vMatrix");
        //指定vMatrix的值
        GLES20.glUniformMatrix4fv(mMatrixHandler,1,false,mMVPMatrix,0);
        //获取顶点着色器的vPosition成员句柄
        mPositionHandle = GLES20.glGetAttribLocation(mProgram,"vPosition");
        //启用三角形的顶点的句柄
        GLES20.glEnableVertexAttribArray(mPositionHandle);
        //准备三角形的坐标数据
        GLES20.glVertexAttribPointer(mPositionHandle,COORDS_PER_VERTEX,GLES20.GL_FLOAT,false,vertexStride,vertexBuffer);
        //获取片元着色器vColor的成员句柄
        mColorHandle = GLES20.glGetUniformLocation(mProgram,"vColor");
        //设置三角形的颜色
        GLES20.glUniform4fv(mColorHandle,1,color,0);
        //绘制三角形
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN,0,shapePos.length / 3);
        //禁止顶点数据
        GLES20.glDisableVertexAttribArray(mPositionHandle);
    }
}
