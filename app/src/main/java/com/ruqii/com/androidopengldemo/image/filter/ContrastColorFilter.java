package com.ruqii.com.androidopengldemo.image.filter;

import android.content.Context;
import android.opengl.GLES20;

/**
 * author:黄汝琪 on 2018/11/8.
 * email:huangruqi88@163.com
 * 一般原图和一半纹理涂对比
 */
public class ContrastColorFilter extends AFilter {

    private ColorFilter.Filter mFilter;
    private int hChangeType;
    private int hChangeColor;

    public ContrastColorFilter(Context context, ColorFilter.Filter mFilter) {
        super(context, "filter/half_color_vertex.glsl", "filter/half_color_fragment.glsl");
        this.mFilter = mFilter;
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

}
