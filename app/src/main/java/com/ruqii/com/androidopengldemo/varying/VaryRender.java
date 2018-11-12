package com.ruqii.com.androidopengldemo.varying;

import android.content.res.Resources;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import com.ruqii.com.androidopengldemo.utils.VaryTools;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * author:黄汝琪 on 2018/11/12.
 * email:huangruqi88@163.com
 */
public class VaryRender implements GLSurfaceView.Renderer {

    private VaryTools mTools;
    private Cube mCube;

    public VaryRender(Resources resources) {
        mTools = new VaryTools();
        mCube = new Cube(resources);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        //开启深度测试
        GLES20.glClear(GLES20.GL_DEPTH_TEST);
        mCube.create();

    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        //设置窗体
        GLES20.glViewport(0, 0, width, height);
        float ratio = (float) width / height;
        mTools.ortho(-ratio * 6, ratio * 6, -6, 6, 3, 20);
        mTools.setCamera(0,0,10,0,0,0,0,1,0);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        mCube.setMatrix(mTools.getFinalMatrix());
        mCube.drawCube();

        //Y轴正方形平移
        mTools.pushMatrix();
        mTools.translate(0,3,0);
        mCube.setMatrix(mTools.getFinalMatrix());
        mCube.drawCube();

        //y轴负方向平移，然后按xyz->(0,0,0)到(1,1,1)旋转30度
        mTools.pushMatrix();
        mTools.translate(0,-3,0);
        mTools.rotate(30f,1,1,1);
        mCube.setMatrix(mTools.getFinalMatrix());

        //x轴负方向平移，然后按xyz->(0,0,0)到(1,-1,1)旋转120度，在放大到0.5倍
        mTools.pushMatrix();
        mTools.translate(-3,0,0);
        mTools.scale(0.5f,0.5f,0.5f);
        mCube.setMatrix(mTools.getFinalMatrix());
        mCube.drawCube();

        //在以上变换的基础上再进行变换
        mTools.pushMatrix();
        mTools.translate(12,0,0);
        mTools.scale(1.0f,1.0f,1.0f);
        mTools.rotate(30,1,2,1);
        mCube.setMatrix(mTools.getFinalMatrix());
        mCube.drawCube();
        mTools.popMatrix();

        //接着被中断的地方执行
        mTools.rotate(30f,-1,-1,1);
        mCube.setMatrix(mTools.getFinalMatrix());
        mCube.drawCube();
        mTools.popMatrix();

    }
}
