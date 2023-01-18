#include <jni.h>
#include <android/log.h>
#include <string>

#include <ORBmatcher.h>
#include <ORBVocabulary.h>
#include <System.h>
#include <jni.h>

#include <importOpenCV.h>

#include "nViewer.hpp"

static const char* TAG = "ORBSLAM";


cv::Mat frame_draw_fast(cv::Mat* img,std::vector<cv::KeyPoint> vKeyPointsUn,cv::Scalar color,float scale)
{
    cv::Mat res = img->clone();

    const float r = 5;
    for(int i=0;i<vKeyPointsUn.size();i++)
    {
        cv::KeyPoint kp = vKeyPointsUn[i];

        cv::Point2f pt1,pt2 ;
        pt1.x = (kp.pt.x)*scale - r ;
        pt1.y = (kp.pt.y)*scale - r ;
        pt2.x = (kp.pt.x)*scale + r ;
        pt2.y = (kp.pt.y)*scale + r ;

        cv::rectangle(res,pt1,pt2,color);
        //cv::circle(res,kp.pt,2,color,-1);

    }

    return res;
}