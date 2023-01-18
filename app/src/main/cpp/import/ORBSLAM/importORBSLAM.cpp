#include <jni.h>
#include <android/log.h>
#include <string>

#include <ORBmatcher.h>
#include <ORBVocabulary.h>
#include <System.h>
#include <MapPoint.h>
#include <Tracking.h>
#include <Atlas.h>
#include <ImuTypes.h>

#include <jni.h>

#include <importOpenCV.h>

#include "nViewer.hpp"


static const char* TAG = "ORBSLAM";

extern "C"
JNIEXPORT jlong JNICALL
Java_cn_koistudio_hitomi_module_OrbSlam_SystemMono_nCreateVocabulary(
        JNIEnv *env, jclass clazz,jstring txt_file) {

    // TODO: implement nCreateVocabulary()

    const char* filename_str;
    filename_str = env->GetStringUTFChars(txt_file, 0);
    if(!filename_str) {
        env->ReleaseStringUTFChars(txt_file, filename_str);
        __android_log_print(ANDROID_LOG_ERROR, TAG, "Vocabulary Filename Error");
        return 0;
    }

    ORB_SLAM3::ORBVocabulary* mpVocabulary = new ORB_SLAM3::ORBVocabulary();
    bool bVocLoad = mpVocabulary->loadFromTextFile(filename_str);
    if(!bVocLoad) {
        __android_log_print(ANDROID_LOG_ERROR, TAG, "Vocabulary File Load Error %s",filename_str);
        delete mpVocabulary;
        return 0;
    }

    __android_log_print(ANDROID_LOG_INFO, TAG,"Vocabulary File Load Success %s",filename_str);

    return (long)mpVocabulary;

}


extern "C"
JNIEXPORT void JNICALL
Java_cn_koistudio_hitomi_module_OrbSlam_SystemMono_nDeleteVocabulary(
        JNIEnv *env, jclass clazz,jlong pVocabulary) {

    ORB_SLAM3::ORBVocabulary* mpVocabulary = (ORB_SLAM3::ORBVocabulary*)pVocabulary;
    delete mpVocabulary;
}


extern "C"
JNIEXPORT jlong JNICALL
Java_cn_koistudio_hitomi_module_OrbSlam_SystemMono_nCreateSystemMono(JNIEnv *env, jclass clazz,
                                                                     jstring file_setting,
                                                                     jstring file_orb_voc) {
    // TODO: implement nCreateSystemMono()
    __android_log_print(ANDROID_LOG_INFO, TAG, "Starting Good Luck!");

    const char* filename_setting_str;
    filename_setting_str = env->GetStringUTFChars(file_setting, 0);
    if(!filename_setting_str) {
        env->ReleaseStringUTFChars(file_setting, filename_setting_str);
        __android_log_print(ANDROID_LOG_ERROR, TAG, "Setting Filename Error");
        return 0;
    }

    const char* filename_voctxt_str;
    filename_voctxt_str = env->GetStringUTFChars(file_orb_voc, 0);
    if(!filename_voctxt_str) {
        env->ReleaseStringUTFChars(file_orb_voc, filename_voctxt_str);
        __android_log_print(ANDROID_LOG_ERROR, TAG, "Vocabulary Filename Error");
        return 0;
    }

    __android_log_print(ANDROID_LOG_INFO, TAG, "Camera %s",filename_setting_str);
    __android_log_print(ANDROID_LOG_INFO, TAG, "Voc %s",filename_voctxt_str);

    ORB_SLAM3::System* system = 0;
    try
    {
        system = new ORB_SLAM3::System(filename_voctxt_str, filename_setting_str,
                                       ORB_SLAM3::System::MONOCULAR, false);
    }
    catch(int err)
    {
        __android_log_print(ANDROID_LOG_INFO, TAG, "catch error %d",err);
    }


    env->ReleaseStringUTFChars(file_orb_voc, filename_voctxt_str);
    env->ReleaseStringUTFChars(file_setting, filename_setting_str);

    __android_log_print(ANDROID_LOG_INFO, TAG, "Start Success! %X",system);

    return (long)system;

}

extern "C"
JNIEXPORT jint JNICALL
Java_cn_koistudio_hitomi_module_OrbSlam_SystemMono_nShutdown(JNIEnv *env, jclass clazz,
                                                             jlong p_system) {
    // TODO: implement nShutdown()

    ORB_SLAM3::System* system = (ORB_SLAM3::System*) p_system;
    system->Shutdown();
}

extern "C"
JNIEXPORT void JNICALL
Java_cn_koistudio_hitomi_module_OrbSlam_SystemMono_nDeleteSystemMono(JNIEnv *env, jclass clazz,  jlong p_system) {
    // TODO: implement nDeleteSystemMono()

    ORB_SLAM3::System* system = (ORB_SLAM3::System*) p_system;
    delete system;
}


extern "C"
JNIEXPORT jint JNICALL
Java_cn_koistudio_hitomi_module_OrbSlam_SystemMono_nSystemGetTrackingState(JNIEnv *env,
                                                                           jclass clazz,
                                                                           jlong p_system) {

    // TODO: implement nSystemGetTrackingState()

    ORB_SLAM3::System* system = (ORB_SLAM3::System*) p_system;
    return system->GetTrackingState();


}

extern "C"
JNIEXPORT jlong JNICALL
Java_cn_koistudio_hitomi_module_OrbSlam_SystemMono_nSystemTrackingMono(JNIEnv *env, jclass clazz,
                                                                       jlong p_system,
                                                                       jobject bitmap,
                                                                       jdouble second) {
    // TODO: implement nSystemTrackingMono()

    cv::Mat input = cv::bitmap2Mat(env, bitmap);

    ORB_SLAM3::System* system = (ORB_SLAM3::System*) p_system;
    cv::Mat pose = system->TrackMonocular(input,second);

    // TODO: Fast Draw
    input = frame_draw_fast(&input,system->GetTrackedKeyPointsUn(),cv::Scalar(0,255,0),2.0f);



    cv::mat2Bitmap(env,bitmap,input);

    return 0;

}
extern "C"
JNIEXPORT jfloatArray JNICALL
Java_cn_koistudio_hitomi_module_OrbSlam_SystemMono_nSystemGetCurrentMapPoints(JNIEnv *env,
                                                                              jclass clazz,
                                                                              jlong p_system) {
    // TODO: implement nSystemGetCurrentMapPoints()

    // Debug
    ORB_SLAM3::System* system = (ORB_SLAM3::System*) p_system;
    //cv::Mat pose = system->mpTracker->mCurrentFrame.mTcw ;
    // __android_log_print(ANDROID_LOG_INFO, TAG, "Pose %d %d",pose.rows,pose.cols);
//    if(pose.rows>3&&pose.cols>3)
//    {
//        __android_log_print(ANDROID_LOG_INFO, TAG,
//            "R0 %f %f %f %f",pose.at<float>(0,0),pose.at<float>(0,1),pose.at<float>(0,2),pose.at<float>(0,3));
//        __android_log_print(ANDROID_LOG_INFO, TAG,
//            "R1 %f %f %f %f",pose.at<float>(1,0),pose.at<float>(1,1),pose.at<float>(1,2),pose.at<float>(1,3));
//        __android_log_print(ANDROID_LOG_INFO, TAG,
//            "R2 %f %f %f %f",pose.at<float>(2,0),pose.at<float>(2,2),pose.at<float>(2,2),pose.at<float>(2,3));
//        __android_log_print(ANDROID_LOG_INFO, TAG,
//                            "R3 %f %f %f %f",pose.at<float>(3,0),pose.at<float>(3,2),pose.at<float>(3,2),pose.at<float>(3,3));
//    }
    //

    // TODO: Print Debug
    std::vector<ORB_SLAM3::MapPoint*> vpMPs = system->mpAtlas->GetAllMapPoints();
    __android_log_print(ANDROID_LOG_INFO, TAG, "MapPoints=%d",vpMPs.size());

    std::vector<float> vPoints ;
    for(size_t i=0, iend=vpMPs.size(); i<iend;i++)
    {
        if(vpMPs[i]->isBad())
            continue;
        cv::Mat pos = vpMPs[i]->GetWorldPos();
        vPoints.push_back(pos.at<float>(0));
        vPoints.push_back(-pos.at<float>(1));
        vPoints.push_back(pos.at<float>(2));
    }


    jfloatArray resArr = env->NewFloatArray(vPoints.size());
//    float _data[6];
//    for(int i=0;i<6;i++)
//    {
//        _data[i] = 0.3 * i;
//    }

    env->SetFloatArrayRegion(resArr,0,vPoints.size(),vPoints.data());
    return resArr;

}
extern "C"
JNIEXPORT jfloatArray JNICALL
Java_cn_koistudio_hitomi_module_OrbSlam_SystemMono_nSystemGetCurrentCamPose(JNIEnv *env,
                                                                            jclass clazz,
                                                                            jlong p_system) {
    // TODO: implement nSystemGetCurrentCamPose()
    // Debug
    ORB_SLAM3::System* system = (ORB_SLAM3::System*) p_system;
    cv::Mat pose = system->mpTracker->mCurrentFrame.mTcw ;

    if(pose.rows==4&&pose.cols==4)
    {
        // pose = pose.t();
        jfloatArray resArr = env->NewFloatArray(16);
        float _data[16];

//        for(int r=0;r<4;r++)
//        {
//            for(int c=0;c<4;c++)
//            {
//                _data[r*4+c] = pose.at<float>(r,c);
//            }
//        }

        cv::Mat Rwc(3,3,CV_32F);
        cv::Mat twc(3,1,CV_32F);

        Rwc = pose.rowRange(0,3).colRange(0,3).t();
        twc = -Rwc*pose.rowRange(0,3).col(3);

        _data[0] = Rwc.at<float>(0,0);
        _data[1] = Rwc.at<float>(1,0);
        _data[2] = Rwc.at<float>(2,0);
        _data[3]  = 0.0;

        _data[4] = Rwc.at<float>(0,1);
        _data[5] = Rwc.at<float>(1,1);
        _data[6] = Rwc.at<float>(2,1);
        _data[7]  = 0.0;

        _data[8] = Rwc.at<float>(0,2);
        _data[9] = Rwc.at<float>(1,2);
        _data[10] = -Rwc.at<float>(2,2);
        _data[11]  = 0.0;

        _data[12] = twc.at<float>(0);
        _data[13] = twc.at<float>(1);
        _data[14] = twc.at<float>(2);
        _data[15]  = 1.0;


        env->SetFloatArrayRegion(resArr,0,16,_data);
        return resArr;
    }

    return nullptr;

}
extern "C"
JNIEXPORT jlong JNICALL
Java_cn_koistudio_hitomi_module_OrbSlam_SystemMono_nSystemTrackingMonoIMU(JNIEnv *env, jclass clazz,
                                                                          jlong p_system,
                                                                          jobject bitmap,
                                                                          jdouble second,
                                                                          jdoubleArray point_data) {

    // __android_log_print(ANDROID_LOG_INFO, TAG, ">>>>>>>> 1");

    // TODO: implement nSystemTrackingMonoIMU()

    // 测试减少一半
    cv::Mat input = cv::bitmap2Mat(env, bitmap);
    cv::Mat inputSmall ;
    cv::resize(input,inputSmall,cv::Size(input.cols/2,input.rows/2));


    // TODO: Align data
    int IMUDataLen = env->GetArrayLength(point_data);
    jdouble *pIMUData = env->GetDoubleArrayElements(point_data, NULL);
    __android_log_print(ANDROID_LOG_INFO, TAG, "IMUDataLen=%d", IMUDataLen);
    vector<ORB_SLAM3::IMU::Point> imupoints;


    for(int i=0;i<IMUDataLen/7;i++)
    {
        double ax = pIMUData[i*7+0];
        double ay = pIMUData[i*7+1];
        double az = pIMUData[i*7+2];
        double gx = pIMUData[i*7+3];
        double gy = pIMUData[i*7+4];
        double gz = pIMUData[i*7+5];
        double timestamp = pIMUData[i*7+6];
        ORB_SLAM3::IMU::Point _point(ax,ay,az,gx,gy,gz,timestamp);
        // __android_log_print(ANDROID_LOG_INFO, TAG, ">>>>>>>> 1.5 %lf ",timestamp);
        imupoints.push_back(_point);
    }

    // __android_log_print(ANDROID_LOG_INFO, TAG, ">>>>>>>> 2 %X ",p_system);

    ORB_SLAM3::System* system = (ORB_SLAM3::System*) p_system;

    try
    {
        cv::Mat pose = system->TrackMonocular(inputSmall,second,imupoints);
        // cv::Mat pose = system->TrackMonocular(input,second);

        // __android_log_print(ANDROID_LOG_INFO, TAG, ">>>>>>>> 3");

        // TODO: Fast Draw
        input = frame_draw_fast(&input,system->GetTrackedKeyPointsUn(),cv::Scalar(0,255,0),2.0f);
    }
    catch(int err)
    {
        __android_log_print(ANDROID_LOG_ERROR, TAG, "Track Error %X",err);
    }




    cv::mat2Bitmap(env,bitmap,input);

    return 0;

}

extern "C"
JNIEXPORT void JNICALL
Java_cn_koistudio_hitomi_module_OrbSlam_SystemMono_nReset(JNIEnv *env, jclass clazz,jlong p_system) {
    // TODO: implement nReset()

    ORB_SLAM3::System* system = (ORB_SLAM3::System*) p_system;
    system->Reset();

}