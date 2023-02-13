package com.flyjingfish.switchkeyboardlib;

import android.app.Activity;
import android.view.KeyEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;

public class Example2SwitchKeyboardUtil extends BaseSwitchKeyboardUtil {
    private View moreBtn;

    private OnKeyboardMenuListener onKeyboardMenuListener;
    private MenuMode menuMode = MenuMode.IDLE;

    private enum MenuMode{
        IDLE,MENU_BTN;
    }

    public Example2SwitchKeyboardUtil(Activity activity) {
        super(activity);
    }

    public Example2SwitchKeyboardUtil(Activity activity, boolean menuViewHeightEqualKeyboard) {
        super(activity, menuViewHeightEqualKeyboard);
    }

    /**
     *
     * @param moreBtn 更多按钮
     */
    public void setMoreViews(@NonNull View moreBtn) {
        this.moreBtn = moreBtn;
    }

    @Override
    protected void onCreate(@NonNull LifecycleOwner owner) {
        super.onCreate(owner);
        super.setOnKeyboardMenuListener(new OnKeyboardMenuListener() {
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
            if (menuMode == MenuMode.IDLE){
                menuMode = MenuMode.MENU_BTN;
                toggleMoreView();
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
