package com.ruqii.com.androidopengldemo.redener;

import android.opengl.GLES20;
import android.opengl.Matrix;
import android.util.Log;
import android.view.View;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * author:黄汝琪 on 2018/11/1.
 * email:huangruqi88@163.com
 */
public class RegularTriangle extends Shape {
    private static final String TAG = "RegularTriangle";
    private FloatBuffer vertexBuffer;
    private final String vertexShaderCode =
            "attribute vec4 vPosition;" +
                    "uniform mat4 vMatrix;"+
                    "void main() {" +
                    "  gl_Position = vMatrix * vPosition;" +
                    "}";
    private final String fragmentShaderCode =
            "precision mediump float;" +
                    "uniform vec4 vColor;" +
                    "void main(){" +
                    "    gl_FragColor = vColor;" +
                    "}";


    private int mProgram;
    public static final int COORDS_PER_VERTEX = 3;
    public static float triangleCoords[] = {
      0.5f,  0.5f, 0.0f,
      -0.5f, -0.5f, 0.0f,
      0.5f, -0.5f, 0.0f
    };

    private int mPositionHandle;
    private int mColorHandle;

    private float[] mViewMatrix = new float[16];
    private float[] mProjectMatrix = new float[16];
    private float[] mMVPMatrix = new float[16];

    /**
     * 顶点个数
     */
    private final int vertexCount = triangleCoords.length / COORDS_PER_VERTEX;
    /**
     * 顶点之间的偏移量
     * 每个顶点四个字节
     */
    private final int vertexStride = COORDS_PER_VERTEX * 4;

    private int mMatrixHandle;
    /**
     * 设置颜色，依次为红绿蓝和透明通道
     */
    float[] color = {1.0f, 1.0f, 1.0f, 1.0f};

    public RegularTriangle(View view) {
        super(view);

        ByteBuffer bb = ByteBuffer.allocateDirect(triangleCoords.length * 4);
        bb.order(ByteOrder.nativeOrder());

        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(triangleCoords);
        vertexBuffer.position(0);
        int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER,vertexShaderCode);
        int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER,fragmentShaderCode);
        if (vertexShader == 0) {
            Log.d(TAG, "loadShader vertex failed");
        }
        if (fragmentShader == 0) {
            Log.d(TAG, "loadShader fragment failed");
        }
        //创建一个空的OpenGl程序
        mProgram = GLES20.glCreateProgram();


        //将顶点着色器加入到程序中
        GLES20.glAttachShader(mProgram,vertexShader);
        //将片元着色器加入到程序中
        GLES20.glAttachShader(mProgram,fragmentShader);

        Log.d(TAG, "Could not link program:" + GLES20.glGetProgramInfoLog(mProgram));
        //连接到OpenGL程序中
        GLES20.glLinkProgram(mProgram);


    }


    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {

    }
    /**
     * 实际上相机设置和投影设置并不是真正的设置，而是通过设置参数，得到一个使用相机后顶点坐标的变换矩阵，
     * 和投影下的顶点坐标变换矩阵，我们还需要把矩阵传入给顶点着色器，在顶点着色器中用传入的矩阵乘以坐标的向量，
     * 得到实际展示的坐标向量。
     * TODO:注意，是矩阵乘以坐标向量，不是坐标向量乘以矩阵，矩阵乘法是不满足交换律的。
     * 而通过上面的相机设置和投影设置，我们得到的是两个矩阵，为了方便，我们需要将相机矩阵和投影矩阵相乘，
     * 得到一个实际的变换矩阵，再传给顶点着色器。矩阵相乘：
     * @param gl
     * @param width
     * @param height
     *
     *
     * Matrix.frustumM (float[] m,         //接收透视投影的变换矩阵
     *                 int mOffset,        //变换矩阵的起始位置（偏移量）
     *                 float left,         //相对观察点近面的左边距
     *                 float right,        //相对观察点近面的右边距
     *                 float bottom,       //相对观察点近面的下边距
     *                 float top,          //相对观察点近面的上边距
     *                 float near,         //相对观察点近面距离
     *                 float far)          //相对观察点远面距离
     *
     * Matrix.multiplyMM (float[] result, //接收相乘结果
     *                 int resultOffset,  //接收矩阵的起始位置（偏移量）
     *                 float[] lhs,       //左矩阵
     *                 int lhsOffset,     //左矩阵的起始位置（偏移量）
     *                 float[] rhs,       //右矩阵
     *                 int rhsOffset)     //右矩阵的起始位置（偏移量）
     *
     */
    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        //计算宽高比
        float ratio = (float) width / height;
        //设置投影透视
        Matrix.frustumM(mProjectMatrix, 0, -ratio, ratio, -1, 1, 3, 7);
        //设置相机的位置
        Matrix.setLookAtM(mViewMatrix, 0, 0, 0, 7.0f, 0f,
                0f, 0f, 0f, 1.0f, 0.0f);
        //计算变换矩阵
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectMatrix, 0, mViewMatrix, 0);

    }
    @Override
    public void onDrawFrame(GL10 gl) {
        //将程序加入到OpenGL2.0环境
        GLES20.glUseProgram(mProgram);
        //获取变换矩阵vMatrix成员句柄
        mMatrixHandle = GLES20.glGetUniformLocation(mProgram,"vMatrix");
        //指定vMatrix的值
        GLES20.glUniformMatrix4fv(mMatrixHandle,1,false,mMVPMatrix,0);
        //获取顶点着色器vPosition的句柄
        mPositionHandle = GLES20.glGetAttribLocation(mProgram,"vPosition");
        //启用三角形顶点的句柄
        GLES20.glEnableVertexAttribArray(mPositionHandle);
        //准备三角形坐标数据
        GLES20.glVertexAttribPointer(mPositionHandle,COORDS_PER_VERTEX,GLES20.GL_FLOAT,false,vertexStride,vertexBuffer);
        //获取片元着色器的句柄
        mColorHandle = GLES20.glGetUniformLocation(mProgram,"vColor");
        //设置三角形颜色
        GLES20.glUniform4fv(mColorHandle,1,color,0);
        //绘制三角形
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES,0,vertexCount);
        //禁止顶点数组句柄
        GLES20.glDisableVertexAttribArray(mPositionHandle);
    }
}
