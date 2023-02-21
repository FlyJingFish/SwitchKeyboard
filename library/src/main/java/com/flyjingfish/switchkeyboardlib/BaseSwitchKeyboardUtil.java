package com.flyjingfish.switchkeyboardlib;

import static android.util.TypedValue.COMPLEX_UNIT_DIP;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;


public class BaseSwitchKeyboardUtil {
    protected SystemKeyboardUtils keyboardUtils;
    protected Handler handler = new Handler(Looper.getMainLooper());
    protected boolean isShowMenu;
    protected View audioBtn;
    protected View audioTouchVIew;
    protected EditText etContent;
    protected Activity activity;
    protected ViewGroup menuViewContainer;
    protected boolean menuViewHeightEqualKeyboard;
    protected boolean useSwitchAnim;
    protected boolean keyboardIsShow;
    protected int keyboardHeight;
    protected static final int SWITCH_ANIM_SPEED = 2;
    private View.OnTouchListener etContentOnTouchListener;
    private boolean isRecordKeyboardHeight;

    public BaseSwitchKeyboardUtil(Activity activity) {
        this.activity = activity;
    }

    /**
     *
     * @param menuViewHeightEqualKeyboard 是否让菜单高度和键盘高度一样（首次可能会有误差）
     */
    public void setMenuViewHeightEqualKeyboard(boolean menuViewHeightEqualKeyboard) {
        this.menuViewHeightEqualKeyboard = menuViewHeightEqualKeyboard;
    }

    /**
     *
     * @param etContent 文本消息输入框（不可为空）
     */
    public void setInputEditText(@NonNull EditText etContent) {
        this.etContent = etContent;
    }

    /**
     *
     * @param audioBtn 语音消息按钮（可为空）
     */
    public void setAudioBtn(@Nullable View audioBtn) {
        this.audioBtn = audioBtn;
    }

    /**
     *
     * @param audioTouchVIew 语音消息按住说话按钮（可为空）
     */
    public void setAudioTouchVIew(@Nullable View audioTouchVIew) {
        this.audioTouchVIew = audioTouchVIew;
    }

    /**
     *
     * @param menuViewContainer 菜单总包裹布局（不可为空）
     */
    public void setMenuViewContainer(@NonNull FrameLayout menuViewContainer) {
        this.menuViewContainer = menuViewContainer;
    }

    /**
     *
     * @param menuViewContainer 菜单总包裹布局（不可为空）
     */
    public void setMenuViewContainer(@NonNull RelativeLayout menuViewContainer) {
        this.menuViewContainer = menuViewContainer;
    }

    /**
     *
     * @param menuViewContainer 菜单总包裹布局（不可为空）
     */
    public void setMenuViewContainer(@NonNull LinearLayout menuViewContainer) {
        this.menuViewContainer = menuViewContainer;
    }

    protected void saveKeyboardHeight(int value){
        SharedPreferences sp = activity.getApplication().getSharedPreferences("SwitchKeyboardData", Context.MODE_PRIVATE);
        sp.edit().putInt("KeyboardHeight", value).apply();
    }

    protected int getKeyboardHeight(){
        SharedPreferences sp = activity.getApplication().getSharedPreferences("SwitchKeyboardData", Context.MODE_PRIVATE);
        isRecordKeyboardHeight = sp.contains("KeyboardHeight");
        return sp.getInt("KeyboardHeight", (int) TypedValue.applyDimension(COMPLEX_UNIT_DIP,240,activity.getResources().getDisplayMetrics()));
    }

    /**
     * 根据生命周期来确保不发生内存泄漏
     * @param lifecycleOwner
     */
    public void attachLifecycle(LifecycleOwner lifecycleOwner){
        lifecycleOwner.getLifecycle().addObserver(new MyLifecycleEventObserver());
    }

    protected class MyLifecycleEventObserver implements LifecycleEventObserver{
        @Override
        public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {
            if (event == Lifecycle.Event.ON_CREATE){
                onCreate(source);
            } else if (event == Lifecycle.Event.ON_DESTROY){
                onDestroy(source);
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    protected void onCreate(@NonNull LifecycleOwner owner){
        keyboardUtils = new SystemKeyboardUtils(activity);
        keyboardUtils.setOnKeyBoardListener(onKeyBoardListener);
        setSystemUi();
        int menuHeight = getKeyboardHeight();
        if (menuViewHeightEqualKeyboard){
            ViewGroup.LayoutParams layoutParams = menuViewContainer.getLayoutParams();
            layoutParams.height = menuHeight;
            menuViewContainer.setLayoutParams(layoutParams);
        }
        if (isRecordKeyboardHeight){
            keyboardHeight  = menuHeight;
        }
        if (audioBtn != null){
            audioBtn.setOnClickListener(v -> {

                if (keyboardIsShow){
                    hideKeyboard();
                    menuViewContainer.setVisibility(View.GONE);
                    etContent.setVisibility(View.GONE);
                    if (audioTouchVIew != null){
                        audioTouchVIew.setVisibility(View.VISIBLE);
                    }
                    if (onKeyboardMenuListener != null){
                        onKeyboardMenuListener.onShowMenuLayout(audioTouchVIew);
                    }
                }else if (menuViewContainer.getVisibility() == View.VISIBLE){
                    menuViewContainer.setVisibility(View.GONE);
                    etContent.setVisibility(View.GONE);
                    if (audioTouchVIew != null){
                        audioTouchVIew.setVisibility(View.VISIBLE);
                    }
                    if (onKeyboardMenuListener != null){
                        onKeyboardMenuListener.onShowMenuLayout(audioTouchVIew);
                    }
                }else if (audioTouchVIew != null && audioTouchVIew.getVisibility() == View.GONE){
                    etContent.setVisibility(View.GONE);
                    audioTouchVIew.setVisibility(View.VISIBLE);
                    if (onKeyboardMenuListener != null){
                        onKeyboardMenuListener.onShowMenuLayout(audioTouchVIew);
                    }
                }else {
                    etContent.setVisibility(View.VISIBLE);
                    if (audioTouchVIew != null){
                        audioTouchVIew.setVisibility(View.GONE);
                    }
                    showKeyboard();
                }
                isShowMenu = false;
            });
        }

        etContent.setOnTouchListener((v, event) -> {
            if (!keyboardIsShow && event.getAction() == MotionEvent.ACTION_DOWN){
                showKeyboardAnim();
            }
            if (etContentOnTouchListener != null){
                return etContentOnTouchListener.onTouch(v,event);
            }
            return false;
        });
    }

    protected void onDestroy(@NonNull LifecycleOwner owner){
        keyboardUtils.onDestroy();
        handler.removeCallbacksAndMessages(null);
        stopSwitchAnim();
    }

    protected void showKeyboardAnim(){
        if (useSwitchAnim){
            int startHeight = menuViewContainer.getHeight();
            if (startHeight != keyboardHeight && keyboardHeight > 0){
                stopSwitchAnim();
                ViewHeight viewHeight = new ViewHeight(menuViewContainer);
                int distance = Math.abs(startHeight - keyboardHeight);
                int duration = distance/SWITCH_ANIM_SPEED;
                if (duration<200){
                    duration = 200;
                }
                switchAnim = ObjectAnimator.ofInt(viewHeight,"viewHeight",startHeight,keyboardHeight);
                switchAnim.setDuration(duration);
                switchAnim.start();
            }
        }
    }

    public void hideKeyboard() {
        if (onKeyboardMenuListener != null){
            onKeyboardMenuListener.onCallHideKeyboard();
        }
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(etContent.getWindowToken(), 0);
        etContent.clearFocus();
    }

    protected void showKeyboard() {
        if (onKeyboardMenuListener != null){
            onKeyboardMenuListener.onCallShowKeyboard();
        }
        etContent.setSelection(etContent.getText().length());
        etContent.requestFocus();
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(etContent, 0);
    }

    public void setSystemUi(){
        Window window = activity.getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            int flag = window.getDecorView().getSystemUiVisibility();
            window.getDecorView().setSystemUiVisibility(flag | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        } else {
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && isShowMenu){
            menuViewContainer.setVisibility(View.GONE);
            isShowMenu = false;
            if (onKeyboardMenuListener != null){
                onKeyboardMenuListener.onHideMenuViewContainer();
            }
            return true;
        }
        return false;
    }

    protected ObjectAnimator switchAnim;
    private final SystemKeyboardUtils.OnKeyBoardListener onKeyBoardListener = new SystemKeyboardUtils.OnKeyBoardListener() {
        @Override
        public void onShow(int height) {
            stopSwitchAnim();
            ViewGroup.LayoutParams layoutParams = menuViewContainer.getLayoutParams();
            layoutParams.height = height;
            menuViewContainer.setLayoutParams(layoutParams);
//            int startHeight = menuViewContainer.getHeight();
//            if (menuViewHeightEqualKeyboard || startHeight <= height || !useSwitchAnim){
//                ViewGroup.LayoutParams layoutParams = menuViewContainer.getLayoutParams();
//                layoutParams.height = height;
//                menuViewContainer.setLayoutParams(layoutParams);
//            }else {
//                stopSwitchAnim();
//                ViewHeight viewHeight = new ViewHeight(menuViewContainer);
//                int distance = Math.abs(startHeight - height);
//                int duration = distance/SWITCH_ANIM_SPEED;
//                if (duration<200){
//                    duration = 200;
//                }
//                switchAnim = ObjectAnimator.ofInt(viewHeight,"viewHeight",startHeight,height);
//                switchAnim.setDuration(duration);
//                switchAnim.start();
//            }

            menuViewContainer.setVisibility(View.INVISIBLE);
            saveKeyboardHeight(height);
            keyboardHeight = height;
            if (onKeyboardMenuListener != null){
                onKeyboardMenuListener.onKeyboardShow(height);
                onKeyboardMenuListener.onScrollToBottom();
            }
            keyboardIsShow = true;
        }

        @Override
        public void onHide(int height) {
            if (onKeyboardMenuListener != null){
                onKeyboardMenuListener.onKeyboardHide(height);
            }
            if (!(BaseSwitchKeyboardUtil.this instanceof SwitchKeyboardUtil) && !isShowMenu){
                menuViewContainer.setVisibility(View.GONE);
            }
            keyboardIsShow = false;
        }
    };

    protected void stopSwitchAnim(){
        if (switchAnim != null){
            switchAnim.cancel();
        }
    }

    public static class ViewHeight{
        private final View view;

        public ViewHeight(View view) {
            this.view = view;
        }

        public void setViewHeight(int height){
            ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
            layoutParams.height = height;
            view.setLayoutParams(layoutParams);
        }
    }
    /**
     * 切换键盘和更多菜单
     */
    public boolean toggleMoreView(){
        if (!isShowMenu){
            isShowMenu = true;
            menuViewContainer.setVisibility(View.VISIBLE);
            if (audioTouchVIew != null){
                audioTouchVIew.setVisibility(View.GONE);
            }
            etContent.setVisibility(View.VISIBLE);
            if (!menuViewHeightEqualKeyboard && !useSwitchAnim){
                handler.postDelayed(() -> {
                    ViewGroup.LayoutParams layoutParams = menuViewContainer.getLayoutParams();
                    layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                    menuViewContainer.setLayoutParams(layoutParams);
                },200);
            }

            hideKeyboard();
        }else {
            isShowMenu = false;
            etContent.setVisibility(View.VISIBLE);
            if (audioTouchVIew != null){
                audioTouchVIew.setVisibility(View.GONE);
            }
            showKeyboard();
        }
        if (onKeyboardMenuListener != null){
            onKeyboardMenuListener.onScrollToBottom();
        }
        return isShowMenu;
    }

    private OnKeyboardMenuListener onKeyboardMenuListener;

    public interface OnKeyboardMenuListener{
        void onScrollToBottom();
        void onCallShowKeyboard();
        void onCallHideKeyboard();
        void onKeyboardHide(int keyboardHeight);
        void onKeyboardShow(int keyboardHeight);
        void onShowMenuLayout(View layoutView);
        void onHideMenuViewContainer();
    }

    /**
     * 设置监听
     * @param onKeyboardMenuListener
     */
    public void setOnKeyboardMenuListener(OnKeyboardMenuListener onKeyboardMenuListener) {
        this.onKeyboardMenuListener = onKeyboardMenuListener;
    }

    public void checkSoftMode(){
        Window window = activity.getWindow();
        final WindowManager.LayoutParams attrs = window.getAttributes();
        int softMode = attrs.softInputMode;
        if (((softMode | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING) == softMode)
                ||((softMode | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN) == softMode)
                ||((softMode | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE) == softMode)){
            int newSoftMode = WindowManager.LayoutParams.SOFT_INPUT_STATE_UNSPECIFIED;
            if ((softMode | WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN) == softMode){
                newSoftMode |= WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN;
            }
            if ((softMode | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) == softMode){
                newSoftMode |= WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN;
            }
            if ((softMode | WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE) == softMode){
                newSoftMode |= WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE;
            }
            if ((softMode | WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE) == softMode){
                newSoftMode |= WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE;
            }
            if ((softMode | WindowManager.LayoutParams.SOFT_INPUT_STATE_UNCHANGED) == softMode){
                newSoftMode |= WindowManager.LayoutParams.SOFT_INPUT_STATE_UNCHANGED;
            }
            window.setSoftInputMode(newSoftMode|WindowManager.LayoutParams.SOFT_INPUT_ADJUST_UNSPECIFIED);
        }
    }

    public boolean isUseSwitchAnim() {
        return useSwitchAnim;
    }

    public void setUseSwitchAnim(boolean useSwitchAnim) {
        this.useSwitchAnim = useSwitchAnim;
    }

    public void setEtContentOnTouchListener(View.OnTouchListener etContentOnTouchListener) {
        this.etContentOnTouchListener = etContentOnTouchListener;
    }

    public void hideMenuAndKeyboard(){
        if (isShowMenu){
            menuViewContainer.setVisibility(View.GONE);
            isShowMenu = false;
            if (onKeyboardMenuListener != null){
                onKeyboardMenuListener.onHideMenuViewContainer();
            }
        }else {
            hideKeyboard();
        }
    }
}
