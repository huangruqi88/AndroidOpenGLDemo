package com.ruqii.com.androidopengldemo.camera;

import android.graphics.Point;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * author:黄汝琪 on 2018/11/14.
 * email:huangruqi88@163.com
 */
public class KikatCamera implements ICamera {

    private Config mConfig;
    private Camera mCamera;
    private CameraSizeComparator mSizeComparator;

    private Camera.Size mPicSize;
    private Camera.Size mPreSize;

    private Point mPictureSize;
    private Point mPreviewSize;

    public KikatCamera() {
        this.mConfig = new Config();
        mConfig.minPreviewWidth = 720;
        mConfig.minPictureWidth = 720;
        mConfig.ratio = 1.778f;
        mSizeComparator = new CameraSizeComparator();
    }

    @Override
    public boolean open(int cameraId) {
        mCamera = Camera.open();
        if (null != mCamera) {
            Camera.Parameters parameters = mCamera.getParameters();
            mPicSize = getPropPictureSize(parameters.getSupportedPictureSizes(), mConfig.ratio,
                    mConfig.minPictureWidth);
            mPreSize = getPropPreviewSize(parameters.getSupportedPreviewSizes(), mConfig.ratio, mConfig
                    .minPreviewWidth);
            parameters.setPictureSize(mPicSize.width, mPicSize.height);
            parameters.setPreviewSize(mPreSize.width, mPreSize.height);
            mCamera.setParameters(parameters);
            Camera.Size pre = parameters.getPreviewSize();
            Camera.Size pic = parameters.getPictureSize();
            mPictureSize = new Point(pic.height, pic.width);
            mPreviewSize = new Point(pre.height, pre.width);

        }
        return false;
    }

    @Override
    public void setConfig(Config config) {
        this.mConfig = config;
    }

    @Override
    public boolean preview() {
        if (null != mCamera) {
            mCamera.startPreview();
        }
        return false;
    }

    @Override
    public boolean switchTo(int cameraId) {
        close();
        open(cameraId);
        return false;
    }

    @Override
    public void takePhoto(TakePhotoCallback callback) {

    }

    @Override
    public boolean close() {
        if (null != mCamera) {
            mCamera.stopPreview();
            mCamera.release();
        }
        return false;
    }

    @Override
    public void setPreviewTexture(SurfaceTexture texture) {
        if (null != mCamera) {
            try {
                mCamera.setPreviewTexture(texture);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public Point getPreSize() {
        return mPictureSize;
    }

    @Override
    public Point getPicSize() {
        return mPreviewSize;
    }

    @Override
    public void setOnPreviewFrameCallback(final PreviewFrameCallback callback) {
        if (null != mCamera) {
            mCamera.setPreviewCallback(new Camera.PreviewCallback() {
                @Override
                public void onPreviewFrame(byte[] data, Camera camera) {
                    callback.onPreviewFrame(data, mPreviewSize.x, mPreviewSize.y);
                }
            });
        }
    }

    public void addBuffer(byte[] buffer){
        if (null != mCamera) {
            mCamera.addCallbackBuffer(buffer);
        }
    }

    public void setOnPreviewFrameCallbackWithBuffer(final PreviewFrameCallback callback){
        if (null != mCamera) {
            mCamera.setPreviewCallbackWithBuffer(new Camera.PreviewCallback() {
                @Override
                public void onPreviewFrame(byte[] data, Camera camera) {
                    callback.onPreviewFrame(data,mPreviewSize.x,mPreviewSize.y);
                }
            });
        }
    }

    /**
     * 比较相机预览的大小
     *
     * @param list
     * @param th
     * @param minWidth
     * @return
     */
    private Camera.Size getPropPreviewSize(List<Camera.Size> list, float th, int minWidth) {
        Collections.sort(list, mSizeComparator);

        int i = 0;
        for (Camera.Size s : list) {
            if ((s.height >= minWidth) && equalRate(s, th)) {
                break;
            }
            i++;
        }
        if (i == list.size()) {
            i = 0;
        }
        return list.get(i);
    }

    /**
     * 比较相机图片的大小
     *
     * @param list
     * @param th
     * @param minWidth
     * @return
     */
    private Camera.Size getPropPictureSize(List<Camera.Size> list, float th, int minWidth) {
        Collections.sort(list, mSizeComparator);

        int i = 0;
        for (Camera.Size s : list) {
            if ((s.height >= minWidth) && equalRate(s, th)) {
                break;
            }
            i++;
        }
        if (i == list.size()) {
            i = 0;
        }
        return list.get(i);
    }

    /**
     * 对比宽高比如果宽高一的
     *
     * @param s    相机的大小
     * @param rate
     * @return
     */
    private boolean equalRate(Camera.Size s, float rate) {
        float r = (float) (s.width) / (float) (s.height);
        if (Math.abs(r - rate) <= 0.03) {
            return true;
        } else {
            return false;
        }
    }

    private class CameraSizeComparator implements Comparator<Camera.Size> {
        @Override
        public int compare(Camera.Size o1, Camera.Size o2) {
            if (o1.height == o2.height) {
                return 0;
            } else if (o1.height > o2.height) {
                return 1;
            } else {
                return -1;
            }
        }
    }
}
