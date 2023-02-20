package com.flyjingfish.switchkeyboard;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.flyjingfish.switchkeyboard.databinding.ActivityExample1Binding;
import com.flyjingfish.switchkeyboardlib.MenuModeView;
import com.flyjingfish.switchkeyboardlib.SwitchKeyboardUtil;

import java.util.ArrayList;
import java.util.List;

public class Example1Activity extends AppCompatActivity {
    private SwitchKeyboardUtil baseSwitchKeyboardUtil;
    private ActivityExample1Binding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseSwitchKeyboardUtil = new SwitchKeyboardUtil(this);
        baseSwitchKeyboardUtil.checkSoftMode();
        binding = ActivityExample1Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        baseSwitchKeyboardUtil.setMenuViewHeightEqualKeyboard(true);
        baseSwitchKeyboardUtil.attachLifecycle(this);
        baseSwitchKeyboardUtil.setInputEditText(binding.etContent);
        baseSwitchKeyboardUtil.setAudioBtn(binding.tvAudio);
        baseSwitchKeyboardUtil.setAudioTouchVIew(binding.tvAudioTouch);
        baseSwitchKeyboardUtil.setMenuViewContainer(binding.llMenu);
        baseSwitchKeyboardUtil.setToggleMenuViews(new MenuModeView(binding.tvMore,binding.llMenuBtn),
                new MenuModeView(binding.tvFace,binding.llEmoji,binding.tvFaceBack),
                new MenuModeView(binding.tvWord,binding.llWord));
        baseSwitchKeyboardUtil.setOnKeyboardMenuListener(new SwitchKeyboardUtil.OnKeyboardMenuListener() {
            @Override
            public void onScrollToBottom() {
                scrollToBottom();
            }

            @Override
            public void onKeyboardHide(int keyboardHeight) {

            }

            @Override
            public void onKeyboardShow(int keyboardHeight) {
                binding.tvAudio.setImageResource(R.drawable.ic_audio);
            }

            @Override
            public void onCallShowKeyboard() {

            }

            @Override
            public void onCallHideKeyboard() {
            }

            @Override
            public void onShowMenuLayout(View layoutView) {
                binding.tvAudio.setImageResource(layoutView == binding.tvAudioTouch?R.drawable.ic_keyboard:R.drawable.ic_audio);
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
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}