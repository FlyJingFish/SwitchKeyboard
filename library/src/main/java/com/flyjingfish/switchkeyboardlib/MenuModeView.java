package com.flyjingfish.switchkeyboardlib;

import android.view.View;

public class MenuModeView {
    /**
     * 点击打开菜单的按钮
     */
    public View clickToggleView;
    /**
     * 盛放 clickToggleView 点击打开的菜单布局
     */
    public View toggleViewContainer;
    /**
     * 返回按钮
     */
    public View backView;
    /**
     * clickToggleView 按钮是否在菜单布局中。 false 代表按钮和输入框一栏里，true代表在下方
     */
    public boolean clickToggleViewIsMenuContainer;

    public MenuModeView(View clickToggleView, View toggleViewContainer) {
        this.clickToggleView = clickToggleView;
        this.toggleViewContainer = toggleViewContainer;
    }

    public MenuModeView(View clickToggleView, View toggleViewContainer, boolean clickToggleViewIsMenuContainer) {
        this.clickToggleView = clickToggleView;
        this.toggleViewContainer = toggleViewContainer;
        this.clickToggleViewIsMenuContainer = clickToggleViewIsMenuContainer;
    }

    public MenuModeView(View clickToggleView, View toggleViewContainer, View backView) {
        this.clickToggleView = clickToggleView;
        this.toggleViewContainer = toggleViewContainer;
        this.backView = backView;
    }

    public MenuModeView(View clickToggleView, View toggleViewContainer, View backView, boolean clickToggleViewIsMenuContainer) {
        this.clickToggleView = clickToggleView;
        this.toggleViewContainer = toggleViewContainer;
        this.backView = backView;
        this.clickToggleViewIsMenuContainer = clickToggleViewIsMenuContainer;
    }
}
