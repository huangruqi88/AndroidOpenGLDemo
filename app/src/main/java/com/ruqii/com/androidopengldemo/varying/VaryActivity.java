package com.ruqii.com.androidopengldemo.varying;

import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.ruqii.com.androidopengldemo.R;

/**
 * author:黄汝琪 on 2018/11/12.
 * email:huangruqi88@163.com
 */
public class VaryActivity extends AppCompatActivity {

    private GLSurfaceView mGLView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opengl);
        setTitle("图形变换");
        initGL();
    }

    private void initGL() {
        mGLView = findViewById(R.id.mGLView);
        mGLView.setEGLContextClientVersion(2);
        mGLView.setRenderer(new VaryRender(getResources()));
        mGLView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }
}
