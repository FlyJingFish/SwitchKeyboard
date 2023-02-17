package com.flyjingfish.switchkeyboard;

import android.os.Bundle;
import android.view.KeyEvent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.flyjingfish.switchkeyboard.databinding.ActivitySecondBinding;
import com.flyjingfish.switchkeyboardlib.SwitchKeyboardUtil;
import com.flyjingfish.switchkeyboardlib.MenuModeView;

import java.util.ArrayList;
import java.util.List;

public class Example1Activity extends AppCompatActivity {
    private SwitchKeyboardUtil baseSwitchKeyboardUtil;
    private ActivitySecondBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseSwitchKeyboardUtil = new SwitchKeyboardUtil(this);
        baseSwitchKeyboardUtil.checkSoftMode();
        binding = ActivitySecondBinding.inflate(getLayoutInflater());
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