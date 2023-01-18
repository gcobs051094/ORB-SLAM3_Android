project("hitomiDebug")

# OpenCV
include_directories(${EXIMPORT_DIR}/opencv/include)
include_directories(./include)


link_directories(./libs/${ANDROID_ABI}/)
link_directories(./ORBSLAM/libs/${ANDROID_ABI}/)

# set(LOCAL_ALLOW_UNDEFINED_SYMBOLS true)

# Bitmap
# find_library(jnigraphics-lib jnigraphics)

# Self
add_library(lib_hitomi SHARED IMPORTED)
set_target_properties(lib_hitomi PROPERTIES IMPORTED_LOCATION
        ${CMAKE_CURRENT_SOURCE_DIR}/libs/${ANDROID_ABI}/libHitomi.so)


add_library(hitomiDebug SHARED
        hitomiDebug.cpp)

target_link_libraries( hitomiDebug
        OpenCV
        lib_hitomi
        android
        jnigraphics
        # lib_DBoW2
        ${log-lib} )