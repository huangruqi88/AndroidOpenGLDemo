package com.ruqii.com.androidopengldemo.image.filter;

import android.content.Context;
import android.opengl.GLES20;

/**
 * author:黄汝琪 on 2018/11/8.
 * email:huangruqi88@163.com
 */
public class ColorFilter extends AFilter{

    private Filter mFilter;
    /**
     * 切换图片模式的类型
     */
    private int hChangeType;
    /**
     * 切换模式的纹理颜色
     */
    private int hChangeColor;


    public ColorFilter(Context context,Filter filter) {
        super(context, "filter/default_vertex.glsl", "filter/color_fragment.glsl");
        this.mFilter = filter;
    }
    @Override
    protected void onDrawCreate(int program) {
        hChangeType = GLES20.glGetUniformLocation(program,"vChangeType");
        hChangeColor = GLES20.glGetUniformLocation(program,"vChangeColor");
    }
    @Override
    protected void onDraw() {
        GLES20.glUniform1i(hChangeType,mFilter.getType());
        GLES20.glUniform3fv(hChangeColor,1,mFilter.getdata(),0);
    }



    public enum Filter{
        /**
         * 默认模式
         */
        NONE(0,new float[]{0.0f,0.0f,0.0f}),
        /**
         * 黑白模式
         */
        GRAY(1,new float[]{0.299f,0.587f,0.114f}),
        /**
         * 冷色调
         */
        COOL(2,new float[]{0.0f,0.0f,0.1f}),
        /**
         * 暖色调
         */
        WARM(2,new float[]{0.1f,0.1f,0.0f}),
        /**
         * 模糊
         */
        BLUR(3,new float[]{0.006f,0.004f,0.002f}),
        /**
         * 放大镜
         */
        MAGN(4,new float[]{0.0f,0.0f,0.4f});


        private int vChangeType;
        private float[] data;

        Filter(int vChangeType,float[] data){
            this.vChangeType=vChangeType;
            this.data=data;
        }

        public int getType(){
            return vChangeType;
        }

        public float[] getdata(){
            return data;
        }

    }
}
