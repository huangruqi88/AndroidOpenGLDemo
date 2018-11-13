package com.ruqii.com.androidopengldemo.varying;

import android.content.res.Resources;
import android.opengl.GLES20;

import com.ruqii.com.androidopengldemo.utils.ShaderUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/**
 * @author huangruqi
 * @Description:
 * @Package: com.ruqii.com.androidopengldemo.varying
 * @data 2018/11/12 15:58
 */
public class Cube {
    private final float cubePositions[] = {
            //正面左上0
            -1.0f, 1.0f, 1.0f,
            //正面左下1
            -1.0f, -1.0f, 1.0f,
            //正面右下2
            1.0f, -1.0f, 1.0f,
            //正面右上3
            1.0f, 1.0f, 1.0f,
            //反面左上4
            -1.0f, 1.0f, -1.0f,
            //反面左下5
            -1.0f, -1.0f, -1.0f,
            //反面右下6
            1.0f, -1.0f, -1.0f,
            //反面右上7
            1.0f, 1.0f, -1.0f,
    };

    private final short index[] = {
            //后面
            6, 7, 4, 6, 4, 5,
            //右面
            6, 3, 7, 6, 2, 3,
            //下面
            6, 5, 1, 6, 1, 2,
            //正面
            0, 3, 2, 0, 2, 1,
            //左面
            0, 1, 5, 0, 5, 4,
            //上面
            0, 7, 3, 0, 4, 7,
    };

    private float color[] = {
            0f, 1f, 0f, 1f,
            0f, 1f, 0f, 1f,
            0f, 1f, 0f, 1f,
            0f, 1f, 0f, 1f,
            1f, 0f, 0f, 1f,
            1f, 0f, 0f, 1f,
            1f, 0f, 0f, 1f,
            1f, 0f, 0f, 1f,
    };
    private FloatBuffer vertexBuffer;
    private FloatBuffer colorBuffer;
    private ShortBuffer indexBuffer;
    private Resources mResources;
    private int mProgram;
    private int hVertex;
    private int hColor;
    private int hMatrix;

    private float[] mMatrix;


    public Cube(Resources resources) {
        mResources = resources;
        initBuffer();
    }

    private void initBuffer() {
        ByteBuffer vv = ByteBuffer.allocateDirect(cubePositions.length * 4);
        vv.order(ByteOrder.nativeOrder());
        vertexBuffer = vv.asFloatBuffer();
        vertexBuffer.put(cubePositions);
        vertexBuffer.position(0);

        ByteBuffer cc = ByteBuffer.allocateDirect(color.length * 4);
        cc.order(ByteOrder.nativeOrder());
        colorBuffer = cc.asFloatBuffer();
        colorBuffer.put(color);
        colorBuffer.position(0);

        ByteBuffer ii = ByteBuffer.allocateDirect(index.length * 2);
        ii.order(ByteOrder.nativeOrder());
        indexBuffer = ii.asShortBuffer();
        indexBuffer.put(index);
        indexBuffer.position(0);
    }

    /**
     * 创建OpenGL环境
     */
    public void create() {
        mProgram = ShaderUtils.createProgram(mResources, "varying/vertex.glsl", "varying/fragment.glsl");
        hVertex = GLES20.glGetAttribLocation(mProgram, "vPosition");
        hColor = GLES20.glGetAttribLocation(mProgram, "aColor");
        hMatrix = GLES20.glGetUniformLocation(mProgram, "vMatrix");
    }

    /**
     * 画正方形
     */
    public void drawCube() {
        //将程序加入到OpenGLES2.0环境
        GLES20.glUseProgram(mProgram);
        //指定vMatrix的值
        if (null != mMatrix) {
            GLES20.glUniformMatrix4fv(hMatrix, 1, false, mMatrix, 0);
        }
        //启用句柄
        GLES20.glEnableVertexAttribArray(hVertex);
        GLES20.glEnableVertexAttribArray(hColor);
        //准备三角形的坐标数据
        GLES20.glVertexAttribPointer(hVertex, 3, GLES20.GL_FLOAT, false, 0, vertexBuffer);
        //设置三角形的颜色
        GLES20.glVertexAttribPointer(hColor, 4, GLES20.GL_FLOAT, false, 0, colorBuffer);
        //索引法绘制正方体
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, index.length, GLES20.GL_UNSIGNED_SHORT, indexBuffer);
        //禁止顶点数组
        GLES20.glDisableVertexAttribArray(hVertex);
        GLES20.glDisableVertexAttribArray(hColor);
    }

    public void setMatrix(float[] matrix) {
        mMatrix = matrix;
    }
}
