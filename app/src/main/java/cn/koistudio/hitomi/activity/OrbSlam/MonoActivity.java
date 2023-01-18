package cn.koistudio.hitomi.activity.OrbSlam;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.media.ImageReader;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import cn.koistudio.hitomi.R;
import cn.koistudio.hitomi.module.OrbSlam.SystemMono;
import cn.koistudio.hitomi.util.uIMU;
import cn.koistudio.hitomi.util.uAssets;
import cn.koistudio.hitomi.util.uCamera;

public class MonoActivity extends AppCompatActivity {

    static private final String TAG = "MonoActivity" ;
    private AppCompatActivity mContext = this;
    private GLSurfaceView glSurfaceView ;
    private MapRender mMapRender = new MapRender();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mono);
        getSupportActionBar().hide();

        glSurfaceView = (GLSurfaceView)findViewById(R.id.SLAM_MAP);
        glSurfaceView.setEGLContextClientVersion(2);
        glSurfaceView.setRenderer(mMapRender);
        glSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
        
    }

    //
    private uIMU muIMU = null;

    // IMG Buffer
    private FpsCounter mFpsCounter = new FpsCounter();
    private uCamera mCamera = null;
    private Bitmap tmBitmap = null;
    private double tmTimestamp = 0;
    private AtomicBoolean bSystemMut = new AtomicBoolean(false);

    @Override
    protected void onStart() {
        super.onStart();
        mCamera = new uCamera(this,onImageAvailableListener);
        mHandler.postDelayed(mRunTrack,1000);
        muIMU = new uIMU(this);

    }

    @Override
    protected void onStop() {
        super.onStop();
        mCamera.close();
        mCamera = null;
        muIMU.close();
        muIMU = null ;
    }

    @Override
    protected void onPause() {
        super.onPause();
        glSurfaceView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //glSurfaceView.onResume();
    }

    private ImageReader.OnImageAvailableListener onImageAvailableListener = new ImageReader.OnImageAvailableListener()
    {

        @Override
        public void onImageAvailable(ImageReader reader) {

            Image image = reader.acquireLatestImage();
            if(image==null)
                return;

            // Get Bitmap
            byte[] bytes = new byte[image.getPlanes()[0].getBuffer().limit()] ;
            image.getPlanes()[0].getBuffer().get(bytes);
            Bitmap bitmap = BitmapFactory.decodeByteArray( bytes,0,bytes.length);

            // double timestampSec = 1.0*image.getTimestamp()/1000/1000 ;
            double timestampSec = 1.0 * System.currentTimeMillis() / 1000 ;

            // Buffer Bitmap
            if(bSystemMut.compareAndSet(false,true)) {
                tmBitmap = bitmap;
                tmTimestamp = timestampSec;
                // Log.v(TAG,"Camera Update IMG ");
                bSystemMut.set(false);
            }
            else {
                Log.w(TAG,"Camera Skip IMG");
            }

            image.close();
        }

    };


    /**
     * 处理照相机数据的线程
     */
    private Handler mHandler = new Handler();
    private Runnable mRunTrack = new Runnable() {
        @Override
        public void run() {

            // TODO: 姿态数据
            List<double[]> _imu = muIMU.ppStream(-1,0,0,0);

            // TODO: 等原子变量
            if(bSystemMut.compareAndSet(false,true))
            {

                if(tmBitmap!=null) {

                    if(mSystem!=null)
                    {
                        if(mSystemStage=="run") {

                            // TODO: 输入图像
                            // mSystem.TrackingMono(tmBitmap,tmTimestamp);
                            mSystem.TrackingMonoIMU(tmBitmap, tmTimestamp, _imu);

                            // TODO: set 现在照相机位置
                            mMapRender.setCameraMatrix(mSystem.mPose);

                            // TODO: set 需要绘制的点
                            mMapRender.setCoords(mSystem.mMapPoints);

                            // TODO: 重新渲染
                            glSurfaceView.requestRender();
                        }

                        mSystemTrackState = mSystem.getTrackingStateInt();
                        ((TextView)findViewById(R.id.SLAM_MESSAGE)).setText(""+mSystem.getTrackingStateStringCN());

                    }
                    else
                    {
                        ((TextView)findViewById(R.id.SLAM_MESSAGE)).setText("未啟動");
                    }

                    // TODO: 左边窗口画图
                    ImageView imageView = findViewById(R.id.SLAM_IMG_CAM);
                    imageView.setImageBitmap(tmBitmap);
                    tmBitmap = null;

                    // TODO: 提示处理的帧率
                    ((TextView)findViewById(R.id.SLAM_STATE)).setText(""+mFpsCounter.update()+"fps");



                }

                bSystemMut.set(false);


            }

            // TODO: 初始化时间较长 按钮加点动态效果
            if(mSystemStage=="init")
            {
                Button _btn = findViewById(R.id.SLAM_START);
                long dots = (System.currentTimeMillis() % 2000) / 500;
                switch ((int) dots) {
                    case 1:
                        _btn.setText(".啟動中.");
                        break;
                    case 2:
                        _btn.setText("..啟動中..");
                        break;
                    case 3:
                        _btn.setText("...啟動中...");
                        break;
                    default:
                        _btn.setText("啟動中");
                }

            }

            if(mCamera!=null)
            {
                mHandler.postDelayed(mRunTrack,50);
            }


        }
    };

    private SystemMono mSystem    = null   ;
    private String mSystemStage   = "null" ;
    private int mSystemTrackState = 0;

    private Runnable mTaskInitSystem = new Runnable() {

        @Override
        public void run() {

            // Prepare File
            String filenameVoc = uAssets.prepareAsset(mContext,"ORBvoc.txt");
            String filenameParam = uAssets.prepareAsset(mContext,"CameraParam.yaml");

            if(filenameVoc!=null||filenameParam!=null)
            {
                mSystem = new SystemMono(mCamera,filenameVoc,filenameParam);
                mSystemStage = "run";

                // TODO: 通知用户
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast hint = Toast.makeText(mContext,"初始化完成",Toast.LENGTH_SHORT);
                        hint.show();
                        ((Button)(mContext.findViewById(R.id.SLAM_START))).setText("停止");
                    }
                });

            }
            else
            {
                Log.e(TAG,"資源文件加載失敗");
                mSystemStage = "null";
            }


        }
    };

    private void mSystemReset()
    {
        while(!bSystemMut.compareAndSet(false,true)) {

            try {
                Thread.sleep(2);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if(mSystem!=null){
            mSystem.reset();
        }


        bSystemMut.set(false);
    }



    public void onClickStart(View view)
    {

        if(mSystemStage=="null")
        {
            // TODO: 启动新的线程资源来初始化SLAM
            Thread thread = new Thread(mTaskInitSystem);
            thread.start();

            mSystemStage = "init";
            Toast hint = Toast.makeText(this,"開始初始化",Toast.LENGTH_LONG);
            hint.show();
        }
        else if(mSystemStage=="init")
        {
            Toast hint = Toast.makeText(this,"請等待初始化完成",Toast.LENGTH_LONG);
            hint.show();
        }
        else if(mSystemStage=="run")
        {
            mSystemStage = "stop";
            ((Button)findViewById(R.id.SLAM_START)).setText("繼續");
        }
        else if(mSystemStage=="stop")
        {
            mSystemStage = "run";
            ((Button)findViewById(R.id.SLAM_START)).setText("停止");
            if(mSystemStage=="run"||mSystemStage=="stop") {
                mSystemReset();
            }
        }

    }

    public void onClickReset(View view)
    {
        if(mSystemStage=="run"||mSystemStage=="stop") {
            mSystemReset();
        }
    }

    public void onClickSight(View view)
    {
        if(view.getId()==R.id.SLAM_VIEW_FOLLOW)
            mMapRender.switchFollow(0);

        if(view.getId()==R.id.SLAM_VIEW_TOP)
            mMapRender.switchTop(0);

        if(view.getId()==R.id.SLAM_VIEW_EX)
            mMapRender.switchDist(1);
        if(view.getId()==R.id.SLAM_VIEW_TR)
            mMapRender.switchDist(-1);
    }



}

class FpsCounter
{

    long mStartSec = 0;
    int mCount = 0;
    int mFpsLast = 0;

    int update()
    {
        long _sec = System.currentTimeMillis()/1000;
        mCount ++ ;

        if(_sec!=mStartSec)
        {
            mStartSec = _sec;
            mFpsLast = mCount;
            mCount = 0;
        }

        return  mFpsLast;

    }


}