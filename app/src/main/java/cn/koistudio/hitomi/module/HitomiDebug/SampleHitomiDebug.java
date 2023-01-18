package cn.koistudio.hitomi.module.HitomiDebug;

import android.graphics.Bitmap;
import android.util.Log;

public class SampleHitomiDebug {

    static private final String TAG = "SampleHitomiDebug 测试";

    // 对上Cmake输出的命名
    static{
        System.loadLibrary("hitomiDebug");
    }

    public void invoke_orb(Bitmap bitmap)
    {
        orbPreview(bitmap);
    }

    native void orbPreview(Bitmap bitmap);

    public void invoke_BowVoc(String filename) { bowVoc(filename); }

    native void bowVoc(String filename);
}
