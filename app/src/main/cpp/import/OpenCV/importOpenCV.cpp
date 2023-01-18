#include "importOpenCV.h"

using namespace cv;
using namespace std;

int cv::mat2Bitmap(JNIEnv *env, jobject bitmap, cv::Mat mat)
{
    AndroidBitmapInfo info;
    void *pixels = 0;

    if (AndroidBitmap_getInfo(env, bitmap, &info) < 0)
        return -1;

    if (info.format != ANDROID_BITMAP_FORMAT_RGBA_8888 && info.format != ANDROID_BITMAP_FORMAT_RGB_565)
        return -1; // Error: Unsupported Format

    if (AndroidBitmap_lockPixels(env, bitmap, &pixels) < 0)
        return -1; // Error: Lock Data Failed

    if (!pixels)
    {
        AndroidBitmap_unlockPixels(env, bitmap);
        return -1; // Error: Get Date Failed
    }

    if(info.height==0||info.width==0)
    {
        AndroidBitmap_unlockPixels(env, bitmap);
        return -1; // Error: Size
    }

    if(info.height!=mat.rows||info.width!=mat.cols)
    {
        cv::resize(mat,mat,Size(info.width,info.height),0,0);
    }

    if (info.format == ANDROID_BITMAP_FORMAT_RGBA_8888) {

        Mat pointer(info.height, info.width, CV_8UC4, pixels);
        AndroidBitmap_unlockPixels(env, bitmap);
        cvtColor(mat, mat, CV_RGB2BGR);
        cvtColor(mat, pointer, CV_BGR2RGBA);
        return 0;

    }
    else if(info.format == ANDROID_BITMAP_FORMAT_RGB_565) {

        Mat pointer(info.height, info.width, CV_8UC2, pixels);
        cvtColor(mat, mat, CV_RGB2BGR);
        cvtColor(mat, pointer, CV_BGR2BGR565);
        AndroidBitmap_unlockPixels(env, bitmap);
        return 0;
    }

    AndroidBitmap_unlockPixels(env, bitmap);
    return 0;

}

cv::Mat cv::bitmap2Mat(JNIEnv *env, jobject bitmap)
{
    AndroidBitmapInfo info;
    void *pixels = 0;

    if (AndroidBitmap_getInfo(env, bitmap, &info) < 0)
        return Mat();

    if (info.format != ANDROID_BITMAP_FORMAT_RGBA_8888 && info.format != ANDROID_BITMAP_FORMAT_RGB_565)
        return Mat(); // Error: Unsupported Format

    if (AndroidBitmap_lockPixels(env, bitmap, &pixels) < 0)
        return Mat(); // Error: Lock Data Failed

    if (!pixels)
    {
        AndroidBitmap_unlockPixels(env, bitmap);
        return Mat(); // Error: Get Date Failed
    }


    if (info.format == ANDROID_BITMAP_FORMAT_RGBA_8888){

        Mat dst(info.height, info.width, CV_8UC4, pixels);
        cvtColor(dst, dst, CV_RGBA2RGB);
        AndroidBitmap_unlockPixels(env, bitmap);
        return dst;

    }
    else if (info.format == ANDROID_BITMAP_FORMAT_RGB_565){

        Mat dst(info.height, info.width, CV_8UC2, pixels);
        cvtColor(dst, dst, COLOR_BGR5652BGR);
        AndroidBitmap_unlockPixels(env, bitmap);
        return dst;
    }

    // Error: Never Go Here
    AndroidBitmap_unlockPixels(env, bitmap);
    return Mat();

}
