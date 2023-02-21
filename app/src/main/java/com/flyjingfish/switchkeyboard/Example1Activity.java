package com.flyjingfish.switchkeyboard;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.flyjingfish.switchkeyboard.databinding.ActivityExample1Binding;
import com.flyjingfish.switchkeyboardlib.MenuModeView;
import com.flyjingfish.switchkeyboardlib.SwitchKeyboardUtil;

import java.util.ArrayList;
import java.util.List;

public class Example1Activity extends AppCompatActivity {
    private SwitchKeyboardUtil switchKeyboardUtil;
    private ActivityExample1Binding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        switchKeyboardUtil = new SwitchKeyboardUtil(this);
        switchKeyboardUtil.checkSoftMode();
        binding = ActivityExample1Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        switchKeyboardUtil.setMenuViewHeightEqualKeyboard(false);
        switchKeyboardUtil.setUseSwitchAnim(true);
        switchKeyboardUtil.attachLifecycle(this);
        switchKeyboardUtil.setInputEditText(binding.etContent);
        switchKeyboardUtil.setAudioBtn(binding.tvAudio);
        switchKeyboardUtil.setAudioTouchVIew(binding.tvAudioTouch);
        switchKeyboardUtil.setMenuViewContainer(binding.llMenu);
        switchKeyboardUtil.setToggleMenuViews(new MenuModeView(binding.tvMore,binding.llMenuBtn),
                new MenuModeView(binding.ivFace,binding.llEmoji),
                new MenuModeView(binding.tvGift,binding.llGift),
                new MenuModeView(binding.tvWord,binding.llWord));
        switchKeyboardUtil.setOnKeyboardMenuListener(new SwitchKeyboardUtil.OnKeyboardMenuListener() {
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
                binding.ivFace.setImageResource(R.drawable.ic_face);
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
                binding.ivFace.setImageResource(layoutView == binding.llEmoji?R.drawable.ic_keyboard:R.drawable.ic_face);
            }
        });
        switchKeyboardUtil.setEtContentOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });

        binding.tvVideo.setOnClickListener(v -> Toast.makeText(this,"去视频通话",Toast.LENGTH_SHORT).show());
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
        if (switchKeyboardUtil.onKeyDown(keyCode, event)){
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}