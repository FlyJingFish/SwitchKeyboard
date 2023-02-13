# SwitchKeyboard
## 切换键盘和菜单的工具类

[![](https://jitpack.io/v/FlyJingFish/SwitchKeyboard.svg)](https://jitpack.io/#FlyJingFish/SwitchKeyboard)
[![GitHub stars](https://img.shields.io/github/stars/FlyJingFish/SwitchKeyboard.svg)](https://github.com/FlyJingFish/SwitchKeyboard/stargazers)
[![GitHub forks](https://img.shields.io/github/forks/FlyJingFish/SwitchKeyboard.svg)](https://github.com/FlyJingFish/SwitchKeyboard/network)
[![GitHub issues](https://img.shields.io/github/issues/FlyJingFish/SwitchKeyboard.svg)](https://github.com/FlyJingFish/SwitchKeyboard/issues)
[![GitHub license](https://img.shields.io/github/license/FlyJingFish/SwitchKeyboard.svg)](https://github.com/FlyJingFish/SwitchKeyboard/blob/master/LICENSE)


<img src="https://github.com/FlyJingFish/SwitchKeyboard/blob/master/screenshot/Screenshot_20221013_130230.jpg" width="405px" height="842px" alt="show" />


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
        implementation 'com.github.FlyJingFish:SwitchKeyboard:1.0.0'
    }
```
## 第三步，使用说明

1、假如输入框一栏**有表情**按钮时使用 Example1SwitchKeyboardUtil 详情可看[SecondActivity2](https://github.com/FlyJingFish/OpenImage/blob/master/app/src/main/java/com/flyjingfish/switchkeyboard/SecondActivity2)

2、假如输入框一栏**没有表情**按钮时使用 Example2SwitchKeyboardUtil 详情可看[SecondActivity3](https://github.com/FlyJingFish/OpenImage/blob/master/app/src/main/java/com/flyjingfish/switchkeyboard/SecondActivity3)

3、如需完全自定义切换逻辑可使用BaseSwitchKeyboardUtil [SecondActivity](https://github.com/FlyJingFish/OpenImage/blob/master/app/src/main/java/com/flyjingfish/switchkeyboard/SecondActivity)



# 最后推荐我写的另一个库，轻松实现在应用内点击小图查看大图的动画放大效果

- [OpenImage](https://github.com/FlyJingFish/OpenImage) 

- [主页查看更多开源库](https://github.com/FlyJingFish)



