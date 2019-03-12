[1.简单工厂模式](#1.简单工厂模式).
# 说明：此处为OpenGL学习练习代码
## 参考链接https://blog.csdn.net/junzia/article/category/6462864

### 1.三角形
### 2.等腰三角形（通过相机和投影用变换矩阵来实现等腰直角三角形）
投影矩阵和视口变换矩阵https://blog.csdn.net/wangdingqiaoit/article/details/51589825
### 3.彩色三角形，正方形和圆形 
GLES20.GL_TRIANGLES，GLES20.GL_TRIANGLE_STRIP，GLES20.GL_TRIANGLE_FAN https://blog.csdn.net/xiajun07061225/article/details/7455283
glViewPort（）在OpenGL中就是设置窗体，多用于VR分屏的操作
glViewPort（）和gluOrtho2D（）和glutInitWindowSize（）区分
https://blog.csdn.net/m0_37402140/article/details/78126767

GLSL 三种变量类型（uniform，attribute和varying）https://www.jianshu.com/p/eed3ebdad4fb
### 4.正方体和圆锥


Android Studio GLSLSupport编辑器（着色器语言）插件 （支持高亮和智能提示）
https://blog.csdn.net/huangruqi888/article/details/83858195
### 5.圆柱，球体和带光源的球体
opengl es2.0之Matrix.setLookAtM解析
https://blog.csdn.net/kkae8643150/article/details/52805738
旋转矩阵(Rotate Matrix)的性质分析
https://blog.csdn.net/zhang11wu4/article/details/49761121

GLSurfaceView渲染过程详解 https://blog.csdn.net/aa841538513/article/details/52291759
setRenderMode()注解中提到：系统默认mode==RENDERMODE_CONTINUOUSLY，这样系统会自动重绘；mode==RENDERMODE_WHEN_DIRTY时，只有surfaceCreate的时候会绘制一次，然后就需要通过requestRender()方法主动请求重绘。同时也提到，如果你的界面不需要频繁的刷新最好是设置成RENDERMODE_WHEN_DIRTY，这样可以降低CPU和GPU的活动，可以省电。

### 6.对于图片的处理，冷色调，暖色调，模糊，放大镜（及渲染半张对比图）

GLES2.0中文API-glVertexAttribPointer()方法参数详解
https://blog.csdn.net/flycatdeng/article/details/82667374
### 7.相机预览

创建config.gradle的配置项
<div id="1.简单工厂模式"></div>
### 8.相机Camra2和加载动画
