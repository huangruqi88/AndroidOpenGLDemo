package com.ruqii.com.androidopengldemo.utils;

import android.opengl.Matrix;

import java.util.Arrays;
import java.util.Stack;

/**
 * author:黄汝琪 on 2018/11/12.
 * email:huangruqi88@163.com
 */
public class VaryTools {
    /**
     * 相机矩阵
     */
    private float[] mMatrixCamera=new float[16];
    /**
     * 投影矩阵
     */
    private float[] mMatrixProjection=new float[16];
    /**
     * 原始矩阵
     */
    private float[] mMatrixCurrent=
            {1,0,0,0,
                    0,1,0,0,
                    0,0,1,0,
                    0,0,0,1};
    /**
     * 变换矩阵堆栈
     */
    private Stack<float[]> mStack;

    public VaryTools(){
        mStack=new Stack<>();
    }

    /**
     * 保护现场 (即原始目标坐标系存入)
     */
    public void pushMatrix(){
        mStack.push(Arrays.copyOf(mMatrixCurrent,16));
    }

    /**
     * 恢复现场 （即将放入原始目标坐标系取出）
     */
    public void popMatrix(){
        mMatrixCurrent=mStack.pop();
    }

    public void clearStack(){
        mStack.clear();
    }

    /**
     * 平移变换
     *
     * @param x 世界坐标X轴
     * @param y 世界坐标Y轴
     * @param z 世界坐标Z轴
     */
    public void translate(float x,float y,float z){
        Matrix.translateM(mMatrixCurrent,0,x,y,z);
    }

    /**
     * 旋转变换
     *
     * @param angle 旋转角度
     * @param x 世界坐标X轴
     * @param y 世界坐标Y轴
     * @param z 世界坐标Z轴
     */
    public void rotate(float angle,float x,float y,float z){
        Matrix.rotateM(mMatrixCurrent,0,angle,x,y,z);
    }

    /**
     * 缩放变换
     *
     * @param x 世界坐标X轴
     * @param y 世界坐标Y轴
     * @param z 世界坐标Z轴
     */
    public void scale(float x,float y,float z){
        Matrix.scaleM(mMatrixCurrent,0,x,y,z);
    }

    /**
     * 设置相机
     *
     * @param ex 相机坐标X轴
     * @param ey 相机坐标y轴
     * @param ez 相机坐标z轴
     * @param cx 目标物体的坐标x轴
     * @param cy 目标物体的坐标y轴
     * @param cz 目标物体的坐标z轴
     * @param ux 相机正上方向量(即相机需要拍摄的方向)x
     * @param uy 相机正上方向量(即相机需要拍摄的方向)y
     * @param uz 相机正上方向量(即相机需要拍摄的方向)z
     */
    public void setCamera(float ex,float ey,float ez,float cx,float cy,float cz,float ux,float uy,float uz){
        Matrix.setLookAtM(mMatrixCamera,0,ex,ey,ez,cx,cy,cz,ux,uy,uz);
    }

    /**
     * 透视投影
     * @param left 左边
     * @param right 右边
     * @param bottom 底边
     * @param top  顶边
     * @param near 近距离（即物体面向屏幕的一面至相机的距离）
     * @param far 远距离（即屏幕背面的一面至相机的距离）
     */
    public void frustum(float left,float right,float bottom,float top,float near,float far){
        Matrix.frustumM(mMatrixProjection,0,left,right,bottom,top,near,far);
    }

    /**
     * 正交投影
     * @param left 左边
     * @param right 右边
     * @param bottom 底边
     * @param top  顶边
     * @param near 近距离（即物体面向屏幕的一面至相机的距离）
     * @param far 远距离（即屏幕背面的一面至相机的距离）
     */
    public void ortho(float left,float right,float bottom,float top,float near,float far){
        Matrix.orthoM(mMatrixProjection,0,left,right,bottom,top,near,far);
    }

    /**
     * 最终的矩阵值
     * @return
     */
    public float[] getFinalMatrix(){
        float[] ans=new float[16];
        Matrix.multiplyMM(ans,0,mMatrixCamera,0,mMatrixCurrent,0);
        Matrix.multiplyMM(ans,0,mMatrixProjection,0,ans,0);
        return ans;
    }
}
