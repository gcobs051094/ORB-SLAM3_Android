package cn.koistudio.hitomi.util;

import android.opengl.GLES20;
import android.util.Log;

public class uGLES {

    private static String TAG = "uGLES";

    public static String Default_VertexShaderCode = "" +
            "attribute vec4 vPosition;" +
            "void main() {" +
            "  gl_Position = vPosition;" +
            "}";

    public static String Default_VertexShaderCode_3D = "" +
            "attribute vec4 vPosition;" +
            "uniform mat4 vMatrix;" +
            "void main() {" +
            "  gl_Position = vMatrix * vPosition ; " +
            "}";

    public static String Default_FragmentShaderCode = "" +
            "precision mediump float;" +
            "uniform vec4 vColor;" +
            "void main() {" +
            "  gl_FragColor = vColor;" +
            "}";

    public static int loadShader(int type, String shaderCode) {

        int shader = GLES20.glCreateShader(type);
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        int[] compiled = new int[1];
        GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compiled, 0);
        Log.d(TAG,"Compile Handler="+shader+" State="+compiled[0]);
        return shader;

    }

    public static int createProgram(int handler_vertex, int mHandler_fragment) {

        int program = GLES20.glCreateProgram();
        GLES20.glAttachShader(program, handler_vertex);
        GLES20.glAttachShader(program, mHandler_fragment);
        GLES20.glLinkProgram(program);

        int[] compiled = new int[1];
        GLES20.glGetProgramiv(program, GLES20.GL_LINK_STATUS, compiled, 0);
        Log.d(TAG,"Link Handler="+program+" State="+compiled[0]);
        return program;
    }


}
