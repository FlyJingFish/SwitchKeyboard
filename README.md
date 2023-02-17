# SwitchKeyboard
## 切换键盘和菜单的工具类,旨在解决切换键盘和菜单时的跳动问题

[![](https://jitpack.io/v/FlyJingFish/SwitchKeyboard.svg)](https://jitpack.io/#FlyJingFish/SwitchKeyboard)
[![GitHub stars](https://img.shields.io/github/stars/FlyJingFish/SwitchKeyboard.svg)](https://github.com/FlyJingFish/SwitchKeyboard/stargazers)
[![GitHub forks](https://img.shields.io/github/forks/FlyJingFish/SwitchKeyboard.svg)](https://github.com/FlyJingFish/SwitchKeyboard/network)
[![GitHub issues](https://img.shields.io/github/issues/FlyJingFish/SwitchKeyboard.svg)](https://github.com/FlyJingFish/SwitchKeyboard/issues)
[![GitHub license](https://img.shields.io/github/license/FlyJingFish/SwitchKeyboard.svg)](https://github.com/FlyJingFish/SwitchKeyboard/blob/master/LICENSE)


## [点此下载apk,也可扫下边二维码下载](https://github.com/FlyJingFish/SwitchKeyboard/blob/master/apk/release/app-release.apk?raw=true)

<img src="https://github.com/FlyJingFish/SwitchKeyboard/blob/master/screenshot/download_qrcode.png" alt="show" width="200px" height="200px" />

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
        implementation 'com.github.FlyJingFish:SwitchKeyboard:1.1.1'
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
    <!--     ll_menu 是存放所有显示在输入框一栏下边的，例如表情、多个功能按钮、常用语等等，详情可看demo       -->
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

            <LinearLayout
                android:id="@+id/ll_menu_btn"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:visibility="visible"
                android:gravity="center"
                android:orientation="vertical">
                <!--     比方说这块是放很多个功能按钮的       -->
            </LinearLayout>

            <RelativeLayout
                android:id="@+id/ll_emoji"
                android:layout_width="match_parent"
                android:layout_height="match_parent"
                android:visibility="gone">
                <!--     比方说这块是放表情相关的       -->
            </RelativeLayout>

            <LinearLayout
                android:id="@+id/ll_word"
                android:layout_width="match_parent"
                android:layout_height="match_parent"
                android:gravity="center"
                android:orientation="vertical"
                android:visibility="gone">
                <!--     比方说这块放常用语的       -->
            </LinearLayout>
        </RelativeLayout>

    </RelativeLayout>
```

**使用说明**

```java

public class Example2Activity extends AppCompatActivity {
    private SwitchKeyboardUtil baseChatKeyboardUtil;
    private ActivitySecond2Binding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseChatKeyboardUtil = new SwitchKeyboardUtil(this, true);
        //checkSoftMode 必须在 setContentView 之前调用
        baseChatKeyboardUtil.checkSoftMode();
        binding = ActivitySecond2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //是否让菜单高度和键盘高度一样（首次可能会有误差）
        baseChatKeyboardUtil.setMenuViewHeightEqualKeyboard(true);
        //所有设置设置这个之后才起效，必须在onCreate中调用
        baseChatKeyboardUtil.attachLifecycle(this);
        //输入框
        baseChatKeyboardUtil.setInputEditText(binding.etContent);
        //切换语音的按钮
        baseChatKeyboardUtil.setAudioBtn(binding.tvAudio);
        //语音录制按钮
        baseChatKeyboardUtil.setAudioTouchVIew(binding.tvAudioTouch);
        //存放所有菜单的布局
        baseChatKeyboardUtil.setMenuViewContainer(binding.llMenu);
        //设置切换菜单的切换按钮和菜单布局
        baseChatKeyboardUtil.setToggleMenuViews(new MenuModeView(binding.tvMore, binding.llMenuBtn),
                new MenuModeView(binding.tvFace, binding.llEmoji, binding.tvFaceBack,true),
                new MenuModeView(binding.tvWord, binding.llWord, binding.tvWordBack,true));
        //设置监听
        baseChatKeyboardUtil.setOnKeyboardMenuListener(new BaseSwitchKeyboardUtil.OnKeyboardMenuListener() {
            @Override
            public void onScrollToBottom() {
                scrollToBottom();
            }

            @Override
            public void onKeyboardHide(int keyboardHeight) {
                //当键盘隐藏后回调
            }

            @Override
            public void onKeyboardShow(int keyboardHeight) {
                //当键盘现实后回调
            }

            @Override
            public void onCallShowKeyboard() {
                //当调用显示键盘前回调
            }

            @Override
            public void onCallHideKeyboard() {
                //当调用隐藏键盘前回调
            }
        });
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //此处是用于点击手机返回按钮时关闭菜单
        if (keyCode == KeyEvent.KEYCODE_BACK && baseChatKeyboardUtil.onKeyDown(keyCode, event)) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void scrollToBottom() {
        binding.rv.scrollToPosition(binding.rv.getAdapter().getItemCount() - 1);
    }
}
```

**更多代码中使用方式如下：**

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



