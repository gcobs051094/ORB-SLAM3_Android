package cn.koistudio.hitomi.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import cn.koistudio.hitomi.R;
import cn.koistudio.hitomi.activity.OpenCV.OpenCVTestActivity;
import cn.koistudio.hitomi.activity.OrbSlam.MonoActivity;

public class MainActivity extends AppCompatActivity {

    private final String TAG = "首頁" ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
    }


    @Override
    protected void onStart() {
        super.onStart();

        checkPermission();

    }

    public void onClickButton(View view)
    {
        if(!checkPermission())
            return;

        if(view.getId()==R.id.HOME_GO_MODULE)
        {
            // 子模块测试
            Intent intent = new Intent(this, ModuleInvoke.class);
            startActivity(intent);
        }
        else if(view.getId()==R.id.HOME_GO_ORBSLAM3)
        {
            Intent intent = new Intent(this, MonoActivity.class);
            startActivity(intent);

        }
    }

    // 确认权限
    private boolean checkPermission()
    {
        if(checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
        {
            Log.d(TAG,"请求权限 <CAMERA>");
            requestPermissions(new String[]{Manifest.permission.CAMERA},1);
            return false ;
        }

        return true ;
    }
    
}