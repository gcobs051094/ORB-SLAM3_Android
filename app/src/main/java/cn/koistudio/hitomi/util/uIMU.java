package cn.koistudio.hitomi.util;

import static androidx.core.content.ContextCompat.getSystemService;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class uIMU {

    private final String TAG = "uAcceleroMeter" ;

    private boolean active = true ;
    public boolean isActive()
    {
        return active ;
    }

    private Context mContext = null;
    private SensorManager mSensorManager = null;
    private Sensor mSensorAcc = null;
    private Sensor mSensorGyr = null;

    private double[] mPoint = null;
    private List<double[]> mIMUBuffer = new LinkedList<>();
    public synchronized List<double[]> ppStream(int type,double x,double y,double z)
    {

        if(type==Sensor.TYPE_ACCELEROMETER)
        {
            mPoint = new double[7] ;
            mPoint[0] = x ;
            mPoint[1] = y ;
            mPoint[2] = z ;
            mPoint[6] = 1.0 * System.currentTimeMillis() / 1000 ; ;
        }
        else if(type==Sensor.TYPE_GYROSCOPE)
        {
            if(mPoint!=null)
            {
                mPoint[3] = x ;
                mPoint[4] = y ;
                mPoint[5] = z ;
                mIMUBuffer.add(mPoint);

                // Log.i(TAG,"push IMU Buffered="+mIMUBuffer.size());
                if(mIMUBuffer.size()>64)
                    mIMUBuffer.remove(0);

            }
        }
        else
        {
            List<double[]> res = new ArrayList<double[]>(mIMUBuffer);
            mIMUBuffer.clear();
            return res;
        }

        return null;
    }


    public uIMU(Context context)
    {
        mContext = context;
        mSensorManager = getSystemService(mContext,SensorManager.class);

        mSensorAcc = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(
                mSensorEventListenerAcc,
                mSensorAcc,
                SensorManager.SENSOR_DELAY_GAME);

        mSensorGyr = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        mSensorManager.registerListener(
                mSensorEventListenerGyr,
                mSensorGyr,
                SensorManager.SENSOR_DELAY_GAME);
    }

    SensorEventListener mSensorEventListenerAcc = new SensorEventListener(){

        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            //Log.i(TAG,"onSensorChanged "+sensorEvent.values.length);
            double x = sensorEvent.values[0];
            double y = sensorEvent.values[1];
            double z = sensorEvent.values[2];

            //Log.i(TAG,"Acc Changed "+ sensorEvent.values.length +" x="+ x + " y=" + y + " z=" + z);
            ppStream(Sensor.TYPE_ACCELEROMETER,y,x,z);

        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {
            //Log.i(TAG,"onAccuracyChanged");

        }
    };

    SensorEventListener mSensorEventListenerGyr = new SensorEventListener(){

        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            //Log.i(TAG,"onSensorChanged "+sensorEvent.values.length);
            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];
            float z = sensorEvent.values[2];

            ppStream(Sensor.TYPE_GYROSCOPE,y,x,z);

            //Log.i(TAG,"Gyr Changed "+ sensorEvent.values.length +" x="+ x + " y=" + y + " z=" + z);
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {
            //Log.i(TAG,"onAccuracyChanged");

        }
    };

    public void close()
    {
        if(!active)
            return;

        mSensorManager.unregisterListener(mSensorEventListenerAcc);
    }

}
