package com.ruqii.com.androidopengldemo.image.filter;

import android.content.Context;

/**
 * author:黄汝琪 on 2018/11/8.
 * email:huangruqi88@163.com
 */
public class ContrastColorFilter extends AFilter {

    private ColorFilter.Filter filter;
    private int hChangeType;
    private int hChangeColor;

    public ContrastColorFilter(Context context, ColorFilter.Filter filter) {
        super(context, "filter/half_color_vertex.glsl", "filter/half_color_fragment.glsl");
        this.hChangeType = hChangeType;
        this.hChangeColor = hChangeColor;
    }
}
