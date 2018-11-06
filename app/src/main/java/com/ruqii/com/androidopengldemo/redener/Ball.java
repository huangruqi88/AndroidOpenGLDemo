package com.ruqii.com.androidopengldemo.redener;

import android.view.PointerIcon;
import android.view.View;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * author:黄汝琪 on 2018/11/6.
 * email:huangruqi88@163.com
 */
public class Ball extends Shape {

    private float step = 5f;
    private FloatBuffer vertexBuffer;
    private int vSize;

    private int mProgram;
    private float[] mViewMatrix = new float[16];
    private float[] mProjectMatrix = new float[16];
    private float[] mMVPMatrix = new float[16];


    public Ball(View view) {
        super(view);
        float[] dataPos = createBallPos();
        ByteBuffer buffer = ByteBuffer.allocateDirect(dataPos.length * 4);
        buffer.order(ByteOrder.nativeOrder());
        vertexBuffer = buffer.asFloatBuffer();
        vertexBuffer.put(dataPos);
        vertexBuffer.position(0);
    }

    private float[] createBallPos() {
        //球以（0,0,0）为中心，以R为半径，则球上任意一点的坐标为
        //(R * cos(a) * sin(b), y0 = R * sin(a) ,R * cos(a) * cos(b))
        //其中，a为圆心到点的线段与xy平面的夹角，b为圆心到点的线段在xz平面的投影与z轴的夹角
        ArrayList<Float> data = new ArrayList<>();
        float r1, r2;
        float h1, h2;
        float sin, cos;

        for (float i = -90; i < 90 + step; i += step) {
            r1 = (float) Math.cos(i * Math.PI / 180.0);
            r2 = (float) Math.cos((i + step) * Math.PI / 180.0);
            h1 = (float) Math.cos(i * Math.PI / 180.0);
            h2 = (float) Math.cos((i + step) * Math.PI / 180.0);

            //固定维度，360度旋转遍历一条纬线
            float step2 = step * 2;
            for (float j = 0; j < 360.0f; j += step2) {
                cos = (float) Math.cos(j * Math.PI / 180.0f);
                sin = -(float) Math.sin(j * Math.PI / 180.0f);

                data.add(r2 * cos);
                data.add(h2);
                data.add(r2 * sin);
                data.add()
            }
        }


        return new float[0];
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {

    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {

    }

    @Override
    public void onDrawFrame(GL10 gl) {

    }
}
