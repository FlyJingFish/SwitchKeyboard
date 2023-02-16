package com.flyjingfish.switchkeyboard;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.flyjingfish.switchkeyboard.databinding.ActivitySecond3Binding;
import com.flyjingfish.switchkeyboardlib.BaseSwitchKeyboardUtil;
import com.flyjingfish.switchkeyboardlib.Example2SwitchKeyboardUtil;

import java.util.ArrayList;
import java.util.List;

public class SecondActivity3 extends AppCompatActivity {
    private Example2SwitchKeyboardUtil baseChatKeyboardUtil;
    private ActivitySecond3Binding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        baseChatKeyboardUtil = new Example2SwitchKeyboardUtil(this,true);
        baseChatKeyboardUtil.checkSoftMode();
        super.onCreate(savedInstanceState);
        binding = ActivitySecond3Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        baseChatKeyboardUtil.attachLifecycle(this);
        baseChatKeyboardUtil.setBaseViews(binding.etContent, binding.tvAudio, binding.tvAudioTouch, binding.llMenu);
        baseChatKeyboardUtil.setMoreViews(binding.tvMore);
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
                binding.llMenuBtn.setVisibility(View.VISIBLE);
                binding.llEmoji.setVisibility(View.GONE);
            }
        });
        List<String> msgList = new ArrayList<>();
        for (int i = 0; i < 60; i++) {
            msgList.add("item="+i);
        }
        MsgAdapter msgAdapter = new MsgAdapter(msgList);
        binding.rv.setAdapter(msgAdapter);
        binding.rv.setLayoutManager(new LinearLayoutManager(this));

        binding.tvFace.setOnClickListener(v -> {
            binding.llMenuBtn.setVisibility(View.GONE);
            binding.llEmoji.setVisibility(View.VISIBLE);
        });
        binding.tvFaceBack.setOnClickListener(v -> {
            binding.llMenuBtn.setVisibility(View.VISIBLE);
            binding.llEmoji.setVisibility(View.GONE);
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