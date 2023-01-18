package cn.koistudio.hitomi.activity.OpenCV;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.media.ImageReader;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.util.concurrent.atomic.AtomicBoolean;

import cn.koistudio.hitomi.R;
import cn.koistudio.hitomi.module.HitomiDebug.SampleHitomiDebug;
import cn.koistudio.hitomi.module.OpenCV.SampleOpenCV;
import cn.koistudio.hitomi.util.uCamera;

public class OpenCVTestActivity extends AppCompatActivity {

    private final String TAG = "OpenCV.Test" ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opencv_test);
        getSupportActionBar().hide();
    }

    private uCamera muCamera = null;

    SampleOpenCV smpleOpenCV = new SampleOpenCV();
    SampleHitomiDebug sampleHitomiDebug = new SampleHitomiDebug();

    @Override
    protected void onStart() {
        super.onStart();

        if(muCamera==null)
        {
            muCamera = new uCamera(this,onImageAvailableListener);
        }

        //SampleOpenCV SsmpleOpenCV = new SampleOpenCV();
        smpleOpenCV.invoke_selfTest();
        mHandleFin = false;
        mHandler.postDelayed(mTask,200);
    }

    // 处理bitmat的线程
    private Handler mHandler = new Handler();
    private boolean mHandleFin = false;
    // private Thread loopBitMap = null;
    private Bitmap mBitmap = null;
    private AtomicBoolean isBitMapUsing = new AtomicBoolean(false);

    private Runnable mTask = new Runnable() {
        @Override
        public void run() {

            if(isBitMapUsing.compareAndSet(false,true))
            {
                // 空闲时操作
                if(mBitmap!=null) {

                    SampleOpenCV sampleOpenCV = new SampleOpenCV();
                    //sampleOpenCV.invoke_cvtGray(mBitmap);
                    sampleHitomiDebug.invoke_orb(mBitmap);
                    ImageView imageView = findViewById(R.id.CAMERA_VIEW);
                    imageView.setImageBitmap(mBitmap);
                    mBitmap = null;
                }

                isBitMapUsing.set(false);
            }

            mHandler.postDelayed(mTask,50);


        }
    };



    @Override
    protected void onStop() {
        super.onStop();
        mHandleFin = true;
        muCamera.close();

    }

    private ImageReader.OnImageAvailableListener onImageAvailableListener = new ImageReader.OnImageAvailableListener()
    {

        @Override
        public void onImageAvailable(ImageReader reader) {
            //Log.d(TAG," onImageAvailable");

            Image image = reader.acquireLatestImage();
            if(image==null)
                return;

            byte[] bytes = new byte[image.getPlanes()[0].getBuffer().limit()] ;
            image.getPlanes()[0].getBuffer().get(bytes);

            Bitmap bitmap = BitmapFactory.decodeByteArray( bytes,0,bytes.length);
            // Log.d(TAG,"H="+bitmap.getHeight()+" W="+bitmap.getWidth()+" Size="+bitmap.);
//            OpenCV_handler(bitmap);
//            Bitmap bitmapL = rotateBitmap(bitmap);

            if(isBitMapUsing.compareAndSet(false,true))
            {
                mBitmap = bitmap;
                Log.d(TAG,"do update");
                isBitMapUsing.set(false);
            }
            else
            {
                Log.i(TAG,"skip update preview");
            }

            // ImageView imageView = findViewById(R.id.CAMERA_VIEW);

//            smpleOpenCV.invoke_cvtGray(bitmap);

            // sampleHitomiDebug.invoke_orb(bitmap);
            // imageView.setImageBitmap(bitmap);


            image.close();
        }

    };



}