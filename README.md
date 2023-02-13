# HollowTextView
## 镂空TextView，支持渐变色粗边，支持设置背景

[![](https://jitpack.io/v/FlyJingFish/HollowTextView.svg)](https://jitpack.io/#FlyJingFish/HollowTextView)
[![GitHub stars](https://img.shields.io/github/stars/FlyJingFish/HollowTextView.svg)](https://github.com/FlyJingFish/HollowTextView/stargazers)
[![GitHub forks](https://img.shields.io/github/forks/FlyJingFish/HollowTextView.svg)](https://github.com/FlyJingFish/HollowTextView/network)
[![GitHub issues](https://img.shields.io/github/issues/FlyJingFish/HollowTextView.svg)](https://github.com/FlyJingFish/HollowTextView/issues)
[![GitHub license](https://img.shields.io/github/license/FlyJingFish/HollowTextView.svg)](https://github.com/FlyJingFish/HollowTextView/blob/master/LICENSE)


<img src="https://github.com/FlyJingFish/HollowTextView/blob/master/screenshot/Screenshot_20221013_130230.jpg" width="405px" height="842px" alt="show" />


## 第一步，根目录build.gradle

```gradle
    allprojects {
        repositories {
            ...
            maven { url 'https://jitpack.io' }
        }
    }
```
## 第二步，需要引用的build.gradle （最新版本[![](https://jitpack.io/v/FlyJingFish/HollowTextView.svg)](https://jitpack.io/#FlyJingFish/HollowTextView)）

```gradle
    dependencies {
        implementation 'com.github.FlyJingFish:HollowTextView:1.0.5'
    }
```
## 第三步，使用说明

**设置背景示例**

```xml
<com.flyjingfish.hollowtextviewlib.HollowTextView
    android:id="@+id/hollowTextView"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:paddingVertical="8dp"
    android:paddingHorizontal="40dp"
    android:text="Hello World!"
    android:gravity="center"
    android:textStyle="bold|italic"
    android:background="@drawable/bg_hollow"
    android:textSize="30sp"/>
```

**粗边普通颜色示例**

```xml

<com.flyjingfish.hollowtextviewlib.HollowTextView
    android:id="@+id/hollowTextView3"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:text=" Hello World! "
    android:gravity="center"
    android:textStyle="bold"
    android:layout_marginTop="10dp"
    app:hollow_stroke_textColor="@color/white"
    app:hollow_stroke_strokeWidth="6dp"
    android:textSize="30sp"/>
```

**粗边渐变颜色示例**

```xml
<com.flyjingfish.hollowtextviewlib.HollowTextView
    android:id="@+id/hollowTextView2"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:text=" Hello World! "
    android:gravity="center"
    android:textStyle="bold"
    android:layout_marginTop="10dp"
    app:hollow_stroke_angle="0"
    app:hollow_stroke_startColor="@color/purple_500"
    app:hollow_stroke_endColor="@color/teal_200"
    app:hollow_stroke_strokeWidth="6dp"
    android:textSize="30sp"/>
```

### 属性一览

| attr                      |  format   |       description       |
|---------------------------|:---------:|:-----------------------:|
| hollow_stroke_startColor  |   color   |      字体粗边渐变颜色开始颜色       |
| hollow_stroke_centerColor |   color   |      字体粗边渐变颜色中心颜色       |
| hollow_stroke_endColor    |   color   |      字体粗边渐变颜色结束颜色       |
| hollow_stroke_angle       |   float   |      字体粗边渐变颜色开始角度       |
| hollow_stroke_rtl_angle   |  boolean  | 字体粗边渐变颜色开始角度是否支持镜像Rtl适配 |
| hollow_stroke_strokeWidth | dimension |        字体粗边画笔宽度         |
| hollow_stroke_textColor   |   color   |  字体粗边颜色（设置渐变色之后此属性无效）   |

### 常见问题

1，如果使用粗边可能会存在左右两侧被切除一部分，可在字符串两侧添加空格解决问题


# 最后推荐我写的另一个库，轻松实现在应用内点击小图查看大图的动画放大效果

- [OpenImage](https://github.com/FlyJingFish/OpenImage) 

- [主页查看更多开源库](https://github.com/FlyJingFish)



