#include "hitomi.hpp"


cv::Mat hitomi::common_size_limit(cv::Mat mat)
{
   cv::Mat res;
   const int cMaxHeight = 720;

   if(mat.rows>cMaxHeight)
   {
       cv::resize( mat, res, cv::Size( cMaxHeight*mat.cols/mat.rows, cMaxHeight) );
       return res ;
   }
   else
   {
      return mat;
   }

   
}

cv::Mat hitomi::common_size_limit(const char* filename)
{
   cv::Mat mat = cv::imread(filename);

   if(mat.empty())
   {
      return mat;
   }

   return common_size_limit(mat);

}
