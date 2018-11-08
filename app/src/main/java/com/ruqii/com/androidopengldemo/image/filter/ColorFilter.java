package com.ruqii.com.androidopengldemo.image.filter;

import android.content.Context;

/**
 * author:黄汝琪 on 2018/11/8.
 * email:huangruqi88@163.com
 */
public class ColorFilter extends AFilter{

    public ColorFilter(Context context, String vertex, String fragment) {
        super(context, vertex, fragment);
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

        public float[] data(){
            return data;
        }

    }
}
