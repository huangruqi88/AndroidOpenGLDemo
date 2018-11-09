//#version 120    //OpenGL 2.1
precision mediump float;

uniform sampler2D vTexture;
uniform int vIsHalf;

varying vec2 aCoordinate;
void main() {
    gl_FragColor = texture2D(vTexture,aCoordinate);
}
