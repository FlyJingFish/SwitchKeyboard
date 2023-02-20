package com.flyjingfish.switchkeyboardlib;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;

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
    private MenuModeView lastMenuModeView;
    private int lastViewHeight;

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
                if (menuMode == IDLE){
                    isShowMenu  = false;
                }
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

            @Override
            public void onShowMenuLayout(View layoutView) {
                if (onKeyboardMenuListener != null) {
                    onKeyboardMenuListener.onShowMenuLayout(layoutView);
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
                    if (lastVisibleView != null){
                        lastVisibleView.setVisibility(View.VISIBLE);
                    }
                    setSwitchAnim(lastMenuModeView);
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
            setSwitchAnim(clickViewMenuMode);
            if (onKeyboardMenuListener != null){
                onKeyboardMenuListener.onShowMenuLayout(clickViewMenuMode.toggleViewContainer);
            }
        }else if (menuMode != clickViewMenuMode){
            menuMode = clickViewMenuMode;
            for (MenuModeView menuModeView : menuModeViews) {
                menuModeView.toggleViewContainer.setVisibility(clickViewMenuMode == menuModeView?View.VISIBLE:View.GONE);
            }
            setSwitchAnim(clickViewMenuMode);
            if (onKeyboardMenuListener != null){
                onKeyboardMenuListener.onShowMenuLayout(clickViewMenuMode.toggleViewContainer);
            }
        }else {
            boolean showMenu = toggleMoreView();
            if (showMenu){
                for (MenuModeView menuModeView : menuModeViews) {
                    menuModeView.toggleViewContainer.setVisibility(clickViewMenuMode == menuModeView?View.VISIBLE:View.GONE);
                }
                setSwitchAnim(clickViewMenuMode);
                if (onKeyboardMenuListener != null){
                    onKeyboardMenuListener.onShowMenuLayout(clickViewMenuMode.toggleViewContainer);
                }
            }
        }
    }

    private void switchMenuIn(MenuModeView clickViewMenuMode){
        recordLastVisibleView();
        for (MenuModeView menuModeView : menuModeViews) {
            menuModeView.toggleViewContainer.setVisibility(clickViewMenuMode == menuModeView?View.VISIBLE:View.GONE);
        }
        setSwitchAnim(clickViewMenuMode);
    }

    private void setSwitchAnim(MenuModeView clickViewMenuMode){
        if (lastVisibleView == null || !useSwitchAnim){
            return;
        }
        clickViewMenuMode.toggleViewContainer.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                stopSwitchAnim();
                clickViewMenuMode.toggleViewContainer.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                ViewHeight viewHeight = new ViewHeight(menuViewContainer);
                clickViewMenuMode.toggleViewContainer.measure(0,0);
                int startHeight = menuViewContainer.getHeight();
                int endHeight = clickViewMenuMode.toggleViewContainer.getMeasuredHeight();
                int distance = Math.abs(startHeight - endHeight);
                switchAnim = ObjectAnimator.ofInt(viewHeight,"viewHeight",startHeight,endHeight);
                switchAnim.setDuration(distance/SWITCH_ANIM_SPEED);
                switchAnim.start();
            }
        });
    }



    private void recordLastVisibleView(){
        for (MenuModeView menuModeView : menuModeViews) {
            if (menuModeView.toggleViewContainer.getVisibility() == View.VISIBLE){
                lastVisibleView = menuModeView.toggleViewContainer;
                lastMenuModeView = menuModeView;
            }
        }
        if (lastVisibleView != null){
            lastViewHeight = lastVisibleView.getHeight();
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
