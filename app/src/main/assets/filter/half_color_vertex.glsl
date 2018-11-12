//#version 120        //OpenGL2.0
attribute vec4 vPosition;
attribute vec2 vCoordinate;     //纹理坐标（即图片所在的android的屏幕坐标系）
uniform mat4 vMatrix;

varying vec2 aCoordinate;       //外部传入的纹理坐标
varying vec4 aPos;
varying vec4 gPosition;     //ShaderUtils: GLES20 Error:0:8: L0001: Typename expected, found 'gPosition'
                              //    0:13: L0002: Undeclared variable 'gPosition'   原因是未声明变量类型
void main() {
    gl_Position = vMatrix * vPosition;
    aPos = vPosition;
    aCoordinate = vCoordinate;
    gPosition = vMatrix * vPosition;
}
