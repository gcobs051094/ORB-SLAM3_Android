#include <jni.h>
#include <android/log.h>
#include <string>
#include <iostream>
#include <fstream>
#include <thread>

using namespace std;
// Write C++ code here.
//
// Do not forget to dynamically load the C++ library into your application.
//
// For instance,
//
// In MainActivity.java:
//    static {
//       System.loadLibrary("hitomi");
//    }
//
// Or, in MainActivity.kt:
//    companion object {
//      init {
//         System.loadLibrary("hitomi")
//      }
//    }

void thread_run()
{
  __android_log_print(ANDROID_LOG_DEBUG, "SampleCpp", "thread_run 1");
  __android_log_print(ANDROID_LOG_DEBUG, "SampleCpp", "thread_run 2");
  __android_log_print(ANDROID_LOG_DEBUG, "SampleCpp", "thread_run 3");

}

extern "C"
JNIEXPORT jint JNICALL
Java_cn_koistudio_hitomi_module_SampleCpp_getInt(JNIEnv *env, jobject thiz, jint input) {
    // TODO: implement getInt()

    std::thread t(thread_run);
    t.join();
    return input * 2;
}


extern "C"
JNIEXPORT jstring JNICALL
Java_cn_koistudio_hitomi_module_SampleCpp_getString(JNIEnv *env, jobject thiz, jstring input) {
    // TODO: implement getString()
    return input;
}



extern "C"
JNIEXPORT void JNICALL
Java_cn_koistudio_hitomi_util_uAssets_fread(JNIEnv *env, jclass clazz, jstring filename) {
    // TODO: implement fread()


    const char* filename_str;
    filename_str = env->GetStringUTFChars(filename, 0);
    if(!filename_str) {
        // std::cout << "error" << std::endl;
        env->ReleaseStringUTFChars(filename, filename_str);
        __android_log_print(ANDROID_LOG_ERROR, "SampleCpp", "error");
        return;
    }

    // env->ReleaseStringUTFChars(prompt, str);

    // std::cout << "success " << filename_str << std::endl;
    __android_log_print(ANDROID_LOG_WARN, "SampleCpp", "%s", filename_str);


    char data[1024];

    std::ifstream inFile(filename_str,std::ios::in|std::ios::binary);
    if(!inFile) {
        __android_log_print(ANDROID_LOG_WARN, "SampleCpp", "open error");
        env->ReleaseStringUTFChars(filename, filename_str);
        return ;
    }

    do
    {
        inFile.read(data, sizeof(data));
        int readedBytes = inFile.gcount(); //看刚才读了多少字节
        __android_log_print(ANDROID_LOG_WARN, "SampleCpp", "read %d Bytes",readedBytes);
        if(readedBytes<sizeof(data))
            break;
    }while(0);

    __android_log_print(ANDROID_LOG_INFO, "SampleCpp", "read fin");

    inFile.close();
    env->ReleaseStringUTFChars(filename, filename_str);
    return ;

}

//typedef struct mObject
//{
//    int _int    ;
//    long _long  ;
//    char _c[8] ;
//};

class mObject
{
public:
    int _int   ;
    long _long ;
    char _c[8] ;
};

extern "C"
JNIEXPORT jlong JNICALL
Java_cn_koistudio_hitomi_module_SampleCpp_createMalloc(JNIEnv *env, jobject thiz) {
    // TODO: implement createMalloc()

    mObject* pmObject = new mObject;
    pmObject-> _int = 8;
    pmObject-> _long = 88;

    __android_log_print(ANDROID_LOG_DEBUG, "SampleCpp", "malloc=%ld",(long)pmObject);

    return (long)pmObject;

}


extern "C"
JNIEXPORT void JNICALL
Java_cn_koistudio_hitomi_module_SampleCpp_releaseMalloc(JNIEnv *env, jobject thiz, jlong address) {
    // TODO: implement releaseMalloc()

    if(address)
    {
        __android_log_print(ANDROID_LOG_DEBUG, "SampleCpp", "delete=%ld",(long)address);


        mObject* pmObject = (mObject*)address;

        __android_log_print(ANDROID_LOG_DEBUG, "SampleCpp ", "pmObject._long=%ld",pmObject->_long);


        delete pmObject;
    }
}