#include <jni.h>
#include <android/log.h>
#include <android/bitmap.h>
#include <opencv2/core.hpp>
#include "opencv2/imgproc.hpp"
#include <string>

#include "importOpenCV.h"

using namespace cv;





extern "C"
JNIEXPORT jint JNICALL
Java_cn_koistudio_hitomi_module_OpenCV_SampleOpenCV_selfTest(JNIEnv *env, jobject thiz) {
    // TODO: implement selfTest()

    __android_log_print(ANDROID_LOG_INFO, "OpenCV", "selfTest entry");

    cv::Mat mat = cv::Mat::ones(8, 8, CV_32S);
    return mat.at<int>(2, 2);
}

extern "C"
JNIEXPORT jint JNICALL
Java_cn_koistudio_hitomi_module_OpenCV_SampleOpenCV_cvtGray(JNIEnv *env, jobject thiz,jobject bitmap) {
    // TODO: implement cvtGray()

    AndroidBitmapInfo info;
    void *pixels = 0;
    //Mat &dst = *((Mat *) m_addr);
    Mat dst;

    if (AndroidBitmap_getInfo(env, bitmap, &info) < 0)
        return -1;

    if (info.format != ANDROID_BITMAP_FORMAT_RGBA_8888 &&
        info.format != ANDROID_BITMAP_FORMAT_RGB_565)
        return -2;

    if (AndroidBitmap_lockPixels(env, bitmap, &pixels) < 0)
        return -3;

    if (!pixels)
        return -4;

    dst.create(info.height, info.width, CV_8UC4);
    if (info.format == ANDROID_BITMAP_FORMAT_RGBA_8888) {
        //LOGD("nBitmapToMat: RGBA_8888 -> CV_8UC4");
        Mat tmp(info.height, info.width, CV_8UC4, pixels);
        if (false)
            cvtColor(tmp, dst, COLOR_mRGBA2RGBA);
        else
            tmp.copyTo(dst);

        cvtColor(dst, dst, CV_RGBA2GRAY);
        cvtColor(dst, tmp, CV_GRAY2RGBA);
        //dst.copyTo(tmp);
    } else {
        // info.format == ANDROID_BITMAP_FORMAT_RGB_565
        // LOGD("nBitmapToMat: RGB_565 -> CV_8UC4");
        Mat tmp(info.height, info.width, CV_8UC2, pixels);
        cvtColor(tmp, dst, COLOR_BGR5652RGBA);
        cvtColor(dst, dst, CV_RGB2GRAY);
        cvtColor(dst, tmp, CV_GRAY2BGR565);
        //dst.copyTo(tmp);
    }


    AndroidBitmap_unlockPixels(env, bitmap);


    return 0;

}

extern "C"
JNIEXPORT void JNICALL
Java_cn_koistudio_hitomi_module_OpenCV_SampleOpenCV_cvYaml(JNIEnv *env, jclass clazz,
                                                           jstring filename) {
    // TODO: implement cvYaml()

    const char *filename_str;
    filename_str = env->GetStringUTFChars(filename, 0);
    if (!filename_str) {
        env->ReleaseStringUTFChars(filename, filename_str);
        __android_log_print(ANDROID_LOG_ERROR, "cvYaml", "error");
        return;
    }

    cv::FileStorage fsSettings(filename_str, cv::FileStorage::READ);
    if (!fsSettings.isOpened()) {
        __android_log_print(ANDROID_LOG_ERROR, "cvYaml", "open fail");
    } else {
        __android_log_print(ANDROID_LOG_INFO, "cvYaml", "open success");
        std::string sCameraName = fsSettings["Camera.type"];
        __android_log_print(ANDROID_LOG_INFO, "cvYaml", "Camera.type=%s", sCameraName.c_str());

    }

    env->ReleaseStringUTFChars(filename, filename_str);

}


extern "C"
JNIEXPORT jint JNICALL
Java_cn_koistudio_hitomi_module_OpenCV_SampleOpenCV_cvtDebug(JNIEnv *env, jobject thiz, jobject bitmap) {
    // TODO: implement cvtDebug()

    cv::Mat inputImg = bitmap2Mat(env,bitmap);


    if(!inputImg.empty()) {
        cv::cvtColor(inputImg,inputImg,CV_RGB2GRAY);
        cv::cvtColor(inputImg,inputImg,CV_GRAY2RGB);
        mat2Bitmap(env,bitmap,inputImg);
    }
    else {
        __android_log_print(ANDROID_LOG_ERROR, "OpenCV", "bitmap2Mat");
    }

    return 0 ;

}