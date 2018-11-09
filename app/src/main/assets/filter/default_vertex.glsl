//#version 120
attribute vec4 vPosition;
attribute vec2 vCoordinate;
uniform mat4 vMatrix;

varying vec2 aCoordinate;

void main() {
    gl_Position = vPosition * vMatrix;
    aCoordinate = vCoordinate;
}
