package com.flyjingfish.switchkeyboardlib;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;

public class SystemKeyboardUtils {
    private View rootView;//activity的根视图
    private int rootViewVisibleHeight;//纪录根视图的显示高度
    private OnKeyBoardListener onKeyBoardListener;
    private boolean isRequestFocus = true;

    public SystemKeyboardUtils(Activity activity,boolean isDialogFragment) {
        //获取activity的根视图
        rootView = activity.getWindow().getDecorView();
        //获取当前根视图在屏幕上显示的大小
        if (isDialogFragment){
            Rect r = new Rect();
            rootView.getWindowVisibleDisplayFrame(r);
            int visibleHeight = r.height();
            if (visibleHeight > 0){
                rootViewVisibleHeight = visibleHeight;
            }
        }
        //监听视图树中全局布局发生改变或者视图树中的某个视图的可视状态发生改变
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(onGlobalLayoutListener);
    }

    private ViewTreeObserver.OnGlobalLayoutListener onGlobalLayoutListener = () -> {
        if (isRequestFocus){
            rootView.requestFocus();
        }
        //获取当前根视图在屏幕上显示的大小
        Rect r = new Rect();
        rootView.getWindowVisibleDisplayFrame(r);
        int visibleHeight = r.height();
        if (rootViewVisibleHeight == 0) {
            rootViewVisibleHeight = visibleHeight;
            return;
        }

        //根视图显示高度没有变化，可以看作软键盘显示／隐藏状态没有改变
        if (rootViewVisibleHeight == visibleHeight) {
            return;
        }

        //根视图显示高度变小超过200，可以看作软键盘显示了
        if (rootViewVisibleHeight - visibleHeight > 200) {
            isRequestFocus = false;
            if (onKeyBoardListener != null) {
                onKeyBoardListener.onShow(rootViewVisibleHeight - visibleHeight);
            }
            rootViewVisibleHeight = visibleHeight;
            return;
        }

        //根视图显示高度变大超过200，可以看作软键盘隐藏了
        if (visibleHeight - rootViewVisibleHeight > 200) {
            if (onKeyBoardListener != null) {
                onKeyBoardListener.onHide(visibleHeight - rootViewVisibleHeight);
            }
            rootViewVisibleHeight = visibleHeight;
            return;
        }

    };

    public void onDestroy(){
        rootView.getViewTreeObserver().removeOnGlobalLayoutListener(onGlobalLayoutListener);
    }

    public void setOnKeyBoardListener(OnKeyBoardListener onKeyBoardListener) {
        this.onKeyBoardListener = onKeyBoardListener;
    }

    public interface OnKeyBoardListener {
        void onShow(int height);

        void onHide(int height);
    }

    public static void hideSoftInput(@NonNull final View view) {
        InputMethodManager imm =
                (InputMethodManager)view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm == null) return;
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
    public static void showSoftInput(Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm == null) {
            return;
        }
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }
}
