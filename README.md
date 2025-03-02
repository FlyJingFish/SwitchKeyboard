# SwitchKeyboard
## 切换键盘和菜单的工具类,旨在解决切换键盘和菜单时的跳动问题

[![](https://jitpack.io/v/FlyJingFish/SwitchKeyboard.svg)](https://jitpack.io/#FlyJingFish/SwitchKeyboard)
[![GitHub stars](https://img.shields.io/github/stars/FlyJingFish/SwitchKeyboard.svg)](https://github.com/FlyJingFish/SwitchKeyboard/stargazers)
[![GitHub forks](https://img.shields.io/github/forks/FlyJingFish/SwitchKeyboard.svg)](https://github.com/FlyJingFish/SwitchKeyboard/network)
[![GitHub issues](https://img.shields.io/github/issues/FlyJingFish/SwitchKeyboard.svg)](https://github.com/FlyJingFish/SwitchKeyboard/issues)
[![GitHub license](https://img.shields.io/github/license/FlyJingFish/SwitchKeyboard.svg)](https://github.com/FlyJingFish/SwitchKeyboard/blob/master/LICENSE)


## [点此下载apk,也可扫下边二维码下载](https://github.com/FlyJingFish/SwitchKeyboard/blob/master/apk/release/app-release.apk?raw=true)

<img src="https://github.com/FlyJingFish/SwitchKeyboard/blob/master/screenshot/download_qrcode.png" alt="show" width="200px" height="200px" />

<img src="https://github.com/FlyJingFish/SwitchKeyboard/blob/master/screenshot/Screenrecording_20230221_192034.gif" width="320px" height="640px" alt="show" />


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
        implementation 'com.github.FlyJingFish:SwitchKeyboard:1.2.3'
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
    private SwitchKeyboardUtil switchKeyboardUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example1);
        switchKeyboardUtil = new SwitchKeyboardUtil(this);
        //是否让菜单高度和键盘高度一样（首次可能会有误差）
        switchKeyboardUtil.setMenuViewHeightEqualKeyboard(false);
        //切换时是否使用动画（默认开启）
        switchKeyboardUtil.setUseSwitchAnim(true);
        //菜单之间切换时从底部弹出 setUseSwitchAnim(true) 时才起作用 
        switchKeyboardUtil.setUseMenuUpAnim(true);
        //所有设置设置这个之后才起效
        switchKeyboardUtil.attachLifecycle(this);
        //输入框（必须设置）
        switchKeyboardUtil.setInputEditText(etContent);
        //切换语音的按钮（不必设置）
        switchKeyboardUtil.setAudioBtn(tvAudio);
        //语音录制按钮（不必设置）
        switchKeyboardUtil.setAudioTouchVIew(tvAudioTouch);
        //存放所有菜单的布局（必须设置）
        switchKeyboardUtil.setMenuViewContainer(llMenu);
        //进入页面时是否自动弹出键盘
        switchKeyboardUtil.setAutoShowKeyboard(true, AutoShowKeyboardType.FIRST_SHOW);
        //设置切换菜单的切换按钮和菜单布局（不必设置）
        switchKeyboardUtil.setToggleMenuViews(
                new MenuModeView(tvMore,llMenuBtn),
                new MenuModeView(ivFace,llEmoji),
                new MenuModeView(tvGift,llGift),
                new MenuModeView(tvWord,llWord));
        //设置监听
        switchKeyboardUtil.setOnKeyboardMenuListener(new BaseSwitchKeyboardUtil.OnKeyboardMenuListener() {
            @Override
            public void onScrollToBottom() {
                //如果你需要让聊天内容在打开菜单或键盘时滑动到底部，则在此写代码
                scrollToBottom();
            }

            @Override
            public void onCallShowKeyboard() {
                //当调用显示键盘前回调
            }

            @Override
            public void onCallHideKeyboard() {
                //当调用隐藏键盘前回调
            }

            @Override
            public void onKeyboardHide(int keyboardHeight) {
                //当键盘隐藏后回调
            }

            @Override
            public void onKeyboardShow(int keyboardHeight) {
                //当键盘显示后回调
                tvAudio.setImageResource(R.drawable.ic_audio);
                ivFace.setImageResource(R.drawable.ic_face);
            }

            @Override
            public void onShowMenuLayout(View layoutView) {
                //当显示某个菜单布局(即 MenuModeView.toggleViewContainer )时回调 
                tvAudio.setImageResource(layoutView == tvAudioTouch?R.drawable.ic_keyboard:R.drawable.ic_audio);
                ivFace.setImageResource(layoutView == llEmoji?R.drawable.ic_keyboard:R.drawable.ic_face);
            }

            @Override
            public void onHideMenuViewContainer() {
                //当收起菜单时回调这个方法
                binding.tvAudio.setImageResource(R.drawable.ic_audio);
                binding.ivFace.setImageResource(R.drawable.ic_face);
            }
        });
        //你的输入框如果需要设置 setOnTouchListener 请调用这个，否则将会影响切换动画
        switchKeyboardUtil.setEtContentOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });

        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN){
                    //收起键盘或菜单
                    switchKeyboardUtil.hideMenuAndKeyboard();
                }
                return false;
            }
        });
        //这个是保持消息平滑移动的关键
        View.OnLayoutChangeListener onLayoutChangeListener = (v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom) -> scrollToBottom();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState != RecyclerView.SCROLL_STATE_IDLE){
                    recyclerView.removeOnLayoutChangeListener(onLayoutChangeListener);
                }else {
                    recyclerView.addOnLayoutChangeListener(onLayoutChangeListener);
                }
            }
        });
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //此处是用于点击手机返回按钮时关闭菜单
        if (switchKeyboardUtil.onKeyDown(keyCode, event)) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void scrollToBottom() {
        recyclerView.scrollToPosition(recyclerView.getAdapter().getItemCount() - 1);
    }
}
```



**更多代码中使用方式如下：**

1、假如有按钮**在输入框一栏** 打开底部隐藏菜单时 详情可看[Example1Activity](https://github.com/FlyJingFish/SwitchKeyboard/blob/master/app/src/main/java/com/flyjingfish/switchkeyboard/Example1Activity.java) 对应布局 [activity_example1](https://github.com/FlyJingFish/SwitchKeyboard/blob/master/app/src/main/res/layout/activity_example1.xml)

2、假如有按钮**不在输入框一栏时** 打开底部隐藏菜单时 详情可看[Example2Activity](https://github.com/FlyJingFish/SwitchKeyboard/blob/master/app/src/main/java/com/flyjingfish/switchkeyboard/Example2Activity.java) 对应布局 [activity_example2](https://github.com/FlyJingFish/SwitchKeyboard/blob/master/app/src/main/res/layout/activity_example2.xml)

3、如果**在Fragment中使用** 详情可看[Example3Activity](https://github.com/FlyJingFish/SwitchKeyboard/blob/master/app/src/main/java/com/flyjingfish/switchkeyboard/Example3Activity.java) 对应Fragment [Example3Fragment](https://github.com/FlyJingFish/SwitchKeyboard/blob/master/app/src/main/java/com/flyjingfish/switchkeyboard/Example3Fragment.java)

4、如果**在DialogFragment中使用** 详情可看[ExampleDialogActivity](https://github.com/FlyJingFish/SwitchKeyboard/blob/master/app/src/main/java/com/flyjingfish/switchkeyboard/ExampleDialogActivity.java) 对应DialogFragment [InputDialog](https://github.com/FlyJingFish/SwitchKeyboard/blob/master/app/src/main/java/com/flyjingfish/switchkeyboard/InputDialog.java)

## 混淆规则

```pro

-keep class com.flyjingfish.switchkeyboardlib.** { *; }

```

## 特别注意(如果不注意的话可能会显示异常)

1、本工具类设置了布局全屏显示,并设置了透明状态栏，请自行适配顶部返回键一栏距离顶部的距离（可搭配使用[TitleBar](https://github.com/FlyJingFish/TitleBar),使用教程看[BaseActivity](https://github.com/FlyJingFish/SwitchKeyboard/blob/master/app/src/main/java/com/flyjingfish/switchkeyboard/BaseActivity.java)）和状态栏字体颜色

**PS:** 如果你设置状态栏字体颜色后出现问题，可在你的代码后**再次调用 setSystemUi() 来修正**

（正常来说如果你在onCreate 中设置过一次字体颜色是没有问题的，但如果在其他地方还有设置字体颜色是有可能影响的,原因是你切换字体颜色的代码影响了原本的设置）

2、布局中不可使用 fitsSystemWindows 属性为true，本库目前已将布局中影响较为大的几个节点自动设置为了false

**PS:** 如果你非要设置 fitsSystemWindows 将会导致显示异常，所以请尽可能的不要使用这个属性

## 关于打开页面时键盘的状态

1、如果想自动弹出键盘，可通过设置**switchKeyboardUtil.setAutoShowKeyboard()** 或者如果是Activity您可在 AndroidManifest.xml 中设置 windowSoftInputMode 为 stateAlwaysVisible 或 stateVisible

2、有些机型会自动打开键盘，可设置 windowSoftInputMode 模式来解决，例如你不希望打开键盘就可以设置 stateAlwaysHidden 或 stateHidden


## 最后推荐我写的另外一些库

- [OpenImage 轻松实现在应用内点击小图查看大图的动画放大效果](https://github.com/FlyJingFish/OpenImage)

- [AndroidAOP 一个注解就可请求权限，禁止多点，切换线程等等，更可定制出属于你的 Aop 代码](https://github.com/FlyJingFish/AndroidAOP)

- [主页查看更多开源库](https://github.com/FlyJingFish)



