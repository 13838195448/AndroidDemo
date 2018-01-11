package com.stone.androiddemo;

/**
 * Created by Administrator on 2018/1/11.
 */
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.hardware.Camera.PreviewCallback;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Environment;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
public class LocalMedia implements Callback, PreviewCallback
{
    private Camera.Parameters mParameters = null;
    @SuppressLint("InlinedApi")
    private int mCameraId = Camera.CameraInfo.CAMERA_FACING_BACK;
    private Camera camera;
    private SurfaceHolder surfaceHolder;
    private int width = 1280;
    private int height = 720;
    private Activity activity;
    private MediaRecorder mediaRecorder;

    @SuppressLint("NewApi")
    @SuppressWarnings("deprecation")
    public LocalMedia(Activity activity, SurfaceView surfaceView, int cameraId)
    {
        this.activity = activity;
        this.mCameraId = cameraId;
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.setFixedSize(width, height);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        surfaceHolder.addCallback(this);
        //startPreview();
    }

    public Camera getCamera()
    {
        return camera;
    }

    public int getCameraId()
    {
        return mCameraId;
    }

    public void startPreview()
    {
        System.out.println("hhhh");
        if (camera != null)
        {
            camera.startPreview();
            System.out.println("cccc");
        }
    }

    public void stopPreview()
    {
        if (camera != null)
        {
            camera.stopPreview();
            camera.setPreviewCallback(null);
            camera.release();
            camera = null;
        }
    }

    @SuppressLint("NewApi")
    @Override
    public void surfaceCreated(SurfaceHolder holder)
    {
        try
        {
            //if (mCameraId == Camera.CameraInfo.CAMERA_FACING_BACK)
            //{
            //camera = Camera.open();
            //}
            //else
            //{
            camera = Camera.open(mCameraId);
            //}
            if (camera != null)
            {
                /*
                 * List<Size> sizes =
                 * camera.getParameters().getSupportedPreviewSizes(); for(int i
                 * = 0; i < sizes.size(); i++) { Log.e("Preview", "w = " +
                 * sizes.get(i).width + ";h = " + sizes.get(i).height); }
                 */
                /*
                 * List<Size> sizes2 =
                 * camera.getParameters().getSupportedPictureSizes(); for(int i
                 * = 0; i < sizes2.size(); i++) { Log.e("Picture", "w = " +
                 * sizes2.get(i).width + ";h = " + sizes2.get(i).height); }
                 */
                camera.setPreviewDisplay(surfaceHolder);
                camera.setDisplayOrientation(getDispalyOritation(activity,
                        mCameraId));
                mParameters = camera.getParameters();
                mParameters.setPreviewSize(1280, 720);
                mParameters.setPictureSize(640, 480);
                mParameters.setPreviewFormat(ImageFormat.YV12);
                camera.setParameters(mParameters);
                camera.setPreviewCallback(this);
                //startPreview();
                System.out.println("bbbb");
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @SuppressLint("NewApi")
    public void setCameraId(int camreaId)
    {

        try
        {
            camera = Camera.open(camreaId);
            if (camera != null)
            {
                camera.setPreviewDisplay(surfaceHolder);
                camera.setDisplayOrientation(getDispalyOritation(activity,
                        mCameraId));
                mParameters = camera.getParameters();
                mParameters.setPreviewSize(1280, 720);
                mParameters.setPictureSize(640, 480);
                mParameters.setPreviewFormat(ImageFormat.YV12);
                camera.setParameters(mParameters);
                camera.setPreviewCallback(this);
                startPreview();
            }
        }
        catch (Exception e)
        {
            // TODO 自动生成的 catch 块
            e.printStackTrace();
        }

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height)
    {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder)
    {
        if (camera != null)
        {
            camera.setPreviewCallback(null);
            camera.stopPreview();
            camera.release();
            camera = null;
        }
    }

    @Override
    public void onPreviewFrame(byte[] data, Camera camera)
    {

    }

    public void takePicture()
    {
        camera.takePicture(mshutter, null, MJpeg);
    }

    public Camera.PictureCallback MJpeg = new Camera.PictureCallback()
    {
        @SuppressLint("InlinedApi")
        @Override
        public void onPictureTaken(byte[] data, Camera camera)
        {
            try
            {
                Bitmap bm = BitmapFactory.decodeByteArray(data, 0, data.length);
                Matrix matrix = new Matrix();
                matrix.postRotate(getDispalyOritation(activity, mCameraId));
                mParameters.setPreviewFormat(ImageFormat.YV12);
                bm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(),
                        bm.getHeight(), matrix, true);

                Date date = new Date();
                SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss"); // 格式化时间
                String filename = Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+format.format(date) + ".jpg";
//
//                String jpgFile = Environment.getExternalStorageDirectory()
//                        .getAbsolutePath() + ".jpg";
                System.out.println("目录==="+filename);
                File file = new File(filename);
                FileOutputStream fos = new FileOutputStream(file);
                bm.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                if (camera != null)
                {
                    camera.startPreview();
                }
                fos.close();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    };

    public Camera.ShutterCallback mshutter = new Camera.ShutterCallback()
    {
        @Override
        public void onShutter()
        {

        }
    };

    @SuppressLint("NewApi") public void startRecord()
    {
        mediaRecorder = new MediaRecorder();
        if (camera != null)
        {
            mediaRecorder.setCamera(camera);

            mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);

            // mediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);

            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
            // 选择当前终端适合的预览size
            /*
             * List<Size> sizes1 =
             * camera.getParameters().getSupportedVideoSizes(); for(int i = 0; i
             * < sizes1.size(); i++) { Log.e("Video", "w = " +
             * sizes1.get(i).width + ";h = " + sizes1.get(i).height); }
             */
            mediaRecorder.setVideoSize(width, height);// 设置分辨率 防止录制的视频出现花屏

            // mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);

            mediaRecorder.setOrientationHint(getDispalyOritation(activity,
                    mCameraId));
            mediaRecorder.setOutputFile(Environment.getExternalStorageDirectory()
                    .getAbsolutePath() + "/b.mkv");
            /*
             * mediaRecorder
             * .setOutputFile(Environment.getExternalStorageDirectory()
             * .getAbsolutePath() + "/b.mkv");
             */
            try
            {
                camera.setDisplayOrientation(getDispalyOritation(activity,
                        mCameraId));
                camera.unlock();
                mediaRecorder.prepare();
                mediaRecorder.start();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

        }

    }

    public void stopRecord()
    {
        if (mediaRecorder != null)
        {
            try
            {
                mediaRecorder.stop();
                mediaRecorder.release();
                mediaRecorder = null;
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    @SuppressLint("NewApi")
    private int getDispalyOritation(Activity activity, int cameraId)
    {
        int degrees = activity.getWindowManager().getDefaultDisplay()
                .getRotation();
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(cameraId, info);
        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT)
        {
            degrees = getDisplayFrontRotation(degrees);
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;
        }
        else
        {
            degrees = getDisplayBackRotation(degrees);
            result = (info.orientation - degrees + 360) % 360;
        }
        return result;
    }

    private int getDisplayBackRotation(int degrees)
    {
        switch (degrees)
        {
            case Surface.ROTATION_0:
                return 0;
            case Surface.ROTATION_90:
                return 90;
            case Surface.ROTATION_180:
                return 180;
            case Surface.ROTATION_270:
                return 190;
            default:
                break;
        }
        return 0;
    }

    private int getDisplayFrontRotation(int degrees)
    {
        switch (degrees)
        {
            case Surface.ROTATION_0:
                return 180;
            case Surface.ROTATION_90:
                return 270;
            case Surface.ROTATION_180:
                return 0;
            case Surface.ROTATION_270:
                return 90;
            default:
                break;
        }
        return 0;
    }
}