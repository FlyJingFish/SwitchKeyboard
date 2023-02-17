# SwitchKeyboard
## 切换键盘和菜单的工具类,旨在解决切换键盘和菜单时的跳动问题

[![](https://jitpack.io/v/FlyJingFish/SwitchKeyboard.svg)](https://jitpack.io/#FlyJingFish/SwitchKeyboard)
[![GitHub stars](https://img.shields.io/github/stars/FlyJingFish/SwitchKeyboard.svg)](https://github.com/FlyJingFish/SwitchKeyboard/stargazers)
[![GitHub forks](https://img.shields.io/github/forks/FlyJingFish/SwitchKeyboard.svg)](https://github.com/FlyJingFish/SwitchKeyboard/network)
[![GitHub issues](https://img.shields.io/github/issues/FlyJingFish/SwitchKeyboard.svg)](https://github.com/FlyJingFish/SwitchKeyboard/issues)
[![GitHub license](https://img.shields.io/github/license/FlyJingFish/SwitchKeyboard.svg)](https://github.com/FlyJingFish/SwitchKeyboard/blob/master/LICENSE)


<img src="https://github.com/FlyJingFish/SwitchKeyboard/blob/master/screenshot/Screenrecording_20230213_185236.gif" width="320px" height="640px" alt="show" />


## 第一步，根目录build.gradle

```gradle
    allprojects {
        repositories {
            ...
            maven { url 'https://jitpack.io' }
        }
    }
```
## 第二步，需要引用的build.gradle （最新版本[![](https://jitpack.io/v/FlyJingFish/SwitchKeyboard.svg)](https://jitpack.io/#FlyJingFish/SwitchKeyboard)）

```gradle
    dependencies {
        implementation 'com.github.FlyJingFish:SwitchKeyboard:1.1'
    }
```
## 第三步，使用说明

**布局要求**

```xml
    <RelativeLayout
        android:id="@+id/ll_menu_content"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:gravity="center_vertical"
        android:layout_alignParentBottom="true"
        android:orientation="vertical">
    <!--     ll_msg_content 是存放输入框一栏的（就是被键盘顶起来的那部分）       -->
        <RelativeLayout
            android:id="@+id/ll_msg_content"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:paddingHorizontal="13dp"
            android:paddingVertical="13dp"
            android:gravity="center_vertical"
            android:focusable="true"
            android:focusableInTouchMode="true"
            android:background="@drawable/bg_edit_person_info"
            android:orientation="horizontal">
        </RelativeLayout>
    <!--     ll_menu 是存放所有显示在输入框一栏下边的，例如表情，多个菜单按钮，详情可看demo       -->
        <RelativeLayout
            android:id="@+id/ll_menu"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:gravity="center"
            android:layout_below="@+id/ll_msg_content"
            android:paddingHorizontal="13dp"
            android:background="@color/white"
            android:visibility="gone"
            android:orientation="horizontal">
        </RelativeLayout>

    </RelativeLayout>
```

**代码中使用方式如下：**

1、假如有按钮**在输入框一栏** 打开底部隐藏菜单时 详情可看[Example1Activity](https://github.com/FlyJingFish/SwitchKeyboard/blob/master/app/src/main/java/com/flyjingfish/switchkeyboard/Example1Activity.java)

2、假如有按钮**不在输入框一栏时** 打开底部隐藏菜单时 详情可看[Example2Activity](https://github.com/FlyJingFish/SwitchKeyboard/blob/master/app/src/main/java/com/flyjingfish/switchkeyboard/Example2Activity.java)



## 特别注意

### 1、在 Activity 调用 setContentView() 之前，先调用 checkSoftMode()

### 2、本工具类设置了布局全屏显示,并设置了透明状态栏，请自行适配顶部返回键一栏距离顶部的距离

### 3、布局中不可使用 fitsSystemWindows 属性为true

### 4、菜单是否和键盘高度一致可根据构造参数设置，更多使用详情请看代码

## 关于打开页面时键盘的状态

### 1、有些机型会自动打开键盘，可设置 windowSoftInputMode 模式来解决，例如你不希望打开键盘就可以设置 stateAlwaysHidden 或 stateAlwaysHidden

### 2、如果想自动弹出键盘，可设置 windowSoftInputMode 为 stateAlwaysVisible 或 stateVisible


# 最后推荐我写的另一个库，轻松实现在应用内点击小图查看大图的动画放大效果

- [OpenImage](https://github.com/FlyJingFish/OpenImage) 

- [主页查看更多开源库](https://github.com/FlyJingFish)



