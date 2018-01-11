package com.stone.androiddemo;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.hardware.Camera;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity implements View.OnClickListener {
    private SurfaceView surfaceView;
    private Button changge, preview, capture, record;
    private LocalMedia surfaceCallback;
    private boolean startPreview = false;
    private boolean recordPreview = false;
    private boolean isBack = true;
    private static Camera mCamera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initSurface();
    }

    private void initView() {
        surfaceView = (SurfaceView) findViewById(R.id.surfaceView);
        surfaceView.setOnClickListener(this);
        changge = (Button) findViewById(R.id.changge);
        changge.setOnClickListener(this);
        preview = (Button) findViewById(R.id.preview);
        preview.setOnClickListener(this);
        capture = (Button) findViewById(R.id.capture);
        capture.setOnClickListener(this);
        record = (Button) findViewById(R.id.record);
        record.setOnClickListener(this);
    }

    @SuppressLint("InlinedApi")
    private void initSurface() {
        surfaceCallback = new LocalMedia(this, surfaceView, Camera.CameraInfo.CAMERA_FACING_BACK);
    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    @SuppressLint("InlinedApi")
    @Override
    public void onClick(View v) {
        // TODO 自动生成的方法存根
        switch (v.getId()) {
            case R.id.changge:
                if (startPreview) {
                    if (isBack) {
                        changge.setText("前置摄像头");
                        surfaceCallback.stopPreview();
                        surfaceCallback.setCameraId(Camera.CameraInfo.CAMERA_FACING_FRONT);
                    } else {
                        changge.setText("后置摄像头");
                        surfaceCallback.stopPreview();
                        surfaceCallback.setCameraId(Camera.CameraInfo.CAMERA_FACING_BACK);

                    }
                    isBack = !isBack;
                } else {
                    Toast.makeText(this, "请先预览", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.preview:

                Log.e("aaaaaaaaa", "aaaaaaa");
                if (!startPreview) {

                    //surfaceCallback = new LocalMedia(this, surfaceView, Camera.CameraInfo.CAMERA_FACING_BACK);
                    surfaceCallback.startPreview();
                    preview.setText("停止");
                } else {
                    surfaceCallback.stopPreview();
                    preview.setText("播放");
                }
                startPreview = !startPreview;
                break;
            case R.id.capture:

                //                surfaceCallback.getCamera().takePicture(surfaceCallback.mshutter,
                //                                                                null, surfaceCallback.MJpeg);

                mHandler.sendEmptyMessageDelayed(1, 5000);
                //                final Handler handler = new Handler(){};
                //                Runnable runnable = new Runnable(){
                //                    @Override
                //                    public void run() {
                //                        // TODO Auto-generated method stub
                //                        // 在此处添加执行的代码
                //                                        surfaceCallback.getCamera().takePicture(surfaceCallback.mshutter,   null, surfaceCallback.MJpeg);
                //                                        System.out.println("执行了吗?");
                ////                        handler.postDelayed(this, 50);// 50是延时时长
                //                    }
                //                };
                //                handler.postDelayed(runnable, 50);// 打开定时器，执行操作
                //                handler.removeCallbacks(runnable);// 关闭定时器处理
                break;
            case R.id.record:
                if (!recordPreview) {
                    record.setText("录制中");
                    surfaceCallback.startRecord();
                } else {
                    record.setText("录制结束");
                    Toast.makeText(this, "停止录制", Toast.LENGTH_SHORT).show();
                    surfaceCallback.stopRecord();
                }
                recordPreview = !recordPreview;
                break;
            default:
                break;
        }
    }


    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case 1:
                    surfaceCallback.getCamera().takePicture(surfaceCallback.mshutter,
                            null, surfaceCallback.MJpeg);
                    mHandler.sendEmptyMessageDelayed(1, 5000);
                    break;
            }

        }
    };


}
