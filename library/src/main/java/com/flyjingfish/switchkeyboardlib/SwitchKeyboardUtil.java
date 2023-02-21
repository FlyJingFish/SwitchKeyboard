package com.flyjingfish.switchkeyboardlib;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;

public class SwitchKeyboardUtil extends BaseSwitchKeyboardUtil {
    private OnKeyboardMenuListener onKeyboardMenuListener;
    private final MenuModeView IDLE = new MenuModeView(null,null);
    private MenuModeView menuMode = IDLE;
    private MenuModeView[] menuModeViews;
    private View lastVisibleView;
    private MenuModeView lastMenuModeView;

    public SwitchKeyboardUtil(Activity activity) {
        super(activity);
    }

    /**
     *
     * @param menuModeView 展开的点击按钮和对应的隐藏布局
     */
    public void setToggleMenuViews(MenuModeView... menuModeView) {
        menuModeViews = menuModeView;
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
                if (menuMode == IDLE && menuViewContainer != null){
                    menuViewContainer.setVisibility(View.GONE);
                }
                if (onKeyboardMenuListener != null) {
                    onKeyboardMenuListener.onKeyboardHide(keyboardHeight);
                }
            }

            @Override
            public void onKeyboardShow(int keyboardHeight) {
                menuMode = IDLE;
                isShowMenu  = false;
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

            @Override
            public void onHideMenuViewContainer() {
                if (onKeyboardMenuListener != null) {
                    onKeyboardMenuListener.onHideMenuViewContainer();
                }
            }
        });
        if (menuModeViews != null){
            int childNum = menuViewContainer.getChildCount();
            for (MenuModeView menuModeView : menuModeViews) {
                boolean isHasView = false;
                for (int i = 0; i < childNum; i++) {
                    View view = menuViewContainer.getChildAt(i);
                    if (view == menuModeView.toggleViewContainer){
                        isHasView = true;
                    }
                }
                if (!isHasView){
                    throw new IllegalArgumentException("menuModeView.toggleViewContainer 必须是 menuViewContainer 的子View");
                }
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
                        setSwitchAnim(lastMenuModeView,menuViewContainer.getVisibility());
                    });
                }
            }
        }
    }

    private void switchMenu(MenuModeView clickViewMenuMode){
        recordLastVisibleView();
        int menuViewContainerVisibility = menuViewContainer.getVisibility();
        if (menuMode == IDLE){
            menuMode = clickViewMenuMode;
            toggleMoreView();
            for (MenuModeView menuModeView : menuModeViews) {
                menuModeView.toggleViewContainer.setVisibility(clickViewMenuMode == menuModeView?View.VISIBLE:View.GONE);
            }
            setSwitchAnim(clickViewMenuMode,menuViewContainerVisibility);
            if (onKeyboardMenuListener != null){
                onKeyboardMenuListener.onShowMenuLayout(clickViewMenuMode.toggleViewContainer);
            }
        }else if (menuMode != clickViewMenuMode){
            menuMode = clickViewMenuMode;
            for (MenuModeView menuModeView : menuModeViews) {
                menuModeView.toggleViewContainer.setVisibility(clickViewMenuMode == menuModeView?View.VISIBLE:View.GONE);
            }
            setSwitchAnim(clickViewMenuMode,menuViewContainerVisibility);
            if (onKeyboardMenuListener != null){
                onKeyboardMenuListener.onShowMenuLayout(clickViewMenuMode.toggleViewContainer);
            }
        }else {
            boolean showMenu = toggleMoreView();
            if (showMenu){
                for (MenuModeView menuModeView : menuModeViews) {
                    menuModeView.toggleViewContainer.setVisibility(clickViewMenuMode == menuModeView?View.VISIBLE:View.GONE);
                }
                setSwitchAnim(clickViewMenuMode,menuViewContainerVisibility);
                if (onKeyboardMenuListener != null){
                    onKeyboardMenuListener.onShowMenuLayout(clickViewMenuMode.toggleViewContainer);
                }
            }else {
                showKeyboardAnim();
            }
        }
    }



    private void switchMenuIn(MenuModeView clickViewMenuMode){
        recordLastVisibleView();
        for (MenuModeView menuModeView : menuModeViews) {
            menuModeView.toggleViewContainer.setVisibility(clickViewMenuMode == menuModeView?View.VISIBLE:View.GONE);
        }
        setSwitchAnim(clickViewMenuMode,menuViewContainer.getVisibility());
    }

    private void setSwitchAnim(MenuModeView clickViewMenuMode, int menuViewContainerVisibility){
        if (lastVisibleView == null || !useSwitchAnim){
            return;
        }
        final ViewHeight viewHeight = new ViewHeight(menuViewContainer);
        if (menuViewContainerVisibility == View.GONE){
            viewHeight.setViewHeight(0);
        }
        clickViewMenuMode.toggleViewContainer.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                stopSwitchAnim();
                clickViewMenuMode.toggleViewContainer.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                menuViewContainer.setVisibility(View.VISIBLE);
                int startHeight = menuViewContainerVisibility == View.GONE?0: menuViewContainer.getHeight();
                int marginVertical = 0;
                if (menuViewContainer instanceof FrameLayout){
                    FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) clickViewMenuMode.toggleViewContainer.getLayoutParams();
                    marginVertical = layoutParams.topMargin+layoutParams.bottomMargin;
                }else if (menuViewContainer instanceof RelativeLayout){
                    RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) clickViewMenuMode.toggleViewContainer.getLayoutParams();
                    marginVertical = layoutParams.topMargin+layoutParams.bottomMargin;
                }else if (menuViewContainer instanceof LinearLayout){
                    LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) clickViewMenuMode.toggleViewContainer.getLayoutParams();
                    marginVertical = layoutParams.topMargin+layoutParams.bottomMargin;
                }
                clickViewMenuMode.toggleViewContainer.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
                int endHeight = clickViewMenuMode.toggleViewContainer.getMeasuredHeight()+menuViewContainer.getPaddingTop()+menuViewContainer.getPaddingBottom()+marginVertical;
                int distance = Math.abs(startHeight - endHeight);
                int duration = distance/SWITCH_ANIM_SPEED;
                if (duration<200){
                    duration = 200;
                }
                switchAnim = ObjectAnimator.ofInt(viewHeight,"viewHeight",startHeight,endHeight);
                switchAnim.setDuration(duration);
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
