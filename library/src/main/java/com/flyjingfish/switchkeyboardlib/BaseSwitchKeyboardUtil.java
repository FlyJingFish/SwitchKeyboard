package com.flyjingfish.switchkeyboardlib;

import static android.util.TypedValue.COMPLEX_UNIT_DIP;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
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
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
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
    protected LifecycleOwner lifecycleOwner;
    protected ViewGroup menuViewContainer;
    protected boolean menuViewHeightEqualKeyboard;
    protected boolean useSwitchAnim = true;
    protected boolean useMenuUpAnim = false;
    protected boolean keyboardIsShow;
    protected int keyboardHeight;
    protected static final int SWITCH_ANIM_SPEED = 2;
    private View.OnTouchListener etContentOnTouchListener;
    private boolean isRecordKeyboardHeight;
    protected boolean isAutoShowKeyboard;
    protected boolean isAutoSetFitsSystemWindowsFalse = true;
    protected AutoShowKeyboardType autoShowKeyboardType;

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
    public void setAudioTouchView(@Nullable View audioTouchVIew) {
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

    public boolean isAutoShowKeyboard() {
        return isAutoShowKeyboard;
    }

    /**
     * 设置显示页面时是否自动弹出键盘
     * @param autoShowKeyboard 是否自动弹出键盘
     * @param autoShowKeyboardType 自动弹出的类型，传入{@link AutoShowKeyboardType#FIRST_SHOW}则只是首次进入页面时才自动弹出键盘
     *                             传入{@link AutoShowKeyboardType#ALWAYS_SHOW}则是每次显示页面时都会自动弹出键盘
     */
    public void setAutoShowKeyboard(boolean autoShowKeyboard,AutoShowKeyboardType autoShowKeyboardType) {
        isAutoShowKeyboard = autoShowKeyboard;
        this.autoShowKeyboardType = autoShowKeyboardType;
    }
    /**
     * 设置显示页面时是否自动弹出键盘，默认首次为首次进入页面自动弹出键盘
     * @param autoShowKeyboard 是否自动弹出键盘
     */
    public void setAutoShowKeyboard(boolean autoShowKeyboard) {
        setAutoShowKeyboard(autoShowKeyboard,AutoShowKeyboardType.FIRST_SHOW);
    }

    /**
     * 是否自动设置菜单栏及其父布局 {@link View#setFitsSystemWindows} 为 false （默认自动设置）
     * @param autoSetFitsSystemWindowsFalse false为不自动设置，如果您布局中存在 fitsSystemWindows 为true 的情况，这将导致显示不正常。此项默认为true
     */
    public void setAutoSetFitsSystemWindowsFalse(boolean autoSetFitsSystemWindowsFalse) {
        isAutoSetFitsSystemWindowsFalse = autoSetFitsSystemWindowsFalse;
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
     * @param fragmentActivity View 在{@link FragmentActivity}中
     */
    public void attachLifecycle(FragmentActivity fragmentActivity){
        attachLifecycleOwner(fragmentActivity);
    }

    /**
     * 根据生命周期来确保不发生内存泄漏
     * @param fragment View 在{@link Fragment}中
     */
    public void attachLifecycle(Fragment fragment){
        attachLifecycleOwner(fragment);
    }

    /**
     * 根据生命周期来确保不发生内存泄漏
     * @param dialogFragment View 在{@link DialogFragment}中
     */
    public void attachLifecycle(DialogFragment dialogFragment){
        attachLifecycleOwner(dialogFragment);
    }

    protected void attachLifecycleOwner(LifecycleOwner lifecycleOwner){
        this.lifecycleOwner = lifecycleOwner;
        if (lifecycleOwner instanceof Fragment){
            lifecycleOwner = ((Fragment) lifecycleOwner).getViewLifecycleOwner();
        }
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
        keyboardUtils = new SystemKeyboardUtils(activity,lifecycleOwner instanceof DialogFragment);
        keyboardUtils.setOnKeyBoardListener(onKeyBoardListener);

        if (isAutoSetFitsSystemWindowsFalse){
            setFitsSystemWindows(etContent);
            setFitsSystemWindows(menuViewContainer);
        }

        setSystemUi(activity.getWindow());
        if (lifecycleOwner instanceof DialogFragment){
            Dialog dialog = ((DialogFragment) lifecycleOwner).requireDialog();
            setSystemUi(dialog.getWindow());
            checkSoftMode(activity.getWindow(),false);
            checkSoftMode(dialog.getWindow(),isAutoShowKeyboard);
        }else {
            checkSoftMode(activity.getWindow(),isAutoShowKeyboard);
        }
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            Window window;
            if (lifecycleOwner instanceof DialogFragment){
                Dialog dialog = ((DialogFragment) lifecycleOwner).requireDialog();
                window = dialog.getWindow();
            }else {
                window = activity.getWindow();
            }
            SystemUiVisibilityListener listener = new SystemUiVisibilityListener();
            window.getDecorView().setOnApplyWindowInsetsListener(listener);
            window.getDecorView().getViewTreeObserver().addOnGlobalLayoutListener(listener);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT_WATCH)
    private class SystemUiVisibilityListener implements View.OnApplyWindowInsetsListener,ViewTreeObserver.OnGlobalLayoutListener {
        private int lastFlag;
        private int lastHeight;
        @Override
        public WindowInsets onApplyWindowInsets(View v, WindowInsets insets) {
            if (!hasFullScreenFlag()){
                if (keyboardIsShow){
                    ViewGroup.LayoutParams layoutParams = menuViewContainer.getLayoutParams();
                    lastHeight = layoutParams.height;
                    layoutParams.height = 0;
                    menuViewContainer.setLayoutParams(layoutParams);
//                    menuViewContainer.setVisibility(View.GONE);
                }
            }
            return v.onApplyWindowInsets(insets);
        }

        @Override
        public void onGlobalLayout() {
            Window window;
            if (lifecycleOwner instanceof DialogFragment){
                Dialog dialog = ((DialogFragment) lifecycleOwner).requireDialog();
                window = dialog.getWindow();
            }else {
                window = activity.getWindow();
            }
            int flag = window.getDecorView().getSystemUiVisibility();
            if (!hasFullScreenFlag()){
                setSystemUi(window);
            }
            if (!hasFullScreenFlag(lastFlag)){
                if (keyboardIsShow){
                    ViewGroup.LayoutParams layoutParams = menuViewContainer.getLayoutParams();
                    layoutParams.height = lastHeight;
                    menuViewContainer.setLayoutParams(layoutParams);
//                    handler.postDelayed(() -> menuViewContainer.setVisibility(View.VISIBLE),150);
                }else {
                    menuViewContainer.requestLayout();
                }
            }
            lastFlag = flag;
        }
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
                ObjectAnimator switchAnim1 = ObjectAnimator.ofInt(viewHeight,"viewHeight",startHeight,keyboardHeight);
                switchAnim1.setDuration(duration);
                switchAnim = new AnimatorSet();
                switchAnim.playTogether(switchAnim1);
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
        Window window;
        if (lifecycleOwner instanceof DialogFragment){
            Dialog dialog = ((DialogFragment) lifecycleOwner).requireDialog();
            window = dialog.getWindow();
        }else {
            window = activity.getWindow();
        }
        setSystemUi(window);
    }

    public void setSystemUi(Window window){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            int flag = window.getDecorView().getSystemUiVisibility();
            window.getDecorView().setSystemUiVisibility(flag | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        } else {
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    private boolean hasFullScreenFlag(){
        Window window;
        if (lifecycleOwner instanceof DialogFragment){
            Dialog dialog = ((DialogFragment) lifecycleOwner).requireDialog();
            window = dialog.getWindow();
        }else {
            window = activity.getWindow();
        }
        int flag = window.getDecorView().getSystemUiVisibility();
        return hasFullScreenFlag(flag);
    }

    private boolean hasFullScreenFlag(int flag){
        return (flag & View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN) == View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
    }

    private void setFitsSystemWindows(View view){
        view.setFitsSystemWindows(false);
        if (view.getParent() instanceof ViewGroup && ((ViewGroup) view.getParent()).getId() != Window.ID_ANDROID_CONTENT){
            setFitsSystemWindows((View) view.getParent());
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

    protected AnimatorSet switchAnim;
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
            etContent.post(() -> etContent.requestFocus());
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
        checkSoftMode(activity.getWindow(),false);
    }

    public void checkSoftMode(Window window,boolean isSetAutoShowKeyboardFlag){
        final WindowManager.LayoutParams attrs = window.getAttributes();
        int softMode = attrs.softInputMode;

        if (((softMode & WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING) == WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
                ||((softMode & WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN) == WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
                ||((softMode & WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE) == WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)){

            int newSoftMode = softMode;
            newSoftMode &= ~WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING;
            newSoftMode &= ~WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN;
            newSoftMode &= ~WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE;
            attrs.softInputMode = newSoftMode|WindowManager.LayoutParams.SOFT_INPUT_ADJUST_UNSPECIFIED;
            if (isSetAutoShowKeyboardFlag){
                attrs.softInputMode = attrs.softInputMode|((lifecycleOwner instanceof DialogFragment)?WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE:(autoShowKeyboardType == AutoShowKeyboardType.ALWAYS_SHOW?WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE:WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE));
            }
            window.setAttributes(attrs);
        }else {
            if (isSetAutoShowKeyboardFlag){
                attrs.softInputMode = softMode|((lifecycleOwner instanceof DialogFragment)?WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE:(autoShowKeyboardType == AutoShowKeyboardType.ALWAYS_SHOW?WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE:WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE));
                window.setAttributes(attrs);
            }
        }
    }

    public boolean isUseSwitchAnim() {
        return useSwitchAnim;
    }

    public void setUseSwitchAnim(boolean useSwitchAnim) {
        this.useSwitchAnim = useSwitchAnim;
    }

    public boolean isUseMenuUpAnim() {
        return useMenuUpAnim;
    }

    public void setUseMenuUpAnim(boolean useMenuUpAnim) {
        this.useMenuUpAnim = useMenuUpAnim;
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
