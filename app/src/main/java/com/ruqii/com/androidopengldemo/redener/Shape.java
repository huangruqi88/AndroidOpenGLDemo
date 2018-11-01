package com.ruqii.com.androidopengldemo.redener;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.view.View;

/**
 * author:黄汝琪 on 2018/10/31.
 * email:huangruqi88@163.com
 */
public abstract class Shape implements GLSurfaceView.Renderer {
    protected View mView;

    public Shape(View view) {
        this.mView = view;
    }

    public int loadShader(int type,String shaderCode){
        //根据type创建顶点着色器或者片源着色器
        int shader = GLES20.glCreateShader(type);
        //将资源加入到着色器中
        GLES20.glShaderSource(shader,shaderCode);
        //编译shader
        GLES20.glCompileShader(shader);

        return shader;
    }
}
