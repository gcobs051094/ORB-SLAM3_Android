#ifndef HITOMI_IMPORTOPENCV_H
#define HITOMI_IMPORTOPENCV_H

#include <jni.h>
#include <android/log.h>
#include <android/bitmap.h>
#include <opencv2/core.hpp>
#include "opencv2/imgproc.hpp"
#include <string>

namespace cv {

    int mat2Bitmap(JNIEnv *env, jobject bitmap, cv::Mat mat);
    cv::Mat bitmap2Mat(JNIEnv *env, jobject bitmap);

}

#endif
