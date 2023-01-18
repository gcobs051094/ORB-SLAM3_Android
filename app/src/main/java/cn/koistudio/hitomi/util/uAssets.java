package cn.koistudio.hitomi.util;

import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class uAssets {

    
    static private final String TAG = "uAssets" ;

    static{
        System.loadLibrary("SampleCpp");
    }

    public static String prepareAsset(AppCompatActivity activity,String filename)
    {
        try
        {

            // 准备复制
            InputStream assetIs = activity.getAssets().open(filename);
            File assetsCp = new File(activity.getFilesDir()+"/"+filename);

            // ++ 检查目录
            if(assetsCp.exists())
            {
                Log.d(TAG,"File Exist "+assetsCp.length()+" Bytes");
                return assetsCp.getAbsolutePath();
            }

            assetsCp.createNewFile();
            FileOutputStream assetsCpFos = new FileOutputStream(assetsCp);

            // 读写缓存
            byte[] _buffer = new byte[8192];
            int _total= 0 ;
            while (true)
            {
                int readCount = assetIs.read(_buffer,0,_buffer.length);
                _total += readCount;
                assetsCpFos.write(_buffer,0, readCount);
                if(readCount<_buffer.length)
                {
                    Log.d(TAG,"Copy File "+_total+"Bytes");
                    break;
                }
                else
                {
                    Log.d(TAG,"Copy File "+_total+"/? Bytes");
                }
            }
            assetsCpFos.close();

            Log.d(TAG,"File Size="+assetsCp.length());

            return assetsCp.getAbsolutePath();

        }
        catch (IOException e)
        {
            e.printStackTrace();
            return null;
        }
    }


    public static void fileIoTest(String filename)
    {
        fread(filename);
    }

    static native void fread(String filename);

}
