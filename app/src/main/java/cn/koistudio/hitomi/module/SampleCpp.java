package cn.koistudio.hitomi.module;

import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

public class SampleCpp {

    private final String TAG = "SamoleCpp 测试";

    // 对上Cmake输出的命名
    static{
        System.loadLibrary("SampleCpp");
    }


    public void invoke_all()
    {
        // hello world 返回
        Log.d(TAG, "Int="+getInt(1024));
        Log.d(TAG, "String="+getString("Hitomi"));

//        // 文件路径测试
//        activity.getPackageResourcePath()

        // 测试对象申请
        pmMalloc = createMalloc();
        Log.d(TAG, "Malloc="+pmMalloc);
        releaseMalloc(pmMalloc);
    }


    public long malloc ;
    native int getInt(int input);
    native String getString(String input);

    private long pmMalloc = 0 ;
    native long createMalloc();
    native void releaseMalloc(long address);

}
