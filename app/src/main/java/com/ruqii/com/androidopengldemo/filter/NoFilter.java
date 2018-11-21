/*
 *
 * NoFilter.java
 * 
 * Created by huangruqi on 2016/11/19
 * Copyright © 2016年 深圳哎吖科技. All rights reserved.
 */
package com.ruqii.com.androidopengldemo.filter;

import android.content.res.Resources;

/**
 * Description:
 */
public class NoFilter extends AFilter {

    public NoFilter(Resources res) {
        super(res);
    }

    @Override
    protected void onCreate() {
        createProgramByAssetsFile("shader/base_vertex.glsl",
            "shader/base_fragment.glsl");
    }

    @Override
    protected void onSizeChanged(int width, int height) {

    }
}
