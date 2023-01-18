#include <jni.h>
#include <android/log.h>
#include <android/bitmap.h>
#include <opencv2/core.hpp>
#include "opencv2/imgproc.hpp"
#include <string>

#include <core/feature/feature.hpp>

//#include <ORBmatcher.h>
//#include <ORBVocabulary.h>
//#include <System.h>

#include <importOpenCV.h>

using namespace cv;


extern "C"
JNIEXPORT void JNICALL
Java_cn_koistudio_hitomi_module_HitomiDebug_SampleHitomiDebug_orbPreview(JNIEnv *env, jobject thiz, jobject bitmap) {
    // TODO: implement orbPreview()

    cv::Mat inputImg = cv::bitmap2Mat(env,bitmap);
    inputImg = hitomi::ORB::preview(inputImg);
    cv::mat2Bitmap(env,bitmap,inputImg);

    return ;

}


extern "C"
JNIEXPORT void JNICALL
Java_cn_koistudio_hitomi_module_HitomiDebug_SampleHitomiDebug_bowVoc(JNIEnv *env, jobject thiz,
        jstring filename) {

    const char* filename_str;
    filename_str = env->GetStringUTFChars(filename, 0);
    if(!filename_str) {
        // std::cout << "error" << std::endl;
        env->ReleaseStringUTFChars(filename, filename_str);
        __android_log_print(ANDROID_LOG_ERROR, "hitomiDebug", "error");
        return;
    }

//    ORB_SLAM3::ORBVocabulary mpVocabulary;
//    bool bVocLoad = mpVocabulary.loadFromTextFile(filename_str);
//    if(bVocLoad)
//        __android_log_print(ANDROID_LOG_ERROR, "hitomiDebug ", "voc true");
//
//    __android_log_print(ANDROID_LOG_ERROR, "hitomiDebug", "fin");
    return;

}

