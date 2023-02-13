package com.flyjingfish.switchkeyboard;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.flyjingfish.switchkeyboard.databinding.ActivitySecondBinding;
import com.flyjingfish.switchkeyboardlib.BaseSwitchKeyboardUtil;

import java.util.ArrayList;
import java.util.List;

public class SecondActivity extends AppCompatActivity {
    private MenuMode menuMode = MenuMode.IDLE;
    private BaseSwitchKeyboardUtil baseSwitchKeyboardUtil;
    private ActivitySecondBinding binding;

    private enum MenuMode{
        IDLE,FACE,MENU_BTN;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySecondBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        baseSwitchKeyboardUtil = new BaseSwitchKeyboardUtil(this,true);
        baseSwitchKeyboardUtil.attachLifecycle(this);
        baseSwitchKeyboardUtil.setBaseViews(binding.etContent,binding.tvAudio, binding.tvAudioTouch, binding.llMenu);
        baseSwitchKeyboardUtil.setOnKeyboardMenuListener(new BaseSwitchKeyboardUtil.OnKeyboardMenuListener() {
            @Override
            public void onScrollToBottom() {
                scrollToBottom();
            }

            @Override
            public void onKeyboardHide(int keyboardHeight) {
                if (menuMode == MenuMode.IDLE){
                    binding.llMenu.setVisibility(View.GONE);
                }
            }

            @Override
            public void onKeyboardShow(int keyboardHeight) {

            }

            @Override
            public void onCallShowKeyboard() {
                menuMode = MenuMode.IDLE;
            }

            @Override
            public void onCallHideKeyboard() {

            }
        });

        View.OnClickListener moreClick = v -> {
            binding.llMenuBtn.setVisibility(View.VISIBLE);
            binding.llEmoji.setVisibility(View.GONE);
            if (menuMode == MenuMode.IDLE){
                menuMode = MenuMode.MENU_BTN;
                baseSwitchKeyboardUtil.toggleMoreView();
            }else if (menuMode == MenuMode.FACE){
                menuMode = MenuMode.MENU_BTN;
            }else {
                baseSwitchKeyboardUtil.toggleMoreView();
            }
        };
        View.OnClickListener faceClick = v -> {
            binding.llMenuBtn.setVisibility(View.GONE);
            binding.llEmoji.setVisibility(View.VISIBLE);
            if (menuMode == MenuMode.IDLE){
                menuMode = MenuMode.FACE;
                baseSwitchKeyboardUtil.toggleMoreView();
            }else if (menuMode == MenuMode.MENU_BTN){
                menuMode = MenuMode.FACE;
            }else {
                baseSwitchKeyboardUtil.toggleMoreView();
            }
        };
        binding.tvMore.setOnClickListener(moreClick);
        binding.tvFace.setOnClickListener(faceClick);
        binding.etContent.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                menuMode = MenuMode.IDLE;
//                binding.llMenu.setVisibility(View.INVISIBLE);
                return false;
            }
        });

        List<String> msgList = new ArrayList<>();
        for (int i = 0; i < 60; i++) {
            msgList.add("item="+i);
        }
        MsgAdapter msgAdapter = new MsgAdapter(msgList);
        binding.rv.setAdapter(msgAdapter);
        binding.rv.setLayoutManager(new LinearLayoutManager(this));

    }

    private void scrollToBottom() {
//        if (!binding.rv.canScrollVertically(1)){
            binding.rv.scrollToPosition(binding.rv.getAdapter().getItemCount() - 1);
//        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && baseSwitchKeyboardUtil.onKeyDown(keyCode, event)){
            menuMode = MenuMode.IDLE;
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}