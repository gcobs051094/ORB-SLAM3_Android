package cn.koistudio.hitomi.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import cn.koistudio.hitomi.R;
import cn.koistudio.hitomi.activity.OpenCV.OpenCVTestActivity;
import cn.koistudio.hitomi.module.HitomiDebug.SampleHitomiDebug;
import cn.koistudio.hitomi.module.SampleCpp;
import cn.koistudio.hitomi.util.uAssets;

public class ModuleInvoke extends AppCompatActivity {

    private final String TAG = "ModuleInvoke";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_module_invoke);
        getSupportActionBar().hide();
    }

    public void onClick_SampleCpp(View view)
    {
        SampleCpp sampleCpp = new SampleCpp();
        sampleCpp.invoke_all();
    }

    public void onClick_File(View view)
    {
        String fileName = uAssets.prepareAsset(this,"ORBvoc.txt");
        Log.w(TAG,"uAssets.prepareAsset="+fileName);
        if(fileName!=null)
        {
            //uAssets.fileIoTest(fileName);
            //SampleOpenCV.invoke_cvYaml(fileName);
            SampleHitomiDebug sampleHitomiDebug = new SampleHitomiDebug();
            sampleHitomiDebug.invoke_BowVoc(fileName);
        }
//        SampleHitomiDebug sampleHitomiDebug = new SampleHitomiDebug();
//        sampleHitomiDebug.invoke_BowVoc(fileName);

    }

    public void onClick_OpenCV(View view)
    {
        Intent intent = new Intent(this, OpenCVTestActivity.class);
        startActivity(intent);

    }



}