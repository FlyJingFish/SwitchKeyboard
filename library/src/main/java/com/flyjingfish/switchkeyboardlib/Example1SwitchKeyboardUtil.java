package com.flyjingfish.switchkeyboardlib;

import android.app.Activity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;

public class Example1SwitchKeyboardUtil extends BaseSwitchKeyboardUtil {
    private View faceBtn;
    private View moreBtn;
    private ViewGroup menuButtons;
    private ViewGroup faceViews;

    private OnKeyboardMenuListener onKeyboardMenuListener;
    private MenuMode menuMode = MenuMode.IDLE;

    private enum MenuMode{
        IDLE,FACE,MENU_BTN;
    }

    public Example1SwitchKeyboardUtil(Activity activity) {
        super(activity);
    }

    public Example1SwitchKeyboardUtil(Activity activity, boolean menuViewHeightEqualKeyboard) {
        super(activity, menuViewHeightEqualKeyboard);
    }

    /**
     *
     * @param faceBtn 表情按钮
     * @param moreBtn 更多按钮
     * @param menuButtons 更多按钮布局
     * @param faceViews 表情布局
     */
    public void setMoreViews(@NonNull View faceBtn,@NonNull View moreBtn,@NonNull ViewGroup menuButtons,@NonNull ViewGroup faceViews) {
        this.faceBtn = faceBtn;
        this.moreBtn = moreBtn;
        this.menuButtons = menuButtons;
        this.faceViews = faceViews;
    }

    @Override
    protected void onCreate(@NonNull LifecycleOwner owner) {
        super.onCreate(owner);
        super.setOnKeyboardMenuListener(new BaseSwitchKeyboardUtil.OnKeyboardMenuListener() {
            @Override
            public void onScrollToBottom() {
                if (onKeyboardMenuListener != null) {
                    onKeyboardMenuListener.onScrollToBottom();
                }
            }

            @Override
            public void onKeyboardHide(int keyboardHeight) {
                if (menuMode == MenuMode.IDLE){
                    menuViewContainer.setVisibility(View.GONE);
                }
                if (onKeyboardMenuListener != null) {
                    onKeyboardMenuListener.onKeyboardHide(keyboardHeight);
                }
            }

            @Override
            public void onKeyboardShow(int keyboardHeight) {
                if (onKeyboardMenuListener != null) {
                    onKeyboardMenuListener.onKeyboardShow(keyboardHeight);
                }
            }

            @Override
            public void onCallShowKeyboard() {
                menuMode = MenuMode.IDLE;
                if (onKeyboardMenuListener != null) {
                    onKeyboardMenuListener.onCallShowKeyboard();
                }
            }

            @Override
            public void onCallHideKeyboard() {
                if (onKeyboardMenuListener != null) {
                    onKeyboardMenuListener.onCallHideKeyboard();
                }
            }
        });
        moreBtn.setOnClickListener(v -> {
            menuButtons.setVisibility(View.VISIBLE);
            faceViews.setVisibility(View.GONE);
            if (menuMode == MenuMode.IDLE){
                menuMode = MenuMode.MENU_BTN;
                toggleMoreView();
            }else if (menuMode == MenuMode.FACE){
                menuMode = MenuMode.MENU_BTN;
            }else {
                toggleMoreView();
            }
        });
        faceBtn.setOnClickListener(v -> {
            menuButtons.setVisibility(View.GONE);
            faceViews.setVisibility(View.VISIBLE);
            if (menuMode == MenuMode.IDLE){
                menuMode = MenuMode.FACE;
                toggleMoreView();
            }else if (menuMode == MenuMode.MENU_BTN){
                menuMode = MenuMode.FACE;
            }else {
                toggleMoreView();
            }
        });
        etContent.setOnTouchListener((v, event) -> {
            menuMode = MenuMode.IDLE;
            return false;
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (super.onKeyDown(keyCode, event)){
            menuMode = MenuMode.IDLE;
            return true;
        }
        return false;
    }

    @Override
    public void setOnKeyboardMenuListener(OnKeyboardMenuListener onKeyboardMenuListener) {
        this.onKeyboardMenuListener = onKeyboardMenuListener;
    }


}
