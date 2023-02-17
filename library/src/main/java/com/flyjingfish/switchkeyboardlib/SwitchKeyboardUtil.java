package com.flyjingfish.switchkeyboardlib;

import android.app.Activity;
import android.view.KeyEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SwitchKeyboardUtil extends BaseSwitchKeyboardUtil {
    private OnKeyboardMenuListener onKeyboardMenuListener;
    private final MenuModeView IDLE = new MenuModeView(null,null);
    private MenuModeView menuMode = IDLE;
    private final List<MenuModeView> menuModeViews = new ArrayList<>();
    private View lastVisibleView;

    public SwitchKeyboardUtil(Activity activity) {
        super(activity);
    }

    /**
     *
     * @param menuModeView 展开的点击按钮和对应的隐藏布局
     */
    public void setToggleMenuViews(MenuModeView... menuModeView) {
        this.menuModeViews.addAll(Arrays.asList(menuModeView));
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
                if (menuMode == IDLE){
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
                menuMode = IDLE;
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
        for (MenuModeView menuModeView : menuModeViews) {
            menuModeView.clickToggleView.setOnClickListener(v -> {
                if (menuModeView.clickToggleViewIsMenuContainer){
                    switchMenuIn(menuModeView);
                }else {
                    switchMenu(menuModeView);
                }
            });
            if (menuModeView.backView != null){
                menuModeView.backView.setOnClickListener(v -> {
                    for (MenuModeView menuModeView2 : menuModeViews) {
                        menuModeView2.toggleViewContainer.setVisibility(View.GONE);
                    }
                    lastVisibleView.setVisibility(View.VISIBLE);
                });
            }
        }

        etContent.setOnTouchListener((v, event) -> {
            menuMode = IDLE;
            return false;
        });
    }

    private void switchMenu(MenuModeView clickViewMenuMode){
        recordLastVisibleView();
        if (menuMode == IDLE){
            menuMode = clickViewMenuMode;
            toggleMoreView();
            for (MenuModeView menuModeView : menuModeViews) {
                menuModeView.toggleViewContainer.setVisibility(clickViewMenuMode == menuModeView?View.VISIBLE:View.GONE);
            }
        }else if (menuMode != clickViewMenuMode){
            menuMode = clickViewMenuMode;
            for (MenuModeView menuModeView : menuModeViews) {
                menuModeView.toggleViewContainer.setVisibility(clickViewMenuMode == menuModeView?View.VISIBLE:View.GONE);
            }
        }else {
            toggleMoreView();
        }
    }

    private void switchMenuIn(MenuModeView clickViewMenuMode){
        recordLastVisibleView();
        for (MenuModeView menuModeView : menuModeViews) {
            menuModeView.toggleViewContainer.setVisibility(clickViewMenuMode == menuModeView?View.VISIBLE:View.GONE);
        }
    }

    private void recordLastVisibleView(){
        for (MenuModeView menuModeView : menuModeViews) {
            if (menuModeView.toggleViewContainer.getVisibility() == View.VISIBLE){
                lastVisibleView = menuModeView.toggleViewContainer;
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (super.onKeyDown(keyCode, event)){
            menuMode = IDLE;
            return true;
        }
        return false;
    }

    @Override
    public void setOnKeyboardMenuListener(OnKeyboardMenuListener onKeyboardMenuListener) {
        this.onKeyboardMenuListener = onKeyboardMenuListener;
    }

}
