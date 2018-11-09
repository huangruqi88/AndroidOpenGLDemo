//#version 120        //OpenGL2.0
attribute vec4 vPosition;
attribute vec2 vCoordinate;     //纹理坐标（即图片所在的android的屏幕坐标系）
uniform mat4 vMatrix;

varying vec2 aCoordinate;       //外部传入的纹理坐标
varying vec4 aPos;
varying gPosition;
void main() {
    gl_Position = vMatrix * vPosition;
    aPos = vPosition;
    aCoordinate = vCoordinate;
    gPositoon = vMatrix * vPosition;
}
