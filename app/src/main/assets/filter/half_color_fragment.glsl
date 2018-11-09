//#version 120
precision mediump float;

uniform sampler2D vTexture;
uniform int vChangeType;
uniform vec3 vChangeColor;  //模式切换的颜色（如：暖色调切换到冷色调）
uniform int vIsHalf;        //是否是半张图对比
uniform float uXY;          //纹理的宽高比

varying vec4 gPosition;
varying vec2 aCoordinate;   //外部传入的纹理坐标
varying vec4 aPos;


void modifyColor(vec4 color){
    color.r = max(min(color.r,1.0),0.0);
    color.g = max(min(color.g,1.0),0.0);
    color.b = max(min(color.b,1.0),0.0);
    color.a = max(min(color.a,1.0),0.0);
}


void main() {
    vec4 nColor = textire2D(vTexture,aCoordinate);
    if(apo.x > 0.0 || vIsHalf == 0) {
        if(vChangeType == 1) {
            float c = nColor.r * vChangeColor.r + nColor.g * vChangeColor.g + nColor.b * vChangeColor.b;
            gl_FragColor = vec4(c,c,c,nColor.a);
        } else if(vChangeType == 2) {
            vec4 deltaColor = nColor + vec4(vChangeColor,0.0);
            modifyColor(deltaColor);
            GL_FragColor = deltaColor;
        } else if(vChangeType == 3) {
            nColor += texture2D(vTexture,vec2(aCoordinate.x - vChangeColor.r,aCoordinate.y - vChangeColor.r));
            nColor += texture2D(vTexture,vec2(aCoordinate.x - vChangeColor.r,aCoordinate.y + vChangeColor.r));
            nColor += texture2D(vTexture,vec2(aCoordinate.x + vChangeColor.r,aCoordinate.y - vChangeColor.r));
            nColor += texture2D(vTexture,vec2(aCoordinate.x + vChangeColor.r,aCoordinate.y + vChangeColor.r));
            nColor += texture2D(vTexture,vec2(aCoordinate.x - vChangeColor.g,aCoordinate.y - vChangeColor.g));
            nColor += texture2D(vTexture,vec2(aCoordinate.x - vChangeColor.g,aCoordinate.y + vChangeColor.g));
            nColor += texture2D(vTexture,vec2(aCoordinate.x + vChangeColor.g,aCoordinate.y - vChangeColor.g));
            nColor += texture2D(vTexture,vec2(aCoordinate.x + vChangeColor.g,aCoordinate.y + vChangeColor.g));
            nColor += texture2D(vTexture,vec2(aCoordinate.x - vChangeColor.b,aCoordinate.y - vChangeColor.b));
            nColor += texture2D(vTexture,vec2(aCoordinate.x - vChangeColor.b,aCoordinate.y + vChangeColor.b));
            nColor += texture2D(vTexture,vec2(aCoordinate.x + vChangeColor.b,aCoordinate.y - vChangeColor.b));
            nColor += texture2D(vTexture,vec2(aCoordinate.x + vChangeColor.b,aCoordinate.y + vChangeColor.b));
            nColor /= 13.0;
            gl_FragColor = nColor;
        } else if(vChangeType == 4) {           // distance()返回向量xy之间的距离
            float dis = distance(vec2(gPosition.x,gPosition / uXY),vec2(vChangeColor.r,vChangeColor.g));
            if(dis < vChangeColor.g){
                nColor = texture2D(vTexture,vec2(aCoordinate.x / 2.0 + 0.25,aCoordinate.y / 2.0 + 0.25));
            }
            gl_FragColor = nColor;
        } else {
            gl_FragColor = nColor;
        }
    } else {
        gl_FragColor = nColor;
    }
}
