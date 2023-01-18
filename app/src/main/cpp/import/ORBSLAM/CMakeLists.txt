project("ORBSLAM")

include_directories(./)
include_directories(./include)
include_directories(./include/CameraModels)
include_directories(./Thirdparty)
include_directories(./Thirdparty/Eigen3)


link_directories(./libs/${ANDROID_ABI}/)

add_library(lib_orbslam_g2o SHARED IMPORTED)
set_target_properties(lib_orbslam_g2o PROPERTIES IMPORTED_LOCATION
        ${CMAKE_CURRENT_SOURCE_DIR}/libs/${ANDROID_ABI}/libg2o.so)

add_library(lib_orbslam_dbow2 SHARED IMPORTED)
set_target_properties(lib_orbslam_dbow2 PROPERTIES IMPORTED_LOCATION
        ${CMAKE_CURRENT_SOURCE_DIR}/libs/${ANDROID_ABI}/libDBoW2.so)

add_library(lib_orbslam SHARED IMPORTED)
set_target_properties(lib_orbslam PROPERTIES IMPORTED_LOCATION
        ${CMAKE_CURRENT_SOURCE_DIR}/libs/${ANDROID_ABI}/libORB_SLAM3.so)


add_library(ORBSLAM SHARED
        importORBSLAM.cpp
        nViewer.cpp
        nViewer.hpp
        )

target_link_libraries(ORBSLAM
        lib_orbslam
        lib_orbslam_dbow2
        lib_orbslam_g2o
        OpenCV
        android
        ${log-lib})