package cn.koistudio.hitomi.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.ImageFormat;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.ImageReader;
import android.util.Log;
import android.util.Size;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.util.Arrays;


public class uCamera {

    private final String TAG = "uCamera" ;
    private ImageReader imageReader ;

    private boolean active = true ;
    public boolean isActive()
    {
        return active ;
    }
    public int height = -1;
    public int width = -1;

    public void close()
    {
        if(!active)
            return;

        camera.close();
    }


    // param.listener : 用于监听预览
    @SuppressLint("MissingPermission")
    public uCamera(Context context, ImageReader.OnImageAvailableListener listener) {


        try {

            CameraManager manager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);

            // TODO: query 获取所有照相机ID, 直接选择首个照相机
            String[] ids = manager.getCameraIdList();
            if (ids == null || ids.length == 0) {
                Log.e(TAG, "Camera No Available");
                throw new IOException();
            }


            // TODO: query 摄像头尺寸
            CameraCharacteristics cameraCharacteristics = manager.getCameraCharacteristics(ids[0]);
            StreamConfigurationMap streamConfigurationMap = cameraCharacteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
            Size[] sizes = streamConfigurationMap.getOutputSizes(ImageFormat.JPEG);
            int beatOf = 0;
            for (int i = 0; i < sizes.length; i++) {

                // TODO: 优先选最小尺寸
                Log.d(TAG, "Support DPI " + sizes[i].getHeight() + " X " + sizes[i].getWidth());
                if (sizes[i].getHeight() < sizes[beatOf].getHeight())
                    beatOf = i;

                // TODO: 优先选720px
                if (sizes[i].getHeight() == 720 && sizes[i].getWidth() == 1280) {
                    beatOf = i;
                    break;
                }
            }

            this.width = sizes[beatOf].getWidth();
            this.height = sizes[beatOf].getHeight();
            Log.i(TAG, "Use DPI " + height + " X " + width);

            // TODO: create 图像获取接口
            imageReader = ImageReader.newInstance(sizes[beatOf].getWidth(), sizes[beatOf].getHeight(), ImageFormat.JPEG, 2);
            imageReader.setOnImageAvailableListener(listener, null);

            // TODO: start 照相机
            manager.openCamera(ids[0], stateCallback, null);

        }catch (Exception e){

            e.printStackTrace();
            active = false ;
        }

    }

    private CameraDevice camera ;
    /**
     * 回调 照相机状态
     */
    private final CameraDevice.StateCallback stateCallback = new CameraDevice.StateCallback() {


        @Override
        public void onOpened(@NonNull CameraDevice cameraDevice) {
            Log.i(TAG, "State == Opened");

            camera = cameraDevice ;

            try
            {
                // TODO: 开始预览
                start_preview();
            }
            catch (Exception e)
            {
                e.printStackTrace();
                active = false ;
            }
        }

        @Override
        public void onDisconnected(@NonNull CameraDevice camera) {
            Log.d(TAG, "State == Disconnected");
            active = false ;
        }

        @Override
        public void onError(@NonNull CameraDevice camera, int error) {
            Log.d(TAG, "State == Error");
            active = false ;
        }
    };

    private CameraCaptureSession captureSession ;

    private CameraCaptureSession.CaptureCallback captureCallback
            = new CameraCaptureSession.CaptureCallback(){
    };

    // 请求
    CaptureRequest.Builder previewRequestBuilder ;

    /**
     * 请求开始预览
     * @throws Exception
     */
    private void start_preview() throws Exception
    {

        // TODO: build request
        previewRequestBuilder = camera.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
        previewRequestBuilder.addTarget(imageReader.getSurface()); // 输出到 imageReader

        // TODO: create session
        camera.createCaptureSession(Arrays.asList(imageReader.getSurface()), new CameraCaptureSession.StateCallback() {

            @Override
            public void onConfigured(@NonNull CameraCaptureSession session) {

                Log.d(TAG,"OK createCaptureSession cb-> <onConfigured>");
                captureSession = session ;

                try
                {
                    session.setRepeatingRequest(
                            previewRequestBuilder.build(),
                            captureCallback,
                            null);

                }
                catch (CameraAccessException e)
                {
                    e.printStackTrace();

                    active = false ;
                    camera.close();
                }
            }

            @Override
            public void onConfigureFailed(@NonNull CameraCaptureSession session)
            {
                Log.d(TAG,"ER createCaptureSession cb-> <onConfigureFailed> ");
                active = false ;
                camera.close();
            }

        }, null);

    }

}
