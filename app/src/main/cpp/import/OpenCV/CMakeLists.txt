project("OpenCV")

include_directories(${EXIMPORT_DIR}/opencv/include)
link_directories(./libs/${ANDROID_ABI}/)

# Bitmap
find_library(jnigraphics-lib jnigraphics)

add_library(lib_opencv_core SHARED IMPORTED)
set_target_properties(lib_opencv_core PROPERTIES IMPORTED_LOCATION
        ${EXIMPORT_DIR}/OpenCV/libs/${ANDROID_ABI}/libopencv_core.so)

add_library(lib_opencv_imgproc SHARED IMPORTED)
set_target_properties(lib_opencv_imgproc PROPERTIES IMPORTED_LOCATION
        ${EXIMPORT_DIR}/OpenCV/libs/${ANDROID_ABI}/libopencv_imgproc.so)

add_library(lib_opencv_highgui SHARED IMPORTED)
set_target_properties(lib_opencv_highgui PROPERTIES IMPORTED_LOCATION
        ${EXIMPORT_DIR}/OpenCV/libs/${ANDROID_ABI}/libopencv_highgui.so)

add_library(lib_opencv_ml SHARED IMPORTED)
set_target_properties(lib_opencv_ml PROPERTIES IMPORTED_LOCATION
        ${EXIMPORT_DIR}/OpenCV/libs/${ANDROID_ABI}/libopencv_ml.so)

add_library(lib_opencv_features2d SHARED IMPORTED)
set_target_properties(lib_opencv_features2d PROPERTIES IMPORTED_LOCATION
        ${EXIMPORT_DIR}/OpenCV/libs/${ANDROID_ABI}/libopencv_features2d.so)

add_library(lib_opencv_flann SHARED IMPORTED)
set_target_properties(lib_opencv_flann PROPERTIES IMPORTED_LOCATION
        ${EXIMPORT_DIR}/OpenCV/libs/${ANDROID_ABI}/libopencv_flann.so)

add_library(lib_opencv_objdetect SHARED IMPORTED)
set_target_properties(lib_opencv_objdetect PROPERTIES IMPORTED_LOCATION
        ${EXIMPORT_DIR}/OpenCV/libs/${ANDROID_ABI}/libopencv_objdetect.so)

add_library(lib_opencv_calib3d SHARED IMPORTED)
set_target_properties(lib_opencv_calib3d PROPERTIES IMPORTED_LOCATION
        ${EXIMPORT_DIR}/OpenCV/libs/${ANDROID_ABI}/libopencv_calib3d.so)

add_library(lib_opencv_imgcodecs SHARED IMPORTED)
set_target_properties(lib_opencv_imgcodecs PROPERTIES IMPORTED_LOCATION
        ${EXIMPORT_DIR}/OpenCV/libs/${ANDROID_ABI}/libopencv_imgcodecs.so)

add_library(lib_opencv_photo SHARED IMPORTED)
set_target_properties(lib_opencv_photo PROPERTIES IMPORTED_LOCATION
        ${EXIMPORT_DIR}/OpenCV/libs/${ANDROID_ABI}/libopencv_photo.so)

add_library(lib_opencv_shape SHARED IMPORTED)
set_target_properties(lib_opencv_shape PROPERTIES IMPORTED_LOCATION
        ${EXIMPORT_DIR}/OpenCV/libs/${ANDROID_ABI}/libopencv_shape.so)

add_library(lib_opencv_stitching SHARED IMPORTED)
set_target_properties(lib_opencv_stitching PROPERTIES IMPORTED_LOCATION
        ${EXIMPORT_DIR}/OpenCV/libs/${ANDROID_ABI}/libopencv_stitching.so)

add_library(lib_opencv_superres SHARED IMPORTED)
set_target_properties(lib_opencv_superres PROPERTIES IMPORTED_LOCATION
        ${EXIMPORT_DIR}/OpenCV/libs/${ANDROID_ABI}/libopencv_superres.so)

add_library(lib_opencv_video SHARED IMPORTED)
set_target_properties(lib_opencv_video PROPERTIES IMPORTED_LOCATION
        ${EXIMPORT_DIR}/OpenCV/libs/${ANDROID_ABI}/libopencv_video.so)

add_library(lib_opencv_videoio SHARED IMPORTED)
set_target_properties(lib_opencv_videoio PROPERTIES IMPORTED_LOCATION
        ${EXIMPORT_DIR}/OpenCV/libs/${ANDROID_ABI}/libopencv_videoio.so)

add_library(lib_opencv_videostab SHARED IMPORTED)
set_target_properties(lib_opencv_videostab PROPERTIES IMPORTED_LOCATION
        ${EXIMPORT_DIR}/OpenCV/libs/${ANDROID_ABI}/libopencv_videostab.so)

add_library(lib_opencv_dnn SHARED IMPORTED)
set_target_properties(lib_opencv_dnn PROPERTIES IMPORTED_LOCATION
        ${EXIMPORT_DIR}/OpenCV/libs/${ANDROID_ABI}/libopencv_dnn.so)


add_library(OpenCV SHARED
        importOpenCV.cpp
        importOpenCV.h
        SampleOpenCV.cpp)

target_link_libraries( OpenCV

        # OpenCV
        lib_opencv_core
        lib_opencv_imgproc
        lib_opencv_highgui
        lib_opencv_ml
        lib_opencv_features2d
        lib_opencv_flann
        lib_opencv_objdetect
        lib_opencv_calib3d
        lib_opencv_imgcodecs
        lib_opencv_photo
        lib_opencv_shape
        lib_opencv_stitching
        lib_opencv_superres
        lib_opencv_video
        lib_opencv_videoio
        lib_opencv_videostab
        lib_opencv_dnn

        # Android
        android
        jnigraphics
        ${log-lib} )
