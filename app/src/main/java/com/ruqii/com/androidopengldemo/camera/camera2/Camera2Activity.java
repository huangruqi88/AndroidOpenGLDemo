package com.ruqii.com.androidopengldemo.camera.camera2;

import android.Manifest;
import android.opengl.GLSurfaceView.Renderer;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import com.ruqii.com.androidopengldemo.R;
import com.ruqii.com.androidopengldemo.camera.renderer.Camera2Renderer;
import com.ruqii.com.androidopengldemo.camera.renderer.TextureController;
import com.ruqii.com.androidopengldemo.utils.PermissionUtils;

/**
 * @author huangruqi
 * @Description:
 * @Package: com.ruqii.com.androidopengldemo.camera.camera2
 * @data 2018/11/21 10:01
 */
public class Camera2Activity extends AppCompatActivity {
    private SurfaceView mSurfaceView;
    private TextureController mController;
    private Renderer mRenderer;
    private int cameraId = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PermissionUtils.askPermission(this, new String[]{Manifest.permission.CAMERA, Manifest
                .permission.WRITE_EXTERNAL_STORAGE}, 10, initViewRunnable);
    }

    private Runnable initViewRunnable = new Runnable() {
        @Override
        public void run() {

            //TODO 设置数据源
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mRenderer = new Camera2Renderer(Camera2Activity.this,cameraId);
            } else {
                mRenderer = new Camera1Renderer();
            }
            setContentView();
            mSurfaceView = (SurfaceView) findViewById(R.id.mSurface);
            mController = new TextureController(Camera2Activity.this);
//            WaterMarkFilter filter=new WaterMarkFilter(getResources());
//            filter.setWaterMark(BitmapFactory.decodeResource(getResources(),R.mipmap.logo));
//            filter.setPosition(300,50,300,150);
//            mController.addFilter(filter);
            onFilterSet(mController);
            mController.setFrameCallback(720, 1280, Camera2Activity.this);
            mSurfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
                @Override
                public void surfaceCreated(SurfaceHolder holder) {
                    mController.surfaceCreated(holder);
                    mController.setRenderer(mRenderer);
                }

                @Override
                public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                    mController.surfaceChanged(width, height);
                }

                @Override
                public void surfaceDestroyed(SurfaceHolder holder) {
                    mController.surfaceDestroyed();
                }
            });

        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionUtils.onRequestPermissionsResult(requestCode == 10, grantResults, initViewRunnable,
                new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(Camera2Activity.this, "没有获得必要的权限", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
    }
}
