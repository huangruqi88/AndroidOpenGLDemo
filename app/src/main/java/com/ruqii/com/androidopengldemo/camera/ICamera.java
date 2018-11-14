package com.ruqii.com.androidopengldemo.camera;

import android.graphics.Point;
import android.graphics.SurfaceTexture;

/**
 * @author:黄汝琪
 * @date 2018/11/14.
 * email:huangruqi88@163.com
 */
public interface ICamera {
    /**
     * 打开相机
     *
     * @param cameraId 相机的ID(即打开前置相机还是后置相机)
     * @return
     */
    boolean open(int cameraId);

    /**
     * 设置配置信息
     *
     * @param config
     */
    void setConfig(Config config);

    /**
     * 是否开启相机预览
     *
     * @return
     */
    boolean preview();

    /**
     * 切换摄像头（例如：将前置摄像头切换为后置或者反之）
     *
     * @param cameraId
     * @return
     */
    boolean switchTo(int cameraId);

    /**
     * 拍照
     *
     * @param callback
     */
    void takePhoto(TakePhotoCallback callback);

    /**
     * 关闭相机
     *
     * @return
     */
    boolean close();

    /**
     * 设置预览贴纸
     *
     * @param texture
     */
    void setPreviewTexture(SurfaceTexture texture);

    /**
     * 获取预览的大小
     */
    Point getPreSize();

    /**
     * 获取照片的大小
     *
     * @return
     */
    Point getPicSize();

    void setOnPreviewFrameCallback(PreviewFrameCallback callback);

    /**
     * 相机的配置项
     */
    class Config {
        /**
         * 宽高比
         */
        float ratio;
        int minPreviewWidth;
        int minPictureWidth;
    }

    interface TakePhotoCallback {
        /**
         * 拍照片
         *
         * @param bytes
         * @param width
         * @param height
         */
        void onTakePhoto(byte[] bytes, int width, int height);
    }

    interface PreviewFrameCallback {
        /**
         * 预览
         *
         * @param bytes
         * @param width
         * @param height
         */
        void onPreviewFrame(byte[] bytes, int width, int height);
    }
}
