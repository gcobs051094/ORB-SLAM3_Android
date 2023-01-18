package cn.koistudio.hitomi.module.OpenCV;

import android.graphics.Bitmap;
import android.util.Log;

public class SampleOpenCV {

    static private final String TAG = "SampleOpenCV 测试";

    // 对上Cmake输出的命名
    static{
        System.loadLibrary("opencv");
    }

    public void invoke_selfTest()
    {
        Log.d(TAG, "SelfTest="+selfTest());
    }

    public int invoke_cvtGray(Bitmap bitmap)
    {
        // int res = cvtGray(bitmap);
        int res = cvtDebug(bitmap);
        Log.d(TAG, "cvtGray="+res);
        return res;
    }

    static public void invoke_cvYaml(String filename)
    {
        Log.d(TAG, "invoke cvYaml");
        cvYaml(filename);

    }

    native int selfTest();

    native int cvtGray(Bitmap bitmap);

    static native void cvYaml(String filename);

    native int cvtDebug(Bitmap bitmap);


}
