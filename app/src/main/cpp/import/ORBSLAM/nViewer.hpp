#ifndef HITOMI_NVIEWER_HPP
#define HITOMI_NVIEWER_HPP


#include <jni.h>
#include <android/log.h>
#include <string>

#include <ORBmatcher.h>
#include <ORBVocabulary.h>
#include <System.h>
#include <jni.h>

#include <importOpenCV.h>


cv::Mat frame_draw_fast(cv::Mat* img,std::vector<cv::KeyPoint> vKeyPointsUn,cv::Scalar scalar,float scale);



#endif
