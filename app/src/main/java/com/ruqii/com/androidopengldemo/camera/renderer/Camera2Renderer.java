package com.ruqii.com.androidopengldemo.camera.renderer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.RequiresApi;
import android.util.Size;
import android.view.Surface;
import android.view.SurfaceHolder;

import java.util.ArrayList;
import java.util.Arrays;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.content.Context.CAMERA_SERVICE;
import static android.hardware.camera2.CameraDevice.TEMPLATE_PREVIEW;

/**
 * @author huangruqi
 * @Description:
 * @Package: com.ruqii.com.androidopengldemo.camera.renderer
 * @data 2018/11/21 10:23
 * <p>
 * Android5.0以上使用Camera2
 */
@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class Camera2Renderer implements GLSurfaceView.Renderer {
    CameraDevice mDevice = null;
    CameraManager mCameraManager = null;
    private HandlerThread mThread;
    private Handler mHandler;
    private Size mPreviewSize;
    private int cameraId;

    public Camera2Renderer(Context context, int cameraId) {
        this.cameraId = cameraId;
        mCameraManager = (CameraManager) context.getSystemService(CAMERA_SERVICE);
        mThread = new HandlerThread("camera2 ");
        mThread.start();
        mHandler = new Handler(mThread.getLooper());
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        try {
            if (mDevice != null) {
                mDevice.close();
                mDevice = null;
            }
            CameraCharacteristics c = mCameraManager.getCameraCharacteristics(cameraId + "");
            StreamConfigurationMap map = c.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
            Size[] sizes = map.getOutputSizes(SurfaceHolder.class);
            //自定义规则，选个大小
            mPreviewSize = sizes[0];
            mController.setDataSize(mPreviewSize.getHeight(), mPreviewSize.getWidth());

            mCameraManager.openCamera(cameraId + "", new CameraDevice.StateCallback() {
                @Override
                public void onOpened(CameraDevice camera) {
                    mDevice = camera;
                    try {
                        Surface surface = new Surface(mController.getTexture());

                        final CaptureRequest.Builder builder = mDevice.createCaptureRequest(TEMPLATE_PREVIEW);
                        builder.addTarget(surface);
                        mController.getTexture().setDefaultBufferSize(mPreviewSize.getWidth(), mPreviewSize.getHeight());
                        mDevice.createCaptureSession(Arrays.asList(surface), new CameraCaptureSession.StateCallback() {
                            @Override
                            public void onConfigured(CameraCaptureSession session) {
                                session.setRepeatingRequest(builder.build(), new CameraCaptureSession.CaptureCallback() {
                                    @Override
                                    public void onCaptureProgressed(CameraCaptureSession session, CaptureRequest request, CaptureResult partialResult) {
                                        super.onCaptureProgressed(session, request, partialResult);
                                    }

                                    @Override
                                    public void onCaptureCompleted(CameraCaptureSession session, CaptureRequest request, TotalCaptureResult result) {
                                        super.onCaptureCompleted(session, request, result);
                                        mController.requestRender();
                                    }
                                },mHandler);
                            }

                            @Override
                            public void onConfigureFailed(CameraCaptureSession session) {

                            }
                        },mHandler);
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onDisconnected(CameraDevice camera) {
                    mDevice = null;
                }

                @Override
                public void onError(CameraDevice camera, int error) {

                }
            }, mHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {

    }

    @Override
    public void onDrawFrame(GL10 gl) {

    }

//    public void onDestroy() {
//        if (mDevice != null) {
//            mDevice.close();
//            mDevice = null;
//        }
//    }
}
