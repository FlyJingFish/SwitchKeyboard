package com.flyjingfish.switchkeyboard;

import android.os.Bundle;
import android.view.KeyEvent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.flyjingfish.switchkeyboard.databinding.ActivitySecondBinding;
import com.flyjingfish.switchkeyboardlib.BaseSwitchKeyboardUtil;
import com.flyjingfish.switchkeyboardlib.Example1SwitchKeyboardUtil;

import java.util.ArrayList;
import java.util.List;

public class SecondActivity2 extends AppCompatActivity {
    private Example1SwitchKeyboardUtil baseChatKeyboardUtil;
    private ActivitySecondBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseChatKeyboardUtil = new Example1SwitchKeyboardUtil(this,false);
        baseChatKeyboardUtil.checkSoftMode();
        binding = ActivitySecondBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        baseChatKeyboardUtil.attachLifecycle(this);
        baseChatKeyboardUtil.setBaseViews(binding.etContent, binding.tvAudio, binding.tvAudioTouch, binding.llMenu);
        baseChatKeyboardUtil.setMoreViews(binding.tvFace, binding.tvMore, binding.llMenuBtn, binding.llEmoji);
        List<String> msgList = new ArrayList<>();
        for (int i = 0; i < 60; i++) {
            msgList.add("item="+i);
        }
        MsgAdapter msgAdapter = new MsgAdapter(msgList);
        binding.rv.setAdapter(msgAdapter);
        binding.rv.setLayoutManager(new LinearLayoutManager(this));

        baseChatKeyboardUtil.setOnKeyboardMenuListener(new BaseSwitchKeyboardUtil.OnKeyboardMenuListener() {
            @Override
            public void onScrollToBottom() {
                scrollToBottom();
            }

            @Override
            public void onKeyboardHide(int keyboardHeight) {

            }

            @Override
            public void onKeyboardShow(int keyboardHeight) {

            }

            @Override
            public void onCallShowKeyboard() {

            }

            @Override
            public void onCallHideKeyboard() {

            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && baseChatKeyboardUtil.onKeyDown(keyCode, event)){
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void scrollToBottom() {
//        if (!binding.rv.canScrollVertically(1)){
        binding.rv.scrollToPosition(binding.rv.getAdapter().getItemCount() - 1);
//        }
    }
}